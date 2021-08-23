package com.example;

import com.example.brackets.Bracket;
import com.example.brackets.BracketHolder;
import com.example.operation.OperationHolder;

public class Token {
    public String name;
    public TokenType type;

    public Token(String name, TokenType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public TokenType getTokenType() {
        return this.type;
    }

    /**
     * Determines the type of token based on the previous token
     *
     * @return the token type
     */
    public static TokenType getType(Token prevToken, String currentTokenName) {
        TokenType result = TokenType.binaryOperation;
        OperationHolder operationHolder = new OperationHolder();
        BracketHolder bracketHolder = new BracketHolder();
        Bracket bracket = bracketHolder.getBracket(currentTokenName);
        if (bracket != null) {
            result = bracket.getBracketType();
        } else if (Character.isLetter(currentTokenName.charAt(0))) {
            if (operationHolder.getUnaryOperation(currentTokenName) != null) {
                result = TokenType.unaryOperation;
            } else {
                result = TokenType.variable;
            }
        } else if (Character.isDigit(currentTokenName.charAt(0))) {
            result = TokenType.constant;
        } else if ((prevToken == null || prevToken.type == TokenType.openBracket) &&
                operationHolder.getUnaryOperation(currentTokenName) != null) {
            result = TokenType.unaryOperation;
        }
        return result;
    }
}

