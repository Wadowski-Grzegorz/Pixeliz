package com.example.backend.repository;

import com.example.backend.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class RoleRepositoryUnitTest {
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp(){
        roleRepository.deleteAll();
        Role role = Role
                .builder()
                .name("admin")
                .admin(true)
                .read(true)
                .write(true)
                .delete(true)
                .build();
        roleRepository.save(role);
    }

    @Test
    public void findByName_NameExists_Role() {
        String name = "admin";
        Optional<Role> expected = roleRepository.findByName(name);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(name, expected.get().getName());
    }
}
