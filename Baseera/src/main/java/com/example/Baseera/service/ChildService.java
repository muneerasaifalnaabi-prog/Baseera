package com.example.Baseera.service;

import com.example.Baseera.dto.request.ChildRequestDTO;
import com.example.Baseera.dto.response.ChildResponseDTO;
import com.example.Baseera.entity.Account;
import com.example.Baseera.entity.Child;
import com.example.Baseera.exception.AccessDeniedCustomException;
import com.example.Baseera.exception.ResourceNotFoundException;
import com.example.Baseera.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class ChildService {

    @Autowired
    private ChildRepository childRepository;

    //****========
    // parent: add a child, automatically linked to the logged-in parent
    //==========****
    public ChildResponseDTO createChild(ChildRequestDTO dto, Account parent) {
        Child child = dto.toEntity(parent);
        Child saved = childRepository.save(child);
        return ChildResponseDTO.fromEntity(saved);
    }

    //****========
    // parent: update a child's own profile fields (never re-parents the child)
    //==========****
    public ChildResponseDTO updateChild(Long childId, ChildRequestDTO dto, Long parentId) {
        Child child = getChildOwnedByParent(childId, parentId);
        dto.applyTo(child);
        Child updated = childRepository.save(child);
        return ChildResponseDTO.fromEntity(updated);
    }

    //****========
    // parent: soft-delete a child
    //==========****
    public String deleteChild(Long childId, Long parentId) {
        Child child = getChildOwnedByParent(childId, parentId);
        child.setIsActive(false);
        childRepository.save(child);
        return "DELETED";
    }

    //****========
    // parent: get one of their own children by id
    //==========****
    public ChildResponseDTO getChildById(Long childId, Long parentId) {
        Child child = getChildOwnedByParent(childId, parentId);
        return ChildResponseDTO.fromEntity(child);
    }

    //****========
    // parent: list all their own children
    //==========****
    public List<ChildResponseDTO> getMyChildren(Long parentId) {
        List<Child> children = childRepository.findAllByParentId(parentId);
        return ChildResponseDTO.fromEntity(children);
    }

    //****========
    // shared helper — every other child-linked write/read across the app
    // (assessments, attachments, activities) calls this FIRST. If the
    // child doesn't belong to this parent, reject before anything else happens.
    //==========****
    public Child getChildOwnedByParent(Long childId, Long parentId) {
        return childRepository.findChildByIdAndParentId(childId, parentId)
                .orElseThrow(() -> new AccessDeniedCustomException("This child does not belong to you"));
    }

    //****========
    // used everywhere age matters (activities, condition context) — the
    // parent is never asked for age again once dateOfBirth is set.
    //==========****
    public int calculateAgeInYears(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}



