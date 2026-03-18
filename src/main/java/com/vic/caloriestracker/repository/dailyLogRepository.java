package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.dailyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface dailyLogRepository extends JpaRepository<dailyLog,Long> {
        Optional<dailyLog> findById(Long id);

        List<dailyLog> findByUserId(Long userId);
}
