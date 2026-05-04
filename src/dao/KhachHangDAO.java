package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import connectDB.ConnectDB;
import entity.KhachHang;

public class KhachHangDAO {
    public KhachHang timTheoMa(String maKH) {
        if (maKH == null || maKH.trim().isEmpty()) return null;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM KhachHang WHERE maKH = ?")) {
            ps.setString(1, maKH.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhachHang kh = new KhachHang();
                    kh.setMaKH(rs.getString("maKH"));
                    kh.setTenKH(rs.getString("tenKH"));
                    kh.setSdt(rs.getString("sdt"));
                    kh.setDiemTichLuy(rs.getInt("diemTichLuy"));
                    kh.setLoaiKH(rs.getString("loaiKH"));
                    return kh;
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
    
    public boolean capNhatDiem(String maKH, int diemCongThem) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE KhachHang SET diemTichLuy = ISNULL(diemTichLuy, 0) + ? WHERE maKH = ?")) {
            ps.setInt(1, diemCongThem);
            ps.setString(2, maKH.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
