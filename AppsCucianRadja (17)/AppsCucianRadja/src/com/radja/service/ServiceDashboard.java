package com.radja.service;

import java.util.Map;

public interface ServiceDashboard {
    int getJumlahTransaksi();
    int getPendapatanPerHari();
    int getPesananByStatusByProses();
    int getPesananByStatusBySelesai();
    
    Map<String, Double> getPendapatanPerBulan();
    Map<String, Integer> getJumlahTransaksiPerBulan();
}
