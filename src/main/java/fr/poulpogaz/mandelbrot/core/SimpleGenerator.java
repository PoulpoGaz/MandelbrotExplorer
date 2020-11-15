package fr.poulpogaz.mandelbrot.core;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * https://fr.wikipedia.org/wiki/Ensemble_de_Mandelbrot
 */
public class SimpleGenerator implements MandelbrotGenerator {

    @Override
    public BufferedImage generate(Settings settings) {
        settings = settings.copy();

        BoundsD bounds = settings.getBounds();
        Dimension imageSize = settings.getImageSize();

        Utils.correctAspectRatio(bounds, imageSize);

        settings.palettePreCompute();

        int[] pixels = new int[imageSize.width * imageSize.height];

        PartialJob job = new PartialJob(new BoundsI(0, 0, imageSize.width, imageSize.height),
                settings,
                pixels);
        job.run();

        return Utils.toImage(pixels, imageSize.width, imageSize.height);
    }

    @Override
    public String toString() {
        return "Simple generator";
    }
}