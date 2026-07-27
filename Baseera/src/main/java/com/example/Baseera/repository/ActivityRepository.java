package com.example.Baseera.repository;

import com.example.Baseera.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByIsActiveTrue();

    List<Activity> findByTargetConditionAndIsActiveTrue(String targetCondition);

    List<Activity> findByTargetConditionAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualAndIsActiveTrue(
            String targetCondition, int ageMonths1, int ageMonths2);
}
