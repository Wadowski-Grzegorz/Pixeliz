package com.example.backend.service;

import com.example.backend.exception.RoleNotFoundException;
import com.example.backend.model.Role;
import com.example.backend.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RoleServiceUnitTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role
                .builder()
                .id(1L)
                .name("admin")
                .admin(true)
                .read(true)
                .write(true)
                .delete(true)
                .build();
    }

    @Test
    void getRoles_ReturnsAllRoles() {
        // precondition
        Role role2 = Role
                .builder()
                .id(2L)
                .name("moderator")
                .admin(true)
                .read(true)
                .write(true)
                .delete(false)
                .build();
        given(roleRepository.findAll()).willReturn(List.of(role, role2));

        // action
        List<Role> roles = roleService.getRoles();

        // verify
        assertThat(roles)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(List.of(role, role2));
    }

    @Test
    void getRole_IdExists_ReturnsRole() {
        // precondition
        Long id = 1L;
        given(roleRepository.findById(id)).willReturn(Optional.of(role));

        // action
        Role returnedRole = roleService.getRole(id);

        // verify
        assertThat(returnedRole).isEqualTo(role);
    }

    @Test
    void getRole_IdDoesNotExists_ThrowsRoleNotFoundException() {
        // precondition
        Long id = 100L;
        given(roleRepository.findById(id)).willReturn(Optional.empty());

        // action
        RoleNotFoundException ex = assertThrows(RoleNotFoundException.class, () -> roleService.getRole(id));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("id", id);
    }

    @Test
    void getRole_NameExists_ReturnsRole() {
        // precondition
        String name = "admin";
        given(roleRepository.findByName(name)).willReturn(Optional.of(role));

        // action
        Role returnedRole = roleService.getRole(name);

        // verify
        assertThat(returnedRole).isEqualTo(role);
    }

    @Test
    void getRole_NameDoesNotExists_ThrowsRoleNotFoundException() {
        // precondition
        String name = "not real role";
        given(roleRepository.findByName(name)).willReturn(Optional.empty());

        // action
        RoleNotFoundException ex = assertThrows(RoleNotFoundException.class, () -> roleService.getRole(name));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("name", name);
    }
}