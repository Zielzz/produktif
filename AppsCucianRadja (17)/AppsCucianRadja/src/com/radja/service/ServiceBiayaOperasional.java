package com.radja.service;

import com.radja.model.BiayaOperasional;
import java.util.List;

public interface ServiceBiayaOperasional {
    void insertData(BiayaOperasional model);
    void updateData(BiayaOperasional model);
    void deleteData(BiayaOperasional model);
    void restoreData(BiayaOperasional model);
    List<BiayaOperasional> getData(String filter);
    List<BiayaOperasional> searchData(String keyword, String filter);
}