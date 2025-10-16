package com.community_centers.service.repository;

import com.community_centers.service.entity.InformationCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InformationCardRepository extends JpaRepository<InformationCard, Integer> {

    List<InformationCard> findByChitalishteId(Integer chitalishteId);

    List<InformationCard> findByChitalishteStatus(String status);

    List<InformationCard> findByYear(Integer year);

    List<InformationCard> findByTotalMembersCountGreaterThan(Integer count);
}