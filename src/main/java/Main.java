import UI.MainFrame;

public class Main {
  private static final int WIDTH = 1500;
  private static final int HEIGHT = 1000;

  public static void main(String[] args) {
    MainFrame frame = new MainFrame(WIDTH, HEIGHT);
  }
}


// // String expression = "cos(3x)+sin(x^2)+(x^3-x)/(x^2-4)+1/2*x^4+x^2-5.32+x-1/(-x)-(3x)+1-e^x/(x)+e^(x^2)*sin(x)";
// String expression = "sin(pi)";
// Parser parser = new Parser(expression);
// parser.printTokensInRPN();
// parser.printTree();
// try{
//   System.out.println("Eval: "+ parser.eval(2.0));
// } catch (Exception e) {
//   e.printStackTrace();
// }
