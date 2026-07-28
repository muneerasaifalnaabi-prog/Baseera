package com.example.Baseera.repository;

import com.example.Baseera.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import com.example.Baseera.enums.ConditionType;
public interface CenterRepository extends JpaRepository<Center, Long> {

    @Query("SELECT c FROM Center c WHERE c.isActive = true AND c.id = :id")
    Optional<Center> findCenterById(@Param("id") Long id);

    @Query("SELECT c FROM Center c WHERE c.isActive = true")
    List<Center> findAllActiveCenters();

    // Optional filters — pass null to skip a filter, matches ActivityRepository.searchActivities style.
    @Query("SELECT c FROM Center c WHERE c.isActive = true " +
            "AND (:city IS NULL OR LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
            "AND (:specialty IS NULL OR c.specialty = :specialty OR c.specialty = 'BOTH')")
    List<Center> searchCenters(@Param("city") String city, @Param("specialty") ConditionType specialty);

    // Used by CenterService.recommendForChild — BOTH satisfies any condition.
    @Query("SELECT c FROM Center c WHERE c.isActive = true " +
            "AND (c.specialty = :condition OR c.specialty = 'BOTH')")
    List<Center> findBySpecialtyMatching(@Param("condition") ConditionType condition);
}
