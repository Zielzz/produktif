package com.radja.tablemodel;

import com.radja.model.Pengeluaran;
import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TabModPengeluaran extends AbstractTableModel {
    private List<Pengeluaran> data = new ArrayList<>();
    private final String[] columns = {"No", "ID Pengeluaran", "Nama Item", "Jumlah", "Harga Total", 
                                     "Tipe Pengeluaran", "Tanggal", "Keterangan"};
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void setData(List<Pengeluaran> data) {
        this.data = (data != null) ? data : new ArrayList<>();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pengeluaran p = data.get(rowIndex);
        switch (columnIndex) {
            case 0: 
                return rowIndex + 1;
            case 1: 
                return p.getIdPengeluaran();
            case 2: 
                return p.getNamaItem() != null ? p.getNamaItem() : "";
            case 3: 
                return p.getJumlah();
            case 4: 
                return p.getHargaTotal();
            case 5: 
                return p.getTipePengeluaran() != null ? p.getTipePengeluaran() : "";
            case 6: 
                return p.getTanggalPengeluaran() != null 
                        ? p.getTanggalPengeluaran().format(dateFormatter) : "";
            case 7: 
                return p.getKeterangan() != null ? p.getKeterangan() : "";
            default: 
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: 
            case 1: 
            case 3: 
                return Integer.class;
            case 4: 
                return Double.class;
            case 2: 
            case 5: 
            case 6: 
            case 7: 
                return String.class;
            default: 
                return Object.class;
        }
    }

    public int getColumnIndex(String columnName) {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }
}