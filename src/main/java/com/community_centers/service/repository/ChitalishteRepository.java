package com.community_centers.service.repository;

import com.community_centers.service.entity.Chitalishte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChitalishteRepository extends JpaRepository<Chitalishte, Long> {

    Optional<Chitalishte> findByBulstat(String bulstat);

    Optional<Chitalishte> findByChitalishtaUrl(String chitalishtaUrl);
}
