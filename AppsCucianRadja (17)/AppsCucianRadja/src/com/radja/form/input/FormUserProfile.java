package com.radja.form.input;

import com.radja.dao.UserDAO;
import com.radja.model.User;
import com.radja.service.ServiceUser;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormUserProfile extends Form {

    private final ServiceUser servis = new UserDAO();

    private JTextField txtNama = new JTextField();
    private JTextField txtUsername = new JTextField();
    private final JComboBox cbxRole = new JComboBox();
    private JTextField txtRfid;

    private final User loggedInUser;

    public FormUserProfile() {
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]", "[][][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(setButton());
    }

    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator();
        separator.putClientProperty(FlatClientProperties.STYLE, "foreground:rgb(206,206,206);");
        return separator;
    }

    private JPanel setInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10");

        JLabel lbTitle = new JLabel("Profile");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold 18");

        panel.add(lbTitle);
        return panel;
    }

    private void reloadLoggedInUser() {
        User updatedUser = servis.getUserById(loggedInUser.getIdUser());
        loggedInUser.setNama(updatedUser.getNama());
        loggedInUser.setUsername(updatedUser.getUsername());
        loggedInUser.setRole(updatedUser.getRole());
        loggedInUser.setRfid(updatedUser.getRfid());
    }

    private void loadData() {
        txtNama.setText(loggedInUser.getNama());
        txtUsername.setText(loggedInUser.getUsername());
        cbxRole.setSelectedItem(loggedInUser.getRole());
        txtRfid.setText(loggedInUser.getRfid());
    }

    private JPanel setButton() {
        JPanel panel = new JPanel(new MigLayout("wrap, insets 20, gap 20", "[50][400, fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10");

        txtNama.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the name");
        txtNama.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        txtRfid = new JTextField();
        txtRfid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter RFID");
        txtRfid.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);

        initComboItem(cbxRole);
        cbxRole.setSelectedItem(loggedInUser.getRole());
        setColorCbx();

        JButton btnSave = new JButton("Save");
        btnSave.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:@accentColor;"
                + "foreground:rgb(255,255,255);");
        btnSave.setIcon(new FlatSVGIcon("com/radja/icon/save_white.svg", 0.4f));
        btnSave.setIconTextGap(5);
        btnSave.addActionListener((e) -> updateData());

        JButton btnCancel = new JButton("Cancel");
        btnCancel.setIcon(new FlatSVGIcon("com/radja/icon/delete.svg", 0.4f));
        btnCancel.setIconTextGap(5);
        btnCancel.addActionListener((e) -> loadData());

        panel.add(new JLabel("Full Name"), "align right");
        panel.add(txtNama);
        panel.add(new JLabel("Username"), "align right");
        panel.add(txtUsername);
        panel.add(new JLabel("Role"), "align right");
        panel.add(cbxRole);
        panel.add(new JLabel("RFID"), "align right");
        panel.add(txtRfid, "hmin 30");
        panel.add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        panel.add(btnCancel, "sg btn, hmin 30");
        return panel;
    }

    private void initComboItem(JComboBox cbx) {
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

    private boolean validasiInput(boolean isUpdate) {
        boolean valid = false;
        String username = txtUsername.getText().trim();
        String rfid = txtRfid.getText().trim();
        String currentUsername = isUpdate ? loggedInUser.getUsername() : null;
        String currentRfid = isUpdate ? loggedInUser.getRfid() : null;

        if (txtNama.getText().trim().isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the name", getOptionAlert());
        } else if (username.isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the username", getOptionAlert());
        } else if (cbxRole.getSelectedItem().equals("Select Role")) {
            Toast.show(this, Toast.Type.INFO, "Please select a role", getOptionAlert());
        } else if (rfid.isEmpty()) {
            Toast.show(this, Toast.Type.INFO, "Please enter the RFID", getOptionAlert());
        } else {
            // Check username uniqueness
            if (isUpdate && username.equals(currentUsername)) {
                valid = true;
            } else {
                User modelUser = new User();
                modelUser.setUsername(username);
                if (!servis.validateUsername(modelUser)) {
                    Toast.show(this, Toast.Type.WARNING,
                            "This username is already taken\nPlease choose another one", getOptionAlert());
                    return false;
                }
            }

            // Check RFID uniqueness
            if (isUpdate && rfid.equals(currentRfid)) {
                valid = true;
            } else {
                User modelUser = new User();
                modelUser.setRfid(rfid);
                modelUser.setIdUser(isUpdate ? loggedInUser.getIdUser() : 0); // Set ID for update check
                if (servis.validateRfid(modelUser)) {
                    valid = true;
                } else {
                    Toast.show(this, Toast.Type.WARNING,
                            "This RFID is already taken\nPlease choose another one", getOptionAlert());
                    return false;
                }
            }
        }
        return valid;
    }

    private void updateData() {
        String selectedRole = cbxRole.getSelectedItem().toString();

        // Cek apakah pengguna tidak boleh mengubah ke Owner/Admin
        if (!loggedInUser.getRole().equalsIgnoreCase("Owner") &&
                (selectedRole.equalsIgnoreCase("Owner") || selectedRole.equalsIgnoreCase("Admin"))) {
            Toast.show(this, Toast.Type.WARNING,
                    "You are not allowed to assign 'Owner' or 'Admin' role", getOptionAlert());
            return;
        }

        if (validasiInput(true)) {
            String nama = txtNama.getText().trim();
            String username = txtUsername.getText().trim();
            String rfid = txtRfid.getText().trim();

            User modelUser = new User();
            modelUser.setIdUser(loggedInUser.getIdUser());
            modelUser.setNama(nama);
            modelUser.setUsername(username);
            modelUser.setRole(selectedRole);
            modelUser.setRfid(rfid);
            modelUser.setUpdateBy(loggedInUser.getIdUser());

            servis.updateData(modelUser);
            Toast.show(this, Toast.Type.SUCCESS, "Data has been successfully updated", getOptionAlert());
            reloadLoggedInUser();
        }
    }
}