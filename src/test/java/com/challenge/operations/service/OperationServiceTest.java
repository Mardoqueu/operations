package com.challenge.operations.service;

import com.challenge.operations.dto.OperationDTO;
import com.challenge.operations.entity.Operation;
import com.challenge.operations.entity.User;
import com.challenge.operations.exception.InsufficientBalanceException;
import com.challenge.operations.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OperationServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private RecordService recordService;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private OperationService operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExecuteOperation_Add_Success() {
        Long userId = 1L;
        String expression = "10 + 1";
        BigDecimal currentBalance = new BigDecimal("100.00");
        BigDecimal operationCost = new BigDecimal("5.00");
        BigDecimal expectedResult = new BigDecimal("11.00");

        User user = new User();
        user.setId(userId);
        user.setBalance(currentBalance);

        Operation operation = new Operation();
        operation.setType("add");
        operation.setCost(operationCost);

        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setUserId(userId);
        operationDTO.setExpression(expression);

        when(userService.findById(userId)).thenReturn(user);
        when(operationRepository.findByType("add")).thenReturn(Optional.of(operation));

        BigDecimal result = operationService.executeOperation(operationDTO);

        assertEquals(expectedResult.setScale(2), result.setScale(2));
        assertEquals(new BigDecimal("95.00").setScale(2), user.getBalance().setScale(2));

        verify(userService, times(1)).findById(userId);
        verify(operationRepository, times(1)).findByType("add");
        verify(userService, times(1)).updateUser(user);
        verify(recordService, times(1)).save(any(), eq(user), eq(result), eq(new BigDecimal("95.00")), anyString());
    }

    @Test
    void testExecuteOperation_InsufficientBalance() {
        Long userId = 1L;
        String expression = "10 - 1";
        BigDecimal currentBalance = new BigDecimal("2.00");
        BigDecimal operationCost = new BigDecimal("5.00");

        User user = new User();
        user.setId(userId);
        user.setBalance(currentBalance);

        Operation operation = new Operation();
        operation.setType("subtract");
        operation.setCost(operationCost);

        OperationDTO operationDTO = new OperationDTO();
        operationDTO.setUserId(userId);
        operationDTO.setExpression(expression);

        when(userService.findById(userId)).thenReturn(user);
        when(operationRepository.findByType("subtract")).thenReturn(Optional.of(operation));

        InsufficientBalanceException exception = assertThrows(InsufficientBalanceException.class, () -> {
            operationService.executeOperation(operationDTO);
        });

        assertEquals("Insufficient balance to carry out the operation.", exception.getMessage());
        verify(userService, times(1)).findById(userId);
        verify(operationRepository, times(1)).findByType("subtract");
    }


    @Test
    void testGenerateRandomString_Success() {
        Long userId = 1L;
        BigDecimal currentBalance = new BigDecimal("100.00");
        BigDecimal operationCost = new BigDecimal("5.00");
        String randomString = "randomString";

        User user = new User();
        user.setId(userId);
        user.setBalance(currentBalance);

        Operation operation = new Operation();
        operation.setType("random-string");
        operation.setCost(operationCost);

        when(userService.findById(userId)).thenReturn(user);
        when(operationRepository.findByType("random-string")).thenReturn(Optional.of(operation));

        String result = operationService.generateRandomString(userId);

        assertNotNull(result);
        assertEquals(new BigDecimal("95.00"), user.getBalance());

        verify(userService, times(1)).findById(userId);
        verify(operationRepository, times(1)).findByType("random-string");
        verify(userService, times(1)).updateUser(user);
        verify(recordService, times(1)).save(any(), eq(user), eq(BigDecimal.ZERO), eq(new BigDecimal("95.00")), anyString());
    }

}
