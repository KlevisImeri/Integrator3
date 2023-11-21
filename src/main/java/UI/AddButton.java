package UI;

import javax.swing.*;
import java.awt.*;
import java.lang.Runnable;

public class AddButton extends JButton{
    
    AddButton(Runnable addExpressionWriter) {
        super("+");

        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBorder(new RoundedBorder(5));
        setForeground(new Color(157, 219, 110)); 
        setBackground(new Color(15, 15, 15));
        setBorderPainted(false);
        setFocusPainted(false);
        setSizeFont(20);
        setContentAreaFilled(false);
        
        addActionListener((e) -> addExpressionWriter.run());
    }

    public void setSizeFont(int size) {
        Font originalFont = getFont();
        Font largerFont = new Font(originalFont.getName(), originalFont.getStyle(), size); 
        setFont(largerFont);
    }

}
