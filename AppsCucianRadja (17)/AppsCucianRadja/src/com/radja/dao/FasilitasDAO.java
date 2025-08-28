package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Fasilitas;
import com.radja.service.ServiceFasilitas;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class FasilitasDAO implements ServiceFasilitas{

    private final Connection conn;
    
    public FasilitasDAO() {
        conn = Koneksi.getConnection();
    }

   @Override
public void insertData(Fasilitas model) {
    PreparedStatement st = null;
    ResultSet rs = null;
    
    try {
        // Cek apakah nama fasilitas sudah ada
        String checkQuery = "SELECT COUNT(*) FROM fasilitas WHERE nama_fasilitas = ? AND is_delete = FALSE";
        st = conn.prepareStatement(checkQuery);
        st.setString(1, model.getNamaFasilitas());
        rs = st.executeQuery();
        
        if (rs.next() && rs.getInt(1) > 0) {
            throw new SQLException("Nama fasilitas sudah ada.");
        }
        
        // Jika nama fasilitas belum ada, lanjutkan proses insert
        String sql = "INSERT INTO fasilitas (nama_fasilitas, insert_by) VALUES (?,?)";
        st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        st.setString(1, model.getNamaFasilitas());
        st.setInt(2, model.getInsertBy());
        st.executeUpdate();
        
        try (ResultSet generatedKeys = st.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                model.setIdFasilitas(generatedKeys.getInt(1));
            }
        }
        
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
public void updateData(Fasilitas model) {
    PreparedStatement st = null;
    ResultSet rs = null;
    
    try {
        // Cek apakah nama fasilitas sudah ada (kecuali yang sedang diupdate)
        String checkQuery = "SELECT COUNT(*) FROM fasilitas WHERE nama_fasilitas = ? AND is_delete = FALSE AND id_fasilitas != ?";
        st = conn.prepareStatement(checkQuery);
        st.setString(1, model.getNamaFasilitas());
        st.setInt(2, model.getIdFasilitas());
        rs = st.executeQuery();
        
        if (rs.next() && rs.getInt(1) > 0) {
            throw new SQLException("Nama fasilitas sudah ada.");
        }
        
        // Jika nama fasilitas belum ada, lanjutkan proses update
        String sql = "UPDATE fasilitas SET nama_fasilitas=?, update_by=?, update_at=NOW() WHERE id_fasilitas=?";
        st = conn.prepareStatement(sql);
        st.setString(1, model.getNamaFasilitas());
        st.setInt(2, model.getUpdateBy());
        st.setInt(3, model.getIdFasilitas());
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
    public void deleteData(Fasilitas model) {
        PreparedStatement st = null;
        String sql = "UPDATE fasilitas SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_fasilitas=?";
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getIdFasilitas());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void restoreData(Fasilitas model) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            String checkQuery = "SELECT is_delete FROM fasilitas WHERE id_fasilitas = ?";
            st = conn.prepareStatement(checkQuery);
            st.setInt(1, model.getIdFasilitas());
            rs = st.executeQuery();

            if (rs.next() && !rs.getBoolean("is_delete")) {
                return;
            }

            String sql = "UPDATE fasilitas SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_fasilitas=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getIdFasilitas());
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
    public List<Fasilitas> getData(String filter) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Fasilitas> list = new ArrayList();
        String sql = "SELECT id_fasilitas, nama_fasilitas, is_delete FROM fasilitas";
    
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " WHERE is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " WHERE is_delete = TRUE";
        }
        
        try {
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                Fasilitas model = new Fasilitas();
                model.setIdFasilitas(rs.getInt("id_fasilitas"));
                model.setNamaFasilitas(rs.getString("nama_fasilitas"));
                model.setIsDelete(rs.getBoolean("is_delete"));
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Fasilitas> searchData(String keyword, String filter) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Fasilitas> list = new ArrayList();
        String sql ="SELECT id_fasilitas, nama_fasilitas, is_delete FROM fasilitas "
                + "WHERE (id_fasilitas LIKE ? "
                + "OR nama_fasilitas LIKE ?)";
        
        if (filter.equals("Filter") || filter.equals("Aktif")) {
            sql += " AND is_delete = FALSE";
        } else if (filter.equals("Terhapus")) {
            sql += " AND is_delete = TRUE";
        }
        
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, "%" + keyword + "%");
            st.setString(2, "%" + keyword + "%");
            rs = st.executeQuery();
            while(rs.next()){
                Fasilitas model = new Fasilitas();
                model.setIdFasilitas(rs.getInt("id_fasilitas"));
                model.setNamaFasilitas(rs.getString("nama_fasilitas"));
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

    @Override
public boolean isNamaFasilitasExist(String namaFasilitas) {
    boolean exists = false;
    String sql = "SELECT COUNT(*) FROM fasilitas WHERE nama_fasilitas = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, namaFasilitas);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            exists = rs.getInt(1) > 0;
        }
        rs.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return exists;
}

@Override
public boolean isNamaFasilitasExistForUpdate(String namaFasilitas, Integer idFasilitas) {
    boolean exists = false;
    String sql = "SELECT COUNT(*) FROM fasilitas WHERE nama_fasilitas = ? AND id_fasilitas != ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setString(1, namaFasilitas);
        st.setInt(2, idFasilitas);
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
            exists = rs.getInt(1) > 0;
        }
        rs.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return exists;
    }
}