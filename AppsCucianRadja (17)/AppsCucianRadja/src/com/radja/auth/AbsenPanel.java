package com.radja.auth;

import com.radja.dao.KaryawanDAO;
import com.radja.model.Karyawan;
import com.radja.service.ServiceKaryawan;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import raven.modal.Toast;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import net.miginfocom.swing.MigLayout;

public class AbsenPanel extends JPanel {
    private final ServiceKaryawan servisKaryawan = new KaryawanDAO();  // Menggunakan servis yang benar
    private JTextField txtRfid;
    private JButton btnKembali;

    public AbsenPanel(Runnable kembaliAction) {
        init();
        btnKembali.addActionListener(e -> kembaliAction.run());
    }

    private void init() {
        setLayout(new MigLayout("wrap, insets 30", "[fill, 350]", "[center]"));
        putClientProperty(FlatClientProperties.STYLE, "arc:20; [light]background:rgb(255,255,255);");

        // Judul
        JLabel lblTitle = new JLabel("ABSENSI KARYAWAN");
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        add(lblTitle, "align center, gapy 20 30");

        // Field RFID
        txtRfid = new JTextField();
        txtRfid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tempel Kartu RFID");
        txtRfid.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, 
            new FlatSVGIcon("com/radja/icon/rfid.svg", 25, 25));
        txtRfid.putClientProperty(FlatClientProperties.STYLE, "arc:10; font: +2");
        add(txtRfid, "h 40!, gapy 0 20");

        // Tombol Kembali
        btnKembali = new JButton("Kembali ke Login");
        btnKembali.putClientProperty(FlatClientProperties.STYLE, "" +
            "[light]background:#607D8B;" +
            "font:bold; borderWidth:0; arc:10");
        add(btnKembali, "h 40!, gapy 20 0");

        // Handler RFID
        txtRfid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    prosesAbsen();
                }
            }
        });
    }

    // Method untuk memproses absen
    private void prosesAbsen() {
        String rfid = txtRfid.getText().trim();
        if (rfid.isEmpty()) {
            Toast.show(this, Toast.Type.ERROR, "Harap tempel kartu RFID karyawan!");
            return;
        }

        // Ganti dengan service yang mengambil data karyawan (bukan user)
        Karyawan karyawan = servisKaryawan.getKaryawanByRFID(rfid);
        if (karyawan != null) {
            boolean sukses = servisKaryawan.prosesAbsensi(karyawan.getIdKaryawan());
            if (sukses) {
                Toast.show(this, Toast.Type.SUCCESS, "Absen berhasil!");
                txtRfid.setText("");  // Reset field setelah sukses absen
            } else {
                Toast.show(this, Toast.Type.ERROR, "Gagal menyimpan absen");
            }
        } else {
            Toast.show(this, Toast.Type.ERROR, "RFID karyawan tidak terdaftar!");
        }
    }

    // Fokuskan pada field RFID
    public void fokusKeRFID() {
        txtRfid.requestFocusInWindow();
    }
}
