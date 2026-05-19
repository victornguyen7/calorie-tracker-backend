package com.vic.caloriestracker.service;

import com.vic.caloriestracker.entity.user;
import com.vic.caloriestracker.repository.userRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final userRepository userRepository;

    public DatabaseUserDetailsService(userRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        user foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                foundUser.getEmail(),
                foundUser.getPasswordHash(),
                List.of()
        );
    }
}
