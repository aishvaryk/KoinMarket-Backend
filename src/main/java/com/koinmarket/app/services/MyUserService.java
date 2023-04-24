package com.koinmarket.app.services;

import com.koinmarket.app.configurations.application_properties.AdminUserConfiguration;
import com.koinmarket.app.configurations.application_properties.JwtConfiguration;
import com.koinmarket.app.entities.JwtToken;
import com.koinmarket.app.entities.User;
import com.koinmarket.app.enums.Role;
import com.koinmarket.app.exceptions.authentication.TokenNotGenerated;
import com.koinmarket.app.exceptions.user.UserAlreadyExistException;
import com.koinmarket.app.exceptions.user.UserNotFoundException;
import com.koinmarket.app.repositories.JwtTokenRepository;
import com.koinmarket.app.repositories.UserRepository;
import com.koinmarket.app.requestBodies.RegisterRequestBody;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MyUserService {

    @Autowired
    private JwtConfiguration jwtConfiguration;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminUserConfiguration adminUserConfiguration;

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        if (!userRepository.existsByUsername(adminUserConfiguration.getUsername())) {
            User user = new User(adminUserConfiguration.getUsername(), "",  passwordEncoder.encode(adminUserConfiguration.getPassword()), Role.ADMIN);
            userRepository.save(user);
        }
    }

    @Transactional
    public JwtToken register(RegisterRequestBody registerRequestBody) {
        String username = registerRequestBody.getUsername();
        String email = registerRequestBody.getEmailAddress();
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException("Username already in use");
        }
        else if (userRepository.existsByEmailAddress(email)) {
            throw new UserAlreadyExistException("Email Address already in use");
        }
        User user = new User(username, email, passwordEncoder.encode(registerRequestBody.getPassword()), Role.USER);
        User savedUser = userRepository.save(user);
        String jwtTokenString = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtTokenString);
        JwtToken jwtToken= jwtTokenRepository.findByToken(jwtTokenString).orElseThrow(()-> {return new TokenNotGenerated();});
        return jwtToken;
    }

    public JwtToken authenticate(String username, String password) {
        User user = userRepository.findByUsername(username).orElse(null);
        if(user==null)    {
            userRepository.findByEmailAddress(username).orElseThrow(()->new UserNotFoundException());
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        password
                )
        );
        String jwtTokenString = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtTokenString);
        JwtToken jwtToken= jwtTokenRepository.findByToken(jwtTokenString).orElseThrow(()-> {return new TokenNotGenerated();});
        return jwtToken;
    }

    @Transactional
    private void saveUserToken(User user, String jwtToken) {
        JwtToken token = new JwtToken(jwtToken, false, user, LocalDateTime.now(), LocalDateTime.now().plusDays(TimeUnit.MILLISECONDS.toDays(jwtConfiguration.getExpiration())));
        jwtTokenRepository.save(token);
    }

    @Transactional
    private void revokeAllUserTokens(User user) {
        List<JwtToken> validUserTokens = jwtTokenRepository.findAllByUser(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }

    @Transactional
    public User currentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
