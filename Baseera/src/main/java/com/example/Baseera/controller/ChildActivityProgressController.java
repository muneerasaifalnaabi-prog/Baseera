package com.example.Baseera.controller;

import com.example.Baseera.dto.request.UpdateProgressRequestDTO;
import com.example.Baseera.dto.response.ChildActivityProgressResponseDTO;
import com.example.Baseera.security.SecurityUtils;
import com.example.Baseera.service.ChildActivityProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children/{childId}/activities")
public class ChildActivityProgressController {

    @Autowired
    private ChildActivityProgressService progressService;

    @Autowired
    private SecurityUtils securityUtils;

    @PostMapping("/{activityId}")
    public ResponseEntity<ChildActivityProgressResponseDTO> addActivity(
            @PathVariable Long childId, @PathVariable Long activityId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(progressService.addActivityToChild(childId, activityId, parentId));
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<ChildActivityProgressResponseDTO> updateProgress(
            @PathVariable Long childId, @PathVariable Long activityId,
            @Valid @RequestBody UpdateProgressRequestDTO dto) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(progressService.updateProgress(childId, activityId, dto, parentId));
    }

    @GetMapping
    public ResponseEntity<List<ChildActivityProgressResponseDTO>> getActivitiesForChild(
            @PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(progressService.getActivitiesForChild(childId, parentId));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<String> removeActivity(
            @PathVariable Long childId, @PathVariable Long activityId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(progressService.removeActivityFromChild(childId, activityId, parentId));
    }
}
