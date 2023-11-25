package UI;

import javax.swing.*;
import data.Expression;
import java.awt.*;
import java.util.function.Consumer;
import data.Controller;

public class ExpressionWriter extends JPanel {
    private Controller controller;
    private static Color darkmodeBackgroundColor = new Color(15, 15, 15);

    private FunctionTextField textField = new FunctionTextField(this::updateFunction);
    private ColorButton colorButton = new ColorButton(this::updateFunction);
    private Expression function;
    private RemoveButton removeButton;

    public ExpressionWriter(Controller controller, Consumer<ExpressionWriter> removeExpressionWriter) {
        this(controller, removeExpressionWriter, null);
    }

    public ExpressionWriter(Controller controller, Consumer<ExpressionWriter> removeExpressionWriter, Expression function) {

        this.removeButton = new RemoveButton(removeExpressionWriter, this);
        this.controller = controller;
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBackground(darkmodeBackgroundColor);
        setLayout(new BorderLayout());
        add(colorButton, BorderLayout.WEST);
        add(textField, BorderLayout.CENTER);
        add(removeButton, BorderLayout.EAST);

        if (function != null) {
            this.function = function;
            colorButton.setSelectedColor(controller.getFunctionColor(function));
            //plese first set the color before updateing the textfield
            //because the text field will sent an event to updateFuntion
            //which will change the color back to default
            textField.setText(controller.getFunctionString(function));
        } else {
            this.function = controller.newFunction();
        }
    }

    private void updateFunction() {
        String newExpression = textField.getText();
        controller.setExpression(function, newExpression);
        try {
            controller.updateFuntion(function, newExpression);
            controller.colorFunction(function, colorButton.getSelectedColor());
            controller.addFunction(function);
            textField.setBorder(FunctionTextField.goodLineBorder);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            textField.setBorder(FunctionTextField.badLineBorder);
        }
        textField.repaint();
    }

    public void close() {
        controller.removeFunction(function);
    }
}
