package com.radja.tablemodel;

import com.radja.model.Pemasukan;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModPemasukan extends AbstractTableModel {
    // Tambah kolom "No" di paling depan
    private final String[] columnNames = {"No", "ID Pesanan", "Nama Layanan", "Harga"};
    private List<Pemasukan> data = new ArrayList<>();

    public void setData(List<Pemasukan> data) {
        this.data = data;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pemasukan p = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                // Kolom No (nomor urut mulai dari 1)
                return rowIndex + 1;
            case 1:
                return p.getIdPesanan();
            case 2:
                return p.getNamaLayanan();
            case 3:
                return p.getHarga();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
