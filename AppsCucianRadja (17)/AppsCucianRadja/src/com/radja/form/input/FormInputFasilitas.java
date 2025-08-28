package com.radja.form.input;

import com.radja.dao.FasilitasDAO;
import com.radja.form.FormFasilitas;
import com.radja.main.FormManager;
import com.radja.model.Fasilitas;
import com.radja.service.ServiceFasilitas;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.BarangDAO;
import com.radja.dao.FasilitasDetailDAO;
import com.radja.model.Barang;
import com.radja.model.FasilitasDetail;
import com.radja.model.User;
import com.radja.service.ServiceBarang;
import com.radja.service.ServiceFasilitasDetail;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputFasilitas extends JPanel{

    private JPanel panelCheckBox;
    private JTextField txtNama;
    
    List<JCheckBox> checkBoxes = new ArrayList<>();
    List<JTextField> textFields = new ArrayList<>();
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private final ServiceBarang servisBarang = new BarangDAO();
    private final ServiceFasilitas servis = new FasilitasDAO();
    private final ServiceFasilitasDetail servisDetail = new FasilitasDetailDAO();
    
    private final Fasilitas model;
    private final FormFasilitas formFasilitas;
    private Integer idFasilitas;
    private final User loggedInUser;
    
    public FormInputFasilitas(Fasilitas model, FormFasilitas formFasilitas) {
        init();
        
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formFasilitas = formFasilitas;
        if (model != null) {
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 500","[50][fill,grow]"));
        
        JLabel lbNama = new JLabel("Nama Fasilitas");
        JLabel lbBarang = new JLabel("Barang");
        
        txtNama = new JTextField();
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama Fasilitas");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        panelCheckBox = new JPanel(new MigLayout("fillx, insets 10, wrap 1", "[grow,fill][50]"));
        panelCheckBox.setBackground(Color.WHITE);

        List<Barang> barangList = servisBarang.getData("Aktif");
        for (Barang barang : barangList) {
            JPanel itemPanel = new JPanel(new MigLayout("fillx, insets 0", "[grow,fill][50]"));
            itemPanel.setOpaque(false);
            String label = barang.getNamaBarang() + " (" + barang.getSatuan() + ")";
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.setActionCommand(String.valueOf(barang.getIdBarang()));

            JTextField txtJumlah = new JTextField(5);
            txtJumlah.setEnabled(false);

            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    txtJumlah.setEnabled(true);
                } else {
                    txtJumlah.setText("");
                    txtJumlah.setEnabled(false);
                }
            });

            checkBoxes.add(checkBox);
            textFields.add(txtJumlah);

            itemPanel.add(checkBox, "growx");
            itemPanel.add(txtJumlah, "w 50!");

            panelCheckBox.add(itemPanel, "growx, wrap");
        }

        JScrollPane scrollPane = new JScrollPane(panelCheckBox);
        
        
        btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);");
        
        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:@accentColor;"
                + "background:rgb(255,255,255);");
        
        add(createSeparator(), "span, growx, height 2!");
        add(lbNama,"align right");
        add(txtNama);
        add(lbBarang,"align right top");
        add(scrollPane);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");
                
        btnSave.addActionListener((e) -> {
            if(model == null){
                insertData();
            }else{
                updateData();
            }
        });
        
        btnCancel.addActionListener((e) -> {
            if(model == null){
                ModalDialog.closeModal("form input");
            }else{
                ModalDialog.closeModal("form update");
            }
        });
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    //----- Save data to database
    private boolean validasiInput() {
        boolean valid = false;
        if (txtNama.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Nama Fasilitas tidak boleh kosong", getOptionAlert());
        } else {
            valid = true;
        }
        
        return valid;
    }

    private void insertData() {
    if (validasiInput()) {
        String namaFasilitas = txtNama.getText();

        // Cek apakah nama fasilitas sudah ada
        if (servis.isNamaFasilitasExist(namaFasilitas)) {
            Toast.show(this, Toast.Type.INFO, "Nama Fasilitas sudah ada, tidak bisa menambahkan fasilitas dengan nama yang sama", getOptionAlert());
            return;
        }

        Fasilitas modelFasilitas = new Fasilitas();
        modelFasilitas.setNamaFasilitas(namaFasilitas);
        modelFasilitas.setInsertBy(loggedInUser.getIdUser());

        servis.insertData(modelFasilitas);

        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox checkBox = checkBoxes.get(i);
            JTextField txtJumlah = textFields.get(i);

            if (checkBox.isSelected()) {
                int idBarang = Integer.parseInt(checkBox.getActionCommand());
                String jumlahText = txtJumlah.getText();
                int jumlah = jumlahText.isEmpty() ? 0 : Integer.parseInt(jumlahText);

                FasilitasDetail modelDetail = new FasilitasDetail();
                Fasilitas modelFas = new Fasilitas();
                modelFas.setIdFasilitas(modelFasilitas.getIdFasilitas());

                Barang modelBarang = new Barang();
                modelBarang.setIdBarang(idBarang);

                modelDetail.setJumlah(jumlah);
                modelDetail.setFasilitas(modelFas);
                modelDetail.setBarang(modelBarang);

                servisDetail.insertData(modelDetail);
            }
        }

        Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

        formFasilitas.refreshTable();
        resetForm();
    }
}

    
    private void loadData() {
        btnSave.setText("Update");
        
        idFasilitas = (model.getIdFasilitas());
        txtNama.setText(model.getNamaFasilitas());
        
        List<FasilitasDetail> fasilitasDetailList = servisDetail.getData(idFasilitas);
        Map<Integer, Integer> listBarangSelected = new HashMap<>();
        for (FasilitasDetail detail : fasilitasDetailList) {
            listBarangSelected.put(detail.getBarang().getIdBarang(), detail.getJumlah());
        }
        
        panelCheckBox.removeAll();
        checkBoxes.clear();
        textFields.clear();
        
        List<Barang> barangList = servisBarang.getData("Aktif");
    
        for (Barang barang : barangList) {
            JPanel itemPanel = new JPanel(new MigLayout("fillx, insets 5", "[grow]10[50]"));
            itemPanel.setOpaque(false);
            JCheckBox checkBox = new JCheckBox(barang.getNamaBarang() + " (" + barang.getSatuan() + ")");
            checkBox.setActionCommand(String.valueOf(barang.getIdBarang()));

            JTextField txtJumlah = new JTextField(5);
            txtJumlah.setEnabled(false);

            if (listBarangSelected.containsKey(barang.getIdBarang())) {
                checkBox.setSelected(true);
                txtJumlah.setText(String.valueOf(listBarangSelected.get(barang.getIdBarang())));
                txtJumlah.setEnabled(true);
            }

            checkBox.addActionListener(e -> {
                boolean selected = checkBox.isSelected();
                txtJumlah.setEnabled(selected);
                if (!selected) {
                    txtJumlah.setText("");
                }
            });

            checkBoxes.add(checkBox);
            textFields.add(txtJumlah);
            
            itemPanel.add(checkBox, "grow");
            itemPanel.add(txtJumlah, "w 50!");
            panelCheckBox.add(itemPanel, "wrap");
        }

        panelCheckBox.revalidate();
        panelCheckBox.repaint();
    }
    
   private void updateData() {
    if (validasiInput()) {
        String namaFasilitas = txtNama.getText();

        // Cek apakah nama fasilitas sudah ada untuk fasilitas lain
        if (servis.isNamaFasilitasExistForUpdate(namaFasilitas, idFasilitas)) {
            Toast.show(this, Toast.Type.INFO, "Nama Fasilitas sudah ada, tidak bisa memperbarui fasilitas dengan nama yang sama", getOptionAlert());
            return;
        }

        // Update fasilitas utama
        Fasilitas modelFasilitas = new Fasilitas();
        modelFasilitas.setIdFasilitas(idFasilitas);
        modelFasilitas.setNamaFasilitas(namaFasilitas);
        modelFasilitas.setUpdateBy(loggedInUser.getIdUser());

        servis.updateData(modelFasilitas);

        servisDetail.deleteData(idFasilitas);

        // Tambahkan data baru ke fasilitas_detail
        for (int i = 0; i < checkBoxes.size(); i++) {
            JCheckBox checkBox = checkBoxes.get(i);
            JTextField txtJumlah = textFields.get(i);

            if (checkBox.isSelected()) {
                int idBarang = Integer.parseInt(checkBox.getActionCommand());
                String jumlahText = txtJumlah.getText();
                int jumlah = jumlahText.isEmpty() ? 0 : Integer.parseInt(jumlahText);

                FasilitasDetail modelDetail = new FasilitasDetail();
                modelDetail.setFasilitas(modelFasilitas);

                Barang modelBarang = new Barang();
                modelBarang.setIdBarang(idBarang);
                modelDetail.setBarang(modelBarang);
                modelDetail.setJumlah(jumlah);

                // Masukkan ke database
                servisDetail.insertData(modelDetail);
            }
        }

        Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

        formFasilitas.refreshTable();
        resetForm();
        ModalDialog.closeModal("form update");
    }
}


    private void resetForm() {
        txtNama.setText("");
        
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setSelected(false);
            textFields.get(i).setText("");
            textFields.get(i).setEnabled(false);
        }
    }
}