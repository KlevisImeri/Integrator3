package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel {
    private JButton addButton;
    private JPanel expressionWritersPanel;
    FunctionGrapher grapher;

    public InputPanel(FunctionGrapher grapher) {
        this.grapher = grapher;

        setBackground(new Color(24, 25, 26));
        setLayout(new BorderLayout());
        setBorder(null);

        JButton addButton = new JButton("+");
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setFocusPainted(false);
        addButton.setForeground(new Color(157, 219, 110)); 
        Font originalFont = addButton.getFont();
        Font largerFont = new Font(originalFont.getName(), originalFont.getStyle(), 20); 
        addButton.setFont(largerFont);

        expressionWritersPanel = new JPanel();
        expressionWritersPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Zero gaps
        expressionWritersPanel.setBackground(new Color(24, 25, 26));
        expressionWritersPanel.setBorder(null);

        add(addButton, BorderLayout.NORTH);
        add(expressionWritersPanel, BorderLayout.CENTER);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addExpressionWriter();
            }
        });
    }

    private void addExpressionWriter() {
        ExpressionWriter expressionWriter = new ExpressionWriter(grapher);

        expressionWriter.setMaximumSize(new Dimension(300, 50)); // Set a maximum size

        JButton removeButton = new JButton("X");
        removeButton.setBorderPainted(false);
        removeButton.setContentAreaFilled(false);
        removeButton.setFocusPainted(false);
        removeButton.setForeground(new Color(237, 83, 83));
        Font originalFont = removeButton.getFont();
        Font largerFont = new Font(originalFont.getName(), originalFont.getStyle(), 20); 
        removeButton.setFont(largerFont);
        

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeExpressionWriter(expressionWriter);
            }
        });

        JPanel expressionWriterPanel = new JPanel();
        expressionWriterPanel.setBackground(new Color(24, 25, 26));
        expressionWriterPanel.setLayout(new FlowLayout()); // Use FlowLayout
        expressionWriterPanel.add(expressionWriter);
        expressionWriterPanel.add(removeButton);
        expressionWriter.setBorder(null);

        expressionWritersPanel.add(expressionWriterPanel);
        expressionWritersPanel.revalidate();
        expressionWritersPanel.repaint();
    }

    private void removeExpressionWriter(ExpressionWriter expressionWriter) {
        expressionWritersPanel.remove(expressionWriter.getParent());
        expressionWriter.close();
        expressionWritersPanel.revalidate();
        expressionWritersPanel.repaint();
    }
}
