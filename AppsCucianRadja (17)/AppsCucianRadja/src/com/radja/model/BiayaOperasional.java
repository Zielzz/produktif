package com.radja.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BiayaOperasional {
    private int idBiaya;
    private String deskripsi;
    private double jumlahBiaya;
    private LocalDate tanggalBiaya;
    private Integer insertBy;
    private LocalDateTime insertAt;
    private boolean isDelete;

    // Getter dan Setter untuk idBiaya
    public int getIdBiaya() {
        return idBiaya;
    }

    public void setIdBiaya(int idBiaya) {
        this.idBiaya = idBiaya;
    }

    // Getter dan Setter untuk deskripsi
    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    // Getter dan Setter untuk jumlahBiaya
    public double getJumlahBiaya() {
        return jumlahBiaya;
    }

    public void setJumlahBiaya(double jumlahBiaya) {
        this.jumlahBiaya = jumlahBiaya;
    }

    // Getter dan Setter untuk tanggalBiaya
    public LocalDate getTanggalBiaya() {
        return tanggalBiaya;
    }

    public void setTanggalBiaya(LocalDate tanggalBiaya) {
        this.tanggalBiaya = tanggalBiaya;
    }

    // Getter dan Setter untuk insertBy
    public Integer getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(Integer insertBy) {
        this.insertBy = insertBy;
    }

    // Getter dan Setter untuk insertAt
    public LocalDateTime getInsertAt() {
        return insertAt;
    }

    public void setInsertAt(LocalDateTime insertAt) {
        this.insertAt = insertAt;
    }

    // Getter dan Setter untuk isDelete
    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
}