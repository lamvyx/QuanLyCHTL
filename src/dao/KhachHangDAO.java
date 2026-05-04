package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
    public List<KhachHang> timTatCa() {
        List<KhachHang> ds = new ArrayList<>();
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM KhachHang");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("sdt"),
                    rs.getInt("diemTichLuy"),
                    rs.getString("loaiKH")
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public KhachHang timTheoMa(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) return null;
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM KhachHang WHERE maKH = ?")) {
            ps.setString(1, maKH.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("sdt"),
                        rs.getInt("diemTichLuy"),
                        rs.getString("loaiKH")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<KhachHang> timTheoTuKhoa(String tuKhoa) {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE maKH LIKE ? OR tenKH LIKE ? OR sdt LIKE ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + tuKhoa + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new KhachHang(
                        rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("sdt"),
                        rs.getInt("diemTichLuy"),
                        rs.getString("loaiKH")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, sdt, diemTichLuy, loaiKH) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setInt(4, kh.getDiemTichLuy() != null ? kh.getDiemTichLuy() : 0);
            ps.setString(5, kh.getLoaiKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhatKhachHang(KhachHang kh) {
        String sql = "UPDATE KhachHang SET tenKH = ?, sdt = ?, diemTichLuy = ?, loaiKH = ? WHERE maKH = ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setInt(3, kh.getDiemTichLuy() != null ? kh.getDiemTichLuy() : 0);
            ps.setString(4, kh.getLoaiKH());
            ps.setString(5, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean xoaKhachHang(String maKH) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM KhachHang WHERE maKH = ?")) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
    
    public boolean capNhatDiem(String maKH, int diemCongThem) {
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE KhachHang SET diemTichLuy = ISNULL(diemTichLuy, 0) + ? WHERE maKH = ?")) {
            ps.setInt(1, diemCongThem);
            ps.setString(2, maKH.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
