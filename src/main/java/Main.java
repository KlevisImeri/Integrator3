import ExpressionEvaluator.Lexer;

public class Main {
  public static void main(String[] args) {
    String expression = "cos(3x)+sin(x^2)+(x^3-x)/(x^2-4)+1/2*x^4+x^2-5.32+x-1/(-x)-(3x)+1-e^x/(x)+e^(x^2)*sin(x)";
    Lexer lex = new Lexer(expression);
    try {
      lex.tokenize();
    } catch (Exception e){
      e.printStackTrace();
    }
    lex.print();
  }
}

