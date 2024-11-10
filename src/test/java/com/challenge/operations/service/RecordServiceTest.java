package com.challenge.operations.service;

import com.challenge.operations.entity.Operation;
import com.challenge.operations.entity.Record;
import com.challenge.operations.entity.User;
import com.challenge.operations.repository.RecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RecordServiceTest {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordService recordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_Success() {
        Operation operation = new Operation();
        User user = new User();
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal userBalance = new BigDecimal("100.00");
        String response = "Operation success";

        recordService.save(operation, user, amount, userBalance, response);

        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void testSave_ThrowsException_WhenOperationIsNull() {
        User user = new User();
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal userBalance = new BigDecimal("100.00");
        String response = "Operation success";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recordService.save(null, user, amount, userBalance, response);
        });

        assertEquals("Operation or user cannot be null.", exception.getMessage());

        verify(recordRepository, never()).save(any());
    }

    @Test
    void testSave_ThrowsException_WhenUserIsNull() {
        Operation operation = new Operation();
        BigDecimal amount = BigDecimal.TEN;
        BigDecimal userBalance = new BigDecimal("100.00");
        String response = "Operation success";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            recordService.save(operation, null, amount, userBalance, response);
        });

        assertEquals("Operation or user cannot be null.", exception.getMessage());

        verify(recordRepository, never()).save(any());
    }

    @Test
    void testListRecordsByUser_Success() {
        Long userId = 1L;

        List<Record> records = new ArrayList<>();
        Record record = new Record();
        records.add(record);
        when(recordRepository.findByUserId(userId)).thenReturn(records);

        List<Record> result = recordService.listRecordsByUser(userId);

        assertNotNull(result);
        assertEquals(1, result.size());

        verify(recordRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testListRecordsByUser_Empty() {
        Long userId = 1L;

        when(recordRepository.findByUserId(userId)).thenReturn(new ArrayList<>());

        List<Record> result = recordService.listRecordsByUser(userId);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(recordRepository, times(1)).findByUserId(userId);
    }
}
