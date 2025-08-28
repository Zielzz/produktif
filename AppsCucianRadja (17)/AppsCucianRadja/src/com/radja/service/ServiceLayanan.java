package com.radja.service;

import com.radja.model.Layanan;
import com.radja.model.LayananDetail;
import java.util.List;

public interface ServiceLayanan {
    void insertData(Layanan model);
    void updateData(Layanan model);
    void deleteData(Layanan model);
    void restoreData(Layanan model);
    
    List<Layanan> getData(String filter);
    List<Layanan> searchData(String keyword, String filter);
}
