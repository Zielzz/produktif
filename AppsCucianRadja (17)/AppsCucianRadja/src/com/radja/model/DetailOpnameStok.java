package com.radja.model;

public class DetailOpnameStok {
    private int idDetailOpname;
    private int idOpname;
    private int idBarang;
    private String namaBarang; // Tambahkan field ini
    private int stokSistem;
    private int stokFisik;
    private int selisih;

    // Getters and Setters
    public int getIdDetailOpname() { return idDetailOpname; }
    public void setIdDetailOpname(int idDetailOpname) { this.idDetailOpname = idDetailOpname; }
    public int getIdOpname() { return idOpname; }
    public void setIdOpname(int idOpname) { this.idOpname = idOpname; }
    public int getIdBarang() { return idBarang; }
    public void setIdBarang(int idBarang) { this.idBarang = idBarang; }
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public int getStokSistem() { return stokSistem; }
    public void setStokSistem(int stokSistem) { this.stokSistem = stokSistem; }
    public int getStokFisik() { return stokFisik; }
    public void setStokFisik(int stokFisik) { this.stokFisik = stokFisik; }
    public int getSelisih() { return selisih; }
    public void setSelisih(int selisih) { this.selisih = selisih; }
}