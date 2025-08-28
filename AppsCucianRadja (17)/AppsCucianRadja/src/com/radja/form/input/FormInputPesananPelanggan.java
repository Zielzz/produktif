package com.radja.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.radja.dao.PelangganDAO;
import com.radja.model.Pelanggan;
import com.radja.service.ServicePelanggan;
import static com.radja.util.AlertUtils.getOptionAlert;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormInputPesananPelanggan extends JPanel {

    private JTextField txtNama;
    private JTextField txtTelepon;
    private JTextArea txtAlamat;
    
    private final ServicePelanggan servisPelanggan = new PelangganDAO();
    private String idPelanggan;
    private Pelanggan pelanggan;
    
    public FormInputPesananPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
        
        init();
        generateIdPelanggan();
    }

    private void init() {
        setLayout(new MigLayout("fillx, wrap, width 300"));
        
        JLabel lbNama = new JLabel("Nama");
        JLabel lbTelepon = new JLabel("Nomor Handphone");
        JLabel lbAlamat = new JLabel("Alamat");
        
        txtNama = new JTextField();
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama Pelanggan");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtTelepon = new JTextField();
        txtTelepon.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nomor handphone (min 11 digit)");
        txtTelepon.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        ((AbstractDocument) txtTelepon.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) 
                    throws BadLocationException {
                StringBuilder sb = new StringBuilder();
                for (char c : string.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                if (fb.getDocument().getLength() + sb.length() <= 15) {
                    super.insertString(fb, offset, sb.toString(), attr);
                }
            }

            @Override
            public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                    throws BadLocationException {
                if (text == null) {
                    super.replace(fb, offset, length, text, attrs);
                    return;
                }
                StringBuilder sb = new StringBuilder();
                for (char c : text.toCharArray()) {
                    if (Character.isDigit(c)) {
                        sb.append(c);
                    }
                }
                if (fb.getDocument().getLength() - length + sb.length() <= 15) {
                    super.replace(fb, offset, length, sb.toString(), attrs);
                }
            }
        });
        
        txtAlamat = new JTextArea();
        txtAlamat.setWrapStyleWord(true);
        txtAlamat.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(txtAlamat);
        
        add(createSeparator(), "span, growx, height 2!, gapy 0 10");
        add(lbNama, "align left, wrap");
        add(txtNama, "growx, wrap, hmin 30");
        add(lbTelepon, "align left, wrap, gapy 10");
        add(txtTelepon, "growx, wrap, hmin 30");
        add(lbAlamat, "align left, wrap, gapy 10");
        add(scroll, "growx, hmin 100, wrap");
        add(createSeparator(), "span, growx, height 2!, gapy 10");
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    private void generateIdPelanggan() {
        idPelanggan = servisPelanggan.generateIdPelanggan();
        System.out.println("ID Pelanggan " + idPelanggan);
    }
    
    public boolean validateInput() {
        if (txtNama.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama pelanggan tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (txtTelepon.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nomor telepon tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (txtTelepon.getText().trim().length() <= 10) {
            Toast.show(this, Toast.Type.INFO, "Nomor telepon harus lebih dari 10 digit", getOptionAlert());
            return false;
        }
        if (txtAlamat.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Alamat tidak boleh kosong", getOptionAlert());
            return false;
        }
        return true;
    }
    
    public void getDataPelanggan() {
        if (!validateInput()) {
            throw new IllegalArgumentException("Validasi data pelanggan gagal");
        }
        String namaPelanggan = txtNama.getText().trim();
        String telepon = txtTelepon.getText().trim();
        String alamat = txtAlamat.getText().trim();
        
        pelanggan.setIdPelanggan(idPelanggan);
        pelanggan.setNamaPelanggan(namaPelanggan);
        pelanggan.setTelepon(telepon);
        pelanggan.setAlamat(alamat);
    }
}