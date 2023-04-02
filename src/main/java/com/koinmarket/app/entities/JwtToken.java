package com.koinmarket.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(unique = true)
    private String token;

    private boolean expired;

    @Column(name = "created_at")
    private LocalDateTime creationTime;

    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public JwtToken(String jwtToken, boolean expired, User user, LocalDateTime creationTime, LocalDateTime expirationTime) {
        this.setToken(jwtToken);
        this.setExpired(expired);
        this.setUser(user);
        this.setCreationTime(creationTime);
        this.setExpirationTime(expirationTime);
    }
}
