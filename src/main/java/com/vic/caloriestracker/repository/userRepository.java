package com.vic.caloriestracker.repository;

import com.vic.caloriestracker.entity.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface userRepository extends JpaRepository<user, Long> {
     Optional<user> findByEmail(String email);

     // Removed redundant findById (provided by JpaRepository)
     // Removed findByUsername -- the `user` entity has no `username` field; use `findByName` if needed.
}
