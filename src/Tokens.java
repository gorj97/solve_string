import java.util.*;

public class Tokens {

    public ArrayList<Token> tokens;
    public Map<String, Double> vars;

    Tokens(String exponential_string){
        this.tokens = new ArrayList<>();
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
            this.tokens.add(new Token(tmp.toString()));
        }
        for (i = 0; i < this.tokens.size(); i++){
            if (Character.isLetter(this.tokens.get(i).value.charAt(0))){
                if (i + 1 < this.tokens.size()){
                    if (this.tokens.get(i + 1).value.charAt(0) == '(')
                        this.tokens.get(i).type = TokenType.ttfunc;
                    else this.tokens.get(i).type = TokenType.ttvar;
                }
                else this.tokens.get(i).type = TokenType.ttvar;
            }
            else if (Character.isDigit(this.tokens.get(i).value.charAt(0)))
                this.tokens.get(i).type = TokenType.ttconst;
            else if (this.tokens.get(i).value.equals("("))
                this.tokens.get(i).type = TokenType.ttopbr;
            else if (this.tokens.get(i).value.equals(")"))
                this.tokens.get(i).type = TokenType.ttclbr;
            else if (this.tokens.get(i).value.equals("-")){
                if (i == 0 || this.tokens.get(i - 1).type == TokenType.ttopbr){
                    this.tokens.add(i, new Token("(", TokenType.ttopbr));
                    this.tokens.add(i + 1, new Token("0", TokenType.ttconst));
                    if (i + 4 >= this.tokens.size()){
                        this.tokens.add(new Token(")", TokenType.ttclbr));
                    }
                    else {
                        this.tokens.add(i + 4, new Token(")", TokenType.ttclbr));
                    }
                }
            }

            if (i != 0 && this.tokens.get(i).value.charAt(0) == '!'){
                switch (this.tokens.get(i - 1).type){
                    case ttop: case ttopbr:
                        this.tokens.get(i).type = TokenType.ttop;
                        this.tokens.get(i).value = "~";
                        break;
                    default:
                        this.tokens.get(i).type = TokenType.ttfunc;
                        break;
                }
            }
            else if (this.tokens.get(i).value.charAt(0) == '!'){
                this.tokens.get(i).type = TokenType.ttop;
                this.tokens.get(i).value = "~";
            }
        }
        this.InitializeVars();
    }

    private void InitializeVars(){
        this.vars = new HashMap<>();
        this.tokens.forEach(t -> {
            if (t.type == TokenType.ttvar && this.vars.get(t.value) == null) {
                this.vars.put(t.value, 1.0);
            }
        });
    }

    private static Map<String, Integer> GetPriorityMap(){
        Map<String, Integer> priority_map = new HashMap<>();
        priority_map.put("-", 10);
        priority_map.put("+", 10);
        priority_map.put("*", 20);
        priority_map.put("/", 20);
        priority_map.put("^", 30);
        priority_map.put("~", 40);
        return priority_map;
    }

    public void TranslationToPostfixFromInfix(){
        ArrayList<Token> result_tokens = new ArrayList<>();
        Stack<Token> stack = new Stack<>();
        Map<String, Integer> priority_map = GetPriorityMap();
        for (Token t : this.tokens) {
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
        this.tokens = result_tokens;
    }

    private static double OperationPlus(Stack<Double> stack){
        return stack.pop() + stack.pop();
    }

    private static double OperationMinus(Stack<Double> stack){
        return - stack.pop() + stack.pop();
    }

    private static double OperationMultiple(Stack<Double> stack){
        return stack.pop() * stack.pop();
    }

    private static double OperationDivision(Stack<Double> stack){
        double tmp = stack.pop();
        return stack.pop() / tmp;
    }

    private static double OperationSin(Stack<Double> stack){
        return Math.sin(stack.pop());
    }

    private static double OperationCos(Stack<Double> stack){
        return Math.cos(stack.pop());
    }

    private static double OperationPow(Stack<Double> stack){
        double tmp = stack.pop();
        return Math.pow(stack.pop(), tmp);
    }

    private static double OperationFactorial(Stack<Double> stack){
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

    private static double OperationNot(Stack<Double> stack){
        double x = stack.pop();
        x = x != 0 ? 0 : 1;
        return x;
    }

    private static double doOperation(String operation, Stack<Double> stack) throws Exception {
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

    public double CalculatePostfix() throws Exception {
        Stack<Double> stack = new Stack<>();
        for (Token t : this.tokens){
            switch (t.type){
                case ttconst:
                    stack.push(Double.valueOf(t.value));
                    break;
                case ttvar:
                    stack.push(this.vars.get(t.value));
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
        if (stack.size() > 1){
            throw new Exception("Есть лишние операнды");
        }
        return stack.peek();
    }

    public boolean CheckExpressionTokens(){
        int count_open_br = 0, count_close_br = 0;
        for (Token t : tokens){
            if (t.type == TokenType.ttopbr)
                count_open_br++;
            if (t.type == TokenType.ttclbr)
                count_close_br++;
        }
        return count_open_br == count_close_br;
    }
}
