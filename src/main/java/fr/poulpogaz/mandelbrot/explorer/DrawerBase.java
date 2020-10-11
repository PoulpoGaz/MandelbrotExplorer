package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.explorer.input.KeyHandler;
import fr.poulpogaz.mandelbrot.explorer.input.MouseHandler;

import java.awt.*;
import java.awt.image.BufferStrategy;

public abstract class DrawerBase extends Canvas implements Runnable {

    private static final int TPS = 30;

    private Thread thread;
    private boolean running;

    protected MouseHandler mouseHandler;
    protected KeyHandler keyHandler;
    protected int fps;

    public DrawerBase() {
        mouseHandler = new MouseHandler();
        keyHandler = new KeyHandler();

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);

        addKeyListener(keyHandler);
    }

    public synchronized void start() {
        thread = new Thread(this, "Drawer");
        running = true;
        thread.start();
    }

    public synchronized void stop() {
        running = false;
    }

    protected abstract void update();

    protected abstract void render(Graphics2D g2d);

    public void run() {
        long lastTime = System.nanoTime();
        float ns = 1000000000.0f / TPS;
        float delta = 0;
        long timer = System.currentTimeMillis();
        int fps = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;


            while (delta >= 1) {
                update();

                keyHandler.reset();
                mouseHandler.reset();
                delta--;
            }

            if (running) {
                BufferStrategy bs = getBufferStrategy();

                if (bs == null) {
                    createBufferStrategy(2);
                } else {
                    Graphics2D g2d = (Graphics2D) bs.getDrawGraphics();

                    render(g2d);

                    g2d.dispose();
                    bs.show();

                    fps++;
                }
            }

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.fps = fps;
                fps = 0;
            }
        }
    }
}