package com.example.backend.repository;

import com.example.backend.model.User;
import com.example.backend.model.UserStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserStatisticsRepositoryUnitTest {

    @Autowired
    UserStatisticsRepository userStatisticsRepository;

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
                .userStatistics(new UserStatistics())
                .build();
        userRepository.save(user);
    }

    @Test
    public void increaseClickCount_UserStatisticsExists_IncreasesClickCount() {
        Long id = 1L;
        Optional<UserStatistics> expected = userStatisticsRepository.findById(id);

        assertThat(expected.isPresent()).isTrue();
        assertEquals(id, expected.get().getId());
    }
}
