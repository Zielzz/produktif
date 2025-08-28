package com.radja.form;

import com.radja.form.input.FormInputBarang;
import com.radja.dao.BarangDAO;
import com.radja.model.Barang;
import com.radja.service.ServiceBarang;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.tablemodel.TabModBarang;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.radja.util.MessageModal;
import com.radja.util.TabelUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.model.User;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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

public class FormBarang extends Form {

    private final TabModBarang tblModel = new TabModBarang();
    private final ServiceBarang servis = new BarangDAO();
    
    private JTable tblData;
    private JTextField txtSearch;
    private JComboBox<String> cbxFilter;
    private JButton btnRestore;
    
    private int idBarang;
    private final User loggedInUser;
    
    public FormBarang() {
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
    
    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JLabel lbTitle = new JLabel("Data Barang");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18;");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap","[][][][][]push[][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");
        
        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);");
        btnAdd.setIcon(new FlatSVGIcon("com/radja/icon/add_white.svg", 0.4f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener((e) -> insertData());
                
        JButton btnUpdate = new JButton("Update");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        btnUpdate.setIcon(new FlatSVGIcon("com/radja/icon/edit.svg", 0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener((e) -> updateData());
        
        JButton btnDelete = new JButton("Delete");
        btnDelete.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        btnDelete.setIcon(new FlatSVGIcon("com/radja/icon/delete.svg", 0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener((e) -> deleteData());
        
        btnRestore = new JButton("Restore");
        btnRestore.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        btnRestore.setIcon(new FlatSVGIcon("com/radja/icon/restore.svg", 0.4f));
        btnRestore.setIconTextGap(5);
        btnRestore.setVisible(false);
        btnRestore.addActionListener((e) -> restoreData());
        
        cbxFilter = new JComboBox<>();
        initComboItem(cbxFilter);
        cbxFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadData();
                    String selectedFilter = cbxFilter.getSelectedItem().toString();
                    btnRestore.setVisible(selectedFilter.equals("Terhapus"));
                }
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
        
        tblData = new JTable();
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedFilter = cbxFilter.getSelectedItem().toString();
                btnRestore.setVisible(selectedFilter.equals("Terhapus"));
            }
        });
        
        panel.add(btnAdd, "hmin 30");
        panel.add(btnUpdate, "hmin 30");
        panel.add(btnDelete, "hmin 30");
        panel.add(btnRestore, "hmin 30");
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
        JPanel panel = new JPanel(new MigLayout("fill, insets 5 0 5 0", "fill", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "[light]background:rgb(255,255,255);"
                + "[dark]background:tint($Panel.background,5%);");
        
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
        TabelUtils.setColumnWidths(tblData, new int[]{0}, new int[]{50});
        int[] kolomHeaderTable = new int[tblData.getColumnModel().getColumnCount()];
        for (int i = 0; i < kolomHeaderTable.length; i++) {
            kolomHeaderTable[i] = i;
        }
        int[] alignKolom = new int[kolomHeaderTable.length];
        for (int i = 0; i < alignKolom.length; i++) {
            alignKolom[i] = JLabel.LEFT;
        }
        int defaultKolom = JLabel.LEFT;
        TabelUtils.setHeaderAlignment(tblData, kolomHeaderTable, alignKolom, defaultKolom);
        TabelUtils.setColumnAlignment(tblData, kolomHeaderTable, JLabel.LEFT);
        tblData.setRowHeight(30);
        tblData.setIntercellSpacing(new Dimension(0, 1));
        tblData.setShowGrid(true);
        tblData.setShowVerticalLines(false);
        tblData.setGridColor(new Color(200, 200, 200));
        tblData.getTableHeader().setReorderingAllowed(false);
        tblData.getTableHeader().setResizingAllowed(false);
        tblData.getTableHeader().putClientProperty(FlatClientProperties.STYLE, ""
                + "height:30;"
                + "hoverBackground:null;"
                + "pressedBackground:null;"
                + "separatorColor:$TableHeader.background;");
        tblData.putClientProperty(FlatClientProperties.STYLE, ""
                + "rowHeight:30;"
                + "showHorizontalLines:true;"
                + "showVerticalLines:false;"
                + "intercellSpacing:0,1;"
                + "cellFocusColor:$TableHeader.hoverBackground;"
                + "selectionBackground:$TableHeader.hoverBackground;"
                + "selectionInactiveBackground:$TableHeader.hoverBackground;"
                + "selectionForeground:$Table.foreground;");
    }
    
    private void checkStokKritis() {
        List<Barang> barangStokKritis = ((BarangDAO)servis).getBarangStokKritis();
        
        if (!barangStokKritis.isEmpty()) {
            StringBuilder message = new StringBuilder();
            message.append("<html><b>Peringatan: Stok berikut hampir habis!</b><br><br>");
            message.append("<table border='1' cellpadding='5'>");
            message.append("<tr><th>Nama Barang</th><th>Satuan</th><th>Stok</th><th>Harga Satuan</th></tr>");
            
            for (Barang barang : barangStokKritis) {
                message.append("<tr>")
                      .append("<td>").append(barang.getNamaBarang()).append("</td>")
                      .append("<td>").append(barang.getSatuan()).append("</td>")
                      .append("<td>").append(barang.getStok()).append("</td>")
                      .append("<td>Rp").append(String.format("%,.2f", barang.getHargaSatuan())).append("</td>")
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
    
    private void loadData() {
        String filter = cbxFilter.getSelectedItem().toString();
        List<Barang> list = servis.getData(filter);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void searchData() {
        String filter = cbxFilter.getSelectedItem().toString();
        String keyword = txtSearch.getText();
        List<Barang> list = servis.searchData(keyword, filter);
        tblModel.setData(list);
    }
    
    public void refreshTable() {
        loadData();
        checkStokKritis();
    }
    
    private void insertData() {
        Option option = ModalDialog.createOption();
        option.setBackground(Color.BLACK);
        ModalDialog.showModal(this, new SimpleModalBorder(new FormInputBarang(null, this), "Tambah Barang"), option, "form input");
    }
    
    private void updateData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Barang model = tblModel.getData(row);
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, 
                    new SimpleModalBorder(new FormInputBarang(model, this), "Perbarui Barang"), 
                    option, "form update");
        } else {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih data yang ingin diperbarui", getOptionAlert());
        }
    }
    
    private void deleteData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Barang model = tblModel.getData(row);
            idBarang = model.getIdBarang();
            
            String message = "Apakah Anda yakin ingin menghapus data ini?";
            String title = "Konfirmasi";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, SimpleModalBorder.YES_NO_OPTION, 
                (controller, action) -> {
                    if (action == SimpleModalBorder.YES_OPTION) {
                        Barang modelBarang = new Barang();
                        modelBarang.setIdBarang(idBarang);
                        modelBarang.setDeleteBy(loggedInUser.getIdUser());

                        servis.deleteData(modelBarang);
                        Toast.show(this, Toast.Type.SUCCESS, "Data telah berhasil dihapus", getOptionAlert());
                        loadData();
                    }
                }), option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih data yang ingin dihapus", getOptionAlert());
        }
    }
    
    private void restoreData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Barang model = tblModel.getData(row);

            if (!model.isDelete()) { 
                Toast.show(this, Toast.Type.WARNING, "Data sudah aktif dan tidak dapat dipulihkan", getOptionAlert());
                return;
            }

            String message = "Apakah Anda yakin ingin memulihkan data ini?";
            String title = "Konfirmasi";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, SimpleModalBorder.YES_NO_OPTION,
                (controller, action) -> {
                    if (action == SimpleModalBorder.YES_OPTION) {
                        servis.restoreData(model);
                        Toast.show(this, Toast.Type.SUCCESS, "Data telah berhasil dipulihkan", getOptionAlert());
                        loadData();
                        btnRestore.setVisible(false);
                        cbxFilter.setSelectedIndex(0);
                    }
                }), option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Silakan pilih data yang ingin dipulihkan", getOptionAlert());
        }
    }
}