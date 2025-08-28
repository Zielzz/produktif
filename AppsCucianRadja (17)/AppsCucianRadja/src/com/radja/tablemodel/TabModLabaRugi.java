package com.radja.tablemodel;

import com.radja.model.LabaRugi;
import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TabModLabaRugi extends AbstractTableModel {
    private List<LabaRugi> data = new ArrayList<>();
    private final String[] columnNames = {"Tanggal", "Pemasukan", "Pengeluaran", "Profit"};

    public TabModLabaRugi() {
    }

    public TabModLabaRugi(List<LabaRugi> data) {
        this.data = data;
    }

    public void setData(List<LabaRugi> data) {
        this.data = data;
        fireTableDataChanged();
    }

    public void clear() {
        data.clear();
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
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        LabaRugi lr = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return new SimpleDateFormat("dd-MM-yyyy").format(lr.getTanggal());
            case 1:
                return String.format("Rp %,d", (long)lr.getPemasukan());
            case 2:
                return String.format("Rp %,d", (long)lr.getPengeluaran());
            case 3:
                return String.format("Rp %,d", (long)lr.getProfit());
            default:
                return null;
        }
    }
}