package com.challenge.operations.service;

import com.challenge.operations.entity.User;
import com.challenge.operations.exception.UserNotFoundException;
import com.challenge.operations.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {

    /**
     * Repository interface for performing CRUD operations on `User` entities.
     * This is auto-wired to provide automatic dependency injection.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Finds a user by their unique ID.
     *
     * @param userId the unique ID of the user to be retrieved
     * @return the User object associated with the given userId
     * @throws IllegalArgumentException if no user is found with the provided ID
     */
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    /**
     * Updates the given user in the repository.
     *
     * @param user the user entity to be updated
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Adds a specified amount to the balance of the user with the given userId.
     *
     * @param userId the ID of the user to update the balance for.
     * @param amount the amount to be added to the user's balance.
     * @throws IllegalArgumentException if the amount is less than or equal to zero,
     *                                  or if the user is not found.
     * @throws UserNotFoundException if the user with the given userId does not exist.
     */
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

    /**
     * Finds a User entity in the repository based on the provided username.
     *
     * @param username the username of the user to be found
     * @return the User entity associated with the given username
     * @throws UsernameNotFoundException if no user is found with the provided username
     */
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return user;
    }

    /**
     * Retrieves the balance of a user.
     *
     * @param userId the ID of the user whose balance is to be retrieved
     * @return the balance of the user as a BigDecimal
     * @throws UserNotFoundException if the user with the specified ID is not found
     */
    public BigDecimal getBalance(Long userId) {
        User user = findById(userId);
        if (user == null) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }

        return user.getBalance();
    }

    public User getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
