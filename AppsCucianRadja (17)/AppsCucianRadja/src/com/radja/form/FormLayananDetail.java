package com.radja.form;

import com.radja.model.Layanan;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.LayananDetailDAO;
import com.radja.model.LayananDetail;
import com.radja.service.ServiceLayananDetail;
import com.radja.tablemodel.TabModLayananDetail;
import com.radja.util.TabelUtils;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;

public class FormLayananDetail extends JPanel{

    private final TabModLayananDetail tblModel = new TabModLayananDetail();
    private final ServiceLayananDetail servis = new LayananDetailDAO();
    
    private JTable tblData;
    private JTextField txtSearch;

    private int idLayanan;
    
    public FormLayananDetail(int idLayanan) {
        init();
        this.idLayanan = idLayanan;
        System.out.println(idLayanan);
        loadData();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap,width 720", "[fill]","[][][][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(setButton());
        add(setTableData());
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    private JPanel setInfo(){
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        JLabel lbTitle = new JLabel("Data Layanan");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18;");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButton(){
        JPanel panel = new JPanel(new MigLayout("wrap","[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, 
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearch.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                searchData();
            }
        });
        
        tblData = new JTable();
        
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }
    
    private JPanel setTableData(){
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10;" +
                "[light]background:rgb(255,255,255);" +
                "[dark]background:tint($Panel.background,5%);");
        
        loadData();
        setTableProperties();
        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }
    
    private void setTableProperties() {
        int[] kolomHeaderTable = {0};
        int[] alignKolom = {JLabel.CENTER};
        int defaultKolom = JLabel.LEFT;
        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        TabelUtils.setHeaderAlignment(tblData, kolomHeaderTable, alignKolom, defaultKolom);
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);
        
        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "" +
                "height:30;" +
                "hoverBackground:null;" +
                "pressedBackground:null;" +
                "separatorColor:$TableHeader.background;");
        tblData.putClientProperty(FlatClientProperties.STYLE, "" +
                "rowHeight:30;" +
                "showHorizontalLines:true;" +
                "intercellSpacing:0,1;" +
                "cellFocusColor:$TableHeader.hoverBackground;" +
                "selectionBackground:$TableHeader.hoverBackground;" +
                "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                "selectionForeground:$Table.foreground;");
    }
    
    //---- Action Button
    private void loadData(){
        List<LayananDetail> list = servis.getData(idLayanan);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void searchData(){
        String keyword = txtSearch.getText();
        List<LayananDetail> list = servis.searchData(idLayanan, keyword);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
}
