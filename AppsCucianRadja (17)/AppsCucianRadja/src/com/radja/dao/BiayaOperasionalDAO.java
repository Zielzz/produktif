package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.BiayaOperasional;
import com.radja.service.ServiceBiayaOperasional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BiayaOperasionalDAO implements ServiceBiayaOperasional {

    private final Connection conn;

    public BiayaOperasionalDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(BiayaOperasional model) {
        String sql = "INSERT INTO biaya_operasional (deskripsi, jumlah_biaya, tanggal_biaya, insert_by) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, model.getDeskripsi());
            pstmt.setDouble(2, model.getJumlahBiaya());
            pstmt.setObject(3, model.getTanggalBiaya() != null ? java.sql.Date.valueOf(model.getTanggalBiaya()) : null);
            pstmt.setObject(4, model.getInsertBy(), java.sql.Types.INTEGER);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        model.setIdBiaya(rs.getInt(1));
                    }
                }
            } else {
                throw new IllegalArgumentException("Gagal menambahkan biaya operasional: Tidak ada baris yang terpengaruh");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menambahkan biaya operasional: " + e.getMessage());
        }
    }

    @Override
    public void updateData(BiayaOperasional model) {
        String sql = "UPDATE biaya_operasional SET deskripsi = ?, jumlah_biaya = ?, tanggal_biaya = ? WHERE id_biaya = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, model.getDeskripsi());
            pstmt.setDouble(2, model.getJumlahBiaya());
            pstmt.setObject(3, model.getTanggalBiaya() != null ? java.sql.Date.valueOf(model.getTanggalBiaya()) : null);
            pstmt.setInt(4, model.getIdBiaya());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Biaya operasional dengan ID " + model.getIdBiaya() + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memperbarui biaya operasional: " + e.getMessage());
        }
    }

    @Override
    public void deleteData(BiayaOperasional model) {
        String sql = "UPDATE biaya_operasional SET is_delete = TRUE WHERE id_biaya = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, model.getIdBiaya());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Biaya operasional dengan ID " + model.getIdBiaya() + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghapus biaya operasional: " + e.getMessage());
        }
    }

    @Override
    public void restoreData(BiayaOperasional model) {
        String checkQuery = "SELECT is_delete FROM biaya_operasional WHERE id_biaya = ?";
        String sql = "UPDATE biaya_operasional SET is_delete = FALSE WHERE id_biaya = ?";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, model.getIdBiaya());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && !rs.getBoolean("is_delete")) {
                    return; // Biaya sudah aktif
                }
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, model.getIdBiaya());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new IllegalArgumentException("Biaya operasional dengan ID " + model.getIdBiaya() + " tidak ditemukan");
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal memulihkan biaya operasional: " + e.getMessage());
        }
    }

    @Override
    public List<BiayaOperasional> getData(String filter) {
        List<BiayaOperasional> list = new ArrayList<>();
        String sql = "SELECT id_biaya, deskripsi, jumlah_biaya, tanggal_biaya, insert_by, insert_at, is_delete " +
                     "FROM biaya_operasional";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " WHERE is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " WHERE is_delete = TRUE";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                BiayaOperasional model = new BiayaOperasional();
                model.setIdBiaya(rs.getInt("id_biaya"));
                model.setDeskripsi(rs.getString("deskripsi"));
                model.setJumlahBiaya(rs.getDouble("jumlah_biaya"));
                model.setTanggalBiaya(rs.getObject("tanggal_biaya", LocalDate.class));
                model.setInsertBy(rs.getObject("insert_by", Integer.class));
                model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                model.setIsDelete(rs.getBoolean("is_delete"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mengambil data biaya operasional: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<BiayaOperasional> searchData(String keyword, String filter) {
        List<BiayaOperasional> list = new ArrayList<>();
        String sql = "SELECT id_biaya, deskripsi, jumlah_biaya, tanggal_biaya, insert_by, insert_at, is_delete " +
                     "FROM biaya_operasional WHERE deskripsi LIKE ?";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " AND is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " AND is_delete = TRUE";
        }
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BiayaOperasional model = new BiayaOperasional();
                    model.setIdBiaya(rs.getInt("id_biaya"));
                    model.setDeskripsi(rs.getString("deskripsi"));
                    model.setJumlahBiaya(rs.getDouble("jumlah_biaya"));
                    model.setTanggalBiaya(rs.getObject("tanggal_biaya", LocalDate.class));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari data biaya operasional: " + e.getMessage());
        }
        return list;
    }
}