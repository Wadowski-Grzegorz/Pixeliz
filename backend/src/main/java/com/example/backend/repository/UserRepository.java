package com.example.backend.repository;

import com.example.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    Optional<User> findByPassword(String password);
    Optional<User> findByLoginAndPassword(String login, String password);
    Optional<User> findByEmailAndPassword(String email, String password);
}
