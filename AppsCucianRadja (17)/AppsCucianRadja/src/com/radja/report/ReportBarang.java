package com.radja.report;

import com.radja.dao.ReportDAO;
import com.radja.dao.BarangDAO;
import com.radja.model.Barang;
import com.radja.service.ServiceBarang;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.service.ServiceReport;
import com.radja.tablemodel.TabModBarang;
import com.radja.util.TabelUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import net.miginfocom.swing.MigLayout;

public class ReportBarang extends Form {

    private final TabModBarang tblModel = new TabModBarang();
    private final ServiceBarang servis = new BarangDAO();
    private final ServiceReport servisReport = new ReportDAO();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    
    private int idBarang;
    private final User loggedInUser;
    
    public ReportBarang() {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
        checkStokKritis();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]","[][][][fill, grow]"));
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
        
        JLabel lbTitle = new JLabel("Laporan Data Barang");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold 18");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButton(){
        JPanel panel = new JPanel(new MigLayout("wrap","[][][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        JButton btnPrint = new JButton("Print");
        btnPrint.setIcon(new FlatSVGIcon("com/radja/icon/print.svg", 0.4f));
        btnPrint.setIconTextGap(5);
        btnPrint.addActionListener((e) -> servisReport.printBarang());
        
        JButton btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener((e) -> servisReport.saveBarangPDF());
        
        JButton btnExportCSV = new JButton("Excel");
        btnExportCSV.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportCSV.setIconTextGap(5);
        btnExportCSV.addActionListener((e) -> servisReport.saveBarangExcel());
        
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
        
        panel.add(btnPrint,"hmin 30, wmin 50");
        panel.add(btnExportPDF,"hmin 30, wmin 50");
        panel.add(btnExportCSV,"hmin 30, wmin 50");
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
        hideColumnId();
        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }
    
    private void hideColumnId(){
        tblData.getColumnModel().getColumn(1).setMinWidth(0);
        tblData.getColumnModel().getColumn(1).setMaxWidth(0);
        tblData.getColumnModel().getColumn(1).setWidth(0);
    }
    
private void setTableProperties() {
    // Set column widths
    int[] columnWidths = {50, 0, 200, 100, 100, 150};
    for (int i = 0; i < columnWidths.length; i++) {
        tblData.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    // Renderer rata kiri untuk semua isi kolom
    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
    leftRenderer.setHorizontalAlignment(JLabel.LEFT);

    // Terapkan ke semua isi kolom
    for (int i = 0; i < tblData.getColumnModel().getColumnCount(); i++) {
        tblData.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
    }

    // Renderer rata kiri untuk semua header kolom
    DefaultTableCellRenderer headerLeftRenderer = new DefaultTableCellRenderer();
    headerLeftRenderer.setHorizontalAlignment(JLabel.LEFT);

    // Terapkan ke semua header kolom
    for (int i = 0; i < tblData.getColumnModel().getColumnCount(); i++) {
        tblData.getColumnModel().getColumn(i).setHeaderRenderer(headerLeftRenderer);
    }

    // Table header styling
    tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE, "" +
            "height:30;" +
            "hoverBackground:null;" +
            "pressedBackground:null;" +
            "separatorColor:$TableHeader.background;" +
            "font:bold;");

    // Table body styling - tanpa garis vertikal
    tblData.putClientProperty(FlatClientProperties.STYLE, "" +
            "rowHeight:30;" +
            "showHorizontalLines:true;" +
            "showVerticalLines:false;" +
            "intercellSpacing:1,1;" +
            "cellFocusColor:$TableHeader.hoverBackground;" +
            "selectionBackground:$TableHeader.hoverBackground;" +
            "selectionInactiveBackground:$TableHeader.hoverBackground;" +
            "selectionForeground:$Table.foreground;");

    tblData.setDefaultEditor(Object.class, null);
}

    private void loadData(){
        List<Barang> list = servis.getData("Aktif");
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void searchData(){
        String keyword = txtSearch.getText();
        List<Barang> list = servis.searchData(keyword, "Aktif");
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void checkStokKritis() {
        List<Barang> barangStokKritis = ((BarangDAO)servis).getBarangStokKritis();
        
        if (!barangStokKritis.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("<html><b>Peringatan: Stok berikut hampir habis!</b><br><br>");
            message.append("<table border='1' cellpadding='5' cellspacing='0' style='margin:auto'>");
            message.append("<tr><th>Nama Barang</th><th>Satuan</th><th>Stok</th></tr>");
            
            for (Barang barang : barangStokKritis) {
                message.append("<tr>")
                      .append("<td style='text-align:center'>").append(barang.getNamaBarang()).append("</td>")
                      .append("<td style='text-align:center'>").append(barang.getSatuan()).append("</td>")
                      .append("<td style='text-align:center'>").append(barang.getStok()).append("</td>")
                      .append("</tr>");
            }
            
            message.append("</table></html>");
            
            JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "Peringatan Stok Hampir Habis",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}