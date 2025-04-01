package com.example.backend.repository;

import com.example.backend.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDrawingRoleRepository extends JpaRepository<UserDrawingRole, UserDrawingKey> {

}
