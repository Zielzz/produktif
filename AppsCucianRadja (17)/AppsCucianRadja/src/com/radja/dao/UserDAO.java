package com.radja.dao;

import com.radja.config.Koneksi;
import com.radja.model.Karyawan;
import com.radja.model.User;
import com.radja.service.ServiceUser;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO implements ServiceUser {
    private final Connection conn;

    public UserDAO() {
        this.conn = Koneksi.getConnection();
    }

    // ✅ Ambil data karyawan berdasarkan RFID
    public Karyawan getKaryawanByRfid(String rfid) {
        String query = "SELECT * FROM karyawan WHERE rfid = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rfid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Karyawan karyawan = new Karyawan();
                karyawan.setIdKaryawan(rs.getInt("id_karyawan"));
                karyawan.setNamaKaryawan(rs.getString("nama_karyawan"));
                karyawan.setRfid(rs.getString("rfid"));
                return karyawan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Mencatat absensi lengkap
    public boolean catatAbsensi(int idKaryawan, String status, LocalDateTime waktuMasuk, LocalDateTime waktuPulang, String keterangan) {
        String query = "INSERT INTO absensi (id_karyawan, status, waktu_masuk, waktu_pulang, keterangan) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, idKaryawan);
            ps.setString(2, status);
            ps.setObject(3, waktuMasuk != null ? waktuMasuk : LocalDateTime.now());
            ps.setObject(4, waktuPulang != null ? waktuPulang : LocalDateTime.now());
            ps.setString(5, keterangan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Menangani proses absensi masuk/pulang
    @Override
    public boolean processAttendance(int userId, String status) {
        String field = status.equalsIgnoreCase("Masuk") ? "waktu_masuk" : "waktu_pulang";
        String sql = "INSERT INTO absensi (id_karyawan, status, " + field + ") VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, status);
            stmt.setObject(3, LocalDateTime.now());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // CRUD User
    @Override
    public void insertData(User model) {
        String sql = "INSERT INTO users (nama, username, password, role, insert_by, rfid_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getNama());
            st.setString(2, model.getUsername());
            st.setString(3, BCrypt.hashpw(model.getPassword(), BCrypt.gensalt()));
            st.setString(4, model.getRole());
            st.setInt(5, model.getInsertBy());
            st.setString(6, model.getRfid());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateData(User model) {
        String sql = "UPDATE users SET nama=?, username=?, password=?, role=?, update_by=?, update_at=NOW(), rfid_id=? WHERE id_users=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getNama());
            st.setString(2, model.getUsername());
            String hashedPassword = BCrypt.hashpw(model.getPassword(), BCrypt.gensalt());
            st.setString(3, hashedPassword);
            st.setString(4, model.getRole());
            st.setInt(5, model.getUpdateBy());
            st.setString(6, model.getRfid());
            st.setInt(7, model.getIdUser());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteData(User model) {
        String sql = "UPDATE users SET delete_by=?, delete_at=NOW(), is_delete=TRUE WHERE id_users=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, model.getDeleteBy());
            st.setInt(2, model.getIdUser());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void restoreData(User model) {
        String checkQuery = "SELECT is_delete FROM users WHERE id_users = ?";
        try (PreparedStatement st = conn.prepareStatement(checkQuery)) {
            st.setInt(1, model.getIdUser());
            ResultSet rs = st.executeQuery();
            if (rs.next() && !rs.getBoolean("is_delete")) return;

            String sql = "UPDATE users SET delete_by=NULL, delete_at=NULL, is_delete=FALSE WHERE id_users=?";
            try (PreparedStatement updateSt = conn.prepareStatement(sql)) {
                updateSt.setInt(1, model.getIdUser());
                updateSt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Ambil semua data user
    @Override
    public List<User> getData(String filter) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id_users, nama, username, role, is_delete, rfid_id FROM users";
        if ("Aktif".equalsIgnoreCase(filter) || "Filter".equalsIgnoreCase(filter)) {
            sql += " WHERE is_delete = FALSE";
        } else if ("Terhapus".equalsIgnoreCase(filter)) {
            sql += " WHERE is_delete = TRUE";
        }

        try (PreparedStatement st = conn.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                User model = new User();
                model.setIdUser(rs.getInt("id_users"));
                model.setNama(rs.getString("nama"));
                model.setUsername(rs.getString("username"));
                model.setRole(rs.getString("role"));
                model.setIsDelete(rs.getBoolean("is_delete"));
                model.setRfid(rs.getString("rfid_id"));
                list.add(model);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Pencarian user
    @Override
    public List<User> searchData(String keyword, String filter) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT id_users, nama, username, role, is_delete, rfid_id FROM users " +
                     "WHERE (id_users LIKE ? OR nama LIKE ? OR username LIKE ? OR role LIKE ? OR rfid_id LIKE ?)";
        if ("Aktif".equalsIgnoreCase(filter) || "Filter".equalsIgnoreCase(filter)) {
            sql += " AND is_delete = FALSE";
        } else if ("Terhapus".equalsIgnoreCase(filter)) {
            sql += " AND is_delete = TRUE";
        }

        try (PreparedStatement st = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++) {
                st.setString(i, "%" + keyword + "%");
            }
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    User model = new User();
                    model.setIdUser(rs.getInt("id_users"));
                    model.setNama(rs.getString("nama"));
                    model.setUsername(rs.getString("username"));
                    model.setRole(rs.getString("role"));
                    model.setIsDelete(rs.getBoolean("is_delete"));
                    model.setRfid(rs.getString("rfid_id"));
                    list.add(model);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Validasi Username Unik
    @Override
    public boolean validateUsername(User model) {
        String sql = "SELECT username FROM users WHERE username LIKE BINARY ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getUsername());
            try (ResultSet rs = st.executeQuery()) {
                return !rs.next(); // true jika username belum dipakai
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Validasi RFID Unik
    @Override
    public boolean validateRfid(User model) {
        String sql = "SELECT rfid_id FROM users WHERE rfid_id = ? AND id_users != ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getRfid());
            st.setInt(2, model.getIdUser() != 0 ? model.getIdUser() : -1); // -1 untuk insert baru
            try (ResultSet rs = st.executeQuery()) {
                return !rs.next(); // true jika RFID belum dipakai oleh user lain
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Proses login
    @Override
    public User processLogin(User model) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, model.getUsername());
            ResultSet rs = st.executeQuery();
            if (rs.next() && BCrypt.checkpw(model.getPassword(), rs.getString("password"))) {
                User loggedUser = new User();
                loggedUser.setIdUser(rs.getInt("id_users"));
                loggedUser.setNama(rs.getString("nama"));
                loggedUser.setUsername(rs.getString("username"));
                loggedUser.setRole(rs.getString("role"));
                loggedUser.setRfid(rs.getString("rfid_id"));
                return loggedUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Get User by ID
    @Override
    public User getUserById(int idUser) {
        String sql = "SELECT id_users, nama, username, role, rfid_id FROM users WHERE is_delete=FALSE AND id_users=?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, idUser);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id_users"));
                user.setNama(rs.getString("nama"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setRfid(rs.getString("rfid_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Get User by RFID
    @Override
    public User getUserByRFID(String rfid) {
        String sql = "SELECT id_users, nama, username, role, rfid_id FROM users WHERE rfid_id = ? AND is_delete = FALSE";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, rfid);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setIdUser(rs.getInt("id_users"));
                user.setNama(rs.getString("nama"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setRfid(rs.getString("rfid_id"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Validasi password lama
    @Override
    public boolean validateOldPassword(String username, String oldPassword) {
        String storedPassword = getPasswordFromDatabase(username);
        return storedPassword != null && BCrypt.checkpw(oldPassword, storedPassword);
    }

    @Override
    public boolean changePassword(String username, String oldPassword, String newPassword) {
        if (!validateOldPassword(username, oldPassword)) return false;

        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
            st.setString(2, username);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Ambil password dari database
    @Override
    public String getPasswordFromDatabase(String username) {
        String query = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) return rs.getString("password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getPasswordByUsername(String username) {
        return getPasswordFromDatabase(username);
    }

    public String getLastErrorMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}