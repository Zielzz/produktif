package com.radja.report;

import com.radja.dao.ReportDAO;
import com.radja.dao.PesananDAO;
import com.radja.model.Pesanan;
import com.radja.service.ServicePesanan;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.service.ServiceReport;
import com.radja.tablemodel.TabModPesanan;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.radja.util.TabelUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker;
import raven.modal.Toast;
import javax.swing.table.DefaultTableCellRenderer;

public class ReportPesananPeriode extends Form{

    private final TabModPesanan tblModel = new TabModPesanan();
    private final ServicePesanan servis = new PesananDAO();
    private final ServiceReport servisReport = new ReportDAO();
    
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    
    private DatePicker datePicker;
    private JFormattedTextField dateEditor;
    
    private JButton btnPrint;
    private JButton btnExportPDF;
    private JButton btnExportExcel;
    
    private int idPesanan;
    private final User loggedInUser;
    
    private Date startDate;
    private Date endDate;
    
    public ReportPesananPeriode() {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
        disableComponent();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]","[][][fill][fill][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(setButtonDate());
        add(setButtonPrint());
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
        
        JLabel lbTitle = new JLabel("Pesanan Report by Period");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold 18");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButtonDate(){
        JPanel panel = new JPanel(new MigLayout());
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        dateEditor = new JFormattedTextField();
        dateEditor.setHorizontalAlignment(JFormattedTextField.CENTER);
        datePicker.setEditor(dateEditor);
        
        JButton btnPreview = new JButton("Preview");
        btnPreview.setIcon(new FlatSVGIcon("com/radja/icon/preview.svg", 0.4f));
        btnPreview.setIconTextGap(5);
        btnPreview.addActionListener((e) -> loadData());
        
        JButton btnReset = new JButton("Reset");
        btnReset.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnReset.setIconTextGap(5);
        btnReset.addActionListener((e) -> resetDate());
        
        panel.add(dateEditor,"wmin 200");
        panel.add(btnPreview,"hmin 30, wmin 50");
        panel.add(btnReset,"hmin 30, wmin 50");
        return panel;
    }
    
    private JPanel setButtonPrint(){
        JPanel panel = new JPanel(new MigLayout("","[][][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        btnPrint = new JButton("Print");
        btnPrint.setIcon(new FlatSVGIcon("com/radja/icon/print.svg", 0.4f));
        btnPrint.setIconTextGap(5);
        btnPrint.addActionListener((e) -> servisReport.printPesananPeriode(startDate, endDate));
        
        btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener((e) -> servisReport.savePesananPeriodePDF(startDate, endDate));
        
        btnExportExcel = new JButton("Excel");
        btnExportExcel.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportExcel.setIconTextGap(5);
        btnExportExcel.addActionListener((e) -> servisReport.savePesananPeriodeExcel(startDate, endDate));
        
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
        panel.add(btnExportExcel,"hmin 30, wmin 50");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }
    
    private JPanel setTableData(){
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0","fill","fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10;" +
                "[light]background:rgb(255,255,255);" +
                "[dark]background:tint($Panel.background,5%);");
        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }
    
    private void hideColumnId(){
        tblData.getColumnModel().getColumn(2).setMinWidth(0);
        tblData.getColumnModel().getColumn(2).setMaxWidth(0);
        tblData.getColumnModel().getColumn(2).setWidth(0);
    }
    
private void setTableProperties() {
    // Lebar kolom (silakan sesuaikan index & jumlah kolommu)
    int[] columnWidths = {50, 100, 200, 100, 150}; // contoh, sesuaikan dengan jumlah kolommu
    for (int i = 0; i < columnWidths.length && i < tblData.getColumnModel().getColumnCount(); i++) {
        tblData.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

    // Renderer rata kiri untuk seluruh isi data
    DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
    leftRenderer.setHorizontalAlignment(JLabel.LEFT);
    for (int i = 0; i < tblData.getColumnModel().getColumnCount(); i++) {
        tblData.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
    }

    // Renderer rata kiri untuk seluruh header kolom
    DefaultTableCellRenderer headerLeftRenderer = new DefaultTableCellRenderer();
    headerLeftRenderer.setHorizontalAlignment(JLabel.LEFT);
    for (int i = 0; i < tblData.getColumnModel().getColumnCount(); i++) {
        tblData.getColumnModel().getColumn(i).setHeaderRenderer(headerLeftRenderer);
    }

    // Styling tabel dan header (biarkan seperti aslinya)
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
    private void disableComponent(){
        btnPrint.setEnabled(false);
        btnExportPDF.setEnabled(false);
        btnExportExcel.setEnabled(false);
    }
    
    private void enableComponent(){
        btnPrint.setEnabled(true);
        btnExportPDF.setEnabled(true);
        btnExportExcel.setEnabled(true);
    }
    
    private boolean validasiInput() {
        boolean valid = false;
        
        if (datePicker.getSelectedDate() == null) {
            Toast.show(this, Toast.Type.INFO, "Please select a date", getOptionAlert());
        } else {
            valid = true;
        }
        return valid;
    }
    
    private void getDateReport(){
        LocalDate[] dates = datePicker.getSelectedDateRange();
        
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = df.format(dates[0]);
        String endDateStr = df.format(dates[1]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, Toast.Type.ERROR, "Failed to retrieve the report date", getOptionAlert());
        }
    }
    
    private void loadData(){
        if(validasiInput()==true){
            getDateReport();
            List<Pesanan> list = servis.getDataReport("Aktif", null, startDate, endDate);
            tblModel.setData(list);
            tblData.setModel(tblModel);
            
            if (tblData.getRowCount() == 0) {
                Toast.show(this, Toast.Type.INFO, "There is no report for the selected period", getOptionAlert());
                resetDate();
            } else {
                enableComponent();
            }
            
            setTableProperties();
            hideColumnId();
        }
    }
    
    private void searchData(){
        String keyword = txtSearch.getText();
        List<Pesanan> list = servis.searchDataReport(keyword, "Aktif", null, startDate, endDate);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void resetDate(){
        disableComponent();
        datePicker.clearSelectedDate();
        dateEditor.setValue(null);
        tblModel.clear();
    }
}
