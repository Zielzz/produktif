package com.radja.form;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.PelangganDAO;
import com.radja.form.input.FormInputPelanggan;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.Pelanggan;
import com.radja.model.User;
import com.radja.service.ServicePelanggan;
import com.radja.tablemodel.TabModPelanggan;
import com.radja.util.TabelUtils;
import java.awt.event.ItemEvent;
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
import javax.swing.JToggleButton;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.SimpleModalBorder;

public class FormPelanggan extends Form {

    private final TabModPelanggan tblModel = new TabModPelanggan();
    private final ServicePelanggan servis = new PelangganDAO();
    private JTable tblData;
    private JTextField txtSearch;
    private JComboBox<String> cbxFilter;
    private JButton btnRestore;
    private JToggleButton btnToggleMember; // New toggle button for member status
    private String idPelanggan;
    private final User loggedInUser;

    public FormPelanggan() {
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
        JLabel lbTitle = new JLabel("Data Pelanggan");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "font:bold +18");
        panel.add(lbTitle);
        return panel;
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("insets 0,wrap", "[][][][][]push[][]", "[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JButton btnAdd = new JButton("Add");
        btnAdd.putClientProperty(FlatClientProperties.STYLE,
                "background:@accentColor;foreground:#FFFFFF");
        btnAdd.setIcon(new FlatSVGIcon("com/radja/icon/add_white.svg", 0.4f));
        btnAdd.setIconTextGap(5);
        btnAdd.addActionListener(e -> insertData());

        JButton btnUpdate = new JButton("Update");
        btnUpdate.putClientProperty(FlatClientProperties.STYLE,
                "foreground:@accentColor;background:#FFFFFF");
        btnUpdate.setIcon(new FlatSVGIcon("com/radja/icon/edit.svg", 0.4f));
        btnUpdate.setIconTextGap(5);
        btnUpdate.addActionListener(e -> updateData());

        JButton btnDelete = new JButton("Delete");
        btnDelete.putClientProperty(FlatClientProperties.STYLE,
                "foreground:@accentColor;background:#FFFFFF");
        btnDelete.setIcon(new FlatSVGIcon("com/radja/icon/delete.svg", 0.4f));
        btnDelete.setIconTextGap(5);
        btnDelete.addActionListener(e -> deleteData());

        btnRestore = new JButton("Restore");
        btnRestore.putClientProperty(FlatClientProperties.STYLE,
                "foreground:@accentColor;background:#FFFFFF");
        btnRestore.setIcon(new FlatSVGIcon("com/radja/icon/restore.svg", 0.4f));
        btnRestore.setIconTextGap(5);
        btnRestore.setVisible(false);
        btnRestore.addActionListener(e -> restoreData());

        btnToggleMember = new JToggleButton("Toggle Member");
        btnToggleMember.putClientProperty(FlatClientProperties.STYLE,
                "foreground:@accentColor;background:#FFFFFF");
        btnToggleMember.setIconTextGap(5);
        btnToggleMember.addActionListener(e -> toggleMemberStatus());

        cbxFilter = new JComboBox<>();
        initComboItem(cbxFilter);
        cbxFilter.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                loadData();
                String selectedFilter = cbxFilter.getSelectedItem().toString();
                btnRestore.setVisible(selectedFilter.equals("Terhapus"));
                btnToggleMember.setVisible(!selectedFilter.equals("Terhapus")); // Hide toggle button for deleted records
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

        panel.add(btnAdd, "hmin 30");
        panel.add(btnUpdate, "hmin 30");
        panel.add(btnDelete, "hmin 30");
        panel.add(btnRestore, "hmin 30");
        panel.add(btnToggleMember, "hmin 30");
        panel.add(cbxFilter, "hmin 30");
        panel.add(txtSearch, "width 200,hmin 30,gapx 8");
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

        tblData = new JTable();
        tblData.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String selectedFilter = cbxFilter.getSelectedItem().toString();
                btnRestore.setVisible(selectedFilter.equals("Terhapus"));
                btnToggleMember.setVisible(!selectedFilter.equals("Terhapus"));
                if (tblData.getSelectedRow() != -1) {
                    Pelanggan model = tblModel.getData(tblData.getSelectedRow());
                    btnToggleMember.setSelected(model.getMemberStatus().equals("aktif"));
                    btnToggleMember.setText(model.getMemberStatus().equals("aktif") ? "Deactivate Member" : "Activate Member");
                }
            }
        });

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
        int[] kolomHeaderTable = {0, 5, 6}; // No, Status Member, Jumlah Transaksi
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
        btnToggleMember.setVisible(!filter.equals("Terhapus"));
        btnToggleMember.setSelected(false);
        btnToggleMember.setText("Toggle Member");
    }

    private void searchData() {
        String filter = cbxFilter.getSelectedItem().toString();
        String keyword = txtSearch.getText();
        List<Pelanggan> list = servis.searchData(keyword, filter);
        tblModel.setData(list);
        tblData.setModel(tblModel);
        hideColumnId();
        btnToggleMember.setVisible(!filter.equals("Terhapus"));
        btnToggleMember.setSelected(false);
        btnToggleMember.setText("Toggle Member");
    }

    public void refreshTable() {
        loadData();
    }

    private void insertData() {
        ModalDialog.showModal(this, new SimpleModalBorder(new FormInputPelanggan(null, this), "Tambah Pelanggan"),
                ModalDialog.createOption(), "form input");
    }

    private void updateData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Pelanggan model = tblModel.getData(row);
            ModalDialog.showModal(this,
                    new SimpleModalBorder(new FormInputPelanggan(model, this), "Perbarui Pelanggan"),
                    ModalDialog.createOption(), "form update");
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah terlebih dahulu",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Pelanggan model = tblModel.getData(row);
            idPelanggan = model.getIdPelanggan();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin menghapus data ini?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                Pelanggan modelPelanggan = new Pelanggan();
                modelPelanggan.setIdPelanggan(idPelanggan);
                modelPelanggan.setDeleteBy(loggedInUser.getIdUser());
                servis.deleteData(modelPelanggan);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dihapus terlebih dahulu",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void restoreData() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Pelanggan model = tblModel.getData(row);
            if (!model.isDelete()) {
                JOptionPane.showMessageDialog(this, "Data sudah aktif dan tidak bisa dipulihkan",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin memulihkan data ini?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                servis.restoreData(model);
                JOptionPane.showMessageDialog(this, "Data berhasil dipulihkan",
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                btnRestore.setVisible(false);
                cbxFilter.setSelectedIndex(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin dipulihkan terlebih dahulu",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void toggleMemberStatus() {
        int row = tblData.getSelectedRow();
        if (row != -1) {
            Pelanggan model = tblModel.getData(row);
            if (model.isDelete()) {
                JOptionPane.showMessageDialog(this, "Data yang dihapus tidak dapat diubah status membernya",
                        "Peringatan", JOptionPane.WARNING_MESSAGE);
                btnToggleMember.setSelected(model.getMemberStatus().equals("aktif"));
                return;
            }
            String newStatus = model.getMemberStatus().equals("aktif") ? "tidak aktif" : "aktif";
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin " + (newStatus.equals("aktif") ? "mengaktifkan" : "menonaktifkan") + " status member untuk " + model.getNamaPelanggan() + "?",
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                model.setMemberStatus(newStatus);
                model.setUpdateBy(loggedInUser.getIdUser());
                servis.updateMemberStatus(model); // New method in PelangganDAO
                JOptionPane.showMessageDialog(this, "Status member berhasil diubah menjadi " + newStatus,
                        "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
                btnToggleMember.setSelected(newStatus.equals("aktif"));
                btnToggleMember.setText(newStatus.equals("aktif") ? "Deactivate Member" : "Activate Member");
            } else {
                btnToggleMember.setSelected(model.getMemberStatus().equals("aktif"));
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih data yang ingin diubah status membernya terlebih dahulu",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            btnToggleMember.setSelected(false);
            btnToggleMember.setText("Toggle Member");
        }
    }
}