package fr.poulpogaz.mandelbrot.explorer.colorpicker;

import fr.poulpogaz.mandelbrot.explorer.sliders.Slider;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class HueSlider extends Slider {

    private static final Color[] COLORS = new Color[] {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED};
    private static final float[] FRACTIONS;

    public HueSlider(int hue) {
        setModel(new DefaultBoundedRangeModel(hue, 0, 0, 360));
    }

    @Override
    protected void paintHorizontalTrack(Graphics2D g2d, Rectangle bounds) {
        Point2D start = new Point2D.Float(bounds.x, 0);
        Point2D end = new Point2D.Float(bounds.x + bounds.width, 0);

        if (isInvert()) {
            Point2D temp = start;
            start = end;
            end = temp;
        }

        g2d.setPaint(new LinearGradientPaint(start, end, FRACTIONS, COLORS));
        g2d.fill(bounds);
    }

    @Override
    protected void paintVerticalTrack(Graphics2D g2d, Rectangle bounds) {
        Point2D start = new Point2D.Float(0, bounds.y);
        Point2D end = new Point2D.Float(0, bounds.y + bounds.height);

        if (isInvert()) {
            Point2D temp = start;
            start = end;
            end = temp;
        }

        g2d.setPaint(new LinearGradientPaint(start, end, FRACTIONS, COLORS));
        g2d.fill(bounds);
    }

    static {
        FRACTIONS = new float[COLORS.length];

        for (int i = 0; i < COLORS.length; i++) {
            FRACTIONS[i] = (float) i / COLORS.length;
        }
    }
}