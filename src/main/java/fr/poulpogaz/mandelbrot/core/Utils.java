package fr.poulpogaz.mandelbrot.core;

import java.awt.*;
import java.awt.image.*;

public class Utils {

    public static BufferedImage toImage(int[] pixels, int width, int height) {
        ColorModel cm = DirectColorModel.getRGBdefault();

        SampleModel sampleModel = cm.createCompatibleSampleModel(width, height);

        DataBuffer db = new DataBufferInt(pixels, height, 0);
        WritableRaster raster = Raster.createWritableRaster(sampleModel, db, null);

        return new BufferedImage(cm, raster, false, null);
    }

    public static void correctAspectRatio(BoundsD bounds, Dimension size) {
        double boundsRatio = bounds.width() / bounds.height();
        double imageRatio = size.getWidth() / size.getHeight();

        if (boundsRatio > imageRatio) {
            double height = bounds.width() * (1 / imageRatio);

            double sub = (height - bounds.height()) / 2;
            bounds.minY(bounds.minY() - sub);
            bounds.maxY(bounds.maxY() + sub);

        } else if (boundsRatio < imageRatio) {
            double width = bounds.height() * imageRatio;

            double sub = (width - bounds.width()) / 2;
            bounds.minX(bounds.minX() - sub);
            bounds.maxX(bounds.maxX() + sub);
        }
    }
}