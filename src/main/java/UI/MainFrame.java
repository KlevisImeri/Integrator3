package UI;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class MainFrame extends JFrame {
    RunnableSet<Expression> functions = new RunnableSet<>();
    FunctionGrapher fGrapher = new FunctionGrapher(functions);
    InputPanel inputPanel = new InputPanel(functions);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, fGrapher);

    public MainFrame(int WIDTH, int HEIGHT) {
        super("Function Grapher");

        functions.setToRunFunc(this.fGrapher::repaint);

        setCleanSplitUI(splitPane);
        add(splitPane, BorderLayout.CENTER);

        setJMenuBar(new Menu(functions));
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
