package com.example.backend.service;

import com.example.backend.dto.UserRoleDTO;
import com.example.backend.exception.RelationNotFoundException;
import com.example.backend.model.*;
import com.example.backend.repository.UserDrawingRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserDrawingRoleServiceTest {

    @Mock
    UserDrawingRoleRepository userDrawingRoleRepository;

    @Mock
    UserService userService;
    @Mock
    DrawingService drawingService;
    @Mock
    RoleService roleService;

    @InjectMocks
    UserDrawingRoleService userDrawingRoleService;

    User user;
    Role role;
    Drawing drawing;
    UserDrawingRole udr;

    @BeforeEach
    void setUp() {
        user = User
                .builder()
                .id(1L)
                .name("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .build();
        role = Role
                .builder()
                .id(1L)
                .name("admin")
                .admin(true)
                .read(true)
                .write(true)
                .delete(true)
                .build();
        drawing = Drawing
                .builder()
                .id(1L)
                .grid("[\"white\",\"white\"]")
                .name("my drawing")
                .size_x(1)
                .size_y(2)
                .build();
        udr = new UserDrawingRole(user, drawing, role);
    }

    @Test
    void createUserDrawingRole_ValidInput_ReturnsCreatedUserDrawingRole() {
        // precondition
        given(userDrawingRoleRepository.save(any(UserDrawingRole.class))).willReturn(udr);

        // action
        UserDrawingRole returnedUdr = userDrawingRoleService.createUserDrawingRole(user, drawing, role);

        // verify
        verify(userDrawingRoleRepository, times(1)).save(any(UserDrawingRole.class));
        assertThat(returnedUdr)
                .isEqualTo(udr);
    }

    @Test
    void getUsersFromDrawing_IdExists_ReturnsUsers() {
        // precondition
        User user2 = User.builder().id(2L).build();
        Long drawingId = 1L;
        Object[] ob1 = new Object[]{user, role};
        Object[] ob2 = new Object[]{user2, role};
        given(userDrawingRoleRepository.findUsersAndRoles(drawingId)).willReturn(List.of(ob1, ob2));

        // action
        List<UserRoleDTO> urDto = userDrawingRoleService.getUsersFromDrawing(drawingId);

        // verify
        assertThat(urDto)
                .isNotNull()
                .isNotEmpty();

        assertThat(urDto)
                .anySatisfy(dto -> assertThat(dto.getUserId()).isEqualTo(user.getId()));
        assertThat(urDto)
                .anySatisfy(dto -> assertThat(dto.getUserId()).isEqualTo(user2.getId()));

        assertThat(urDto.get(0).getRole().getId())
                .isEqualTo(role.getId());
        assertThat(urDto.get(1).getRole().getId())
                .isEqualTo(role.getId());
    }

    @Test
    void getUserDrawingRole() {
        // precondition
        Long drawingId = 1L;
        Long userId = 1L;
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);
        given(userDrawingRoleRepository.findById(key)).willReturn(Optional.of(udr));

        // action
        UserDrawingRole returnedUdr = userDrawingRoleService.getUserDrawingRole(drawingId, userId);

        // verify
        assertThat(returnedUdr)
                .isNotNull()
                .isEqualTo(udr);
    }

    @Test
    void getUserDrawingRole_IdDoesNotExists_ThrowsRelationNotFoundException() {
        // precondition
        Long drawingId = 100L;
        Long userId = 100L;
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);
        given(userDrawingRoleRepository.findById(key)).willReturn(Optional.empty());

        // action
        RelationNotFoundException ex = assertThrows(RelationNotFoundException.class,
                () -> userDrawingRoleService.getUserDrawingRole(drawingId, userId));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("drawingId", drawingId)
                .containsEntry("userId", userId);
    }

    @Test
    void getUserFromDrawing() {
        // precondition
        Long drawingId = 1L;
        Long userId = 1L;
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);
        given(userDrawingRoleRepository.findById(key)).willReturn(Optional.of(udr));

        // action
        UserRoleDTO urDTO = userDrawingRoleService.getUserFromDrawing(drawingId, userId);

        // verify
        assertThat(urDTO.getUserId()).isEqualTo(userId);
        assertThat(urDTO.getRole().getId()).isEqualTo(role.getId());
    }

    @Test
    void updateRole() {
        // precondition
        Long drawingId = 1L;
        Long userId = 1L;
        Long newRoleId = 2L;
        Role newRole = Role.builder().id(newRoleId).build();
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);

        given(roleService.getRole(newRoleId)).willReturn(newRole);
        given(userDrawingRoleRepository.findById(key)).willReturn(Optional.of(udr));
        given(userDrawingRoleRepository.save(any(UserDrawingRole.class))).willReturn(udr);

        // action
        UserRoleDTO returnedUdrDto =
                userDrawingRoleService.updateRole(drawingId, userId, newRoleId);

        // verify
        verify(userDrawingRoleRepository, times(1)).save(any(UserDrawingRole.class));
        assertThat(returnedUdrDto.getRole().getId()).isEqualTo(newRoleId);
        assertThat(returnedUdrDto.getUserId()).isEqualTo(userId);
    }

    @Test
    void deleteUserDrawingRole() {
        // precondition
        Long drawingId = 1L;
        Long userId = 1L;
        UserDrawingKey key = new UserDrawingKey(drawingId, userId);
        given(userDrawingRoleRepository.findById(key)).willReturn(Optional.of(udr));

        // action
        userDrawingRoleService.deleteUserDrawingRole(drawingId, userId);

        // verify
        verify(userDrawingRoleRepository, times(1)).delete(any(UserDrawingRole.class));
    }
}