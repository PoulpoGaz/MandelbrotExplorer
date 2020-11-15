package fr.poulpogaz.mandelbrot.explorer.colorpicker;

import fr.poulpogaz.mandelbrot.layout.HCOrientation;
import fr.poulpogaz.mandelbrot.layout.HorizontalConstraint;
import fr.poulpogaz.mandelbrot.layout.HorizontalLayout;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Consumer;

import static fr.poulpogaz.mandelbrot.explorer.colorpicker.ColorModel.COLOR_PROPERTY;
import static fr.poulpogaz.mandelbrot.explorer.colorpicker.ColorNumberDocument.Type.HEX;

public class ColorPicker extends JComponent {

    public static final int CANCEL_OPTION = 0;
    public static final int CHOOSE_OPTION = 1;
    public static final int ERROR_OPTION = -1;

    private final ColorModel model;

    private SaturationBrightnessChooser chooser;
    private AlphaSlider alphaSlider;
    private HueSlider hueSlider;
    private PreviewComponent previewComponent;

    private JSpinner red;
    private JSpinner green;
    private JSpinner blue;

    private JSpinner hue;
    private JSpinner saturation;
    private JSpinner brightness;

    private JSpinner alpha;
    private JTextField alphaTextField;

    private JTextField hex;

    private boolean alphaEnabled = true;

    private int returnValue;

    public ColorPicker() {
        model = new ColorModel();

        initComponents();
        initListeners();
    }

    private void initComponents() {
        setLayout(new MigLayout());

        // text field
        red = createSpinner(255, model.getRed(), model::setRed);
        green = createSpinner(255, model.getGreen(), model::setGreen);
        blue = createSpinner(255, model.getBlue(), model::setBlue);

        hue = createSpinner(360, model.getHue(), model::setHue);
        saturation = createSpinner(100, model.getSaturation(), model::setSaturation);
        brightness = createSpinner(100, model.getBrightness(), model::setBrightness);

        alpha = createSpinner(255, model.getAlpha(), model::setAlpha);
        alphaTextField = new JTextField("A:");

        hex = createHexField();

        // sliders and chooser
        chooser = new SaturationBrightnessChooser();
        chooser.setHue(model.getHue());
        chooser.setPosition(model.getSaturation(), model.getBrightness());
        chooser.setPreferredSize(new Dimension(150, 150));

        alphaSlider = new AlphaSlider(model.getAlpha());
        alphaSlider.setVertical(false);
        alphaSlider.setPreferredSize(new Dimension(150, 25));

        hueSlider = new HueSlider(model.getHue());
        hueSlider.setInvert(true);
        hueSlider.setPreferredSize(new Dimension(25, 150));

        previewComponent = new PreviewComponent();
        previewComponent.setPreferredSize(new Dimension(25, 25));

        add(chooser, "span 1 4");
        add(hueSlider, "span 1 4");

        add(new JLabel("R:"), "align right");
        add(red);
        add(new JLabel("H:"), "align right");
        add(hue, "wrap");

        add(new JLabel("G:"), "align right");
        add(green);
        add(new JLabel("S:"), "align right");
        add(saturation, "wrap");

        add(new JLabel("B:"), "align right");
        add(blue);
        add(new JLabel("V:"), "align right");
        add(brightness, "wrap");

        add(alphaTextField, "align right");
        add(alpha);
        add(new JLabel("Hex:"), "align right");
        add(hex, "wrap");

        add(alphaSlider);
        add(previewComponent);
    }

    private void initListeners() {
        chooser.addChangeListener((e) -> {
            Point pos = chooser.getPosition();

            model.setHSB(model.getHue(), pos.x, pos.y);
        });

        alphaSlider.addChangeListener((e) -> {
            model.setAlpha(alphaSlider.getValue());
        });

        hueSlider.addChangeListener((e) -> {
            model.setHue(hueSlider.getValue());
        });

        model.addPropertyChangeListener(COLOR_PROPERTY, this::syncComponents);
    }

    private void syncComponents(PropertyChangeEvent evt) {
        hueSlider.setValue(model.getHue());
        alphaSlider.setValue(model.getAlpha());

        chooser.setHue(model.getHue());
        chooser.setPosition(model.getSaturation(), model.getBrightness());

        previewComponent.repaint();

        red.setValue(model.getRed());
        green.setValue(model.getGreen());
        blue.setValue(model.getBlue());
        alpha.setValue(model.getAlpha());
        hue.setValue(model.getHue());
        saturation.setValue(model.getSaturation());
        brightness.setValue(model.getBrightness());

        String hexText = hex.getText();
        if (hexText.isEmpty() || !hexText.equals(model.getHex())) {
            hex.setText(model.getHex());
        }
    }

    private JSpinner createSpinner(int max, int value, Consumer<Integer> listener) {
        JSpinner spinner = new JSpinner();
        spinner.setModel(new SpinnerNumberModel(value, 0, max, 1));
        spinner.addChangeListener((l) -> listener.accept((Integer) spinner.getValue()));

        return spinner;
    }

    private JTextField createHexField() {
        JTextField field = new JTextField();
        field.setDocument(new ColorNumberDocument(HEX));
        field.setText(model.getHex());

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                model.setHex(field.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                model.setHex(field.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                model.setHex(field.getText());
            }
        });

        return field;
    }

    protected JDialog createDialog(Component parent, String title) {
        JDialog dialog;

        if (parent == null) {
            dialog = new JDialog((Frame) null, title, true);
        } else {
            Window window = SwingUtilities.getWindowAncestor(parent);
            if (window instanceof Frame) {
                dialog = new JDialog((Frame) window, title, true);
            } else {
                dialog = new JDialog((Dialog) window, title, true);
            }
        }

        JPanel content = new JPanel();
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.setLayout(new BorderLayout());

        content.add(this, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new HorizontalLayout());

        HorizontalConstraint constraint = new HorizontalConstraint();
        constraint.orientation = HCOrientation.RIGHT;
        constraint.leftGap = 5;

        JButton choose = new JButton("Choose");
        choose.addActionListener((l) -> {
            dialog.dispose();
            returnValue = CHOOSE_OPTION;
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener((l) -> {
            dialog.dispose();
            returnValue = CANCEL_OPTION;
        });

        bottom.add(choose, constraint);
        bottom.add(cancel, constraint);

        content.add(bottom, BorderLayout.SOUTH);
        dialog.setContentPane(content);

        if (JDialog.isDefaultLookAndFeelDecorated()) {
            boolean supportsWindowDecorations = UIManager.getLookAndFeel().getSupportsWindowDecorations();

            if (supportsWindowDecorations) {
                dialog.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
            }
        }

        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        return dialog;
    }

    public int showDialog(Component parent, String title) {
        JDialog dialog = createDialog(parent, title);

        returnValue = ERROR_OPTION;

        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                returnValue = CANCEL_OPTION;
            }
        });
        dialog.setVisible(true);

        return returnValue;
    }

    public Color getColor() {
        return model.getColor();
    }

    public void setColor(Color color) {
        model.setColor(color);
    }

    public boolean isAlphaEnabled() {
        return alphaEnabled;
    }

    public void setAlphaEnabled(boolean alphaEnabled) {
        if (this.alphaEnabled != alphaEnabled) {
            this.alphaEnabled = alphaEnabled;

            alpha.setVisible(alphaEnabled);
            alphaSlider.setVisible(alphaEnabled);
            alphaTextField.setVisible(alphaEnabled);
        }
    }

    public void addColorListener(PropertyChangeListener listener) {
        model.addPropertyChangeListener(COLOR_PROPERTY, listener);
    }

    public void removeColorListener(PropertyChangeListener listener) {
        model.removePropertyChangeListener(COLOR_PROPERTY, listener);
    }

    private class PreviewComponent extends JComponent {

        @Override
        protected void paintComponent(Graphics g) {
            Rectangle bounds = getBounds();
            Insets insets = getInsets();

            RoundRectangle2D.Float rectangle = new RoundRectangle2D.Float();
            rectangle.x = insets.left + 2;
            rectangle.y = insets.top + 2;
            rectangle.width = bounds.width - insets.left - insets.right - 4;
            rectangle.height = bounds.height - insets.top - insets.bottom - 4;
            rectangle.archeight = 3;
            rectangle.arcwidth = 3;

            Graphics2D g2d = (Graphics2D) g;

            Object originalAntialias = g2d.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Stroke originalStroke = g2d.getStroke();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(model.getColor());
            g2d.fill(rectangle);

            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(new Color(25, 25, 25));
            g2d.draw(rectangle);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, originalAntialias);
            g2d.setStroke(originalStroke);
        }
    }
}