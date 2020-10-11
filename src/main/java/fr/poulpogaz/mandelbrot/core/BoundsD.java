package fr.poulpogaz.mandelbrot.core;

public class BoundsD {

    private double minX;
    private double minY;

    private double maxX;
    private double maxY;

    public BoundsD(double minX, double minY, double maxX, double maxY) {
        setBounds(minX, minY, maxX, maxY);
    }

    public BoundsD(BoundsD bounds) {
        this.minX = bounds.minX;
        this.minY = bounds.minY;
        this.maxX = bounds.maxX;
        this.maxY = bounds.maxY;
    }

    public void setBounds(double minX, double minY, double maxX, double maxY) {
        if (minX > maxX) {
            this.maxX = minX;
            this.minX = maxX;
        } else {
            this.minX = minX;
            this.maxX = maxX;
        }

        if (minY > maxY) {
            this.maxY = minY;
            this.minY = maxY;
        } else {
            this.maxY = maxY;
            this.minY = minY;
        }
    }

    public void move(double x, double y) {
        minX += x;
        minY += y;

        maxX += x;
        maxY += y;
    }

    public double width() {
        return maxX - minX;
    }

    public double height() {
        return maxY - minY;
    }

    public double minX() {
        return minX;
    }

    public void minX(double minX) {
        this.minX = Math.min(minX, maxX);
        this.maxX = Math.max(minX, maxX);
    }

    public double minY() {
        return minY;
    }

    public void minY(double minY) {
        this.minY = Math.min(minY, maxY);
        this.maxY = Math.max(minY, maxY);
    }

    public double maxX() {
        return maxX;
    }

    public void maxX(double maxX) {
        this.maxX = Math.max(minX, maxX);
        this.minX = Math.min(minX, maxX);
    }

    public double maxY() {
        return maxY;
    }

    public void maxY(double maxY) {
        this.maxY = Math.max(minY, maxY);
        this.minY = Math.min(minY, maxY);
    }

    @Override
    public String toString() {
        return "BoundsD{" +
                "minX=" + minX +
                ", minY=" + minY +
                ", maxX=" + maxX +
                ", maxY=" + maxY +
                '}';
    }
}