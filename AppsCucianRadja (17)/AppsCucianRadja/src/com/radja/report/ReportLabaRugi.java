package com.radja.report;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.LabaRugiDAO;
import com.radja.model.LabaRugi;
import com.radja.service.ServiceLabaRugi;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.tablemodel.TabModLabaRugi;
import com.radja.util.AlertUtils;
import com.radja.util.TabelUtils;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker;
import raven.modal.Toast;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.awt.Dimension;
import java.awt.Color;

public class ReportLabaRugi extends Form {
    private final JTable tblData = new JTable();
    private final ServiceLabaRugi serviceLabaRugi = new LabaRugiDAO();
    private final User loggedInUser;
    private DatePicker datePicker;
    private JFormattedTextField dateEditor;
    private JTextField txtSearch;
    private JButton btnPrint, btnExportPDF, btnExportExcel;

    // Label khusus untuk nilai di kotak info total
    private JLabel lblTotalPemasukanValue, lblTotalPengeluaranValue, lblTotalProfitValue;

    private Date startDate, endDate;

    public ReportLabaRugi() {
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
        disableComponents();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][][fill][fill][fill,grow]"));
        add(createInfoPanel());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(createDatePanel());
        add(createButtonPanel());
        add(createTablePanel());
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JLabel lbTitle = new JLabel("Laba Rugi Report");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");

        panel.add(lbTitle);
        return panel;
    }

    private JPanel createDatePanel() {
        JPanel panel = new JPanel(new MigLayout());
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        dateEditor = new JFormattedTextField();
        dateEditor.setHorizontalAlignment(JFormattedTextField.CENTER);
        datePicker.setEditor(dateEditor);

        JButton btnPreview = new JButton("Preview");
        btnPreview.setIcon(new FlatSVGIcon("com/radja/icon/preview.svg", 0.4f));
        btnPreview.addActionListener(e -> loadData());

        JButton btnReset = new JButton("Reset");
        btnReset.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnReset.addActionListener(e -> resetDate());

        panel.add(dateEditor, "wmin 200");
        panel.add(btnPreview, "hmin 30, wmin 50");
        panel.add(btnReset, "hmin 30, wmin 50");
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new MigLayout("", "[][][]push[][]", "[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        btnPrint = new JButton("Print");
        btnPrint.setIcon(new FlatSVGIcon("com/radja/icon/print.svg", 0.4f));
        btnPrint.addActionListener(e -> {
            try {
                if (startDate != null && endDate != null) {
                    serviceLabaRugi.printLabaRugi(startDate, endDate);
                    Toast.show(this, Toast.Type.SUCCESS, "Report printed successfully", AlertUtils.getOptionAlert());
                } else {
                    Toast.show(this, Toast.Type.ERROR, "Please select a valid date range", AlertUtils.getOptionAlert());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.show(this, Toast.Type.ERROR, "Failed to print report: " + ex.getMessage(), AlertUtils.getOptionAlert());
            }
        });

        btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.addActionListener(e -> {
            try {
                if (startDate != null && endDate != null) {
                    serviceLabaRugi.saveLabaRugiPDF(startDate, endDate);
                    Toast.show(this, Toast.Type.SUCCESS, "PDF exported successfully", AlertUtils.getOptionAlert());
                } else {
                    Toast.show(this, Toast.Type.ERROR, "Please select a valid date range", AlertUtils.getOptionAlert());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.show(this, Toast.Type.ERROR, "Failed to export PDF: " + ex.getMessage(), AlertUtils.getOptionAlert());
            }
        });

        btnExportExcel = new JButton("Excel");
        btnExportExcel.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportExcel.addActionListener(e -> {
            try {
                if (startDate != null && endDate != null) {
                    serviceLabaRugi.saveLabaRugiExcel(startDate, endDate);
                    Toast.show(this, Toast.Type.SUCCESS, "Excel exported successfully", AlertUtils.getOptionAlert());
                } else {
                    Toast.show(this, Toast.Type.ERROR, "Please select a valid date range", AlertUtils.getOptionAlert());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.show(this, Toast.Type.ERROR, "Failed to export Excel: " + ex.getMessage(), AlertUtils.getOptionAlert());
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

        lblTotalPemasukanValue = new JLabel("Rp 0");
        lblTotalPengeluaranValue = new JLabel("Rp 0");
        lblTotalProfitValue = new JLabel("Rp 0");

        JPanel panelTotals = new JPanel(new MigLayout("gap 10"));
        panelTotals.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        panelTotals.putClientProperty(FlatClientProperties.STYLE,
                "arc:15;" +
                "background:#FFFFFF;");
        panelTotals.setOpaque(true);

        panelTotals.add(createInfoBox("Total Pemasukan", lblTotalPemasukanValue), "w 180, h 60, gapright 10");
        panelTotals.add(createInfoBox("Total Pengeluaran", lblTotalPengeluaranValue), "w 180, h 60, gapright 10");
        panelTotals.add(createInfoBox("Total Profit", lblTotalProfitValue), "w 180, h 60");

        panel.add(btnPrint, "hmin 30, wmin 50");
        panel.add(btnExportPDF, "hmin 30, wmin 50");
        panel.add(btnExportExcel, "hmin 30, wmin 50");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        panel.add(panelTotals, "gapx 10, aligny center");

        return panel;
    }

    private JPanel createInfoBox(String title, JLabel valueLabel) {
        JPanel box = new JPanel(new MigLayout("fill, ins 10", "[center]", "[center][center]"));
        box.setOpaque(true);
        box.setBorder(BorderFactory.createLineBorder(Color.decode("#cccccc"), 2));
        box.putClientProperty(FlatClientProperties.STYLE,
                "arc:15;" +
                "background:#f5f5f5;");

        JLabel lblTitle = new JLabel(title);
        lblTitle.putClientProperty(FlatClientProperties.STYLE, 
                "font:12;" +
                "foreground:#808080;");

        valueLabel.putClientProperty(FlatClientProperties.STYLE, "font:bold 16;");

        box.add(lblTitle, "wrap");
        box.add(valueLabel);

        return box;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                "[light]background:rgb(255,255,255);" +
                "[dark]background:tint($Panel.background,5%);");
        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }

    private void setTableProperties() {
        int colCount = tblData.getColumnModel().getColumnCount();

        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < colCount; i++) {
            tblData.getColumnModel().getColumn(i).setCellRenderer(leftRenderer);
        }

        DefaultTableCellRenderer headerLeftRenderer = new DefaultTableCellRenderer();
        headerLeftRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        for (int i = 0; i < colCount; i++) {
            tblData.getColumnModel().getColumn(i).setHeaderRenderer(headerLeftRenderer);
        }

        if (colCount >= 4) {
            tblData.getColumnModel().getColumn(0).setPreferredWidth(120);
            tblData.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblData.getColumnModel().getColumn(2).setPreferredWidth(150);
            tblData.getColumnModel().getColumn(3).setPreferredWidth(150);
        }

        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;" +
                        "hoverBackground:null;" +
                        "pressedBackground:null;" +
                        "font:bold;" +
                        "separatorColor:$TableHeader.background;");
        tblData.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;" +
                        "showHorizontalLines:true;" +
                        "showVerticalLines:false;" +
                        "intercellSpacing:0,1;" +
                        "cellFocusColor:$TableHeader.hoverBackground;" +
                        "selectionBackground:$TableHeader.hoverBackground;" +
                        "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                        "selectionForeground:$Table.foreground;");

        tblData.setShowVerticalLines(false);
        tblData.setShowHorizontalLines(true);
        tblData.setIntercellSpacing(new Dimension(0, 1));
        tblData.setGridColor(Color.LIGHT_GRAY);

        tblData.setRowHeight(30);
    }

    private void disableComponents() {
        btnPrint.setEnabled(false);
        btnExportPDF.setEnabled(false);
        btnExportExcel.setEnabled(false);
        System.out.println("Buttons disabled");
    }

    private void enableComponents() {
        btnPrint.setEnabled(true);
        btnExportPDF.setEnabled(true);
        btnExportExcel.setEnabled(true);
        System.out.println("Buttons enabled");
    }

    private boolean validateInput() {
        if (datePicker.getSelectedDate() == null) {
            Toast.show(this, Toast.Type.INFO, "Please select a date range", AlertUtils.getOptionAlert());
            return false;
        }
        return true;
    }

    private void getDateReport() {
        LocalDate[] dates = datePicker.getSelectedDateRange();
        if (dates == null || dates.length < 2 || dates[0] == null || dates[1] == null) {
            Toast.show(this, Toast.Type.ERROR, "Invalid date range selected", AlertUtils.getOptionAlert());
            startDate = null;
            endDate = null;
            return;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = df.format(dates[0]);
        String endDateStr = df.format(dates[1]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
            System.out.println("Date range: " + startDate + " to " + endDate);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show(this, Toast.Type.ERROR, "Failed to parse dates: " + e.getMessage(), AlertUtils.getOptionAlert());
            startDate = null;
            endDate = null;
        }
    }

    private void loadData() {
        if (validateInput()) {
            getDateReport();
            if (startDate == null || endDate == null) {
                System.out.println("No valid date range, skipping data load");
                return;
            }
            List<LabaRugi> list = serviceLabaRugi.getLabaRugiData(startDate, endDate);
            System.out.println("Jumlah data: " + list.size());
            double totalPemasukan = serviceLabaRugi.getTotalPemasukan(startDate, endDate);
            double totalPengeluaran = serviceLabaRugi.getTotalPengeluaran(startDate, endDate);
            double totalProfit = totalPemasukan - totalPengeluaran;

            tblData.setModel(new TabModLabaRugi(list));

            lblTotalPemasukanValue.setText(String.format("Rp %,d", (long) totalPemasukan));
            lblTotalPengeluaranValue.setText(String.format("Rp %,d", (long) totalPengeluaran));
            lblTotalProfitValue.setText(String.format("Rp %,d", (long) totalProfit));

            if (list.isEmpty()) {
                Toast.show(this, Toast.Type.INFO, "No data found for the selected period", AlertUtils.getOptionAlert());
                resetDate();
            } else {
                enableComponents();
            }

            setTableProperties();
        }
    }

    private void searchData() {
        String keyword = txtSearch.getText();
        List<LabaRugi> list = serviceLabaRugi.searchLabaRugiData(keyword, startDate, endDate);
        System.out.println("Search results: " + list.size() + " items for keyword: " + keyword);
        tblData.setModel(new TabModLabaRugi(list));
        setTableProperties();
    }

    private void resetDate() {
        disableComponents();
        datePicker.clearSelectedDate();
        dateEditor.setValue(null);
        tblData.setModel(new TabModLabaRugi());

        lblTotalPemasukanValue.setText("Rp 0");
        lblTotalPengeluaranValue.setText("Rp 0");
        lblTotalProfitValue.setText("Rp 0");
    }
}