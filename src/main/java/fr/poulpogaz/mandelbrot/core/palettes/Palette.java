package fr.poulpogaz.mandelbrot.core.palettes;

import java.awt.*;

public abstract class Palette {

    protected int[] palette = null;

    protected abstract int compute(int iteration, int maxIteration);

    public void preCompute(int maxIteration) {
        palette = new int[maxIteration + 1];

        for (int i = 0; i <= maxIteration; i++) {
            palette[i] = compute(i, maxIteration);
        }
    }

    public int of(int iteration, int maxIteration) {
        if (palette == null || palette.length != maxIteration + 1) {
            preCompute(maxIteration);
        }

        return palette[iteration];
    }
}