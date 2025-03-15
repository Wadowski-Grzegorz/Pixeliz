package com.example.backend.service;

import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long id){
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    // this will go to login
    public User createUser(UserDTO userDTO){
        return userRepository.save(DTOtoUser(userDTO));
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public void deleteUser(Long id){
        getUser(id);
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, UserDTO userDTO){
        User user = getUser(id);
        if(userDTO.getUsername() != null && !userDTO.getUsername().isBlank()){
            user.setUsername(userDTO.getUsername());
        }
        if (userDTO.getLogin() != null && !userDTO.getLogin().isBlank()) {
            user.setLogin(userDTO.getLogin());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getEmail() != null && !userDTO.getEmail().isBlank()) {
            user.setEmail(userDTO.getEmail());
        }

        return userRepository.save(user);
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
