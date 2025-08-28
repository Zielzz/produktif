package com.radja.form.input;

import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.dao.LayananDAO;
import com.radja.model.Layanan;
import com.radja.model.Pesanan;
import com.radja.service.ServiceLayanan;
import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormInputPesananLayanan extends JPanel {

    private JLabel lbLayananSelected;
    private JLabel lbHarga;
    
    private JLabel lbTotalHarga;
    private JLabel lbHargaLayanan;
    
    private JTextField txtMerk;
    private JTextField txtNoPlat;
    private JComboBox cbxLayanan;
    
    private ServiceLayanan servisLayanan = new LayananDAO();
    private Pesanan pesanan;
    private int idLayanan;
    
    public FormInputPesananLayanan(Pesanan pesanan) {
        this.pesanan = pesanan;
        
        init();
        getLayanan();
        hideComponent();
        setColorCbx();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap, width 300"));
        
        JLabel lbMerk = new JLabel("Merk Kendaraan");
        JLabel lbNoPlat = new JLabel("Nomor Plat Kendaraan");
        JLabel lbLayanan = new JLabel("Layanan");
        
        txtMerk = new JTextField();
        txtMerk.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan merk kendaraan");
        txtMerk.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtNoPlat = new JTextField();
        txtNoPlat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nomor plat kendaraan");
        txtNoPlat.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        cbxLayanan = new JComboBox();
        
        lbLayananSelected = new JLabel();
        lbLayananSelected.setForeground(Color.GRAY);
        lbHarga = new JLabel();
        lbHarga.setForeground(Color.GRAY);
        lbTotalHarga = new JLabel();
        lbTotalHarga.putClientProperty(FlatClientProperties.STYLE, "font:bold 16;");
        lbHargaLayanan = new JLabel();
        lbHargaLayanan.putClientProperty(FlatClientProperties.STYLE, "font:bold 16;");
        
        add(createSeparator(), "span, growx, height 2!, gapy 0 10");
        add(lbMerk, "align left, wrap");
        add(txtMerk, "growx, wrap, hmin 30");
        add(lbNoPlat, "align left, wrap, gapy 10");
        add(txtNoPlat, "growx, wrap, hmin 30");
        add(lbLayanan, "align left, wrap, gapy 10");
        add(cbxLayanan, "growx, hmin 30, wrap");
        add(lbLayananSelected, "span, split 2, align left, hmin 30");
        add(lbHarga, "hmin 30, gapx push n");
        add(lbTotalHarga, "span, split 2, align left, hmin 30, gapy 10");
        add(lbHargaLayanan, "hmin 30, gapx push n");
        add(createSeparator(), "span, growx, height 2!, gapy 10");
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    private void getLayanan() {
        DefaultComboBoxModel<String> modelCbx = new DefaultComboBoxModel<>();
        modelCbx.addElement("Pilih Layanan");

        Map<String, Layanan> hasMap = new HashMap<>();

        List<Layanan> list = servisLayanan.getData("Aktif");
        for (Layanan modelLayanan : list) {
            modelCbx.addElement(modelLayanan.getNamaLayanan());
            hasMap.put(modelLayanan.getNamaLayanan(), modelLayanan);
        }

        cbxLayanan.setModel(modelCbx);
        cbxLayanan.addActionListener(e -> {
            String namaLayanan = cbxLayanan.getSelectedItem().toString();
            if (!"Pilih Layanan".equals(namaLayanan)) {
                Layanan selectedLayanan = hasMap.get(namaLayanan);
                if (selectedLayanan != null) {
                    idLayanan = selectedLayanan.getIdLayanan();
                    lbLayananSelected.setText(selectedLayanan.getNamaLayanan());
                    
                    DecimalFormat df = new DecimalFormat("#,##0.##");
                    String hargaFormat = df.format(selectedLayanan.getHarga());
                    lbHarga.setText("Rp." + hargaFormat.replace("", ""));
                    
                    lbTotalHarga.setText("Total Harga");
                    lbHargaLayanan.setText("Rp." + hargaFormat.replace("", ""));
                    
                    showComponent();
                }
            } else {
                hideComponent();
            }
        });
    }
    
    private void setColorBasedOnSelection() {
        if (cbxLayanan.getSelectedItem().equals("Pilih Layanan")) {
            cbxLayanan.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        } else {
            cbxLayanan.putClientProperty(FlatClientProperties.STYLE, "foreground:$TextField.foreground;");
        }
    }

    private void setColorCbx() {
        cbxLayanan.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setColorBasedOnSelection();
            }
        });

        cbxLayanan.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setColorBasedOnSelection();
            }
        });

        setColorBasedOnSelection();
    }
    
    private void hideComponent() {
        lbLayananSelected.setVisible(false);
        lbHarga.setVisible(false);
        lbTotalHarga.setVisible(false);
        lbHargaLayanan.setVisible(false);
    }
    
    private void showComponent() {
        lbLayananSelected.setVisible(true);
        lbHarga.setVisible(true);
        lbTotalHarga.setVisible(true);
        lbHargaLayanan.setVisible(true);
    }
    
    public boolean validateInput() {
        if (txtMerk.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Merk kendaraan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (txtNoPlat.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nomor plat kendaraan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (cbxLayanan.getSelectedItem().equals("Pilih Layanan")) {
            Toast.show(this, Toast.Type.INFO, "Layanan harus dipilih", getOptionAlert());
            return false;
        }
        return true;
    }
    
    public void getDataLayanan() {
        if (!validateInput()) {
            throw new IllegalArgumentException("Validasi data layanan gagal");
        }
        String merkKendaraan = txtMerk.getText().trim();
        String nomorPlat = txtNoPlat.getText().trim();
        String namaLayanan = cbxLayanan.getSelectedItem().toString();

        String hargaText = lbHarga.getText().replaceAll("[^0-9]", "");
        String totalText = lbHargaLayanan.getText().replaceAll("[^0-9]", "");

        double hargaLayanan = hargaText.isEmpty() ? 0.0 : Double.parseDouble(hargaText);
        double totalHarga = totalText.isEmpty() ? 0.0 : Double.parseDouble(totalText);

        System.out.println("Harga Layanan: " + hargaLayanan);
        System.out.println("Total Harga: " + totalHarga);

        Layanan layanan = new Layanan();
        layanan.setIdLayanan(idLayanan);
        layanan.setNamaLayanan(namaLayanan);
        layanan.setHarga(hargaLayanan);

        pesanan.setLayanan(layanan);
        pesanan.setMerk(merkKendaraan);
        pesanan.setNoPlat(nomorPlat);
        pesanan.setTotalHarga(totalHarga);
    }
}