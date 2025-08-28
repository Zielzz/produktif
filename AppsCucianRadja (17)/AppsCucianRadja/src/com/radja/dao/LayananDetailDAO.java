package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Fasilitas;
import com.radja.model.Layanan;
import com.radja.model.LayananDetail;
import com.radja.service.ServiceLayananDetail;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LayananDetailDAO implements ServiceLayananDetail{

    private final Connection conn;
    
    public LayananDetailDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(LayananDetail model) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO layanan_detail (id_layanan, id_fasilitas) VALUES (?,?)";
            
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getLayanan().getIdLayanan());
            st.setInt(2, model.getFasilitas().getIdFasilitas());
                        
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(LayananDetail model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE layanan_detail SET id_layanan=?, id_fasilitas=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getLayanan().getIdLayanan());
            st.setInt(2, model.getFasilitas().getIdFasilitas());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteData(int idLayanan) {
        PreparedStatement st = null;
        String sql = "DELETE FROM layanan_detail WHERE id_layanan=?";
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, idLayanan);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<LayananDetail> getData(int idLayanan) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<LayananDetail> list = new ArrayList();
        String sql = "SELECT det.id_layanan, lyn.nama_layanan, fst.id_fasilitas, fst.nama_fasilitas, lyn.harga\n" +
                    "FROM layanan_detail det\n" +
                    "INNER JOIN layanan lyn ON lyn.id_layanan = det.id_layanan\n" +
                    "INNER JOIN fasilitas fst ON fst.id_fasilitas = det.id_fasilitas\n" +
                    "WHERE det.id_layanan=?";
    
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, idLayanan);
            rs = st.executeQuery();
            while(rs.next()){
                LayananDetail model = new LayananDetail();
                
                Layanan modelLayanan = new Layanan();
                modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));
                
                Fasilitas modelFasilitas = new Fasilitas();
                modelFasilitas.setIdFasilitas(rs.getInt("id_fasilitas"));
                modelFasilitas.setNamaFasilitas(rs.getString("nama_fasilitas"));
                
                modelLayanan.setHarga(rs.getDouble("harga"));
                
                model.setLayanan(modelLayanan);
                model.setFasilitas(modelFasilitas);
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LayananDetail> searchData(int idLayanan, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<LayananDetail> list = new ArrayList();
        String sql = "SELECT det.id_layanan, lyn.nama_layanan, fst.id_fasilitas, fst.nama_fasilitas, lyn.harga\n" +
                    "FROM layanan_detail det\n" +
                    "INNER JOIN layanan lyn ON lyn.id_layanan = det.id_layanan\n" +
                    "INNER JOIN fasilitas fst ON fst.id_fasilitas = det.id_fasilitas\n" +
                    "WHERE det.id_layanan=? AND "
                + "(det.id_layanan LIKE ? OR "
                + "lyn.nama_layanan LIKE ? OR "
                + "fst.nama_fasilitas LIKE ? OR "
                + "lyn.harga LIKE ?)";
    
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, idLayanan);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            rs = st.executeQuery();
            while(rs.next()){
                LayananDetail model = new LayananDetail();
                
                Layanan modelLayanan = new Layanan();
                modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));
                
                Fasilitas modelFasilitas = new Fasilitas();
                modelFasilitas.setIdFasilitas(rs.getInt("id_fasilitas"));
                modelFasilitas.setNamaFasilitas(rs.getString("nama_fasilitas"));
                
                modelLayanan.setHarga(rs.getDouble("harga"));
                
                model.setLayanan(modelLayanan);
                model.setFasilitas(modelFasilitas);
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<LayananDetail> getDataReport(String filter) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<LayananDetail> list = new ArrayList();
        String sql = "SELECT det.id_layanan, lyn.nama_layanan, fst.id_fasilitas, fst.nama_fasilitas, lyn.harga, lyn.is_delete\n" +
                    "FROM layanan_detail det\n" +
                    "INNER JOIN layanan lyn ON lyn.id_layanan = det.id_layanan\n" +
                    "INNER JOIN fasilitas fst ON fst.id_fasilitas = det.id_fasilitas\n" +
                    "WHERE lyn.is_delete = ?";
    
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, filter);
            rs = st.executeQuery();
            while(rs.next()){
                LayananDetail model = new LayananDetail();
                
                Layanan modelLayanan = new Layanan();
                modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));
                
                Fasilitas modelFasilitas = new Fasilitas();
                modelFasilitas.setIdFasilitas(rs.getInt("id_fasilitas"));
                modelFasilitas.setNamaFasilitas(rs.getString("nama_fasilitas"));
                
                modelLayanan.setHarga(rs.getDouble("harga"));
                
                model.setLayanan(modelLayanan);
                model.setFasilitas(modelFasilitas);
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LayananDetail> searchDataReport(String keyword, String filter) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<LayananDetail> list = new ArrayList();
        String sql = "SELECT det.id_layanan, lyn.nama_layanan, fst.id_fasilitas, fst.nama_fasilitas, lyn.harga, lyn.is_delete\n" +
                    "FROM layanan_detail det\n" +
                    "INNER JOIN layanan lyn ON lyn.id_layanan = det.id_layanan\n" +
                    "INNER JOIN fasilitas fst ON fst.id_fasilitas = det.id_fasilitas\n" +
                    "WHERE lyn.is_delete = ? AND "
                + "(det.id_layanan LIKE ? OR "
                + "lyn.nama_layanan LIKE ? OR "
                + "fst.nama_fasilitas LIKE ? OR "
                + "lyn.harga LIKE ?)";
    
        try {
            st = conn.prepareStatement(sql);
            st.setString(1, filter);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            rs = st.executeQuery();
            while(rs.next()){
                LayananDetail model = new LayananDetail();
                
                Layanan modelLayanan = new Layanan();
                modelLayanan.setIdLayanan(rs.getInt("id_layanan"));
                modelLayanan.setNamaLayanan(rs.getString("nama_layanan"));
                
                Fasilitas modelFasilitas = new Fasilitas();
                modelFasilitas.setIdFasilitas(rs.getInt("id_fasilitas"));
                modelFasilitas.setNamaFasilitas(rs.getString("nama_fasilitas"));
                
                modelLayanan.setHarga(rs.getDouble("harga"));
                
                model.setLayanan(modelLayanan);
                model.setFasilitas(modelFasilitas);
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
