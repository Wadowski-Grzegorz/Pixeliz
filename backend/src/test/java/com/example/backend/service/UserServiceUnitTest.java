package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    private User user;


    @BeforeEach
    void setUp() {
        user = User
                .builder()
                .id(1L)
                .username("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .build();
    }

    @Test
    void getUser_IdExists_ReturnsUser() {
        // precondition
        Long id = 1L;
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        // action
        User returnedUser = userService.getUser(id);

        // verify
        assertThat(returnedUser).isEqualTo(user);
    }

    @Test
    void getUser_IdDoesNotExist_ThrowsUserNotFoundException() {
        // precondition
        Long id = 100L;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // action
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUser(id));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("id", id);
    }

    @Test
    void getUser_UsernameExists_ReturnsUser() {
        // precondition
        String username = "Jamal";
        given(userRepository.findByUsername(username)).willReturn(Optional.of(user));

        // action
        User returnedUser = userService.getUser(username);

        // verify
        assertThat(returnedUser).isEqualTo(user);
    }

    @Test
    void getUser_UsernameDoesNotExist_ThrowsUserNotFoundException() {
        // precondition
        String username = "not real username";
        given(userRepository.findByUsername(username)).willReturn(Optional.empty());

        // action
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUser(username));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("username", username);
    }

    @Test
    @Disabled
    void testGetUser1() {
    }

    @Test
    @Disabled
    void createUser() {
    }

    @Test
    void getUsers() {
        // precondition
        User user2 = User
                .builder()
                .id(2L)
                .username("Ahmed")
                .login("Ahmed445")
                .password("ImperialPassword")
                .email("Ahmed@email.com")
                .build();
        given(userRepository.findAll()).willReturn(List.of(user, user2));

        // action
        List<User> users = userService.getUsers();

        // verify
        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .isEqualTo(List.of(user, user2));
    }

    @Test
    void deleteUser_IdExists() {
        // precondition
        Long id = 1L;
        given(userRepository.findById(id)).willReturn(Optional.of(user));

        // action
        userService.deleteUser(id);

        // verify
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteUser_IdDoesNotExist_ThrowsUserNotFoundException() {
        // precondition
        Long id = 100L;
        given(userRepository.findById(id)).willReturn(Optional.empty());

        // action
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.deleteUser(id));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("id", id);
    }

    @Test
    void updateUser() {
        // precondition
        Long id = 1L;
        UserDTO uDto = new UserDTO(
                "newUsername",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.findByUsername("newUsername")).willReturn(Optional.empty());
        given(userRepository.findByLogin("newLogin")).willReturn(Optional.empty());
        given(userRepository.findByEmail("newEmail@email.com")).willReturn(Optional.empty());
        given(userRepository.findByPassword("newPassword")).willReturn(Optional.empty());
        given(userRepository.save(user)).willReturn(user);

        // action
        User returnedUser = userService.updateUser(id, uDto);

        // verify
        assertThat(returnedUser.getId()).isEqualTo(id);
        assertThat(returnedUser.getUsername()).isEqualTo("newUsername");
        assertThat(returnedUser.getLogin()).isEqualTo("newLogin");
        assertThat(returnedUser.getEmail()).isEqualTo("newEmail@email.com");
        assertThat(returnedUser.getPassword()).isEqualTo("newPassword");
    }
}