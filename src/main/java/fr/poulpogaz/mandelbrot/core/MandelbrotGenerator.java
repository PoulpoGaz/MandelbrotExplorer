package fr.poulpogaz.mandelbrot.core;

import java.awt.image.BufferedImage;

public interface MandelbrotGenerator {

    BufferedImage generate(Settings settings);
}
