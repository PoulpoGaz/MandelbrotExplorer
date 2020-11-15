package fr.poulpogaz.mandelbrot.explorer.sliders;

import javax.swing.event.ChangeListener;
import java.awt.*;

public interface BiSliderModel {

    void setMinimum(Point point);

    Point getMinimum();

    void setXMinimum(int min);

    int getXMinimum();

    void setYMinimum(int min);

    int getYMinimum();

    void setPosition(int x, int y);

    void setPosition(Point position);

    Point getPosition();

    void setX(int x);

    int getX();

    void setY(int y);

    int getY();

    void setMaximum(Point point);

    Point getMaximum();

    void setXMaximum(int min);

    int getXMaximum();

    void setYMaximum(int min);

    int getYMaximum();

    void addChangeListener(ChangeListener listener);

    void removeChangeListener(ChangeListener listener);
}