package com.example.Baseera.service;

import com.example.Baseera.dto.request.CenterRequestDTO;
import com.example.Baseera.dto.response.CenterResponseDTO;
import com.example.Baseera.entity.Assessment;
import com.example.Baseera.entity.Center;
import com.example.Baseera.entity.Child;
import com.example.Baseera.enums.ConditionType;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.AssessmentRepository;
import com.example.Baseera.repository.CenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CenterService {

    @Autowired
    private CenterRepository centerRepository;

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private ChildService childService;

    // admin: create a new center in the catalog
    public CenterResponseDTO createCenter(CenterRequestDTO dto) {
        Center center = dto.toEntity();
        Center saved = centerRepository.save(center);
        return CenterResponseDTO.fromEntity(saved);
    }

    // admin: update an existing center's fields
    public CenterResponseDTO updateCenter(Long centerId, CenterRequestDTO dto) {
        Center center = centerRepository.findCenterById(centerId)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found: " + centerId));

        dto.applyTo(center);
        Center updated = centerRepository.save(center);
        return CenterResponseDTO.fromEntity(updated);
    }

    // admin: deactivate the center (soft delete)
    public String deleteCenter(Long centerId) {
        Center center = centerRepository.findCenterById(centerId)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found: " + centerId));

        center.setIsActive(false);
        centerRepository.save(center);
        return "DELETED";
    }


    // parent/admin: get one center by id

    public CenterResponseDTO getCenterById(Long centerId) {
        Center center = centerRepository.findCenterById(centerId)
                .orElseThrow(() -> new ResourceNotFoundException("Center not found: " + centerId));
        return CenterResponseDTO.fromEntity(center);
    }


    // parent/admin: browse the full active directory, optionally filtered by city/specialty

    public List<CenterResponseDTO> searchCenters(String city, ConditionType specialty) {
        List<Center> centers = centerRepository.searchCenters(city, specialty);
        return CenterResponseDTO.fromEntity(centers);
    }

    // parent: automatic recommendation once the child has a suggestedCondition.
    // No manual filtering needed — pulls centers matching ASD/ADHD/BOTH,
    // sorted nearest-first if the child's centers have coordinates.
    public List<CenterResponseDTO> recommendForChild(Long childId, Long parentId) {
        Child child = childService.getChildOwnedByParent(childId, parentId);

        Assessment latest = assessmentRepository.findLatestByChildId(childId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No assessment yet for this child — submit one to get a recommendation"));

        List<Center> matches = centerRepository.findBySpecialtyMatching(latest.getSuggestedCondition());

        // NOTE: nearest-first sorting needs a reference point (e.g. the parent's
        // own location) which Baseera doesn't collect yet — Child has no
        // coordinates. Once that exists, compute a haversine distance here per
        // center and sort by it; for now, matches are returned unsorted.
        return CenterResponseDTO.fromEntity(matches);
    }
}
