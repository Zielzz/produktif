package com.radja.service.impl;

import com.radja.dao.UserDAO;
import com.radja.model.User;
import com.radja.model.Karyawan;
import com.radja.service.ServiceUser;
import java.time.LocalDateTime;
import java.util.List;

public class ServiceUserImpl implements ServiceUser {

    private UserDAO userDAO = new UserDAO();

    @Override
    public void insertData(User model) {
        userDAO.insertData(model);
    }

    @Override
    public void updateData(User model) {
        userDAO.updateData(model);
    }

    @Override
    public void deleteData(User model) {
        userDAO.deleteData(model);
    }

    @Override
    public void restoreData(User model) {
        userDAO.restoreData(model);
    }

    @Override
    public List<User> getData(String filter) {
        return userDAO.getData(filter);
    }

    @Override
    public List<User> searchData(String keyword, String filter) {
        return userDAO.searchData(keyword, filter);
    }

    @Override
    public boolean validateOldPassword(String username, String oldPassword) {
        return userDAO.validateOldPassword(username, oldPassword);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        return userDAO.changePassword(username, oldPassword, newPassword);
    }

    @Override
    public boolean validateUsername(User model) {
        return userDAO.validateUsername(model);
    }

    @Override
    public boolean processAttendance(int userId, String status) {
        return userDAO.processAttendance(userId, status);
    }

    @Override
    public User processLogin(User model) {
        return userDAO.processLogin(model);
    }

    @Override
    public User getUserById(int idUser) {
        return userDAO.getUserById(idUser);
    }

    @Override
    public User getUserByRFID(String rfid) {
        return userDAO.getUserByRFID(rfid);
    }

    @Override
    public Karyawan getKaryawanByRfid(String rfid) {
        return userDAO.getKaryawanByRfid(rfid);
    }

    @Override
    public boolean catatAbsensi(int idKaryawan, String status, LocalDateTime waktuMasuk, LocalDateTime waktuPulang, String keterangan) {
        return userDAO.catatAbsensi(idKaryawan, status, waktuMasuk, waktuPulang, keterangan);
    }

    @Override
    public String getPasswordFromDatabase(String username) {
        return userDAO.getPasswordFromDatabase(username);
    }

    @Override
    public String getPasswordByUsername(String username) {
        return getPasswordFromDatabase(username);
    }

    @Override
    public boolean validateRfid(User model) {
        return userDAO.validateRfid(model);
    }
}