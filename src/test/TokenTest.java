package test;

import com.example.Token;
import com.example.TokenType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TokenTest {

    private static class TestCase {
        Token prevToken;
        String currentTokenName;
        TokenType expected;

        TestCase(Token prevToken, String currentTokenName, TokenType expected) {
            this.prevToken = prevToken;
            this.currentTokenName = currentTokenName;
            this.expected = expected;
        }
    }

    private static final ArrayList<TestCase> testCases = new ArrayList<>();

    @Before
    public void setUp() {
        testCases.add(new TestCase(new Token("-", TokenType.unaryOperation), "5", TokenType.constant));
        testCases.add(new TestCase(new Token("-", TokenType.binaryOperation), "10", TokenType.constant));
        testCases.add(new TestCase(new Token("*", TokenType.binaryOperation), "(", TokenType.openBracket));
        testCases.add(new TestCase(new Token("(", TokenType.openBracket), "(", TokenType.openBracket));
        testCases.add(new TestCase(new Token("5", TokenType.constant), "-", TokenType.binaryOperation));
        testCases.add(new TestCase(new Token("-", TokenType.unaryOperation), "xyz", TokenType.variable));
        testCases.add(new TestCase(null, "-", TokenType.unaryOperation));
        testCases.add(new TestCase(null, "()", TokenType.binaryOperation));
        testCases.add(new TestCase(null, "xyz", TokenType.variable));
        testCases.add(new TestCase(new Token("fact", TokenType.unaryOperation), "(", TokenType.openBracket));
        testCases.add(new TestCase(new Token(")", TokenType.closeBracket), "*", TokenType.binaryOperation));
        testCases.add(new TestCase(new Token("fact", TokenType.unaryOperation), "cos", TokenType.unaryOperation));
        testCases.add(new TestCase(new Token(")", TokenType.closeBracket), ")", TokenType.closeBracket));
    }

    @After
    public void tearDown() {
        testCases.clear();
    }

    @Test
    public void getType() {
        for (TestCase testCase : testCases) {
            TokenType actual = Token.getType(testCase.prevToken, testCase.currentTokenName);
            assertEquals(testCase.expected, actual);
        }
    }
}