package com.challenge.operations.service;

import com.challenge.operations.dto.OperationDTO;
import com.challenge.operations.entity.Operation;
import com.challenge.operations.entity.User;
import com.challenge.operations.exception.InsufficientBalanceException;
import com.challenge.operations.exception.OperationNotFoundException;
import com.challenge.operations.generator.RandomStringGenerator;
import com.challenge.operations.repository.OperationRepository;
import com.challenge.operations.util.ExpressionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

/**
 * Service for handling operations related to mathematical expressions and random string generation.
 *
 * This service leverages user and record services, and operation repository
 * to execute and log operations while managing user balances.
 */
@Service
public class OperationService {

    @Autowired
    private UserService userService;

    @Autowired
    private RecordService recordService;

    @Autowired
    private OperationRepository operationRepository;

    @Value("${randomsize}")
    private int stringLength;

    /**
     * Executes the given operation described by the provided {@link OperationDTO}.
     * The method computes the result of the expression in the operation DTO,
     * deducts the cost of the operation from the user's balance, and records the operation.
     *
     * @param operationDTO the DTO containing details about the operation to be executed,
     *                     including the user ID and the arithmetic expression to evaluate.
     * @return the result of the evaluated expression as a {@link BigDecimal}.
     * @throws IllegalArgumentException if the user associated with the given user ID is not found.
     * @throws InsufficientBalanceException if the user does not have sufficient balance to perform the operation.
     * @throws OperationNotFoundException if the operation type detected from the expression is not recognized.
     */
    public BigDecimal executeOperation(OperationDTO operationDTO) {
        User user = userService.findById(operationDTO.getUserId());

        // Evaluate the expression to get the result
        ExpressionEvaluator evaluator = new ExpressionEvaluator();
        Double result = evaluator.evaluate(operationDTO.getExpression());
        BigDecimal resultado = BigDecimal.valueOf(result);

        // Identify the most cost-intensive type of operation in the expression
        String operationType = detectOperationType(operationDTO.getExpression());

        Operation operation = operationRepository.findByType(operationType)
                .orElseThrow(() -> new OperationNotFoundException("Operation not found."));

        BigDecimal currentBalance = user.getBalance();
        BigDecimal costOperation = operation.getCost();

        if (currentBalance.compareTo(costOperation) < 0) {
            throw new InsufficientBalanceException("Insufficient balance to carry out the operation.");
        }

        // Deducts the cost of the operation from the user's balance
        BigDecimal balanceNew = currentBalance.subtract(costOperation);
        user.setBalance(balanceNew);
        userService.updateUser(user);

        // Saves the operation record
        recordService.save(operation, user, resultado, balanceNew, "Result: " + resultado);

        return resultado;
    }

    /**
     * Detects the type of operation specified in a mathematical expression.
     *
     * This method analyzes the input expression string to determine the type of mathematical
     * operation it represents (e.g., addition, subtraction, multiplication, division, square root).
     *
     * @param expression the mathematical expression to be analyzed.
     * @return a string representing the type of operation detected ("multiply", "divide", "add", "subtract", "sqrt").
     * @throws OperationNotFoundException if the expression does not contain a valid operation.
     */
    private String detectOperationType(String expression) {
        if (expression.contains("*")) {
            return "multiply";
        } else if (expression.contains("/")) {
            return "divide";
        } else if (expression.contains("+")) {
            return "add";
        } else if (expression.contains("-")) {
            return "subtract";
        } else if (expression.contains("sqrt")) {
            return "sqrt";
        } else {
            throw new OperationNotFoundException("Invalid operation in expression.");
        }
    }

    /**
     * Generates a random string for a given user and updates the user's balance after deducting the cost of the operation.
     *
     * @param userId the ID of the user for whom the random string is to be generated
     * @return a randomly generated string
     * @throws IllegalArgumentException if the user or operation is not found
     * @throws InsufficientBalanceException if the user's balance is insufficient to carry out the operation
     */
    public String generateRandomString(Long userId) {

        User user = userService.findById(userId);

        Operation operation = operationRepository.findByType("random-string")
                .orElseThrow(() -> new IllegalArgumentException("Operation not found."));

        BigDecimal currentBalance = user.getBalance();
        BigDecimal costOperation = operation.getCost();

        if (currentBalance.compareTo(costOperation) < 0) {
            throw new InsufficientBalanceException("Insufficient balance to carry out the operation.");
        }

        BigDecimal balanceNew = currentBalance.subtract(costOperation);
        user.setBalance(balanceNew);
        userService.updateUser(user);

        String randomString = RandomStringGenerator.generateRandomString(stringLength);

        recordService.save(operation, user, BigDecimal.ZERO, balanceNew, "Generated random string: " + randomString);

        return randomString;
    }

}


