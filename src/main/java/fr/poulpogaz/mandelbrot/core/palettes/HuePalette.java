package fr.poulpogaz.mandelbrot.core.palettes;

import java.awt.*;

public class HuePalette extends Palette {

    @Override
    public int compute(int iteration, int maxIteration) {
        return Color.HSBtoRGB((float) iteration / maxIteration, 1f, 1f);
    }
}