package UI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import data.Controller;

public class MainFrame extends JFrame {
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 1000;
    private static final Color LIGHTBLACK = new Color(30, 31, 32);
    Controller controller;
    FunctionGrapher fGrapher;
    InputPanel inputPanel;
    JSplitPane splitPane;
    Menu menu;

    public MainFrame(Controller controller) {
        super("Function Grapher");
        
        UIManager.put("PopupMenu.border", new LineBorder(new Color(15,15,15)));
        UIManager.put("Menu.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("Menu.selectionForeground", Color.BLACK);
        UIManager.put("MenuItem.selectionBackground", Color.LIGHT_GRAY);
        UIManager.put("MenuItem.selectionForeground", Color.BLACK);
        UIManager.put("Menu.border", new LineBorder(LIGHTBLACK));
        UIManager.put("MenuItem.border", new LineBorder(LIGHTBLACK));
    
        this.controller = controller;
        this.fGrapher = new FunctionGrapher(controller);
        this.inputPanel = new InputPanel(controller);
        this.splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, fGrapher);
        this.menu = new Menu(controller);

        controller.addCallbackRun(this.fGrapher::repaint);
        controller.addCallbackReload(this.inputPanel::update);
        
        CustomSplitPane.setCleanSplitUI(splitPane);
        
        add(splitPane, BorderLayout.CENTER);
        setJMenuBar(menu);
        getJMenuBar().setBorder(null);
        
        addWindowListeners();
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());    
        } catch (UnsupportedLookAndFeelException 
                | ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException  e) {
            e.printStackTrace();
        }
    }

    public void addWindowListeners() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                menu.saveFile();
            }
        });
    }
}
