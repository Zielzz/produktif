package com.radja.model;

import java.util.Date;

public class LabaRugi {
    private Date tanggal;
    private double pemasukan;
    private double pengeluaran;

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public double getPemasukan() {
        return pemasukan;
    }

    public void setPemasukan(double pemasukan) {
        this.pemasukan = pemasukan;
    }

    public double getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(double pengeluaran) {
        this.pengeluaran = pengeluaran;
    }

    public double getProfit() {
        return pemasukan - pengeluaran;
    }
}