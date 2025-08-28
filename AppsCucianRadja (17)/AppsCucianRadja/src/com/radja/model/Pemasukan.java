package com.radja.model;

public class Pemasukan {
    private String idPesanan;
    private String namaLayanan;
    private double harga;

    public Pemasukan(String idPesanan, String namaLayanan, double harga) {
        this.idPesanan = idPesanan;
        this.namaLayanan = namaLayanan;
        this.harga = harga;
    }

    public String getIdPesanan() { return idPesanan; }
    public String getNamaLayanan() { return namaLayanan; }
    public double getHarga() { return harga; }

    // Setter jika diperlukan
}


