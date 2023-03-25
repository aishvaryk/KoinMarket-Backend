package com.koinmarket.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue
    public Integer id;

    @Column(unique = true)
    public String token;

    public boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    public JwtToken(String jwtToken, boolean expired, User user) {
        this.setToken(jwtToken);
        this.setExpired(expired);
        this.setUser(user);
    }
}
