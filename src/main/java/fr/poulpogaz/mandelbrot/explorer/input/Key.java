package fr.poulpogaz.mandelbrot.explorer.input;

public class Key {

    public int keyCode;
    public boolean press;
    public int pressedNTick = 0;
    public boolean release;

    public Key(int keyCode) {
        this.keyCode = keyCode;
        this.press = false;
        this.release = false;
    }

    public void update(boolean press) {
        if(press) {
            //Press
            this.press = true;
            release = false;

            pressedNTick++;
        } else {
            //Release
            this.press = false;
            release = true;

            pressedNTick = 0;
        }
    }
}