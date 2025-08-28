package com.radja.form.input;

import com.radja.dao.KaryawanDAO;
import com.radja.form.FormKaryawan;
import com.radja.main.FormManager;
import com.radja.model.Karyawan;
import com.radja.service.ServiceKaryawan;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.model.User;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputKaryawan extends JPanel {

    private JTextField txtNama;
    private JTextField txtTelepon;
    private JTextField txtAlamat;
    private JTextField txtRfid; // Tambahan field untuk RFID

    private JButton btnSave;
    private JButton btnCancel;

    private final ServiceKaryawan servis = new KaryawanDAO();
    private Karyawan model;
    private FormKaryawan formKaryawan;
    private int idKaryawan;
    private User loggedInUser;

    public FormInputKaryawan(Karyawan model, FormKaryawan formKaryawan) {
        init();

        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formKaryawan = formKaryawan;
        if (model != null) {
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400", "[50][fill,grow]"));

        JLabel lbNama = new JLabel("Nama Karyawan");
        JLabel lbTelepon = new JLabel("Telepon");
        JLabel lbAlamat = new JLabel("Alamat");
        JLabel lbRfid = new JLabel("RFID"); // Label untuk RFID

        txtNama = new JTextField();
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama Karyawan");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtTelepon = new JTextField();
        txtTelepon.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukan nomor telepon Karyawan");
        txtTelepon.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        // Add document filter to only allow numbers and limit to 15 digits
        ((AbstractDocument) txtTelepon.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
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
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
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

        txtAlamat = new JTextField();
        txtAlamat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan alamat Karyawan");
        txtAlamat.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        // Tambahan field untuk RFID
        txtRfid = new JTextField();
        txtRfid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nomor RFID");
        txtRfid.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

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
        add(txtNama);
        add(lbTelepon, "align right");
        add(txtTelepon);
        add(lbAlamat, "align right");
        add(txtAlamat);
        add(lbRfid, "align right"); // Tambahkan label RFID
        add(txtRfid); // Tambahkan field RFID
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

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private boolean validasiInput() {
        boolean valid = false;
        if (txtNama.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama karyawan tidak boleh kosong", getOptionAlert());
        } else if (txtTelepon.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nomor telepon karyawan tidak boleh kosong", getOptionAlert());
        } else if (txtTelepon.getText().length() < 10) {
            Toast.show(this, Toast.Type.INFO, "Nomor telepon minimal 10 digit", getOptionAlert());
        } else if (txtAlamat.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Alamat karyawan tidak boleh kosong", getOptionAlert());
        } else if (txtRfid.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nomor RFID tidak boleh kosong", getOptionAlert());
        } else {
            valid = true;
        }
        return valid;
    }

    private void insertData() {
        if (validasiInput() == true) {
            String namaKaryawan = txtNama.getText();
            String telepon = txtTelepon.getText();
            String alamat = txtAlamat.getText();
            String rfid = txtRfid.getText(); // Ambil data RFID

            Karyawan modelKaryawan = new Karyawan();
            modelKaryawan.setNamaKaryawan(namaKaryawan);
            modelKaryawan.setTelepon(telepon);
            modelKaryawan.setAlamat(alamat);
            modelKaryawan.setRfid(rfid); // Set data RFID
            modelKaryawan.setInsertBy(loggedInUser.getIdUser());

            servis.insertData(modelKaryawan);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

            formKaryawan.refreshTable();
            resetForm();
        }
    }

    private void loadData() {
        btnSave.setText("Update");

        idKaryawan = model.getIdKaryawan();
        txtNama.setText(model.getNamaKaryawan());
        txtTelepon.setText(model.getTelepon());
        txtAlamat.setText(model.getAlamat());
        txtRfid.setText(model.getRfid()); // Load data RFID
    }

    private void updateData() {
        if (validasiInput() == true) {
            String namaKaryawan = txtNama.getText();
            String telepon = txtTelepon.getText();
            String alamat = txtAlamat.getText();
            String rfid = txtRfid.getText(); // Ambil data RFID

            Karyawan modelKaryawan = new Karyawan();
            modelKaryawan.setIdKaryawan(idKaryawan);
            modelKaryawan.setNamaKaryawan(namaKaryawan);
            modelKaryawan.setTelepon(telepon);
            modelKaryawan.setAlamat(alamat);
            modelKaryawan.setRfid(rfid); // Set data RFID
            modelKaryawan.setUpdateBy(loggedInUser.getIdUser());

            servis.updateData(modelKaryawan);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

            formKaryawan.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtTelepon.setText("");
        txtAlamat.setText("");
        txtRfid.setText(""); // Reset field RFID
    }
}