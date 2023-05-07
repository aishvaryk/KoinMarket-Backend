package com.koinmarket.app.configurations;

import com.koinmarket.app.configurations.security_exceptions.CustomAccessDeniedHandler;
import com.koinmarket.app.configurations.security_exceptions.CustomAuthenticationEntryPoint;
import com.koinmarket.app.services.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    @Autowired
    private JwtAuthorisationFilter jwtAuthorisationFilter;

    @Autowired
    private LogoutService logoutService;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private CustomAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/login").permitAll()
            .requestMatchers("/list").permitAll()
            .requestMatchers("/metadata").permitAll()
            .requestMatchers("/register").permitAll()
            .requestMatchers("/swagger-ui/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            .and()
            .addFilterBefore(jwtAuthorisationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout().logoutUrl("/logout").addLogoutHandler(logoutService).logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
        ;

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/v3/api-docs/**");
    }

}
