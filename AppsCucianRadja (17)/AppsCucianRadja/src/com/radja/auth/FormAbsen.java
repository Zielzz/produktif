package com.radja.auth;

import com.radja.dao.AbsensiDAOImpl;
import com.radja.main.Form;
import com.radja.model.Karyawan;
import com.radja.absensi.service.AbsensiService;
import com.formdev.flatlaf.FlatClientProperties;
import static com.radja.util.AlertUtils.getOptionAlert;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import raven.modal.Toast;
import java.io.File;
import java.time.ZoneId;

public class FormAbsen extends Form {
    private final AbsensiService servis = new AbsensiService(new AbsensiDAOImpl());
    private JTextField txtRfid;
    private JTextField txtStartDate; // Replaced JDateChooser with JTextField
    private JTextField txtEndDate;   // Replaced JDateChooser with JTextField
    private JButton btnAbsen;
    private JButton btnIzinSakit;
    private JButton btnGenerateReport;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // For parsing dates

    public FormAbsen() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("wrap, insets 20", "[fill, 300]", "[center]"));

        JLabel lblTitle = new JLabel("ABSENSI KARYAWAN");
        lblTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +10");
        add(lblTitle, "gapy 20 30");

        txtRfid = new JTextField();
        txtRfid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tap Kartu RFID");
        txtRfid.putClientProperty(FlatClientProperties.STYLE, "arc:10; font: +2");
        add(txtRfid, "h 40!, gapy 0 20");

        // Text fields for date input
        txtStartDate = new JTextField();
        txtStartDate.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tanggal Mulai (yyyy-MM-dd)");
        txtStartDate.putClientProperty(FlatClientProperties.STYLE, "arc:10; font: +2");
        add(txtStartDate, "h 40!, gapy 10");

        txtEndDate = new JTextField();
        txtEndDate.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Tanggal Selesai (yyyy-MM-dd)");
        txtEndDate.putClientProperty(FlatClientProperties.STYLE, "arc:10; font: +2");
        add(txtEndDate, "h 40!, gapy 10");

        JPanel buttonPanel = new JPanel(new MigLayout("insets 0", "[fill][fill][fill]", "[fill]"));
        btnAbsen = new JButton("Absen");
        btnAbsen.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:#4CAF50;"
                + "[light]foreground:rgb(255,255,255); "
                + "arc:10; borderWidth:0; "
                + "focusWidth:0; innerFocusWidth:0; "
                + "font:bold 16");
        buttonPanel.add(btnAbsen, "h 40!");

        btnIzinSakit = new JButton("Izin/Sakit");
        btnIzinSakit.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:#FFC107;"
                + "[light]foreground:rgb(255,255,255); "
                + "arc:10; borderWidth:0; "
                + "focusWidth:0; innerFocusWidth:0; "
                + "font:bold 16");
        buttonPanel.add(btnIzinSakit, "h 40!, gapx 10");

        btnGenerateReport = new JButton("Buat Laporan");
        btnGenerateReport.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:#2196F3;"
                + "[light]foreground:rgb(255,255,255); "
                + "arc:10; borderWidth:0; "
                + "focusWidth:0; innerFocusWidth:0; "
                + "font:bold 16");
        buttonPanel.add(btnGenerateReport, "h 40!, gapx 10");
        add(buttonPanel, "gapy 20");

        txtRfid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processAbsen();
                }
            }
        });

        btnAbsen.addActionListener(e -> processAbsen());
        btnIzinSakit.addActionListener(e -> processIzinSakit());
        btnGenerateReport.addActionListener(e -> processGenerateReport());
    }

    private void processAbsen() {
        if (!servis.isDatabaseConnected()) {
            Toast.show(this, Toast.Type.ERROR, "Koneksi database gagal. Silakan hubungi administrator.", getOptionAlert());
            return;
        }

        String rfid = txtRfid.getText().trim();
        if (rfid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap tap kartu RFID terlebih dahulu!");
            return;
        }

        Karyawan karyawan = servis.getKaryawanByRfid(rfid);
        if (karyawan != null) {
            if (servis.isSudahAbsenMasuk(karyawan.getIdKaryawan(), LocalDate.now())) {
                if (servis.isSudahAbsenPulang(karyawan.getIdKaryawan(), LocalDate.now())) {
                    Toast.show(this, Toast.Type.ERROR, "Karyawan sudah absen HADIR (Pulang) hari ini.", getOptionAlert());
                } else {
                    boolean success = servis.checkOut(karyawan.getIdKaryawan());
                    if (success) {
                        Toast.show(this, Toast.Type.SUCCESS, "Absen HADIR (Pulang) berhasil!", getOptionAlert());
                    } else {
                        String errorMsg = servis.getLastErrorMessage();
                        Toast.show(this, Toast.Type.ERROR, "Gagal menyimpan absensi HADIR (Pulang): " + (errorMsg.isEmpty() ? "Periksa status absensi." : errorMsg), getOptionAlert());
                    }
                }
            } else {
                boolean success = servis.checkIn(karyawan.getIdKaryawan());
                if (success) {
                    Toast.show(this, Toast.Type.SUCCESS, "Absen HADIR (Masuk) berhasil!", getOptionAlert());
                } else {
                    String errorMsg = servis.getLastErrorMessage();
                    Toast.show(this, Toast.Type.ERROR, "Gagal menyimpan absensi HADIR (Masuk): " + (errorMsg.isEmpty() ? "Periksa status absensi." : errorMsg), getOptionAlert());
                }
            }
            resetForm();
        } else {
            Toast.show(this, Toast.Type.ERROR, "RFID tidak terdaftar!", getOptionAlert());
        }
    }

    private void processIzinSakit() {
        if (!servis.isDatabaseConnected()) {
            Toast.show(this, Toast.Type.ERROR, "Koneksi database gagal. Silakan hubungi administrator.", getOptionAlert());
            return;
        }

        String rfid = txtRfid.getText().trim();
        if (rfid.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap tap kartu RFID terlebih dahulu!");
            return;
        }

        Karyawan karyawan = servis.getKaryawanByRfid(rfid);
        if (karyawan != null) {
            if (servis.isSudahAbsenMasuk(karyawan.getIdKaryawan(), LocalDate.now()) || servis.isSudahAbsenPulang(karyawan.getIdKaryawan(), LocalDate.now())) {
                Toast.show(this, Toast.Type.ERROR, "Karyawan sudah memiliki absensi hari ini.", getOptionAlert());
                return;
            }

            String keterangan = JOptionPane.showInputDialog(this, "Masukkan keterangan (contoh: Izin: MU, Sakit, dll):", "Input Keterangan", JOptionPane.PLAIN_MESSAGE);
            if (keterangan != null && !keterangan.trim().isEmpty()) {
                boolean success = servis.catatIzinAtauSakit(karyawan.getIdKaryawan(), keterangan);
                if (success) {
                    Toast.show(this, Toast.Type.SUCCESS, "Absensi TIDAK HADIR (" + keterangan + ") berhasil!", getOptionAlert());
                } else {
                    String errorMsg = servis.getLastErrorMessage();
                    Toast.show(this, Toast.Type.ERROR, "Gagal menyimpan absensi TIDAK HADIR: " + (errorMsg.isEmpty() ? "Periksa status absensi." : errorMsg), getOptionAlert());
                }
                resetForm();
            } else {
                Toast.show(this, Toast.Type.ERROR, "Keterangan tidak boleh kosong!", getOptionAlert());
            }
        } else {
            Toast.show(this, Toast.Type.ERROR, "RFID tidak terdaftar!", getOptionAlert());
        }
    }

    private void processGenerateReport() {
        if (!servis.isDatabaseConnected()) {
            Toast.show(this, Toast.Type.ERROR, "Koneksi database gagal. Silakan hubungi administrator.", getOptionAlert());
            return;
        }

        // Validate date inputs
        String startDateStr = txtStartDate.getText().trim();
        String endDateStr = txtEndDate.getText().trim();
        if (startDateStr.isEmpty() || endDateStr.isEmpty()) {
            Toast.show(this, Toast.Type.ERROR, "Harap masukkan tanggal mulai dan selesai!", getOptionAlert());
            return;
        }

        LocalDate startDate = null;
        LocalDate endDate = null;
        try {
            dateFormat.setLenient(false); // Strict parsing
            java.util.Date parsedStartDate = dateFormat.parse(startDateStr);
            java.util.Date parsedEndDate = dateFormat.parse(endDateStr);
            startDate = parsedStartDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            endDate = parsedEndDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch (ParseException e) {
            Toast.show(this, Toast.Type.ERROR, "Format tanggal tidak valid! Gunakan yyyy-MM-dd.", getOptionAlert());
            return;
        }

        if (endDate.isBefore(startDate)) {
            Toast.show(this, Toast.Type.ERROR, "Tanggal selesai tidak boleh sebelum tanggal mulai!", getOptionAlert());
            return;
        }

        // Generate the report
        try {
            File reportFile = servis.generateAttendanceReport(startDate, endDate);
            if (reportFile != null && reportFile.exists()) {
                Toast.show(this, Toast.Type.SUCCESS, "Laporan absensi berhasil dibuat: " + reportFile.getAbsolutePath(), getOptionAlert());
                java.awt.Desktop.getDesktop().open(reportFile);
            } else {
                Toast.show(this, Toast.Type.ERROR, "Gagal membuat laporan absensi!", getOptionAlert());
            }
        } catch (Exception e) {
            Toast.show(this, Toast.Type.ERROR, "Error saat membuat laporan: " + e.getMessage(), getOptionAlert());
        }
    }

    private void resetForm() {
        txtRfid.setText("");
        txtRfid.requestFocus();
        // Do not reset date fields to allow multiple report generations
    }
}