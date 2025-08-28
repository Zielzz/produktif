import com.radja.dao.UserDAO;
import com.radja.model.User;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RFIDScanner {
    private JTextField txtFieldRfid;
    private JTextField txtFieldUsername;
    private JPasswordField txtFieldPassword;
    private JButton btnScanRFID;

    public RFIDScanner() {
        // Inisialisasi komponen GUI
        txtFieldRfid = new JTextField(20);
        txtFieldUsername = new JTextField(20);
        txtFieldPassword = new JPasswordField(20);
        btnScanRFID = new JButton("Scan RFID");

        // Nonaktifkan username dan password saat awal
        txtFieldUsername.setEnabled(false);
        txtFieldPassword.setEnabled(false);

        // ActionListener untuk tombol scan RFID
        btnScanRFID.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scanRFID();
            }
        });
    }

    // Simulasikan proses scanning RFID
    private void scanRFID() {
        // Misalnya, RFID di-scan dan menghasilkan nilai "123456789"
        String scannedRFID = "123456789"; // Ganti dengan nilai dari pembaca RFID sebenarnya
        txtFieldRfid.setText(scannedRFID); // Isi otomatis ke txtFieldRfid

        // Nonaktifkan username dan password jika RFID terisi
        txtFieldUsername.setEnabled(false);
        txtFieldPassword.setEnabled(false);

        // Panggil fungsi validasi RFID setelah di-scan
        validateRFID(scannedRFID);
    }

    // Fungsi untuk validasi RFID
    private void validateRFID(String rfid) {
        if (rfid == null || rfid.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "RFID tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Panggil DAO atau service untuk memeriksa RFID di database
            UserDAO userDAO = new UserDAO();
            User user = userDAO.getUserByRFID(rfid);

            if (user != null) {
                // Jika RFID valid, otomatis login
                autoLogin(user);
            } else {
                JOptionPane.showMessageDialog(null, "RFID tidak valid!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat memvalidasi RFID: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fungsi untuk otomatis login
    private void autoLogin(User user) {
        // Isi otomatis username dan password dari data user
        txtFieldUsername.setText(user.getUsername());
        txtFieldPassword.setText(user.getPassword());

        // Tampilkan pesan bahwa login berhasil
        JOptionPane.showMessageDialog(null, "Login berhasil sebagai: " + user.getUsername(), "Success", JOptionPane.INFORMATION_MESSAGE);

        // Lanjutkan ke halaman utama atau fungsi lainnya
        // Contoh: openMainMenu();
    }

    public static void main(String[] args) {
        // Memastikan bahwa GUI dibuat dan dijalankan pada Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                RFIDScanner rfidScanner = new RFIDScanner();
                JFrame frame = new JFrame("RFID Scanner Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new java.awt.FlowLayout());

                // Tambahkan komponen ke frame
                frame.add(new JLabel("RFID:"));
                frame.add(rfidScanner.txtFieldRfid);
                frame.add(rfidScanner.btnScanRFID);
                frame.add(new JLabel("Username:"));
                frame.add(rfidScanner.txtFieldUsername);
                frame.add(new JLabel("Password:"));
                frame.add(rfidScanner.txtFieldPassword);

                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}