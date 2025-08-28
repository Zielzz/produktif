package com.radja.form.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.PelangganDAO;
import com.radja.form.FormPelanggan;
import com.radja.main.FormManager;
import com.radja.model.Pelanggan;
import com.radja.model.User;
import com.radja.service.ServicePelanggan;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;

public class FormInputPelanggan extends JPanel {

    private JTextField txtIdPelanggan;
    private JTextField txtNama;
    private JTextField txtTelepon;
    private JTextField txtAlamat;
    private JComboBox<String> cbxMemberStatus;
    private JButton btnSave;
    private JButton btnCancel;
    private final ServicePelanggan servis = new PelangganDAO();
    private Pelanggan model;
    private FormPelanggan formPelanggan;
    private User loggedInUser;

    public FormInputPelanggan(Pelanggan model, FormPelanggan formPelanggan) {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formPelanggan = formPelanggan;
        if (model != null) {
            loadData();
        } else {
            generateIdPelanggan();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx,insets 5 30 5 30,wrap 2,gap 20,width 400", "[50][fill,grow]"));

        txtIdPelanggan = new JTextField();
        txtIdPelanggan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "ID Pelanggan");
        txtIdPelanggan.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtNama = new JTextField();
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama pelanggan");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtTelepon = new JTextField();
        txtTelepon.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nomor telepon pelanggan");
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

        txtAlamat = new JTextField();
        txtAlamat.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan alamat pelanggan");
        txtAlamat.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        cbxMemberStatus = new JComboBox<>(new String[]{"tidak aktif", "aktif"});
        cbxMemberStatus.putClientProperty(FlatClientProperties.STYLE, "arc:5");

        btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE,
                "background:@accentColor;foreground:#FFFFFF");

        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.putClientProperty(FlatClientProperties.STYLE,
                "foreground:@accentColor;background:#FFFFFF");

        add(createSeparator(), "span,growx,height 2!");
        add(new JLabel("ID Pelanggan"), "align right");
        add(txtIdPelanggan);
        add(new JLabel("Nama Pelanggan"), "align right");
        add(txtNama);
        add(new JLabel("Telepon"), "align right");
        add(txtTelepon);
        add(new JLabel("Alamat"), "align right");
        add(txtAlamat);
        add(new JLabel("Status Member"), "align right");
        add(cbxMemberStatus);
        add(createSeparator(), "span,growx,height 2!");
        add(btnSave, "span,split 2,align center,sg btn,hmin 30");
        add(btnCancel, "sg btn,hmin 30");

        btnSave.addActionListener(e -> {
            if (model == null) {
                insertData();
            } else {
                updateData();
            }
        });

        btnCancel.addActionListener(e -> {
            ModalDialog.closeModal(model == null ? "form input" : "form update");
        });
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private boolean validasiInput() {
        if (txtIdPelanggan.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Pelanggan gagal tergenerate", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (txtNama.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama pelanggan tidak boleh kosong", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (txtTelepon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nomor telepon pelanggan tidak boleh kosong", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (txtTelepon.getText().length() < 10) {
            JOptionPane.showMessageDialog(this, "Nomor telepon minimal 10 digit", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (txtAlamat.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Alamat pelanggan tidak boleh kosong", "Info", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }

    private void insertData() {
        if (validasiInput()) {
            Pelanggan modelPelanggan = new Pelanggan();
            modelPelanggan.setIdPelanggan(txtIdPelanggan.getText());
            modelPelanggan.setNamaPelanggan(txtNama.getText());
            modelPelanggan.setTelepon(txtTelepon.getText());
            modelPelanggan.setAlamat(txtAlamat.getText());
            modelPelanggan.setMemberStatus(cbxMemberStatus.getSelectedItem().toString());
            modelPelanggan.setInsertBy(loggedInUser.getIdUser());
            servis.insertData(modelPelanggan);
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            formPelanggan.refreshTable();
            resetForm();
            generateIdPelanggan();
            ModalDialog.closeModal("form input");
        }
    }

    private void loadData() {
        btnSave.setText("Update");
        txtIdPelanggan.setText(model.getIdPelanggan());
        txtNama.setText(model.getNamaPelanggan());
        txtTelepon.setText(model.getTelepon());
        txtAlamat.setText(model.getAlamat());
        cbxMemberStatus.setSelectedItem(model.getMemberStatus());
        txtIdPelanggan.setEditable(false);
    }

    private void updateData() {
        if (validasiInput()) {
            Pelanggan modelPelanggan = new Pelanggan();
            modelPelanggan.setIdPelanggan(txtIdPelanggan.getText());
            modelPelanggan.setNamaPelanggan(txtNama.getText());
            modelPelanggan.setTelepon(txtTelepon.getText());
            modelPelanggan.setAlamat(txtAlamat.getText());
            modelPelanggan.setMemberStatus(cbxMemberStatus.getSelectedItem().toString());
            modelPelanggan.setUpdateBy(loggedInUser.getIdUser());
            servis.updateData(modelPelanggan);
            JOptionPane.showMessageDialog(this, "Data berhasil diperbarui", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            formPelanggan.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        }
    }

    private void resetForm() {
        txtIdPelanggan.setText("");
        txtNama.setText("");
        txtTelepon.setText("");
        txtAlamat.setText("");
        cbxMemberStatus.setSelectedIndex(0);
    }

    private void generateIdPelanggan() {
        txtIdPelanggan.setText(servis.generateIdPelanggan());
        txtIdPelanggan.setEditable(false);
    }
}