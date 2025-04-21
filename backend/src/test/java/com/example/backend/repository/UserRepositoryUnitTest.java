package com.example.backend.repository;

import com.example.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryUnitTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        User user = User
                .builder()
                .name("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .build();
        userRepository.save(user);
    }

    @Test
    public void findByName_NameExists_User(){
        String name = "Jamal";
        Optional<User> expected = userRepository.findByName(name);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(name, expected.get().getName());
    }

    @Test
    public void findByEmail_EmailExists_User(){
        String email = "jamal@email.com";
        Optional<User> expected = userRepository.findByEmail(email);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(email, expected.get().getEmail());
    }

    @Test
    public void findByLogin_LoginExists_User(){
        String login = "Jamal445";
        Optional<User> expected = userRepository.findByLogin(login);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(login, expected.get().getLogin());
    }

    @Test
    public void findByPassword_PasswordExists_User(){
        String password = "StrongPassword";
        Optional<User> expected = userRepository.findByPassword(password);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(password, expected.get().getPassword());
    }

    @Test
    public void findByLoginAndPassword_LoginAndPasswordExists_User(){
        String login = "Jamal445";
        String password = "StrongPassword";
        Optional<User> expected = userRepository.findByLoginAndPassword(login, password);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(login, expected.get().getLogin());
        assertEquals(password, expected.get().getPassword());
    }
    @Test
    public void findByEmailAndPassword_EmailAndPasswordExists_User(){
        String email = "jamal@email.com";
        String password = "StrongPassword";
        Optional<User> expected = userRepository.findByEmailAndPassword(email, password);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(email, expected.get().getEmail());
        assertEquals(password, expected.get().getPassword());
    }
}
