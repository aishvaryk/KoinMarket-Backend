package com.koinmarket.app.services;

import com.koinmarket.app.entities.JwtToken;
import com.koinmarket.app.entities.User;
import com.koinmarket.app.enums.Role;
import com.koinmarket.app.exceptions.authentication.TokenNotGenerated;
import com.koinmarket.app.repositories.JwtTokenRepository;
import com.koinmarket.app.repositories.UserRepository;
import com.koinmarket.app.requestBodies.RegisterRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtToken register(RegisterRequestBody registerRequestBody) {
        var user = new User(registerRequestBody.getEmailAddress(), registerRequestBody.getUsername(), passwordEncoder.encode(registerRequestBody.getPassword()), Role.USER);
        var savedUser = repository.save(user);
        var jwtTokenString = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtTokenString);
        JwtToken jwtToken= jwtTokenRepository.findByToken(jwtTokenString).orElseThrow(()-> {return new TokenNotGenerated();});
        return jwtToken;
    }

    public JwtToken authenticate(String username, String password) {
        User user = repository.findByUsername(username).orElse(null);
        if(user==null)    {
            repository.findByEmailAddress(username).orElseThrow();
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

    private void saveUserToken(User user, String jwtToken) {
        JwtToken token = new JwtToken(jwtToken, false, user);
        jwtTokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<JwtToken> validUserTokens = jwtTokenRepository.findAllByUser(user);
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
        });
        jwtTokenRepository.saveAll(validUserTokens);
    }
}
