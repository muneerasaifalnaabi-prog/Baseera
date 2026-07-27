package com.example.Baseera.repository;

import com.example.Baseera.entity.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CenterRepository extends JpaRepository<Center, Long> {

    List<Center> findByCityIgnoreCase(String city);

    List<Center> findBySpecialtyIgnoreCase(String specialty);
}
