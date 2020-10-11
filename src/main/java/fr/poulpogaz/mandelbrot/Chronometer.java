package fr.poulpogaz.mandelbrot;

public class Chronometer {

    private long start = 0;
    private long end = 0;

    public Chronometer() {

    }

    public void start() {
        start = System.nanoTime();
    }

    public void end() {
        end = System.nanoTime();
    }

    public long now() {
        return System.nanoTime() - start;
    }

    public long nowMillis() {
        return (System.nanoTime() - start) / 1_000_000;
    }

    public long get() {
        return end - start;
    }

    public long getMillis() {
        return (end - start) / 1_000_000;
    }

    public double getMillisDouble() {
        return (end - start) / 1_000_000d;
    }
}