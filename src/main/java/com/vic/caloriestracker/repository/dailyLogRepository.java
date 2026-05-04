package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.dailyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface dailyLogRepository extends JpaRepository<dailyLog,Long> {
        List<dailyLog> findByUserId_Id(Long userId);

        List<dailyLog> findAllByUserId_IdAndDateOrderByIdAsc(Long userId, int date);
}
