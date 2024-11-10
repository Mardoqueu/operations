package com.challenge.operations.controller;

import com.challenge.operations.dto.OperationDTO;
import com.challenge.operations.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST controller for handling various operations such as executing mathematical expressions and generating random strings.
 */
@RestController
@RequestMapping("/api/operations")
public class OperationController {

    /**
     * Autowired instance of OperationService. This service handles the business logic related to executing
     * mathematical operations and generating random strings. It works with UserService, RecordService,
     * and OperationRepository to manage user operations, balance updates, and operation records.
     */
    @Autowired
    private OperationService operationService;

    /**
     * Executes an operation based on the provided {@link OperationDTO} and returns the result.
     *
     * @param operationDTO the data transfer object containing information about the operation to be executed,
     *                     including user ID and the mathematical expression.
     * @return a ResponseEntity containing the result of the operation as a BigDecimal.
     */
    @PostMapping("/execute")
    public ResponseEntity<BigDecimal> executeOperation(@RequestBody OperationDTO operationDTO) {
        return ResponseEntity.ok(operationService.executeOperation(operationDTO));
    }

    /**
     * Endpoint for generating a random string for a given user.
     *
     * @param userId the ID of the user for whom the random string is to be generated
     * @return a ResponseEntity containing the generated random string
     */
    @PostMapping("/random-string")
    public ResponseEntity<String> randomString(@RequestParam Long userId) {
        String randomString = operationService.generateRandomString(userId);
        return ResponseEntity.ok(randomString);
    }
}
