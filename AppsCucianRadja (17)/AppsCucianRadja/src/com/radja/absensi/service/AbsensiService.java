package com.radja.absensi.service;

import com.radja.dao.AbsensiDAOImpl;
import com.radja.config.Koneksi;
import com.radja.model.Absensi;
import com.radja.model.Karyawan;
import com.radja.service.ServiceAbsensi;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.File;
import java.io.FileWriter;

public class AbsensiService implements ServiceAbsensi {

    private final AbsensiDAOImpl absensiDAOImpl;
    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(AbsensiService.class.getName());
    private String lastErrorMessage = "";

    public AbsensiService(AbsensiDAOImpl absensiDAO) {
        this.absensiDAOImpl = absensiDAO;
        this.conn = Koneksi.getConnection();
        if (conn == null) {
            lastErrorMessage = "Gagal mendapatkan koneksi database";
            LOGGER.severe("Gagal mendapatkan koneksi database");
        } else {
            LOGGER.info("Koneksi database berhasil");
        }
    }

    public boolean isDatabaseConnected() {
        return conn != null;
    }

    public String getLastErrorMessage() {
        String msg = lastErrorMessage;
        if (msg.isEmpty()) {
            msg = absensiDAOImpl.getLastErrorMessage();
        }
        return msg;
    }

    @Override
    public boolean checkIn(int idKaryawan) {
        LOGGER.info("Memproses check-in untuk karyawan ID: " + idKaryawan);
        if (isSudahAbsenMasuk(idKaryawan, LocalDate.now()) || isSudahAbsen(idKaryawan, "TIDAK HADIR")) {
            lastErrorMessage = "Karyawan sudah absen masuk atau tidak hadir";
            LOGGER.warning("Check-in gagal: Karyawan sudah absen masuk atau tidak hadir");
            return false;
        }

        Karyawan karyawan = absensiDAOImpl.getKaryawanById(idKaryawan);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan dengan ID " + idKaryawan + " tidak ditemukan";
            LOGGER.warning("Check-in gagal: Karyawan dengan ID " + idKaryawan + " tidak ditemukan");
            return false;
        }

        return catatAbsensi(idKaryawan, "HADIR", LocalDateTime.now(), "Absen Masuk");
    }

    @Override
    public boolean checkOut(int idKaryawan) {
        LOGGER.info("Memproses check-out untuk karyawan ID: " + idKaryawan);
        if (!isSudahAbsenMasuk(idKaryawan, LocalDate.now())) {
            lastErrorMessage = "Karyawan belum absen masuk";
            LOGGER.warning("Check-out gagal: Karyawan belum absen masuk");
            return false;
        }
        if (isSudahAbsenPulang(idKaryawan, LocalDate.now())) {
            lastErrorMessage = "Karyawan sudah absen pulang";
            LOGGER.warning("Check-out gagal: Karyawan sudah absen pulang");
            return false;
        }

        Karyawan karyawan = absensiDAOImpl.getKaryawanById(idKaryawan);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan dengan ID " + idKaryawan + " tidak ditemukan";
            LOGGER.warning("Check-out gagal: Karyawan dengan ID " + idKaryawan + " tidak ditemukan");
            return false;
        }

        String sql = "UPDATE absensi SET waktu_pulang = ?, keterangan = ? WHERE id_karyawan = ? AND tanggal = ? AND status = 'HADIR'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(2, "Absen Pulang");
            stmt.setInt(3, idKaryawan);
            stmt.setDate(4, java.sql.Date.valueOf(LocalDate.now()));
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                lastErrorMessage = "";
                LOGGER.info("Check-out berhasil untuk karyawan ID: " + idKaryawan);
                return true;
            } else {
                lastErrorMessage = "Tidak ada absensi masuk untuk diperbarui";
                LOGGER.warning("Check-out gagal: Tidak ada absensi masuk untuk diperbarui");
                return false;
            }
        } catch (SQLException e) {
            lastErrorMessage = "Error saat check-out: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat check-out untuk karyawan ID: " + idKaryawan, e);
            return false;
        }
    }

    @Override
    public boolean isSudahIzinSakit(int idKaryawan, LocalDate now) {
        boolean result = absensiDAOImpl.isSudahAbsen(idKaryawan, now, "IZIN") || absensiDAOImpl.isSudahAbsen(idKaryawan, now, "SAKIT");
        LOGGER.info("Hasil pemeriksaan izin/sakit untuk karyawan ID " + idKaryawan + ": " + result);
        return result;
    }

    private boolean isSudahAbsen(int idKaryawan, String status) {
        boolean result = absensiDAOImpl.isSudahAbsen(idKaryawan, LocalDate.now(), status);
        LOGGER.info("Hasil pemeriksaan absensi untuk karyawan ID " + idKaryawan + " dengan status " + status + ": " + result);
        return result;
    }

    @Override
    public boolean isSudahAbsenPulang(int idKaryawan, LocalDate now) {
        LOGGER.info("Memeriksa apakah karyawan ID " + idKaryawan + " sudah absen pulang pada " + now);
        
        if (now == null) {
            lastErrorMessage = "Parameter 'now' tidak boleh null";
            LOGGER.severe("Parameter 'now' tidak boleh null");
            return false;
        }
        
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.severe("Koneksi database tidak tersedia");
            return false;
        }
        
        String sql = "SELECT COUNT(*) FROM absensi WHERE id_karyawan = ? AND tanggal = ? AND waktu_pulang IS NOT NULL";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            stmt.setDate(2, java.sql.Date.valueOf(now));
            java.sql.ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean result = rs.getInt(1) > 0;
                lastErrorMessage = "";
                LOGGER.info("Hasil pemeriksaan absen pulang: " + result);
                return result;
            } else {
                lastErrorMessage = "Tidak ada hasil dari query untuk karyawan ID " + idKaryawan + " pada " + now;
                LOGGER.warning("Tidak ada hasil dari query untuk karyawan ID " + idKaryawan + " pada " + now);
                return false;
            }
        } catch (SQLException e) {
            lastErrorMessage = "Error saat memeriksa absen pulang: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat memeriksa absen pulang untuk karyawan ID: " + idKaryawan + ", SQL Error: " + e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean isSudahAbsenMasuk(int idKaryawan, LocalDate now) {
        boolean result = absensiDAOImpl.isSudahAbsen(idKaryawan, now, "HADIR");
        LOGGER.info("Hasil pemeriksaan absen masuk untuk karyawan ID " + idKaryawan + ": " + result);
        return result;
    }

    @Override
    public boolean catatIzinSakit(int idKaryawan, String jenis, String keterangan) {
        LOGGER.info("Mencatat izin/sakit untuk karyawan ID: " + idKaryawan + ", jenis: " + jenis);
        if (!jenis.equals("IZIN") && !jenis.equals("SAKIT")) {
            lastErrorMessage = "Jenis izin/sakit tidak valid: " + jenis;
            LOGGER.warning("Jenis izin/sakit tidak valid: " + jenis);
            return false;
        }
        if (isSudahAbsen(idKaryawan, "HADIR") || isSudahAbsen(idKaryawan, "TIDAK HADIR")) {
            lastErrorMessage = "Karyawan sudah absen hadir atau tidak hadir";
            LOGGER.warning("Izin/sakit gagal: Karyawan sudah absen hadir atau tidak hadir");
            return false;
        }

        Karyawan karyawan = absensiDAOImpl.getKaryawanById(idKaryawan);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan dengan ID " + idKaryawan + " tidak ditemukan";
            LOGGER.warning("Izin/sakit gagal: Karyawan dengan ID " + idKaryawan + " tidak ditemukan");
            return false;
        }

        boolean result = absensiDAOImpl.catatIzinSakit(karyawan, jenis, keterangan, LocalDate.now());
        LOGGER.info("Hasil pencatatan izin/sakit: " + result);
        return result;
    }

    public boolean catatIzinAtauSakit(int idKaryawan, String keterangan) {
        String jenis = keterangan.toUpperCase().contains("SAKIT") ? "SAKIT" : "IZIN";
        return catatIzinSakit(idKaryawan, jenis, keterangan);
    }

    @Override
    public List<Absensi> getData() {
        LOGGER.info("Mengambil semua data absensi");
        return absensiDAOImpl.getAllAbsensi();
    }

    @Override
    public List<Absensi> searchData(String keyword) {
        LOGGER.info("Mencari data absensi dengan keyword: " + keyword);
        return absensiDAOImpl.searchAbsensi(keyword);
    }

    @Override
    public List<Absensi> getRiwayatKaryawan(int idKaryawan, LocalDate start, LocalDate end) {
        LOGGER.info("Mengambil riwayat absensi untuk karyawan ID: " + idKaryawan + " dari " + start + " sampai " + end);
        return absensiDAOImpl.getRiwayatAbsensi(idKaryawan, start, end);
    }

    @Override
    public boolean catatAbsensi(int userId, String status, LocalDateTime waktuAbsensi, String keterangan) {
        LOGGER.info("Mencatat absensi untuk karyawan ID: " + userId + ", status: " + status + ", waktu: " + waktuAbsensi);
        String sql = "INSERT INTO absensi (id_karyawan, status, waktu_masuk, keterangan, tanggal) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, status);
            stmt.setTimestamp(3, Timestamp.valueOf(waktuAbsensi));
            stmt.setString(4, keterangan);
            stmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
            int rowsAffected = stmt.executeUpdate();
            lastErrorMessage = "";
            LOGGER.info("Absensi berhasil dicatat, baris terpengaruh: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            lastErrorMessage = "Error saat mencatat absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mencatat absensi untuk karyawan ID: " + userId, e);
            return false;
        }
    }

    public Karyawan getKaryawanByRfid(String rfid) {
        LOGGER.info("Mengambil karyawan dengan RFID: " + rfid);
        return absensiDAOImpl.getKaryawanByRfid(rfid);
    }

    @Override
    public List<Absensi> getAbsensiByRfid(String rfid) {
        LOGGER.info("Mengambil absensi untuk RFID: " + rfid);
        return absensiDAOImpl.getAbsensiByRfid(rfid);
    }

    @Override
    public List<Absensi> searchDataByRfid(String keyword, String rfid) {
        LOGGER.info("Mencari absensi untuk RFID: " + rfid + " dengan keyword: " + keyword);
        return absensiDAOImpl.searchDataByRfid(keyword, rfid);
    }

    @Override
    public List<Absensi> getAllAbsensi() {
        LOGGER.info("Mengambil semua absensi");
        return absensiDAOImpl.getAllAbsensi();
    }

    @Override
    public List<Absensi> searchAbsensi(String keyword) {
        LOGGER.info("Mencari absensi dengan keyword: " + keyword);
        return absensiDAOImpl.searchAbsensi(keyword);
    }

    @Override
    public List<Absensi> getAbsensiByDate(Date startDate, Date endDate) {
        LOGGER.info("Mengambil absensi untuk rentang tanggal: " + startDate + " sampai " + endDate);
        return absensiDAOImpl.getAbsensiByDate(startDate, endDate);
    }

    @Override
    public List<Absensi> getAbsensiByRfidAndDate(String rfid, Date startDate, Date endDate) {
        LOGGER.info("Mengambil absensi untuk RFID: " + rfid + " dari " + startDate + " sampai " + endDate);
        return absensiDAOImpl.getAbsensiByRfidAndDate(rfid, startDate, endDate);
    }

    @Override
    public List<Absensi> getAbsensiByDateRange(Date startDate, Date endDate) {
        LOGGER.info("Mengambil absensi untuk rentang tanggal: " + startDate + " sampai " + endDate);
        return absensiDAOImpl.getAbsensiByDateRange(startDate, endDate);
    }

    @Override
    public List<Absensi> searchAbsensi(String keyword, Date startDate, Date endDate) {
        LOGGER.info("Mencari absensi dengan keyword: " + keyword + " dari " + startDate + " sampai " + endDate);
        return absensiDAOImpl.searchAbsensi(keyword, startDate, endDate);
    }

    @Override
    public List<Absensi> getRiwayatAbsensi(int idKaryawan, long time, long time0) {
        LOGGER.info("Mengambil riwayat absensi untuk karyawan ID: " + idKaryawan + " dari " + time + " sampai " + time0);
        return absensiDAOImpl.getRiwayatAbsensi(idKaryawan, new java.sql.Date(time).toLocalDate(), new java.sql.Date(time0).toLocalDate());
    }

    @Override
    public List<Absensi> searchAbsensiForKasir(String keyword, int idKaryawan, long l, long l0) {
        LOGGER.info("Mencari absensi untuk kasir ID: " + idKaryawan + " dengan keyword: " + keyword);
        return absensiDAOImpl.searchAbsensiByKaryawan(idKaryawan, keyword, new java.sql.Date(l).toLocalDate(), new java.sql.Date(l0).toLocalDate());
    }

    @Override
    public List<Absensi> getAbsensiByKasir(int idKaryawan) {
        LOGGER.info("Mengambil absensi untuk kasir ID: " + idKaryawan);
        return absensiDAOImpl.getAbsensiByKasir(idKaryawan);
    }

    @Override
    public List<Absensi> searchAbsensiByKaryawan(int idKaryawan, String keyword, LocalDate minusMonths, LocalDate now) {
        LOGGER.info("Mencari absensi untuk karyawan ID: " + idKaryawan + " dengan keyword: " + keyword);
        return absensiDAOImpl.searchAbsensiByKaryawan(idKaryawan, keyword, minusMonths, now);
    }

    @Override
    public boolean isSudahAbsen(int idKaryawan, LocalDate now, String hadir) {
        boolean result = absensiDAOImpl.isSudahAbsen(idKaryawan, now, hadir);
        LOGGER.info("Hasil pemeriksaan absensi untuk karyawan ID " + idKaryawan + " dengan status " + hadir + ": " + result);
        return result;
    }

    public File generateAttendanceReport(LocalDate startDate, LocalDate endDate) {
        LOGGER.info("Generating attendance report from " + startDate + " to " + endDate);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.severe(lastErrorMessage);
            return null;
        }

        List<Absensi> absensiList = absensiDAOImpl.getAbsensiByDateRange(
                java.sql.Date.valueOf(startDate),
                java.sql.Date.valueOf(endDate)
        );

        if (absensiList.isEmpty()) {
            lastErrorMessage = "Tidak ada data absensi untuk rentang tanggal tersebut";
            LOGGER.warning(lastErrorMessage);
            return null;
        }

        StringBuilder latexContent = new StringBuilder();
        latexContent.append("\\documentclass[a4paper,12pt]{article}\n");
        latexContent.append("\\usepackage[utf8]{inputenc}\n");
        latexContent.append("\\usepackage[T1]{fontenc}\n");
        latexContent.append("\\usepackage{lmodern}\n");
        latexContent.append("\\usepackage{geometry}\n");
        latexContent.append("\\geometry{margin=1in}\n");
        latexContent.append("\\usepackage{longtable}\n");
        latexContent.append("\\usepackage{datetime}\n");
        latexContent.append("\\title{Laporan Absensi Karyawan}\n");
        latexContent.append("\\author{Cuci Mobil Radja}\n");
        latexContent.append("\\date{\\today}\n");
        latexContent.append("\\begin{document}\n");
        latexContent.append("\\maketitle\n");
        latexContent.append("\\section*{Laporan Absensi dari " + startDate + " hingga " + endDate + "}\n");
        latexContent.append("\\begin{longtable}{|c|c|c|c|c|c|c|}\n");
        latexContent.append("\\hline\n");
        latexContent.append("\\textbf{Nama Karyawan} & \\textbf{Tanggal} & \\textbf{Waktu Masuk} & \\textbf{Waktu Pulang} & \\textbf{Durasi} & \\textbf{Status} & \\textbf{Keterangan} \\\\ \\hline\n");
        latexContent.append("\\endhead\n");

        for (Absensi absensi : absensiList) {
            Karyawan karyawan = absensi.getKaryawan();
            String namaKaryawan = karyawan != null ? karyawan.getNamaKaryawan().replace("&", "\\&") : "Tidak Diketahui";
            String waktuMasuk = absensi.getWaktuMasuk() != null ? absensi.getWaktuMasuk().toString() : "-";
            String waktuPulang = absensi.getWaktuPulang() != null ? absensi.getWaktuPulang().toString() : "-";
            String status = absensi.getStatus() != null ? absensi.getStatus() : "-";
            String keterangan = absensi.getKeterangan() != null ? absensi.getKeterangan().replace("&", "\\&") : "-";
            String durasi = "-";

            // Calculate duration for HADIR status with both check-in and check-out times
            if ("HADIR".equals(status) && absensi.getWaktuMasuk() != null && absensi.getWaktuPulang() != null) {
                Duration duration = Duration.between(absensi.getWaktuMasuk(), absensi.getWaktuPulang());
                long hours = duration.toHours();
                long minutes = duration.toMinutesPart();
                durasi = String.format("%02d:%02d", hours, minutes);
            }

            latexContent.append(String.format(
                    "%s & %s & %s & %s & %s & %s & %s \\\\ \\hline\n",
                    namaKaryawan,
                    absensi.getTanggal(),
                    waktuMasuk,
                    waktuPulang,
                    durasi,
                    status,
                    keterangan
            ));
        }

        latexContent.append("\\end{longtable}\n");
        latexContent.append("\\end{document}\n");

        try {
            File tempDir = new File(System.getProperty("java.io.tmpdir"));
            File texFile = new File(tempDir, "attendance_report_" + System.currentTimeMillis() + ".tex");
            try (FileWriter writer = new FileWriter(texFile)) {
                writer.write(latexContent.toString());
            }

            ProcessBuilder pb = new ProcessBuilder("latexmk", "-pdf", texFile.getAbsolutePath());
            pb.directory(tempDir);
            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                File pdfFile = new File(tempDir, texFile.getName().replace(".tex", ".pdf"));
                if (pdfFile.exists()) {
                    lastErrorMessage = "";
                    LOGGER.info("Attendance report generated successfully at: " + pdfFile.getAbsolutePath());
                    return pdfFile;
                } else {
                    lastErrorMessage = "Gagal menghasilkan file PDF";
                    LOGGER.severe(lastErrorMessage);
                    return null;
                }
            } else {
                lastErrorMessage = "Gagal mengkompilasi LaTeX ke PDF";
                LOGGER.severe(lastErrorMessage);
                return null;
            }
        } catch (Exception e) {
            lastErrorMessage = "Error saat menghasilkan laporan: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error generating attendance report", e);
            return null;
        }
    }
    
    public List<Absensi> getAbsensiForUser(String role, String rfid, String keyword, Date startDate, Date endDate) {
        LOGGER.info("Memproses getAbsensiForUser untuk role: " + role + ", RFID: " + rfid);
        return absensiDAOImpl.getAbsensiForUser(role, rfid, keyword, startDate, endDate);
    }

    @Override
    public boolean checkOut(int idKaryawan, LocalDateTime waktuAbsensi, String keterangan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean checkIn(int idKaryawan, LocalDateTime waktuAbsensi, String keterangan) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}