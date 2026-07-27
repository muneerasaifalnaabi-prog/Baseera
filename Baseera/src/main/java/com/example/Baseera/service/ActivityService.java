package com.example.Baseera.service;

import com.example.Baseera.dto.request.ActivityRequestDTO;
import com.example.Baseera.dto.response.ActivityResponseDTO;
import com.example.Baseera.entity.Activity;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.ActivityRepository;

import java.util.List;

public class ActivityService {
        private ActivityRepository activityRepository;
//        private final AssessmentRepository assessmentRepository;
//        private final ChildService childService; // for ownership check + calculateAgeInMonths()

        // General catalog browse — no child context, optional condition filter
        public List<ActivityResponseDTO> browse(String targetCondition) {
            List<Activity> activities = (targetCondition != null && !targetCondition.isBlank())
                    ? activityRepository.findByTargetConditionAndIsActiveTrue(targetCondition)
                    : activityRepository.findByIsActiveTrue();

            return ActivityResponseDTO.fromEntity(activities);
        }

        // Matched to a specific child — age computed from dateOfBirth, condition pulled from latest assessment
//        public List<ActivityResponseDTO> browseForChild(Long childId, Long parentId) {
//            Child child = childService.getChildOwnedByParent(childId, parentId);
//
//            int ageMonths = childService.calculateAgeInMonths(child.getDateOfBirth());
//            String condition = getLatestSuggestedCondition(childId);
//
//            List<Activity> activities = activityRepository
//                    .findByTargetConditionAndMinAgeLessThanEqualAndMaxAgeGreaterThanEqualAndIsActiveTrue(
//                            condition, ageMonths, ageMonths);
//
//            return ActivityResponseDTO.fromEntity(activities);
//        }
//
//        private String getLatestSuggestedCondition(Long childId) {
//            List<Assessment> history = assessmentRepository.findByChildIdAndIsActiveTrueOrderByCreatedAtDesc(childId);
//
//            if (history.isEmpty()) {
//                throw new ResourceNotFoundException(
//                        "No assessment found yet for child " + childId + " — cannot determine matching activities");
//            }
//
//            return history.get(0).getSuggestedCondition();
//        }

        // ---- Admin CRUD ----

        public ActivityResponseDTO createActivity(ActivityRequestDTO dto) {
            Activity activity = dto.toEntity();
            Activity saved = activityRepository.save(activity);
            return ActivityResponseDTO.fromEntity(saved);
        }

        public ActivityResponseDTO updateActivity(Long activityId, ActivityRequestDTO dto) {
            Activity activity = activityRepository.findById(activityId)
                    .filter(Activity::getIsActive)
                    .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

            dto.applyTo(activity);
            Activity saved = activityRepository.save(activity);
            return ActivityResponseDTO.fromEntity(saved);
        }

        public void deleteActivity(Long activityId) {
            Activity activity = activityRepository.findById(activityId)
                    .filter(Activity::getIsActive)
                    .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

            activity.setIsActive(false); // soft delete
            activityRepository.save(activity);
        }
    }

