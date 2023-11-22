package UI;

import javax.swing.*;
import java.awt.*;

public class ColorButton extends JButton {
    private Color selectedColor = new Color(250, 207, 207);
    Runnable updateFunction;

    ColorButton(Runnable updateFunction) {
        this.updateFunction = updateFunction;
        setBorderPainted(false); 
        setFocusPainted(false); 
        addColorPickerOpenClickListener();
        setBackground(selectedColor);
    }

    private void openColorPicker() {
        selectedColor = JColorChooser.showDialog(null, "Choose Color", selectedColor);

        if (selectedColor != null) {
            setBackground(selectedColor);
            updateFunction.run();
        }
    }

    public void addColorPickerOpenClickListener() {
        addActionListener((e) -> openColorPicker());
    }

    public Color getSelectedColor() {
        return selectedColor;
    }
    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        setBackground(selectedColor);
    }

}
