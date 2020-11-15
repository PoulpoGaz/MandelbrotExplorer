package fr.poulpogaz.mandelbrot.core.palettes;

import java.util.ArrayList;
import java.util.Comparator;

public class GradientPalette extends Palette {

    private ArrayList<Point> points = new ArrayList<>();

    public GradientPalette() {
        points.add(new Point(0xFF000000, 0));
        points.add(new Point(0xFFFFFFFF, 1));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected int compute(int iteration, int maxIteration) {
        if (points.size() == 1) {
            return points.get(0).color;
        }

        float i = (float) iteration / maxIteration;

        Point min = null;
        Point max = null;

        int k;
        for (k = 0; k < points.size(); k++) {
            Point p = points.get(k);

            if (p.pos >= i) {

                if (k == 0) {
                    return points.get(0).color;
                }

                min = points.get(k - 1);
                max = p;

                break;
            }
        }

        if (k == points.size()) {
            return points.get(k - 1).color;
        }

        double t = (i - min.pos) / (max.pos - min.pos);

        return colorLerp(min.color, max.color, t);
    }

    public Point add(int color, double pos) {
        Point point = new Point(color, pos);
        points.add(point);
        sort();

        return point;
    }

    public void remove(Point point) {
        if (points.size() > 1) {
            points.remove(point);
        }
    }

    protected void sort() {
        points.sort(Comparator.comparingDouble(Point::getPos));
    }

    public Point get(int i) {
        return points.get(i);
    }

    public Point[] getPoints() {
        return points.toArray(new Point[0]);
    }

    @Override
    public String toString() {
        return "Gradient palette";
    }

    public class Point {

        private int color;
        private double pos;

        public Point(int color, double pos) {
            this.color = color;
            this.pos = pos;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public double getPos() {
            return pos;
        }

        public void setPos(double pos) {
            this.pos = pos;
            sort();
        }
    }
}