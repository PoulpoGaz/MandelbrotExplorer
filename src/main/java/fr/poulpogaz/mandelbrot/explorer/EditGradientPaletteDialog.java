package fr.poulpogaz.mandelbrot.explorer;

import com.formdev.flatlaf.ui.FlatUIUtils;
import fr.poulpogaz.mandelbrot.core.palettes.GradientPalette;
import fr.poulpogaz.mandelbrot.explorer.colorpicker.ColorPicker;
import fr.poulpogaz.mandelbrot.layout.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class EditGradientPaletteDialog extends JDialog {

    private EditGradientModel model;

    public EditGradientPaletteDialog(GradientPalette palette, Frame parent) {
        super(parent, "Edit", true);
        model = new EditGradientModel(palette);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setResizable(false);
        initComponents();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new VerticalLayout(5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        VerticalConstraint constraint = new VerticalConstraint();
        constraint.fillXAxis = true;

        GradientPanel gradientPanel = new GradientPanel(model);
        ColorComponent color = new ColorComponent();

        JSpinner position = new JSpinner(new SpinnerNumberModel(0, 0, 1, 0.001));
        position.addChangeListener((c) -> {
            model.setSelectedPointPos((Double) position.getValue());
        });

        model.addPropertyChangeListener(EditGradientModel.SELECTED_POINT_POS_PROPERTY, (e) -> {
            position.setValue(model.getSelectedPointPos());
        });

        model.addPropertyChangeListener(EditGradientModel.SELECTED_POINT_PROPERTY, (e) -> {
            position.setValue(model.getSelectedPointPos());
        });

        JButton add = new JButton("+");
        add.addActionListener((e) -> model.addPoint());

        JButton remove = new JButton("-");
        remove.addActionListener((e) -> model.removePoint());

        panel.add(gradientPanel);

        JPanel bot = new JPanel();
        bot.setLayout(new HorizontalLayout(5));

        HorizontalConstraint hConstraint = new HorizontalConstraint();
        hConstraint.fillYAxis = true;
        hConstraint.orientation = HCOrientation.RIGHT;

        bot.add(remove, hConstraint);
        bot.add(add, hConstraint);
        bot.add(position, hConstraint);

        hConstraint.endComponent = true;
        bot.add(color, hConstraint);

        panel.add(bot, constraint);

        setContentPane(panel);
    }

    private class ColorComponent extends JComponent {

        private final ColorPicker picker = new ColorPicker();

        public ColorComponent() {
            model.addPropertyChangeListener(EditGradientModel.SELECTED_POINT_COLOR_PROPERTY, (e) -> repaint());
            model.addPropertyChangeListener(EditGradientModel.SELECTED_POINT_PROPERTY, (e) -> repaint());

            picker.setAlphaEnabled(false);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    picker.setColor(new Color(model.getSelectedPointColor()));

                    int result = picker.showDialog(SwingUtilities.getWindowAncestor(ColorComponent.this), "Choose a color");

                    if (result == ColorPicker.CHOOSE_OPTION) {
                        model.setSelectedPointColor(picker.getColor().getRGB());
                    }
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;

            Dimension dim = getSize();

            FlatUIUtils.setRenderingHints(g2d);

            g2d.setColor(new Color(model.getSelectedPointColor()));
            g2d.fillRoundRect(0, 0, dim.width, dim.height, 6, 6);
        }
    }
}