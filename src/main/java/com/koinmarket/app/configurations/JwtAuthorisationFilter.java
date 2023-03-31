package com.koinmarket.app.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.koinmarket.app.entities.User;
import com.koinmarket.app.repositories.JwtTokenRepository;
import com.koinmarket.app.repositories.UserRepository;
import com.koinmarket.app.requestBodies.LoginRequestBody;
import com.koinmarket.app.requestBodies.RegisterRequestBody;
import com.koinmarket.app.services.JwtService;
import com.koinmarket.app.utils.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthorisationFilter extends BasicAuthenticationFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenRepository tokenRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    public JwtAuthorisationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            if (cachedRequest.getRequestURI().equals("/register")) {
                if (checkIfUsernameOrEmailExits(cachedRequest, response)) return;
            } else if (cachedRequest.getRequestURI().equals("/login")) {
                if (checkIfUsernameIncorrect(cachedRequest, response)) return;
            }
            filterChain.doFilter(cachedRequest, response);
            return;
        }
        final String token = authHeader.split(" ")[1].trim();
        final String username = jwtService.extractUsername(token);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(cachedRequest, response);
    }

    private boolean checkIfUsernameIncorrect(HttpServletRequest request, HttpServletResponse response) throws IOException {ObjectMapper mapper = new ObjectMapper();
        LoginRequestBody loginRequestBody = mapper.readValue(request.getInputStream(), LoginRequestBody.class);
        String username = loginRequestBody.getUsername();
        User user = userRepository.findByUsername(username).orElse(null);
        if(user==null) {
            userRepository.findByEmailAddress(username).orElse(null);
            if(user==null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().println("{ \"error\": \"" + "Incorrect Email or username" + "\" }");
                return true;
            }
        }
        return false;
    }

    private boolean checkIfUsernameOrEmailExits( HttpServletRequest request, HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        RegisterRequestBody registerRequestBody = mapper.readValue(request.getInputStream(),RegisterRequestBody.class);
        String username = registerRequestBody.getUsername();
        String email = registerRequestBody.getEmailAddress();
        if (userRepository.existsByUsername(username)) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().println("{ \"error\": \"" + "Username already exists" + "\" }");
            return true;
        }
        else if (userRepository.existsByEmailAddress(email)) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getOutputStream().println("{ \"error\": \"" + "Email already exists" + "\" }");
            return true;
        }
        return false;
    }
}
