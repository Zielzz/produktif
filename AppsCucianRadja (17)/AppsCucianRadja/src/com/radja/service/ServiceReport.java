package com.radja.service;

import com.radja.model.BiayaOperasional;
import com.radja.model.Pemasukan;
import com.radja.model.Pesanan;
import com.radja.model.Pengeluaran;
import java.util.Date;
import java.util.List;

public interface ServiceReport {

    // Laporan Barang
    void printBarang();
    void saveBarangPDF();
    void saveBarangExcel();

    // Laporan Layanan
    void printLayanan();
    void saveLayananPDF();
    void saveLayananExcel();

    // Laporan Pelanggan
    void printPelanggan();
    void savePelangganPDF();
    void savePelangganExcel();

    // Laporan Karyawan
    void printKaryawan();
    void saveKaryawanPDF();
    void saveKaryawanExcel();

    // Laporan Pesanan Berdasarkan Periode
    void printPesananPeriode(Date startDate, Date endDate);
    void savePesananPeriodePDF(Date startDate, Date endDate);
    void savePesananPeriodeExcel(Date startDate, Date endDate);

    // Laporan Pengeluaran (Gabungan dari biaya_operasional dan pengeluaran_barang)
    void printPengeluaran();
    void savePengeluaranPDF();
    void savePengeluaranExcel();
    void printPengeluaranPeriode(Date startDate, Date endDate);
    void savePengeluaranPeriodePDF(Date startDate, Date endDate);
    void savePengeluaranPeriodeExcel(Date startDate, Date endDate);

    // Laporan Absensi
    void printAbsensi();
    void saveAbsensiPDF();
    void saveAbsensiExcel();

    // Bukti Transaksi
    void generateQRCodeBuktiTransaksi(List<Pesanan> list);

    // Data Pemasukan
    List<Pesanan> getPemasukanData(String filter);
    List<Pesanan> searchPemasukanData(String keyword, String filter);
    List<Pemasukan> getPemasukanList();
    List<Pemasukan> searchPemasukan(String keyword);
    void printPemasukan();
    void savePemasukanPDF();
    void savePemasukanExcel();

    // Data Pengeluaran
    List<Pengeluaran> getPengeluaranData(String filter);
    List<Pengeluaran> searchPengeluaranData(String keyword, String filter);
    List<Pengeluaran> getPengeluaranDataByDateRange(Date startDate, Date endDate, String filter);
    List<Pengeluaran> searchPengeluaranDataByDateRange(String keyword, Date startDate, Date endDate, String filter);
    List<BiayaOperasional> getBiayaOperasionalData(String filter);
    List<BiayaOperasional> searchBiayaOperasionalData(String keyword, String filter);
}