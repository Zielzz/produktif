package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Barang;
import com.radja.service.ServiceBarang;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BarangDAO implements ServiceBarang {

    private final Connection conn;
    public static final int STOK_MINIMAL = 50;

    public BarangDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Barang model) {
        String sql = "INSERT INTO barang (nama_barang, satuan, stok, harga_satuan, insert_by, isi_sachet) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, model.getNamaBarang());
            pstmt.setString(2, model.getSatuan());
            pstmt.setInt(3, model.getStok());
            pstmt.setDouble(4, model.getHargaSatuan());
            pstmt.setObject(5, model.getInsertBy(), java.sql.Types.INTEGER);
            pstmt.setObject(6, model.getIsiSachet(), java.sql.Types.DECIMAL);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        model.setIdBarang(rs.getInt(1)); // Set ID yang dihasilkan ke model
                    }
                }
            } else {
                throw new IllegalArgumentException("Gagal menambahkan barang: Tidak ada baris yang terpengaruh");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menambahkan barang: " + e.getMessage());
        }
    }

    @Override
    public void updateData(Barang model) {
        String sql = "UPDATE barang SET nama_barang = ?, satuan = ?, stok = ?, harga_satuan = ?, update_by = ?, update_at = NOW(), isi_sachet = ? WHERE id_barang = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, model.getNamaBarang());
            pstmt.setString(2, model.getSatuan());
            pstmt.setInt(3, model.getStok());
            pstmt.setDouble(4, model.getHargaSatuan());
            pstmt.setObject(5, model.getUpdateBy(), java.sql.Types.INTEGER);
            pstmt.setObject(6, model.getIsiSachet(), java.sql.Types.DECIMAL);
            pstmt.setInt(7, model.getIdBarang());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Barang dengan ID " + model.getIdBarang() + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memperbarui barang: " + e.getMessage());
        }
    }

    @Override
    public void deleteData(Barang model) {
        String sql = "UPDATE barang SET delete_by = ?, delete_at = NOW(), is_delete = TRUE WHERE id_barang = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, model.getDeleteBy(), java.sql.Types.INTEGER);
            pstmt.setInt(2, model.getIdBarang());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Barang dengan ID " + model.getIdBarang() + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus barang: " + e.getMessage());
        }
    }

    @Override
    public void restoreData(Barang model) {
        String checkQuery = "SELECT is_delete FROM barang WHERE id_barang = ?";
        String sql = "UPDATE barang SET delete_by = NULL, delete_at = NULL, is_delete = FALSE WHERE id_barang = ?";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, model.getIdBarang());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && !rs.getBoolean("is_delete")) {
                    return; // Barang sudah aktif
                }
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, model.getIdBarang());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new IllegalArgumentException("Barang dengan ID " + model.getIdBarang() + " tidak ditemukan");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memulihkan barang: " + e.getMessage());
        }
    }

    @Override
    public List<Barang> getData(String filter) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT id_barang, nama_barang, satuan, stok, harga_satuan, insert_by, update_by, delete_by, insert_at, update_at, delete_at, is_delete, isi_sachet FROM barang";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " WHERE is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " WHERE is_delete = TRUE";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Barang model = new Barang();
                model.setIdBarang(rs.getInt("id_barang"));
                model.setNamaBarang(rs.getString("nama_barang"));
                model.setSatuan(rs.getString("satuan"));
                model.setStok(rs.getInt("stok"));
                model.setHargaSatuan(rs.getDouble("harga_satuan"));
                model.setInsertBy(rs.getObject("insert_by", Integer.class));
                model.setUpdateBy(rs.getObject("update_by", Integer.class));
                model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                model.setIsDelete(rs.getBoolean("is_delete"));
                model.setIsiSachet(rs.getObject("isi_sachet", Double.class));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mengambil data barang: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Barang> searchData(String keyword, String filter) {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT id_barang, nama_barang, satuan, stok, harga_satuan, insert_by, update_by, delete_by, insert_at, update_at, delete_at, is_delete, isi_sachet FROM barang " +
                     "WHERE (id_barang LIKE ? OR nama_barang LIKE ? OR satuan LIKE ? OR stok LIKE ?)";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " AND is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " AND is_delete = TRUE";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            pstmt.setString(3, "%" + keyword + "%");
            pstmt.setString(4, "%" + keyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Barang model = new Barang();
                    model.setIdBarang(rs.getInt("id_barang"));
                    model.setNamaBarang(rs.getString("nama_barang"));
                    model.setSatuan(rs.getString("satuan"));
                    model.setStok(rs.getInt("stok"));
                    model.setHargaSatuan(rs.getDouble("harga_satuan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));
                    model.setIsiSachet(rs.getObject("isi_sachet", Double.class));
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari data barang: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<Barang> getBarangStokKritis() {
        List<Barang> list = new ArrayList<>();
        String sql = "SELECT id_barang, nama_barang, satuan, stok, harga_satuan, insert_by, update_by, delete_by, insert_at, update_at, delete_at, is_delete, isi_sachet FROM barang " +
                     "WHERE stok <= ? AND is_delete = FALSE";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, STOK_MINIMAL);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Barang model = new Barang();
                    model.setIdBarang(rs.getInt("id_barang"));
                    model.setNamaBarang(rs.getString("nama_barang"));
                    model.setSatuan(rs.getString("satuan"));
                    model.setStok(rs.getInt("stok"));
                    model.setHargaSatuan(rs.getDouble("harga_satuan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));
                    model.setIsiSachet(rs.getObject("isi_sachet", Double.class));
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mengambil data barang stok kritis: " + e.getMessage());
        }
        return list;
    }

    @Override
    public void logBarangMasuk(Barang model, int idUsers) {
        // Validasi input untuk memastikan stok valid dan id_barang diisi
        if (model.getStok() <= 0) {
            throw new IllegalArgumentException("Stok barang masuk harus lebih dari 0");
        }
        if (model.getIdBarang() == 0) {
            throw new IllegalArgumentException("ID barang tidak valid untuk log barang masuk");
        }
        if (model.getNamaBarang() == null || model.getNamaBarang().trim().isEmpty()) {
            throw new IllegalArgumentException("Nama barang tidak boleh kosong");
        }
        if (model.getHargaSatuan() <= 0) {
            throw new IllegalArgumentException("Harga satuan harus lebih dari 0");
        }

        String sql = "INSERT INTO log_barang_masuk (id_barang, nama_barang, satuan, stok, harga_satuan, id_users, tanggal_masuk) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, model.getIdBarang());
            pstmt.setString(2, model.getNamaBarang());
            pstmt.setString(3, model.getSatuan());
            pstmt.setInt(4, model.getStok()); // Stok di sini adalah stok tambahan yang dimasukkan
            pstmt.setDouble(5, model.getHargaSatuan());
            pstmt.setInt(6, idUsers);
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Gagal menyimpan log barang masuk: Tidak ada baris yang terpengaruh");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menyimpan log barang masuk: " + e.getMessage());
        }
    }
}