package com.example.Baseera.controller;

import com.example.Baseera.dto.request.ChildRequestDTO;
import com.example.Baseera.dto.response.ChildResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.security.SecurityUtils;
import com.example.Baseera.service.ChildService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/children")
public class ChildController {

    @Autowired
    private ChildService childService;

    @Autowired
    private SecurityUtils securityUtils;

    // parent: add a child — automatically linked to the logged-in parent from the JWT
    @PostMapping
    public ResponseEntity<ChildResponseDTO> createChild(@Valid @RequestBody ChildRequestDTO dto) {
        Account parent = securityUtils.getCurrentAccount();
        return ResponseEntity.status(HttpStatus.CREATED).body(childService.createChild(dto, parent));
    }

    // parent: update one of their own children
    @PutMapping("/{childId}")
    public ResponseEntity<ChildResponseDTO> updateChild(@PathVariable Long childId, @Valid @RequestBody ChildRequestDTO dto) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(childService.updateChild(childId, dto, parentId));
    }

    // parent: soft-delete a child
    @DeleteMapping("/{childId}")
    public ResponseEntity<String> deleteChild(@PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(childService.deleteChild(childId, parentId));
    }

    // parent: get one of their own children
    @GetMapping("/{childId}")
    public ResponseEntity<ChildResponseDTO> getChildById(@PathVariable Long childId) {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(childService.getChildById(childId, parentId));
    }

    // parent: list all their own children
    @GetMapping
    public ResponseEntity<List<ChildResponseDTO>> getMyChildren() {
        Long parentId = securityUtils.getCurrentAccountId();
        return ResponseEntity.ok(childService.getMyChildren(parentId));
    }
}
