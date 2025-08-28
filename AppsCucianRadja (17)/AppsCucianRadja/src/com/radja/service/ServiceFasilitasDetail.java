package com.radja.service;

import com.radja.model.FasilitasDetail;
import java.util.List;

public interface ServiceFasilitasDetail {
    void insertData(FasilitasDetail model);
    void updateData(FasilitasDetail model);
    void deleteData(int idFasilitas);
    
    List<FasilitasDetail> getData(int idFasilitas);
    List<FasilitasDetail> searchData(int idFasilitas, String keyword);
}
