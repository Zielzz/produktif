package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Pelanggan;
import com.radja.service.ServicePelanggan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PelangganDAO implements ServicePelanggan {

    private final Connection conn;

    public PelangganDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Pelanggan model) {
        String sql = "INSERT INTO pelanggan (id_pelanggan, nama_pelanggan, telepon, alamat, member_status, insert_by, insert_at, jumlah_transaksi) " +
                    "VALUES (?, ?, ?, ?, ?, ?, NOW(), 0)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getIdPelanggan());
            st.setString(2, model.getNamaPelanggan());
            st.setString(3, model.getTelepon());
            st.setString(4, model.getAlamat());
            st.setString(5, model.getMemberStatus() != null ? model.getMemberStatus() : "tidak aktif");
            st.setObject(6, model.getInsertBy(), java.sql.Types.INTEGER);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No rows inserted for pelanggan ID " + model.getIdPelanggan());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert pelanggan for ID " + model.getIdPelanggan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String generateIdPelanggan() {
        String urutan;
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String kodePrefix = "CR" + now.format(formatter);

        String sql = "SELECT id_pelanggan FROM pelanggan WHERE id_pelanggan LIKE ? ORDER BY id_pelanggan DESC LIMIT 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, kodePrefix + "%");
            try (ResultSet rs = st.executeQuery()) {
                int nomor = 1;
                if (rs.next()) {
                    String lastId = rs.getString("id_pelanggan");
                    nomor = Integer.parseInt(lastId.substring(10)) + 1;
                }
                urutan = kodePrefix + String.format("%03d", nomor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to generate ID pelanggan: " + e.getMessage(), e);
        }
        return urutan;
    }

    @Override
    public void updateData(Pelanggan model) {
        try (PreparedStatement st = conn.prepareStatement(
                "UPDATE pelanggan SET nama_pelanggan=?, telepon=?, alamat=?, member_status=?, update_by=?, update_at=NOW() WHERE id_pelanggan=?")) {
            st.setString(1, model.getNamaPelanggan());
            st.setString(2, model.getTelepon());
            st.setString(3, model.getAlamat());
            st.setString(4, model.getMemberStatus());
            st.setObject(5, model.getUpdateBy(), java.sql.Types.INTEGER);
            st.setString(6, model.getIdPelanggan());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No rows updated for pelanggan ID " + model.getIdPelanggan());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update pelanggan for ID " + model.getIdPelanggan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteData(Pelanggan model) {
        try (PreparedStatement st = conn.prepareStatement(
                "UPDATE pelanggan SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_pelanggan=?")) {
            st.setObject(1, model.getDeleteBy(), java.sql.Types.INTEGER);
            st.setString(2, model.getIdPelanggan());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No rows deleted for pelanggan ID " + model.getIdPelanggan());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete pelanggan for ID " + model.getIdPelanggan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void restoreData(Pelanggan model) {
        try (PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT is_delete FROM pelanggan WHERE id_pelanggan = ?")) {
            checkStmt.setString(1, model.getIdPelanggan());
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next() && !rs.getBoolean("is_delete")) {
                    return; // Already restored
                }
            }
            try (PreparedStatement st = conn.prepareStatement(
                    "UPDATE pelanggan SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_pelanggan=?")) {
                st.setString(1, model.getIdPelanggan());
                int rowsAffected = st.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("No rows restored for pelanggan ID " + model.getIdPelanggan());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to restore pelanggan for ID " + model.getIdPelanggan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void updateMemberStatus(Pelanggan model) {
        try (PreparedStatement st = conn.prepareStatement(
                "UPDATE pelanggan SET member_status=?, update_by=?, update_at=NOW() WHERE id_pelanggan=?")) {
            st.setString(1, model.getMemberStatus());
            st.setObject(2, model.getUpdateBy(), java.sql.Types.INTEGER);
            st.setString(3, model.getIdPelanggan());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No rows updated for pelanggan ID " + model.getIdPelanggan());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update member status for pelanggan ID " + model.getIdPelanggan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pelanggan> getData(String filter) {
        List<Pelanggan> list = new ArrayList<>();
        String sql = "SELECT id_pelanggan, nama_pelanggan, telepon, alamat, member_status, insert_by, update_by, delete_by, is_delete, jumlah_transaksi " +
                     "FROM pelanggan";
        if (filter.equals("Aktif")) {
            sql += " WHERE is_delete = FALSE AND member_status = 'aktif'";
        } else if (filter.equals("Terhapus")) {
            sql += " WHERE is_delete = TRUE";
        } else if (!filter.equals("Semua")) {
            sql += " WHERE is_delete = FALSE";
        }
        try (PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Pelanggan model = new Pelanggan();
                model.setIdPelanggan(rs.getString("id_pelanggan"));
                model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                model.setTelepon(rs.getString("telepon"));
                model.setAlamat(rs.getString("alamat"));
                model.setMemberStatus(rs.getString("member_status"));
                model.setInsertBy(rs.getObject("insert_by", Integer.class));
                model.setUpdateBy(rs.getObject("update_by", Integer.class));
                model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                model.setIsDelete(rs.getBoolean("is_delete"));
                model.setJumlahTransaksi(rs.getInt("jumlah_transaksi"));
                list.add(model);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve pelanggan data: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
public List<Pelanggan> searchData(String keyword, String filter) {
    List<Pelanggan> list = new ArrayList<>();
    String sql = "SELECT id_pelanggan, nama_pelanggan, telepon, alamat, member_status, insert_by, update_by, delete_by, is_delete, jumlah_transaksi " +
                 "FROM pelanggan WHERE (nama_pelanggan LIKE ? OR telepon LIKE ?)";
    if (filter.equals("Aktif")) {
        sql += " AND is_delete = FALSE AND member_status = 'aktif'";
    } else if (filter.equals("Terhapus")) {
        sql += " AND is_delete = TRUE";
    } else if (!filter.equals("Semua")) {
        sql += " AND is_delete = FALSE";
    }
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, "%" + keyword + "%"); // Search in nama_pelanggan
        st.setString(2, "%" + keyword + "%"); // Search in telepon
        try (ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Pelanggan model = new Pelanggan();
                model.setIdPelanggan(rs.getString("id_pelanggan"));
                model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                model.setTelepon(rs.getString("telepon"));
                model.setAlamat(rs.getString("alamat"));
                model.setMemberStatus(rs.getString("member_status"));
                model.setInsertBy(rs.getObject("insert_by", Integer.class));
                model.setUpdateBy(rs.getObject("update_by", Integer.class));
                model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                model.setIsDelete(rs.getBoolean("is_delete"));
                model.setJumlahTransaksi(rs.getInt("jumlah_transaksi"));
                list.add(model);
            }
        }
    } catch (SQLException e) {
        throw new RuntimeException("Failed to search pelanggan data: " + e.getMessage(), e);
    }
    return list;
}
}
    
