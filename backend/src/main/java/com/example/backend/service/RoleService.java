package com.example.backend.service;

import com.example.backend.exception.RoleNotFoundException;
import com.example.backend.model.Role;
import com.example.backend.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles(){
        return roleRepository.findAll();
    }

    public Role getRole(Long id){
        return roleRepository
                .findById(id)
                .orElseThrow(() -> new RoleNotFoundException(Map.of("id", id)));
    }

    public Role getRole(String name){
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(Map.of("name", name)));
    }
}
