package com.example.Baseera.controller;

import com.example.Baseera.dto.request.CenterRequestDTO;
import com.example.Baseera.dto.response.CenterResponseDTO;
import com.example.Baseera.service.CenterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centers")
@AllArgsConstructor
public class CenterController {
    private final CenterService centerService;

    // Create a new center
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CenterResponseDTO createCenter(@Valid @RequestBody CenterRequestDTO request) {
        return centerService.createCenter(request);
    }

    // Retrieve all active centers
    @GetMapping
    public List<CenterResponseDTO> getAllCenters() {
        return centerService.getAllCenters();
    }

    // Retrieve a center by its ID
    @GetMapping("/{id}")
    public CenterResponseDTO getCenterById(@PathVariable Long id) {
        return centerService.getCenterById(id);
    }

    // Update an existing center
    @PutMapping("/{id}")
    public CenterResponseDTO updateCenter(@PathVariable Long id,
                                          @Valid @RequestBody CenterRequestDTO request) {
        return centerService.updateCenter(id, request);
    }

    // Soft delete a center
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCenter(@PathVariable Long id) {
        centerService.deleteCenter(id);
    }
}

