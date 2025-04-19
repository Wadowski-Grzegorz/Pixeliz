package com.example.backend.service;

import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.exception.UserAlreadyExistsException;
import com.example.backend.exception.UserNotFoundException;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long id){
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(Map.of("id", id)));
    }

    public User getUser(String username){
        return userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(Map.of("username", username)));
    }

    public User getUser(LoginDTO loginDTO){
        // can be login by (login, password) or (email, password)
        String login = loginDTO.getLogin();
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();

        if(password.isBlank() || (email.isBlank() && login.isBlank())){
            throw new IllegalArgumentException("Blank credentials");
        }

        Optional<User> user = Optional.empty();
        if(!email.isBlank()){
            user = userRepository.findByEmailAndPassword(email, password);
        } else if(!login.isBlank()){
            user = userRepository.findByLoginAndPassword(login, password);
        }

        return user.orElseThrow(UserNotFoundException::new);
    }

    public User createUser(UserDTO userDTO){
        User user = DTOtoUser(userDTO);
        verifyUserCredentials(user);
        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        getUser(id);
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, UserDTO userDTO){
        // update what is present
        User user = getUser(id);

        updateIfPresent(
                userDTO.getUsername(),
                "username",
                v -> userRepository.findByUsername(v).isPresent(),
                user::setUsername);

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

        updateIfPresent(
                userDTO.getPassword(),
                "password",
                v -> userRepository.findByPassword(v).isPresent(),
                user::setPassword);

        return userRepository.save(user);
    }

    void verifyUserCredentials(User user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new UserAlreadyExistsException("email");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("username");
        }
        if(userRepository.findByLogin(user.getLogin()).isPresent()){
            throw new UserAlreadyExistsException("login");
        }
        if(userRepository.findByPassword(user.getPassword()).isPresent()){
            throw new UserAlreadyExistsException("password");
        }
    }

    interface Checker{
        boolean check(String value);
    }
    interface Updater{
        void update(String value);
    }

    void updateIfPresent(String value, String type, Checker checker, Updater updater){
        if(value != null && !value.isBlank()){
            if(checker.check(value)){
                throw new UserAlreadyExistsException(type);
            }
            updater.update(value);
        }
    }

    public User DTOtoUser(UserDTO userDTO){
        return User.builder()
                .username(userDTO.getUsername())
                .login(userDTO.getLogin())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build();
    }

    public UserDTO UserToDTO(User user){
        return UserDTO.builder()
                .username(user.getUsername())
                .login(user.getLogin())
                .email(user.getEmail())
                .build();
    }
}
