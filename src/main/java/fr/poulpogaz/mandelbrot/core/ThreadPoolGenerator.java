package fr.poulpogaz.mandelbrot.core;

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
    public BufferedImage generate(Settings settings) {
        settings = settings.copy();

        BoundsD bounds = settings.getBounds();
        Dimension imageSize = settings.getImageSize();

        Utils.correctAspectRatio(bounds, imageSize);

        settings.palettePreCompute();

        if (executor == null) {
            int nCore = Runtime.getRuntime().availableProcessors();

            executor = Executors.newFixedThreadPool(nCore);
        }

        int width = (int) Math.ceil(imageSize.getWidth() / TILE_SIZE);
        int height = (int) Math.ceil(imageSize.getHeight() / TILE_SIZE);

        int[] pixels = new int[imageSize.width * imageSize.height];

        List<CompletableFuture<Void>> jobsList = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int minX_ = x * TILE_SIZE;
                int minY_ = y * TILE_SIZE;

                int maxX_ = Math.min(minX_ + TILE_SIZE, imageSize.width);
                int maxY_ = Math.min(minY_ + TILE_SIZE, imageSize.height);

                BoundsI imageBounds = new BoundsI(minX_, minY_, maxX_, maxY_);

                PartialJob job = new PartialJob(imageBounds, settings, pixels);
                jobsList.add(CompletableFuture.runAsync(job, executor));
            }
        }

        CompletableFuture<?> allJobs = CompletableFuture.allOf(jobsList.toArray(new CompletableFuture[0]));
        allJobs.join();

        return Utils.toImage(pixels, imageSize.width, imageSize.height);
    }

    @Override
    public String toString() {
        return "Thread pool generator";
    }
}