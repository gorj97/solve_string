package com.example.brackets;

import com.example.TokenType;

import java.util.HashMap;
import java.util.Map;

public class BracketHolder {

    private final Map<String, Bracket> bracketMap;

    public BracketHolder() {
        bracketMap = new HashMap<>();
        bracketMap.put("(", new Bracket("(", TokenType.openBracket));
        bracketMap.put(")", new Bracket(")", TokenType.closeBracket));
    }

    public Bracket getBracket(String bracketSymbol) {
        return bracketMap.get(bracketSymbol);
    }
}
