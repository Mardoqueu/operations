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


