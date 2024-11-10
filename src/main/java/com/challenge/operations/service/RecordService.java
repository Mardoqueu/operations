package com.challenge.operations.service;

import com.challenge.operations.entity.Operation;
import com.challenge.operations.entity.User;
import com.challenge.operations.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.challenge.operations.entity.Record;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public void save(Operation operation, User user, BigDecimal amount, BigDecimal userBalance, String response) {
        if (operation == null || user == null) {
            throw new IllegalArgumentException("Operation or user cannot be null.");
        }

        Record record = new Record();
        record.setOperation(operation);
        record.setUser(user);
        record.setAmount(amount);
        record.setUserBalance(userBalance);
        record.setOperationResponse(response);

        recordRepository.save(record);
    }

    public List<Record> listRecordsByUser(Long userId) {
        return recordRepository.findByUserId(userId);
    }

}

