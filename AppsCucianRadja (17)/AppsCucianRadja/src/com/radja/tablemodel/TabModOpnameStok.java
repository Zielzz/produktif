package com.radja.tablemodel;

import com.radja.model.OpnameStok;
import java.time.LocalDate;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TabModOpnameStok extends AbstractTableModel {
    private final List<OpnameStok> list = new ArrayList<>();
    private final String[] columnNames = {"No", "ID Opname", "Tanggal Opname", "Keterangan"};

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<OpnameStok> list) {
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
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
        OpnameStok opname = list.get(rowIndex);
        switch (columnIndex) {
            case 0: return rowIndex + 1;
            case 1: return opname.getIdOpname();
            case 2: return opname.getTanggalOpname();
            case 3: return opname.getKeterangan();
            default: return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: case 1: return Integer.class;
            case 2: return LocalDate.class;
            case 3: return String.class;
            default: return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}