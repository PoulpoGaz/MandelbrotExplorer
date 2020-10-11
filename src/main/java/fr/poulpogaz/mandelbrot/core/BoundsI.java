package fr.poulpogaz.mandelbrot.core;

public class BoundsI {

    private int minX;
    private int minY;

    private int maxX;
    private int maxY;

    public BoundsI(int minX, int minY, int maxX, int maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    public BoundsI(BoundsI bounds) {
        this.minX = bounds.minX;
        this.minY = bounds.minY;
        this.maxX = bounds.maxX;
        this.maxY = bounds.maxY;
    }

    public void setBounds(int minX, int minY, int maxX, int maxY) {
        maxX(maxX);
        maxY(maxY);
        minX(minX);
        minY(minY);
    }

    public void move(int x, int y) {
        minX += x;
        minY += y;

        maxX += x;
        maxY += y;
    }

    public int width() {
        return maxX - minX;
    }

    public int height() {
        return maxY - minY;
    }

    public int minX() {
        return minX;
    }

    public void minX(int minX) {
        this.minX = Math.min(minX, maxX);
        this.maxX = Math.max(minX, maxX);
    }

    public int minY() {
        return minY;
    }

    public void minY(int minY) {
        this.minY = Math.min(minY, maxY);
        this.maxY = Math.max(minY, maxY);
    }

    public int maxX() {
        return maxX;
    }

    public void maxX(int maxX) {
        this.maxX = Math.max(minX, maxX);
        this.minX = Math.min(minX, maxX);
    }

    public int maxY() {
        return maxY;
    }

    public void maxY(int maxY) {
        this.maxY = Math.max(minY, maxY);
        this.minY = Math.min(minY, maxY);
    }

    @Override
    public String toString() {
        return "BoundsI{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                '}';
    }
}