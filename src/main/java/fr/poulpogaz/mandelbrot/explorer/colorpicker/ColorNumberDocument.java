package fr.poulpogaz.mandelbrot.explorer.colorpicker;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;

public class ColorNumberDocument extends PlainDocument {

    private final Type type;

    public ColorNumberDocument(Type type) {
        this.type = type;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(type == Type.HEX) {
            str = str.replaceAll("[^[0-9][A-F][a-f]]", "");
        } else {
            str = str.replaceAll("[^[0-9]]", "");
        }

        String text = getText(0, getLength());

        String out = text + str;

        if(out.equals("")) {
            return;
        }

        if(out.length() > type.maxChar) {
            return;
        }

        if(type == Type.HEX) {
            if(out.length() > 8) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
        } else {
            try {
                int newInt = Integer.parseInt(out);

                if (newInt > type.max) {
                    Toolkit.getDefaultToolkit().beep();
                    return;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        super.insertString(offs, str, a);
    }

    enum Type {
        HEX(0, 8),
        SAT_BRI(100, 3),
        RGBA(255, 3),
        HUE(360, 3);

        private final int max;
        private final int maxChar;

        Type(int max, int maxChar) {
            this.max = max;
            this.maxChar = maxChar;
        }
    }
}