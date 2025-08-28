package com.radja.tablemodel;

import com.radja.model.Layanan;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModLayanan extends AbstractTableModel{

    private final List<Layanan> list = new ArrayList<>();
    
    public Layanan getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<Layanan> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","ID Layanan","Nama Layanan","Harga"};
    
    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    private DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Layanan model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getIdLayanan();
            case 2:
                return model.getNamaLayanan();
            case 3:
                return decimalFormat.format(model.getHarga());
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
