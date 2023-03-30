package com.koinmarket.app.repositories;

import com.koinmarket.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAddress(String email);

    Boolean existsByUsername(String username);

    Boolean existsByEmailAddress(String email);
}
