package com.radja.form.input;

import com.radja.dao.UserDAO;
import com.radja.model.User;
import com.radja.service.ServiceUser;
import com.formdev.flatlaf.FlatClientProperties;
import com.radja.main.Form;
import com.radja.main.FormManager;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class FormChangePassword extends Form{

    private final ServiceUser servis = new UserDAO();
    
    private final JTextField txtUsername = new JTextField();
    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    
    private final User loggedInUser;
    
    public FormChangePassword() {
        this.loggedInUser = FormManager.getLoggedInUser();
        init();
    }

    @Override
    public void formOpen() {
        super.formOpen();
        loadData();
    }
    
    private void init() {
        setLayout(new MigLayout("fillx,wrap", "[fill]","[][][fill, grow]"));
        add(setInfo());
        add(createSeparator(), "span, growx, height 2!, gapx 10 10");
        add(setButton());
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
        
        JLabel lbTitle = new JLabel("Change Password");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold 18");
        
        panel.add(lbTitle);
        return panel;
    }
    
    private void reloadLoggedInUser(){
        User updatedUser = servis.getUserById(loggedInUser.getIdUser());
        loggedInUser.setUsername(updatedUser.getUsername());
    }
    
     private void loadData(){
        txtUsername.setEditable(false);
        txtUsername.setText(loggedInUser.getUsername());
        txtOldPassword.setText("");
        txtNewPassword.setText("");
        txtConfirmPassword.setText("");
    }
    
    private JPanel setButton(){
        JPanel panel = new JPanel(new MigLayout("wrap, insets 20, gap 20","[50][400, fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:10");
        
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        
        txtOldPassword = new JPasswordField();
        txtOldPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the old password");
        txtOldPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtOldPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
        txtNewPassword = new JPasswordField();
        txtNewPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the new password");
        txtNewPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtNewPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter the confirm password");
        txtConfirmPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtConfirmPassword.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "showCapsLock:true;");
        
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
        
        panel.add(new JLabel("Username"),"align right");
        panel.add(txtUsername);
        panel.add(new JLabel("Old Password"),"align right");
        panel.add(txtOldPassword, "hmin 30");
        panel.add(new JLabel("New Password"),"align right");
        panel.add(txtNewPassword, "hmin 30");
        panel.add(new JLabel("Confirmation Password"),"align right");
        panel.add(txtConfirmPassword, "hmin 30");
        //panel.add(createSeparator(), "span, growx, height 2!");
        panel.add(btnSave, "span, split 2, align center, sg btn, hmin 30");
        panel.add(btnCancel, "sg btn, hmin 30");
        return panel;
    }
    
    private boolean validationInput() {
        boolean valid = false;
        if (txtOldPassword.getText().trim().isEmpty() ){
            Toast.show(this, Toast.Type.INFO, "Old password cannot be empty", getOptionAlert());
        }else if (txtNewPassword.getText().trim().isEmpty() ){
            Toast.show(this, Toast.Type.INFO, "New password cannot be empty", getOptionAlert());
        }else if (txtConfirmPassword.getText().trim().isEmpty() ){
            Toast.show(this, Toast.Type.INFO, "Confirmation password cannot be empty", getOptionAlert());
        }else{
            valid = true;
        }
        return valid;
    }
    
    private void updateData(){
        if(validationInput()==true){
            String username = txtUsername.getText();
            String oldPassword = txtOldPassword.getText();
            String newPassword = txtNewPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();

            boolean validOldPassword = servis.validateOldPassword(username, oldPassword);
            if (!validOldPassword) {
                Toast.show(this, Toast.Type.ERROR, "Old password does not match", getOptionAlert());
                return;
            }
            
            if (!newPassword.equals(confirmPassword)) {
                Toast.show(this, Toast.Type.ERROR, "New password and confirm password do not match", getOptionAlert());
                return;
            }

            boolean result = servis.changePassword(username, oldPassword, newPassword);

            if (result) {
                Toast.show(this, Toast.Type.SUCCESS, "Password successfully updated", getOptionAlert());
            } else {
                Toast.show(this, Toast.Type.ERROR, "Failed to change password", getOptionAlert());
            }

            reloadLoggedInUser();
            loadData();
        }
    }
    
    private boolean validateOldPassword(String username, String oldPassword) {
    // Ambil password yang disimpan di database
    String storedPassword = servis.getPasswordByUsername(username);

    // Jika password yang disimpan ada dan cocok dengan password lama yang dimasukkan
    if (storedPassword != null && storedPassword.equals(oldPassword)) {
        return true;  // Password cocok
    } else {
        return false;  // Password tidak cocok
    }
}

}
