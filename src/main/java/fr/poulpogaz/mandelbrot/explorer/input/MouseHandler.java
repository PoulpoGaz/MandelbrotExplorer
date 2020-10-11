package fr.poulpogaz.mandelbrot.explorer.input;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MouseHandler extends MouseAdapter {

    public static final int RIGHT_BUTTON = 0;
    public static final int MIDDLE_BUTTON = 1;
    public static final int LEFT_BUTTON = 2;

    private final boolean[] press = new boolean[3];
    private final boolean[] release = new boolean[3];
    private int wheelFactor = 0;

    private int mouseX = 0;
    private int mouseY = 0;

    private boolean mouseDragged = false;

    public void mousePressed(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            press[RIGHT_BUTTON] = true;
            release[RIGHT_BUTTON] = false;
        } else if(SwingUtilities.isMiddleMouseButton(e)) {
            press[MIDDLE_BUTTON] = true;
            release[MIDDLE_BUTTON] = false;
        } else if(SwingUtilities.isLeftMouseButton(e)) {
            press[LEFT_BUTTON] = true;
            release[LEFT_BUTTON] = false;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)) {
            press[RIGHT_BUTTON] = false;
            release[RIGHT_BUTTON] = true;
        } else if(SwingUtilities.isMiddleMouseButton(e)) {
            press[MIDDLE_BUTTON] = false;
            release[MIDDLE_BUTTON] = true;
        } else if(SwingUtilities.isLeftMouseButton(e)) {
            press[LEFT_BUTTON] = false;
            release[LEFT_BUTTON] = true;
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        wheelFactor += e.getWheelRotation();
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        mouseDragged = true;
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public void reset() {
        for(int i = 0; i < 3; i++) {
            if(!press[i]) {
                release[i] = false;
            }
        }

        mouseDragged = false;
        wheelFactor = 0;
    }

    public boolean isMouseReleased(int mouseButton) {
        return release[mouseButton];
    }

    public boolean isMousePressed(int mouseButton) {
        return press[mouseButton];
    }

    public int wheel() {
        return wheelFactor;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public boolean isMouseDragged() {
        return mouseDragged;
    }
}