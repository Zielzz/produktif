package com.radja.service;

import com.radja.model.User;
import com.radja.model.Karyawan;
import java.time.LocalDateTime;
import java.util.List;

public interface ServiceUser {
    
    String getPasswordFromDatabase(String username);
    String getPasswordByUsername(String username);

    // User management methods
    void insertData(User model);
    void updateData(User model);
    void deleteData(User model);
    void restoreData(User model);

    List<User> getData(String filter);
    List<User> searchData(String keyword, String filter);

    boolean validateOldPassword(String username, String oldPassword);
    boolean changePassword(String username, String oldPassword, String newPassword);
    boolean validateUsername(User model);
    boolean validateRfid(User model); // Added to validate RFID uniqueness

    // Method for processing attendance with status
    boolean processAttendance(int userId, String status);
    
    // Methods for User login
    User processLogin(User model);
    User getUserById(int idUser);
    User getUserByRFID(String rfid);

    // Method to get Karyawan based on RFID
    Karyawan getKaryawanByRfid(String rfid);

    // Method to record attendance in the system
    boolean catatAbsensi(int idKaryawan, String status, LocalDateTime waktuMasuk, LocalDateTime waktuPulang, String keterangan); 
}