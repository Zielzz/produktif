package com.radja.tablemodel;

import com.radja.model.LayananDetail;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModLayananDetail extends AbstractTableModel{

    private final List<LayananDetail> list = new ArrayList<>();
    
    public LayananDetail getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<LayananDetail> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","ID Layanan","Nama Layanan","Fasilitas","Harga"};
    
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
        LayananDetail model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getLayanan().getIdLayanan();
            case 2:
                return model.getLayanan().getNamaLayanan();
            case 3:
                return model.getFasilitas().getNamaFasilitas();
            case 4:
                return decimalFormat.format(model.getLayanan().getHarga());
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
