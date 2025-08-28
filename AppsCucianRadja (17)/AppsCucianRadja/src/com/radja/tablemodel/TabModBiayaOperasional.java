package com.radja.tablemodel;

import com.radja.model.BiayaOperasional;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModBiayaOperasional extends AbstractTableModel {
    private final List<BiayaOperasional> list = new ArrayList<>();
    private final String[] columns = {"No", "ID Biaya", "Deskripsi", "Jumlah Biaya", "Tanggal Biaya"};

    public void setData(List<BiayaOperasional> list) {
        this.list.clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    public BiayaOperasional getData(int row) {
        return list.get(row);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        BiayaOperasional biaya = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return biaya.getIdBiaya();
            case 2:
                return biaya.getDeskripsi();
            case 3:
                DecimalFormat df = new DecimalFormat("Rp#,##0.00");
                return df.format(biaya.getJumlahBiaya());
            case 4:
                return biaya.getTanggalBiaya() != null ? biaya.getTanggalBiaya().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
                return Integer.class;
            case 3:
                return String.class; // Jumlah biaya diformat sebagai string
            default:
                return String.class;
        }
    }
}