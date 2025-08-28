package com.radja.main;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;

public class MainForm extends JPanel {

    private static final Logger LOGGER = Logger.getLogger(MainForm.class.getName());
    private JPanel mainPanel;
    private JButton buttonUndo;
    private JButton buttonRedo;

    public MainForm() {
        LOGGER.log(Level.INFO, "Initializing MainForm");
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][fill,grow][][]"));
        add(createHeader());
        add(createMain());
        add(new JSeparator(), "height 2!");
        add(createFooter());
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));

        JToolBar toolBar = new JToolBar();
        JButton buttonDrawer = new JButton(new FlatSVGIcon("raven/modal/demo/icons/menu.svg", 0.5f));
        buttonUndo = new JButton(new FlatSVGIcon("raven/modal/demo/icons/undo.svg", 0.5f));
        buttonRedo = new JButton(new FlatSVGIcon("raven/modal/demo/icons/redo.svg", 0.5f));
        buttonDrawer.addActionListener(e -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
            }
        });
        buttonUndo.addActionListener(e -> FormManager.undo());
        buttonRedo.addActionListener(e -> FormManager.redo());

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        panel.add(toolBar);
        return panel;
    }

    private JPanel createFooter() {
        JPanel panel = new JPanel(new MigLayout("insets 1 n 1 n,al trailing center,gapx 10,height 30!", "[]push[]", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "[light]background:tint($Panel.background,20%);" +
                "[dark]background:tint($Panel.background,5%);");

        // Tanggal dan waktu
        JLabel lbDate = new JLabel();
        lbDate.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground:$Label.disabledForeground;");
        Timer timer = new Timer(1000, (e) -> { // Ubah ke 1000ms untuk efisiensi
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lbDate.setText(df.format(new Date()));
        });
        panel.add(lbDate);

        timer.start();
        return panel;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    public void setForm(Form form) {
        LOGGER.log(Level.INFO, "Setting form: " + form.getClass().getSimpleName());
        mainPanel.removeAll();
        mainPanel.add(form);
        mainPanel.repaint();
        mainPanel.revalidate();

        // Check button
        buttonUndo.setEnabled(FormManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormManager.FORMS.isRedoAble());
        // Check component orientation and update
        if (mainPanel.getComponentOrientation().isLeftToRight() != form.getComponentOrientation().isLeftToRight()) {
            applyComponentOrientation(mainPanel.getComponentOrientation());
        }
    }
}