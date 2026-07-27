package com.example.Baseera.service;

import com.example.Baseera.repository.AccountRepository;
import com.example.Baseera.repository.ChildRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChildService {
    private final ChildRepository childRepository;
    private final AccountRepository accountRepository;
}
