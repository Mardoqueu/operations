package com.challenge.operations.controller;

import com.challenge.operations.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller for managing user-related operations through RESTful endpoints.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Service for handling user-related operations such as fetching
     * user details, updating user balance, and performing other user-related actions.
     */
    @Autowired
    private UserService userService;


    /**
     * Adds a specified amount to the balance of the user with the given userId.
     *
     * @param userId the ID of the user to update the balance for.
     * @param amount the amount to be added to the user's balance.
     * @return a ResponseEntity containing a success message if the balance is
     *         added successfully.
     */
    @PostMapping("/add-balance")
    public ResponseEntity<String> addBalance(@RequestParam Long userId, @RequestParam BigDecimal amount) {
        userService.addBalance(userId, amount);
        return ResponseEntity.ok("Balance added successfully!");
    }

    /**
     * Retrieves the balance of a user.
     *
     * @param userId the ID of the user whose balance is to be retrieved
     * @return the balance of the specified user as a {@link BigDecimal} wrapped in a {@link ResponseEntity}
     */
    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam Long userId) {
        return ResponseEntity.ok(userService.getBalance(userId));
    }
}
