package com.radja.form.input;

import com.radja.dao.BarangDAO;
import com.radja.form.FormBarang;
import com.radja.main.FormManager;
import com.radja.model.Barang;
import com.radja.service.ServiceBarang;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.model.User;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputBarang extends JPanel {

    private JComboBox<String> cmbNamaBarang;
    private JTextField txtSatuan;
    private JTextField txtIsiSachet;
    private JTextField txtStok;
    private JTextField txtHargaSatuan;
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private final ServiceBarang servis = new BarangDAO();
    private Barang model;
    private FormBarang formBarang;
    private int idBarang;
    private final User loggedInUser;
    
    public FormInputBarang(Barang model, FormBarang formBarang) {
        init();
        
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formBarang = formBarang;
        if (model != null) {
            loadData();
            txtStok.setEditable(false);
            txtStok.setFocusable(false);
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill,grow]"));
        
        JLabel lbNama = new JLabel("Nama Barang");
        JLabel lbSatuan = new JLabel("Satuan");
        JLabel lbIsiSachet = new JLabel("Isi Satuan (Opsional)");
        JLabel lbStok = new JLabel("Stok");
        JLabel lbHargaSatuan = new JLabel("Harga Satuan");
        
        cmbNamaBarang = new JComboBox<>();
        initComboNamaBarang();
        cmbNamaBarang.setEditable(true);
        cmbNamaBarang.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Pilih atau masukkan nama barang");
        cmbNamaBarang.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadBarangData();
                }
            }
        });
        
        txtSatuan = new JTextField();
        txtSatuan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan satuan barang");
        txtSatuan.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtIsiSachet = new JTextField();
        txtIsiSachet.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Misal: 15 untuk sachet");
        txtIsiSachet.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtIsiSachet.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        
        txtStok = new JTextField();
        txtStok.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan stok tambahan");
        txtStok.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtHargaSatuan = new JTextField();
        txtHargaSatuan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan harga satuan (Rp)");
        txtHargaSatuan.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtStok.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
        
        txtHargaSatuan.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = txtHargaSatuan.getText();
                if (!Character.isDigit(c) && c != '.' || (c == '.' && text.contains("."))) {
                    e.consume();
                }
            }
        });
        
        btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);");
        
        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        
        add(createSeparator(), "span, growx, height 2!");
        add(lbNama, "align right");
        add(cmbNamaBarang);
        add(lbSatuan, "align right");
        add(txtSatuan);
        add(lbIsiSachet, "align right");
        add(txtIsiSachet);
        add(lbStok, "align right");
        add(txtStok);
        add(lbHargaSatuan, "align right");
        add(txtHargaSatuan);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
                
        btnSave.addActionListener((e) -> {
            if (model == null) {
                insertData();
            } else {
                updateData();
            }
        });
        
        btnCancel.addActionListener((e) -> {
            if (model == null) {
                ModalDialog.closeModal("form input");
            } else {
                ModalDialog.closeModal("form update");
            }
        });
    }
    
    private void initComboNamaBarang() {
        List<Barang> barangList = servis.getData("Aktif");
        cmbNamaBarang.addItem(""); // Allow empty selection
        for (Barang barang : barangList) {
            cmbNamaBarang.addItem(barang.getNamaBarang());
        }
    }
    
    private void loadBarangData() {
        String selectedName = cmbNamaBarang.getSelectedItem() != null ? cmbNamaBarang.getSelectedItem().toString().trim() : "";
        txtStok.setText(""); // Clear txtStok when a name is selected
        
        if (!selectedName.isEmpty()) {
            List<Barang> barangList = servis.getData("Aktif");
            for (Barang barang : barangList) {
                if (barang.getNamaBarang().equalsIgnoreCase(selectedName)) {
                    txtSatuan.setText(barang.getSatuan());
                    txtIsiSachet.setText(barang.getIsiSachet() != null ? String.valueOf(barang.getIsiSachet().intValue()) : "");
                    txtHargaSatuan.setText(String.format("%.2f", barang.getHargaSatuan()));
                    idBarang = barang.getIdBarang(); // Store ID for existing item
                    
                    // Disable fields for existing item
                    txtSatuan.setEditable(false);
                    txtSatuan.setFocusable(false);
                    txtIsiSachet.setEditable(false);
                    txtIsiSachet.setFocusable(false);
                    txtHargaSatuan.setEditable(false);
                    txtHargaSatuan.setFocusable(false);
                    return;
                }
            }
        }
        // Clear and enable fields if no matching item is found or empty selection
        txtSatuan.setText("");
        txtIsiSachet.setText("");
        txtHargaSatuan.setText("");
        idBarang = 0; // Reset ID if no matching item
        
        txtSatuan.setEditable(true);
        txtSatuan.setFocusable(true);
        txtIsiSachet.setEditable(true);
        txtIsiSachet.setFocusable(true);
        txtHargaSatuan.setEditable(true);
        txtHargaSatuan.setFocusable(true);
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    private boolean validasiInput() {
        if (cmbNamaBarang.getSelectedItem() == null || cmbNamaBarang.getSelectedItem().toString().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama barang tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (txtSatuan.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Satuan barang tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (model == null && txtStok.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Stok tambahan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (txtHargaSatuan.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Harga satuan tidak boleh kosong", getOptionAlert());
            return false;
        }
        try {
            double hargaSatuan = Double.parseDouble(txtHargaSatuan.getText().trim());
            if (hargaSatuan < 0) {
                Toast.show(this, Toast.Type.INFO, "Harga satuan tidak boleh negatif", getOptionAlert());
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.show(this, Toast.Type.INFO, "Harga satuan harus berupa angka yang valid", getOptionAlert());
            return false;
        }
        return true;
    }

    private void insertData() {
        if (validasiInput()) {
            String namaBarang = cmbNamaBarang.getSelectedItem().toString().trim();
            String satuan = txtSatuan.getText().trim();
            Double isiSachet = txtIsiSachet.getText().trim().isEmpty() ? null : Double.parseDouble(txtIsiSachet.getText().trim());
            int stok = txtStok.getText().isEmpty() ? 0 : Integer.parseInt(txtStok.getText().trim());
            double hargaSatuan = Double.parseDouble(txtHargaSatuan.getText().trim());
            
            // Check if item already exists
            List<Barang> barangList = servis.getData("Aktif");
            Barang existingBarang = null;
            for (Barang barang : barangList) {
                if (barang.getNamaBarang().equalsIgnoreCase(namaBarang)) {
                    existingBarang = barang;
                    break;
                }
            }
            
            if (existingBarang != null) {
                // Item exists, update stock in barang table
                existingBarang.setStok(existingBarang.getStok() + stok);
                existingBarang.setSatuan(satuan);
                existingBarang.setIsiSachet(isiSachet);
                existingBarang.setHargaSatuan(hargaSatuan);
                existingBarang.setUpdateBy(loggedInUser.getIdUser());
                
                // Create a new Barang object for logging with only the added stock
                Barang logBarang = new Barang();
                logBarang.setIdBarang(existingBarang.getIdBarang());
                logBarang.setNamaBarang(namaBarang);
                logBarang.setStok(stok); // Log only the added stock
                logBarang.setHargaSatuan(hargaSatuan);
                logBarang.setSatuan(satuan);
                logBarang.setIsiSachet(isiSachet);
                
                try {
                    servis.updateData(existingBarang);
                    servis.logBarangMasuk(logBarang, loggedInUser.getIdUser());
                    Toast.show(this, Toast.Type.SUCCESS, "Stok barang telah berhasil diperbarui", getOptionAlert());
                    formBarang.refreshTable();
                    resetForm();
                    ModalDialog.closeModal("form input");
                } catch (IllegalArgumentException e) {
                    Toast.show(this, Toast.Type.ERROR, e.getMessage(), getOptionAlert());
                }
            } else {
                // New item, insert as usual
                Barang modelBarang = new Barang();
                modelBarang.setNamaBarang(namaBarang);
                modelBarang.setSatuan(satuan);
                modelBarang.setIsiSachet(isiSachet);
                modelBarang.setStok(stok);
                modelBarang.setHargaSatuan(hargaSatuan);
                modelBarang.setInsertBy(loggedInUser.getIdUser());
                
                try {
                    servis.insertData(modelBarang);
                    servis.logBarangMasuk(modelBarang, loggedInUser.getIdUser());
                    Toast.show(this, Toast.Type.SUCCESS, "Data telah berhasil ditambahkan", getOptionAlert());
                    formBarang.refreshTable();
                    resetForm();
                    ModalDialog.closeModal("form input");
                } catch (IllegalArgumentException e) {
                    Toast.show(this, Toast.Type.ERROR, e.getMessage(), getOptionAlert());
                }
            }
        }
    }
    
    private void loadData() {
        btnSave.setText("Update");
        
        idBarang = model.getIdBarang();
        cmbNamaBarang.setSelectedItem(model.getNamaBarang());
        txtSatuan.setText(model.getSatuan());
        txtIsiSachet.setText(model.getIsiSachet() != null ? String.valueOf(model.getIsiSachet().intValue()) : "");
        txtStok.setText(String.valueOf(model.getStok()));
        txtHargaSatuan.setText(String.format("%.2f", model.getHargaSatuan()));
        
        // Disable fields for update mode
        txtSatuan.setEditable(false);
        txtSatuan.setFocusable(false);
        txtIsiSachet.setEditable(false);
        txtIsiSachet.setFocusable(false);
        txtHargaSatuan.setEditable(false);
        txtHargaSatuan.setFocusable(false);
    }
    
    private void updateData() {
        if (validasiInput()) {
            String namaBarang = cmbNamaBarang.getSelectedItem().toString().trim();
            String satuan = txtSatuan.getText().trim();
            Double isiSachet = txtIsiSachet.getText().trim().isEmpty() ? null : Double.parseDouble(txtIsiSachet.getText().trim());
            double hargaSatuan = Double.parseDouble(txtHargaSatuan.getText().trim());
            
            Barang modelBarang = new Barang();
            modelBarang.setIdBarang(idBarang);
            modelBarang.setNamaBarang(namaBarang);
            modelBarang.setSatuan(satuan);
            modelBarang.setIsiSachet(isiSachet);
            modelBarang.setStok(model.getStok());
            modelBarang.setHargaSatuan(hargaSatuan);
            modelBarang.setUpdateBy(loggedInUser.getIdUser());
            
            try {
                servis.updateData(modelBarang);
                Toast.show(this, Toast.Type.SUCCESS, "Data telah berhasil diperbarui", getOptionAlert());
                formBarang.refreshTable();
                resetForm();
                ModalDialog.closeModal("form update");
            } catch (IllegalArgumentException e) {
                Toast.show(this, Toast.Type.ERROR, e.getMessage(), getOptionAlert());
            }
        }
    }
    
    private void resetForm() {
        cmbNamaBarang.setSelectedIndex(0);
        txtSatuan.setText("");
        txtIsiSachet.setText("");
        txtStok.setText("");
        txtHargaSatuan.setText("");
        
        // Re-enable fields after reset
        txtSatuan.setEditable(true);
        txtSatuan.setFocusable(true);
        txtIsiSachet.setEditable(true);
        txtIsiSachet.setFocusable(true);
        txtHargaSatuan.setEditable(true);
        txtHargaSatuan.setFocusable(true);
    }
}