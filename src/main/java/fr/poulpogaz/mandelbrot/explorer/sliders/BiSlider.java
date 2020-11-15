package fr.poulpogaz.mandelbrot.explorer.sliders;

import fr.poulpogaz.mandelbrot.Math2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class BiSlider extends JComponent {

    public static final String MODEL_PROPERTY = "ModelProperty";

    private static final int BORDER_SIZE = 3;

    private BiSliderModel model;

    private final ChangeListener listener = this::stateChanged;

    public BiSlider() {
        this(new DefaultBiSliderModel());
    }

    public BiSlider(DefaultBiSliderModel model) {
        setModel(model);

        setPreferredSize(new Dimension(100, 100));

        MouseListener listener = new MouseListener();

        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    private void cursorToValue(Point cursor) {
        Rectangle bounds = getValidBounds();

        int xLength = model.getXMaximum() - model.getXMinimum();
        int yLength = model.getYMaximum() - model.getYMinimum();

        int x = (cursor.x - bounds.x) * xLength / bounds.width;
        int y = (cursor.y - bounds.y) * yLength / bounds.height;

        model.setPosition(x, y);
    }

    private Point valueToCursor() {
        Rectangle bounds = getValidBounds();

        int xLength = model.getXMaximum() - model.getXMinimum();
        int yLength = model.getYMaximum() - model.getYMinimum();

        int x = model.getX() * bounds.width / xLength + bounds.x;
        int y = model.getY() * bounds.height / yLength + bounds.y;

        return new Point(x, y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        paintContent(g2d, getValidBounds());
        paintCursor(g2d, valueToCursor());
    }

    protected void paintContent(Graphics2D g2d, Rectangle bounds) {
        g2d.setColor(Color.BLACK);
        g2d.fill(bounds);
    }

    protected void paintCursor(Graphics2D g2d, Point cursor) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(cursor.x - 2, cursor.y - 2, 4, 4);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(cursor.x - 1, cursor.y - 1, 2, 2);
    }

    private Rectangle getValidBounds() {
        Rectangle bounds = getBounds();
        Insets insets = getInsets();

        return new Rectangle(insets.left + BORDER_SIZE,
                insets.top + BORDER_SIZE,
                bounds.width - insets.left - insets.right - BORDER_SIZE * 2,
                bounds.height - insets.top - insets.bottom - BORDER_SIZE * 2);
    }

    private void stateChanged(ChangeEvent evt) {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);

        for (ChangeListener listener : listeners) {
            listener.stateChanged(evt);
        }

        repaint();
    }

    public void setPosition(int x, int y) {
        model.setPosition(x, y);
    }

    public Point getPosition() {
        return model.getPosition();
    }

    public BiSliderModel getModel() {
        return model;
    }

    public void setModel(BiSliderModel model) {
        if (model != null && !Objects.equals(model, this.model)) {
            BiSliderModel old = this.model;

            if (this.model != null) {
                model.removeChangeListener(listener);
            }

            this.model = model;
            model.addChangeListener(listener);

            firePropertyChange(MODEL_PROPERTY, old, model);
            repaint();
        }
    }

    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }

    private class MouseListener extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            processMouseEvent(e.getPoint());
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            processMouseEvent(e.getPoint());
        }

        private void processMouseEvent(Point point) {
            Rectangle bounds = getValidBounds();

            point.x = Math2.clamp(point.x, bounds.x, bounds.width + bounds.x);
            point.y = Math2.clamp(point.y, bounds.y, bounds.height + bounds.y);

            cursorToValue(point);
        }
    }
}