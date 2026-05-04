package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVienDAO {
    private static final String FIND_BY_TAI_KHOAN_SQL =
            "SELECT maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK " +
            "FROM NhanVien " +
            "WHERE maTK = ?";

    public NhanVien timTheoMaTaiKhoan(int maTK) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_TAI_KHOAN_SQL)) {

            ps.setInt(1, maTK);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate ngaySinh = rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null;
                    LocalDate ngayVaoLam = rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null;

                    return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("tenNV"),
                            rs.getString("sdt"),
                            rs.getString("diaChi"),
                            ngaySinh,
                            ngayVaoLam,
                            rs.getInt("maTK")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn thông tin nhân viên: " + e.getMessage(), e);
        }
    }
}