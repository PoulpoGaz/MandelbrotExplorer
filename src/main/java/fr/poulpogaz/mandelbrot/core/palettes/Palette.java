package fr.poulpogaz.mandelbrot.core.palettes;

// https://en.wikipedia.org/wiki/Plotting_algorithms_for_the_Mandelbrot_set#Continuous_(smooth)_coloring
public abstract class Palette {

    private static final double LOG_2 = Math.log(2);

    protected int[] palette = null;

    protected abstract int compute(int iteration, int maxIteration);

    public void preCompute(int maxIteration) {
        palette = new int[maxIteration + 1];

        for (int i = 0; i <= maxIteration; i++) {
            palette[i] = compute(i, maxIteration);
        }
    }

    public int of(int iteration, int maxIteration, double x, double y, boolean smooth) {
        if (palette == null || palette.length != maxIteration + 1) {
            preCompute(maxIteration);
        }

        if (smooth && iteration < maxIteration) {
            double zn = Math.log(x * x + y * y) / 2;
            double nu = Math.log(zn / LOG_2) / LOG_2;

            double i = iteration + 1 - nu;

            if (i < 0) {
                return palette[0];
            }

            iteration = (int) Math.floor(i);

            int color1 = palette[iteration];
            int color2 = palette[iteration + 1];

            return colorLerp(color1, color2, i % 1);
        } else {
            return palette[iteration];
        }
    }

    public static int colorLerp(int c1, int c2, double t) {
        int r1 = (c1 >> 16) & 0xFF;
        int g1 = (c1 >> 8) & 0xFF;
        int b1 = c1 & 0xFF;

        int r2 = (c2 >> 16) & 0xFF;
        int g2 = (c2 >> 8) & 0xFF;
        int b2 = c2 & 0xFF;

        int r3 = lerp(r1, r2, t);
        int g3 = lerp(g1, g2, t);
        int b3 = lerp(b1, b2, t);

        return 0xFF000000 | (r3 & 0xFF) << 16 | (g3 & 0xFF) << 8 | (b3 & 0xFF);
    }

    public static int lerp(int a, int b, double t) {
        return (int) ((1 - t) * a + t * b);
    }

    public static double lerp(double a, double b, double t) {
        return (1 - t) * a + t * b;
    }
}