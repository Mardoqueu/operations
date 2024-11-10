package com.challenge.operations.service;

import com.challenge.operations.entity.User;
import com.challenge.operations.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById_Success() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testFindById_UserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findById(userId);
        });

        assertEquals("User not found.", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser_Success() {
        User user = new User();
        user.setId(1L);

        userService.updateUser(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddBalance_Success() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("50.00");
        BigDecimal currentBalance = new BigDecimal("100.00");

        User user = new User();
        user.setId(userId);
        user.setBalance(currentBalance);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userService.addBalance(userId, amount);

        assertEquals(new BigDecimal("150.00"), user.getBalance());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testAddBalance_NegativeAmount() {
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("-10.00");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addBalance(userId, amount);
        });

        assertEquals("The recharge value must be positive.", exception.getMessage());

        verify(userRepository, never()).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testAddBalance_ZeroAmount() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.ZERO;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addBalance(userId, amount);
        });

        assertEquals("The recharge value must be positive.", exception.getMessage());

        verify(userRepository, never()).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}
