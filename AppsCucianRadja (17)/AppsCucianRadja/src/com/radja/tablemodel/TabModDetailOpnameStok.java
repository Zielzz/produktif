package com.radja.tablemodel;

import com.radja.model.DetailOpnameStok;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TabModDetailOpnameStok extends AbstractTableModel {
    private final List<DetailOpnameStok> list = new ArrayList<>();
    private final String[] columnNames = {"No", "ID Barang", "Nama Barang", "Stok Sistem", "Stok Fisik", "Selisih"};

    public void setData(List<DetailOpnameStok> list) {
        this.list.clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }

    public List<DetailOpnameStok> getData() {
        return new ArrayList<>(list);
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        DetailOpnameStok detail = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return detail.getIdBarang();
            case 2:
                return detail.getNamaBarang() != null ? detail.getNamaBarang() : "-"; // Handle null namaBarang
            case 3:
                return detail.getStokSistem();
            case 4:
                return detail.getStokFisik();
            case 5:
                return detail.getSelisih();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // No
            case 1: // ID Barang
            case 3: // Stok Sistem
            case 4: // Stok Fisik
            case 5: // Selisih
                return Integer.class;
            case 2: // Nama Barang
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Tabel hanya untuk tampilan
    }

    public void setColumnRenderers(javax.swing.JTable table) {
        // Optional: Tambahkan renderer khusus jika diperlukan, misalnya untuk alignment
        table.getColumnModel().getColumn(0).setPreferredWidth(50); // No
        table.getColumnModel().getColumn(1).setPreferredWidth(80); // ID Barang
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Nama Barang
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Stok Sistem
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // Stok Fisik
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Selisih
    }
}