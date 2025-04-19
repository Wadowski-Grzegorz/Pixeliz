package com.example.backend.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class DrawingRepositoryUnitTest {
    @Autowired
    private DrawingRepository drawingRepository;

}
