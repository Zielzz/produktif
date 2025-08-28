package com.radja.form.input;

import com.radja.dao.LayananDAO;
import com.radja.form.FormLayanan;
import com.radja.main.FormManager;
import com.radja.model.Layanan;
import com.radja.service.ServiceLayanan;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.dao.FasilitasDAO;
import com.radja.dao.LayananDetailDAO;
import com.radja.model.Fasilitas;
import com.radja.model.LayananDetail;
import com.radja.model.User;
import com.radja.service.ServiceFasilitas;
import com.radja.service.ServiceLayananDetail;
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

public class FormInputLayanan extends JPanel{

    private JPanel panelCheckBox;
    private JTextField txtNama;
    private JTextField txtHarga;
    
    List<JCheckBox> checkBoxes = new ArrayList<>();
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private final ServiceFasilitas servisFasilitas = new FasilitasDAO();
    private final ServiceLayanan servis = new LayananDAO();
    private final ServiceLayananDetail servisDetail = new LayananDetailDAO();
    
    private final Layanan model;
    private final FormLayanan formLayanan;
    private Integer idLayanan;
    private final User loggedInUser;
    
    public FormInputLayanan(Layanan model, FormLayanan formLayanan) {
        init();
        
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formLayanan = formLayanan;
        if (model != null) {
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 500","[50][fill,grow]"));
        
        JLabel lbNama = new JLabel("Nama Layanan");
        JLabel lbHarga = new JLabel("Harga Layanan");
        JLabel lbFasilitas = new JLabel("Fasilitas");
        
        txtNama = new JTextField();
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama layanan");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtHarga = new JTextField();
        txtHarga.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan harga layanan");
        txtHarga.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        panelCheckBox = new JPanel(new MigLayout("fillx, insets 10, wrap 1", "[grow,fill][50]"));
        panelCheckBox.setBackground(Color.WHITE);

        List<Fasilitas> FasilitasList = servisFasilitas.getData("Aktif");
        for (Fasilitas Fasilitas : FasilitasList) {
            JPanel itemPanel = new JPanel(new MigLayout("fillx, insets 0", "[grow,fill][50]"));
            itemPanel.setOpaque(false);
            String label = Fasilitas.getNamaFasilitas();
            JCheckBox checkBox = new JCheckBox(label);
            checkBox.setActionCommand(String.valueOf(Fasilitas.getIdFasilitas()));

            checkBoxes.add(checkBox);
            
            itemPanel.add(checkBox, "growx");
            
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
        add(lbHarga,"align right");
        add(txtHarga);
        add(lbFasilitas,"align right top");
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
            Toast.show(this, Toast.Type.INFO, "Nama Layanan tidak boleh kosong", getOptionAlert());
        } else {
            valid = true;
        }
        
        return valid;
    }

    private void insertData(){
        if(validasiInput()==true){
            String namaLayanan   = txtNama.getText();
            Double harga = Double.valueOf(txtHarga.getText());
            
            Layanan modelLayanan = new Layanan();
            modelLayanan.setNamaLayanan(namaLayanan);
            modelLayanan.setHarga(harga);
            modelLayanan.setInsertBy(loggedInUser.getIdUser());
            
            servis.insertData(modelLayanan);
            
            for (int i = 0; i < checkBoxes.size(); i++) {
                JCheckBox checkBox = checkBoxes.get(i);
                
                if (checkBox.isSelected()) {
                    int idFasilitas = Integer.parseInt(checkBox.getActionCommand());
                
                    LayananDetail modelDetail = new LayananDetail();
                    Layanan modelFas = new Layanan();
                    modelFas.setIdLayanan(modelLayanan.getIdLayanan());

                    Fasilitas modelFasilitas = new Fasilitas();
                    modelFasilitas.setIdFasilitas(idFasilitas);

                    modelDetail.setLayanan(modelFas);
                    modelDetail.setFasilitas(modelFasilitas);

                    servisDetail.insertData(modelDetail);
                }
            }
            
            
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());
            
            formLayanan.refreshTable();
            resetForm();
        }
    }
    
    private void loadData() {
        btnSave.setText("Update");
        
        idLayanan = (model.getIdLayanan());
        txtNama.setText(model.getNamaLayanan());
        txtHarga.setText(String.valueOf(model.getHarga()).replace(",", "").replace(".0", ""));
        
        List<LayananDetail> layananDetailList = servisDetail.getData(idLayanan);
        
        Map<Integer, String> listFasilitasSelected = new HashMap<>();
        
        for (LayananDetail detail : layananDetailList) {
            listFasilitasSelected.put(detail.getFasilitas().getIdFasilitas(), detail.getFasilitas().getNamaFasilitas());
        }
        
        panelCheckBox.removeAll();
        checkBoxes.clear();
        
        List<Fasilitas> FasilitasList = servisFasilitas.getData("Aktif");
    
        for (Fasilitas Fasilitas : FasilitasList) {
            JPanel itemPanel = new JPanel(new MigLayout("fillx, insets 5", "[grow]10[50]"));
            itemPanel.setOpaque(false);
            JCheckBox checkBox = new JCheckBox(Fasilitas.getNamaFasilitas());
            checkBox.setActionCommand(String.valueOf(Fasilitas.getIdFasilitas()));

            if (listFasilitasSelected.containsKey(Fasilitas.getIdFasilitas())) {
                checkBox.setSelected(true);
            }

            checkBoxes.add(checkBox);
            
            itemPanel.add(checkBox, "grow");
            panelCheckBox.add(itemPanel, "wrap");
        }

        panelCheckBox.revalidate();
        panelCheckBox.repaint();
    }
    
    private void updateData() {
        if (validasiInput()) {
            String namaLayanan = txtNama.getText();
            Double harga = Double.valueOf(txtHarga.getText());
            
            // Update Layanan utama
            Layanan modelLayanan = new Layanan();
            modelLayanan.setIdLayanan(idLayanan);
            modelLayanan.setNamaLayanan(namaLayanan);
            modelLayanan.setHarga(harga);
            modelLayanan.setUpdateBy(loggedInUser.getIdUser());

            servis.updateData(modelLayanan);

            servisDetail.deleteData(idLayanan);

            // Tambahkan data baru ke Layanan_detail
            for (int i = 0; i < checkBoxes.size(); i++) {
                JCheckBox checkBox = checkBoxes.get(i);
                
                if (checkBox.isSelected()) {
                    int idFasilitas = Integer.parseInt(checkBox.getActionCommand());
                    
                    LayananDetail modelDetail = new LayananDetail();
                    modelDetail.setLayanan(modelLayanan);

                    Fasilitas modelFasilitas = new Fasilitas();
                    modelFasilitas.setIdFasilitas(idFasilitas);
                    modelDetail.setFasilitas(modelFasilitas);
                    
                    // Masukkan ke database
                    servisDetail.insertData(modelDetail);
                }
            }

            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

            formLayanan.refreshTable();
            resetForm();
            ModalDialog.closeModal("form update");
        }
    }

    private void resetForm() {
        txtNama.setText("");
        txtHarga.setText("");
        
        for (int i = 0; i < checkBoxes.size(); i++) {
            checkBoxes.get(i).setSelected(false);
        }
    }
}
