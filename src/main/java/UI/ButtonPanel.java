package UI;

import javax.swing.*;
import java.awt.*;

public class ButtonPanel extends JPanel {

    ButtonPanel(AddButton button) {
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        setLayout(new BorderLayout());
        setBackground(new Color(15, 15, 15));
        add(button, BorderLayout.CENTER);
    }

}
