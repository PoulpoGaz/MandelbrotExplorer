package fr.poulpogaz.mandelbrot.explorer.sliders;

import fr.poulpogaz.mandelbrot.Math2;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;

public class DefaultBiSliderModel implements BiSliderModel {

    private EventListenerList listenerList = new EventListenerList();

    private Point minimum = new Point(0, 0);
    private Point position = new Point(0, 0);
    private Point maximum = new Point(100, 100);

    public DefaultBiSliderModel() {
    }

    public DefaultBiSliderModel(Point minimum, Point position, Point maximum) {
        this.minimum = minimum;
        this.position = position;
        this.maximum = maximum;
    }

    private void set(Point min, Point pos, Point max) {
        min.x = Math2.min(min.x, pos.x, max.x);
        min.y = Math2.min(min.y, pos.y, max.y);

        max.x = Math2.max(min.x, pos.x, max.x);
        max.y = Math2.max(min.y, pos.y, max.y);

        pos.x = Math2.clamp(pos.x, min.x, max.x);
        pos.y = Math2.clamp(pos.y, min.y, max.y);

        if (!minimum.equals(min) || !maximum.equals(max) || !position.equals(pos)) {
            minimum = min;
            maximum = max;
            position = pos;

            fireStateChanged();
        }
    }

    @Override
    public void setMinimum(Point point) {
        set(point, position, maximum);
    }

    @Override
    public Point getMinimum() {
        return new Point(minimum);
    }

    @Override
    public void setXMinimum(int min) {
        set(new Point(min, minimum.y), position, maximum);
    }

    @Override
    public int getXMinimum() {
        return minimum.x;
    }

    @Override
    public void setYMinimum(int min) {
        set(new Point(minimum.x, min), position, maximum);
    }

    @Override
    public int getYMinimum() {
        return minimum.y;
    }

    @Override
    public void setPosition(int x, int y) {
        set(minimum, new Point(x, y), maximum);
    }

    @Override
    public void setPosition(Point position) {
        set(minimum, position, maximum);
    }

    @Override
    public Point getPosition() {
        return new Point(position);
    }

    @Override
    public void setX(int x) {
        set(minimum, new Point(x, position.y), maximum);
    }

    @Override
    public int getX() {
        return position.x;
    }

    @Override
    public void setY(int y) {
        set(minimum, new Point(position.x, y), maximum);
    }

    @Override
    public int getY() {
        return position.y;
    }

    @Override
    public void setMaximum(Point point) {
        set(minimum, point, point);
    }

    @Override
    public Point getMaximum() {
        return new Point(maximum);
    }

    @Override
    public void setXMaximum(int min) {
        set(minimum, position, new Point(min, maximum.y));
    }

    @Override
    public int getXMaximum() {
        return maximum.x;
    }

    @Override
    public void setYMaximum(int min) {
        set(minimum, position, new Point(maximum.x, min));
    }

    @Override
    public int getYMaximum() {
        return maximum.y;
    }

    protected void fireStateChanged() {
        ChangeListener[] listeners = listenerList.getListeners(ChangeListener.class);

        ChangeEvent event = null;
        for (ChangeListener listener : listeners) {
            if (event == null) {
                event = new ChangeEvent(this);
            }

            listener.stateChanged(event);
        }
    }

    @Override
    public void addChangeListener(ChangeListener listener) {
        listenerList.add(ChangeListener.class, listener);
    }

    @Override
    public void removeChangeListener(ChangeListener listener) {
        listenerList.remove(ChangeListener.class, listener);
    }
}