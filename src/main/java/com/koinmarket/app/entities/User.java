package com.koinmarket.app.entities;

import com.koinmarket.app.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "KoinMarket_User") //user is a reserved keyword in PostgreSQL
public class User implements UserDetails {

    @Id
    @Getter @Setter
    @GeneratedValue
    private Integer id;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String emailAddress;

    @Getter @Setter
    private String password;

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {};

    public User(String userName, String emailAddress, String password, Role role) {
        this.setUsername(userName);
        this.setEmailAddress(emailAddress);
        this.setPassword(password);
        this.setRole(role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


