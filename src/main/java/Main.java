import ExpressionEvaluator.Parser;

public class Main {
  public static void main(String[] args) {
    String expression = "cos(3x)+sin(x^2)+(x^3-x)/(x^2-4)+1/2*x^4+x^2-5.32+x-1/(-x)-(3x)+1-e^x/(x)+e^(x^2)*sin(x)";
    Parser parser = new Parser(expression);
    parser.printTokensInRPN();
  }
}

