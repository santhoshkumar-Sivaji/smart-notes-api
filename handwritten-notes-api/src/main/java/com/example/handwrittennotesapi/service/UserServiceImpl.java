package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.model.User;
import com.example.handwrittennotesapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getCurrentUser() {
        // In a real application, this would be derived from the SecurityContext
        // For now, we'll create a user if one doesn't exist, to allow testing.
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setCreatedAt(java.time.LocalDateTime.now());
            return userRepository.save(newUser);
        });
    }
}
