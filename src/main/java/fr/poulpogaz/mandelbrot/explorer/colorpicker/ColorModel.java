package fr.poulpogaz.mandelbrot.explorer.colorpicker;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class ColorModel {

    public static final String COLOR_PROPERTY = "ColorProperty";

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private Color color;
    private final int[] hsb = new int[3];
    private String hex;

    public ColorModel() {
        this(Color.WHITE);
    }

    public ColorModel(Color color) {
        setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        if (!Objects.equals(this.color, color)) {
            Color old = this.color;

            this.color = color;

            float[] floats = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            hsb[0] = (int) (floats[0] * 360);
            hsb[1] = (int) (floats[1] * 100);
            hsb[2] = (int) (floats[2] * 100);

            hex = computeHex();

            firePropertyChange(COLOR_PROPERTY, old, color);
        }
    }

    public int[] getHSB() {
        return hsb;
    }

    public void setHSB(int hue, int saturation, int brightness) {
        if ((hsb[0] != hue || hsb[1] != saturation || hsb[2] != brightness) && checkHSBRange(hue, saturation, brightness)) {
            int rgba = Color.HSBtoRGB(hue / 360f, saturation / 100f, brightness / 100f);
            rgba = ((getAlpha() & 0xFF) << 24) | (rgba & 0x00FFFFFF);

            Color old = this.color;

            this.color = new Color(rgba, true);
            this.hsb[0] = hue;
            this.hsb[1] = saturation;
            this.hsb[2] = brightness;

            hex = computeHex();

            firePropertyChange(COLOR_PROPERTY, old, color);
        }
    }

    protected boolean checkHSBRange(int hue, int saturation, int brightness) {
        if (hue < 0 || hue > 360) {
            return false;
        }

        if (saturation < 0 || saturation > 100) {
            return false;
        }

        return brightness >= 0 && brightness <= 100;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        Color color = fromHex(hex);

        if (!this.hex.equals(hex) && color != null && color != this.color ) {
            Color old = this.color;

            this.color = color;

            float[] floats = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            hsb[0] = (int) (floats[0] * 360);
            hsb[1] = (int) (floats[1] * 100);
            hsb[2] = (int) (floats[2] * 100);

            this.hex = hex;

            firePropertyChange(COLOR_PROPERTY, old, color);
        }
    }

    protected Color fromHex(String hex) {
        return switch (hex.length()) {
            case 3 -> {
                int red = Integer.parseInt(hex.substring(0, 1), 16) * 16;
                int green = Integer.parseInt(hex.substring(1, 2), 16) * 16;
                int blue = Integer.parseInt(hex.substring(2, 3), 16) * 16;

                yield new Color(red, green, blue);
            }
            case 6 -> {
                int red = Integer.parseInt(hex.substring(0, 2), 16);
                int green = Integer.parseInt(hex.substring(2, 4), 16);
                int blue = Integer.parseInt(hex.substring(4, 6), 16);

                yield new Color(red, green, blue);
            }
            case 8 -> {
                int alpha = Integer.parseInt(hex.substring(0, 2), 16);
                int red = Integer.parseInt(hex.substring(2, 4), 16);
                int green = Integer.parseInt(hex.substring(4, 6), 16);
                int blue = Integer.parseInt(hex.substring(6, 8), 16);

                yield new Color(red, green, blue, alpha);
            }
            default -> null;
        };
    }

    protected String computeHex() {
        String alpha = Integer.toHexString(color.getAlpha());
        String red = Integer.toHexString(color.getRed());
        String green = Integer.toHexString(color.getGreen());
        String blue = Integer.toHexString(color.getBlue());

        return alpha + red + green + blue;
    }

    public int getRed() {
        return color.getRed();
    }

    public void setRed(int red) {
        setColor(new Color(red, getGreen(), getBlue(), getAlpha()));
    }

    public int getGreen() {
        return color.getGreen();
    }

    public void setGreen(int green) {
        setColor(new Color(getRed(), green, getBlue(), getAlpha()));
    }

    public int getBlue() {
        return color.getBlue();
    }

    public void setBlue(int blue) {
        setColor(new Color(getRed(), getGreen(), blue, getAlpha()));
    }

    public int getAlpha() {
        return color.getAlpha();
    }

    public void setAlpha(int alpha) {
        setColor(new Color(getRed(), getGreen(), getBlue(), alpha));
    }

    public int getHue() {
        return hsb[0];
    }

    public void setHue(int hue) {
        setHSB(hue, hsb[1], hsb[2]);
    }

    public int getSaturation() {
        return hsb[1];
    }

    public void setSaturation(int saturation) {
        setHSB(hsb[0], saturation, hsb[2]);
    }

    public int getBrightness() {
        return hsb[2];
    }

    public void setBrightness(int brightness) {
        setHSB(hsb[0], hsb[1], brightness);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(property, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(String property, PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(property, listener);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue) {
        changeSupport.firePropertyChange(property, oldValue, newValue);
    }
}