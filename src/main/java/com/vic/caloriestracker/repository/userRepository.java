package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, Long> {
     user findByUsername(String username);

     user findByEmail(String email);

     user findById(long id);
}
