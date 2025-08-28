package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.LabaRugi;
import com.radja.service.ServiceLabaRugi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LabaRugiDAO implements ServiceLabaRugi {
    private final Connection conn;

    public LabaRugiDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public List<LabaRugi> getLabaRugiData(Date startDate, Date endDate) {
        List<LabaRugi> list = new ArrayList<>();
        String query = "SELECT p.tanggal_pesanan as tanggal, p.total_harga as pemasukan, " +
                "(SELECT SUM(harga_total) FROM (" +
                "  SELECT jumlah_biaya AS harga_total, tanggal_biaya AS tanggal " +
                "  FROM biaya_operasional " +
                "  WHERE tanggal_biaya BETWEEN ? AND ? " +
                "  UNION ALL " +
                "  SELECT (stok * harga_satuan) AS harga_total, DATE(tanggal_masuk) AS tanggal " +
                "  FROM log_barang_masuk " +
                "  WHERE tanggal_masuk BETWEEN ? AND ? " +
                ") AS pengeluaran WHERE tanggal BETWEEN ? AND ?) as pengeluaran_total " +
                "FROM pesanan p WHERE p.status = 'Selesai' AND p.tanggal_pesanan BETWEEN ? AND ? AND p.is_delete = 0";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            ps.setDate(5, new java.sql.Date(startDate.getTime()));
            ps.setDate(6, new java.sql.Date(endDate.getTime()));
            ps.setDate(7, new java.sql.Date(startDate.getTime()));
            ps.setDate(8, new java.sql.Date(endDate.getTime()));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LabaRugi lr = new LabaRugi();
                lr.setTanggal(rs.getDate("tanggal"));
                lr.setPemasukan(rs.getDouble("pemasukan"));
                double pengeluaranTotal = rs.getDouble("pengeluaran_total") != 0.0 ? rs.getDouble("pengeluaran_total") : 0.0;
                lr.setPengeluaran(pengeluaranTotal);
                list.add(lr);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mengambil data laba rugi: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<LabaRugi> searchLabaRugiData(String keyword, Date startDate, Date endDate) {
        List<LabaRugi> list = new ArrayList<>();
        String query = "SELECT p.tanggal_pesanan as tanggal, p.total_harga as pemasukan, " +
                "(SELECT SUM(harga_total) FROM (" +
                "  SELECT jumlah_biaya AS harga_total, tanggal_biaya AS tanggal " +
                "  FROM biaya_operasional " +
                "  WHERE tanggal_biaya BETWEEN ? AND ? AND deskripsi LIKE ? " +
                "  UNION ALL " +
                "  SELECT (stok * harga_satuan) AS harga_total, DATE(tanggal_masuk) AS tanggal " +
                "  FROM log_barang_masuk " +
                "  WHERE tanggal_masuk BETWEEN ? AND ? AND nama_barang LIKE ? " +
                ") AS pengeluaran WHERE tanggal BETWEEN ? AND ?) as pengeluaran_total " +
                "FROM pesanan p WHERE p.status = 'Selesai' AND p.tanggal_pesanan BETWEEN ? AND ? AND p.no_antrian LIKE ? AND p.is_delete = 0";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ps.setString(3, searchPattern);
            ps.setDate(4, new java.sql.Date(startDate.getTime()));
            ps.setDate(5, new java.sql.Date(endDate.getTime()));
            ps.setString(6, searchPattern);
            ps.setDate(7, new java.sql.Date(startDate.getTime()));
            ps.setDate(8, new java.sql.Date(endDate.getTime()));
            ps.setDate(9, new java.sql.Date(startDate.getTime()));
            ps.setDate(10, new java.sql.Date(endDate.getTime()));
            ps.setString(11, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                LabaRugi lr = new LabaRugi();
                lr.setTanggal(rs.getDate("tanggal"));
                lr.setPemasukan(rs.getDouble("pemasukan"));
                double pengeluaranTotal = rs.getDouble("pengeluaran_total") != 0.0 ? rs.getDouble("pengeluaran_total") : 0.0;
                lr.setPengeluaran(pengeluaranTotal);
                list.add(lr);
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal mencari data laba rugi: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public double getTotalPemasukan(Date startDate, Date endDate) {
        double total = 0;
        String query = "SELECT COALESCE(SUM(total_harga), 0) as total FROM pesanan " +
                "WHERE status = 'Selesai' AND tanggal_pesanan BETWEEN ? AND ? AND is_delete = 0";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghitung total pemasukan: " + e.getMessage(), e);
        }
        return total;
    }

    @Override
    public double getTotalPengeluaran(Date startDate, Date endDate) {
        double total = 0;
        String query = "SELECT COALESCE(SUM(harga_total), 0) as total FROM (" +
                "SELECT jumlah_biaya AS harga_total FROM biaya_operasional " +
                "WHERE tanggal_biaya BETWEEN ? AND ? " +
                "UNION ALL " +
                "SELECT (stok * harga_satuan) AS harga_total FROM log_barang_masuk " +
                "WHERE tanggal_masuk BETWEEN ? AND ? " +
                ") AS total_pengeluaran";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDate(1, new java.sql.Date(startDate.getTime()));
            ps.setDate(2, new java.sql.Date(endDate.getTime()));
            ps.setDate(3, new java.sql.Date(startDate.getTime()));
            ps.setDate(4, new java.sql.Date(endDate.getTime()));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException("Gagal menghitung total pengeluaran: " + e.getMessage(), e);
        }
        return total;
    }

    @Override
    public void printLabaRugi(Date startDate, Date endDate) {
    }

    @Override
    public void saveLabaRugiPDF(Date startDate, Date endDate) {
    }

    @Override
    public void saveLabaRugiExcel(Date startDate, Date endDate) {
    }
}