import java.util.*;

public class Main {

    public static void main(String [] args) {
        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Введите выражение: ");
                String string = in.nextLine();
                Tokens tokens = new Tokens(string);

                if (!tokens.CheckExpressionTokens()) {
                    throw new Exception("Количество скобок разного типа не совпадает");
                }

                tokens.TranslationToPostfixFromInfix();

                for (var v : tokens.vars.entrySet()) {
                    System.out.print(v.getKey() + " = ");
                    double tmp = in.nextDouble();
                    v.setValue(tmp);
                }

                double result = tokens.CalculatePostfix();

                System.out.println(result);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}
