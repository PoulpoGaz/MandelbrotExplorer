package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.core.palettes.GradientPalette;
import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class EditGradientModel {

    private final PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    public static final String SELECTED_POINT_PROPERTY = "SelectedPointProperty";
    public static final String SELECTED_POINT_POS_PROPERTY = "SelectedPointPosProperty";
    public static final String SELECTED_POINT_COLOR_PROPERTY = "SelectedPointColorProperty";

    private final GradientPalette palette;
    private GradientPalette.Point selectedPoint;

    public EditGradientModel(GradientPalette palette) {
        this.palette = palette;
        selectedPoint = palette.getPoints()[0];

    }

    public void addPoint() {
        GradientPalette.Point[] points = palette.getPoints();

        int current = -1;

        for (int i = 0; i < points.length; i++) {
            if (points[i] == selectedPoint) {
                current = i;
                break;
            }
        }

        GradientPalette.Point added;
        if (current == points.length - 1) {
            added = palette.add(selectedPoint.getColor(), 1);
        } else {
            GradientPalette.Point next = points[current + 1];

            int color = Palette.colorLerp(selectedPoint.getColor(), next.getColor(), 0.5f);
            double pos = Palette.lerp(selectedPoint.getPos(), next.getPos(), 0.5f);

            added = palette.add(color, pos);
        }

        setSelectedPoint(added);
    }

    public void removePoint() {
        GradientPalette.Point[] points = palette.getPoints();

        if (points.length > 1) {
            int old = -1;

            for (int i = 0; i < points.length; i++) {
                if (points[i] == selectedPoint) {
                    old = i;
                    break;
                }
            }

            palette.remove(selectedPoint);
            setSelectedPoint(palette.get(Math.max(old - 1, 0)));
        }
    }

    public double getSelectedPointPos() {
        return selectedPoint.getPos();
    }

    public void setSelectedPointPos(double pos) {
        if (selectedPoint.getPos() != pos) {
            double old = selectedPoint.getPos();

            selectedPoint.setPos(pos);

            firePropertyChange(SELECTED_POINT_POS_PROPERTY, old, pos);
        }
    }

    public int getSelectedPointColor() {
        return selectedPoint.getColor();
    }

    public void setSelectedPointColor(int color) {
        if (selectedPoint.getColor() != color) {
            int old = selectedPoint.getColor();

            selectedPoint.setColor(color);

            firePropertyChange(SELECTED_POINT_COLOR_PROPERTY, old, color);
        }
    }

    public GradientPalette.Point getSelectedPoint() {
        return selectedPoint;
    }

    public void setSelectedPoint(GradientPalette.Point selectedPoint) {
        if (this.selectedPoint != selectedPoint) {
            GradientPalette.Point old = this.selectedPoint;

            this.selectedPoint = selectedPoint;

            firePropertyChange(SELECTED_POINT_PROPERTY, old, selectedPoint);
        }
    }

    public GradientPalette.Point[] getPoints() {
        return palette.getPoints();
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