package com.radja.tablemodel;

import com.radja.model.Fasilitas;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModFasilitas extends AbstractTableModel{

    private final List<Fasilitas> list = new ArrayList<>();
    
    public Fasilitas getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<Fasilitas> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","ID Fasilitas","Nama Fasilitas"};
    
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
        Fasilitas model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getIdFasilitas();
            case 2:
                return model.getNamaFasilitas();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
