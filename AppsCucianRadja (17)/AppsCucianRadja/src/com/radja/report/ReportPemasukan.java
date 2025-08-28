package com.radja.report;

import com.radja.dao.ReportDAO;
import com.radja.model.Pesanan;
import com.radja.service.ServiceReport;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.tablemodel.TabModPesanan;
import com.radja.util.TabelUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.model.Pemasukan;
import com.radja.tablemodel.TabModPemasukan;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import net.miginfocom.swing.MigLayout;

public class ReportPemasukan extends Form {

    private final TabModPemasukan tblModel = new TabModPemasukan();
    private final ServiceReport servisReport = new ReportDAO();
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    private final User loggedInUser;

    public ReportPemasukan() {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][][][fill, grow]"));
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

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JLabel lbTitle = new JLabel("Laporan Pemasukan");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");

        panel.add(lbTitle);
        return panel;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[][][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnPrint = new JButton("Print");
        btnPrint.setIcon(new FlatSVGIcon("com/radja/icon/print.svg", 0.4f));
        btnPrint.setIconTextGap(5);
        btnPrint.addActionListener((e) -> servisReport.printPemasukan());

        JButton btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener((e) -> servisReport.savePemasukanPDF());

        JButton btnExportCSV = new JButton("Excel");
        btnExportCSV.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportCSV.setIconTextGap(5);
        btnExportCSV.addActionListener((e) -> servisReport.savePemasukanExcel());

        txtSearch = new JTextField();
        txtSearch.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search...");
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtSearch.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON,
                new FlatSVGIcon("raven/modal/demo/icons/search.svg", 0.4f));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchData();
            }
        });

        panel.add(btnPrint, "hmin 30, wmin 50");
        panel.add(btnExportPDF, "hmin 30, wmin 50");
        panel.add(btnExportCSV, "hmin 30, wmin 50");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE,
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
    

    private void hideColumnId() {
        // Sembunyikan kolom ID pada index 1
        if (tblData.getColumnModel().getColumnCount() > 1) {
            tblData.getColumnModel().getColumn(1).setMinWidth(0);
            tblData.getColumnModel().getColumn(1).setMaxWidth(0);
            tblData.getColumnModel().getColumn(1).setWidth(0);
        }
    }

    private void setTableProperties() {
    int[] kolomHeaderTable = {0}; 
    int[] alignKolom = {JLabel.CENTER}; 
    int defaultKolom = JLabel.LEFT; 

    TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
    TabelUtils.setHeaderAlignment(tblData, kolomHeaderTable, alignKolom, defaultKolom);
    TabelUtils.setColumnAlignment(tblData, new int[]{0}, JLabel.CENTER);

    tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
            "height:30;" +
            "hoverBackground:null;" +
            "pressedBackground:null;" +
            "separatorColor:$TableHeader.background;");

    tblData.putClientProperty(FlatClientProperties.STYLE,
            "rowHeight:30;" +
            "showHorizontalLines:true;" +
            "intercellSpacing:0,1;" +
            "cellFocusColor:$TableHeader.hoverBackground;" +
            "selectionBackground:$TableHeader.hoverBackground;" +
            "selectionInactiveBackground:$TableHeader.hoverBackground;" +
            "selectionForeground:$Table.foreground;");
}


    private void loadData() {
    List<Pemasukan> list = servisReport.getPemasukanList();
    tblModel.setData(list);
    tblData.setModel(tblModel);
}

private void searchData() {
    String keyword = txtSearch.getText();
    List<Pemasukan> list = servisReport.searchPemasukan(keyword);
    tblModel.setData(list);
    tblData.setModel(tblModel);
}


    private void rearrangeColumns() {
    TableColumnModel columnModel = tblData.getColumnModel();
        int colCount = columnModel.getColumnCount();
        if (colCount > 3) {
            // Dapatkan index kolom berdasarkan header
            int indexNoAntrian = -1;
            int indexTanggal = -1;
            for (int i = 0; i < colCount; i++) {
                String header = columnModel.getColumn(i).getHeaderValue().toString();
                if (header.equalsIgnoreCase("No Antrian")) {
                    indexNoAntrian = i;
                }
                if (header.equalsIgnoreCase("Tanggal")) {
                    indexTanggal = i;
                }
            }
            if (indexNoAntrian != -1 && indexTanggal != -1) {
                int targetIndex = indexTanggal + 1;
                if (indexNoAntrian != targetIndex) {
                    columnModel.moveColumn(indexNoAntrian, targetIndex);
                }
            }
        }
    }
}