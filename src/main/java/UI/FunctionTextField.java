package UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import java.lang.Runnable;
import java.awt.*;

public class FunctionTextField extends JTextField {
    private Color darkmodeBackgroundColor = new Color(24, 25, 26);  
    static Color redMistakeColor = new Color(237, 83, 83);
    static Color greenGoodExpressionColor = new Color(157, 219, 110);
    static LineBorder goodLineBorder = new LineBorder(greenGoodExpressionColor, 1);
    static LineBorder badLineBorder = new LineBorder(redMistakeColor, 1);
    Runnable updateFunction;

    FunctionTextField(Runnable updateFunction) {
        super(20);

        this.updateFunction = updateFunction;

        setBackground(darkmodeBackgroundColor);
        setForeground(Color.WHITE);
        setFont(getFont().deriveFont(18f));
        addStringListener();
    }

    private void addStringListener() {
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFunction.run();
            }
        
            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFunction.run();
            }
        
            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFunction.run();
            }
        });        
    }
}
