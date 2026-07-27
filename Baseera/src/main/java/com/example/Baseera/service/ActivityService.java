package com.example.Baseera.service;


import com.example.Baseera.dto.request.ActivityRequestDTO;
import com.example.Baseera.dto.response.ActivityResponseDTO;
import com.example.Baseera.entity.Activity;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    //****========
    // admin: create a new activity in the catalog
    //==========****
    public ActivityResponseDTO createActivity(ActivityRequestDTO dto) {
        Activity activity = dto.toEntity();
        Activity saved = activityRepository.save(activity);
        return ActivityResponseDTO.fromEntity(saved);
    }

    //****========
    // admin: update an existing activity's fields
    //==========****
    public ActivityResponseDTO updateActivity(Long activityId, ActivityRequestDTO dto) {
        Activity activity = activityRepository.findActivityById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

        dto.applyTo(activity);
        Activity updated = activityRepository.save(activity);
        return ActivityResponseDTO.fromEntity(updated);
    }

    //****========
    // admin: deactivate the activity (soft delete, never removed from the database)
    //==========****
    public String deleteActivity(Long activityId) {
        Activity activity = activityRepository.findActivityById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

        if (activity.getIsActive()) {
            activity.setIsActive(false);
            activityRepository.save(activity);
            return "DELETED";
        } else {
            return "NOT FOUND";
        }
    }

    //****========
    // parent/admin: get one activity by id
    //==========****
    public ActivityResponseDTO getActivityById(Long activityId) {
        Activity activity = activityRepository.findActivityById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

        return ActivityResponseDTO.fromEntity(activity);
    }

    //****========
    // parent/admin: get the full active catalog, no filters
    //==========****
    public List<ActivityResponseDTO> getAllActivities() {
        List<Activity> activities = activityRepository.findAllActiveActivities();
        return ActivityResponseDTO.fromEntity(activities);
    }

    //****========
    // parent/admin: filter by name, condition (ASD/ADHD), and/or age — all optional, pass null to skip
    //==========****
    public List<ActivityResponseDTO> searchActivities(String name, String targetCondition, Integer age) {
        List<Activity> activities = activityRepository.searchActivities(name, targetCondition, age);
        return ActivityResponseDTO.fromEntity(activities);
    }
}
