package UI;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import data.Controller;

public class MainFrame extends JFrame {
    Controller controller;
    FunctionGrapher fGrapher;
    InputPanel inputPanel;
    JSplitPane splitPane;
    private static final Color LIGHTBLACK = new Color(30, 31, 32);
    private static final Color BLACK = new Color(15, 15, 15);
    public MainFrame(Controller controller, int WIDTH, int HEIGHT) {
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
        
        controller.addCallbackRun(this.fGrapher::repaint);
        controller.addCallbackReload(this.inputPanel::update);
        
        CustomSplitPane.setCleanSplitUI(splitPane);
        
        add(splitPane, BorderLayout.CENTER);
        setJMenuBar(new Menu(controller));
        getJMenuBar().setBorder(null);
        
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
}
