package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;

// https://en.wikipedia.org/wiki/Plotting_algorithms_for_the_Mandelbrot_set
public class PartialJob implements Runnable {

    private final BoundsD bounds;

    private final BoundsI partialImageBounds;
    private final Dimension imageSize;

    private final int maxIteration;
    private final Palette palette;
    private final int[] pixels;

    private final boolean smooth;

    public PartialJob(BoundsI partialImageBounds, Settings settings, int[] pixels) {
        this.partialImageBounds = partialImageBounds;
        this.bounds = new BoundsD(settings.getBounds());
        this.imageSize = settings.getImageSize();
        this.maxIteration = settings.getMaxIteration();
        this.palette = settings.getPalette();
        this.smooth = settings.isSmooth();
        this.pixels = pixels;
    }

    public void run() {
        double widthFraction = bounds.width() / imageSize.width;
        double heightFraction = bounds.height() / imageSize.height;

        for (int yImage = partialImageBounds.minY(); yImage < partialImageBounds.maxY(); yImage++) {
            double yCache = (double) yImage * heightFraction + bounds.minY();

            for (int xImage = partialImageBounds.minX(); xImage < partialImageBounds.maxX(); xImage++) {
                double y = yCache;
                double x = (double) xImage * widthFraction + bounds.minX();

                double x0 = x;
                double y0 = y;

                double x2 = x * x;
                double y2 = y * y;

                int iteration = 0;

                while (x2 + y2 < 4 && iteration < maxIteration) {
                    y = 2 * x * y + y0;
                    x = x2 - y2 + x0;

                    iteration++;

                    x2 = x * x;
                    y2 = y * y;
                }

                pixels[yImage * imageSize.width + xImage] = palette.of(iteration, maxIteration, x, y, smooth);
            }
        }
    }
}