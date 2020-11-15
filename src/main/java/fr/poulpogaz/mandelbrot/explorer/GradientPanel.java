package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.Math2;
import fr.poulpogaz.mandelbrot.core.palettes.GradientPalette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GradientPanel extends JComponent {

    private static final int CURSOR_EXTRA_SIZE = 2;

    private final EditGradientModel model;

    public GradientPanel(EditGradientModel model) {
        this.model = model;

        model.addPropertyChangeListener((e) -> repaint());

        setMinimumSize(new Dimension(200, 50));
        setPreferredSize(new Dimension(200, 50));

        MouseHandler handler = new MouseHandler();
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    private double cursorToValue(int cursor, Rectangle bounds) {
        return (cursor - bounds.getX()) / bounds.getWidth();
    }

    private int valueToCursor(double value, Rectangle bounds) {
        return (int) (value * bounds.width + bounds.x);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Rectangle bounds = getValidBounds();

        GradientPalette.Point[] points = model.getPoints();
        GradientPalette.Point selectedPoint = model.getSelectedPoint();

        Paint old = g2d.getPaint();

        Paint gradient = createLinearGradient(bounds.width, points);
        g2d.setPaint(gradient);
        g2d.fill(bounds);

        g2d.setPaint(old);

        for (GradientPalette.Point point : points) {
            if (point != selectedPoint) {
                int cursor = valueToCursor(point.getPos(), bounds);
                paintCursor(g2d, bounds, cursor, false);
            }
        }

        int cursor = valueToCursor(selectedPoint.getPos(), bounds);
        paintCursor(g2d, bounds, cursor, true);
    }

    protected void paintCursor(Graphics2D g2d, Rectangle bounds, int cursorPos, boolean selected) {
        g2d.setColor(selected ? Color.BLACK : Color.DARK_GRAY);
        g2d.drawRect(cursorPos - 2, bounds.y - 2, 4, bounds.height + 3);

        g2d.setColor(selected ? Color.WHITE : Color.LIGHT_GRAY);
        g2d.drawRect(cursorPos - 1, bounds.y - 1, 2, bounds.height + 1);
    }

    protected Paint createLinearGradient(int width, GradientPalette.Point[] points) {
        float[] fractions = toFloat(points);
        Color[] colors = toColor(points);

        for (int i = 0; i < fractions.length - 1; i++) {
            float fraction = fractions[i];
            float next = fractions[i + 1];

            if (fraction == next) {
                float[] tempFractions = fractions;
                Color[] tempColors = colors;

                int length = fractions.length - i - 1;

                fractions = new float[fractions.length - 1];
                System.arraycopy(tempFractions, 0, fractions, 0, i);
                System.arraycopy(tempFractions, i + 1, fractions, i, length);

                colors = new Color[tempColors.length - 1];
                System.arraycopy(tempColors, 0, colors, 0, i);
                System.arraycopy(tempColors, i + 1, colors, i, length);
            }
        }

        if (fractions.length == 1) {
            return colors[0];
        } else {
            return new LinearGradientPaint(0, 0, width, 0, fractions, colors);
        }
    }

    private float[] toFloat(GradientPalette.Point[] points) {
        float[] fractions = new float[points.length];

        for (int i = 0; i < points.length; i++) {
            fractions[i] = (float) points[i].getPos();
        }

        return fractions;
    }

    private Color[] toColor(GradientPalette.Point[] colors) {
        Color[] colors2 = new Color[colors.length];

        for (int i = 0; i < colors.length; i++) {
            colors2[i] = new Color(colors[i].getColor());
        }

        return colors2;
    }

    private Rectangle getValidBounds() {
        Dimension size = getSize();
        Insets insets = getInsets();

        return new Rectangle(insets.left + CURSOR_EXTRA_SIZE,
                insets.top + CURSOR_EXTRA_SIZE,
                size.width - insets.left - insets.right - 2 * CURSOR_EXTRA_SIZE,
                size.height - insets.top - insets.bottom - 2 * CURSOR_EXTRA_SIZE);
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            Rectangle bounds = getValidBounds();

            GradientPalette.Point nearestPoint = null;
            int minLength = Integer.MAX_VALUE;

            for (GradientPalette.Point point : model.getPoints()) {
                int cursor = valueToCursor(point.getPos(), bounds);
                int length = Math.abs(cursor - e.getX());

                if (length < 8 && length < minLength) {
                    nearestPoint = point;
                    minLength = length;
                }
            }

            if (nearestPoint != null) {
                model.setSelectedPoint(nearestPoint);
            }

            move(e.getPoint());
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            move(e.getPoint());
            repaint();
        }

        private void move(Point p) {
            Rectangle bounds = getValidBounds();

            int cursorPos = Math2.clamp(p.x, bounds.x, bounds.width + bounds.x);

            model.setSelectedPointPos(cursorToValue(cursorPos, bounds));
        }
    }
}