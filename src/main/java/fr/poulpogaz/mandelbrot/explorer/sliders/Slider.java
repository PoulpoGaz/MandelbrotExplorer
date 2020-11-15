package fr.poulpogaz.mandelbrot.explorer.sliders;

import fr.poulpogaz.mandelbrot.Math2;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Slider extends JComponent {

    private static final int BORDER_SIZE = 3;

    public static final String ORIENTATION_PROPERTY = "OrientationProperty";
    public static final String MODEL_PROPERTY = "ModelProperty";
    public static final String INVERT_PROPERTY = "InvertProperty";

    private BoundedRangeModel model;
    private final ChangeListener listener = this::stateChanged;

    private boolean vertical;
    private boolean invert;

    public Slider() {
        this(new DefaultBoundedRangeModel(), true, false);
    }

    public Slider(BoundedRangeModel model) {
        this(model, true, false);
    }

    public Slider(BoundedRangeModel model, boolean vertical) {
        this(model, vertical, false);
    }

    public Slider(boolean vertical, boolean invert) {
        this(new DefaultBoundedRangeModel(), vertical, invert);
    }

    public Slider(BoundedRangeModel model, boolean vertical, boolean invert) {
        setModel(model);
        setVertical(vertical);
        setInvert(invert);

        if (vertical) {
            setPreferredSize(new Dimension(25, 100));
        } else {
            setPreferredSize(new Dimension(100, 25));
        }

        initListeners();
    }

    protected void initListeners() {
        MouseListener listener = new MouseListener();

        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    private int cursorToValue(int cursor) {
        Rectangle bounds = getValidBounds();

        int length = model.getMaximum() - model.getMinimum();

        int value;
        if (vertical) {
            value = (cursor - bounds.y) * length / bounds.height;
        } else {
            value = (cursor - bounds.x) * length / bounds.width;
        }

        if (invert) {
            value = length - value;
        }

        return value + model.getMinimum();
    }

    private int valueToCursor() {
        Rectangle bounds = getValidBounds();

        int length = model.getMaximum() - model.getMinimum();
        int value = model.getValue() - model.getMinimum(); // range 0; val; max - min

        if (invert) {
            value = length - value;
        }

        if (isVertical()) {
            return value * bounds.height / length + bounds.y;
        } else {
            return value * bounds.width / length + bounds.x;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Rectangle bounds = getValidBounds();

        int cursor = valueToCursor();

        Graphics2D g2d = (Graphics2D) g;
        if(vertical) {
            paintVerticalTrack(g2d, bounds);
            paintVerticalCursor(g2d, bounds, cursor);
        } else {
            paintHorizontalTrack(g2d, bounds);
            paintHorizontalCursor(g2d, bounds, cursor);
        }
    }

    protected void paintVerticalTrack(Graphics2D g2d, Rectangle bounds) {
        GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, 0, bounds.height, Color.BLACK);
        g2d.setPaint(gradient);

        g2d.fill(bounds);
    }

    protected void paintHorizontalTrack(Graphics2D g2d, Rectangle bounds) {
        GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, bounds.width, 0, Color.BLACK);
        g2d.setPaint(gradient);

        g2d.fill(bounds);
    }

    protected void paintVerticalCursor(Graphics2D g2d, Rectangle bounds, int cursorPos) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(bounds.x - 2, cursorPos - 2, bounds.width + 3, 4);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(bounds.x - 1, cursorPos - 1, bounds.width + 1, 2);
    }

    protected void paintHorizontalCursor(Graphics2D g2d, Rectangle bounds, int cursorPos) {
        g2d.setColor(Color.BLACK);
        g2d.drawRect(cursorPos - 2, bounds.y - 2, 4, bounds.height + 3);

        g2d.setColor(Color.WHITE);
        g2d.drawRect(cursorPos - 1, bounds.y - 1, 2, bounds.height + 1);
    }

    private Rectangle getValidBounds() {
        Rectangle rectangle = getBounds();
        Insets insets = getInsets();

        rectangle.x = insets.left + BORDER_SIZE;
        rectangle.y = insets.top + BORDER_SIZE;
        rectangle.width = rectangle.width - insets.left - insets.right - 2 * BORDER_SIZE;
        rectangle.height = rectangle.height - insets.top - insets.bottom - 2 * BORDER_SIZE;

        return rectangle;
    }

    private void stateChanged(ChangeEvent evt) {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);

        for (ChangeListener listener : listeners) {
            listener.stateChanged(evt);
        }

        repaint();
    }

    public BoundedRangeModel getModel() {
        return this.model;
    }

    public void setModel(BoundedRangeModel model) {
        if (model != null && !Objects.equals(model, this.model)) {
            BoundedRangeModel old = this.model;

            if (this.model != null) {
                model.removeChangeListener(listener);
            }

            this.model = model;
            model.addChangeListener(listener);

            firePropertyChange(MODEL_PROPERTY, old, model);
            repaint();
        }
    }

    public void setValue(int value) {
        model.setValue(value);
    }

    public int getValue() {
        return model.getValue();
    }

    public void setMinimum(int minimum) {
        model.setMinimum(minimum);
    }

    public int getMinimum() {
        return model.getMinimum();
    }

    public void setMaximum(int maximum) {
        model.setMaximum(maximum);
    }

    public int getMaximum() {
        return model.getMaximum();
    }

    public boolean isVertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        if (this.vertical != vertical) {
            boolean old = this.vertical;

            this.vertical = vertical;

            firePropertyChange(ORIENTATION_PROPERTY, old, vertical);
            repaint();
        }
    }

    public boolean isInvert() {
        return invert;
    }

    public void setInvert(boolean invert) {
        if (this.invert != invert) {
            boolean old = this.invert;

            this.invert = invert;

            firePropertyChange(INVERT_PROPERTY, old, invert);
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

            int cursorPos;

            if(vertical) {
                cursorPos = Math2.clamp(point.y, bounds.y, bounds.height + bounds.y);
            } else {
                cursorPos = Math2.clamp(point.x, bounds.x, bounds.width + bounds.x);
            }

            model.setValue(cursorToValue(cursorPos));
        }
    }
}