public class Token {
    String value;
    TokenType type;

    public void printTokenValue(){
        System.out.print(this.value);
    }

    public void printTypes(){
        System.out.print(this.type);
        System.out.print(' ');
    }

    Token(String value, TokenType type){
        this.value = value;
        this.type = type;
    }

    Token(String value){
        this.value = value;
        this.type = TokenType.ttop;
    }

    Token(){

    }
}

enum TokenType{
    ttvar,
    ttconst,
    ttfunc,
    ttopbr,
    ttclbr,
    ttop
}