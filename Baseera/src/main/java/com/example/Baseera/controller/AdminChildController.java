package com.example.Baseera.controller;

import com.example.Baseera.dto.response.ChildAdminResponseDTO;
import com.example.Baseera.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Deliberately no "list every child" endpoint here. Admin gets a total
 * count (oversight, zero exposure) and a targeted search by parent email
 * (real support cases only) — never a general browse of every family's data.
 */
@RestController
@RequestMapping("/api/admin/children")
@PreAuthorize("hasRole('ADMIN')")
public class AdminChildController {

    @Autowired
    private ChildService childService;

    // admin: just the number — for a dashboard stat, not individual records
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalChildrenCount() {
        return ResponseEntity.ok(childService.getTotalActiveChildrenCount());
    }

    // admin: search by the PARENT's email — a targeted lookup, not a browse
    @GetMapping("/search")
    public ResponseEntity<List<ChildAdminResponseDTO>> searchByParentEmail(@RequestParam String parentEmail) {
        return ResponseEntity.ok(childService.searchChildrenByParentEmail(parentEmail));
    }
}