package UI;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class RemoveButton extends JButton {
    ExpressionWriter parent;

    RemoveButton(Consumer<ExpressionWriter> removeExpressionWriter, ExpressionWriter parent) {
        super("X");
        this.parent = parent;
        setBorderPainted(false);
        setForeground(new Color(237, 83, 83));
        setBackground(new Color(15, 15, 15));
        setBorderPainted(false);
        setFocusPainted(false);
        setSizeFont(20);
        addActionListener((e) -> removeExpressionWriter.accept(parent));
        setContentAreaFilled(false);
    }

    public void setSizeFont(int size) {
        Font originalFont = getFont();
        Font largerFont = new Font(originalFont.getName(), originalFont.getStyle(), size);
        setFont(largerFont);
    }

}
