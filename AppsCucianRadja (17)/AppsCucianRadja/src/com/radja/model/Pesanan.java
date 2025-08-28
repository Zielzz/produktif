package com.radja.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pesanan {
    private String idPesanan;
    private LocalDate tanggalPesanan;
    private String merkMobil;
    private String nomorPlat;
    private Double totalHarga;
    private String status;
    private String idPelanggan;
    private String namaPelanggan;
    private int idLayanan;
    private Pelanggan pelanggan;
    private Layanan layanan;
    private int noAntrian;
    private Integer insertBy;
    private Integer updateBy;
    private Integer deleteBy;
    private LocalDateTime insertAt;
    private LocalDateTime updateAt;
    private LocalDateTime deleteAt;
    private boolean isDelete;

    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan;
    }

    public LocalDate getTanggalPesanan() {
        return tanggalPesanan;
    }

    public void setTanggalPesanan(LocalDate tanggalPesanan) {
        this.tanggalPesanan = tanggalPesanan;
    }

    public String getMerkMobil() {
        return merkMobil;
    }

    public void setMerkMobil(String merkMobil) {
        this.merkMobil = merkMobil;
    }

    public String getNomorPlat() {
        return nomorPlat;
    }

    public void setNomorPlat(String nomorPlat) {
        this.nomorPlat = nomorPlat;
    }

    public Double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIdPelanggan() {
        return idPelanggan;
    }

    public void setIdPelanggan(String idPelanggan) {
        this.idPelanggan = idPelanggan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public int getIdLayanan() {
        return idLayanan;
    }

    public void setIdLayanan(int idLayanan) {
        this.idLayanan = idLayanan;
    }

    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public void setLayanan(Layanan layanan) {
        this.layanan = layanan;
    }

    public int getNoAntrian() {
        return noAntrian;
    }

    public void setNoAntrian(int noAntrian) {
        this.noAntrian = noAntrian;
    }

    public Integer getInsertBy() {
        return insertBy;
    }

    public void setInsertBy(Integer insertBy) {
        this.insertBy = insertBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDeleteBy() {
        return deleteBy;
    }

    public void setDeleteBy(Integer deleteBy) {
        this.deleteBy = deleteBy;
    }

    public LocalDateTime getInsertAt() {
        return insertAt;
    }

    public void setInsertAt(LocalDateTime insertAt) {
        this.insertAt = insertAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDateTime getDeleteAt() {
        return deleteAt;
    }

    public void setDeleteAt(LocalDateTime deleteAt) {
        this.deleteAt = deleteAt;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    // Backward compatibility for generateQRCodeBuktiTransaksi
    public String getTanggal() {
        return tanggalPesanan != null ? tanggalPesanan.toString() : null;
    }

    public void setTanggal(String tanggal) {
        this.tanggalPesanan = tanggal != null ? LocalDate.parse(tanggal) : null;
    }

    public String getMerk() {
        return merkMobil;
    }

    public void setMerk(String merk) {
        this.merkMobil = merk;
    }

    public String getNoPlat() {
        return nomorPlat;
    }

    public void setNoPlat(String noPlat) {
        this.nomorPlat = noPlat;
    }
    
    
}