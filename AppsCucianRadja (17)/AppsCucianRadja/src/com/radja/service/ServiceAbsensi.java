package com.radja.service;

import com.radja.model.Absensi;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface ServiceAbsensi {
    // Metode yang sudah ada tetap tidak diubah
    List<Absensi> getAbsensiByRfid(String rfid);
    List<Absensi> searchDataByRfid(String keyword, String rfid);
    List<Absensi> getData();
    List<Absensi> searchData(String keyword);
    List<Absensi> getRiwayatKaryawan(int idKaryawan, LocalDate start, LocalDate end);
    boolean checkIn(int idKaryawan);
    boolean checkOut(int idKaryawan);
    boolean catatIzinSakit(int idKaryawan, String jenis, String keterangan);
    boolean catatAbsensi(int userId, String status, LocalDateTime waktuAbsensi, String keterangan);
    List<Absensi> getAllAbsensi();
    List<Absensi> searchAbsensi(String keyword);
    List<Absensi> getAbsensiByDate(Date startDate, Date endDate);
    List<Absensi> getAbsensiByRfidAndDate(String rfid, Date startDate, Date endDate);
    List<Absensi> getAbsensiByDateRange(Date startDate, Date endDate);
    List<Absensi> searchAbsensi(String keyword, Date startDate, Date endDate);
    List<Absensi> getRiwayatAbsensi(int idKaryawan, long time, long time0);
    List<Absensi> searchAbsensiForKasir(String keyword, int idKaryawan, long l, long l0);
    List<Absensi> getAbsensiByKasir(int idKaryawan);
    List<Absensi> searchAbsensiByKaryawan(int idKaryawan, String keyword, LocalDate minusMonths, LocalDate now);
    boolean isSudahAbsen(int idKaryawan, LocalDate now, String hadir);
    boolean isSudahAbsenPulang(int idKaryawan, LocalDate now);
    boolean isSudahIzinSakit(int idKaryawan, LocalDate now);
    boolean isSudahAbsenMasuk(int idKaryawan, LocalDate now);
    boolean checkOut(int idKaryawan, LocalDateTime waktuAbsensi, String keterangan);
    boolean checkIn(int idKaryawan, LocalDateTime waktuAbsensi, String keterangan);

    // Metode baru untuk mendukung laporan absensi dengan filter
    List<Absensi> getAbsensiForUser(String role, String rfid, String keyword, Date startDate, Date endDate);
}