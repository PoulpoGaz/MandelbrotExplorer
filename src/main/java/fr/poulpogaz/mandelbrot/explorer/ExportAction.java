package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.layout.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExportAction implements ActionListener {

    private MandelbrotDrawer drawer;

    public ExportAction(MandelbrotDrawer drawer) {
        this.drawer = drawer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Window window = SwingUtilities.getWindowAncestor(drawer);

        DimensionDialog dialog = new DimensionDialog((Frame) window);

        Dimension dimension = null;
        int result = dialog.showDialog();

        if (result == DimensionDialog.CLOSE) {
            return;
        } else if (result == DimensionDialog.OK) {
            dimension = dialog.getDimension();
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(new File(""));
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));

        result = chooser.showSaveDialog(window);

        if (result == JFileChooser.APPROVE_OPTION) {
            BufferedImage image = drawer.generate(dimension);

            try {
                ImageIO.write(image, "png", chooser.getSelectedFile());
            } catch (IOException ex) {
                ex.printStackTrace();

                JOptionPane.showMessageDialog(window, "Failed to save image: \n" + ex, "IOException", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static class DimensionDialog extends JDialog {

        public static final int OK = 0;
        public static final int SKIP = 1;
        public static final int CLOSE = 2;

        private int result = CLOSE;

        private JSpinner width;
        private JSpinner height;

        public DimensionDialog(Frame ancestor) {
            super(ancestor, "Dimension", true);

            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            initComponents();
            pack();

            setResizable(true);
            setLocationRelativeTo(null);
        }

        private void initComponents() {
            width = new JSpinner(new SpinnerNumberModel(1600, 120, 8192, 1));
            height = new JSpinner(new SpinnerNumberModel(900, 120, 8192, 1));

            JPanel content = new JPanel();
            content.setLayout(new VerticalLayout(5, 10, 10));
            content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

            JPanel bot = new JPanel();
            bot.setLayout(new HorizontalLayout());

            HorizontalConstraint horiz = new HorizontalConstraint();
            horiz.orientation = HCOrientation.RIGHT;

            JButton skip = new JButton("Skip");
            skip.addActionListener((l) -> {
                result = SKIP;
                dispose();
            });

            JButton accept = new JButton("Ok");
            accept.addActionListener((l) -> {
                result = OK;
                dispose();
            });

            bot.add(accept, horiz);
            bot.add(skip, horiz);

            VerticalConstraint constraint = new VerticalConstraint();
            constraint.xAlignment = 0;

            content.add(new JLabel("Dimensions:"), constraint);
            content.add(width);
            content.add(height);


            constraint.fillXAxis = true;
            content.add(bot, constraint);

            setContentPane(content);
        }

        public int showDialog() {
            result = CLOSE;
            setVisible(true);

            return result;
        }

        public Dimension getDimension() {
            return new Dimension((int) width.getValue(), (int) height.getValue());
        }
    }
}