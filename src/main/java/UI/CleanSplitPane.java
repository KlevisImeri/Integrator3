package UI;
import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.Color;

class CustomSplitPane {
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
        splitPane.setBorder(null);
    }
}
