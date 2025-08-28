package com.radja.tablemodel;

import com.radja.model.FasilitasDetail;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModFasilitasDetail extends AbstractTableModel{

    private final List<FasilitasDetail> list = new ArrayList<>();
    
    public FasilitasDetail getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<FasilitasDetail> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","ID Fasilitas","Nama Fasilitas","Barang","Satuan","Stok","Jumlah"};
    
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
        FasilitasDetail model = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return model.getFasilitas().getIdFasilitas();
            case 2:
                return model.getFasilitas().getNamaFasilitas();
            case 3:
                return model.getBarang().getNamaBarang();
            case 4:
                return model.getBarang().getSatuan();
            case 5:
                return model.getBarang().getStok();
            case 6:
                return model.getJumlah();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
