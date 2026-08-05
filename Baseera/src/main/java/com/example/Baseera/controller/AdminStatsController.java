package com.example.Baseera.controller;

import com.example.Baseera.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * One place for every admin dashboard stat, regardless of which
 * domain it comes from. Right now that's just accounts, but this is
 * where Activities/Centers counts would plug in later too, instead of
 * scattering /stats endpoints across every individual controller.
 */
@RestController
@RequestMapping("/api/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("activeAccounts", accountService.getActiveCount());
        stats.put("deactivatedAccounts", accountService.getDeactivatedCount());
        stats.put("registrationTrend", accountService.getRegistrationTrend());
        return ResponseEntity.ok(stats);
    }
}