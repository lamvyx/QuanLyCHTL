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
}
