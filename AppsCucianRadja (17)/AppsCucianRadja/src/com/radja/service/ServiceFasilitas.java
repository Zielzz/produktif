package com.radja.service;

import com.radja.model.Fasilitas;
import java.util.List;

public interface ServiceFasilitas {
    void insertData(Fasilitas model);
    void updateData(Fasilitas model);
    void deleteData(Fasilitas model);
    void restoreData(Fasilitas model);
    
    List<Fasilitas> getData(String filter);
    List<Fasilitas> searchData(String keyword, String filter);

    public boolean isNamaFasilitasExistForUpdate(String namaFasilitas, Integer idFasilitas);

    public boolean isNamaFasilitasExist(String namaFasilitas);
}