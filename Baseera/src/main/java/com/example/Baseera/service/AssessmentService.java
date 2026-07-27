package com.example.Baseera.service;


import com.example.Baseera.repository.AssessmentRepository;
import com.example.Baseera.repository.ChildRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AssessmentService {
    private final AssessmentRepository assessmentRepository;
    private final ChildRepository childRepository;


}
