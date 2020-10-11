package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;

public class PartialJob implements Runnable {

    private final BoundsD bounds;

    private final BoundsI partialImageBounds;
    private final Dimension imageSize;

    private final int maxIteration;
    private final Palette palette;
    private final int[] pixels;

    public PartialJob(BoundsD bounds, BoundsI partialImageBounds, Dimension imageSize, int maxIteration, Palette palette, int[] pixels) {
        this.bounds = bounds;
        this.partialImageBounds = partialImageBounds;
        this.imageSize = imageSize;
        this.maxIteration = maxIteration;
        this.palette = palette;
        this.pixels = pixels;
    }

    public void run() {
        for (int yImage = partialImageBounds.minY(); yImage < partialImageBounds.maxY(); yImage++) {
            double yCache = (double) yImage / imageSize.height * bounds.height() + bounds.minY();

            for (int xImage = partialImageBounds.minX(); xImage < partialImageBounds.maxX(); xImage++) {
                double y = yCache;
                double x = (double) xImage / imageSize.width * bounds.width() + bounds.minX();

                int color;
                if (isInsideMandelbrot(x, y)) {
                    color = palette.of(maxIteration, maxIteration);
                } else {
                    double x0 = x;
                    double y0 = y;

                    int iteration = 0;

                    while (x * x + y * y < 4 && iteration < maxIteration) {
                        double xTemp = x * x - y * y + x0;
                        y = 2 * x * y + y0;
                        x = xTemp;

                        iteration++;
                    }

                    color = palette.of(iteration, maxIteration);
                }

                pixels[yImage * imageSize.width + xImage] = color;
            }
        }
    }

    private boolean isInsideMandelbrot(double x, double y) {
        // check if the point is inside the main cardioid
        double pSquare = Math.pow(x - 0.25, 2) + y * y;
        double p = Math.sqrt(pSquare);

        double lambda = p - 2 * pSquare + 0.25;

        if (x < lambda) { // the point is inside the main cardioid, so inside the mandelbrot set
            return true;
        }

        return Math.pow(x + 1, 2) + y * y < 1 / 16d; // check if the point is inside the main bud
    }
}