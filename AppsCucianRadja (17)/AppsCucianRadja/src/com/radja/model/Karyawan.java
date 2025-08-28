package com.radja.model;

public class Karyawan {
    private int idKaryawan;
    private String namaKaryawan;
    private String telepon;
    private String alamat;
    private String rfid; 
    private Integer insertBy;
    private Integer updateBy;
    private Integer deleteBy;
    private boolean isDelete;

    // Getters and Setters
    public int getIdKaryawan() {
        return idKaryawan;
    }

    public void setIdKaryawan(int idKaryawan) {
        this.idKaryawan = idKaryawan;
    }

    public String getNamaKaryawan() {
        return namaKaryawan;
    }

    public void setNamaKaryawan(String namaKaryawan) {
        this.namaKaryawan = namaKaryawan;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    
    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
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

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public String toString() {
        return "Karyawan [idKaryawan=" + idKaryawan + ", namaKaryawan=" + namaKaryawan + ", telepon=" + telepon + ", alamat=" + alamat + ", rfid=" + rfid + "]";
    }
}
