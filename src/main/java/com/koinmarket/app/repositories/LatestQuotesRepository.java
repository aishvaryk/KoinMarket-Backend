package com.koinmarket.app.repositories;

import com.koinmarket.app.entities.LatestQuotesUSD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LatestQuotesRepository extends JpaRepository<LatestQuotesUSD, Integer> {
}
