package com.koinmarket.app.repositories;
import com.koinmarket.app.entities.Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<Metadata,Integer> {
}
