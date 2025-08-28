package com.radja.absensi.panel;

import com.radja.absensi.service.AbsensiService;
import com.radja.model.Absensi;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class AbsensiManagerPanel extends JPanel {
    private final AbsensiService absensiService;

    private JTextField tfKaryawanId;
    private JButton btnCheckIn, btnCheckOut, btnIzinSakit, btnExport;
    private JTable tableAbsensi;

    public AbsensiManagerPanel(AbsensiService absensiService) {
        this.absensiService = absensiService;

        // Layout Panel
        setLayout(new BorderLayout());

        // Panel for controls
        JPanel panelControls = new JPanel();
        panelControls.setLayout(new FlowLayout());

        tfKaryawanId = new JTextField(10);
        panelControls.add(new JLabel("ID Karyawan:"));
        panelControls.add(tfKaryawanId);

        btnCheckIn = new JButton("Check In");
        btnCheckIn.addActionListener(e -> checkInAction());
        panelControls.add(btnCheckIn);

        btnCheckOut = new JButton("Check Out");
        btnCheckOut.addActionListener(e -> checkOutAction());
        panelControls.add(btnCheckOut);

        btnIzinSakit = new JButton("Izin Sakit");
        btnIzinSakit.addActionListener(e -> izinSakitAction());
        panelControls.add(btnIzinSakit);

        add(panelControls, BorderLayout.NORTH);

        // Table for Absensi
        tableAbsensi = new JTable();
        add(new JScrollPane(tableAbsensi), BorderLayout.CENTER);
    }

    // Action for Check In
    private void checkInAction() {
        int idKaryawan = Integer.parseInt(tfKaryawanId.getText());
        if (absensiService.checkIn(idKaryawan)) {
            JOptionPane.showMessageDialog(this, "Check-In Berhasil!");
        } else {
            JOptionPane.showMessageDialog(this, "Check-In Gagal. Karyawan sudah Check-In.");
        }
    }

    // Action for Check Out
    private void checkOutAction() {
        int idKaryawan = Integer.parseInt(tfKaryawanId.getText());
        if (absensiService.checkOut(idKaryawan)) {
            JOptionPane.showMessageDialog(this, "Check-Out Berhasil!");
        } else {
            JOptionPane.showMessageDialog(this, "Check-Out Gagal.");
        }
    }

    // Action for Izin Sakit
    private void izinSakitAction() {
        int idKaryawan = Integer.parseInt(tfKaryawanId.getText());
        String jenis = JOptionPane.showInputDialog(this, "Masukkan Jenis Izin (IZIN/SAKIT):");
        String keterangan = JOptionPane.showInputDialog(this, "Masukkan Keterangan:");

        if (absensiService.catatIzinSakit(idKaryawan, jenis, keterangan)) {
            JOptionPane.showMessageDialog(this, "Izin/Sakit Berhasil Dicatat.");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mencatat izin/sakit.");
        }
    }

}
