package com.radja.form.input;

import com.radja.dao.BiayaOperasionalDAO;
import com.radja.form.FormBiayaOperasional;
import com.radja.model.BiayaOperasional;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.service.ServiceBiayaOperasional;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import static com.radja.util.AlertUtils.getOptionAlert;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputBiayaOperasional extends JPanel {

    private JTextField txtDeskripsi;
    private JTextField txtJumlahBiaya;
    private JTextField txtTanggalBiaya;
    private JButton btnSave;
    private JButton btnCancel;
    private final ServiceBiayaOperasional servis = new BiayaOperasionalDAO();
    private BiayaOperasional model;
    private FormBiayaOperasional formBiayaOperasional;
    private int idBiaya;
    private User loggedInUser;

    public FormInputBiayaOperasional(BiayaOperasional model, FormBiayaOperasional formBiayaOperasional) {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formBiayaOperasional = formBiayaOperasional;
        if (model != null) {
            loadData();
        } else {
            // Set tanggal hari ini untuk mode "Add"
            txtTanggalBiaya.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400", "[50][fill,grow]"));

        JLabel lbDeskripsi = new JLabel("Deskripsi");
        JLabel lbJumlahBiaya = new JLabel("Jumlah Biaya");
        JLabel lbTanggalBiaya = new JLabel("Tanggal Biaya");

        txtDeskripsi = new JTextField();
        txtDeskripsi.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan deskripsi biaya");
        txtDeskripsi.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtDeskripsi.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/radja/icon/edit.svg", 0.4f));

        txtJumlahBiaya = new JTextField();
        txtJumlahBiaya.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan jumlah biaya (Rp)");
        txtJumlahBiaya.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtJumlahBiaya.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/radja/icon/money.svg", 0.4f));
        txtJumlahBiaya.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                String text = txtJumlahBiaya.getText();
                if (!Character.isDigit(c) && c != '.' || (c == '.' && text.contains("."))) {
                    e.consume();
                }
            }
        });

        txtTanggalBiaya = new JTextField();
        txtTanggalBiaya.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tanggal hari ini");
        txtTanggalBiaya.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("com/radja/icon/calendar.svg", 0.4f));
        txtTanggalBiaya.setEditable(false); // Nonaktifkan edit teks

        btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE,
                "background:@accentColor;foreground:rgb(255,255,255);");

        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.putClientProperty(FlatClientProperties.STYLE,
                "foreground:@accentColor;background:rgb(255,255,255);");

        add(createSeparator(), "span, growx, height 2!");
        add(lbDeskripsi, "align right");
        add(txtDeskripsi);
        add(lbJumlahBiaya, "align right");
        add(txtJumlahBiaya);
        add(lbTanggalBiaya, "align right");
        add(txtTanggalBiaya);
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
        if (txtDeskripsi.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Deskripsi tidak boleh kosong", getOptionAlert());
            return false;
        }
        if (txtJumlahBiaya.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Jumlah biaya tidak boleh kosong", getOptionAlert());
            return false;
        }
        try {
            double jumlahBiaya = Double.parseDouble(txtJumlahBiaya.getText().trim());
            if (jumlahBiaya < 0) {
                Toast.show(this, Toast.Type.INFO, "Jumlah biaya tidak boleh negatif", getOptionAlert());
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.show(this, Toast.Type.INFO, "Jumlah biaya harus berupa angka yang valid", getOptionAlert());
            return false;
        }
        if (txtTanggalBiaya.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Tanggal biaya tidak boleh kosong", getOptionAlert());
            return false;
        }
        return true;
    }

    private void insertData() {
        if (validasiInput()) {
            String deskripsi = txtDeskripsi.getText().trim();
            double jumlahBiaya = Double.parseDouble(txtJumlahBiaya.getText().trim());
            LocalDate tanggalBiaya = LocalDate.now(); // Gunakan tanggal hari ini

            BiayaOperasional modelBiaya = new BiayaOperasional();
            modelBiaya.setDeskripsi(deskripsi);
            modelBiaya.setJumlahBiaya(jumlahBiaya);
            modelBiaya.setTanggalBiaya(tanggalBiaya);
            modelBiaya.setInsertBy(loggedInUser.getIdUser());

            try {
                servis.insertData(modelBiaya);
                Toast.show(this, Toast.Type.SUCCESS, "Data telah berhasil ditambahkan", getOptionAlert());
                formBiayaOperasional.refreshTable();
                resetForm();
                ModalDialog.closeModal("form input");
            } catch (IllegalArgumentException e) {
                Toast.show(this, Toast.Type.ERROR, e.getMessage(), getOptionAlert());
            }
        }
    }

    private void loadData() {
        btnSave.setText("Update");
        idBiaya = model.getIdBiaya();
        txtDeskripsi.setText(model.getDeskripsi());
        txtJumlahBiaya.setText(String.format("%.2f", model.getJumlahBiaya()));
        // Set tanggal hari ini untuk mode "Update"
        txtTanggalBiaya.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    private void updateData() {
        if (validasiInput()) {
            String deskripsi = txtDeskripsi.getText().trim();
            double jumlahBiaya = Double.parseDouble(txtJumlahBiaya.getText().trim());
            LocalDate tanggalBiaya = LocalDate.now(); // Gunakan tanggal hari ini

            BiayaOperasional modelBiaya = new BiayaOperasional();
            modelBiaya.setIdBiaya(idBiaya);
            modelBiaya.setDeskripsi(deskripsi);
            modelBiaya.setJumlahBiaya(jumlahBiaya);
            modelBiaya.setTanggalBiaya(tanggalBiaya);

            try {
                servis.updateData(modelBiaya);
                Toast.show(this, Toast.Type.SUCCESS, "Data telah berhasil diperbarui", getOptionAlert());
                formBiayaOperasional.refreshTable();
                resetForm();
                ModalDialog.closeModal("form update");
            } catch (IllegalArgumentException e) {
                Toast.show(this, Toast.Type.ERROR, e.getMessage(), getOptionAlert());
            }
        }
    }

    private void resetForm() {
        txtDeskripsi.setText("");
        txtJumlahBiaya.setText("");
        txtTanggalBiaya.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))); // Reset ke tanggal hari ini
    }
}