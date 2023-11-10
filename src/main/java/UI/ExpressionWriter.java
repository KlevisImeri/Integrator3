package UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class ExpressionWriter extends JPanel {
    private JTextField textField;
    private JButton colorButton; // Change to JButton
    private Color selectedColor;
    private FunctionGrapher grapher;
    Function function = new Function();

    public ExpressionWriter(FunctionGrapher grapher) {
        setBackground(new Color(24, 25, 26));
        setLayout(new FlowLayout());

        this.grapher = grapher;

        textField = new JTextField(20);
        textField.setBackground(new Color(24, 25, 26));
        textField.setForeground(Color.WHITE);
        Font customFont = textField.getFont().deriveFont(16f);
        textField.setFont(customFont);
        // Insets textFieldInsets = new Insets(5, 10, 5, 10);
        // textField.setMargin(textFieldInsets);

        colorButton = new JButton(); // Use a JButton
        colorButton.setBorderPainted(false); // Remove border
        colorButton.setPreferredSize(new Dimension(20, 20)); // Set preferred size
        colorButton.setFocusPainted(false); // Remove focus border
        colorButton.setBackground(Color.WHITE); // Set background color
        
        add(colorButton); 
        add(textField);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFunction();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFunction();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFunction();
            }
        });

        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openColorPicker();
            }
        });
    }

    private void openColorPicker() {
        Color currentColor = colorButton.getBackground();
        selectedColor = JColorChooser.showDialog(null, "Choose Color", currentColor);

        if (selectedColor != null) {
            colorButton.setBackground(selectedColor);
            updateFunction();
        }
    }

    private void updateFunction() {
        String expression = textField.getText();
        try {
            function.editFunction(expression);
            function.setColor(selectedColor);
            grapher.addFunction(function);
            textField.setBorder(new LineBorder(new Color(157, 219, 110), 1));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            textField.setBorder(new LineBorder(new Color(237, 83, 83), 1));
        }
        textField.repaint();
    }

    public void close() {
        grapher.removeFunction(function);
    }
}
