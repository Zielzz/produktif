package com.radja.service;

import com.radja.model.Karyawan;
import java.util.List;

public interface ServiceKaryawan {
    // Basic CRUD Operations
    boolean insertData(Karyawan karyawan);           // Menginsert data karyawan
    boolean updateData(Karyawan karyawan);           // Mengupdate data karyawan
    boolean deleteData(int idKaryawan);              // Menghapus data berdasarkan ID
    boolean restoreData(int idKaryawan);             // Mengembalikan data berdasarkan ID
    
    // RFID Operations
    Karyawan getKaryawanByRFID(String rfid);         // Mendapatkan data karyawan berdasarkan RFID
    boolean updateRFID(int idKaryawan, String newRFID); // Update RFID berdasarkan ID karyawan
    
    // Attendance Operations
    boolean prosesAbsensi(int idKaryawan);           // Proses absensi karyawan
    
    
    // Data Retrieval
    List<Karyawan> getData(String filter);           // Mendapatkan data karyawan berdasarkan filter
    List<Karyawan> searchData(String keyword, String filter); // Mencari data karyawan berdasarkan keyword dan filter
}
