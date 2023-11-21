package UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.function.Consumer;
import java.util.Set;

public class ExpressionWriter extends JPanel {
    private FunctionTextField textField = new FunctionTextField(this::updateFunction);
    private ColorButton colorButton = new ColorButton(this::updateFunction);
    private RemoveButton removeButton;
    Set<Expression> functions;
    Expression function = new Expression();
    private Color darkmodeBackgroundColor = new Color(15, 15, 15);
    private Color redMistakeColor = new Color(237, 83, 83);
    private Color greenGoodExpressionColor = new Color(157, 219, 110);
    private LineBorder goodLineBorder = new LineBorder(greenGoodExpressionColor, 1);
    private LineBorder badLineBorder = new LineBorder(redMistakeColor, 1);

    public ExpressionWriter(Set<Expression> functions, Consumer<ExpressionWriter> removeExpressionWriter) {
        this.removeButton = new RemoveButton(removeExpressionWriter, this);
        this.functions = functions;
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBackground(darkmodeBackgroundColor);
        setLayout(new BorderLayout());
        add(colorButton, BorderLayout.WEST);
        add(textField, BorderLayout.CENTER);
        add(removeButton, BorderLayout.EAST);

    }

    private void updateFunction() {
        String expression = textField.getText();
        try {
            function.editFunction(expression);
            function.setColor(colorButton.getSelectedColor());
            functions.add(function);
            textField.setBorder(goodLineBorder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            textField.setBorder(badLineBorder);
        }
        textField.repaint();
    }

    public void close() {
        functions.remove(function);
    }
}
