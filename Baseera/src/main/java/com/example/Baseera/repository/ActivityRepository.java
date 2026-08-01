package com.example.Baseera.repository;


import com.example.Baseera.entity.Activity;
import com.example.Baseera.enums.ConditionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    @Query("SELECT a FROM Activity a WHERE a.isActive = true AND a.id = :id")
    Optional<Activity> findActivityById(@Param("id") Long id);

    @Query("SELECT a FROM Activity a WHERE a.isActive = true")
    List<Activity> findAllActiveActivities();

    // Flexible filter — name, condition, and age are all optional (pass null to skip that filter)
    @Query("SELECT a FROM Activity a WHERE a.isActive = true " +
            "AND (:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:targetCondition IS NULL OR a.targetCondition = :targetCondition) " +
            "AND (:age IS NULL OR (a.minAge <= :age AND a.maxAge >= :age))")
    List<Activity> searchActivities(@Param("name") String name, @Param("targetCondition") ConditionType targetCondition, @Param("age") Integer age);
}
