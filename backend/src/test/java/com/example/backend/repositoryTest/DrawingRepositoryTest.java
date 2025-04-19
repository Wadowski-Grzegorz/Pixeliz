package com.example.backend.repositoryTest;

import com.example.backend.repository.DrawingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DrawingRepositoryTest {
    @Autowired
    private DrawingRepository drawingRepository;

}
