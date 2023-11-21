package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Set;

public class FunctionGrapher extends JPanel {
    private static int SCALE = 100;
    Set<Expression> functions;
    private int divisionsSize = 5;
    private int numberOfXdivisions;
    private int numberOfYdivisions;
    private int offsetX = 0;
    private int offsetY = 0;
    private int lastX = 0;
    private int lastY = 0;
    private int middleX;
    private int middleY;
    private Color darkmodeBackgroundColor = new Color(24, 25, 26);

    public FunctionGrapher(Set<Expression> functions) {
        setBackground(darkmodeBackgroundColor);
        setListeners();
        this.functions = functions;
    }

    // Listeners of the cartesian plane
    private void addZoomListener() {
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    // Zoom in
                    SCALE += 10;
                } else {
                    // Zoom out
                    SCALE = Math.max(10, SCALE - 10);
                }
                repaint();
            }
        });
    }

    private void addClickListener() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    lastX = e.getX();
                    lastY = e.getY();
                }
            }
        });
    }

    private void addDragListener() {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int dx = e.getX() - lastX;
                    int dy = e.getY() - lastY;
                    lastX = e.getX();
                    lastY = e.getY();
                    offsetX += dx;
                    offsetY += dy;
                    repaint();
                }
            }
        });
    }

    private void addResizeListener() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }

    private void setListeners() {
        addZoomListener();
        addClickListener();
        addDragListener();
        addResizeListener();
    }

    @Override
    protected void paintComponent(Graphics g) {
        setMiddleX();
        setMiddleY();
        // Should come after set middle
        setNumberOfXdivisions();
        setNumberOfYdivisions();

        super.paintComponent(g);
        drawAxes(g);
        for (Expression function : functions) {
            drawFunction(g, function);
        }
    }

    private void drawAxes(Graphics g) {
        g.setColor(Color.WHITE);

        // X-axis
        g.drawLine(0, middleY, getWidth(), middleY);
        g.drawString("X", getWidth() - 15, middleY + 15);

        // Y-axis
        g.drawLine(middleX, 0, middleX, getHeight());
        g.drawString("Y", middleX - 15, 15);

        // Draw axis divisions
        drawAxisDivisions(g);
    }

    private void drawFunction(Graphics g, Expression function) {
        g.setColor(function.color);

        setMiddleX();
        setMiddleY();

        double pixelsPerUnit = 1.0 / SCALE;

        try {
            for (int i = 0; i < getWidth(); i++) {
                double x1 = (i - middleX) * pixelsPerUnit;
                double x2 = (i + 1 - middleX) * pixelsPerUnit;
                double y1 = function.parser.eval(x1);
                double y2 = function.parser.eval(x2);

                int screenX1 = i;
                int screenY1 = middleY - (int) (y1 * SCALE);
                int screenX2 = i + 1;
                int screenY2 = middleY - (int) (y2 * SCALE);

                g.drawLine(screenX1, screenY1, screenX2, screenY2);
                g.drawLine(screenX1, screenY1 + 1, screenX2, screenY2 + 1);
                g.drawLine(screenX1, screenY1 - 1, screenX2, screenY2 - 1);
            }
        } catch (Exception e) {
        }
    }

    private void drawAxisDivisions(Graphics g) {
        g.setColor(Color.WHITE);

        double increment = 100.0 / SCALE;

        // X-axis positive divisions
        for (double i = 0; i < numberOfXdivisions; i += increment) {
            int x = middleX + (int) (i * SCALE);

            g.drawLine(x, middleY - divisionsSize, x, middleY + divisionsSize);
            if (i != 0) {
                g.drawString(String.format("%.2f", i), x - 15, middleY + 20);
            }
        }

        // X-axis negative divisions
        for (double i = 0; i > -numberOfXdivisions; i -= increment) {
            int x = middleX + (int) (i * SCALE);

            g.drawLine(x, middleY - divisionsSize, x, middleY + divisionsSize);
            if (i != 0) {
                g.drawString(String.format("%.2f", i), x - 15, middleY + 20);
            }
        }

        // Y-axis negative divisions
        for (double i = 0; i < numberOfYdivisions; i += increment) {
            int y = middleY + (int) (i * SCALE);

            g.drawLine(middleX - divisionsSize, y, middleX + divisionsSize, y);
            if (i != 0) {
                g.drawString(String.format("%.2f", -i), middleX - 30, y + 5);
            }
        }

        // Y-axis positive divisions
        for (double i = 0; i > -numberOfYdivisions; i -= increment) {
            int y = middleY + (int) (i * SCALE);

            g.drawLine(middleX - divisionsSize, y, middleX + divisionsSize, y);
            if (i != 0) {
                g.drawString(String.format("%.2f", -i), middleX - 30, y + 5);
            }
        }
    }

    public void setNumberOfXdivisions() {
        int positiveSide = (getWidth() - middleX) / SCALE;
        int negativeSide = middleX / SCALE;
        this.numberOfXdivisions = Math.max(positiveSide, negativeSide) + 1;
    }

    public void setNumberOfYdivisions() {
        int negativeSide = (getHeight() - middleY) / SCALE + 1;
        int positiveSide = middleY / SCALE;
        this.numberOfYdivisions = Math.max(positiveSide, negativeSide) + 1;
    }

    public void setMiddleX() {
        this.middleX = getWidth() / 2 + offsetX;
    }

    public void setMiddleY() {
        this.middleY = getHeight() / 2 + offsetY;
    }
}
