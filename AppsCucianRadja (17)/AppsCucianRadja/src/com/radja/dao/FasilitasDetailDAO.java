package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Barang;
import com.radja.model.Fasilitas;
import com.radja.model.FasilitasDetail;
import com.radja.service.ServiceFasilitasDetail;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FasilitasDetailDAO implements ServiceFasilitasDetail{

    private final Connection conn;
    
    public FasilitasDetailDAO() {
        conn = Koneksi.getConnection();
    }

    @Override
    public void insertData(FasilitasDetail model) {
        PreparedStatement st = null;
        try {
            String sql = "INSERT INTO fasilitas_detail (id_fasilitas, id_barang, jumlah) VALUES (?,?,?)";
            
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getFasilitas().getIdFasilitas());
            st.setInt(2, model.getBarang().getIdBarang());
            st.setInt(3, model.getJumlah());
                        
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(FasilitasDetail model) {
        PreparedStatement st = null;
        try {
            String sql = "UPDATE fasilitas_detail SET id_fasilitas=?, id_barang=?, jumlah=?";
            st = conn.prepareStatement(sql);
            st.setInt(1, model.getFasilitas().getIdFasilitas());
            st.setInt(2, model.getBarang().getIdBarang());
            st.setInt(3, model.getJumlah());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void deleteData(int idFasilitas) {
        PreparedStatement st = null;
        String sql = "DELETE FROM fasilitas_detail WHERE id_fasilitas=?";
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, idFasilitas);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<FasilitasDetail> getData(int idFasilitas) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<FasilitasDetail> list = new ArrayList();
        String sql = "SELECT det.id_fasilitas, fst.nama_fasilitas, brg.nama_barang, brg.id_barang, brg.satuan, brg.stok, det.jumlah\n" +
                    "FROM fasilitas_detail det\n" +
                    "INNER JOIN fasilitas fst ON fst.id_fasilitas = det.id_fasilitas\n" +
                    "INNER JOIN barang brg ON brg.id_barang = det.id_barang\n" +
                    "WHERE det.id_fasilitas=?";
    
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, idFasilitas);
            rs = st.executeQuery();
            while(rs.next()){
                FasilitasDetail model = new FasilitasDetail();
                Fasilitas modelFasilitas = new Fasilitas();
                modelFasilitas.setIdFasilitas(rs.getInt("id_fasilitas"));
                modelFasilitas.setNamaFasilitas(rs.getString("nama_fasilitas"));
                
                Barang modelBarang = new Barang();
                modelBarang.setIdBarang(rs.getInt("id_barang"));
                modelBarang.setNamaBarang(rs.getString("nama_barang"));
                modelBarang.setSatuan(rs.getString("satuan"));
                modelBarang.setStok(rs.getInt("stok"));
                
                model.setJumlah(rs.getInt("jumlah"));
                
                model.setFasilitas(modelFasilitas);
                model.setBarang(modelBarang);
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<FasilitasDetail> searchData(int idFasilitas, String keyword) {
        PreparedStatement st = null;
        ResultSet rs = null;
        List<FasilitasDetail> list = new ArrayList();
        String sql = "SELECT det.id_fasilitas, fst.nama_fasilitas, brg.nama_barang, brg.satuan, brg.stok, det.jumlah\n" +
                    "FROM fasilitas_detail det\n" +
                    "INNER JOIN fasilitas fst ON fst.id_fasilitas = det.id_fasilitas\n" +
                    "INNER JOIN barang brg ON brg.id_barang = det.id_barang\n" +
                    "WHERE det.id_fasilitas=? AND "
                + "(det.id_fasilitas LIKE ? OR "
                + "fst.nama_fasilitas LIKE ? OR "
                + "brg.nama_barang LIKE ? OR "
                + "brg.satuan LIKE ? OR "
                + "brg.stok LIKE ? OR "
                + "det.jumlah LIKE ?)";
    
        try {
            st = conn.prepareStatement(sql);
            st.setInt(1, idFasilitas);
            st.setString(2, "%" + keyword + "%");
            st.setString(3, "%" + keyword + "%");
            st.setString(4, "%" + keyword + "%");
            st.setString(5, "%" + keyword + "%");
            st.setString(6, "%" + keyword + "%");
            st.setString(7, "%" + keyword + "%");
            rs = st.executeQuery();
            while(rs.next()){
                FasilitasDetail model = new FasilitasDetail();
                Fasilitas modelFasilitas = new Fasilitas();
                modelFasilitas.setIdFasilitas(rs.getInt("id_fasilitas"));
                modelFasilitas.setNamaFasilitas(rs.getString("nama_fasilitas"));
                
                Barang modelBarang = new Barang();
                modelBarang.setNamaBarang(rs.getString("nama_barang"));
                modelBarang.setSatuan(rs.getString("satuan"));
                modelBarang.setStok(rs.getInt("stok"));
                
                model.setJumlah(rs.getInt("jumlah"));
                
                model.setFasilitas(modelFasilitas);
                model.setBarang(modelBarang);
                
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
