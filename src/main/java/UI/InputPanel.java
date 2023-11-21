package UI;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class InputPanel extends JPanel {
    private AddButton addButton = new AddButton(this::addExpressionWriter);
    FunctionGrapher grapher;
    Set<Expression> functions;

    public InputPanel(Set<Expression> functions) {
        this.functions = functions;

        setBackground(new Color(24, 25, 26));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(null);

        add(new ButtonPanel(addButton));
    }

    public void addExpressionWriter() {
        add(new ExpressionWriter(functions, this::removeExpressionWriter));
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
