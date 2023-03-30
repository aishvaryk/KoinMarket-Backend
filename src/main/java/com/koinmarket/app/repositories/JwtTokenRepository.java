package com.koinmarket.app.repositories;

import com.koinmarket.app.entities.JwtToken;
import com.koinmarket.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {

    List<JwtToken> findAllByUser(User user);

    Optional<JwtToken> findByToken(String token);

}
