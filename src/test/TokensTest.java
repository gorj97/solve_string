package test;

import com.example.Token;
import com.example.Tokens;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.*;

public class TokensTest {

    private static class TestCase {
        String testData;
        String postfixTestData;
        double calculateTestData;
        String exceptionMessage = null;

        TestCase(String testData, String postfixTestData, double calculateTestData) {
            this.testData = testData;
            this.postfixTestData = postfixTestData;
            this.calculateTestData = calculateTestData;
        }

        TestCase(String testData, String postfixTestData, String exceptionMessage) {
            this.testData = testData;
            this.postfixTestData = postfixTestData;
            this.exceptionMessage = exceptionMessage;
        }

        TestCase(String testData, String exceptionMessage) {
            this.testData = testData;
            this.exceptionMessage = exceptionMessage;
        }
    }

    private static final ArrayList<TestCase> testCases = new ArrayList<>();
    private static final Scanner in = new Scanner(System.in);

    @Before
    public void setUp() {
        testCases.add(new TestCase("2", "2", 2.0));
        testCases.add(new TestCase("3+2", "32+", 5.0));
        testCases.add(new TestCase("2.3 + 1.7", "2.31.7+", 4.0));
        testCases.add(new TestCase("4 - 2", "42-", 2.0));
        testCases.add(new TestCase("2*2", "22*", 4.0));
        testCases.add(new TestCase("8/2", "82/", 4.0));
        testCases.add(new TestCase("!5", "5!", 0.0));
        testCases.add(new TestCase("!0", "0!", 1.0));
        testCases.add(new TestCase("-2", "2-", -2.0));
        testCases.add(new TestCase("fact(5)", "5fact", 120.0));
        testCases.add(new TestCase("cos(0)", "0cos", 1.0));
        testCases.add(new TestCase("sin(0)", "0sin", 0.0));
        testCases.add(new TestCase("(2+3)/2", "23+2/", 2.5));
        testCases.add(new TestCase("-fact(5+1)", "51+fact-", -720.0));
        testCases.add(new TestCase("(((-1-5)/3)+2)", "1-5-3/2+", 0.0));
        testCases.add(new TestCase("((fact(5)-(50+50)*cos(0))/4)^2", "5fact5050+0cos*-4/2^", 25.0));
        testCases.add(new TestCase("(4+2", "The amount of brackets of different types does not match"));
        testCases.add(new TestCase("2+(4-3 * 4))", "The amount of brackets of different types does not match"));
        testCases.add(new TestCase("(", "The amount of brackets of different types does not match"));
        testCases.add(new TestCase("*3", "3*", "Insufficient operands passed for operation/function"));
        testCases.add(new TestCase("+", "+", "Insufficient operands passed for operation/function"));
        testCases.add(new TestCase("-", "-", null));
        testCases.add(new TestCase("2-3 4", "234-", "There are extra operands"));
    }

    @After
    public void tearDown() {
        testCases.clear();
    }

    @Test
    public void translationToPostfixFromInfixTest() {
        for (TestCase testCase : testCases) {
            try {
                final Tokens testData = new Tokens(testCase.testData, in);
                final String expected = testCase.postfixTestData;
                testData.translationToPostfixFromInfix();
                StringBuilder actual = new StringBuilder();
                for (Token token : testData.tokens) {
                    actual.append(token.name);
                }
                assertEquals(expected, actual.toString());
            } catch (Exception e) {
                final String expected = testCase.exceptionMessage;
                final String actual = e.getMessage();
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void calculatePostfix() {
        for (TestCase testCase : testCases) {
            try {
                final Tokens testData = new Tokens(testCase.testData, in);
                testData.translationToPostfixFromInfix();
                final double expected = testCase.calculateTestData;
                final double actual = testData.calculatePostfix();
                assertEquals(expected, actual, 0.0);
            } catch (Exception e) {
                final String expected = testCase.exceptionMessage;
                final String actual = e.getMessage();
                assertEquals(expected, actual);
            }
        }
    }
}