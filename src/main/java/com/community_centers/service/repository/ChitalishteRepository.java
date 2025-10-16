package com.community_centers.service.repository;

import com.community_centers.service.entity.Chitalishte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChitalishteRepository extends JpaRepository<Chitalishte, Integer> {

    Page<Chitalishte> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Chitalishte> findByRegionContainingIgnoreCase(String region, Pageable pageable);

    Page<Chitalishte> findByMunicipalityContainingIgnoreCase(String municipality, Pageable pageable);

    Page<Chitalishte> findByTownContainingIgnoreCase(String town, Pageable pageable);

    @Query("SELECT c FROM Chitalishte c WHERE " +
            "(:name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:region = '' OR LOWER(c.region) LIKE LOWER(CONCAT('%', :region, '%'))) AND " +
            "(:municipality = '' OR LOWER(c.municipality) LIKE LOWER(CONCAT('%', :municipality, '%'))) AND " +
            "(:town = '' OR LOWER(c.town) LIKE LOWER(CONCAT('%', :town, '%')))")
    Page<Chitalishte> findBySearchCriteria(@Param("name") String name,
                                           @Param("region") String region,
                                           @Param("municipality") String municipality,
                                           @Param("town") String town,
                                           Pageable pageable);

    Optional<Chitalishte> findByBulstat(String bulstat);

    Optional<Chitalishte> findByRegistrationNumber(Integer registrationNumber);

    Long countByRegion(String region);

    Long countByMunicipality(String municipality);

    Long countByStatus(String status);

    List<Chitalishte> findByStatus(String status);
}