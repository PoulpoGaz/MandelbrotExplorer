package fr.poulpogaz.mandelbrot.core;

import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolGenerator implements MandelbrotGenerator {

    public static final int TILE_SIZE = 64;

    private ExecutorService executor;

    @Override
    public BufferedImage generate(BoundsD bounds, Dimension size, Palette palette, int maxIteration) {
        Utils.correctAspectRatio(bounds, size);

        palette.preCompute(maxIteration);

        if (executor == null) {
            int nCore = Runtime.getRuntime().availableProcessors();

            executor = Executors.newFixedThreadPool(nCore);
        }

        int width = (int) Math.ceil(size.getWidth() / TILE_SIZE);
        int height = (int) Math.ceil(size.getHeight() / TILE_SIZE);

        double add = bounds.width() / width; // bounds.height() / height = bounds.width() / width because we have corrected the aspect ratio

        int[] pixels = new int[size.width * size.height];

        List<CompletableFuture<Void>> jobsList = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int minX_ = x * TILE_SIZE;
                int minY_ = y * TILE_SIZE;

                int maxX_ = Math.min(minX_ + TILE_SIZE, size.width);
                int maxY_ = Math.min(minY_ + TILE_SIZE, size.height);

                BoundsI imageBounds = new BoundsI(minX_, minY_, maxX_, maxY_);

                PartialJob job = new PartialJob(bounds, imageBounds, size, maxIteration, palette, pixels);
                jobsList.add(CompletableFuture.runAsync(job, executor));
            }
        }

        CompletableFuture<?> allJobs = CompletableFuture.allOf(jobsList.toArray(new CompletableFuture[0]));
        allJobs.join();

        return Utils.toImage(pixels, size.width, size.height);
    }

    @Override
    public String toString() {
        return "Thread pool generator";
    }
}