package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class ForkJoinGenerator implements MandelbrotGenerator {

    private static final int TILE_SIZE = 64;

    private ForkJoinPool pool;

    @Override
    public BufferedImage generate(BoundsD bounds, Dimension size, Palette palette, int maxIteration) {
        Utils.correctAspectRatio(bounds, size);

        palette.preCompute(maxIteration);

        if (pool == null) {
            pool = new ForkJoinPool();
        }

        int[] pixels = new int[size.width * size.height];

        pool.invoke(new Action(bounds,
                new BoundsI(0, 0, size.width, size.height),
                size,
                maxIteration,
                palette,
                pixels));

        return Utils.toImage(pixels, size.width, size.height);
    }

    private static class Action extends RecursiveAction {

        private static final int[][] DIRECTIONS = new int[][] {
                {0, 0, -1, -1}, // top left
                {0, 1, -1, 0},  // bot left
                {1, 1, 0, 0},   // bot right
                {1, 0, 0, -1}}; // top right

        private final BoundsD fullBounds;

        private final BoundsI partialImageBounds;
        private final Dimension imageSize;

        private final int maxIteration;
        private final Palette palette;
        private final int[] pixels;

        public Action(BoundsD fullBounds, BoundsI partialImageBounds, Dimension imageSize, int maxIteration, Palette palette, int[] pixels) {
            this.fullBounds = fullBounds;
            this.partialImageBounds = partialImageBounds;
            this.imageSize = imageSize;
            this.maxIteration = maxIteration;
            this.palette = palette;
            this.pixels = pixels;
        }

        @Override
        protected void compute() {
            if (partialImageBounds.width() <= TILE_SIZE && partialImageBounds.height() <= TILE_SIZE) {
                PartialJob job = new PartialJob(fullBounds, partialImageBounds, imageSize, maxIteration, palette, pixels);
                job.run();
            } else {
                List<ForkJoinTask<?>> tasks = new ArrayList<>();

                int imageWidthHalf = partialImageBounds.width() / 2;
                int imageHeightHalf = partialImageBounds.height() / 2;

                for (int[] direction : DIRECTIONS) {
                    BoundsI subImageBounds = new BoundsI(partialImageBounds.minX() + imageWidthHalf * direction[0],
                            partialImageBounds.minY() + imageHeightHalf * direction[1],
                            partialImageBounds.maxX() + imageWidthHalf * direction[2],
                            partialImageBounds.maxY() + imageHeightHalf * direction[3]);

                    tasks.add(new Action(fullBounds,
                            subImageBounds,
                            imageSize,
                            maxIteration,
                            palette,
                            pixels));

                }

                invokeAll(tasks);
            }
        }
    }

    public String toString() {
        return "Fork/Join generator";
    }
}