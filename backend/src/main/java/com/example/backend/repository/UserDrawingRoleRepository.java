package com.example.backend.repository;

import com.example.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDrawingRoleRepository extends JpaRepository<UserDrawingRole, UserDrawingKey> {

    @Query("SELECT udr.user, udr.role FROM UserDrawingRole udr " +
            "WHERE udr.drawing.id = :drawingId")
    List<Object[]> findUsersAndRoles(@Param("drawingId") Long drawingId);
}
