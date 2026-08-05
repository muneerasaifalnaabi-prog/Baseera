package com.example.Baseera.repository;

import com.example.Baseera.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Query("SELECT c FROM Child c WHERE c.isActive = true AND c.id = :id")
    Optional<Child> findChildById(@Param("id") Long id);

    // Ownership check used by every child-linked write across the app.
    @Query("SELECT c FROM Child c WHERE c.isActive = true AND c.id = :id AND c.parent.id = :parentId")
    Optional<Child> findChildByIdAndParentId(@Param("id") Long id, @Param("parentId") Long parentId);


    @Query("SELECT c FROM Child c WHERE c.isActive = true AND c.parent.id = :parentId")
    List<Child> findAllByParentId(@Param("parentId") Long parentId);

    // admin: every active child across every parent, for the admin view-only screen
    @Query("SELECT c FROM Child c WHERE c.isActive = true")
    List<Child> findAllActive();
    long countByIsActiveTrue();

    @Query("SELECT c FROM Child c WHERE c.isActive = true AND c.parent.email = :email")
    List<Child> findAllByParentEmail(@Param("email") String email);
}
