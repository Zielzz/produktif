    package com.radja.tablemodel;

import com.radja.model.Pelanggan;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModPelanggan extends AbstractTableModel {

    private final List<Pelanggan> list = new ArrayList<>();
    private final String[] columnNames = {"No", "ID Pelanggan", "Nama", "Telepon", "Alamat", "Status Member", "Jumlah Transaksi"};

    public Pelanggan getData(int index) {
        return list.get(index);
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<Pelanggan> list) {
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
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pelanggan model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getIdPelanggan();
            case 2:
                return model.getNamaPelanggan();
            case 3:
                return model.getTelepon();
            case 4:
                return model.getAlamat();
            case 5:
                return model.getMemberStatus();
            case 6:
                return model.getJumlahTransaksi();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}