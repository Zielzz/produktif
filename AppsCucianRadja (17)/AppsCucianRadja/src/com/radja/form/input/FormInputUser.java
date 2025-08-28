package com.radja.form.input;

import com.radja.dao.UserDAO;
import com.radja.form.FormUser;
import com.radja.main.FormManager;
import com.radja.model.User;
import com.radja.service.ServiceUser;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;

public class FormInputUser extends JPanel {

    private JLabel lbPassword;
    private JTextField txtNama;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbxRole;
    private JTextField txtRfid;
    private JButton btnGenerate;
    
    private JButton btnSave;
    private JButton btnCancel;
    
    private final ServiceUser servis = new UserDAO();
    private User model;
    private FormUser formUser;  
    private int idUser;
    private User loggedInUser;
    
    public FormInputUser(User model, FormUser formUser) {
        init();
        
        this.loggedInUser = FormManager.getLoggedInUser();
        this.model = model;
        this.formUser = formUser;
        if (model != null) {
            loadData();
        }
    }

    private void init() {
        setLayout(new MigLayout("fillx, insets 5 30 5 30, wrap 2, gap 20, width 400","[50][fill,grow]"));

        lbPassword = new JLabel("Password");
        txtNama = new JTextField();
        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        cbxRole = new JComboBox<>();
        txtRfid = new JTextField();
        btnGenerate = new JButton("Generate");

        // Set properties
        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan nama");
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan username");
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan password");
        txtRfid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan RFID");

        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtRfid.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.STYLE, 
            "showRevealButton:true;showCapsLock:true;");

        btnSave = new JButton("Save");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.putClientProperty(FlatClientProperties.STYLE, 
            "background:@accentColor;foreground:rgb(255,255,255);");

        btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/cancel.svg", 0.4f));
        btnCancel.setIconTextGap(5);

        btnGenerate.setIcon(new FlatSVGIcon("com/radja/icon/refresh.svg", 0.4f));
        btnGenerate.addActionListener(e -> {
            txtPassword.setText(generateRandomPassword(8));
        });

        // Add components to layout
        add(createSeparator(), "span, growx, height 2!");
        add(new JLabel("Full Name"),"align right");
        add(txtNama);
        add(new JLabel("Username"),"align right");
        add(txtUsername);
        add(new JLabel("Password"),"align right");
        add(txtPassword, "split 2, hmin 30, growx");
        add(btnGenerate, "wrap");
        add(new JLabel("Role"),"align right");
        add(cbxRole);
        add(new JLabel("RFID"),"align right");
        add(txtRfid);
        add(createSeparator(), "span, growx, height 2!");
        add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        add(btnCancel, "sg btn, hmin 30");

        // Event listeners
        btnSave.addActionListener(e -> {
            if(model == null) {
                insertData();
            } else {
                updateData();
            }
        });

        btnCancel.addActionListener(e -> {
            if(model == null) {
                ModalDialog.closeModal("form input");
            } else {
                ModalDialog.closeModal("form update");
            }
        });

        initComboItem(cbxRole);
        setColorCbx();
    }
    
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }
    
    private void initComboItem(JComboBox<String> cbx) {
        cbx.addItem("Select Role");
        cbx.addItem("Owner");
        cbx.addItem("Kasir");
    }
    
    private void setColorBasedOnSelection() {
        if (cbxRole.getSelectedItem().equals("Select Role")) {
            cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:$Label.disabledForeground;");
        } else {
            cbxRole.putClientProperty(FlatClientProperties.STYLE, "foreground:$TextField.foreground;");
        }
    }

    private void setColorCbx() {
        cbxRole.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                setColorBasedOnSelection();
            }
        });

        cbxRole.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setColorBasedOnSelection();
            }
        });

        setColorBasedOnSelection();
    }

    private boolean validasiInput(boolean validatePassword, boolean isUpdate) {
        boolean valid = false;
        String username = txtUsername.getText().trim();
        String currentUsername = isUpdate ? model.getUsername() : null;

        if (txtNama.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the name", getOptionAlert());
        } else if (txtUsername.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
        } else if (validatePassword && txtPassword.getPassword().length == 0) {
            Toast.show(this, Toast.Type.INFO, "Please enter the password", getOptionAlert());
        } else if (cbxRole.getSelectedItem().equals("Select Role")) {
            Toast.show(this, Toast.Type.INFO, "Please select a role", getOptionAlert());
        } else if (txtRfid.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the RFID", getOptionAlert());
        } else {
            if (isUpdate && username.equals(currentUsername)) {
                valid = true;
            } else {
                User modelUser = new User();
                modelUser.setUsername(username);
                if (servis.validateUsername(modelUser)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING, 
                            "This username is already taken\nPlease choose another one",
                            getOptionAlert());
                }
            }
        }
        return valid;
    }

    private void insertData() {
        if(validasiInput(true, false)) {
            String nama = txtNama.getText();
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String role = cbxRole.getSelectedItem().toString();
            String rfid = txtRfid.getText();

            User modelUser = new User();
            modelUser.setNama(nama);
            modelUser.setUsername(username);
            modelUser.setPassword(password);
            modelUser.setRole(role);
            modelUser.setRfid(rfid);
            modelUser.setInsertBy(loggedInUser.getIdUser());

            servis.insertData(modelUser);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully added", getOptionAlert());

            formUser.refreshTable();
            resetForm();
        }
    }
    
    private void updateData() {
        if(validasiInput(false, true)) {
            String nama = txtNama.getText();
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String role = cbxRole.getSelectedItem().toString();
            String rfid = txtRfid.getText();

            User modelUser = new User();
            modelUser.setIdUser(idUser);
            modelUser.setNama(nama);
            modelUser.setUsername(username);
            modelUser.setRole(role);
            modelUser.setRfid(rfid);
            modelUser.setUpdateBy(loggedInUser.getIdUser());

            // Only update password if field is not empty
            if (password.length() > 0) {
                modelUser.setPassword(password);
            }

            servis.updateData(modelUser);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());

            formUser.refreshTable();
            ModalDialog.closeModal("form update");
        }
    }
    
    private void loadData() {
        btnSave.setText("Update");
        idUser = model.getIdUser();
        txtNama.setText(model.getNama());
        txtUsername.setText(model.getUsername());
        cbxRole.setSelectedItem(model.getRole());
        txtRfid.setText(model.getRfid());
        
        // Auto-generate random password for Kasir role
        if (model.getRole().equals("Kasir")) {
            txtPassword.setText(generateRandomPassword(8));
            txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password generated");
        } else {
            txtPassword.setText("");
            txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Leave empty to keep current");
        }
    }
    
    private void resetForm() {
        txtNama.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbxRole.setSelectedIndex(0);
        txtRfid.setText("");
    }
}