package fr.poulpogaz.mandelbrot.explorer.colorpicker;

import fr.poulpogaz.mandelbrot.explorer.sliders.Slider;

import javax.swing.*;
import java.awt.*;

public class AlphaSlider extends Slider {

    public AlphaSlider(int alpha) {
        setModel(new DefaultBoundedRangeModel(alpha, 0, 0, 255));
    }

    @Override
    protected void paintHorizontalTrack(Graphics2D g2d, Rectangle bounds) {
        if (isInvert()) {
            g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, bounds.width, 0, new Color(0, 0, 0, 0)));
        } else {
            g2d.setPaint(new GradientPaint(bounds.width, 0, Color.WHITE, 0, 0, new Color(0, 0, 0, 0)));
        }

        g2d.fill(bounds);
    }

    @Override
    protected void paintVerticalTrack(Graphics2D g2d, Rectangle bounds) {
        if (isInvert()) {
            g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, 0, bounds.height, new Color(0, 0, 0, 0)));
        } else {
            g2d.setPaint(new GradientPaint(0, bounds.height, Color.WHITE, 0, 0, new Color(0, 0, 0, 0)));
        }

        g2d.fill(bounds);
    }
}