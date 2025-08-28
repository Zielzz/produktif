package com.radja.report;

import com.radja.dao.ReportDAO;
import com.radja.model.Pengeluaran;
import com.radja.service.ServiceReport;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.tablemodel.TabModPengeluaran;
import com.radja.util.TabelUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.miginfocom.swing.MigLayout;

public class ReportPengeluaran extends Form {

    private static final Logger LOGGER = Logger.getLogger(ReportPengeluaran.class.getName());
    private final TabModPengeluaran tblModel = new TabModPengeluaran();
    private final ServiceReport servisReport = new ReportDAO();
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    private final User loggedInUser;

    public ReportPengeluaran() {
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

        JLabel lbTitle = new JLabel("Laporan Pengeluaran");
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
        btnPrint.addActionListener((e) -> servisReport.printPengeluaran());

        JButton btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener((e) -> servisReport.savePengeluaranPDF());

        JButton btnExportCSV = new JButton("Excel");
        btnExportCSV.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportCSV.setIconTextGap(5);
        btnExportCSV.addActionListener((e) -> servisReport.savePengeluaranExcel());

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
        if (tblData.getColumnModel().getColumnCount() > 1) {
            tblData.getColumnModel().getColumn(1).setMinWidth(0);
            tblData.getColumnModel().getColumn(1).setMaxWidth(0);
            tblData.getColumnModel().getColumn(1).setWidth(0);
        }
    }

private void setTableProperties() {
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(SwingConstants.LEFT); // Ubah ke LEFT

    int colCount = tblData.getColumnModel().getColumnCount();
    for (int i = 0; i < colCount; i++) {
        tblData.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
    }

    DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tblData.getTableHeader().getDefaultRenderer();
    headerRenderer.setHorizontalAlignment(SwingConstants.LEFT); // Ubah ke LEFT

    int[] columnWidths = {30, 0, 150, 80, 100, 100, 100, 200, 80, 150}; // Sesuaikan dengan jumlah kolom
    for (int i = 0; i < columnWidths.length && i < colCount; i++) {
        tblData.getColumnModel().getColumn(i).setPreferredWidth(columnWidths[i]);
    }

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
        try {
            List<Pengeluaran> list = servisReport.getPengeluaranData("Aktif");
            if (list == null) {
                LOGGER.warning("No data returned from getPengeluaranData");
                tblModel.setData(List.of());
            } else {
                tblModel.setData(list);
            }
            tblData.setModel(tblModel);
            tblModel.fireTableDataChanged();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error loading pengeluaran data", e);
            tblModel.setData(List.of());
            tblData.setModel(tblModel);
            tblModel.fireTableDataChanged();
        }
    }

    private void searchData() {
        try {
            String keyword = txtSearch.getText();
            List<Pengeluaran> list = servisReport.searchPengeluaranData(keyword, "Aktif");
            if (list == null) {
                LOGGER.warning("No data returned from searchPengeluaranData for keyword: " + keyword);
                tblModel.setData(List.of());
            } else {
                tblModel.setData(list);
            }
            tblData.setModel(tblModel);
            tblModel.fireTableDataChanged();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error searching pengeluaran data", e);
            tblModel.setData(List.of());
            tblData.setModel(tblModel);
            tblModel.fireTableDataChanged();
        }
    }
}