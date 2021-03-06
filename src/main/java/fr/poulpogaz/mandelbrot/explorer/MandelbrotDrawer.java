package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.Chronometer;
import fr.poulpogaz.mandelbrot.core.BoundsD;
import fr.poulpogaz.mandelbrot.core.MandelbrotGenerator;
import fr.poulpogaz.mandelbrot.core.Settings;
import fr.poulpogaz.mandelbrot.core.palettes.Palette;
import fr.poulpogaz.mandelbrot.explorer.input.MouseHandler;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MandelbrotDrawer extends DrawerBase {

    private static final double ZOOM_SPEED = 0.1;
    private static final double MOVE_SPEED = 0.9;
    private static final double ZOOM_T = 0.2;

    private final Chronometer generationTime = new Chronometer();

    private final Lock lock = new ReentrantLock();

    // mandelbrot
    private final Settings settings = new Settings();
    private MandelbrotGenerator generator;

    // update variables
    private Point lastPoint;

    public MandelbrotDrawer(MandelbrotGenerator generator) {
        this.generator = generator;

        setPreferredSize(new Dimension(512, 288));

        keyHandler.addKey(KeyEvent.VK_SHIFT); // zoom out
        keyHandler.addKey(KeyEvent.VK_R); // reset
    }

    @Override
    protected void update() {
        Point point = new Point(mouseHandler.getMouseX(), mouseHandler.getMouseY());

        if (mouseHandler.isMouseDragged() && mouseHandler.isMousePressed(MouseHandler.LEFT_BUTTON) && lastPoint != null) {
            move(point, lastPoint, 1);
        } else if (mouseHandler.isMousePressed(MouseHandler.RIGHT_BUTTON)) {
            zoom(point, keyHandler.isKeyPressed(KeyEvent.VK_SHIFT));
        }

        if (keyHandler.isKeyReleased(KeyEvent.VK_R)) {
            settings.setBounds(new BoundsD(Settings.DEFAULT_BOUNDS));
        }

        lastPoint = point;
    }

    private void move(Point from, Point to, double t) {
        Dimension size = getSize();

        Point2D diff = new Point2D.Double(t * (to.x - from.x), t * (to.y - from.y));

        BoundsD bounds = settings.getBounds();

        double w = bounds.width();
        double h = bounds.height();

        double moveX = MOVE_SPEED * diff.getX() * w / size.width;
        double moveY = MOVE_SPEED * diff.getY() * h / size.height;

        bounds.move(moveX, moveY);
    }

    private void zoom(Point to, boolean zoomOut) {
        double zoom = zoomOut ? 1 + ZOOM_SPEED : 1 - ZOOM_SPEED;

        BoundsD bounds = settings.getBounds();

        // zoom in/out
        double newWidth = bounds.width() * zoom;
        double newHeight = bounds.height() * zoom;

        double xAdd = (newWidth - bounds.width()) / 2;
        double yAdd = (newHeight - bounds.height()) / 2;

        bounds.minX(bounds.minX() - xAdd);
        bounds.maxX(bounds.maxX() + xAdd);

        bounds.minY(bounds.minY() - yAdd);
        bounds.maxY(bounds.maxY() + yAdd);

        Dimension size = getSize();

        move(new Point(size.width / 2, size.height / 2), to, ZOOM_T);
    }

    @Override
    protected void render(Graphics2D g2d) {
        // generate
        BufferedImage image;

        try {
            lock.lock();

            settings.setImageSize(getSize());
            generationTime.start();
            image = generator.generate(settings);
            generationTime.end();
        } finally {
            lock.unlock();
        }

        g2d.setColor(Color.BLACK);
        g2d.drawImage(image, 0, 0, null);

        // show info
        g2d.drawString("Generated in: " + generationTime.getMillis() + "ms", 5, 10);
        g2d.drawString("FPS: " + fps, 5, 30);
    }

    public BufferedImage generate(Dimension size) {
        try {
            lock.lock();

            settings.setImageSize(size == null ? getSize() : size);
            return generator.generate(settings);
        } finally {
            lock.unlock();
        }
    }

    public MandelbrotGenerator getGenerator() {
        return generator;
    }

    public void setGenerator(MandelbrotGenerator generator) {
        try {
            lock.lock();
            this.generator = generator;
        } finally {
            lock.unlock();
        }
    }

    public Palette getPalette() {
        return settings.getPalette();
    }

    public void setPalette(Palette palette) {
        try {
            lock.lock();
            settings.setPalette(palette);
        } finally {
            lock.unlock();
        }
    }

    public int getMaxIteration() {
        return settings.getMaxIteration();
    }

    public void setMaxIteration(int iteration) {
        try {
            lock.lock();
            settings.setMaxIteration(iteration);
        } finally {
            lock.unlock();
        }
    }

    public boolean isSmooth() {
        return settings.isSmooth();
    }

    public void setSmooth(boolean smooth) {
        try {
            lock.lock();
            settings.setSmooth(smooth);
        } finally {
            lock.unlock();
        }
    }
}