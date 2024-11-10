package com.challenge.operations.util;

import com.challenge.operations.exception.InvalidExpressionException;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.StringTokenizer;

public class ExpressionEvaluator {

    // Defines the precedence of operators
    private int precedence(String operator) {
        return switch (operator) {
            case "+", "-" -> 1;
            case "*", "/" -> 2;
            case "sqrt" -> 3;
            default -> -1;
        };
    }

    // Converts to postfix notation (RPN) using the Shunting-yard algorithm
    private String toPostfix(String expression) {
        Stack<String> stack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(expression, "+-*/() ", true);
        boolean lastTokenWasOperator = true; // Helps detect consecutive operators

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.isEmpty()) continue;

            if (token.matches("\\d+(\\.\\d+)?")) {  // Check if it is a number
                postfix.append(token).append(" ");
                lastTokenWasOperator = false;
            } else if (token.equals("sqrt")) {  // Check if it is the sqrt function
                stack.push("sqrt");
                lastTokenWasOperator = true;
            } else if (token.matches("[+\\-*/]")) {  // Checks if it is an operator
                if (lastTokenWasOperator) {
                    throw new InvalidExpressionException("Invalid expression: consecutive operators.");
                }
                while (!stack.isEmpty() && precedence(stack.peek()) >= precedence(token)) {
                    postfix.append(stack.pop()).append(" ");
                }
                stack.push(token);
                lastTokenWasOperator = true;
            } else if (token.equals("(")) {  // Checks if it is an open parenthesis
                stack.push("(");
                lastTokenWasOperator = true;
            } else if (token.equals(")")) {  // Checks if it is a closed parenthesis
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop()).append(" ");
                }
                if (stack.isEmpty() || !stack.pop().equals("(")) {
                    throw new InvalidExpressionException("Invalid expression: mismatched parentheses.");
                }
                if (!stack.isEmpty() && stack.peek().equals("sqrt")) {
                    postfix.append(stack.pop()).append(" ");
                }
                lastTokenWasOperator = false;
            } else {
                throw new InvalidExpressionException("Invalid token: " + token);
            }
        }

        if (lastTokenWasOperator) {
            throw new InvalidExpressionException("Invalid expression: ends with an operator.");
        }

        while (!stack.isEmpty()) {  // Remove remaining operators
            String op = stack.pop();
            if (op.equals("(") || op.equals(")")) {
                throw new InvalidExpressionException("Invalid expression: mismatched parentheses.");
            }
            postfix.append(op).append(" ");
        }

        return postfix.toString();
    }


    // Evaluates postfix expression
    public double evaluate(String expression) {
        try {
            String postfix = toPostfix(expression);
            Stack<Double> stack = new Stack<>();

            for (String token : postfix.split("\\s")) {
                if (token.matches("\\d+(\\.\\d+)?")) {  // Numbers
                    stack.push(Double.parseDouble(token));
                } else if (token.matches("[+\\-*/]")) {  // Operators
                    if (stack.size() < 2) {
                        throw new InvalidExpressionException("Invalid expression: insufficient values for operator " + token);
                    }
                    double b = stack.pop();
                    double a = stack.pop();
                    switch (token) {
                        case "+" -> stack.push(a + b);
                        case "-" -> stack.push(a - b);
                        case "*" -> stack.push(a * b);
                        case "/" -> {
                            if (b == 0) {  // Check division by zero
                                throw new InvalidExpressionException("Division by zero is not allowed.");
                            }
                            stack.push(a / b);
                        }
                    }
                } else if (token.equals("sqrt")) {  // sqrt function
                    if (stack.isEmpty()) {
                        throw new InvalidExpressionException("Invalid expression: no value for sqrt.");
                    }
                    double a = stack.pop();
                    stack.push(Math.sqrt(a));
                } else {
                    throw new InvalidExpressionException("Invalid token in postfix: " + token);
                }
            }

            if (stack.size() != 1) {
                throw new InvalidExpressionException("Invalid expression: too many values.");
            }

            return stack.pop();
        } catch (EmptyStackException e) {
            throw new InvalidExpressionException("Invalid expression: missing values.");
        }
    }

}
