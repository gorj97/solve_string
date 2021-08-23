package com.example.brackets;

import com.example.TokenType;

public class Bracket {
    private final String bracketSymbol;
    private final TokenType bracketType;

    public Bracket(String bracketSymbol, TokenType bracketType) {
        this.bracketSymbol = bracketSymbol;
        this.bracketType = bracketType;
    }

    public TokenType getBracketType() {
        return bracketType;
    }

    public String getBracketSymbol() {
        return bracketSymbol;
    }
}
