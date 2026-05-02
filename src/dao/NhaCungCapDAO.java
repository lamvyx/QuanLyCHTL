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
}
