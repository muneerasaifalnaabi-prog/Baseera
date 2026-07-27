package com.example.Baseera.controller;

import com.example.Baseera.dto.request.ActivityRequestDTO;
import com.example.Baseera.dto.response.ActivityResponseDTO;
import com.example.Baseera.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    // admin only — create a new activity in the catalog
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityResponseDTO> createActivity(@Valid @RequestBody ActivityRequestDTO dto) {
        return new ResponseEntity<>(activityService.createActivity(dto), HttpStatus.CREATED);
    }

    // parent + admin — get the full active catalog
    @GetMapping
    public ResponseEntity<List<ActivityResponseDTO>> getAllActivities() {
        return ResponseEntity.ok(activityService.getAllActivities());
    }

    // parent + admin — get one activity by id
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseDTO> getActivityById(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.getActivityById(id));
    }

    // parent + admin — filter by name, condition (ASD/ADHD), and/or age; all optional
    @GetMapping("/search")
    public ResponseEntity<List<ActivityResponseDTO>> searchActivities(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String targetCondition,
            @RequestParam(required = false) Integer age) {
        return ResponseEntity.ok(activityService.searchActivities(name, targetCondition, age));
    }

    // admin only — update an existing activity's fields
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivityResponseDTO> updateActivity(@PathVariable Long id, @Valid @RequestBody ActivityRequestDTO dto) {
        return ResponseEntity.ok(activityService.updateActivity(id, dto));
    }

    // admin only — deactivate the activity (soft delete)
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteActivity(@PathVariable Long id) {
        return ResponseEntity.ok(activityService.deleteActivity(id));
    }
}