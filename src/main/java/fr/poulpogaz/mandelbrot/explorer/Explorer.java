package fr.poulpogaz.mandelbrot.explorer;

import fr.poulpogaz.mandelbrot.core.ForkJoinGenerator;
import fr.poulpogaz.mandelbrot.core.MandelbrotGenerator;
import fr.poulpogaz.mandelbrot.core.SimpleGenerator;
import fr.poulpogaz.mandelbrot.core.ThreadPoolGenerator;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Explorer extends JFrame implements WindowListener {

    private MandelbrotDrawer drawer;

    private JSpinner nIteration;
    private JComboBox<MandelbrotGenerator> generators;

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

        drawer = new MandelbrotDrawer();

        generators = new JComboBox<>();
        generators.addItemListener(this::switchGenerator);
        generators.addItem(new SimpleGenerator());
        generators.addItem(new ThreadPoolGenerator());
        generators.addItem(new ForkJoinGenerator());

        nIteration = new JSpinner(new SpinnerNumberModel(128, 1, Integer.MAX_VALUE, 1));
        nIteration.addChangeListener(this::iterationChanged);

        JButton export = new JButton("Export");
        export.addActionListener(new ExportAction(drawer));

        JPanel bot = new JPanel();
        bot.add(generators);
        bot.add(nIteration);
        bot.add(export);

        content.add(drawer, BorderLayout.CENTER);
        content.add(bot, BorderLayout.SOUTH);

        setContentPane(content);
    }

    private void iterationChanged(ChangeEvent changeEvent) {
        drawer.setIteration((Integer) nIteration.getValue());
    }

    private void switchGenerator(ItemEvent event) {
        drawer.setGenerator((MandelbrotGenerator) event.getItem());
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