package fr.poulpogaz.mandelbrot.core;

import java.awt.image.BufferedImage;

/**
 * https://fr.wikipedia.org/wiki/Ensemble_de_Mandelbrot
 */
public class SimpleGenerator extends MandelbrotGenerator {

    @Override
    public BufferedImage generate() {
        Utils.correctAspectRatio(bounds, imageSize);

        palette.preCompute(maxIteration);

        int[] pixels = new int[imageSize.width * imageSize.height];

        PartialJob job = new PartialJob(bounds,
                new BoundsI(0, 0, imageSize.width, imageSize.height),
                imageSize,
                maxIteration,
                palette,
                smooth,
                pixels);
        job.run();

        return Utils.toImage(pixels, imageSize.width, imageSize.height);
    }

    @Override
    public String toString() {
        return "Simple generator";
    }
}