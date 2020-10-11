package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.GrayScalePalette;
import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface MandelbrotGenerator {

    BoundsD DEFAULT_BOUNDS = new BoundsD(-2.5, -1.5, 1.5, 1.5);

    default BufferedImage generate(Dimension size) {
        return generate(DEFAULT_BOUNDS, size, new GrayScalePalette(), 128);
    }

    default BufferedImage generate(BoundsD bounds, Dimension size) {
        return generate(bounds, size, new GrayScalePalette(), 128);
    }

    default BufferedImage generate(Dimension size, Palette palette, int iteration) {
        return generate(DEFAULT_BOUNDS, size, palette, iteration);
    }

    BufferedImage generate(BoundsD bounds, Dimension size, Palette palette, int iteration);
}
