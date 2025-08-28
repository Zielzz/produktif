package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Fasilitas;
import com.radja.model.Layanan;
import com.radja.model.LayananDetail;
import com.radja.service.ServiceLayanan;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LayananDAO implements ServiceLayanan{

    private final Connection conn;
    
    public LayananDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(Layanan model) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO layanan (nama_layanan, harga, insert_by) VALUES (?,?,?)";
            
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, model.getNamaLayanan());
            st.setDouble(2, model.getHarga());
            st.setInt   (3, model.getInsertBy());
                        
            st.executeUpdate();
            
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    model.setIdLayanan(rs.getInt(1));
                }
            }
            
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(Layanan model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE layanan SET nama_layanan=?, harga=?, update_by=?, update_at=NOW() WHERE id_layanan=?";
            
            st = conn.prepareStatement(sql);
            st.setString(1, model.getNamaLayanan());
            st.setDouble(2, model.getHarga());
            st.setInt   (3, model.getUpdateBy());
            st.setInt   (4, model.getIdLayanan());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(Layanan model) {
        PreparedStatement st = null;
        String sql = "UPDATE layanan SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_layanan=?";
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getIdLayanan());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void restoreData(Layanan model) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String checkQuery = "SELECT is_delete FROM layanan WHERE id_layanan = ?";
            st = conn.prepareStatement(checkQuery);
            st.setInt(1, model.getIdLayanan());
            rs = st.executeQuery();

            if (rs.next() && !rs.getBoolean("is_delete")) {
                return;
            }

            String sql = "UPDATE layanan SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_layanan=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdLayanan());
            st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    @Override
    public List<Layanan> getData(String filter) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Layanan> list = new ArrayList();
        String sql = "SELECT id_layanan, nama_layanan, harga, is_delete FROM layanan";
    
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " WHERE is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " WHERE is_delete = TRUE";
        }
        
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                Layanan model = new Layanan();
                model.setIdLayanan(rs.getInt("id_layanan"));
                model.setNamaLayanan(rs.getString("nama_layanan"));
                model.setHarga(rs.getDouble("harga"));
                model.setIsDelete(rs.getBoolean("is_delete"));
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Layanan> searchData(String keyword, String filter) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Layanan> list = new ArrayList();
        String sql ="SELECT id_layanan, nama_layanan, harga, is_delete FROM layanan "
                + "WHERE (id_layanan LIKE ? "
                + "OR nama_layanan LIKE ? "
                + "OR harga LIKE ?)";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " AND is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " AND is_delete = TRUE";
        }
        
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            rs = st.executeQuery();
            while(rs.next()){
                Layanan model = new Layanan();
                model.setIdLayanan(rs.getInt("id_layanan"));
                model.setNamaLayanan(rs.getString("nama_layanan"));
                model.setHarga(rs.getDouble("harga"));
                model.setIsDelete(rs.getBoolean("is_delete"));
                
                list.add(model);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
