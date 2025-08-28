package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Karyawan;
import com.radja.service.ServiceKaryawan;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KaryawanDAO implements ServiceKaryawan {

    private final Connection conn;
    private static final Logger LOGGER = Logger.getLogger(KaryawanDAO.class.getName());

    public KaryawanDAO() {
        this.conn = Koneksi.getConnection();
    }
    
// Fungsi untuk mendapatkan karyawan berdasarkan RFID
    public Karyawan getKaryawanByRfid(String rfid) {
        String sql = "SELECT * FROM karyawan WHERE rfid = ? AND is_delete = false";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rfid);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Karyawan karyawan = new Karyawan();
                karyawan.setIdKaryawan(rs.getInt("id_karyawan"));
                karyawan.setNamaKaryawan(rs.getString("nama_karyawan"));
                karyawan.setTelepon(rs.getString("telepon"));
                karyawan.setAlamat(rs.getString("alamat"));
                karyawan.setRfid(rs.getString("rfid"));
                return karyawan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
public boolean insertData(Karyawan model) {
    String sql = "INSERT INTO karyawan (nama_karyawan, telepon, alamat, rfid, insert_by) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, model.getNamaKaryawan());
        st.setString(2, model.getTelepon());
        st.setString(3, model.getAlamat());
        st.setString(4, model.getRfid()); // Tambahkan RFID
        st.setInt(5, model.getInsertBy());
        int affectedRows = st.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error inserting data", e);
        // Tambahkan pengecekan khusus untuk error duplikasi RFID
        if (e.getSQLState().equals("23505")) { // Error code untuk constraint violation
            LOGGER.log(Level.WARNING, "Duplicate RFID detected: " + model.getRfid());
        }
    }
    return false;
}

   @Override
public boolean updateData(Karyawan model) {
    String sql = "UPDATE karyawan SET nama_karyawan=?, telepon=?, alamat=?, rfid=?, update_by=?, update_at=NOW() WHERE id_karyawan=?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, model.getNamaKaryawan());
        st.setString(2, model.getTelepon());
        st.setString(3, model.getAlamat());
        st.setString(4, model.getRfid()); // Tambahkan RFID
        st.setInt(5, model.getUpdateBy());
        st.setInt(6, model.getIdKaryawan());
        int affectedRows = st.executeUpdate();
        return affectedRows > 0;
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error updating data", e);
    }
    return false;
}

@Override
public boolean deleteData(int idKaryawan) {
    String sqlDeleteAbsensi = "DELETE FROM absensi WHERE id_karyawan = ?";
    String sqlDeleteKaryawan = "DELETE FROM karyawan WHERE id_karyawan = ?";

    try {
        conn.setAutoCommit(false);  // mulai transaksi

        try (PreparedStatement stAbsensi = conn.prepareStatement(sqlDeleteAbsensi)) {
            stAbsensi.setInt(1, idKaryawan);
            stAbsensi.executeUpdate();  // hapus absensi dulu
        }

        int affectedRows;
        try (PreparedStatement stKaryawan = conn.prepareStatement(sqlDeleteKaryawan)) {
            stKaryawan.setInt(1, idKaryawan);
            affectedRows = stKaryawan.executeUpdate();  // hapus karyawan
        }

        if (affectedRows > 0) {
            conn.commit();  // commit transaksi
            return true;
        } else {
            conn.rollback();  // rollback kalau tidak ada yang terhapus
        }
    } catch (SQLException e) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Rollback error", ex);
        }
        LOGGER.log(Level.SEVERE, "Error deleting data", e);
    } finally {
        try {
            conn.setAutoCommit(true);  // kembalikan auto commit
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error resetting auto-commit", e);
        }
    }
    return false;
}




    @Override
    public boolean restoreData(int idKaryawan) {
        try {
            conn.setAutoCommit(false); // Mulai transaksi
            // Pembaruan status pemulihan
            updateDeleteStatus(idKaryawan, null, false);
            conn.commit(); // Sukses, lakukan commit
            return true;
        } catch (SQLException e) {
            try {
                conn.rollback(); // Jika ada error, rollback transaksi
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction", ex);
            }
            LOGGER.log(Level.SEVERE, "Error restoring data", e);
        } finally {
            try {
                conn.setAutoCommit(true); // Kembalikan auto-commit ke default
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Error resetting auto-commit", e);
            }
        }
        return false;
    }

    private void updateDeleteStatus(int idKaryawan, Integer deleteBy, boolean isDelete) {
        String sql = "UPDATE karyawan SET delete_by = ?, delete_at = NOW(), is_delete = ? WHERE id_karyawan = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, deleteBy, Types.INTEGER); // Handle deleteBy as nullable
            st.setBoolean(2, isDelete);
            st.setInt(3, idKaryawan);
            st.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating delete status", e);
        }
    }

  @Override
public List<Karyawan> getData(String filter) {
    List<Karyawan> list = new ArrayList<>();
    String sql = "SELECT id_karyawan, nama_karyawan, telepon, alamat, rfid, is_delete FROM karyawan";

    if ("Aktif".equals(filter)) {
        sql += " WHERE is_delete = FALSE";
    } else if ("Terhapus".equals(filter)) {
        sql += " WHERE is_delete = TRUE";
    }

    try (PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
        while (rs.next()) {
            Karyawan model = new Karyawan();
            model.setIdKaryawan(rs.getInt("id_karyawan"));
            model.setNamaKaryawan(rs.getString("nama_karyawan"));
            model.setTelepon(rs.getString("telepon"));
            model.setAlamat(rs.getString("alamat"));
            model.setRfid(rs.getString("rfid")); // <== Tambahan penting
            model.setIsDelete(rs.getBoolean("is_delete"));
            list.add(model);
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error fetching data", e);
    }
    return list;
}


    @Override
    public List<Karyawan> searchData(String keyword, String filter) {
        List<Karyawan> list = new ArrayList<>();
        String sql = "SELECT id_karyawan, nama_karyawan, telepon, alamat, is_delete FROM karyawan "
                + "WHERE (id_karyawan LIKE ? OR nama_karyawan LIKE ? OR telepon LIKE ? OR alamat LIKE ?)";

        if ("Aktif".equals(filter)) {
            sql += " AND is_delete = FALSE";
        } else if ("Terhapus".equals(filter)) {
            sql += " AND is_delete = TRUE";
        }

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Karyawan model = new Karyawan();
                    model.setIdKaryawan(rs.getInt("id_karyawan"));
                    model.setNamaKaryawan(rs.getString("nama_karyawan"));
                    model.setTelepon(rs.getString("telepon"));
                    model.setAlamat(rs.getString("alamat"));
                    model.setIsDelete(rs.getBoolean("is_delete"));
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error searching data", e);
        }
        return list;
    }

    @Override
    public Karyawan getKaryawanByRFID(String rfid) {
        String sql = "SELECT id_karyawan, nama_karyawan, telepon, alamat, is_delete FROM karyawan WHERE rfid = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, rfid);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Karyawan karyawan = new Karyawan();
                    karyawan.setIdKaryawan(rs.getInt("id_karyawan"));
                    karyawan.setNamaKaryawan(rs.getString("nama_karyawan"));
                    karyawan.setTelepon(rs.getString("telepon"));
                    karyawan.setAlamat(rs.getString("alamat"));
                    karyawan.setIsDelete(rs.getBoolean("is_delete"));
                    return karyawan;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching karyawan by RFID", e);
        }
        return null; // Jika tidak ditemukan
    }

    @Override
    public boolean updateRFID(int idKaryawan, String newRFID) {
        String sql = "UPDATE karyawan SET rfid = ? WHERE id_karyawan = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, newRFID);
            st.setInt(2, idKaryawan);
            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating RFID", e);
        }
        return false;
    }

    @Override
    public boolean prosesAbsensi(int idKaryawan) {
        String sql = "INSERT INTO absensi (id_karyawan, waktu_absen) VALUES (?, NOW())";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idKaryawan);
            int affectedRows = st.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error processing absensi", e);
        }
        return false;
    }

    // Implementasikan getCurrentUserId() sesuai konteks aplikasi
    private int getCurrentUserId() {
        // Placeholder untuk logika mendapatkan ID pengguna yang sedang aktif
        return 1; // Gantikan dengan logika yang sesuai
    }
}