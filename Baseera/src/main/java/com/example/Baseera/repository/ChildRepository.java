package com.example.Baseera.repository;

import com.example.Baseera.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    List<Child> findByParentId(Long parentId);
}
