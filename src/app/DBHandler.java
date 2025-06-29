package app;

import java.sql.*;
import java.util.*;

public class DBHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/rekam_medis";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // 🔐 Login User
    public static User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password); // Gunakan hash di produksi

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("role"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Tambah Pasien
    public static void insertPasien(String nama, String nik) {
        String sql = "INSERT INTO pasien (nama, nik) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nama);
            stmt.setString(2, nik);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Update Pasien
    public static void updatePasien(int id, String nama, String nik) {
        String sql = "UPDATE pasien SET nama = ?, nik = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nama);
            stmt.setString(2, nik);
            stmt.setInt(3, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Hapus Pasien
    public static void deletePasien(int id) {
        String sql = "DELETE FROM pasien WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ✅ Ambil Daftar Pasien
    public static List<Pasien<String>> getPasienList(String keyword) {
        List<Pasien<String>> list = new ArrayList<>();
        String sql = "SELECT * FROM pasien WHERE nama LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nama = rs.getString("nama");
                String nik = rs.getString("nik");
                list.add(new Pasien<>(id, nama, nik));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    // ✅ Ambil tindakan berdasarkan pasien
public static List<String[]> getTindakanByPasien(int pasienId) {
    List<String[]> list = new ArrayList<>();
    String sql = "SELECT tanggal, deskripsi, dokter FROM tindakan WHERE pasien_id = ?";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, pasienId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            list.add(new String[]{
                rs.getString("tanggal"),
                rs.getString("deskripsi"),
                rs.getString("dokter")
            });
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}

// ✅ Tambahkan tindakan baru ke pasien
    public static void tambahTindakan(int pasienId, String tanggal, String deskripsi, String dokter) {
        String sql = "INSERT INTO tindakan (pasien_id, tanggal, deskripsi, dokter) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
          PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pasienId);
            stmt.setString(2, tanggal);
            stmt.setString(3, deskripsi);
            stmt.setString(4, dokter);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
