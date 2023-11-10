package UI;

import java.awt.BorderLayout;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.border.Border; 
import java.awt.Color;

public class MainFrame extends JFrame { 

    public MainFrame(int WIDTH, int HEIGHT) {
        super("Function Grapher");

        FunctionGrapher fGrapher = new FunctionGrapher();
        InputPanel inputPanel = new InputPanel(fGrapher);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, fGrapher);
        splitPane.setResizeWeight(0.23); 
        splitPane.setBackground(new Color(15, 15, 15));
        

        // Create a custom UI for the slider
        BasicSplitPaneUI splitPaneUI = new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void setBorder(Border b) {
                        // Remove the border to make it look cleaner
                    }
                };
            }
        };

        // Set the custom UI to the splitPane
        splitPane.setUI(splitPaneUI);


        add(splitPane, BorderLayout.CENTER);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setVisible(true);
    }
}
