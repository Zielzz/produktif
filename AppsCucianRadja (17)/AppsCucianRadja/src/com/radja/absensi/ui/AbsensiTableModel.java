package com.radja.absensi.ui;

import com.radja.model.Absensi;
import com.radja.model.Karyawan;
import javax.swing.table.AbstractTableModel;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

public class AbsensiTableModel extends AbstractTableModel {
    private static final Logger LOGGER = Logger.getLogger(AbsensiTableModel.class.getName());
    private final List<Absensi> data;
    private final String[] columns = {
        "No", "Tanggal", "Nama Karyawan", "Masuk", "Pulang", "Durasi", "Status", "Keterangan"
    };
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public AbsensiTableModel() {
        LOGGER.log(Level.INFO, "Inisialisasi AbsensiTableModel tanpa data");
        this.data = new ArrayList<>();
    }

    public AbsensiTableModel(List<Absensi> data) {
        LOGGER.log(Level.INFO, "Inisialisasi AbsensiTableModel dengan data");
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        LOGGER.log(Level.FINE, "Mengembalikan jumlah baris");
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int col) {
        try {
            if (row < 0 || row >= data.size()) {
                LOGGER.log(Level.WARNING, "Baris tidak valid: " + row);
                return "-";
            }
            Absensi absensi = data.get(row);
            if (absensi == null) {
                LOGGER.log(Level.WARNING, "Objek Absensi null pada baris: " + row);
                return "-";
            }
            switch (col) {
                case 0:
                    return String.valueOf(row + 1);
                case 1:
                    return absensi.getTanggal() != null ? absensi.getTanggal().format(dateFormat) : "-";
                case 2:
                    Karyawan karyawan = absensi.getKaryawan();
                    return karyawan != null && karyawan.getNamaKaryawan() != null ? karyawan.getNamaKaryawan() : "-";
                case 3:
                    return absensi.getWaktuMasuk() != null ? absensi.getWaktuMasuk().format(timeFormat) : "-";
                case 4:
                    return absensi.getWaktuPulang() != null ? absensi.getWaktuPulang().format(timeFormat) : "-";
                case 5:
                    return absensi.getDurasi() != null ? String.format("%.2f jam", absensi.getDurasi()) : "-";
                case 6:
                    return absensi.getStatus() != null ? absensi.getStatus() : "-";
                case 7:
                    return absensi.getKeterangan() != null ? absensi.getKeterangan() : "-";
                default:
                    LOGGER.log(Level.WARNING, "Kolom tidak valid: " + col);
                    return "-";
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Gagal mengambil nilai untuk baris dan kolom", e);
            return "-";
        }
    }

    public Absensi getAbsensiAt(int row) {
        if (row >= 0 && row < data.size()) {
            return data.get(row);
        }
        LOGGER.log(Level.WARNING, "Gagal mengambil absensi pada baris: " + row);
        return null;
    }

    public void updateData(List<Absensi> newData) {
        LOGGER.log(Level.INFO, "Memperbarui data dengan entri baru");
        data.clear();
        data.addAll(newData != null ? newData : new ArrayList<>());
        fireTableDataChanged();
    }

    public void addAbsensi(Absensi absensi) {
        if (absensi != null) {
            data.add(0, absensi);
            LOGGER.log(Level.INFO, "Menambahkan absensi baru: ID " + absensi.getIdAbsensi());
            fireTableRowsInserted(0, 0);
        } else {
            LOGGER.log(Level.WARNING, "Gagal menambahkan absensi: objek null");
        }
    }

    public void setData(List<Absensi> newData) {
        updateData(newData);
    }

    public void clear() {
        LOGGER.log(Level.INFO, "Membersihkan data AbsensiTableModel");
        data.clear();
        fireTableDataChanged();
    }
}