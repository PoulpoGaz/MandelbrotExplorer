package fr.poulpogaz.mandelbrot.explorer.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

public class KeyHandler extends KeyAdapter {

    private final HashMap<Integer, Key> keys;

    public KeyHandler() {
        keys = new HashMap<>();
    }

    public void keyPressed(KeyEvent e) {
        if(keys.containsKey(e.getKeyCode())) {
            keys.get(e.getKeyCode()).update(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        if(keys.containsKey(e.getKeyCode())) {
            keys.get(e.getKeyCode()).update(false);
        }
    }

    public void reset() {
        keys.forEach((k, v) -> {
            if(!v.press) {
                v.release = false;
            }
        });
    }

    public boolean isKeyPressed(int keyCode) {
        Key key = keys.get(keyCode);

        if(key != null) {
            return key.press;
        }

        System.err.println("Key " + keyCode + " doesn't exist");
        return false;
    }

    public boolean isKeyPressed(int keyCode, int tickPeriod) {
        Key key = keys.get(keyCode);

        if(key != null) {
            return key.press && key.pressedNTick % tickPeriod == 0;
        }

        System.err.println("Key " + keyCode + " doesn't exist");
        return false;
    }

    public boolean isKeyReleased(int keyCode) {
        Key key = keys.get(keyCode);

        if(key != null) {
            return key.release;
        }

        System.err.println("Key " + keyCode + " doesn't exist");
        return false;
    }

    public void addKey(int keyCode) {
        keys.put(keyCode, new Key(keyCode));
    }

    public void removeKey(int keyCode) {
        keys.remove(keyCode);
    }
}