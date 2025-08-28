package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.service.ServiceDashboard;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

public class DashboardDAO implements ServiceDashboard{

    private final Connection conn;
    
    public DashboardDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public int getJumlahTransaksi() {
        int totalTransaksi = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) AS total FROM pesanan WHERE is_delete = FALSE";
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            if(rs.next()){
                totalTransaksi = rs.getInt("total");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return totalTransaksi;
    }

    @Override
    public int getPendapatanPerHari() {
        int totalPendapatanPerHari = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT \n" +
                    "    DATE(tanggal_pesanan) AS tanggal,\n" +
                    "    SUM(total_harga) AS total_pendapatan\n" +
                    "FROM pesanan\n" +
                    "WHERE STATUS = 'Selesai' AND is_delete=FALSE\n" +
                    "GROUP BY DATE(tanggal_pesanan)\n" +
                    "ORDER BY tanggal ASC;";
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            if(rs.next()){
                totalPendapatanPerHari = rs.getInt("total_pendapatan");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return totalPendapatanPerHari;
    }

    @Override
    public int getPesananByStatusByProses() {
        int pesananStatusProses = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT \n" +
                    "    DATE(tanggal_pesanan) AS tanggal,\n" +
                    "    COUNT(id_pesanan) AS jumlah_pesanan\n" +
                    "FROM pesanan\n" +
                    "WHERE STATUS != 'Selesai' AND is_delete=FALSE\n" +
                    "GROUP BY DATE(tanggal_pesanan)\n" +
                    "ORDER BY tanggal ASC;";
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                pesananStatusProses += rs.getInt("jumlah_pesanan");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return pesananStatusProses;
    }

    @Override
    public int getPesananByStatusBySelesai() {
        int pesananStatusSelesai = 0;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "SELECT \n" +
                    "    DATE(tanggal_pesanan) AS tanggal,\n" +
                    "    STATUS,\n" +
                    "    COUNT(id_pesanan) AS jumlah_pesanan\n" +
                    "FROM pesanan\n" +
                    "WHERE STATUS = 'Selesai' AND is_delete=FALSE\n" +
                    "GROUP BY DATE(tanggal_pesanan), STATUS\n" +
                    "ORDER BY tanggal ASC;";
        try{
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                pesananStatusSelesai += rs.getInt("jumlah_pesanan"); // Akumulasi jumlah pesanan
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return pesananStatusSelesai;
    }

    @Override
    public Map<String, Double> getPendapatanPerBulan() {
        Map<String, Double> data = new LinkedHashMap<>();
        String query = "SELECT DATE_FORMAT(tanggal_pesanan, '%Y-%m') AS bulan, SUM(total_harga) AS pendapatan \n" +
                "FROM pesanan WHERE is_delete=FALSE GROUP BY bulan ORDER BY bulan";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("bulan"), rs.getDouble("pendapatan"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    @Override
    public Map<String, Integer> getJumlahTransaksiPerBulan() {
        Map<String, Integer> data = new LinkedHashMap<>();
        String query = "SELECT DATE_FORMAT(tanggal_pesanan, '%Y-%m') AS bulan, COUNT(id_pesanan) AS jumlah \n" +
                    "FROM pesanan WHERE is_delete=FALSE GROUP BY bulan ORDER BY bulan";

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                data.put(rs.getString("bulan"), rs.getInt("jumlah"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
