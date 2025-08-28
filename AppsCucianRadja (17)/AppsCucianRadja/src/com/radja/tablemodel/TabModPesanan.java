package com.radja.tablemodel;

import com.radja.model.Pesanan;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.table.AbstractTableModel;

public class TabModPesanan extends AbstractTableModel {

    private final List<Pesanan> list = new ArrayList<>();
    private final DecimalFormat decimalFormat;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String[] COLUMN_NAMES = {
        "No", "ID Pesanan", "No Antrian", "Tanggal", "Nama", "Merk", "No. Plat", "Layanan", "Total", "Status"
    };

    public TabModPesanan() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("id-ID"));
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        decimalFormat = new DecimalFormat("#,##0.00", symbols);
    }

    public Pesanan getData(int index) {
        return list.get(index);
    }

    public void clear() {
        list.clear();
        fireTableDataChanged();
    }

    public void setData(List<Pesanan> list) {
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
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pesanan model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return rowIndex + 1;
            case 1:
                return model.getIdPesanan();
            case 2:
                return model.getNoAntrian() != 0 ? model.getNoAntrian() : "";
            case 3:
                return model.getTanggalPesanan() != null ? model.getTanggalPesanan().format(dateFormatter) : "";
            case 4:
                return model.getNamaPelanggan() != null ? model.getNamaPelanggan() : "";
            case 5:
                return model.getMerkMobil();
            case 6:
                return model.getNomorPlat();
            case 7:
                return model.getLayanan() != null && model.getLayanan().getNamaLayanan() != null 
                       ? model.getLayanan().getNamaLayanan() : "";
            case 8:
                return decimalFormat.format(model.getTotalHarga());
            case 9:
                return model.getStatus();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
}