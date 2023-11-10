package UI;

import java.awt.Color;
import ExpressionEvaluator.Parser;

public class Function {
    public Parser parser = new Parser();
    public Color color = new Color(255,139,142);

    public Function() {}

    public Function(Color color) {
        parser = new Parser();
        this.color = color;
    }

    public Function(String expression, Color color) throws Exception {
        this.parser = new Parser(expression);
        this.color = color;
    }

    public void editFunction(String expression) throws Exception {
        parser.parse(expression);
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
