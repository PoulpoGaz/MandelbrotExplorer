package fr.poulpogaz.mandelbrot;

import com.formdev.flatlaf.FlatDarculaLaf;
import fr.poulpogaz.mandelbrot.explorer.Explorer;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        FlatDarculaLaf.install();

        EventQueue.invokeLater(Explorer::new);
    }
}