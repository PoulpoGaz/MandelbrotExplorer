package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.core.ForkJoinGenerator;
import fr.poulpogaz.mandelbrot.core.MandelbrotGenerator;
import fr.poulpogaz.mandelbrot.core.SimpleGenerator;
import fr.poulpogaz.mandelbrot.core.ThreadPoolGenerator;
import fr.poulpogaz.mandelbrot.core.palettes.GradientPalette;
import fr.poulpogaz.mandelbrot.core.palettes.GrayScalePalette;
import fr.poulpogaz.mandelbrot.core.palettes.HuePalette;
import fr.poulpogaz.mandelbrot.core.palettes.Palette;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Explorer extends JFrame implements WindowListener {

    private MandelbrotDrawer drawer;

    private final SimpleGenerator simpleGenerator = new SimpleGenerator();
    private final ThreadPoolGenerator threadPoolGenerator = new ThreadPoolGenerator();
    private final ForkJoinGenerator forkJoinGenerator = new ForkJoinGenerator();

    private final GrayScalePalette grayScalePalette = new GrayScalePalette();
    private final GradientPalette gradientPalette = new GradientPalette();
    private final HuePalette huePalette = new HuePalette();

    private JSpinner nIteration;
    private JComboBox<MandelbrotGenerator> generators;
    private JComboBox<Palette> palettes;

    private JButton editGradient;

    public Explorer() {
        super("Mandelbrot explorer");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        pack();

        addWindowListener(this);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private void initComponents() {
        JPanel content = new JPanel();
        content.setLayout(new BorderLayout());

        drawer = new MandelbrotDrawer(simpleGenerator);
        drawer.setPalette(grayScalePalette);

        generators = new JComboBox<>();
        generators.addItem(simpleGenerator);
        generators.addItem(threadPoolGenerator);
        generators.addItem(forkJoinGenerator);
        generators.setSelectedItem(simpleGenerator);
        generators.addItemListener(this::switchGenerator);

        palettes = new JComboBox<>();
        palettes.addItem(grayScalePalette);
        palettes.addItem(huePalette);
        palettes.addItem(gradientPalette);
        palettes.setSelectedItem(grayScalePalette);
        palettes.addItemListener(this::switchPalette);

        nIteration = new JSpinner(new SpinnerNumberModel(drawer.getMaxIteration(), 1, Integer.MAX_VALUE, 1));
        nIteration.addChangeListener(this::iterationChanged);

        JCheckBox smooth = new JCheckBox("Smooth");
        smooth.setSelected(drawer.isSmooth());
        smooth.addActionListener((l) -> {
            drawer.setSmooth(smooth.isSelected());
        });

        JButton export = new JButton("Export");
        export.addActionListener(new ExportAction(drawer));

        editGradient = new JButton("Edit palette");
        editGradient.setVisible(drawer.getPalette() instanceof GradientPalette);
        editGradient.addActionListener((e) -> {
            GradientPalette pal = (GradientPalette) palettes.getSelectedItem();

            new EditGradientPaletteDialog(pal, this);
        });

        JPanel bot = new JPanel();
        bot.add(generators);
        bot.add(nIteration);
        bot.add(export);

        JPanel top = new JPanel();
        top.add(palettes);
        top.add(editGradient);
        top.add(smooth);

        content.add(drawer, BorderLayout.CENTER);
        content.add(top, BorderLayout.NORTH);
        content.add(bot, BorderLayout.SOUTH);

        setContentPane(content);
    }

    private void iterationChanged(ChangeEvent e) {
        drawer.setMaxIteration((Integer) nIteration.getValue());
    }

    private void switchGenerator(ItemEvent e) {
        drawer.setGenerator((MandelbrotGenerator) e.getItem());
    }

    private void switchPalette(ItemEvent e) {
        Palette pal = (Palette) e.getItem();

        drawer.setPalette(pal);

        editGradient.setVisible(pal instanceof GradientPalette);
    }

    @Override
    public void windowOpened(WindowEvent e) {
        drawer.start();
    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {
        drawer.stop();
    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}