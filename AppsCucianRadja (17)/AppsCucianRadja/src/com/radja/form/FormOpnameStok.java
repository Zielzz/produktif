package com.radja.form;

import com.radja.dao.BarangDAO;
import com.radja.dao.OpnameStokDAO;
import com.radja.model.Barang;
import com.radja.model.OpnameStok;
import com.radja.model.DetailOpnameStok;
import com.radja.service.ServiceBarang;
import com.radja.service.ServiceOpnameStok;
import com.radja.tablemodel.TabModOpnameStok;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.form.input.FormInputOpnameStok;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.util.AlertUtils;
import com.radja.util.TabelUtils;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.SimpleModalBorder;
import raven.modal.option.Option;

public class FormOpnameStok extends Form {
    private final TabModOpnameStok tblModel = new TabModOpnameStok();
    private final ServiceOpnameStok servis = new OpnameStokDAO();
    private final ServiceBarang servisBarang = new BarangDAO();
    private JTable tblData;
    private JTextField txtSearch;
    private JComboBox<String> cbxFilter;
    private final User loggedInUser;

    public FormOpnameStok() {
        loggedInUser = FormManager.getLoggedInUser();
        init();
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

        JLabel lbTitle = new JLabel("Opname Stok");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18;");

        panel.add(lbTitle);
        return panel;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap", "[][]push[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, "background:@accentColor;foreground:rgb(255,255,255);");
        btnAdd.setIcon(new FlatSVGIcon("com/radja/icon/add_white.svg", 0.4f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener((e) -> insertData());

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

        panel.add(btnAdd, "hmin 30");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }

    private JPanel setTableData() {
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE,
                "arc:10;" +
                        "[light]background:rgb(255,255,255);" +
                        "[dark]background:tint($Panel.background,5%);");

        tblData = new JTable();
        loadData();
        setTableProperties();
        JScrollPane scrollPane = new JScrollPane(tblData);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane);
        return panel;
    }

    private void setTableProperties() {
        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        TabelUtils.setColumnAlignment(tblData, new int[]{0, 1, 2}, JLabel.CENTER);
        tblData.setRowHeight(30);
        tblData.setIntercellSpacing(new java.awt.Dimension(1, 1));
        tblData.setShowGrid(true);
        tblData.setGridColor(new Color(200, 200, 200));
        tblData.getTableHeader().setReorderingAllowed(false);
        tblData.getTableHeader().setResizingAllowed(false);
        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE,
                "height:30;" +
                        "hoverBackground:null;" +
                        "pressedBackground:null;" +
                        "separatorColor:$TableHeader.background;");
        tblData.putClientProperty(FlatClientProperties.STYLE,
                "rowHeight:30;" +
                        "showHorizontalLines:true;" +
                        "intercellSpacing:1,1;" +
                        "cellFocusColor:$TableHeader.hoverBackground;" +
                        "selectionBackground:$TableHeader.hoverBackground;" +
                        "selectionInactiveBackground:$TableHeader.hoverBackground;" +
                        "selectionForeground:$Table.foreground;");
    }

    private void loadData() {
        List<OpnameStok> list = servis.getAllOpnameStok();
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }

    private void searchData() {
        String keyword = txtSearch.getText();
        // Implementasi pencarian jika diperlukan
    }

    private void insertData() {
        Option option = ModalDialog.createOption();
        option.setBackground(Color.BLACK);
        ModalDialog.showModal(this, new SimpleModalBorder(new FormInputOpnameStok(this, servisBarang), "Tambah Opname Stok"), option, "form input opname");
    }

    public void refreshTable() {
        loadData();
    }
}