package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import connectDB.ConnectDB;
import entity.KhuyenMai;

public class KhuyenMaiDAO {
    public List<KhuyenMai> timTatCa() {
        List<KhuyenMai> ds = new ArrayList<>();
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM KhuyenMai");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.sql.Date start = rs.getDate("ngayBD");
                java.sql.Date end = rs.getDate("ngayKT");
                ds.add(new KhuyenMai(
                    rs.getString("maKM"),
                    rs.getString("tenKM"),
                    rs.getString("moTa"),
                    rs.getDouble("phanTramGiam"),
                    start != null ? start.toLocalDate() : null,
                    end != null ? end.toLocalDate() : null
                ));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public List<KhuyenMai> timTatCaHieuLuc() {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE ngayBD <= CAST(GETDATE() AS DATE) AND ngayKT >= CAST(GETDATE() AS DATE)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.sql.Date start = rs.getDate("ngayBD");
                java.sql.Date end = rs.getDate("ngayKT");
                ds.add(new KhuyenMai(rs.getString("maKM"), rs.getString("tenKM"), rs.getString("moTa"), 
                    rs.getDouble("phanTramGiam"), start != null ? start.toLocalDate() : null, end != null ? end.toLocalDate() : null));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public KhuyenMai timTheoMa(String maKM) {
        if (maKM == null || maKM.trim().isEmpty()) return null;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM KhuyenMai WHERE maKM = ?")) {
            ps.setString(1, maKM.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    java.sql.Date start = rs.getDate("ngayBD");
                    java.sql.Date end = rs.getDate("ngayKT");
                    return new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getString("moTa"),
                        rs.getDouble("phanTramGiam"),
                        start != null ? start.toLocalDate() : null,
                        end != null ? end.toLocalDate() : null
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<KhuyenMai> timTheoTuKhoa(String tuKhoa) {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai WHERE maKM LIKE ? OR tenKM LIKE ? OR moTa LIKE ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = "%" + tuKhoa + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    java.sql.Date start = rs.getDate("ngayBD");
                    java.sql.Date end = rs.getDate("ngayKT");
                    ds.add(new KhuyenMai(
                        rs.getString("maKM"),
                        rs.getString("tenKM"),
                        rs.getString("moTa"),
                        rs.getDouble("phanTramGiam"),
                        start != null ? start.toLocalDate() : null,
                        end != null ? end.toLocalDate() : null
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean themKhuyenMai(KhuyenMai km) {
        String sql = "INSERT INTO KhuyenMai (maKM, tenKM, moTa, phanTramGiam, ngayBD, ngayKT) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getMaKM());
            ps.setString(2, km.getTenKM());
            ps.setString(3, km.getMoTa());
            ps.setDouble(4, km.getPhanTramGiam() != null ? km.getPhanTramGiam() : 0);
            ps.setDate(5, km.getNgayBD() != null ? java.sql.Date.valueOf(km.getNgayBD()) : null);
            ps.setDate(6, km.getNgayKT() != null ? java.sql.Date.valueOf(km.getNgayKT()) : null);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhatKhuyenMai(KhuyenMai km) {
        String sql = "UPDATE KhuyenMai SET tenKM = ?, moTa = ?, phanTramGiam = ?, ngayBD = ?, ngayKT = ? WHERE maKM = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, km.getTenKM());
            ps.setString(2, km.getMoTa());
            ps.setDouble(3, km.getPhanTramGiam() != null ? km.getPhanTramGiam() : 0);
            ps.setDate(4, km.getNgayBD() != null ? java.sql.Date.valueOf(km.getNgayBD()) : null);
            ps.setDate(5, km.getNgayKT() != null ? java.sql.Date.valueOf(km.getNgayKT()) : null);
            ps.setString(6, km.getMaKM());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean xoaKhuyenMai(String maKM) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM KhuyenMai WHERE maKM = ?")) {
            ps.setString(1, maKM);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
