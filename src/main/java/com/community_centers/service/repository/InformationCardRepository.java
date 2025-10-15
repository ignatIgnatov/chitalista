package com.community_centers.service.repository;

import com.community_centers.service.entity.InformationCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InformationCardRepository extends JpaRepository<InformationCard, Long> {
    Optional<InformationCard> findByBulstatAndYear(String bulstat, int year);
}
