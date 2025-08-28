package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Barang;
import com.radja.model.OpnameStok;
import com.radja.model.DetailOpnameStok;
import com.radja.service.ServiceOpnameStok;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OpnameStokDAO implements ServiceOpnameStok {
    private final Connection conn;

    public OpnameStokDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertOpnameStok(OpnameStok opname, List<DetailOpnameStok> details) {
        String sqlOpname = "INSERT INTO opname_stok (tanggal_opname, keterangan, insert_by, insert_at, is_delete) VALUES (?, ?, ?, ?, ?)";
        String sqlDetail = "INSERT INTO detail_opname_stok (id_opname, id_barang, stok_sistem, stok_fisik) VALUES (?, ?, ?, ?)";
        
        try {
            conn.setAutoCommit(false);

            // Insert ke tabel opname_stok
            try (PreparedStatement pstmtOpname = conn.prepareStatement(sqlOpname, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstmtOpname.setObject(1, opname.getTanggalOpname());
                pstmtOpname.setString(2, opname.getKeterangan());
                pstmtOpname.setObject(3, opname.getInsertBy(), java.sql.Types.INTEGER);
                pstmtOpname.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                pstmtOpname.setBoolean(5, opname.isDelete());
                pstmtOpname.executeUpdate();

                try (ResultSet rs = pstmtOpname.getGeneratedKeys()) {
                    if (rs.next()) {
                        opname.setIdOpname(rs.getInt(1));
                    }
                }
            }

            // Insert ke tabel detail_opname_stok
            try (PreparedStatement pstmtDetail = conn.prepareStatement(sqlDetail)) {
                for (DetailOpnameStok detail : details) {
                    pstmtDetail.setInt(1, opname.getIdOpname());
                    pstmtDetail.setInt(2, detail.getIdBarang());
                    pstmtDetail.setInt(3, detail.getStokSistem());
                    pstmtDetail.setInt(4, detail.getStokFisik());
                    pstmtDetail.addBatch();
                }
                pstmtDetail.executeBatch();
            }

            // Update stok di tabel barang
            String sqlUpdateBarang = "UPDATE barang SET stok = ? WHERE id_barang = ?";
            try (PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdateBarang)) {
                for (DetailOpnameStok detail : details) {
                    pstmtUpdate.setInt(1, detail.getStokFisik());
                    pstmtUpdate.setInt(2, detail.getIdBarang());
                    pstmtUpdate.addBatch();
                }
                pstmtUpdate.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                throw new IllegalArgumentException("Rollback failed: " + rollbackEx.getMessage());
            }
            throw new IllegalArgumentException("Gagal menyimpan opname stok: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                throw new IllegalArgumentException("Failed to reset auto-commit: " + e.getMessage());
            }
        }
    }

    @Override
    public List<OpnameStok> getAllOpnameStok() {
        List<OpnameStok> list = new ArrayList<>();
        String sql = "SELECT * FROM opname_stok WHERE is_delete = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                OpnameStok opname = new OpnameStok();
                opname.setIdOpname(rs.getInt("id_opname"));
                opname.setTanggalOpname(rs.getObject("tanggal_opname", LocalDate.class));
                opname.setKeterangan(rs.getString("keterangan"));
                opname.setInsertBy(rs.getObject("insert_by", Integer.class));
                opname.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                opname.setIsDelete(rs.getBoolean("is_delete"));
                list.add(opname);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mengambil data opname stok: " + e.getMessage());
        }
        return list;
    }

   @Override
public List<DetailOpnameStok> getDetailOpnameStok(int idOpname) {
    List<DetailOpnameStok> list = new ArrayList<>();
    String sql = "SELECT dos.*, b.nama_barang FROM detail_opname_stok dos JOIN barang b ON dos.id_barang = b.id_barang WHERE dos.id_opname = ?";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, idOpname);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                DetailOpnameStok detail = new DetailOpnameStok();
                detail.setIdDetailOpname(rs.getInt("id_detail_opname"));
                detail.setIdOpname(rs.getInt("id_opname"));
                detail.setIdBarang(rs.getInt("id_barang"));
                detail.setNamaBarang(rs.getString("nama_barang")); // Set namaBarang
                detail.setStokSistem(rs.getInt("stok_sistem"));
                detail.setStokFisik(rs.getInt("stok_fisik"));
                detail.setSelisih(rs.getInt("selisih"));
                list.add(detail);
            }
        }
    } catch (SQLException e) {
        throw new IllegalArgumentException("Gagal mengambil detail opname stok: " + e.getMessage());
    }
    return list;
}
}