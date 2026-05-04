package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import connectDB.ConnectDB;
import entity.Thue;

public class ThueDAO {
    public List<Thue> timTatCa() {
        List<Thue> ds = new ArrayList<>();
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Thue");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new Thue(rs.getString("maThue"), rs.getString("tenThue"), rs.getDouble("phanTram"), rs.getString("moTa")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public Thue timTheoMa(String maThue) {
        if (maThue == null || maThue.trim().isEmpty()) return null;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM Thue WHERE maThue = ?")) {
            ps.setString(1, maThue.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Thue(rs.getString("maThue"), rs.getString("tenThue"), rs.getDouble("phanTram"), rs.getString("moTa"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean themThue(Thue th) {
        String sql = "INSERT INTO Thue (maThue, tenThue, phanTram, moTa) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, th.getMaThue());
            ps.setString(2, th.getTenThue());
            ps.setDouble(3, th.getPhanTram() != null ? th.getPhanTram() : 0);
            ps.setString(4, th.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean capNhatThue(Thue th) {
        String sql = "UPDATE Thue SET tenThue = ?, phanTram = ?, moTa = ? WHERE maThue = ?";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, th.getTenThue());
            ps.setDouble(2, th.getPhanTram() != null ? th.getPhanTram() : 0);
            ps.setString(3, th.getMoTa());
            ps.setString(4, th.getMaThue());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean xoaThue(String maThue) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Thue WHERE maThue = ?")) {
            ps.setString(1, maThue);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}
