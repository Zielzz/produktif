package com.radja.service;

import com.radja.model.Pesanan;
import java.util.Date;
import java.util.List;

public interface ServicePesanan {
    void insertData(Pesanan model);
    void updateData(Pesanan model);
    void deleteData(Pesanan model);
    void restoreData(Pesanan model);
    
    List<Pesanan> getData(String filter, String status);
    List<Pesanan> getDataById(String idPesanan);
    List<Pesanan> searchData(String keyword, String filter, String status);
    
    String generateIdPesanan();
    String generateNoAntrian();
    
    void updateStok(String idPesanan);
    
    List<Pesanan> getDataReport(String filter, String status, Date startDate, Date endDate);
    List<Pesanan> searchDataReport(String keyword, String filter, String status, Date startDate, Date endDate);
}
