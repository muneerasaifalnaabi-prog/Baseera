package com.example.Baseera.controller;

import com.example.Baseera.dto.request.ActivityRequestDTO;
import com.example.Baseera.dto.response.ActivityResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.Baseera.service.ActivityService;

import java.util.List;

@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // public/parent: browse the full active catalog, no filters
    @GetMapping("/api/activities")
    public ResponseEntity<List<ActivityResponseDTO>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    // public/parent: filter by name, condition (ASD/ADHD), and/or age — all optional
    @GetMapping("/api/activities/search")
    public ResponseEntity<List<ActivityResponseDTO>> searchActivities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String targetCondition,
            @RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(activityService.searchActivities(name, targetCondition, age));
    }

    // public/parent: get one activity by id
    @GetMapping("/api/activities/{id}")
    public ResponseEntity<ActivityResponseDTO> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    // admin: create a new activity in the catalog
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/activities")
    public ResponseEntity<ActivityResponseDTO> createActivity(@Valid @RequestBody ActivityRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.createActivity(dto));
    }

    // admin: update an existing activity's fields
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/activities/{id}")
    public ResponseEntity<ActivityResponseDTO> updateActivity(
            @PathVariable Long id, @Valid @RequestBody ActivityRequestDTO dto) {
        return ResponseEntity.ok(activityService.updateActivity(id, dto));
    }

    // admin: deactivate the activity (soft delete)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/admin/activities/{id}")
    public ResponseEntity<String> deleteActivity(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.deleteActivity(id));
    }
}
