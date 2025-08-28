package com.radja.tablemodel;

import com.radja.model.User;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TabModUser extends AbstractTableModel{

    private final List<User> list = new ArrayList<>();
    
    public User getData(int index){
        return list.get(index);
    }
    
    public void clear(){
        list.clear();
        fireTableDataChanged();
    }
    
    public void setData(List<User> list){
        clear();
        this.list.addAll(list);
        fireTableDataChanged();
    }
    
    private final String[] columnNames = {"No","ID User","Nama User","Username","Role","RFID"};
    
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
        User user = list.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return (rowIndex + 1);
            case 1:
                return user.getIdUser();
            case 2:
                return user.getNama();
            case 3:
                return user.getUsername();
            case 4:
                return user.getRole();
            case 5:
                return user.getRfid();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
    
}
