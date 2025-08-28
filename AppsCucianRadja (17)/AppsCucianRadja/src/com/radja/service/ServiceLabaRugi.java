package com.radja.service;

import com.radja.model.LabaRugi;
import java.util.Date;
import java.util.List;

public interface ServiceLabaRugi {
    List<LabaRugi> getLabaRugiData(Date startDate, Date endDate);
    List<LabaRugi> searchLabaRugiData(String keyword, Date startDate, Date endDate);
    double getTotalPemasukan(Date startDate, Date endDate);
    double getTotalPengeluaran(Date startDate, Date endDate);
    void printLabaRugi(Date startDate, Date endDate);
    void saveLabaRugiPDF(Date startDate, Date endDate);
    void saveLabaRugiExcel(Date startDate, Date endDate);
}