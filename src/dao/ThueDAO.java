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
}
