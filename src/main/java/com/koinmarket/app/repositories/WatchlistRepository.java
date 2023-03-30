package com.koinmarket.app.repositories;

import com.koinmarket.app.entities.JwtToken;
import com.koinmarket.app.entities.User;
import com.koinmarket.app.entities.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Integer> {
    List<Watchlist> findAllByUser(User user);

    @Override
    void deleteById(Integer integer);
}
