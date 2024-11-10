package com.challenge.operations.dto;

/**
 * Data Transfer Object for representing an operation to be executed.
 */
public class OperationDTO {

    private Long userId;
    private String expression;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}

