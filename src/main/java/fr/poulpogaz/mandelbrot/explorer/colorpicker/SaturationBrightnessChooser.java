package fr.poulpogaz.mandelbrot.explorer.colorpicker;

import fr.poulpogaz.mandelbrot.explorer.sliders.BiSlider;

import java.awt.*;
import java.awt.image.MemoryImageSource;

public class SaturationBrightnessChooser extends BiSlider {

    private float hue = 0.0f;

    private MemoryImageSource source;
    private int[] pixels;
    private int width;
    private int height;

    private Image image;

    public SaturationBrightnessChooser() {

    }

    public SaturationBrightnessChooser(float hue) {
        this.hue = hue;
    }

    @Override
    protected void paintContent(Graphics2D g2d, Rectangle bounds) {
        if(image == null || bounds.width != width || bounds.height != height) {
            createImage(bounds);
        }

        g2d.drawImage(image, bounds.x, bounds.y, null);
    }

    private void createImage(Rectangle bounds) {
        width = bounds.width;
        height = bounds.height;

        pixels = new int[width * height];
        draw(pixels, width, height);

        source = new MemoryImageSource(width, height, pixels, 0, width);
        source.setAnimated(true);

        image = createImage(source);
    }

    private void draw(int[] pixels, int width, int height) {
        float hue = this.hue / 360f;

        for (int y = 0; y < height; y++) {
            float brightness = (float) y / height;

            for (int x = 0; x < width; x++) {
                float saturation = (float) x / width;

                pixels[y * width + x] = Color.HSBtoRGB(hue, saturation, brightness);
            }
        }
    }

    private void updateImage() {
        if(image != null) {
            draw(pixels, width, height);

            source.newPixels(0, 0, width, height);
        }
    }

    public void setHue(int hue) {
        if (this.hue != hue) {
            this.hue = hue;

            updateImage();
            repaint();
        }
    }

    public float getHue() {
        return hue;
    }
}