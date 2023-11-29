package UI;

import javax.swing.*;

import data.Controller;
import data.Expression;
import java.awt.*;

public class InputPanel extends JPanel {
    Controller controller;
    private AddButton addButton = new AddButton(this::addExpressionWriter);

    public InputPanel(Controller controller) {
        this.controller = controller;

        setBackground(new Color(24, 25, 26));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);

        add(new ButtonPanel(addButton));
    }

        
    public void update() {
        removeAll();
        add(new ButtonPanel(addButton));

        for (Expression function : controller.getFunctions()) {            
            addExpressionWriter(function);
        }
    }
    
    
    public void addExpressionWriter(Expression function) {
        add(new ExpressionWriter(controller, this::removeExpressionWriter, function));
        revalidate();
        repaint();
    }

    public void removeExpressionWriter(ExpressionWriter expressionWriter) {
        expressionWriter.close();
        remove(expressionWriter);
        revalidate();
        repaint();
    }
}
