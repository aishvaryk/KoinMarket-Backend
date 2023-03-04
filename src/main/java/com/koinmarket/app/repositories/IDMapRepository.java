package com.koinmarket.app.repositories;

import com.koinmarket.app.entities.IDMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IDMapRepository extends JpaRepository<IDMap, Integer> {
}
