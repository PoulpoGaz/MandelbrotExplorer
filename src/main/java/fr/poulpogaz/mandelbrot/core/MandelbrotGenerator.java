package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.HuePalette;
import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class MandelbrotGenerator {

    public static final BoundsD DEFAULT_BOUNDS = new BoundsD(-2.5, -1.5, 1.5, 1.5);

    protected Dimension imageSize = new Dimension(1600, 900);
    protected BoundsD bounds = DEFAULT_BOUNDS;
    protected Palette palette = new HuePalette();
    protected int maxIteration = 100;

    protected boolean smooth = false;

    public abstract BufferedImage generate();

    public void copy(MandelbrotGenerator generator) {
        this.imageSize = generator.getImageSize();
        this.bounds = generator.getBounds();
        this.palette = generator.getPalette();
        this.maxIteration = generator.getMaxIteration();
        this.smooth = generator.isSmooth();
    }

    public Dimension getImageSize() {
        return imageSize;
    }

    public void setImageSize(Dimension imageSize) {
        this.imageSize = imageSize;
    }

    public BoundsD getBounds() {
        return bounds;
    }

    public void setBounds(BoundsD bounds) {
        this.bounds = bounds;
    }

    public Palette getPalette() {
        return palette;
    }

    public void setPalette(Palette palette) {
        this.palette = palette;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public boolean isSmooth() {
        return smooth;
    }

    public void setSmooth(boolean smooth) {
        this.smooth = smooth;
    }
}
