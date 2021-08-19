import java.util.*;

public class Main {

    public static void main(String [] args) {
        while (true) {
            try {
                Scanner in = new Scanner(System.in);
                System.out.println("Введите выражение: ");
                String string = in.nextLine();
                ArrayList<Token> tokens = GetTokensFromExpression(string);

                if (!CheckExpressionTokens(tokens)) {
                    throw new Exception("Количество скобок разного типа не совпадает");
                }

//                tokens.forEach(Token::printTokenValue);
//                System.out.println();

                tokens = GetPostfixFromTokens(tokens);

//                tokens.forEach(Token::printTokenValue);
//                System.out.println();

                Map<String, Double> vars = new HashMap<>();
                tokens.forEach(t -> {
                    if (t.type == TokenType.ttvar && vars.get(t.value) == null) {
                        System.out.print(t.value + " = ");
                        double value_var = in.nextDouble();
                        vars.put(t.value, value_var);
                    }
                });

                double result = CalculatePostfix(tokens, vars);

                System.out.println(result);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean CheckExpressionTokens(ArrayList<Token> tokens){
        int count_open_br = 0, count_close_br = 0;
        for (Token t : tokens){
            if (t.type == TokenType.ttopbr)
                count_open_br++;
            if (t.type == TokenType.ttclbr)
                count_close_br++;
        }
        return count_open_br == count_close_br;
    }

    public static ArrayList<Token> GetTokensFromExpression(String exponential_string){
        ArrayList<Token> result_tokens = new ArrayList<>();
        StringBuilder tmp;
        int i = 0;
        char [] exp = exponential_string.toCharArray();
        while (i < exp.length){
            if (exp[i] == ' ') {
                i++;
                continue;
            }
            tmp = new StringBuilder();
            if (!Character.isDigit(exp[i]) && !Character.isLetter(exp[i]) && exp[i] != '.'){
                tmp.append(exp[i++]);
            }
            else {
               while (i < exp.length && (Character.isDigit(exp[i]) || Character.isLetter(exp[i]) || exp[i] == '.')){
                   tmp.append(exp[i++]);
               }
            }
            result_tokens.add(new Token(tmp.toString()));
        }
        for (i = 0; i < result_tokens.size(); i++){
            if (Character.isLetter(result_tokens.get(i).value.charAt(0))){
                if (i + 1 < result_tokens.size()){
                    if (result_tokens.get(i + 1).value.charAt(0) == '(')
                        result_tokens.get(i).type = TokenType.ttfunc;
                    else result_tokens.get(i).type = TokenType.ttvar;
                }
                else result_tokens.get(i).type = TokenType.ttvar;
            }
            else if (Character.isDigit(result_tokens.get(i).value.charAt(0)))
                result_tokens.get(i).type = TokenType.ttconst;
            else if (result_tokens.get(i).value.equals("("))
                result_tokens.get(i).type = TokenType.ttopbr;
            else if (result_tokens.get(i).value.equals(")"))
                result_tokens.get(i).type = TokenType.ttclbr;
            else if (result_tokens.get(i).value.equals("-")){
                if (i == 0 || result_tokens.get(i - 1).type == TokenType.ttopbr){
                    result_tokens.add(i, new Token("(", TokenType.ttopbr));
                    result_tokens.add(i + 1, new Token("0", TokenType.ttconst));
                    if (i + 4 >= result_tokens.size()){
                        result_tokens.add(new Token(")", TokenType.ttclbr));
                    }
                    else {
                        result_tokens.add(i + 4, new Token(")", TokenType.ttclbr));
                    }
                }
            }

            if (i != 0 && result_tokens.get(i).value.charAt(0) == '!'){
                switch (result_tokens.get(i - 1).type){
                    case ttop: case ttopbr:
                        result_tokens.get(i).type = TokenType.ttop;
                        result_tokens.get(i).value = "~";
                        break;
                    default:
                        result_tokens.get(i).type = TokenType.ttfunc;
                        break;
                }
            }
            else if (result_tokens.get(i).value.charAt(0) == '!'){
                result_tokens.get(i).type = TokenType.ttop;
                result_tokens.get(i).value = "~";
            }
        }
        return result_tokens;
    }

    public static ArrayList<Token> GetPostfixFromTokens(ArrayList<Token> tokens){
        ArrayList<Token> result_tokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        Map<String, Integer> priority_map = GetPriorityMap();
        for (Token t : tokens) {
            switch (t.type){
                case ttconst: case ttvar:
                    result_tokens.add(t);
                    break;
                case ttopbr: case ttfunc:
                    stack.push(t);
                    break;
                case ttclbr:
                    while (stack.size() != 0 && stack.peek().type != TokenType.ttopbr){
                        result_tokens.add(stack.pop());
                    }
                    stack.pop();
                    if (stack.size() != 0 && stack.peek().type == TokenType.ttfunc){
                        result_tokens.add(stack.pop());
                    }
                    break;
                case ttop:
                    while (stack.size() != 0 &&
                            (stack.peek().type != TokenType.ttfunc &&
                            stack.peek().type != TokenType.ttopbr &&
                            priority_map.get(stack.peek().value) >= priority_map.get(t.value))) {
                            result_tokens.add(stack.pop());
                    }
                    stack.push(t);
                    break;
            }
        }
        while (stack.size() != 0)
            result_tokens.add(stack.pop());
        return result_tokens;
    }

    public static Map<String, Integer> GetPriorityMap(){
        Map<String, Integer> priority_map = new HashMap<>();
        priority_map.put("-", 10);
        priority_map.put("+", 10);
        priority_map.put("*", 20);
        priority_map.put("/", 20);
        priority_map.put("^", 30);
        priority_map.put("~", 40);
        return priority_map;
    }

    public static double OperationPlus(Stack<Double> stack){
        return stack.pop() + stack.pop();
    }

    public static double OperationMinus(Stack<Double> stack){
        return - stack.pop() + stack.pop();
    }

    public static double OperationMultiple(Stack<Double> stack){
        return stack.pop() * stack.pop();
    }

    public static double OperationDivision(Stack<Double> stack){
        double tmp = stack.pop();
        return stack.pop() / tmp;
    }

    public static double OperationSin(Stack<Double> stack){
        return Math.sin(stack.pop());
    }

    public static double OperationCos(Stack<Double> stack){
        return Math.cos(stack.pop());
    }

    public static double OperationPow(Stack<Double> stack){
        double tmp = stack.pop();
        return Math.pow(stack.pop(), tmp);
    }

    public static double OperationFactorial(Stack<Double> stack){
        double x = stack.pop();
        double result = 1;
        x = Math.round(x);
        if (x == 0) return result;
        while (x > 0){
            result *= x;
            x--;
        }
        return result;
    }

    public static double OperationNot(Stack<Double> stack){
        double x = stack.pop();
        x = x != 0 ? 0 : 1;
        return x;
    }

    public static double doOperation(String operation, Stack<Double> stack) throws Exception {
        double result;
        switch (operation){
            case "+":
                result = OperationPlus(stack);
                break;
            case "-":
                result = OperationMinus(stack);
                break;
            case "!":
                result = OperationFactorial(stack);
                break;
            case "~":
                result = OperationNot(stack);
                break;
            case "*":
                result = OperationMultiple(stack);
                break;
            case "/":
                result = OperationDivision(stack);
                break;
            case "sin":
                result = OperationSin(stack);
                break;
            case "cos":
                result = OperationCos(stack);
                break;
            case "^":
                result = OperationPow(stack);
                break;
            default:
                throw new Exception("Не опознанный символ");
        }
        return result;
    }

    public static double CalculatePostfix(ArrayList<Token> tokens, Map<String, Double> vars) throws Exception {
        Stack<Double> stack = new Stack<>();
        for (Token t : tokens){
            switch (t.type){
                case ttconst:
                    stack.push(Double.valueOf(t.value));
                    break;
                case ttvar:
                    stack.push(vars.get(t.value));
                    break;
                case ttop: case ttfunc:
                    try {
                        stack.push(doOperation(t.value, stack));
                    }
                    catch (EmptyStackException e){
                        throw new Exception("Переданно недостаточно операндов для операции/функции");
                    }
                    break;
            }
        }
        return stack.peek();
    }

}
