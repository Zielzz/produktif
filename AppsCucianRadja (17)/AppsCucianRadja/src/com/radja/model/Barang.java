package com.radja.model;

import java.time.LocalDateTime;

public class Barang {
    private int idBarang;
    private String namaBarang;
    private String satuan;
    private int stok;
    private double hargaSatuan;
    private Integer insertBy;
    private Integer updateBy;
    private Integer deleteBy;
    private LocalDateTime insertAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    private boolean isDelete;
    private Double isiSachet; // Menggunakan Double untuk mendukung null

    // Getter dan Setter untuk idBarang
    public int getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    // Getter dan Setter untuk namaBarang
    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    // Getter dan Setter untuk satuan
    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    // Getter dan Setter untuk stok
    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    // Getter dan Setter untuk hargaSatuan
    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(double hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    // Getter dan Setter untuk insertBy
    public Integer getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(Integer insertBy) {
        this.insertBy = insertBy;
    }

    // Getter dan Setter untuk updateBy
    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    // Getter dan Setter untuk deleteBy
    public Integer getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(Integer deleteBy) {
        this.deleteBy = deleteBy;
    }

    // Getter dan Setter untuk insertAt
    public LocalDateTime getInsertAt() {
        return insertAt;
    }

    public void setInsertAt(LocalDateTime insertAt) {
        this.insertAt = insertAt;
    }

    // Getter dan Setter untuk updateAt
    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    // Getter dan Setter untuk deleteAt
    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    // Getter dan Setter untuk isDelete
    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    // Getter dan Setter untuk isiSachet
    public Double getIsiSachet() {
        return isiSachet;
    }

    public void setIsiSachet(Double isiSachet) {
        this.isiSachet = isiSachet;
    }
}