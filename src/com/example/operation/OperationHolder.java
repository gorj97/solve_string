package com.example.operation;

import com.example.operation.BinaryOperation;
import com.example.operation.UnaryOperation;

import java.util.HashMap;
import java.util.Map;

public class OperationHolder {

    private final Map<String, BinaryOperation> binaryOperationMap;
    private final Map<String, UnaryOperation> unaryOperationMap;

    public OperationHolder() {
        binaryOperationMap = new HashMap<>();
        binaryOperationMap.put("-", new BinaryOperation("-", 10, (a, b) -> a - b));
        binaryOperationMap.put("+", new BinaryOperation("+", 10, (a, b) -> a + b));
        binaryOperationMap.put("*", new BinaryOperation("*", 20, (a, b) -> a * b));
        binaryOperationMap.put("/", new BinaryOperation("/", 20, (a, b) -> a / b));
        binaryOperationMap.put("^", new BinaryOperation("^", 30, Math::pow));

        unaryOperationMap = new HashMap<>();
        unaryOperationMap.put("cos", new UnaryOperation("cos", 40, Math::cos));
        unaryOperationMap.put("sin", new UnaryOperation("sin", 40, Math::sin));
        unaryOperationMap.put("fact", new UnaryOperation("fact", 40, a -> {
            double result = 1;
            long x = Math.round(a);
            if (x == 0) return result;
            while (x > 0) {
                result *= x;
                x--;
            }
            return result;
        }));
        unaryOperationMap.put("-", new UnaryOperation("-", 50, a -> -a));
        unaryOperationMap.put("!", new UnaryOperation("!", 50, a -> a != 0 ? 0.0 : 1.0));
    }

    public BinaryOperation getBinaryOperation(String operationSymbol) {
        return binaryOperationMap.get(operationSymbol);
    }

    public UnaryOperation getUnaryOperation(String operationSymbol) {
        return unaryOperationMap.get(operationSymbol);
    }
}
