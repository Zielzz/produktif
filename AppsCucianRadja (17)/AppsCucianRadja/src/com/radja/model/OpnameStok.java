package com.radja.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OpnameStok {
    private int idOpname;
    private LocalDate tanggalOpname;
    private String keterangan;
    private Integer insertBy;
    private LocalDateTime insertAt;
    private boolean isDelete;

    // Getters and Setters
    public int getIdOpname() { return idOpname; }
    public void setIdOpname(int idOpname) { this.idOpname = idOpname; }
    public LocalDate getTanggalOpname() { return tanggalOpname; }
    public void setTanggalOpname(LocalDate tanggalOpname) { this.tanggalOpname = tanggalOpname; }
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    public Integer getInsertBy() { return insertBy; }
    public void setInsertBy(Integer insertBy) { this.insertBy = insertBy; }
    public LocalDateTime getInsertAt() { return insertAt; }
    public void setInsertAt(LocalDateTime insertAt) { this.insertAt = insertAt; }
    public boolean isDelete() { return isDelete; }
    public void setIsDelete(boolean isDelete) { this.isDelete = isDelete; }
}