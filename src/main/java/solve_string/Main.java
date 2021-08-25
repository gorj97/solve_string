package solve_string;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Enter expression: ");
                String string = in.nextLine();
                Tokens tokens = new Tokens(string, in);

                tokens.translationToPostfixFromInfix();

                double result = tokens.calculatePostfix();

                System.out.println(result);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
