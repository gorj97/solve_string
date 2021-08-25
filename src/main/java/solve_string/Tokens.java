package solve_string;

import solve_string.operation.OperationHolder;

import java.util.*;

public class Tokens {

    public ArrayList<Token> tokens;
    public Map<String, Double> vars;

    /**
     * @param exponentialString a string representing a mathematical expression
     * @param in                scanner for reading variable values from the keyboard
     * @throws Exception artificially throwing an exception to report an error in an expression
     */
    public Tokens(String exponentialString, Scanner in) throws Exception {
        this.tokens = new ArrayList<>();
        StringBuilder tokenName;
        int i = 0;
        char[] exponential = exponentialString.toCharArray();
        while (i < exponential.length) {
            if (exponential[i] == ' ') {
                i++;
                continue;
            }
            tokenName = new StringBuilder();
            if (!Character.isDigit(exponential[i]) && !Character.isLetter(exponential[i]) && exponential[i] != '.') {
                tokenName.append(exponential[i++]);
            } else {
                while (i < exponential.length && (Character.isDigit(exponential[i]) ||
                        Character.isLetter(exponential[i]) || exponential[i] == '.')) {
                    tokenName.append(exponential[i++]);
                }
            }
            this.tokens.add(new Token(tokenName.toString(),
                    Token.getType(this.tokens.size() == 0 ? null : this.tokens.get(this.tokens.size() - 1),
                            tokenName.toString())));
        }
        if (!this.checkEqualsBrackets()) {
            throw new Exception("The amount of brackets of different types does not match");
        }
        this.initializeVars(in);
    }

    public void initializeVars(Scanner in) {
        this.vars = new HashMap<>();
        this.tokens.forEach(token -> {
            if (token.type == TokenType.variable && this.vars.get(token.name) == null) {
                System.out.print(token.name + " = ");
                double varValue = in.nextDouble();
                this.vars.put(token.name, varValue);
            }
        });
    }

    /**
     * Transform infix expression to postfix
     */
    public void translationToPostfixFromInfix() {
        ArrayList<Token> resultTokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        OperationHolder operationHolder = new OperationHolder();
        for (Token token : this.tokens) {
            switch (token.type) {
                case constant:
                case variable:
                    resultTokens.add(token);
                    break;
                case openBracket:
                case unaryOperation:
                    stack.push(token);
                    break;
                case closeBracket:
                    while (stack.size() != 0 && stack.peek().type != TokenType.openBracket) {
                        resultTokens.add(stack.pop());
                    }
                    stack.pop();
                    if (stack.size() != 0 && stack.peek().type == TokenType.unaryOperation) {
                        resultTokens.add(stack.pop());
                    }
                    break;
                case binaryOperation:
                    while (stack.size() != 0 &&
                            (stack.peek().type == TokenType.unaryOperation ||
                                    (stack.peek().type != TokenType.openBracket &&
                                            operationHolder.getBinaryOperation(stack.peek().name).getPriority() >=
                                                    operationHolder.getBinaryOperation(token.name).getPriority()))) {
                        resultTokens.add(stack.pop());
                    }
                    stack.push(token);
                    break;
            }
        }
        while (stack.size() != 0)
            resultTokens.add(stack.pop());
        this.tokens = resultTokens;
    }

    /**
     * Calculating the result of a mathematical expression in postfix form
     *
     * @return result of a mathematical expression
     * @throws Exception artificially throwing an exception to report an error in an expression
     */
    public double calculatePostfix() throws Exception {
        Stack<Double> stack = new Stack<>();
        OperationHolder operationHolder = new OperationHolder();
        for (Token token : this.tokens) {
            switch (token.type) {
                case constant:
                    stack.push(Double.valueOf(token.name));
                    break;
                case variable:
                    stack.push(this.vars.get(token.name));
                    break;
                case binaryOperation:
                    try {
                        double b = stack.pop();
                        double a = stack.pop();
                        stack.push(operationHolder.getBinaryOperation(token.name).executeOperation(a, b));
                    } catch (EmptyStackException e) {
                        throw new Exception("Insufficient operands passed for operation/function");
                    }
                    break;
                case unaryOperation:
                    stack.push(operationHolder.getUnaryOperation(token.name).executeOperation(stack.pop()));
            }
        }
        if (stack.size() > 1) {
            throw new Exception("There are extra operands");
        }
        return stack.peek();
    }

    public boolean checkEqualsBrackets() {
        int countOpenBrackets = 0, countCloseBrackets = 0;
        for (Token token : tokens) {
            if (token.type == TokenType.openBracket) {
                countOpenBrackets++;
            }
            if (token.type == TokenType.closeBracket) {
                countCloseBrackets++;
            }
        }
        return countOpenBrackets == countCloseBrackets;
    }
}
