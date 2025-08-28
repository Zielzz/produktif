package com.radja.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import com.radja.form.FormPesananMenungguAntrian;
import com.radja.model.Layanan;
import com.radja.model.Pelanggan;
import com.radja.model.Pesanan;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JPanel;
import net.miginfocom.layout.PlatformDefaults;
import net.miginfocom.swing.MigLayout;
import raven.extras.SlidePane;
import raven.extras.SlidePaneTransition;

public class FormInputPesanan extends JPanel {

    private JButton btnNext;
    private JButton btnBefore;
    private SlidePane slidePane;
    
    private Pelanggan pelanggan = new Pelanggan();
    private Pesanan pesanan = new Pesanan();
    private FormPesananMenungguAntrian formPesanan;
    
    private FormInputPesananPelanggan formPelanggan = new FormInputPesananPelanggan(pelanggan);
    private FormInputPesananLayanan formLayanan = new FormInputPesananLayanan(pesanan);
    private FormInputPesananReview formReview = new FormInputPesananReview(
        pelanggan != null ? pelanggan : new Pelanggan(),
        pesanan != null ? pesanan : new Pesanan()
    );

    private JPanel[] panels = { formPelanggan, formLayanan, formReview };
    private int currentIndex = 0;
    
    public FormInputPesanan(Pesanan model, FormPesananMenungguAntrian formPesanan) {
        this.formPesanan = formPesanan;
        init();
        updateButtonState();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx"));
        add(createExample());
        add(createTestButton(), "align right");
    }
    
    private Component createTestButton() {
        JPanel panel = new JPanel(new MigLayout("fillx"));

        btnNext = new JButton("Next");
        btnNext.setIcon(new FlatSVGIcon("com/radja/icon/next.svg", 0.4f));
        btnNext.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor; foreground:rgb(255,255,255);");

        btnBefore = new JButton("Before");
        btnBefore.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnBefore.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor; background:rgb(255,255,255);");
        btnBefore.setEnabled(false);

        btnNext.addActionListener(e -> goToNextPanel());
        btnBefore.addActionListener(e -> goToPreviousPanel());

        panel.add(btnBefore, "span, split 2, align right, hmin 30");
        panel.add(btnNext, "hmin 30, align right");

        return panel;
    }

    private Component createExample() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx"));
        
        slidePane = new SlidePane((container, component) -> minSize(container, component));
        slidePane.addSlide(panels[0]);

        panel.add(slidePane);
        return panel;
    }
    
    private void goToNextPanel() {
        try {
            if (currentIndex == 0) { 
                formPelanggan.getDataPelanggan();
            } else if (currentIndex == 1) {
                formLayanan.getDataLayanan();
                formReview.updateData();
            }
            
            if (currentIndex < panels.length - 1) {
                currentIndex++;
                slidePane.addSlide(panels[currentIndex], SlidePaneTransition.Type.FORWARD);
            } else {
                formReview.insertData();
                formPesanan.refreshTable();
            }
            updateButtonState();
        } catch (IllegalArgumentException e) {
            // Exception will be handled by the form's validation
        }
    }
    
    private void goToPreviousPanel() {
        if (currentIndex > 0) {
            currentIndex--;
            slidePane.addSlide(panels[currentIndex], SlidePaneTransition.Type.BACK);
        }
        updateButtonState();
    }

    private void updateButtonState() {
        btnBefore.setEnabled(currentIndex > 0);
        btnNext.setText(currentIndex == panels.length - 1 ? "Submit" : "Next");
    }

    private Dimension minSize(Container container, Component component) {
        Container parent = container.getParent();
        Dimension comSize = component.getPreferredSize();
        Dimension parentSize = parent.getSize();
        Insets parentInsets = FlatUIUtils.addInsets(parent.getInsets(), getMiglayoutDefaultInsets());
        int width = Math.min(comSize.width, parentSize.width - (parentInsets.left + parentInsets.right));
        int height = Math.min(comSize.height, parentSize.height - (parentInsets.top + parentInsets.bottom));
        return new Dimension(width, height);
    }

    private Insets getMiglayoutDefaultInsets() {
        int top = (int) PlatformDefaults.getPanelInsets(0).getValue();
        int left = (int) PlatformDefaults.getPanelInsets(1).getValue();
        int bottom = (int) PlatformDefaults.getPanelInsets(2).getValue();
        int right = (int) PlatformDefaults.getPanelInsets(3).getValue();
        return UIScale.scale(new Insets(top, left, bottom, right));
    }
}