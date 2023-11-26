package UI;


import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.io.*;
import data.Controller;


public class Menu extends JMenuBar {
    Controller controller;
    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");
    JMenuItem saveMenuItem = new JMenuItem("Save");
    private File file;
    private static final Color LIGHTBLACK = new Color(24, 25, 26);
    private static final Color BLACK = new Color(15, 15, 15);

    Menu(Controller controller) {
        
        this.controller = controller;
        openMenuItem.addActionListener(e -> openFile());
        saveMenuItem.addActionListener(e -> saveFile());
        
        setBackground(BLACK);
        fileMenu.setBackground(LIGHTBLACK); 
        fileMenu.setForeground(Color.WHITE);
        openMenuItem.setBackground(LIGHTBLACK);
        openMenuItem.setForeground(Color.WHITE);
        saveMenuItem.setBackground(LIGHTBLACK);
        saveMenuItem.setForeground(Color.WHITE);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        add(fileMenu);
        
        setBorder(null);
    }

    private void openFile() {
        if (file == null) {
            askToSaveExistingFile();
        }

        if (openFileChooser()) {
            controller.readExpressions(file);
        }
    }

    private void saveFile() {
        if (file == null) {
            saveAsFile();
        } else {
            controller.writeExpressions(file);
        }
    }

    private void saveAsFile() {
        if (openFileChooser()) {
            controller.writeExpressions(file);
        }
    }

    private boolean openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("JSON Files (*.json)", "json"));
        int result = fileChooser.showOpenDialog(this);
    
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fileChooser.getSelectedFile();
            String filePath = file.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".json")) {
                file = new File(filePath + ".json");
            }   
            return true;
        } 
           
        return false;
    }
    

    private void askToSaveExistingFile() {
        int option = JOptionPane.showConfirmDialog(this, "Do you want to save the current file?");
        if (option == JOptionPane.YES_OPTION) {
            saveAsFile();
        }
    }
}
