package com.example.Baseera.service;

import com.example.Baseera.dto.request.CenterRequestDTO;
import com.example.Baseera.dto.response.CenterResponseDTO;
import com.example.Baseera.entity.Center;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.CenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CenterService {
    private final CenterRepository centerRepository;

    // Create a new center
    public CenterResponseDTO createCenter(CenterRequestDTO request) {
        Center center = request.toEntity();
        Center savedCenter = centerRepository.save(center);
        return CenterResponseDTO.fromEntity(savedCenter);
    }
    // Retrieve all active centers
    public List<CenterResponseDTO> getAllCenters() {

        // Fetch only active centers
        List<Center> centers = centerRepository.findByIsActiveTrue();

        // Convert the entities into response DTOs
        return CenterResponseDTO.fromEntity(centers);
    }

    // Retrieve a center by its ID
    public CenterResponseDTO getCenterById(Long id) {

        // Find the center or throw an exception if it does not exist
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found."));

        // Ensure the center is active
        if (!center.getIsActive()) {
            throw new ResourceNotFoundException("Center not found.");
        }

        // Return the center details
        return CenterResponseDTO.fromEntity(center);
    }

    // Update an existing center
    public CenterResponseDTO updateCenter(Long id, CenterRequestDTO request) {

        // Find the center by its ID
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found."));

        // Ensure the center is active
        if (!center.getIsActive()) {
            throw new ResourceNotFoundException("Center not found.");
        }

        // Update the center fields
        request.applyTo(center);

        // Save the updated center
        Center updatedCenter = centerRepository.save(center);

        // Return the updated center
        return CenterResponseDTO.fromEntity(updatedCenter);
    }

    // Soft delete a center
    public void deleteCenter(Long id) {

        // Find the center
        Center center = centerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found."));

        // Ensure the center is active
        if (!center.getIsActive()) {
            throw new ResourceNotFoundException("Center not found.");
        }

        // Mark the center as inactive
        center.setIsActive(false);

        // Save the updated status
        centerRepository.save(center);
    }

}
