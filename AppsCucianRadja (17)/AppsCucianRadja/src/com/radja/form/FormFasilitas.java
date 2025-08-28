package com.radja.form;

import com.radja.form.input.FormInputFasilitas;
import com.radja.dao.FasilitasDAO;
import com.radja.model.Fasilitas;
import com.radja.service.ServiceFasilitas;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.tablemodel.TabModFasilitas;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.radja.util.MessageModal;
import com.radja.util.TabelUtils;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.model.User;
import java.awt.Color;
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

public class FormFasilitas extends Form{

    private final TabModFasilitas tblModel = new TabModFasilitas();
    private final ServiceFasilitas servis = new FasilitasDAO();
    
    private JTable tblData;
    private JTextField txtSearch;
    private JComboBox cbxFilter;
    private JButton btnRestore;
    
    private int idFasilitas;
    private final User loggedInUser;
    
    public FormFasilitas() {
        init();
        this.loggedInUser = FormManager.getLoggedInUser();
    }
    
    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
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
        
        JLabel lbTitle = new JLabel("Data Fasilitas");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold 18;");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private JPanel setButton(){
        JPanel panel = new JPanel(new MigLayout("wrap","[][][][][]push[][]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
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
        
        JButton btnDetail = new JButton("Detail");
        btnDetail.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        btnDetail.setIcon(new FlatSVGIcon("com/radja/icon/detail.svg", 0.4f));
        btnDetail.setIconTextGap(5);
        btnDetail.addActionListener((e) -> viewDetail());
        
        btnRestore = new JButton("Restore");
        btnRestore.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        btnRestore.setIcon(new FlatSVGIcon("com/radja/icon/restore.svg", 0.4f));
        btnRestore.setIconTextGap(5);
        btnRestore.setVisible(false);
        btnRestore.addActionListener((e) -> restoreData());
        
        cbxFilter = new JComboBox();
        initComboItem(cbxFilter);
        cbxFilter.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    loadData();
                    String selectedFilter = cbxFilter.getSelectedItem().toString();
                    if (selectedFilter.equals("Filter") || selectedFilter.equals("Aktif")) {
                        btnRestore.setVisible(false);
                    }
                }
            }
        });
        
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
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedFilter = cbxFilter.getSelectedItem().toString();
        
                if (selectedFilter.equals("Filter") || selectedFilter.equals("Aktif")) {
                    btnRestore.setVisible(false);
                } else {
                    btnRestore.setVisible(true);
                }
                
            }
        });
        
        panel.add(btnAdd, "hmin 30");
        panel.add(btnUpdate,"hmin 30");
        panel.add(btnDelete,"hmin 30");
        panel.add(btnDetail,"hmin 30");
        panel.add(btnRestore,"hmin 30");
        panel.add(cbxFilter,"hmin 30");
        panel.add(txtSearch, "width 300, height 30, gapx 8");
        return panel;
    }
    
    private void initComboItem(JComboBox cbx) {
        cbx.addItem("Filter");
        cbx.addItem("Semua");
        cbx.addItem("Aktif");
        cbx.addItem("Terhapus");
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
        String filter = cbxFilter.getSelectedItem().toString();
        List<Fasilitas> list = servis.getData(filter);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    private void searchData(){
        String filter = cbxFilter.getSelectedItem().toString();
        String keyword = txtSearch.getText();
        List<Fasilitas> list = servis.searchData(keyword, filter);
        tblModel.setData(list);
        tblData.setModel(tblModel);
    }
    
    public void refreshTable(){
        loadData();
    }
    
    private void insertData() {
        Option option = ModalDialog.createOption();
        option.setBackground(Color.BLACK);
        ModalDialog.showModal(this, new SimpleModalBorder(new FormInputFasilitas(null, this), "Tambah Fasilitas"), option, "form input");
    }
    
    private void updateData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Fasilitas model = tblModel.getData(row);
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, 
                    new SimpleModalBorder(new FormInputFasilitas(model, this), "Perbarui Fasilitas"), 
                    option, "form update");
        } else {
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to update", getOptionAlert());
        }
    }
    
    private void deleteData() {
        int row = tblData.getSelectedRow();
        if(row != -1){
            Fasilitas model = tblModel.getData(row);
            idFasilitas = model.getIdFasilitas();
            
            String message = "Are you sure you want to delete this data?";
            String title = "Confirmation";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, SimpleModalBorder.YES_NO_OPTION, 
                (controller, action) -> {
                    if (action == SimpleModalBorder.YES_OPTION) {
                        Fasilitas modelFasilitas = new Fasilitas();
                        modelFasilitas.setIdFasilitas(idFasilitas);
                        modelFasilitas.setDeleteBy(loggedInUser.getIdUser());

                        servis.deleteData(modelFasilitas);
                        Toast.show(this,Toast.Type.SUCCESS,"Data has been successfully deleted", getOptionAlert());
                        loadData();
                    }
                }), option);
        }else{
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to delete", getOptionAlert());
        }
    }
    
    private void restoreData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Fasilitas model = tblModel.getData(row);

            if (!model.isDelete()) { 
                Toast.show(this, Toast.Type.WARNING, "Data is already active and cannot be restored", getOptionAlert());
                return;
            }

            String message = "Are you sure you want to restore this data?";
            String title = "Confirmation";

            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, new MessageModal(MessageModal.Type.INFO, message, title, raven.modal.component.SimpleModalBorder.YES_NO_OPTION,
                (controller, action) -> {
                    if (action == raven.modal.component.SimpleModalBorder.YES_OPTION) {
                        servis.restoreData(model);
                        Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully restored", getOptionAlert());
                        loadData();
                        btnRestore.setVisible(false);
                        cbxFilter.setSelectedIndex(0);
                    }
                }), option);
        } else {
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to restore", getOptionAlert());
        }
    }
    
    private void viewDetail(){
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Fasilitas model = tblModel.getData(row);
            idFasilitas = model.getIdFasilitas();
            
            Option option = ModalDialog.createOption();
            option.setBackground(Color.BLACK);
            ModalDialog.showModal(this, 
                    new SimpleModalBorder(new FormFasilitasDetail(idFasilitas), "Detail"), 
                    option, "form detail");
        } else {
            Toast.show(this, Toast.Type.INFO, "Please select the data you want to view data", getOptionAlert());
        }
    }

}
