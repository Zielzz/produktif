package com.radja.service;

import com.radja.model.Barang;
import java.util.List;

public interface ServiceBarang {
    void insertData(Barang model);
    void updateData(Barang model);
    void deleteData(Barang model);
    void restoreData(Barang model);
    List<Barang> getData(String filter);
    List<Barang> searchData(String keyword, String filter);
    List<Barang> getBarangStokKritis();
    void logBarangMasuk(Barang model, int idUser);
}