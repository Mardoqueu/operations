package com.challenge.operations.service;

import com.challenge.operations.entity.Operation;
import com.challenge.operations.entity.User;
import com.challenge.operations.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.challenge.operations.entity.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service to handle operations related to records in the application.
 */
@Service
public class RecordService {

    /**
     * The RecordRepository instance that provides CRUD operations and custom
     * queries for Record entities. This repository is essential for saving,
     * retrieving, and managing record data in the application.
     */
    @Autowired
    private RecordRepository recordRepository;

    /**
     * Saves a new record in the repository with details about the operation, user,
     * amount involved, user's balance after the operation, and the operation's response.
     *
     * @param operation the operation being recorded; must not be null.
     * @param user the user who performed the operation; must not be null.
     * @param amount the amount involved in the operation; can be null.
     * @param userBalance the balance of the user after the operation; can be null.
     * @param response the response or result of the operation; can be null.
     * @throws IllegalArgumentException if the operation or user is null.
     */
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

    /**
     * Retrieves a list of records associated with a specific user.
     *
     * @param userId the ID of the user whose records are to be retrieved
     * @return a list of records associated with the specified user
     */
    public List<Record> listRecordsByUser(Long userId) {
        return recordRepository.findByUserId(userId);
    }

}

