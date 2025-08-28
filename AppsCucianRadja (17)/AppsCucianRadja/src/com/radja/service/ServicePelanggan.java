package com.radja.service;

import com.radja.model.Pelanggan;
import java.util.List;

public interface ServicePelanggan {
    void insertData(Pelanggan model);
    void updateData(Pelanggan model);
    void deleteData(Pelanggan model);
    void restoreData(Pelanggan model);
    void updateMemberStatus(Pelanggan model); 

    
    List<Pelanggan> getData(String filter);
    List<Pelanggan> searchData(String keyword, String filter);
    
    String generateIdPelanggan();
}
