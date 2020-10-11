package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * https://fr.wikipedia.org/wiki/Ensemble_de_Mandelbrot
 */
public class SimpleGenerator implements MandelbrotGenerator {

    @Override
    public BufferedImage generate(BoundsD bounds, Dimension size, Palette palette, int maxIteration) {
        Utils.correctAspectRatio(bounds, size);

        palette.preCompute(maxIteration);

        int[] pixels = new int[size.width * size.height];

        PartialJob job = new PartialJob(bounds,
                new BoundsI(0, 0, size.width, size.height),
                size,
                maxIteration,
                palette,
                pixels);
        job.run();

        return Utils.toImage(pixels, size.width, size.height);
    }

    @Override
    public String toString() {
        return "Simple generator";
    }
}