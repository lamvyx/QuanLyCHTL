package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietPhieuNhap;
import entity.PhieuNhap;

public class PhieuNhapDAO {
    
    public String taoMaPhieuMoi() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maPhieu, 3, LEN(maPhieu)) AS INT)) AS maxNum FROM PhieuNhap WHERE maPhieu LIKE 'PN%';";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int max = 0;
            if (rs.next()) {
                max = rs.getInt("maxNum");
            }
            return String.format("PN%03d", max + 1);
        } catch (SQLException e) {
            return "PN001";
        }
    }

    public boolean lapPhieuNhap(PhieuNhap pn, List<ChiTietPhieuNhap> dsCT) {
        Connection con = null;
        try {
            con = ConnectDB.getInstance().getConnection();
            con.setAutoCommit(false);

            // 1. Insert PhieuNhap
            String sqlPN = "INSERT INTO PhieuNhap(maPhieu, ngayNhap, maNV, maNCC, ghiChu) VALUES(?,?,?,?,?)";
            try (PreparedStatement psPN = con.prepareStatement(sqlPN)) {
                psPN.setString(1, pn.getMaPhieu());
                psPN.setDate(2, java.sql.Date.valueOf(pn.getNgayNhap()));
                psPN.setString(3, pn.getMaNV());
                psPN.setString(4, pn.getMaNCC());
                psPN.setString(5, pn.getGhiChu());
                psPN.executeUpdate();
            }

            // 2. Insert ChiTietPhieuNhap & Update SanPham stock
            String sqlCT = "INSERT INTO ChiTietPhieuNhap(maPhieu, maSP, soLuong, giaNhap) VALUES(?,?,?,?)";
            String sqlUpdateStock = "UPDATE SanPham SET soLuongTon = soLuongTon + ? WHERE maSP = ?";
            
            try (PreparedStatement psCT = con.prepareStatement(sqlCT);
                 PreparedStatement psStock = con.prepareStatement(sqlUpdateStock)) {
                
                for (ChiTietPhieuNhap ct : dsCT) {
                    // Add detail
                    psCT.setString(1, ct.getMaPhieu());
                    psCT.setString(2, ct.getMaSP());
                    psCT.setInt(3, ct.getSoLuong());
                    psCT.setDouble(4, ct.getGiaNhap());
                    psCT.addBatch();

                    // Update stock
                    psStock.setInt(1, ct.getSoLuong());
                    psStock.setString(2, ct.getMaSP());
                    psStock.addBatch();
                }
                psCT.executeBatch();
                psStock.executeBatch();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {}
            }
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) {}
            }
        }
    }
}
