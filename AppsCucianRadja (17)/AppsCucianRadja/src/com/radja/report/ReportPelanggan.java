package com.radja.report;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.PelangganDAO;
import com.radja.dao.ReportDAO;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.Pelanggan;
import com.radja.model.User;
import com.radja.service.ServicePelanggan;
import com.radja.service.ServiceReport;
import com.radja.tablemodel.TabModPelanggan;
import com.radja.util.TabelUtils;
import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import net.miginfocom.swing.MigLayout;

public class ReportPelanggan extends Form {

    private final TabModPelanggan tblModel = new TabModPelanggan();
    private final ServicePelanggan servis = new PelangganDAO();
    private final ServiceReport servisReport = new ReportDAO();
    private final JTable tblData = new JTable();
    private JTextField txtSearch;
    private JComboBox<String> cbxFilter;
    private final User loggedInUser;

    public ReportPelanggan() {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][][][fill,grow]"));
        add(setInfo());
        add(createSeparator(), "span,growx,height 2!,gapx 10 10");
        add(setButton());
        add(setTableData());
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206)");
        return separator;
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        JLabel lbTitle = new JLabel("Laporan Data Pelanggan");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("insets 0,wrap", "[][][][]push[]", "[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnPrint = new JButton("Print");
        btnPrint.setIcon(new FlatSVGIcon("com/radja/icon/print.svg", 0.4f));
        btnPrint.setIconTextGap(5);
        btnPrint.addActionListener((e) -> servisReport.printPelanggan());

        JButton btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener((e) -> servisReport.savePelangganPDF());

        JButton btnExportCSV = new JButton("Excel");
        btnExportCSV.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportCSV.setIconTextGap(5);
        btnExportCSV.addActionListener((e) -> servisReport.savePelangganExcel());

        cbxFilter = new JComboBox<>();
        initComboItem(cbxFilter);
        cbxFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadData();
            }
        });

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

        panel.add(btnPrint, "hmin 30");
        panel.add(btnExportPDF, "hmin 30");
        panel.add(btnExportCSV, "hmin 30");
        panel.add(cbxFilter, "hmin 30");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }

    private void initComboItem(JComboBox<String> cbx) {
        cbx.addItem("Filter");
        cbx.addItem("Semua");
        cbx.addItem("Aktif");
        cbx.addItem("Terhapus");
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 5 0 5 0", "[fill]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                "[light]background:#FFFFFF;" +
                "[dark]background:tint($Panel.background,5%)");

        loadData();
        setTableProperties();
        hideColumnId();
        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }

    private void hideColumnId() {
        tblData.getColumnModel().getColumn(1).setMinWidth(0);
        tblData.getColumnModel().getColumn(1).setMaxWidth(0);
        tblData.getColumnModel().getColumn(1).setWidth(0);
    }

    private void setTableProperties() {
        int[] kolomHeaderTable = {0, 5, 6}; // Updated to include Jumlah Transaksi
        int[] alignKolom = {JLabel.CENTER, JLabel.CENTER, JLabel.CENTER};
        int defaultKolom = JLabel.LEFT;
        TabelUtils.setColumnWidths(tblData, new int[]{0, 5, 6}, new int[]{50, 100, 100});
        TabelUtils.setHeaderAlignment(tblData, kolomHeaderTable, alignKolom, defaultKolom);
        TabelUtils.setColumnAlignment(tblData, new int[]{0, 5, 6}, JLabel.CENTER);

        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;" +
                "hoverBackground:$TableHeader.background;" +
                "pressedBackground:$TableHeader.background;" +
                "separatorColor:$TableHeader.background");
        tblData.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;" +
                "showHorizontalLines:true;" +
                "intercellSpacing:0,1;" +
                "cellFocusColor:$TableHeader.hoverBackground;" +
                "selectionBackground:$TableHeader.hoverBackground;" +
                "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                "selectionForeground:$Table.foreground");
    }

    private void loadData() {
        String filter = cbxFilter.getSelectedItem().toString();
        List<Pelanggan> list = servis.getData(filter);
        tblModel.setData(list);
        tblData.setModel(tblModel);
        hideColumnId();
    }

    private void searchData() {
    String filter = cbxFilter.getSelectedItem().toString();
    String keyword = txtSearch.getText().trim();
    List<Pelanggan> list = servis.searchData(keyword, filter);
    tblModel.setData(list);
    tblData.setModel(tblModel);
    hideColumnId();
}

    private void printReport() {
        try {
            servisReport.printPelanggan();
            JOptionPane.showMessageDialog(this, "Laporan berhasil dicetak", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak laporan: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToPDF() {
        try {
            servisReport.savePelangganPDF();
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke PDF", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengekspor PDF: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToExcel() {
        try {
            servisReport.savePelangganExcel();
            JOptionPane.showMessageDialog(this, "Data berhasil diekspor ke Excel", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal mengekspor Excel: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}