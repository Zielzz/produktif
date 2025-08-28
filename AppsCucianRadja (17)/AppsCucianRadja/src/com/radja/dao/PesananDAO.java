package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Layanan;
import com.radja.model.Pelanggan;
import com.radja.model.Pesanan;
import com.radja.service.ServicePesanan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PesananDAO implements ServicePesanan {

    private final Connection conn;

    public PesananDAO() {
        conn = Koneksi.getConnection();
    }

    private String getOrCreatePelangganId(String telepon, String namaPelanggan, String alamat, Integer insertBy) {
        if (telepon == null || telepon.trim().isEmpty()) {
            throw new IllegalArgumentException("Nomor telepon tidak boleh kosong");
        }

        String idPelanggan = null;
        String checkSql = "SELECT id_pelanggan FROM pelanggan WHERE telepon = ? AND is_delete = FALSE LIMIT 1";
        String insertSql = "INSERT INTO pelanggan (id_pelanggan, nama_pelanggan, telepon, alamat, member_status, insert_by, insert_at) VALUES (?, ?, ?, ?, 'tidak aktif', ?, NOW())";

        try (PreparedStatement checkSt = conn.prepareStatement(checkSql)) {
            checkSt.setString(1, telepon);
            try (ResultSet rs = checkSt.executeQuery()) {
                if (rs.next()) {
                    idPelanggan = rs.getString("id_pelanggan");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memeriksa pelanggan dengan nomor telepon " + telepon + ": " + e.getMessage(), e);
        }

        if (idPelanggan == null) {
            idPelanggan = generateIdPelanggan();
            try (PreparedStatement insertSt = conn.prepareStatement(insertSql)) {
                insertSt.setString(1, idPelanggan);
                insertSt.setString(2, namaPelanggan != null && !namaPelanggan.trim().isEmpty() ? namaPelanggan : "Pelanggan");
                insertSt.setString(3, telepon);
                insertSt.setString(4, alamat != null && !alamat.trim().isEmpty() ? alamat : "");
                insertSt.setObject(5, insertBy, java.sql.Types.INTEGER);
                int rowsAffected = insertSt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Gagal membuat pelanggan baru untuk nomor telepon " + telepon);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Gagal membuat pelanggan baru untuk nomor telepon " + telepon + ": " + e.getMessage(), e);
            }
        }

        return idPelanggan;
    }

    /**
     * Generates a unique id_pelanggan in the format CRYYYYMMDDXXX.
     */
    private String generateIdPelanggan() {
        String urutan;
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String kodePrefix = "CR" + now.format(formatter);

        String sql = "SELECT id_pelanggan FROM pelanggan WHERE id_pelanggan LIKE ? ORDER BY id_pelanggan DESC LIMIT 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, kodePrefix + "%");
            try (ResultSet rs = st.executeQuery()) {
                int nomor;
                if (rs.next()) {
                    String lastId = rs.getString("id_pelanggan");
                    nomor = Integer.parseInt(lastId.substring(10)) + 1;
                } else {
                    nomor = 1;
                }
                urutan = kodePrefix + String.format("%03d", nomor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghasilkan ID pelanggan: " + e.getMessage(), e);
        }
        return urutan;
    }

    /**
     * Retrieves the harga of a layanan based on id_layanan.
     */
    private double getHargaLayanan(int idLayanan) {
        String sql = "SELECT harga FROM layanan WHERE id_layanan = ? AND is_delete = FALSE";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idLayanan);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("harga");
                } else {
                    throw new SQLException("Layanan dengan ID " + idLayanan + " tidak ditemukan atau telah dihapus");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil harga layanan untuk ID " + idLayanan + ": " + e.getMessage(), e);
        }
    }

    @Override
public void insertData(Pesanan model) {
    String sql = "INSERT INTO pesanan (id_pesanan, tanggal_pesanan, merk_mobil, nomor_plat, total_harga, status, id_pelanggan, id_layanan, no_antrian, insert_by, insert_at) VALUES (?,?,?,?,?,?,?,?,?,?,NOW())";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        String idPesanan = generateIdPesanan();
        model.setIdPesanan(idPesanan);
        Pelanggan pelanggan = model.getPelanggan();
        if (pelanggan == null) {
            throw new IllegalArgumentException("Data pelanggan tidak boleh kosong");
        }
        String idPelanggan = getOrCreatePelangganId(
            pelanggan.getTelepon(),
            pelanggan.getNamaPelanggan(),
            pelanggan.getAlamat(),
            model.getInsertBy()
        );
        model.setIdPelanggan(idPelanggan);
        double totalHarga = getHargaLayanan(model.getIdLayanan());

        st.setString(1, model.getIdPesanan());
        st.setDate(2, model.getTanggalPesanan() != null ? Date.valueOf(model.getTanggalPesanan()) : Date.valueOf(LocalDate.now()));
        st.setString(3, model.getMerkMobil() != null ? model.getMerkMobil() : "");
        st.setString(4, model.getNomorPlat() != null ? model.getNomorPlat() : "");
        st.setDouble(5, totalHarga);
        st.setString(6, model.getStatus() != null ? model.getStatus() : "Menunggu Antrian");
        st.setString(7, model.getIdPelanggan());
        st.setInt(8, model.getIdLayanan());
        Integer noAntrian = model.getNoAntrian();
        if (noAntrian != null && noAntrian != 0) {
            System.out.println("Using provided noAntrian: " + noAntrian + " for idPesanan: " + idPesanan);
            st.setInt(9, noAntrian);
        } else {
            String generatedNo = generateNoAntrian();
            System.out.println("Generated noAntrian: " + generatedNo + " for idPesanan: " + idPesanan);
            st.setObject(9, generatedNo != null ? Integer.parseInt(generatedNo) : null, java.sql.Types.INTEGER);
        }
        st.setObject(10, model.getInsertBy(), java.sql.Types.INTEGER);

        int rowsAffected = st.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Gagal menyimpan pesanan untuk ID " + model.getIdPesanan());
        }
    } catch (SQLException e) {
        throw new RuntimeException("Gagal menyimpan pesanan untuk ID " + model.getIdPesanan() + ": " + e.getMessage(), e);
    } catch (NumberFormatException e) {
        throw new RuntimeException("Gagal menyimpan pesanan untuk ID " + model.getIdPesanan() + ": Nomor antrian tidak valid", e);
    }
}

   @Override
public void updateData(Pesanan model) {
    String sql = "UPDATE pesanan SET status=?, no_antrian=?, update_by=?, update_at=NOW() WHERE id_pesanan=?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        // Set status, default ke "Menunggu Antrian" jika null
        st.setString(1, model.getStatus() != null ? model.getStatus() : "Menunggu Antrian");

        // Gunakan nomor antrian yang sudah ada jika tersedia
        Integer noAntrian = model.getNoAntrian();
        if (noAntrian != null && noAntrian != 0) {
            System.out.println("Using existing noAntrian: " + noAntrian + " for idPesanan: " + model.getIdPesanan());
            st.setInt(2, noAntrian);
        } else {
            // Generate nomor antrian hanya jika belum ada
            String generatedNo = generateNoAntrian();
            if (generatedNo != null) {
                try {
                    noAntrian = Integer.parseInt(generatedNo);
                    System.out.println("Generated new noAntrian: " + noAntrian + " for idPesanan: " + model.getIdPesanan());
                    st.setInt(2, noAntrian);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid generated noAntrian: " + generatedNo + " for idPesanan: " + model.getIdPesanan());
                    st.setNull(2, java.sql.Types.INTEGER);
                }
            } else {
                System.out.println("No new noAntrian generated for idPesanan: " + model.getIdPesanan());
                st.setNull(2, java.sql.Types.INTEGER);
            }
        }

        // Set update_by, boleh null jika tidak ada user yang login
        st.setObject(3, model.getUpdateBy(), java.sql.Types.INTEGER);

        // Set id_pesanan
        st.setString(4, model.getIdPesanan());

        // Eksekusi query
        int rowsAffected = st.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("Pesanan dengan ID " + model.getIdPesanan() + " tidak ditemukan");
        }
    } catch (SQLException e) {
        throw new RuntimeException("Gagal memperbarui pesanan untuk ID " + model.getIdPesanan() + ": " + e.getMessage(), e);
    }
}

    @Override
    public void deleteData(Pesanan model) {
        String sql = "UPDATE pesanan SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_pesanan=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setObject(1, model.getDeleteBy(), java.sql.Types.INTEGER);
            st.setString(2, model.getIdPesanan());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Pesanan dengan ID " + model.getIdPesanan() + " tidak ditemukan");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus pesanan untuk ID " + model.getIdPesanan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void restoreData(Pesanan model) {
        String checkQuery = "SELECT is_delete FROM pesanan WHERE id_pesanan = ?";
        String sql = "UPDATE pesanan SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_pesanan=?";
        try (PreparedStatement checkSt = conn.prepareStatement(checkQuery)) {
            checkSt.setString(1, model.getIdPesanan());
            try (ResultSet rs = checkSt.executeQuery()) {
                if (rs.next() && !rs.getBoolean("is_delete")) {
                    return; // Already restored
                }
            }
            try (PreparedStatement st = conn.prepareStatement(sql)) {
                st.setString(1, model.getIdPesanan());
                int rowsAffected = st.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("Pesanan dengan ID " + model.getIdPesanan() + " tidak ditemukan");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memulihkan pesanan untuk ID " + model.getIdPesanan() + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pesanan> getData(String filter, String status) {
        List<Pesanan> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, " +
            "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, " +
            "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
            "FROM pesanan psn " +
            "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
            "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan"
        );

        List<String> conditions = new ArrayList<>();
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            conditions.add("psn.is_delete = FALSE");
        } else if (filter.equals("Terhapus")) {
            conditions.add("psn.is_delete = TRUE");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("psn.status = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            if (status != null && !status.isEmpty()) {
                st.setString(1, status);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);

                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data pesanan: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Pesanan> searchData(String keyword, String filter, String status) {
        List<Pesanan> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, " +
            "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, " +
            "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
            "FROM pesanan psn " +
            "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
            "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan " +
            "WHERE (psn.id_pesanan LIKE ? OR psn.no_antrian LIKE ? OR psn.tanggal_pesanan LIKE ? " +
            "OR plg.nama_pelanggan LIKE ? OR psn.merk_mobil LIKE ? OR psn.nomor_plat LIKE ? " +
            "OR psn.status LIKE ? OR lyn.nama_layanan LIKE ?)"
        );

        List<String> conditions = new ArrayList<>();
        if (filter.equals("Aktif") || filter.equals("Filter")) {
            conditions.add("psn.is_delete = FALSE");
        } else if (filter.equals("Terhapus")) {
            conditions.add("psn.is_delete = TRUE");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("psn.status = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append(" AND ").append(String.join(" AND ", conditions));
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            for (int i = 1; i <= 8; i++) {
                st.setString(i, "%" + keyword + "%");
            }
            int paramIndex = 9;
            if (status != null && !status.isEmpty()) {
                st.setString(paramIndex, status);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari data pesanan: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public String generateIdPesanan() {
        String urutan;
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
        String kodePrefix = now.format(formatter);

        String sql = "SELECT id_pesanan FROM pesanan WHERE id_pesanan LIKE ? ORDER BY id_pesanan DESC LIMIT 1";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, kodePrefix + "%");
            try (ResultSet rs = st.executeQuery()) {
                int nomor;
                if (rs.next()) {
                    String lastId = rs.getString("id_pesanan");
                    nomor = Integer.parseInt(lastId.substring(4)) + 1;
                } else {
                    nomor = 1;
                }
                urutan = kodePrefix + String.format("%04d", nomor);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghasilkan ID pesanan: " + e.getMessage(), e);
        }
        return urutan;
    }

    @Override
public String generateNoAntrian() {
    String sql = "SELECT no_antrian FROM pesanan WHERE no_antrian IS NOT NULL AND tanggal_pesanan = ? ORDER BY no_antrian DESC LIMIT 1 FOR UPDATE";
    Integer nomorAntrian = null;
    try {
        conn.setAutoCommit(false); // Start transaction
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, Date.valueOf(LocalDate.now()));
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    nomorAntrian = rs.getObject("no_antrian", Integer.class);
                    if (nomorAntrian != null) {
                        nomorAntrian += 1; // Increment +1
                    }
                }
                if (nomorAntrian == null) {
                    nomorAntrian = 1;
                }
            }
        }
        conn.commit(); // Commit transaction
    } catch (SQLException e) {
        try {
            conn.rollback(); // Rollback on error
        } catch (SQLException rollbackEx) {
            throw new RuntimeException("Failed to rollback transaction: " + rollbackEx.getMessage(), rollbackEx);
        }
        throw new RuntimeException("Gagal menghasilkan nomor antrian: " + e.getMessage(), e);
    } finally {
        try {
            conn.setAutoCommit(true); // Restore auto-commit
        } catch (SQLException e) {
            throw new RuntimeException("Failed to restore auto-commit: " + e.getMessage(), e);
        }
    }
    return String.valueOf(nomorAntrian);
}

    @Override
    public void updateStok(String idPesanan) {
        String sql = "UPDATE barang brg " +
                     "JOIN (SELECT fst_det.id_barang, SUM(fst_det.jumlah) AS total_dipakai " +
                     "FROM fasilitas_detail fst_det " +
                     "JOIN layanan_detail lyn_det ON lyn_det.id_fasilitas = fst_det.id_fasilitas " +
                     "JOIN layanan lyn ON lyn.id_layanan = lyn_det.id_layanan " +
                     "JOIN pesanan psn ON psn.id_layanan = lyn.id_layanan " +
                     "WHERE psn.id_pesanan = ? AND psn.is_delete = FALSE " +
                     "GROUP BY fst_det.id_barang) AS usage_data ON brg.id_barang = usage_data.id_barang " +
                     "SET brg.stok = brg.stok - usage_data.total_dipakai";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, idPesanan);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Tidak ada stok yang diperbarui untuk pesanan ID " + idPesanan);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memperbarui stok barang untuk ID pesanan " + idPesanan + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<Pesanan> getDataReport(String filter, String status, java.util.Date startDate, java.util.Date endDate) {
        List<Pesanan> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, " +
            "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, " +
            "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
            "FROM pesanan psn " +
            "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
            "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan " +
            "WHERE psn.tanggal_pesanan BETWEEN ? AND ?"
        );

        List<String> conditions = new ArrayList<>();
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            conditions.add("psn.is_delete = FALSE");
        } else if (filter.equals("Terhapus")) {
            conditions.add("psn.is_delete = TRUE");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("psn.status = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append(" AND ").append(String.join(" AND ", conditions));
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            st.setDate(1, startDate != null ? new java.sql.Date(startDate.getTime()) : null);
            st.setDate(2, endDate != null ? new java.sql.Date(endDate.getTime()) : null);
            int paramIndex = 3;
            if (status != null && !status.isEmpty()) {
                st.setString(paramIndex, status);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);

                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data laporan pesanan untuk rentang tanggal: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Pesanan> searchDataReport(String keyword, String filter, String status, java.util.Date startDate, java.util.Date endDate) {
        List<Pesanan> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, " +
            "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, " +
            "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
            "FROM pesanan psn " +
            "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
            "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan " +
            "WHERE psn.tanggal_pesanan BETWEEN ? AND ? AND (psn.id_pesanan LIKE ? " +
            "OR psn.no_antrian LIKE ? OR psn.tanggal_pesanan LIKE ? " +
            "OR plg.nama_pelanggan LIKE ? OR psn.merk_mobil LIKE ? OR psn.nomor_plat LIKE ? " +
            "OR psn.status LIKE ? OR lyn.nama_layanan LIKE ?)"
        );

        List<String> conditions = new ArrayList<>();
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            conditions.add("psn.is_delete = FALSE");
        } else if (filter.equals("Terhapus")) {
            conditions.add("psn.is_delete = TRUE");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("psn.status = ?");
        }

        if (!conditions.isEmpty()) {
            sql.append(" AND ").append(String.join(" AND ", conditions));
        }

        try (PreparedStatement st = conn.prepareStatement(sql.toString())) {
            st.setDate(1, startDate != null ? new java.sql.Date(startDate.getTime()) : null);
            st.setDate(2, endDate != null ? new java.sql.Date(endDate.getTime()) : null);
            for (int i = 3; i <= 10; i++) {
                st.setString(i, "%" + keyword + "%");
            }
            int paramIndex = 11;
            if (status != null && !status.isEmpty()) {
                st.setString(paramIndex, status);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);

                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari data laporan pesanan untuk keyword '" + keyword + "': " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Pesanan> getDataById(String idPesanan) {
        List<Pesanan> list = new ArrayList<>();
        String sql = "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, plg.telepon, " +
                     "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, lyn.harga, " +
                     "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
                     "FROM pesanan psn " +
                     "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
                     "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan " +
                     "WHERE psn.id_pesanan = ?";

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, idPesanan);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    modelPelanggan.setTelepon(rs.getString("telepon"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));
                    modelLayanan.setHarga(rs.getDouble("harga"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);

                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data pesanan untuk ID " + idPesanan + ": " + e.getMessage(), e);
        }
        return list;
    }


    public List<Pesanan> getDataReport(String filter, String status, LocalDate startDate, LocalDate endDate) {
        List<Pesanan> list = new ArrayList<>();
        String sql = "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, " +
                     "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, " +
                     "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
                     "FROM pesanan psn " +
                     "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
                     "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan " +
                     "WHERE psn.tanggal_pesanan BETWEEN ? AND ?";

        List<String> conditions = new ArrayList<>();
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            conditions.add("psn.is_delete = FALSE");
        } else if (filter.equals("Terhapus")) {
            conditions.add("psn.is_delete = TRUE");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("psn.status = ?");
        }

        if (!conditions.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditions);
        }

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, startDate != null ? Date.valueOf(startDate) : null);
            st.setDate(2, endDate != null ? Date.valueOf(endDate) : null);
            int paramIndex = 3;
            if (status != null && !status.isEmpty()) {
                st.setString(paramIndex, status);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);

                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data laporan pesanan: " + e.getMessage(), e);
        }
        return list;
    }

    
    public List<Pesanan> searchDataReport(String keyword, String filter, String status, LocalDate startDate, LocalDate endDate) {
        List<Pesanan> list = new ArrayList<>();
        String sql = "SELECT psn.id_pesanan, psn.no_antrian, psn.tanggal_pesanan, plg.id_pelanggan, plg.nama_pelanggan, " +
                     "psn.merk_mobil, psn.nomor_plat, psn.total_harga, psn.status, lyn.id_layanan, lyn.nama_layanan, " +
                     "psn.insert_by, psn.update_by, psn.delete_by, psn.insert_at, psn.update_at, psn.delete_at, psn.is_delete " +
                     "FROM pesanan psn " +
                     "INNER JOIN pelanggan plg ON plg.id_pelanggan = psn.id_pelanggan " +
                     "INNER JOIN layanan lyn ON lyn.id_layanan = psn.id_layanan " +
                     "WHERE psn.tanggal_pesanan BETWEEN ? AND ? AND (psn.id_pesanan LIKE ? " +
                     "OR psn.no_antrian LIKE ? OR psn.tanggal_pesanan LIKE ? " +
                     "OR plg.nama_pelanggan LIKE ? OR psn.merk_mobil LIKE ? OR psn.nomor_plat LIKE ? " +
                     "OR psn.status LIKE ? OR lyn.nama_layanan LIKE ?)";

        List<String> conditions = new ArrayList<>();
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            conditions.add("psn.is_delete = FALSE");
        } else if (filter.equals("Terhapus")) {
            conditions.add("psn.is_delete = TRUE");
        }
        if (status != null && !status.isEmpty()) {
            conditions.add("psn.status = ?");
        }

        if (!conditions.isEmpty()) {
            sql += " AND " + String.join(" AND ", conditions);
        }

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setDate(1, startDate != null ? Date.valueOf(startDate) : null);
            st.setDate(2, endDate != null ? Date.valueOf(endDate) : null);
            for (int i = 3; i <= 10; i++) {
                st.setString(i, "%" + keyword + "%");
            }
            int paramIndex = 11;
            if (status != null && !status.isEmpty()) {
                st.setString(paramIndex, status);
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Pesanan model = new Pesanan();
                    model.setIdPesanan(rs.getString("id_pesanan"));
                    model.setNoAntrian(rs.getObject("no_antrian", Integer.class));
                    model.setTanggalPesanan(rs.getDate("tanggal_pesanan") != null ? rs.getDate("tanggal_pesanan").toLocalDate() : null);
                    model.setMerkMobil(rs.getString("merk_mobil"));
                    model.setNomorPlat(rs.getString("nomor_plat"));
                    model.setTotalHarga(rs.getDouble("total_harga"));
                    model.setStatus(rs.getString("status"));
                    model.setIdPelanggan(rs.getString("id_pelanggan"));
                    model.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    model.setIdLayanan(rs.getInt("id_layanan"));
                    model.setInsertBy(rs.getObject("insert_by", Integer.class));
                    model.setUpdateBy(rs.getObject("update_by", Integer.class));
                    model.setDeleteBy(rs.getObject("delete_by", Integer.class));
                    model.setInsertAt(rs.getTimestamp("insert_at") != null ? rs.getTimestamp("insert_at").toLocalDateTime() : null);
                    model.setUpdateAt(rs.getTimestamp("update_at") != null ? rs.getTimestamp("update_at").toLocalDateTime() : null);
                    model.setDeleteAt(rs.getTimestamp("delete_at") != null ? rs.getTimestamp("delete_at").toLocalDateTime() : null);
                    model.setIsDelete(rs.getBoolean("is_delete"));

                    Pelanggan modelPelanggan = new Pelanggan();
                    modelPelanggan.setIdPelanggan(rs.getString("id_pelanggan"));
                    modelPelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));

                    Layanan modelLayanan = new Layanan();
                    modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                    modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));

                    model.setPelanggan(modelPelanggan);
                    model.setLayanan(modelLayanan);

                    list.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari data laporan pesanan: " + e.getMessage(), e);
        }
        return list;
    }

    }