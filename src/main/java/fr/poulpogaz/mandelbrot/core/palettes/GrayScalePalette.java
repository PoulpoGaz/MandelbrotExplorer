package fr.poulpogaz.mandelbrot.core.palettes;

public class GrayScalePalette extends Palette {

    protected int compute(int iteration, int maxIteration) {
        int gray = 255 * (1 - iteration / maxIteration);

        return (0xFF << 24) |
                ((gray & 0xFF) << 16) |
                ((gray & 0xFF) << 8)  |
                ((gray & 0xFF) << 0);
    }

    public String toString() {
        return "Gray scale palette";
    }
}