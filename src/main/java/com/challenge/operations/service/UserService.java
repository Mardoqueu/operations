package com.challenge.operations.service;

import com.challenge.operations.entity.User;
import com.challenge.operations.exception.UserNotFoundException;
import com.challenge.operations.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void addBalance(Long userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("The recharge value must be positive.");
        }

        User user = findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        BigDecimal newBalance = user.getBalance().add(amount);
        user.setBalance(newBalance);
        updateUser(user);
    }

    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return user;
    }

    public BigDecimal getBalance(Long userId) {
        User user = findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        return user.getBalance();
    }
}
