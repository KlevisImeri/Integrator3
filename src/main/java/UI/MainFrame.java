package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import data.Controller;

public class MainFrame extends JFrame {
    Controller controller;
    FunctionGrapher fGrapher;
    InputPanel inputPanel;
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, fGrapher);

    public MainFrame(Controller controller, int WIDTH, int HEIGHT) {
        super("Function Grapher");

        this.controller = controller;
        this.fGrapher = new FunctionGrapher(controller);
        this.inputPanel = new InputPanel(controller);
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, fGrapher);
        
        controller.addCallbackRun(this.fGrapher::repaint);
        controller.addCallbackReload(this.inputPanel::update);
        
        setCleanSplitUI(splitPane);
        add(splitPane, BorderLayout.CENTER);
        setJMenuBar(new Menu(controller));

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    static void setCleanSplitUI(JSplitPane splitPane) {
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
        splitPane.setResizeWeight(0.23);
        splitPane.setBackground(new Color(15, 15, 15));
        splitPane.setUI(splitPaneUI);
    }
}
