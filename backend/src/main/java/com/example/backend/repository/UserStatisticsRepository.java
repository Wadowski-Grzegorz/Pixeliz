package com.example.backend.repository;

import com.example.backend.model.UserStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserStatisticsRepository extends JpaRepository<UserStatistics, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserStatistics" +
            " SET click_count = click_count + 1" +
            " WHERE id = :id")
    void increaseClickCount(@Param("id") Long id);
}
