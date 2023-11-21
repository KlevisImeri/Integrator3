package UI;

import ExpressionEvaluator.Parser;
import java.awt.Color;
import java.io.Serializable; 

public class Expression implements Serializable {
    private static final long serialVersionUID = 1L; 

    Parser parser = new Parser();
    Color color = new Color(255, 139, 142);

    public Expression() {}

    public Expression(Color color) {
        parser = new Parser();
        this.color = color;
    }

    public Expression(String expression, Color color) throws Exception {
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
