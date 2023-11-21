package UI;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Set;

public class Menu extends JMenuBar {
    JMenu fileMenu = new JMenu("File");
    JMenuItem openMenuItem = new JMenuItem("Open");
    JMenuItem saveMenuItem = new JMenuItem("Save");
    File file;
    Set<Expression> functions;

    Menu(Set<Expression> functions) {
        this.functions = functions;
        openMenuItem.addActionListener(e -> openFile());
        saveMenuItem.addActionListener(e -> saveFile());
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        add(fileMenu);
    }
    
    private void openFile() {
        // Check if there's an existing unsaved file
        if (file == null) {
            // Prompt the user to save the current file
            int option = JOptionPane.showConfirmDialog(this, "Do you want to save the current file?");
            if (option == JOptionPane.YES_OPTION) {
                saveAsFile();
            }
        }

        // Create a file chooser for opening a new file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Function Files (*.functions)", "functions"));

        // Show the file chooser dialog
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(selectedFile))) {
                // Read the data from the selected file and update the functions and file
                functions.clear();
                functions.addAll((RunnableSet<Expression>) ois.readObject());
                file = selectedFile; 
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void saveFile() {
        if (file == null) {
            // If the file is not saved yet, call SaveAs
            saveAsFile();
        } else {
            // If the file is already saved, save the data into the existing file
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                oos.writeObject(functions);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void saveAsFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Function Files (*.functions)", "functions"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.getName().toLowerCase().endsWith(".functions")) {
                selectedFile = new File(selectedFile.getAbsolutePath() + ".functions");
            }

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(selectedFile))) {
                oos.writeObject(functions);
                file = selectedFile;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
