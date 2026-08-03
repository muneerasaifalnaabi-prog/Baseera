package com.example.Baseera.service;

import com.example.Baseera.dto.request.UpdateProgressRequestDTO;
import com.example.Baseera.dto.response.ChildActivityProgressResponseDTO;
import com.example.Baseera.entity.Activity;
import com.example.Baseera.entity.Child;
import com.example.Baseera.entity.ChildActivityProgress;
import com.example.Baseera.enums.ProgressStatus;
import com.example.Baseera.exception.DuplicateResourceException;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.ActivityRepository;
import com.example.Baseera.repository.ChildActivityProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChildActivityProgressService {

    @Autowired
    private ChildActivityProgressRepository progressRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ChildService childService;

    //****========
    // parent: add one specific activity to one specific child.
    // Ownership checked first, then blocks adding the same activity twice.
    //==========****
    public ChildActivityProgressResponseDTO addActivityToChild(Long childId, Long activityId, Long parentId) {
        Child child = childService.getChildOwnedByParent(childId, parentId);

        Activity activity = activityRepository.findActivityById(activityId)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + activityId));

        progressRepository.findByChildIdAndActivityId(childId, activityId)
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("This activity has already been added for this child");
                });

        ChildActivityProgress progress = new ChildActivityProgress();
        progress.setChild(child);
        progress.setActivity(activity);
        progress.setStatus(ProgressStatus.NOT_STARTED);

        ChildActivityProgress saved = progressRepository.save(progress);
        return ChildActivityProgressResponseDTO.fromEntity(saved);
    }

    //****========
    // parent: update the status/notes of an already-added activity
    //==========****
    public ChildActivityProgressResponseDTO updateProgress(
            Long childId, Long activityId, UpdateProgressRequestDTO dto, Long parentId) {
        childService.getChildOwnedByParent(childId, parentId);

        ChildActivityProgress progress = progressRepository.findByChildIdAndActivityId(childId, activityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This activity hasn't been added for this child yet"));

        progress.setStatus(dto.getStatus());
        if (dto.getNotes() != null) {
            progress.setNotes(dto.getNotes());
        }

        ChildActivityProgress updated = progressRepository.save(progress);
        return ChildActivityProgressResponseDTO.fromEntity(updated);
    }

    //****========
    // parent: list every activity added for this child, with progress
    //==========****
    public List<ChildActivityProgressResponseDTO> getActivitiesForChild(Long childId, Long parentId) {
        childService.getChildOwnedByParent(childId, parentId);
        List<ChildActivityProgress> progressList = progressRepository.findAllByChildId(childId);
        return ChildActivityProgressResponseDTO.fromEntity(progressList);
    }

    //****========
    // parent: remove an activity from a child (soft delete)
    //==========****
    public String removeActivityFromChild(Long childId, Long activityId, Long parentId) {
        childService.getChildOwnedByParent(childId, parentId);

        ChildActivityProgress progress = progressRepository.findByChildIdAndActivityId(childId, activityId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "This activity hasn't been added for this child"));

        progress.setIsActive(false);
        progressRepository.save(progress);
        return "REMOVED";
    }
}
