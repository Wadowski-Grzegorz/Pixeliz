package com.example.backend.service;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.exception.InvalidDataException;
import com.example.backend.exception.PermissionDeniedException;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.SecurityRole;
import com.example.backend.model.User;
import com.example.backend.model.UserStatistics;
import com.example.backend.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    private void checkUser(User user, String username){
        User userCheck = getUserByUsername(username);
        if(!user.equals(userCheck)){
            throw new PermissionDeniedException();
        }
    }

    public User getUser(Long id){
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(Map.of("id", id)));
    }

    public UserDTO getUserSummary(Long id){
        User user = getUser(id);
        return UserDTO
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public User getUserByName(String name){
        return userRepository
                .findByName(name)
                .orElseThrow(() -> new UserNotFoundException(Map.of("name", name)));
    }

    public User getUserByEmail(String email){
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(Map.of("email", email)));
    }

    public User getUserByUsername(String username){
        return getUserByEmail(username);
    }

    public AuthResponseDTO createUser(UserDTO userDTO){
        User user = dtoToUser(userDTO);
        verifyUserCredentials(user);
        user.setUserStatistics(new UserStatistics());
        user.setSecurityRole(SecurityRole.USER);
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO
                .builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponseDTO login(LoginDTO loginDTO){
        if((loginDTO.getLogin() == null || loginDTO.getLogin().isEmpty())
                && (loginDTO.getEmail() == null || loginDTO.getEmail().isEmpty())){
            throw new InvalidDataException("Provide login or email with password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );
        var user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new UserNotFoundException(Map.of("emailLogin", loginDTO.getEmail())));
        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO
                .builder()
                .token(jwtToken)
                .build();
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id, String username, boolean isAdmin){
        User user = getUser(id);

        if(!isAdmin){
            checkUser(user, username);
        }

        userRepository.deleteById(id);
    }

    public AuthResponseDTO updateUser(Long id, UserDTO userDTO, String username, boolean isAdmin){
        // update what is present
        User user = getUser(id);

        if(!isAdmin){
            checkUser(user, username);
        }

        updateIfPresent(
                userDTO.getName(),
                "name",
                v -> userRepository.findByName(v).isPresent(),
                user::setName);

        updateIfPresent(
                userDTO.getLogin(),
                "login",
                v -> userRepository.findByLogin(v).isPresent(),
                user::setLogin);

        updateIfPresent(
                userDTO.getEmail(),
                "Email",
                v -> userRepository.findByEmail(v).isPresent(),
                user::setEmail);

        if(userDTO.getPassword() != null && !userDTO.getPassword().isBlank()){
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }

        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthResponseDTO
                .builder()
                .token(jwtToken)
                .build();
    }

    private void verifyUserCredentials(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("email");
        }
        if(userRepository.findByName(user.getName()).isPresent()){
            throw new UserAlreadyExistsException("name");
        }
        if(userRepository.findByLogin(user.getLogin()).isPresent()){
            throw new UserAlreadyExistsException("login");
        }
    }

    private interface Checker{
        boolean check(String value);
    }
    private interface Updater{
        void update(String value);
    }

    private void updateIfPresent(String value, String type, Checker checker, Updater updater){
        if(value != null && !value.isBlank()){
            if(checker.check(value)){
                throw new UserAlreadyExistsException(type);
            }
            updater.update(value);
        }
    }

    private User dtoToUser(UserDTO userDTO){
        return User.builder()
                .name(userDTO.getName())
                .login(userDTO.getLogin())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .build();
    }

    private UserDTO userToDTO(User user){
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .login(user.getLogin())
                .email(user.getEmail())
                .build();
    }
}
