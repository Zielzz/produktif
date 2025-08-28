package com.radja.model;

public class Fasilitas {
    private int idFasilitas;
    private String namaFasilitas;
    private Integer insertBy;
    private Integer updateBy;
    private Integer deleteBy;
    private boolean isDelete;

    public int getIdFasilitas() {
        return idFasilitas;
    }

    public void setIdFasilitas(int idFasilitas) {
        this.idFasilitas = idFasilitas;
    }

    public String getNamaFasilitas() {
        return namaFasilitas;
    }

    public void setNamaFasilitas(String namaFasilitas) {
        this.namaFasilitas = namaFasilitas;
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
}
