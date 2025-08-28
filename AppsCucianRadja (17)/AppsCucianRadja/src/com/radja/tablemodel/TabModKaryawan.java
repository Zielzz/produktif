package com.radja.tablemodel;

import com.radja.model.Karyawan;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModKaryawan extends AbstractTableModel {

    private final List<Karyawan> list = new ArrayList<>();

    public Karyawan getData(int index) {
        // Ensure that the index is valid
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            System.err.println("Invalid index: " + index);
            return null; // Return null if the index is out of bounds
        }
    }

    public void clear() {
        list.clear();
        fireTableDataChanged(); // Refresh the table view
    }

    public void setData(List<Karyawan> list) {
        clear();
        if (list != null) {
            this.list.addAll(list);
        } else {
            System.err.println("Data list is null, no data added.");
        }
        fireTableDataChanged(); // Refresh the table view
    }

    private final String[] columnNames = {"No", "ID Karyawan", "Nama Karyawan", "Telepon", "Alamat"};

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
        Karyawan model = list.get(rowIndex);
        
        // Null check for the model to prevent NullPointerException
        if (model == null) {
            System.err.println("Model is null at row: " + rowIndex);
            return null;
        }

        switch (columnIndex) {
            case 0: // No
                return (rowIndex + 1);
            case 1: // ID Karyawan
                return model.getIdKaryawan();
            case 2: // Nama Karyawan
                return model.getNamaKaryawan();
            case 3: // Telepon
                return model.getTelepon();
            case 4: // Alamat
                return model.getAlamat();
            case 5: // rfid
                return model.getRfid();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    // Optional: Add this method to allow for easy debugging
    public void printData() {
        for (Karyawan karyawan : list) {
            System.out.println("Karyawan: " + karyawan.getIdKaryawan() + " - " + karyawan.getNamaKaryawan());
        }
    }
}
