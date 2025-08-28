package com.radja.tablemodel;

import com.radja.model.Barang;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants;

public class TabModBarang extends AbstractTableModel {
    private final List<Barang> list = new ArrayList<>();
    private final String[] columnNames = {"No", "ID Barang", "Nama Barang", "Satuan", "Stok", "Harga Satuan"};
    private final DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance();

    public TabModBarang() {
        currencyFormat.setPositivePrefix("Rp");
        currencyFormat.setNegativePrefix("Rp-");
        currencyFormat.setMaximumFractionDigits(2);
        currencyFormat.setMinimumFractionDigits(2);
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<Barang> list) {
        clear();
        if (list != null) {
            this.list.addAll(list);
        } else {
            System.err.println("Data list is null, no data added.");
        }
        fireTableDataChanged();
    }

    public Barang getData(int index) {
        if (index >= 0 && index < list.size()) {
            return list.get(index);
        } else {
            System.err.println("Invalid index: " + index);
            return null;
        }
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
        if (rowIndex < 0 || rowIndex >= list.size()) {
            System.err.println("Invalid row index: " + rowIndex);
            return null;
        }

        Barang barang = list.get(rowIndex);
        if (barang == null) {
            System.err.println("Barang is null at row: " + rowIndex);
            return null;
        }

        switch (columnIndex) {
            case 0: // No
                return rowIndex + 1;
            case 1: // ID Barang
                return barang.getIdBarang() != 0 ? barang.getIdBarang() : null;
            case 2: // Nama Barang
                return barang.getNamaBarang() != null ? barang.getNamaBarang().trim() : null;
            case 3: // Satuan
                return formatSatuan(barang);
            case 4: // Stok
                return barang.getStok() != 0 ? barang.getStok() : 0;
            case 5: // Harga Satuan
                return barang.getHargaSatuan() != 0 ? currencyFormat.format(barang.getHargaSatuan()) : currencyFormat.format(0);
            default:
                System.err.println("Invalid column index: " + columnIndex);
                return null;
        }
    }

    private String formatSatuan(Barang barang) {
        if (barang == null) {
            System.err.println("Barang is null in formatSatuan");
            return null;
        }

        String satuan = barang.getSatuan() != null ? barang.getSatuan().trim() : "";
        Double isiSachet = barang.getIsiSachet();

        if (isiSachet != null && isiSachet > 0) {
            satuan += "/" + isiSachet.intValue();
        }

        return satuan.isEmpty() ? null : satuan;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0: // No
            case 1: // ID Barang
            case 4: // Stok
                return Integer.class;
            case 2: // Nama Barang
            case 3: // Satuan
            case 5: // Harga Satuan
                return String.class;
            default:
                return Object.class;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void setColumnRenderers(JTable table) {
        if (table == null) {
            System.err.println("Table is null in setColumnRenderers");
            return;
        }

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);

        if (table.getColumnCount() >= columnNames.length) {
            table.getColumnModel().getColumn(0).setCellRenderer(rightRenderer); // No
            table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer); // ID Barang
            table.getColumnModel().getColumn(2).setCellRenderer(leftRenderer);  // Nama Barang
            table.getColumnModel().getColumn(3).setCellRenderer(leftRenderer);  // Satuan
            table.getColumnModel().getColumn(4).setCellRenderer(rightRenderer); // Stok
            table.getColumnModel().getColumn(5).setCellRenderer(rightRenderer); // Harga Satuan

            // Set maximum width for Satuan column
            table.getColumnModel().getColumn(3).setMaxWidth(120);
        } else {
            System.err.println("Table has fewer columns than expected: " + table.getColumnCount());
        }
    }

    public void printData() {
        for (Barang barang : list) {
            System.out.println("Barang: " + barang.getIdBarang() + " - " + barang.getNamaBarang());
        }
    }
}