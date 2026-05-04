package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhaCungCap;

public class NhaCungCapDAO {
    private static final String SELECT_ALL = "SELECT maNCC, tenNCC, sdt, diaChi, email FROM NhaCungCap ORDER BY maNCC";

    public List<NhaCungCap> timTatCa() {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {

            List<NhaCungCap> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC"),
                        rs.getString("sdt"),
                        rs.getString("diaChi"),
                        rs.getString("email")
                ));
            }
            return list;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn nhà cung cấp: " + e.getMessage(), e);
        }
    }

    public NhaCungCap timTheoMa(String maNCC) {
        if (maNCC == null || maNCC.trim().isEmpty()) return null;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM NhaCungCap WHERE maNCC = ?")) {
            ps.setString(1, maNCC.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhaCungCap(
                            rs.getString("maNCC"),
                            rs.getString("tenNCC"),
                            rs.getString("sdt"),
                            rs.getString("diaChi"),
                            rs.getString("email")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<NhaCungCap> timTheoTuKhoa(String tuKhoa) {
        List<NhaCungCap> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE maNCC LIKE ? OR tenNCC LIKE ? OR sdt LIKE ? OR email LIKE ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + tuKhoa + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new NhaCungCap(
                            rs.getString("maNCC"),
                            rs.getString("tenNCC"),
                            rs.getString("sdt"),
                            rs.getString("diaChi"),
                            rs.getString("email")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean themNhaCungCap(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap (maNCC, tenNCC, sdt, diaChi, email) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhatNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET tenNCC = ?, sdt = ?, diaChi = ?, email = ? WHERE maNCC = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSdt());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean xoaNhaCungCap(String maNCC) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM NhaCungCap WHERE maNCC = ?")) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
