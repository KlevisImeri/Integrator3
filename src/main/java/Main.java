import UI.MainFrame;
import data.JSONHashSet;
import data.Expression;
import data.Controller;

public class Main {
  private static final int WIDTH = 1500;
  private static final int HEIGHT = 1000;

  public static void main(String[] args) {
    JSONHashSet<Expression> functions = new JSONHashSet<>(Expression.class);
    Controller controler = new Controller(functions);
    MainFrame frame = new MainFrame(controler, WIDTH, HEIGHT);
  }
}