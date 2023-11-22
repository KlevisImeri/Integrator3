package UI;

import javax.swing.*;

import data.Expression;

import java.awt.*;
import java.util.function.Consumer;

public class AddButton extends JButton{
    
    AddButton(Consumer<Expression> addExpressionWriter) {
        super("+");

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBorder(new RoundedBorder(5));
        setForeground(new Color(157, 219, 110)); 
        setBackground(new Color(15, 15, 15));
        setBorderPainted(false);
        setFocusPainted(false);
        setSizeFont(20);
        setContentAreaFilled(false);
        
        addActionListener((e) -> addExpressionWriter.accept(null));
    }

    public void setSizeFont(int size) {
        Font originalFont = getFont();
        Font largerFont = new Font(originalFont.getName(), originalFont.getStyle(), size); 
        setFont(largerFont);
    }

}
