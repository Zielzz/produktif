package com.radja.form.input;

import com.radja.model.Barang;
import com.radja.model.OpnameStok;
import com.radja.model.DetailOpnameStok;
import com.radja.service.ServiceBarang;
import com.radja.service.ServiceOpnameStok;
import com.radja.form.FormOpnameStok;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.OpnameStokDAO;
import com.radja.util.AlertUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputOpnameStok extends JPanel {
    private JTextField txtTanggal;
    private JTextField txtKeterangan;
    private JTable tblDetail;
    private DefaultTableModel tblModel;
    private final ServiceBarang servisBarang;
    private final FormOpnameStok formOpnameStok;
    private final User loggedInUser;

    public FormInputOpnameStok(FormOpnameStok formOpnameStok, ServiceBarang servisBarang) {
        this.formOpnameStok = formOpnameStok;
        this.servisBarang = servisBarang;
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 600", "[50][fill,grow]"));

        JLabel lbTanggal = new JLabel("Tanggal Opname");
        JLabel lbKeterangan = new JLabel("Keterangan");
        JLabel lbDetail = new JLabel("Detail Barang");

        txtTanggal = new JTextField(LocalDate.now().toString());
        txtTanggal.setEnabled(false);

        txtKeterangan = new JTextField();
        txtKeterangan.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan keterangan");

        tblDetail = new JTable();
        tblModel = new DefaultTableModel(new Object[]{"ID Barang", "Nama Barang", "Stok Sistem", "Stok Fisik"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3; // Hanya kolom stok fisik yang bisa diedit
            }
        };
        tblDetail.setModel(tblModel);
        loadBarang();

        JButton btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnSave.addActionListener((e) -> saveData());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor;background:rgb(255,255,255);");
        btnCancel.addActionListener((e) -> ModalDialog.closeModal("form input opname"));

        add(createSeparator(), "span, growx, height 2!");
        add(lbTanggal, "align right");
        add(txtTanggal);
        add(lbKeterangan, "align right");
        add(txtKeterangan);
        add(lbDetail, "align right");
        add(new JScrollPane(tblDetail), "span, grow");
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private void loadBarang() {
        List<Barang> barangList = servisBarang.getData("Aktif");
        for (Barang barang : barangList) {
            tblModel.addRow(new Object[]{barang.getIdBarang(), barang.getNamaBarang(), barang.getStok(), ""});
        }
    }

    private boolean validasiInput() {
    // Validasi keterangan
    String keterangan = txtKeterangan.getText().trim();
    if (keterangan.isEmpty()) {
        Toast.show(this, Toast.Type.INFO, "Keterangan tidak boleh kosong", AlertUtils.getOptionAlert());
        return false;
    }

    // Validasi tabel stok fisik
    for (int i = 0; i < tblModel.getRowCount(); i++) {
        Object stokFisikObj = tblModel.getValueAt(i, 3);
        String namaBarang = tblModel.getValueAt(i, 1).toString();

        // Cek apakah stok fisik kosong atau null
        if (stokFisikObj == null || stokFisikObj.toString().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Stok fisik untuk barang " + namaBarang + " tidak boleh kosong", AlertUtils.getOptionAlert());
            return false;
        }

        // Validasi format angka dan nilai negatif
        try {
            int stokFisik = Integer.parseInt(stokFisikObj.toString().trim());
            if (stokFisik < 0) {
                Toast.show(this, Toast.Type.INFO, "Stok fisik untuk barang " + namaBarang + " tidak boleh negatif", AlertUtils.getOptionAlert());
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.show(this, Toast.Type.INFO, "Stok fisik untuk barang " + namaBarang + " harus berupa angka yang valid", AlertUtils.getOptionAlert());
            return false;
        }
    }

    return true;
}

    private void saveData() {
        if (!validasiInput()) {
            return;
        }

        OpnameStok opname = new OpnameStok();
        opname.setTanggalOpname(LocalDate.now());
        opname.setKeterangan(txtKeterangan.getText().trim());
        opname.setInsertBy(loggedInUser.getIdUser());
        opname.setIsDelete(false);

        List<DetailOpnameStok> details = new ArrayList<>();
        for (int i = 0; i < tblModel.getRowCount(); i++) {
            DetailOpnameStok detail = new DetailOpnameStok();
            detail.setIdBarang(Integer.parseInt(tblModel.getValueAt(i, 0).toString()));
            detail.setStokSistem(Integer.parseInt(tblModel.getValueAt(i, 2).toString()));
            detail.setStokFisik(Integer.parseInt(tblModel.getValueAt(i, 3).toString()));
            details.add(detail);
        }

        try {
            ServiceOpnameStok servis = new OpnameStokDAO();
            servis.insertOpnameStok(opname, details);
            Toast.show(this, Toast.Type.SUCCESS, "Opname stok berhasil disimpan", AlertUtils.getOptionAlert());
            formOpnameStok.refreshTable();
            ModalDialog.closeModal("form input opname");
        } catch (IllegalArgumentException e) {
            Toast.show(this, Toast.Type.ERROR, e.getMessage(), AlertUtils.getOptionAlert());
        }
    }
}