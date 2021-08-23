package com.example.operation;

public abstract class Operation {
    private final String operationSymbol;
    private final int priority;

    protected Operation(String operationSymbol, int priority) {
        this.operationSymbol = operationSymbol;
        this.priority = priority;
    }

    public String getOperationSymbol() {
        return operationSymbol;
    }

    public int getPriority() {
        return priority;
    }
}
