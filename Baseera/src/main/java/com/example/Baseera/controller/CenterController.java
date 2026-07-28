package com.example.Baseera.controller;

import com.example.Baseera.dto.request.CenterRequestDTO;
import com.example.Baseera.dto.response.CenterResponseDTO;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.security.SecurityUtils;
import com.example.Baseera.service.CenterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CenterController {

    @Autowired
    private CenterService centerService;

    @Autowired
    private SecurityUtils securityUtils;

    // public: browse the center directory, filterable by city and specialty
    @GetMapping("/api/centers")
    public ResponseEntity<List<CenterResponseDTO>> searchCenters(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) ConditionType specialty) {
        return ResponseEntity.ok(centerService.searchCenters(city, specialty));
    }

    // public: get one center by id
    @GetMapping("/api/centers/{id}")
    public ResponseEntity<CenterResponseDTO> getCenterById(@PathVariable Long id) {
        return ResponseEntity.ok(centerService.getCenterById(id));
    }

    // parent: automatic recommendation once the child has a suggestedCondition
    @GetMapping("/api/children/{childId}/centers/recommended")
    public ResponseEntity<List<CenterResponseDTO>> recommendForChild(@PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(centerService.recommendForChild(childId, parentId));
    }

    // admin: create a new center
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/admin/centers")
    public ResponseEntity<CenterResponseDTO> createCenter(@Valid @RequestBody CenterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(centerService.createCenter(dto));
    }

    // admin: update an existing center
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/centers/{id}")
    public ResponseEntity<CenterResponseDTO> updateCenter(@PathVariable Long id, @Valid @RequestBody CenterRequestDTO dto) {
        return ResponseEntity.ok(centerService.updateCenter(id, dto));
    }

    // admin: soft-delete a center
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/admin/centers/{id}")
    public ResponseEntity<String> deleteCenter(@PathVariable Long id) {
        return ResponseEntity.ok(centerService.deleteCenter(id));
    }
}
