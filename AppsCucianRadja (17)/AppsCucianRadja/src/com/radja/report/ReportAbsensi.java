package com.radja.report;

import com.radja.dao.AbsensiDAOImpl;
import com.radja.dao.ReportDAO;
import com.radja.model.Absensi;
import com.radja.model.User;
import com.radja.service.ServiceAbsensi;
import com.radja.service.ServiceReport;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.absensi.ui.AbsensiTableModel;
import com.radja.util.TabelUtils;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.datetime.component.date.DatePicker;
import raven.modal.Toast;

public class ReportAbsensi extends Form {

    private static final Logger LOGGER = Logger.getLogger(ReportAbsensi.class.getName());
    private final AbsensiTableModel tblModel = new AbsensiTableModel(new ArrayList<>());
    private final ServiceAbsensi servis;
    private final ServiceReport servisReport;
    private final JTable tblData = new JTable();
    
    private DatePicker datePicker;
    private JFormattedTextField dateEditor;
    private JTextField txtSearch;
    
    private JButton btnPrint;
    private JButton btnExportPDF;
    private JButton btnExportExcel;
    
    private Date startDate;
    private Date endDate;
    private User loggedInUser;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public ReportAbsensi() {
        this(new AbsensiDAOImpl(), new ReportDAO());
    }

    public ReportAbsensi(ServiceAbsensi servis, ServiceReport servisReport) {
        this.servis = servis;
        this.servisReport = servisReport;
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
        disableComponent();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loggedInUser = FormManager.getLoggedInUser();
        if (loggedInUser == null) {
            Toast.show(FormManager.getFrame(), Toast.Type.ERROR, "Tidak ada pengguna yang login! Silakan login kembali.", getOptionAlert());
            FormManager.logout();
            return;
        }
        LOGGER.log(Level.INFO, "Membuka ReportAbsensi untuk pengguna: " + loggedInUser.getUsername());
        resetDate();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][][][][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(setButtonDate());
        add(setButtonPrint());
        add(setTableData());
        setTableProperties();
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        JLabel lbTitle = new JLabel("Laporan Data Absensi");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18");
        panel.add(lbTitle);
        return panel;
    }

    private JPanel setButtonDate() {
        JPanel panel = new JPanel(new MigLayout("wrap 1", "[grow]", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.BETWEEN_DATE_SELECTED);
        datePicker.setUsePanelOption(true);
        datePicker.setCloseAfterSelected(true);
        datePicker.setDateFormat("dd/MM/yyyy");
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

        JPanel datePanel = new JPanel(new MigLayout("", "[][100][100]", "[]")); // Lebar tetap untuk tombol
        datePanel.add(dateEditor, "wmin 200");
        datePanel.add(btnPreview, "hmin 30, wmin 100");
        datePanel.add(btnReset, "hmin 30, wmin 100");

        panel.add(datePanel, "growx, gapx 5");
        return panel;
    }

    private JPanel setButtonPrint() {
        JPanel panel = new JPanel(new MigLayout("", "[][][][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        btnPrint = new JButton("Print");
        btnPrint.setIcon(new FlatSVGIcon("com/radja/icon/print.svg", 0.4f));
        btnPrint.setIconTextGap(5);
        btnPrint.addActionListener(e -> servisReport.printAbsensi());

        btnExportPDF = new JButton("PDF");
        btnExportPDF.setIcon(new FlatSVGIcon("com/radja/icon/pdf.svg", 0.4f));
        btnExportPDF.setIconTextGap(5);
        btnExportPDF.addActionListener(e -> servisReport.saveAbsensiPDF());

        btnExportExcel = new JButton("Excel");
        btnExportExcel.setIcon(new FlatSVGIcon("com/radja/icon/xls.svg", 0.4f));
        btnExportExcel.setIconTextGap(5);
        btnExportExcel.addActionListener(e -> servisReport.saveAbsensiExcel());

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
        panel.add(btnExportExcel, "hmin 30, wmin 50");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }

    private JPanel setTableData() {
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
        // Atur model tabel terlebih dahulu
        tblData.setModel(tblModel);

        // Atur lebar kolom menggunakan metode yang benar
        TabelUtils.setColumnWidths(tblData, new int[]{50, 100, 150, 100, 100, 100, 100, 150});

        // Atur alignment kolom
        TabelUtils.setColumnAlignment(tblData, new int[]{0}, new int[]{JLabel.CENTER});

        // Atur alignment header
        TabelUtils.setHeaderAlignment(tblData, new int[]{0}, new int[]{JLabel.CENTER}, JLabel.LEFT);

        // Atur properti visual tabel
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

        LOGGER.log(Level.INFO, "Properti tabel diatur untuk kolom: " + tblModel.getColumnCount());
    }

    private void disableComponent() {
        btnPrint.setEnabled(false);
        btnExportPDF.setEnabled(false);
        btnExportExcel.setEnabled(false);
    }

    private void enableComponent() {
        btnPrint.setEnabled(true);
        btnExportPDF.setEnabled(true);
        btnExportExcel.setEnabled(true);
    }

    private boolean validasiInput() {
        boolean valid = false;
        if (datePicker.getSelectedDate() == null) {
            Toast.show(FormManager.getFrame(), Toast.Type.INFO, "Please select a date", getOptionAlert());
        } else {
            valid = true;
        }
        return valid;
    }

    private void getDateReport() {
        LocalDate[] dates = datePicker.getSelectedDateRange();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = df.format(dates[0]);
        String endDateStr = df.format(dates[1]);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);
            if (endDate.before(startDate)) {
                Toast.show(FormManager.getFrame(), Toast.Type.ERROR, "End date cannot be before start date", getOptionAlert());
                startDate = null;
                endDate = null;
                resetDate();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Gagal memproses tanggal laporan", e);
            Toast.show(FormManager.getFrame(), Toast.Type.ERROR, "Failed to retrieve the report date", getOptionAlert());
        }
    }

    private void loadData() {
        if (validasiInput()) {
            getDateReport();
            if (startDate == null || endDate == null) return;

            loggedInUser = FormManager.getLoggedInUser();
            if (loggedInUser == null) {
                Toast.show(FormManager.getFrame(), Toast.Type.ERROR, "Tidak ada pengguna yang login! Silakan login kembali.", getOptionAlert());
                FormManager.logout();
                return;
            }

            String role = loggedInUser.getRole() != null ? loggedInUser.getRole().toLowerCase() : "";
            String rfid = loggedInUser.getRfid() != null ? loggedInUser.getRfid() : "";

            String keyword = txtSearch.getText().trim();
            List<Absensi> list = servis.getAbsensiForUser(role, rfid, keyword, startDate, endDate);
            LOGGER.log(Level.INFO, "Mengambil entri absensi untuk role: " + role);

            if (list.isEmpty()) {
                Toast.show(FormManager.getFrame(), Toast.Type.INFO, "There is no report for the selected period", getOptionAlert());
                resetDate();
            } else {
                enableComponent();
            }

            updateTableData(list);
        }
    }

    private void searchData() {
        if (startDate == null || endDate == null) {
            Toast.show(FormManager.getFrame(), Toast.Type.INFO, "Please select a date range first", getOptionAlert());
            return;
        }

        loggedInUser = FormManager.getLoggedInUser();
        if (loggedInUser == null) {
            Toast.show(FormManager.getFrame(), Toast.Type.ERROR, "Tidak ada pengguna yang login! Silakan login kembali.", getOptionAlert());
            FormManager.logout();
            return;
        }

        String role = loggedInUser.getRole() != null ? loggedInUser.getRole().toLowerCase() : "";
        String rfid = loggedInUser.getRfid() != null ? loggedInUser.getRfid() : "";

        String keyword = txtSearch.getText().trim();
        List<Absensi> list = servis.getAbsensiForUser(role, rfid, keyword, startDate, endDate);
        LOGGER.log(Level.INFO, "Mengambil entri absensi untuk pencarian");

        if (list.isEmpty()) {
            Toast.show(FormManager.getFrame(), Toast.Type.INFO, "No data found for the search", getOptionAlert());
        } else {
            enableComponent();
        }

        updateTableData(list);
    }

    private void updateTableData(List<Absensi> data) {
        tblModel.setData(data);
        tblData.setModel(tblModel);
    }

    private void resetDate() {
        disableComponent();
        datePicker.clearSelectedDate();
        dateEditor.setValue(null);
        txtSearch.setText("");
        tblModel.setData(new ArrayList<>());
        tblData.setModel(tblModel);
    }
}