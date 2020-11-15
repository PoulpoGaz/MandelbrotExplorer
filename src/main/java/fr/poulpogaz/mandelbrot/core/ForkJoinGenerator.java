package fr.poulpogaz.mandelbrot.core;

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
    public BufferedImage generate(Settings settings) {
        settings = settings.copy();

        BoundsD bounds = settings.getBounds();
        Dimension imageSize = settings.getImageSize();

        Utils.correctAspectRatio(bounds, imageSize);

        settings.palettePreCompute();

        if (pool == null) {
            pool = new ForkJoinPool();
        }

        int[] pixels = new int[imageSize.width * imageSize.height];

        pool.invoke(new Action(
                new BoundsI(0, 0, imageSize.width, imageSize.height),
                settings,
                pixels));

        return Utils.toImage(pixels, imageSize.width, imageSize.height);
    }

    private static class Action extends RecursiveAction {

        private static final int[][] DIRECTIONS = new int[][] {
                {0, 0, -1, -1}, // top left
                {0, 1, -1, 0},  // bot left
                {1, 1, 0, 0},   // bot right
                {1, 0, 0, -1}}; // top right

        private final BoundsI partialImageBounds;
        private final Settings settings;
        private final int[] pixels;

        public Action(BoundsI partialImageBounds, Settings settings, int[] pixels) {
            this.partialImageBounds = partialImageBounds;
            this.settings = settings;
            this.pixels = pixels;
        }

        @Override
        protected void compute() {
            if (partialImageBounds.width() <= TILE_SIZE && partialImageBounds.height() <= TILE_SIZE) {
                PartialJob job = new PartialJob(partialImageBounds, settings, pixels);
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

                    tasks.add(new Action(
                            subImageBounds,
                            settings,
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