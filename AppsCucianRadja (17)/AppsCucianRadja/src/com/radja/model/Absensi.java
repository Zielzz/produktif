package com.radja.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Absensi {
    private int idAbsensi;
    private Karyawan karyawan;
    private LocalDate tanggal;
    private LocalDateTime waktuMasuk;
    private LocalDateTime waktuPulang;
    private Double durasi; // Maps to database durasi column (in hours)
    private String status;
    private String keterangan;
    private boolean isDelete;

    public Absensi() {
        this.tanggal = LocalDate.now();
        this.isDelete = false;
    }

    public int getIdAbsensi() {
        return idAbsensi;
    }

    public void setIdAbsensi(int idAbsensi) {
        this.idAbsensi = idAbsensi;
    }

    public Karyawan getKaryawan() {
        return karyawan;
    }

    public void setKaryawan(Karyawan karyawan) {
        this.karyawan = karyawan;
    }

    public LocalDate getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDate tanggal) {
        this.tanggal = tanggal;
    }

    public LocalDateTime getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(LocalDateTime waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public LocalDateTime getWaktuPulang() {
        return waktuPulang;
    }

    public void setWaktuPulang(LocalDateTime waktuPulang) {
        this.waktuPulang = waktuPulang;
    }

    public Double getDurasi() {
        return durasi;
    }

    public void setDurasi(Double durasi) {
        if (durasi != null && durasi < 0) {
            throw new IllegalArgumentException("Durasi cannot be negative: " + durasi);
        }
        this.durasi = durasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public String getFormattedDurasi() {
        if (durasi == null || durasi <= 0) {
            return "-";
        }
        long totalMinutes = (long) (durasi * 60);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("durasi %d jam dan %d menit", hours, minutes);
    }

    @Override
    public String toString() {
        return String.format("Absensi{id=%d, karyawan=%s, tanggal=%s, masuk=%s, pulang=%s, durasi=%s, status='%s', keterangan='%s', isDelete=%b}",
                idAbsensi,
                (karyawan != null ? karyawan.getNamaKaryawan() : "-"),
                (tanggal != null ? tanggal : "-"),
                (waktuMasuk != null ? waktuMasuk : "-"),
                (waktuPulang != null ? waktuPulang : "-"),
                getFormattedDurasi(),
                (status != null ? status : "-"),
                (keterangan != null ? keterangan : "-"),
                isDelete);
    }
}