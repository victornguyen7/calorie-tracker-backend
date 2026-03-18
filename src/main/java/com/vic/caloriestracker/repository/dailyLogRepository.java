package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.dailyLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface dailyLogRepository extends JpaRepository<dailyLog,Long> {
        dailyLog findById(long id);

        dailyLog findByUserId(long id);
}
