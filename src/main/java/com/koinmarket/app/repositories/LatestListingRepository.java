package com.koinmarket.app.repositories;

import com.koinmarket.app.entities.LatestListings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LatestListingRepository extends JpaRepository<LatestListings, Integer> {
}
