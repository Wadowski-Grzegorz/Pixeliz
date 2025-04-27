package com.example.backend.service;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    private User user;


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
    void getUserByName_NameExists_ReturnsUser() {
        // precondition
        String name = "Jamal";
        given(userRepository.findByName(name)).willReturn(Optional.of(user));

        // action
        User returnedUser = userService.getUserByName(name);

        // verify
        assertThat(returnedUser).isEqualTo(user);
    }

    @Test
    void getUserByName_NameDoesNotExist_ThrowsUserNotFoundException() {
        // precondition
        String name = "not real name";
        given(userRepository.findByName(name)).willReturn(Optional.empty());

        // action
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.getUserByName(name));

        // verify
        assertThat(ex.getCriteria())
                .isNotNull()
                .isNotEmpty()
                .containsEntry("name", name);
    }

    @Test
    void createUser_ValidInput_CreatesUserAndReturnsToken() {
        // precondition
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );
        String token = "newToken";
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(token);
        given(userRepository.save(any(User.class))).willReturn(user);
        given(jwtService.generateToken(any(User.class))).willReturn(token);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
        given(userRepository.findByName(anyString())).willReturn(Optional.empty());
        given(userRepository.findByLogin(anyString())).willReturn(Optional.empty());
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        // action
        AuthResponseDTO returned = userService.createUser(uDto);

        // verify
        assertThat(returned).isEqualTo(authResponseDTO);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_EmailExists_ThrowsUserAlreadyExistsException() {
        // precondition
        UserDTO uDto = new UserDTO(
                "unique",
                "unique",
                "used@email.com",
                "UniquePassword"
        );
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

        // action
        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(uDto));

        // verify
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isNotEmpty();
    }

    @Test
    void createUser_NameExists_ThrowsUserAlreadyExistsException() {
        // precondition
        UserDTO uDto = new UserDTO(
                "used",
                "unique",
                "unique@email.com",
                "UniquePassword"
        );
        given(userRepository.findByName(anyString())).willReturn(Optional.of(user));

        // action
        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(uDto));

        // verify
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isNotEmpty();
    }

    @Test
    void createUser_LoginExists_ThrowsUserAlreadyExistsException() {
        // precondition
        UserDTO uDto = new UserDTO(
                "unique",
                "used",
                "unique@email.com",
                "UniquePassword"
        );
        given(userRepository.findByLogin(anyString())).willReturn(Optional.of(user));

        // action
        UserAlreadyExistsException ex = assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(uDto));

        // verify
        assertThat(ex).isNotNull();
        assertThat(ex.getMessage()).isNotEmpty();
    }

    @Test
    void getUsers_ReturnsAllUsers() {
        // precondition
        User user2 = User
                .builder()
                .id(2L)
                .name("Ahmed")
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
    void deleteUser_IdExists_DeletesUser() {
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
    void updateUser_ValidInput_ReturnsToken() {
        // precondition
        Long id = 1L;
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );
        User newUser = User
                .builder()
                .name(uDto.getName())
                .login(uDto.getLogin())
                .email(uDto.getEmail())
                .password(uDto.getPassword())
                .build();
        String token = "I am a token";
        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.findByName("newName")).willReturn(Optional.empty());
        given(userRepository.findByLogin("newLogin")).willReturn(Optional.empty());
        given(userRepository.findByEmail("newEmail@email.com")).willReturn(Optional.empty());
        given(userRepository.save(any(User.class))).willReturn(newUser);
        given(jwtService.generateToken(any(User.class))).willReturn(token);

        // action
        AuthResponseDTO authToken = userService.updateUser(id, uDto);

        // verify
        assertThat(authToken.getToken()).isNotEmpty();
    }

    @Test
    void updateUser_IdDoesNotExists_ThrowsUserNotFoundException() {
        // precondition
        Long id = 100L;
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );

        given(userRepository.findById(id)).willReturn(Optional.empty());

        // action
        UserNotFoundException ex = assertThrows(UserNotFoundException.class, () -> userService.updateUser(id, uDto));

        // verify
        assertThat(ex.getCriteria())
                .isNotEmpty()
                .containsEntry("id", id);
    }

    @Test
    void updateUser_NameExists_ThrowsUserAlreadyExistsException() {
        // precondition
        Long id = 1L;
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.findByName(anyString())).willReturn(Optional.of(new User()));

        // action
        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.updateUser(id, uDto)
        );

        // verify
        assertThat(ex.getMessage()).isNotEmpty();
    }

    @Test
    void updateUser_LoginExists_ThrowsUserAlreadyExistsException() {
        // precondition
        Long id = 1L;
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.findByName(anyString())).willReturn(Optional.empty());
        given(userRepository.findByLogin(anyString())).willReturn(Optional.of(new User()));

        // action
        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.updateUser(id, uDto)
        );

        // verify
        assertThat(ex.getMessage()).isNotEmpty();
    }

    @Test
    void updateUser_EmailExists_ThrowsUserAlreadyExistsException() {
        // precondition
        Long id = 1L;
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );

        given(userRepository.findById(id)).willReturn(Optional.of(user));
        given(userRepository.findByName(anyString())).willReturn(Optional.empty());
        given(userRepository.findByLogin(anyString())).willReturn(Optional.empty());
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(new User()));

        // action
        UserAlreadyExistsException ex = assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.updateUser(id, uDto)
        );

        // verify
        assertThat(ex.getMessage()).isNotEmpty();
    }
}