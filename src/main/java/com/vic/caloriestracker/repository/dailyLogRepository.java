package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.dailyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface dailyLogRepository extends JpaRepository<dailyLog,Long> {
        List<dailyLog> findByUserId_Id(Long userId);

        Optional<dailyLog> findByUserId_IdAndDate(Long userId, int date);
}
