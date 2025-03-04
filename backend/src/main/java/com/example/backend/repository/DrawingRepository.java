package com.example.backend.repository;

import com.example.backend.model.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Long> {
}

