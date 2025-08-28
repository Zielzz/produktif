package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Absensi;
import com.radja.model.Karyawan;
import com.radja.model.User;
import com.radja.main.FormManager;
import com.radja.service.ServiceAbsensi;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AbsensiDAOImpl implements ServiceAbsensi {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(AbsensiDAOImpl.class.getName());
    private String lastErrorMessage = "";

    public AbsensiDAOImpl() {
        LOGGER.info("Menginisialisasi AbsensiDAOImpl");
        this.conn = Koneksi.getConnection();
        if (this.conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.severe("Gagal mendapatkan koneksi database");
        } else {
            LOGGER.info("Koneksi database berhasil");
        }
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public Karyawan getKaryawanById(int idKaryawan) {
        LOGGER.info("Mengambil karyawan dengan ID: " + idKaryawan);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return null;
        }

        String sql = "SELECT * FROM karyawan WHERE id_karyawan = ? AND is_delete = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Karyawan karyawan = new Karyawan();
                karyawan.setIdKaryawan(rs.getInt("id_karyawan"));
                karyawan.setNamaKaryawan(rs.getString("nama_karyawan"));
                karyawan.setTelepon(rs.getString("telepon"));
                karyawan.setAlamat(rs.getString("alamat"));
                karyawan.setRfid(rs.getString("rfid"));
                lastErrorMessage = "";
                LOGGER.info("Berhasil mengambil data karyawan dengan ID: " + idKaryawan);
                return karyawan;
            } else {
                lastErrorMessage = "Karyawan dengan ID " + idKaryawan + " tidak ditemukan";
                LOGGER.warning(lastErrorMessage);
            }
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil data karyawan: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil karyawan dengan ID: " + idKaryawan, e);
        }
        return null;
    }

    public Karyawan getKaryawanByRfid(String rfid) {
        LOGGER.info("Mengambil karyawan dengan RFID: " + rfid);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return null;
        }

        if (rfid == null || rfid.trim().isEmpty()) {
            lastErrorMessage = "RFID tidak boleh kosong";
            LOGGER.warning(lastErrorMessage);
            return null;
        }

        String sql = "SELECT * FROM karyawan WHERE rfid = ? AND is_delete = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rfid);
            LOGGER.fine("Menjalankan query: " + sql + " dengan rfid: " + rfid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Karyawan karyawan = new Karyawan();
                karyawan.setIdKaryawan(rs.getInt("id_karyawan"));
                karyawan.setNamaKaryawan(rs.getString("nama_karyawan"));
                karyawan.setTelepon(rs.getString("telepon"));
                karyawan.setAlamat(rs.getString("alamat"));
                karyawan.setRfid(rs.getString("rfid"));
                lastErrorMessage = "";
                LOGGER.info("Berhasil mengambil data karyawan dengan RFID: " + rfid);
                return karyawan;
            } else {
                lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
                LOGGER.warning(lastErrorMessage);
            }
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil data karyawan: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil karyawan dengan RFID: " + rfid, e);
        }
        return null;
    }
    
    // In AbsensiDAOImpl.java, add a method to fetch the latest Absensi record for an employee on a specific date
public Absensi getLatestAbsensiForKaryawan(int idKaryawan, LocalDate tanggal) {
    LOGGER.info("Mengambil absensi terbaru untuk karyawan " + idKaryawan + " pada tanggal " + tanggal);
    if (conn == null) {
        lastErrorMessage = "Koneksi database tidak tersedia";
        LOGGER.warning(lastErrorMessage);
        return null;
    }

    String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                 "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                 "WHERE a.id_karyawan = ? AND a.tanggal = ? AND a.is_delete = FALSE " +
                 "ORDER BY a.id_absensi DESC LIMIT 1";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idKaryawan);
        stmt.setDate(2, java.sql.Date.valueOf(tanggal));
        LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan + ", tanggal: " + tanggal);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Absensi absensi = parseAbsensi(rs);
            absensi.setDurasi(rs.getDouble("durasi")); // Ensure the duration is set
            lastErrorMessage = "";
            LOGGER.info("Berhasil mengambil absensi terbaru untuk karyawan " + idKaryawan);
            return absensi;
        } else {
            lastErrorMessage = "Absensi untuk karyawan " + idKaryawan + " pada tanggal " + tanggal + " tidak ditemukan";
            LOGGER.warning(lastErrorMessage);
            return null;
        }
    } catch (SQLException e) {
        lastErrorMessage = "Gagal mengambil absensi terbaru: " + e.getMessage();
        LOGGER.log(Level.SEVERE, "Error saat mengambil absensi terbaru untuk karyawan " + idKaryawan, e);
        return null;
    }
}

    public boolean isSudahAbsen(int idKaryawan, LocalDate tanggal, String status) {
        LOGGER.info("Memeriksa absensi untuk karyawan " + idKaryawan + ", tanggal " + tanggal + ", status " + status);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        String sql = "SELECT * FROM absensi WHERE id_karyawan = ? AND tanggal = ? AND UPPER(status) = UPPER(?) AND is_delete = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            stmt.setDate(2, java.sql.Date.valueOf(tanggal));
            stmt.setString(3, status);
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan + ", tanggal: " + tanggal + ", status: " + status);
            ResultSet rs = stmt.executeQuery();
            boolean sudahAbsen = rs.next();
            lastErrorMessage = "";
            LOGGER.info("Hasil pemeriksaan absensi: " + sudahAbsen);
            return sudahAbsen;
        } catch (SQLException e) {
            lastErrorMessage = "Gagal memeriksa status absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat memeriksa absensi untuk karyawan " + idKaryawan, e);
            return false;
        }
    }

   // In AbsensiDAOImpl.java, update checkIn method
public boolean checkIn(Karyawan karyawan, LocalDateTime waktuMasuk) {
    LOGGER.info("Mencatat check-in untuk karyawan " + (karyawan != null ? karyawan.getIdKaryawan() : "null") + " pada " + waktuMasuk);
    if (conn == null) {
        lastErrorMessage = "Koneksi database tidak tersedia";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    if (karyawan == null || waktuMasuk == null) {
        lastErrorMessage = "Karyawan atau waktu masuk tidak valid";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    // Validasi waktu check-in (07:00 - 22:59)
    LocalTime waktuSekarang = waktuMasuk.toLocalTime();
    if (waktuSekarang.isBefore(LocalTime.of(7, 0)) || waktuSekarang.isAfter(LocalTime.of(22, 59))) {
        lastErrorMessage = "Check-in hanya diizinkan antara pukul 07:00 dan 22:59";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    // Check for existing incomplete check-in
    String sqlCheck = "SELECT id_absensi FROM absensi WHERE id_karyawan = ? AND tanggal = ? AND status = 'HADIR' AND waktu_pulang IS NULL AND is_delete = FALSE";
    try (PreparedStatement stmt = conn.prepareStatement(sqlCheck)) {
        stmt.setInt(1, karyawan.getIdKaryawan());
        stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            lastErrorMessage = "Karyawan sudah check-in tetapi belum check-out hari ini";
            LOGGER.warning(lastErrorMessage);
            return false;
        }
    } catch (SQLException e) {
        lastErrorMessage = "Gagal memeriksa status check-in: " + e.getMessage();
        LOGGER.log(Level.SEVERE, "Error saat memeriksa check-in untuk karyawan " + karyawan.getIdKaryawan(), e);
        return false;
    }

    if (isSudahAbsen(karyawan.getIdKaryawan(), LocalDate.now(), "HADIR")) {
        lastErrorMessage = "Karyawan sudah absen masuk hari ini";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    if (isSudahAbsen(karyawan.getIdKaryawan(), LocalDate.now(), "TIDAK HADIR")) {
        lastErrorMessage = "Karyawan sudah mengajukan izin/sakit hari ini";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    // Cek keterlambatan dan tambahkan keterangan
    String keterangan = null;
    if (waktuSekarang.isAfter(LocalTime.of(7, 0))) {
        keterangan = "Terlambat Masuk";
        LOGGER.info("Karyawan " + karyawan.getIdKaryawan() + " terlambat masuk pada " + waktuMasuk);
    }

    String sql = "INSERT INTO absensi (id_karyawan, tanggal, waktu_masuk, status, keterangan) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, karyawan.getIdKaryawan());
        stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
        stmt.setTimestamp(3, Timestamp.valueOf(waktuMasuk));
        stmt.setString(4, "HADIR");
        stmt.setString(5, keterangan);
        LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + karyawan.getIdKaryawan() + ", tanggal: " + LocalDate.now() + ", waktu_masuk: " + waktuMasuk + ", keterangan: " + (keterangan != null ? keterangan : "null"));
        int rowsAffected = stmt.executeUpdate();
        lastErrorMessage = "";
        LOGGER.info("Check-in berhasil, baris terpengaruh: " + rowsAffected);
        return rowsAffected > 0;
    } catch (SQLException e) {
        lastErrorMessage = "Gagal mencatat check-in: " + e.getMessage();
        LOGGER.log(Level.SEVERE, "Error saat check-in untuk karyawan " + karyawan.getIdKaryawan(), e);
        return false;
    }
}

    // In AbsensiDAOImpl.java, update checkOut method
public boolean checkOut(Karyawan karyawan, LocalDateTime waktuPulang) {
    LOGGER.info("Mencatat check-out untuk karyawan " + (karyawan != null ? karyawan.getIdKaryawan() : "null") + " pada " + waktuPulang);
    if (conn == null) {
        lastErrorMessage = "Koneksi database tidak tersedia";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    if (karyawan == null || waktuPulang == null) {
        lastErrorMessage = "Karyawan atau waktu pulang tidak valid";
        LOGGER.warning(lastErrorMessage);
        return false;
    }

    String sqlSelect = "SELECT waktu_masuk, keterangan FROM absensi WHERE id_karyawan = ? AND tanggal = ? AND status = 'HADIR' AND waktu_pulang IS NULL";
    String existingKeterangan = null;
    LocalDateTime waktuMasuk = null;
    try (PreparedStatement stmt = conn.prepareStatement(sqlSelect)) {
        stmt.setInt(1, karyawan.getIdKaryawan());
        stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now()));
        LOGGER.fine("Menjalankan query: " + sqlSelect + " dengan id_karyawan: " + karyawan.getIdKaryawan() + ", tanggal: " + LocalDate.now());
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            Timestamp waktuMasukTs = rs.getTimestamp("waktu_masuk");
            existingKeterangan = rs.getString("keterangan");
            if (waktuMasukTs != null) {
                waktuMasuk = waktuMasukTs.toLocalDateTime();
                if (waktuPulang.isBefore(waktuMasuk) || waktuPulang.equals(waktuMasuk)) {
                    lastErrorMessage = "Waktu pulang harus lebih lambat dari waktu masuk";
                    LOGGER.warning(lastErrorMessage);
                    return false;
                }
                // Enforce minimum 1-minute duration
                Duration minDuration = Duration.between(waktuMasuk, waktuPulang);
                if (minDuration.toMinutes() < 1) {
                    lastErrorMessage = "Durasi kerja minimal 1 menit";
                    LOGGER.warning(lastErrorMessage);
                    return false;
                }
            } else {
                lastErrorMessage = "Waktu masuk tidak ditemukan untuk absensi ini";
                LOGGER.warning(lastErrorMessage);
                return false;
            }
        } else {
            lastErrorMessage = "Tidak ada check-in aktif untuk karyawan ini";
            LOGGER.warning(lastErrorMessage);
            return false;
        }
    } catch (SQLException e) {
        lastErrorMessage = "Gagal memeriksa waktu masuk: " + e.getMessage();
        LOGGER.log(Level.SEVERE, "Error saat memeriksa waktu masuk", e);
        return false;
    }

    // Keterangan logic remains the same
    String keterangan = existingKeterangan;
    LocalTime waktuSekarang = waktuPulang.toLocalTime();
    if (waktuSekarang.isAfter(LocalTime.of(23, 0))) {
        keterangan = existingKeterangan != null && existingKeterangan.contains("Terlambat Masuk") ? "Terlambat Masuk, Pulang Awal" : "Pulang Awal";
        LOGGER.info("Karyawan " + karyawan.getIdKaryawan() + " pulang awal pada " + waktuPulang);
    } else {
        keterangan = existingKeterangan != null && existingKeterangan.contains("Terlambat Masuk") ? "Terlambat Masuk" : null;
    }

    // Calculate duration in hours
    double durasiJam = Duration.between(waktuMasuk, waktuPulang).toMinutes() / 60.0;

    String sql = "UPDATE absensi SET waktu_pulang = ?, keterangan = ?, durasi = ? WHERE id_karyawan = ? AND tanggal = ? AND status = 'HADIR' AND waktu_pulang IS NULL";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setTimestamp(1, Timestamp.valueOf(waktuPulang));
        stmt.setString(2, keterangan);
        stmt.setDouble(3, durasiJam);
        stmt.setInt(4, karyawan.getIdKaryawan());
        stmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
        LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + karyawan.getIdKaryawan() + ", tanggal: " + LocalDate.now() + ", waktu_pulang: " + waktuPulang + ", keterangan: " + (keterangan != null ? keterangan : "null") + ", durasi: " + durasiJam);
        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected > 0) {
            lastErrorMessage = "";
            LOGGER.info("Check-out berhasil, baris terpengaruh: " + rowsAffected);
            return true;
        } else {
            lastErrorMessage = "Tidak ada absensi masuk untuk diperbarui atau sudah check-out";
            LOGGER.warning(lastErrorMessage);
            return false;
        }
    } catch (SQLException e) {
        lastErrorMessage = "Gagal mencatat check-out: " + e.getMessage();
        LOGGER.log(Level.SEVERE, "Error saat check-out untuk karyawan " + karyawan.getIdKaryawan(), e);
        return false;
    }
}

    public boolean catatIzinSakit(Karyawan karyawan, String jenis, String keterangan, LocalDate tanggal) {
        LOGGER.info("Mencatat izin/sakit untuk karyawan " + (karyawan != null ? karyawan.getIdKaryawan() : "null") + " pada " + tanggal);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        if (karyawan == null || keterangan == null || keterangan.trim().isEmpty()) {
            lastErrorMessage = "Karyawan atau keterangan tidak valid";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        if (isSudahAbsen(karyawan.getIdKaryawan(), tanggal, "TIDAK HADIR")) {
            lastErrorMessage = "Karyawan sudah mengajukan izin/sakit pada tanggal ini";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        if (isSudahAbsen(karyawan.getIdKaryawan(), tanggal, "HADIR")) {
            lastErrorMessage = "Karyawan sudah absen hadir pada tanggal ini";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        String sql = "INSERT INTO absensi (id_karyawan, tanggal, status, keterangan) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, karyawan.getIdKaryawan());
            stmt.setDate(2, java.sql.Date.valueOf(tanggal));
            stmt.setString(3, "TIDAK HADIR");
            stmt.setString(4, keterangan);
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + karyawan.getIdKaryawan() + ", tanggal: " + tanggal + ", status: TIDAK HADIR, keterangan: " + keterangan);
            int rowsAffected = stmt.executeUpdate();
            lastErrorMessage = "";
            LOGGER.info("Izin/Sakit berhasil dicatat, baris terpengaruh: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mencatat izin/sakit: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mencatat izin/sakit untuk karyawan " + karyawan.getIdKaryawan(), e);
            return false;
        }
    }

    public boolean catatIzinSakit(int idKaryawan, String keterangan) {
        Karyawan karyawan = getKaryawanById(idKaryawan);
        return catatIzinSakit(karyawan, "IZIN", keterangan, LocalDate.now());
    }

    @Override
    public List<Absensi> getAllAbsensi() {
        LOGGER.info("Mengambil semua absensi");
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        User loggedInUser = FormManager.getLoggedInUser();
        if (loggedInUser == null) {
            lastErrorMessage = "Tidak ada pengguna yang login";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        String role = loggedInUser.getRole() != null ? loggedInUser.getRole().toLowerCase() : "";
        String username = loggedInUser.getUsername() != null ? loggedInUser.getUsername() : "unknown";
        LOGGER.info("Role pengguna: " + role + ", Username: " + username);

        // For 'Kasir' role, filter by RFID
        if ("kasir".equals(role)) {
            String rfid = loggedInUser.getRfid();
            if (rfid == null || rfid.trim().isEmpty()) {
                lastErrorMessage = "RFID tidak valid untuk pengguna: " + username;
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }
            return getAbsensiByRfid(rfid);  // Show absences only for the logged-in Kasir
        }

        // For 'Owner' role, return all attendance records
        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE a.is_delete = FALSE ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            LOGGER.fine("Menjalankan query: " + sql);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk role: " + role);
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil semua absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil semua absensi", e);
        }
        return list;
    }

    public List<Absensi> searchAbsensi(String keyword) {
        LOGGER.info("Mencari absensi dengan keyword: " + keyword);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        User loggedInUser = FormManager.getLoggedInUser();
        if (loggedInUser == null) {
            lastErrorMessage = "Tidak ada pengguna yang login";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        String role = loggedInUser.getRole() != null ? loggedInUser.getRole().toLowerCase() : "";
        String username = loggedInUser.getUsername() != null ? loggedInUser.getUsername() : "unknown";
        LOGGER.info("Role pengguna: " + role + ", Username: " + username);

        if ("karyawan".equals(role)) {
            String rfid = loggedInUser.getRfid();
            if (rfid == null || rfid.trim().isEmpty()) {
                lastErrorMessage = "RFID tidak valid untuk pengguna: " + username;
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }
            Karyawan karyawan = getKaryawanByRfid(rfid);
            if (karyawan == null) {
                lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }
            LOGGER.info("Karyawan mencari absensi untuk RFID: " + rfid);
            return searchDataByRfid(keyword, rfid);
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE (k.nama_karyawan LIKE ? OR UPPER(a.status) LIKE UPPER(?) OR a.keterangan LIKE ?) AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);
            stmt.setString(3, kw);
            LOGGER.fine("Menjalankan query: " + sql + " dengan keyword: " + kw);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk pencarian, role: " + role);
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mencari absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mencari absensi dengan keyword: " + keyword, e);
        }
        return list;
    }

    public List<Absensi> getRiwayatAbsensi(int idKaryawan, LocalDate mulai, LocalDate sampai) {
        LOGGER.info("Mengambil riwayat absensi untuk karyawan " + idKaryawan + " dari " + mulai + " sampai " + sampai);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        if (idKaryawan <= 0) {
            lastErrorMessage = "ID Karyawan tidak valid";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> riwayat = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE a.id_karyawan = ? AND a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            stmt.setDate(2, java.sql.Date.valueOf(mulai));
            stmt.setDate(3, java.sql.Date.valueOf(sampai));
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan + ", mulai: " + mulai + ", sampai: " + sampai);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                riwayat.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri riwayat absensi");
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil riwayat absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil riwayat absensi untuk karyawan " + idKaryawan, e);
        }
        return riwayat;
    }

    public List<Absensi> searchAbsensiByKaryawan(int idKaryawan, String keyword, LocalDate mulai, LocalDate sampai) {
        LOGGER.info("Mencari absensi untuk karyawan " + idKaryawan + " dengan keyword: " + keyword);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        if (idKaryawan <= 0) {
            lastErrorMessage = "ID Karyawan tidak valid";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE a.id_karyawan = ? AND (k.nama_karyawan LIKE ? OR UPPER(a.status) LIKE UPPER(?) OR a.keterangan LIKE ?) " +
                     "AND a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
            stmt.setString(2, kw);
            stmt.setString(3, kw);
            stmt.setString(4, kw);
            stmt.setDate(5, java.sql.Date.valueOf(mulai));
            stmt.setDate(6, java.sql.Date.valueOf(sampai));
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan + ", keyword: " + kw + ", mulai: " + mulai + ", sampai: " + sampai);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk pencarian karyawan");
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mencari absensi untuk karyawan " + idKaryawan + ": " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mencari absensi untuk karyawan " + idKaryawan, e);
        }
        return list;
    }

    public List<Absensi> getAbsensiByRfid(String rfid) {
        LOGGER.info("Mengambil absensi untuk RFID: " + rfid);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        if (rfid == null || rfid.trim().isEmpty()) {
            lastErrorMessage = "RFID tidak boleh kosong";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        Karyawan karyawan = getKaryawanByRfid(rfid);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE k.rfid = ? AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rfid);
            LOGGER.fine("Menjalankan query: " + sql + " dengan rfid: " + rfid);
            ResultSet rs = ps.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk RFID: " + rfid);
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil absensi untuk RFID " + rfid + ": " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil absensi untuk RFID: " + rfid, e);
        }
        return list;
    }
    
    @Override
    public List<Absensi> getAbsensiForUser(String role, String rfid, String keyword, Date startDate, Date endDate) {
        LOGGER.info("Mengambil absensi untuk role: " + role + ", RFID: " + rfid + ", keyword: " + keyword + ", dari " + startDate + " sampai " + endDate);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> list = new ArrayList<>();
        String sql;
        PreparedStatement stmt;

        try {
            if ("kasir".equalsIgnoreCase(role)) {
                if (rfid == null || rfid.trim().isEmpty()) {
                    lastErrorMessage = "RFID tidak valid untuk kasir";
                    LOGGER.warning(lastErrorMessage);
                    return new ArrayList<>();
                }
                Karyawan karyawan = getKaryawanByRfid(rfid);
                if (karyawan == null) {
                    lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
                    LOGGER.warning(lastErrorMessage);
                    return new ArrayList<>();
                }
                sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                      "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                      "WHERE k.rfid = ? AND a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                      "AND (k.nama_karyawan LIKE ? OR UPPER(a.status) LIKE UPPER(?) OR a.keterangan LIKE ?) " +
                      "ORDER BY a.tanggal DESC";
                stmt = conn.prepareStatement(sql);
                String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
                stmt.setString(1, rfid);
                stmt.setDate(2, new java.sql.Date(startDate.getTime()));
                stmt.setDate(3, new java.sql.Date(endDate.getTime()));
                stmt.setString(4, kw);
                stmt.setString(5, kw);
                stmt.setString(6, kw);
            } else if ("owner".equalsIgnoreCase(role)) {
                sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                      "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                      "WHERE a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                      (keyword != null && !keyword.trim().isEmpty() ?
                      "AND (k.nama_karyawan LIKE ? OR UPPER(a.status) LIKE UPPER(?) OR a.keterangan LIKE ?) " : "") +
                      "ORDER BY a.tanggal DESC";
                stmt = conn.prepareStatement(sql);
                String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
                stmt.setDate(1, new java.sql.Date(startDate.getTime()));
                stmt.setDate(2, new java.sql.Date(endDate.getTime()));
                if (keyword != null && !keyword.trim().isEmpty()) {
                    stmt.setString(3, kw);
                    stmt.setString(4, kw);
                    stmt.setString(5, kw);
                    LOGGER.fine("Menggunakan filter keyword: " + kw);
                } else {
                    LOGGER.fine("Tanpa filter keyword");
                }
            } else {
                lastErrorMessage = "Role tidak dikenali: " + role;
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }

            LOGGER.fine("Menjalankan query: " + sql);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk role: " + role);
            stmt.close();
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil absensi untuk role: " + role, e);
            return new ArrayList<>();
        }
        return list;
    }
    
    public List<Absensi> searchDataByRfid(String keyword, String rfid) {
        LOGGER.info("Mencari absensi untuk RFID: " + rfid + " dengan keyword: " + keyword);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        if (rfid == null || rfid.trim().isEmpty()) {
            lastErrorMessage = "RFID tidak boleh kosong";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        Karyawan karyawan = getKaryawanByRfid(rfid);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE k.rfid = ? AND a.is_delete = FALSE " +
                     "AND (UPPER(a.status) LIKE UPPER(?) OR a.keterangan LIKE ? OR a.tanggal LIKE ?) " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rfid);
            String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
            ps.setString(2, kw);
            ps.setString(3, kw);
            ps.setString(4, kw);
            LOGGER.fine("Menjalankan query: " + sql + " dengan rfid: " + rfid + ", keyword: " + kw);
            ResultSet rs = ps.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk pencarian RFID: " + rfid);
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mencari absensi untuk RFID " + rfid + ": " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mencari absensi untuk RFID: " + rfid, e);
        }
        return list;
    }
    
    

    // In AbsensiDAOImpl.java, update parseAbsensi method
private Absensi parseAbsensi(ResultSet rs) throws SQLException {
    Absensi absensi = new Absensi();
    absensi.setIdAbsensi(rs.getInt("id_absensi"));
    absensi.setKaryawan(getKaryawanById(rs.getInt("id_karyawan")));
    absensi.setTanggal(rs.getDate("tanggal").toLocalDate());
    Timestamp jamMasuk = rs.getTimestamp("waktu_masuk");
    absensi.setWaktuMasuk(jamMasuk != null ? jamMasuk.toLocalDateTime() : null);
    Timestamp jamPulang = rs.getTimestamp("waktu_pulang");
    absensi.setWaktuPulang(jamPulang != null ? jamPulang.toLocalDateTime() : null);
    String status = rs.getString("status");
    // Normalize legacy status values
    if ("Masuk".equalsIgnoreCase(status) || "Pulang".equalsIgnoreCase(status)) {
        status = "HADIR";
        LOGGER.warning("Legacy status found in absensi ID: " + absensi.getIdAbsensi() + ", normalized to HADIR");
    }
    absensi.setStatus(status);
    absensi.setKeterangan(rs.getString("keterangan"));
    absensi.setDurasi(rs.getDouble("durasi")); // Add this line to set duration
    LOGGER.fine("Berhasil mem-parsing absensi dengan ID: " + absensi.getIdAbsensi());
    return absensi;
}

    @Override
    public boolean checkIn(int idKaryawan) {
        Karyawan karyawan = getKaryawanById(idKaryawan);
        return checkIn(karyawan, LocalDateTime.now());
    }

    @Override
    public boolean checkOut(int idKaryawan) {
        Karyawan karyawan = getKaryawanById(idKaryawan);
        return checkOut(karyawan, LocalDateTime.now());
    }

    @Override
    public boolean catatIzinSakit(int idKaryawan, String jenis, String keterangan) {
        Karyawan karyawan = getKaryawanById(idKaryawan);
        return catatIzinSakit(karyawan, jenis, keterangan, LocalDate.now());
    }

    @Override
    public boolean catatAbsensi(int userId, String status, LocalDateTime waktuAbsensi, String keterangan) {
        LOGGER.info("Mencatat absensi untuk karyawan " + userId + ", status: " + status + ", waktu: " + waktuAbsensi);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        Karyawan karyawan = getKaryawanById(userId);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan tidak ditemukan";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        if (!status.equals("HADIR") && !status.equals("TIDAK HADIR")) {
            lastErrorMessage = "Status absensi tidak valid: " + status;
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        if (status.equals("HADIR")) {
            if (isSudahAbsen(userId, LocalDate.now(), "HADIR")) {
                lastErrorMessage = "Karyawan sudah absen masuk hari ini";
                LOGGER.warning(lastErrorMessage);
                return false;
            }
            return checkIn(karyawan, waktuAbsensi);
        } else {
            return catatIzinSakit(karyawan, keterangan.split(":")[0], keterangan, LocalDate.now());
        }
    }

    @Override
    public List<Absensi> getData() {
        return getAllAbsensi();
    }

    @Override
    public List<Absensi> getAbsensiByDate(Date startDate, Date endDate) {
        LOGGER.info("Mengambil absensi untuk rentang tanggal " + startDate + " sampai " + endDate);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        User loggedInUser = FormManager.getLoggedInUser();
        if (loggedInUser == null) {
            lastErrorMessage = "Tidak ada pengguna yang login";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        String role = loggedInUser.getRole() != null ? loggedInUser.getRole().toLowerCase() : "";
        String username = loggedInUser.getUsername() != null ? loggedInUser.getUsername() : "unknown";
        LOGGER.info("Role pengguna: " + role + ", Username: " + username);

        if ("karyawan".equals(role)) {
            String rfid = loggedInUser.getRfid();
            if (rfid == null || rfid.trim().isEmpty()) {
                lastErrorMessage = "RFID tidak valid untuk pengguna: " + username;
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }
            LOGGER.info("Karyawan mengambil absensi untuk RFID: " + rfid);
            return getAbsensiByRfidAndDate(rfid, startDate, endDate);
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new java.sql.Date(startDate.getTime()));
            stmt.setDate(2, new java.sql.Date(endDate.getTime()));
            LOGGER.fine("Menjalankan query: " + sql + " dengan startDate: " + startDate + ", endDate: " + endDate);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk rentang tanggal");
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil absensi untuk rentang tanggal: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil absensi untuk rentang tanggal", e);
        }
        return list;
    }

    @Override
    public List<Absensi> getAbsensiByRfidAndDate(String rfid, Date startDate, Date endDate) {
        LOGGER.info("Mengambil absensi untuk RFID: " + rfid + " dari " + startDate + " sampai " + endDate);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        if (rfid == null || rfid.trim().isEmpty()) {
            lastErrorMessage = "RFID tidak boleh kosong";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        Karyawan karyawan = getKaryawanByRfid(rfid);
        if (karyawan == null) {
            lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE k.rfid = ? AND a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rfid);
            stmt.setDate(2, new java.sql.Date(startDate.getTime()));
            stmt.setDate(3, new java.sql.Date(endDate.getTime()));
            LOGGER.fine("Menjalankan query: " + sql + " dengan rfid: " + rfid + ", startDate: " + startDate + ", endDate: " + endDate);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk RFID dan rentang tanggal");
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil absensi untuk RFID " + rfid + ": " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil absensi untuk RFID: " + rfid, e);
        }
        return list;
    }

    @Override
    public List<Absensi> getAbsensiByDateRange(Date startDate, Date endDate) {
        return getAbsensiByDate(startDate, endDate);
    }

    @Override
    public List<Absensi> searchAbsensi(String keyword, Date startDate, Date endDate) {
        LOGGER.info("Mencari absensi dengan keyword: " + keyword + ", rentang tanggal: " + startDate + " sampai " + endDate);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        User loggedInUser = FormManager.getLoggedInUser();
        if (loggedInUser == null) {
            lastErrorMessage = "Tidak ada pengguna yang login";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        String role = loggedInUser.getRole() != null ? loggedInUser.getRole().toLowerCase() : "";
        String username = loggedInUser.getUsername() != null ? loggedInUser.getUsername() : "unknown";
        LOGGER.info("Role pengguna: " + role + ", Username: " + username);

        if ("karyawan".equals(role)) {
            String rfid = loggedInUser.getRfid();
            if (rfid == null || rfid.trim().isEmpty()) {
                lastErrorMessage = "RFID tidak valid untuk pengguna: " + username;
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }
            Karyawan karyawan = getKaryawanByRfid(rfid);
            if (karyawan == null) {
                lastErrorMessage = "Karyawan dengan RFID " + rfid + " tidak ditemukan";
                LOGGER.warning(lastErrorMessage);
                return new ArrayList<>();
            }
            LOGGER.info("Karyawan mencari absensi untuk RFID: " + rfid);
            return getAbsensiByRfidAndDate(rfid, startDate, endDate);
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE (k.nama_karyawan LIKE ? OR UPPER(a.status) LIKE UPPER(?) OR a.keterangan LIKE ?) " +
                     "AND a.tanggal BETWEEN ? AND ? AND a.is_delete = FALSE " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            String kw = "%" + (keyword != null ? keyword.trim() : "") + "%";
            stmt.setString(1, kw);
            stmt.setString(2, kw);
            stmt.setString(3, kw);
            stmt.setDate(4, new java.sql.Date(startDate.getTime()));
            stmt.setDate(5, new java.sql.Date(endDate.getTime()));
            LOGGER.fine("Menjalankan query: " + sql + " dengan keyword: " + kw + ", startDate: " + startDate + ", endDate: " + endDate);
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk pencarian");
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mencari absensi: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mencari absensi dengan keyword: " + keyword, e);
        }
        return list;
    }

    @Override
    public List<Absensi> getRiwayatAbsensi(int idKaryawan, long time, long time0) {
        return getRiwayatAbsensi(idKaryawan,
                new java.sql.Date(time).toLocalDate(),
                new java.sql.Date(time0).toLocalDate());
    }

    @Override
    public List<Absensi> searchAbsensiForKasir(String keyword, int idKaryawan, long l, long l0) {
        return searchAbsensiByKaryawan(idKaryawan, keyword,
                new java.sql.Date(l).toLocalDate(),
                new java.sql.Date(l0).toLocalDate());
    }

    @Override
    public List<Absensi> getAbsensiByKasir(int idKaryawan) {
        LOGGER.info("Mengambil absensi untuk karyawan dengan ID karyawan: " + idKaryawan);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        if (idKaryawan <= 0) {
            lastErrorMessage = "ID Karyawan tidak valid";
            LOGGER.warning(lastErrorMessage);
            return new ArrayList<>();
        }

        List<Absensi> list = new ArrayList<>();
        String sql = "SELECT a.*, k.nama_karyawan FROM absensi a " +
                     "JOIN karyawan k ON a.id_karyawan = k.id_karyawan " +
                     "WHERE a.id_karyawan = ? AND a.is_delete = FALSE " +
                     "AND a.tanggal BETWEEN ? AND ? " +
                     "ORDER BY a.tanggal DESC";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            stmt.setDate(2, java.sql.Date.valueOf(LocalDate.now().minusMonths(1)));
            stmt.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan + ", startDate: " + LocalDate.now().minusMonths(1) + ", endDate: " + LocalDate.now());
            ResultSet rs = stmt.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                list.add(parseAbsensi(rs));
                rowCount++;
            }
            lastErrorMessage = "";
            LOGGER.info("Mengambil " + rowCount + " entri absensi untuk karyawan dengan ID karyawan: " + idKaryawan);
        } catch (SQLException e) {
            lastErrorMessage = "Gagal mengambil absensi untuk karyawan dengan ID karyawan " + idKaryawan + ": " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat mengambil absensi untuk karyawan dengan ID karyawan: " + idKaryawan, e);
        }
        return list;
    }

    // Metode tambahan untuk mendukung validasi absensi
    public boolean isSudahAbsenMasuk(int idKaryawan, LocalDate tanggal) {
        return isSudahAbsen(idKaryawan, tanggal, "HADIR");
    }

    public boolean isSudahAbsenPulang(int idKaryawan, LocalDate tanggal) {
        LOGGER.info("Memeriksa apakah karyawan " + idKaryawan + " sudah absen pulang pada " + tanggal);
        if (conn == null) {
            lastErrorMessage = "Koneksi database tidak tersedia";
            LOGGER.warning(lastErrorMessage);
            return false;
        }

        String sql = "SELECT * FROM absensi WHERE id_karyawan = ? AND tanggal = ? AND status = 'HADIR' AND waktu_pulang IS NOT NULL AND is_delete = FALSE";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idKaryawan);
            stmt.setDate(2, java.sql.Date.valueOf(tanggal));
            LOGGER.fine("Menjalankan query: " + sql + " dengan id_karyawan: " + idKaryawan + ", tanggal: " + tanggal);
            ResultSet rs = stmt.executeQuery();
            boolean sudahAbsen = rs.next();
            lastErrorMessage = "";
            LOGGER.info("Hasil pemeriksaan absen pulang: " + sudahAbsen);
            return sudahAbsen;
        } catch (SQLException e) {
            lastErrorMessage = "Gagal memeriksa status absen pulang: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "Error saat memeriksa absen pulang untuk karyawan " + idKaryawan, e);
            return false;
        }
    }
    
        public boolean isSudahIzinSakit(int idKaryawan, LocalDate tanggal) {
        return isSudahAbsen(idKaryawan, tanggal, "TIDAK HADIR");
    }

    @Override
    public List<Absensi> searchData(String keyword) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Absensi> getRiwayatKaryawan(int idKaryawan, LocalDate start, LocalDate end) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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