package com.radja.service;

import com.radja.model.LayananDetail;
import java.util.List;

public interface ServiceLayananDetail {
    void insertData(LayananDetail model);
    void updateData(LayananDetail model);
    void deleteData(int idLayanan);
    
    List<LayananDetail> getData(int idLayanan);
    List<LayananDetail> searchData(int idLayanan, String keyword);
    
    List<LayananDetail> getDataReport(String filter);
    List<LayananDetail> searchDataReport(String keyword, String filter);
}
