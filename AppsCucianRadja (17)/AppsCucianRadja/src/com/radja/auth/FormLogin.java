package com.radja.auth;

import com.radja.dao.AbsensiDAOImpl;
import com.radja.dao.UserDAO;
import com.radja.main.Form;
import com.radja.main.FormManager;
import com.radja.model.Karyawan;
import com.radja.model.User;
import com.radja.service.ServiceAbsensi;
import com.radja.service.ServiceUser;
import com.radja.report.ReportAbsensi;
import static com.radja.util.AlertUtils.getOptionAlert;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.radja.form.FormDashboard;
import com.radja.model.Absensi;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;
import java.util.logging.Logger;
import java.util.logging.Level;

public class FormLogin extends Form {

    private final ServiceUser userService = new UserDAO();
    private final ServiceAbsensi absensiService = new AbsensiDAOImpl();
    private static final Logger LOGGER = Logger.getLogger(FormLogin.class.getName());
    private JLabel imageLogo;
    private JPanel panelForm;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JTextField txtRfid;
    private JButton btnLogin;
    private JButton btnMasuk;
    private JButton btnPulang;
    private JButton btnIzinSakit;
    private JCheckBox chkLoginWithRfid;
    private boolean isMasuk;
    private final JFrame mainFrame; // Menyimpan referensi ke JFrame

    public FormLogin(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        init();
        setActionButton();
        SwingUtilities.invokeLater(() -> txtRfid.requestFocusInWindow());
    }

    private void init() {
        setLayout(new MigLayout("fill, insets 20", "[center]", "[center]"));

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1000, 625));
        layeredPane.setLayout(null);

        imageLogo = new JLabel();
        ImageIcon bgIcon = new ImageIcon(new ImageIcon(getClass().getResource("/com/radja/icon/cuci2.png"))
                .getImage().getScaledInstance(1000, 625, Image.SCALE_SMOOTH));
        imageLogo.setIcon(bgIcon);
        imageLogo.setBounds(0, 0, 1000, 625);
        layeredPane.add(imageLogo, JLayeredPane.DEFAULT_LAYER);

        panelForm = new JPanel(new MigLayout("wrap, insets 20", "fill, 280:450"));
        panelForm.putClientProperty(FlatClientProperties.STYLE, "arc:20; [light]background:rgb(255,255,255);");
        panelForm.setOpaque(false);
        panelForm.setBounds(540, 45, 430, 560);
        layeredPane.add(panelForm, JLayeredPane.PALETTE_LAYER);

        JLabel lbLogo = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/com/radja/icon/Logo.png"))
                .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)));
        lbLogo.setHorizontalAlignment(JLabel.RIGHT);

        JLabel lbTitleApp = new JLabel("RADJA CAR WASH", JLabel.CENTER);
        lbTitleApp.putClientProperty(FlatClientProperties.STYLE, "font:bold 20; foreground:@accentColor");

        JLabel lbTitleForm = new JLabel("Login & Absensi", JLabel.CENTER);
        lbTitleForm.putClientProperty(FlatClientProperties.STYLE, "font:bold +10; foreground:@accentColor");

        JLabel lbDescription = new JLabel("Silakan login atau lakukan absensi", JLabel.CENTER);
        lbDescription.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        JLabel lbUsername = new JLabel("Username");
        lbUsername.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        txtUsername = new JTextField();
        txtUsername.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Username");
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtUsername.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("com/radja/icon/username.svg", 25, 25));
        txtUsername.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        JLabel lbPassword = new JLabel("Password");
        lbPassword.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        txtPassword = new JPasswordField();
        txtPassword.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Password");
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtPassword.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("com/radja/icon/password.svg", 25, 25));
        txtPassword.putClientProperty(FlatClientProperties.STYLE, "arc:10; showRevealButton:true; showCapsLock:true");

        JLabel lbRfid = new JLabel("RFID (Untuk Login/Absensi)");
        lbRfid.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        txtRfid = new JTextField();
        txtRfid.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Scan RFID");
        txtRfid.putClientProperty(FlatClientProperties.TEXT_FIELD_SHOW_CLEAR_BUTTON, true);
        txtRfid.putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_ICON, new FlatSVGIcon("com/radja/icon/rfid.svg", 25, 25));
        txtRfid.putClientProperty(FlatClientProperties.STYLE, "arc:10");

        chkLoginWithRfid = new JCheckBox("Login dengan RFID");
        chkLoginWithRfid.putClientProperty(FlatClientProperties.STYLE, "foreground:@accentColor");

        btnLogin = new JButton("Login");
        btnLogin.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:#4CAF50;" +
                        "[light]foreground:#FFFFFF;" +
                        "arc:10;" +
                        "font:bold 16");

        btnMasuk = new JButton("Masuk");
        btnMasuk.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:#2196F3;" +
                        "[light]foreground:#FFFFFF;" +
                        "arc:10;" +
                        "font:bold +2");

        btnPulang = new JButton("Pulang");
        btnPulang.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:#FFC107;" +
                        "[light]foreground:#FFFFFF;" +
                        "arc:10;" +
                        "font:bold +2");

        btnIzinSakit = new JButton("Izin/Sakit");
        btnIzinSakit.putClientProperty(FlatClientProperties.STYLE,
                "[light]background:#F44336;" +
                        "[light]foreground:#FFFFFF;" +
                        "arc:10;" +
                        "font:bold +2");

        panelForm.add(lbLogo, "split 2, gapx 30 5");
        panelForm.add(lbTitleApp, "gapx 5 30");
        panelForm.add(new JSeparator(), "gapy 5 5");
        panelForm.add(lbTitleForm);
        panelForm.add(lbDescription);
        panelForm.add(lbUsername, "gapy 8");
        panelForm.add(txtUsername, "hmin 30");
        panelForm.add(lbPassword, "gapy 8");
        panelForm.add(txtPassword, "hmin 30");
        panelForm.add(lbRfid, "gapy 8");
        panelForm.add(txtRfid, "hmin 30");
        panelForm.add(chkLoginWithRfid, "gapy 5");
        panelForm.add(btnLogin, "hmin 30, gapy 15 5, wrap");
        panelForm.add(btnMasuk, "hmin 30, split 3, growx");
        panelForm.add(btnPulang, "hmin 30, growx");
        panelForm.add(btnIzinSakit, "hmin 30, growx");

        add(layeredPane);

        btnLogin.addActionListener(e -> processLogin());
        btnMasuk.addActionListener(e -> processAttendance("HADIR", true));
        btnPulang.addActionListener(e -> processAttendance("HADIR", false));
        btnIzinSakit.addActionListener(e -> processAttendance("TIDAK HADIR", false));

        txtRfid.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (chkLoginWithRfid.isSelected()) {
                        processLogin();
                    } else {
                        processAttendance("HADIR", true);
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (!txtRfid.getText().trim().isEmpty()) {
                    txtUsername.setEnabled(false);
                    txtPassword.setEnabled(false);
                } else {
                    txtUsername.setEnabled(true);
                    txtPassword.setEnabled(true);
                }
            }
        });

        chkLoginWithRfid.addActionListener(e -> {
            if (chkLoginWithRfid.isSelected()) {
                btnMasuk.setEnabled(false);
                btnPulang.setEnabled(false);
                btnIzinSakit.setEnabled(false);
            } else {
                btnMasuk.setEnabled(true);
                btnPulang.setEnabled(true);
                btnIzinSakit.setEnabled(true);
            }
        });
    }

    private void setActionButton() {
        KeyAdapter enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    processLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyListener);
        txtPassword.addKeyListener(enterKeyListener);
    }

    private boolean validasiInput() {
        if (chkLoginWithRfid.isSelected()) {
            if (txtRfid.getText().trim().isEmpty()) {
                showToastError("Silakan scan RFID terlebih dahulu");
                txtRfid.putClientProperty("JComponent.outline", "error");
                txtRfid.requestFocus();
                return false;
            }
            txtRfid.putClientProperty("JComponent.outline", null);
            return true;
        }
        if (txtUsername.getText().trim().isEmpty()) {
            showToastError("Username tidak boleh kosong");
            txtUsername.putClientProperty("JComponent.outline", "error");
            txtUsername.requestFocus();
            return false;
        }
        if (txtPassword.getPassword().length == 0) {
            showToastError("Password tidak boleh kosong");
            txtPassword.putClientProperty("JComponent.outline", "error");
            txtPassword.requestFocus();
            return false;
        }
        txtUsername.putClientProperty("JComponent.outline", null);
        txtPassword.putClientProperty("JComponent.outline", null);
        return true;
    }

    private boolean validateAttendance(String rfid, String status, Karyawan karyawan, LocalDateTime waktuAbsensi) {
        if (rfid == null || rfid.trim().isEmpty()) {
            showToastError("RFID tidak boleh kosong. Silakan scan RFID terlebih dahulu.");
            txtRfid.requestFocus();
            return false;
        }

        if (karyawan == null) {
            showToastError("Karyawan dengan RFID " + rfid + " tidak ditemukan.");
            txtRfid.setText("");
            txtRfid.requestFocus();
            return false;
        }

        if (!status.equals("HADIR") && !status.equals("TIDAK HADIR")) {
            showToastError("Status absensi tidak valid: " + status);
            return false;
        }

        if (waktuAbsensi == null) {
            showToastError("Waktu absensi tidak valid.");
            return false;
        }

        if (status.equals("HADIR")) {
            LocalTime waktuSekarang = waktuAbsensi.toLocalTime();
            if (isMasuk && waktuSekarang.isBefore(LocalTime.of(7, 0))) {
                showToastError("Absen masuk hanya diizinkan setelah pukul 7:00.");
                return false;
            }
            if (!isMasuk && waktuSekarang.isAfter(LocalTime.of(22, 59))) {
                showToastError("Absen pulang hanya diizinkan sebelum pukul 23:00.");
                return false;
            }
        }

        try {
            if (status.equals("HADIR")) {
                if (!isMasuk && !absensiService.isSudahAbsenMasuk(karyawan.getIdKaryawan(), LocalDate.now())) {
                    showToastError("Tidak dapat absen pulang karena belum absen masuk hari ini.");
                    return false;
                }
                if (absensiService.isSudahAbsenPulang(karyawan.getIdKaryawan(), LocalDate.now())) {
                    showToastError("Anda sudah absen pulang hari ini.");
                    return false;
                }
                if (absensiService.isSudahIzinSakit(karyawan.getIdKaryawan(), LocalDate.now())) {
                    showToastError("Tidak dapat absen hadir karena sudah mengajukan izin/sakit hari ini.");
                    return false;
                }
            } else if (status.equals("TIDAK HADIR")) {
                if (absensiService.isSudahIzinSakit(karyawan.getIdKaryawan(), LocalDate.now())) {
                    showToastError("Anda sudah mengajukan izin/sakit hari ini.");
                    return false;
                }
                if (absensiService.isSudahAbsenMasuk(karyawan.getIdKaryawan(), LocalDate.now()) ||
                        absensiService.isSudahAbsenPulang(karyawan.getIdKaryawan(), LocalDate.now())) {
                    showToastError("Tidak dapat mengajukan izin/sakit karena sudah absen hadir hari ini.");
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saat memeriksa status absensi untuk karyawan " + (karyawan != null ? karyawan.getIdKaryawan() : "null"), e);
            showToastError("Gagal memeriksa status absensi: " + e.getMessage());
            return false;
        }

        return true;
    }

    private void processLogin() {
        if (!isDisplayable() || mainFrame == null) {
            LOGGER.log(Level.WARNING, "FormLogin belum ditampilkan atau mainFrame null. Tidak dapat menampilkan toast.");
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "Form belum siap. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        if (!validasiInput()) {
            return;
        }

        FormManager.logout();

        String rfid = txtRfid.getText().trim();

        try {
            if (!rfid.isEmpty() && chkLoginWithRfid.isSelected()) {
                User user = userService.getUserByRFID(rfid);
                if (user != null) {
                    FormManager.login(user);
                    resetForm();
                    showToastSuccess("Login dengan RFID berhasil. Selamat datang, " + user.getRole() + ".");
                    if ("Owner".equals(user.getRole())) {
                        showOwnerDashboard(user);
                    } else if ("Kasir".equals(user.getRole())) {
                        showKasirDashboard(user);
                    }
                } else {
                    showToastError(((UserDAO) userService).getLastErrorMessage());
                    txtRfid.setText("");
                    txtRfid.requestFocus();
                }
            } else {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword());

                User userLogin = new User();
                userLogin.setUsername(username);
                userLogin.setPassword(password);

                User user = userService.processLogin(userLogin);

                if (user != null) {
                    FormManager.login(user);
                    resetForm();
                    showToastSuccess("Login berhasil. Selamat datang, " + user.getRole() + ".");
                    if ("Owner".equals(user.getRole())) {
                        showOwnerDashboard(user);
                    } else if ("Kasir".equals(user.getRole())) {
                        showKasirDashboard(user);
                    }
                } else {
                    showToastError(((UserDAO) userService).getLastErrorMessage());
                    txtUsername.setText("");
                    txtPassword.setText("");
                    txtUsername.requestFocus();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saat proses login untuk RFID/username: " + (rfid.isEmpty() ? txtUsername.getText() : rfid), e);
            showToastError("Gagal melakukan login: " + e.getMessage());
        }
    }

    private void processAttendance(String status, boolean isMasuk) {
        if (!isDisplayable() || mainFrame == null) {
            LOGGER.log(Level.WARNING, "FormLogin belum ditampilkan atau mainFrame null. Tidak dapat menampilkan toast.");
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, "Form belum siap. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            });
            return;
        }

        this.isMasuk = isMasuk;
        String rfid = txtRfid.getText().trim();
        Karyawan karyawan = userService.getKaryawanByRfid(rfid);
        LocalDateTime waktuAbsensi = LocalDateTime.now();

        if (!validateAttendance(rfid, status, karyawan, waktuAbsensi)) {
            return;
        }

        String keterangan = "";
        String finalStatus = status;
        if (status.equals("TIDAK HADIR")) {
            String[] options = {"Izin", "Sakit"};
            int choice = JOptionPane.showOptionDialog(
                    mainFrame,
                    "Pilih jenis absensi:",
                    "Izin/Sakit",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == JOptionPane.CLOSED_OPTION) {
                showToastError("Proses Izin/Sakit dibatalkan");
                txtRfid.setText("");
                txtRfid.requestFocus();
                return;
            }
            String jenis = options[choice];

            JTextField keteranganField = new JTextField();
            Object[] message = {
                    "Masukkan keterangan untuk " + jenis.toLowerCase() + ":", keteranganField
            };
            int option = JOptionPane.showConfirmDialog(
                    mainFrame,
                    message,
                    "Keterangan " + jenis,
                    JOptionPane.OK_CANCEL_OPTION
            );
            if (option != JOptionPane.OK_OPTION || keteranganField.getText().trim().isEmpty()) {
                showToastError("Keterangan wajib diisi");
                txtRfid.setText("");
                txtRfid.requestFocus();
                return;
            }
            keterangan = jenis + ": " + keteranganField.getText().trim();
            finalStatus = "TIDAK HADIR";
        }

        try {
            boolean success;
            String message = "";
            if (finalStatus.equals("HADIR")) {
                if (isMasuk) {
                    success = absensiService.checkIn(karyawan.getIdKaryawan());
                    if (success) {
                        message = "Absensi Masuk berhasil";
                        showToastSuccess(message);
                    } else {
                        showToastError("Gagal mencatat absen masuk: " + ((AbsensiDAOImpl) absensiService).getLastErrorMessage());
                    }
                } else {
                    success = absensiService.checkOut(karyawan.getIdKaryawan());
                    if (success) {
                        Absensi latestAbsensi = ((AbsensiDAOImpl) absensiService).getLatestAbsensiForKaryawan(karyawan.getIdKaryawan(), LocalDate.now());
                        if (latestAbsensi != null) {
                            message = "Absensi Pulang berhasil. " + latestAbsensi.getFormattedDurasi();
                        } else {
                            message = "Absensi Pulang berhasil. Durasi kerja tidak tersedia.";
                        }
                        showToastSuccess(message);
                    } else {
                        showToastError("Gagal mencatat absen pulang: " + ((AbsensiDAOImpl) absensiService).getLastErrorMessage());
                    }
                }
            } else {
                success = absensiService.catatIzinSakit(karyawan.getIdKaryawan(), keterangan.split(":")[0], keterangan);
                if (success) {
                    message = "Izin/Sakit berhasil dicatat";
                    showToastSuccess(message);
                } else {
                    showToastError("Gagal mencatat izin/sakit: " + ((AbsensiDAOImpl) absensiService).getLastErrorMessage());
                }
            }

            if (success) {
                txtRfid.setText("");
                txtRfid.requestFocus();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error saat mencatat absensi untuk RFID: " + rfid, e);
            showToastError("Terjadi kesalahan saat mencatat absensi: " + e.getMessage());
            txtRfid.setText("");
            txtRfid.requestFocus();
        }
    }

    private String formatDuration(double durasiInHours) {
        long totalMinutes = (long) (durasiInHours * 60);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%d jam %d menit", hours, minutes);
    }

    private void showKasirDashboard(User user) {
        if ("Kasir".equals(user.getRole())) {
            FormManager.showForm(new FormDashboard());
            JOptionPane.showMessageDialog(mainFrame, "Menampilkan dashboard untuk Kasir ID: " + user.getIdKaryawan());
        }
    }

    private void showOwnerDashboard(User user) {
        if ("Owner".equals(user.getRole())) {
            FormManager.showForm(new ReportAbsensi());
            JOptionPane.showMessageDialog(mainFrame, "Menampilkan laporan absensi untuk Owner");
        }
    }

    private void showAbsensiReport() {
        FormManager.showForm(new ReportAbsensi());
    }

    private void showToastError(String message) {
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> {
                Toast.show(mainFrame, Toast.Type.ERROR, message, getOptionAlert());
            });
        } else {
            LOGGER.log(Level.WARNING, "Main frame tidak tersedia. Menggunakan JOptionPane sebagai fallback.");
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }

    private void showToastSuccess(String message) {
        if (mainFrame != null) {
            SwingUtilities.invokeLater(() -> {
                Toast.show(mainFrame, Toast.Type.SUCCESS, message, getOptionAlert());
            });
        } else {
            LOGGER.log(Level.WARNING, "Main frame tidak tersedia. Menggunakan JOptionPane sebagai fallback.");
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }

    private void resetForm() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtRfid.setText("");
        txtUsername.setEnabled(true);
        txtPassword.setEnabled(true);
        btnMasuk.setEnabled(!chkLoginWithRfid.isSelected());
        btnPulang.setEnabled(!chkLoginWithRfid.isSelected());
        btnIzinSakit.setEnabled(!chkLoginWithRfid.isSelected());
        txtRfid.requestFocus();
    }
}