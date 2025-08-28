package com.radja.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Pengeluaran {
    private int idPengeluaran;
    private Integer idBarang; 
    private String namaItem; 
    private int jumlah;
    private double hargaTotal;
    private String tipePengeluaran;
    private LocalDate tanggalPengeluaran;
    private String keterangan;
    private Integer insertBy;
    private LocalDateTime insertAt;
    private boolean isDelete;

    // No-arg constructor
    public Pengeluaran() {
    }

    // Getters
    public int getIdPengeluaran() {
        return idPengeluaran;
    }

    public Integer getIdBarang() {
        return idBarang;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getHargaTotal() {
        return hargaTotal;
    }

    public String getTipePengeluaran() {
        return tipePengeluaran;
    }

    public LocalDate getTanggalPengeluaran() {
        return tanggalPengeluaran;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public Integer getInsertBy() {
        return insertBy;
    }

    public LocalDateTime getInsertAt() {
        return insertAt;
    }

    public boolean isDelete() {
        return isDelete;
    }

    // Setters
    public void setIdPengeluaran(int idPengeluaran) {
        this.idPengeluaran = idPengeluaran;
    }

    public void setIdBarang(Integer idBarang) {
        this.idBarang = idBarang;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public void setHargaTotal(double hargaTotal) {
        this.hargaTotal = hargaTotal;
    }

    public void setTipePengeluaran(String tipePengeluaran) {
        this.tipePengeluaran = tipePengeluaran;
    }

    public void setTanggalPengeluaran(LocalDate tanggalPengeluaran) {
        this.tanggalPengeluaran = tanggalPengeluaran;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public void setInsertBy(Integer insertBy) {
        this.insertBy = insertBy;
    }

    public void setInsertAt(LocalDateTime insertAt) {
        this.insertAt = insertAt;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
}