package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectDB.ConnectDB;
import entity.TaiKhoan;

public class TaiKhoanDAO {
    private static final String LOGIN_SQL =
            "SELECT maTK, username, password, role, trangThai " +
            "FROM TaiKhoan " +
            "WHERE username = ? AND password = ? AND trangThai = 1";

    public TaiKhoan dangNhap(String username, String password) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(LOGIN_SQL)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoan(
                            rs.getInt("maTK"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getBoolean("trangThai")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Loi truy van dang nhap", e);
        }
    }
}
