package dao;

import connectDB.ConnectDB;
import entity.KhachHang;
import java.sql.*;

public class KhachHangDAO {
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang(maKH, tenKH, sdt, diemTichLuy, loaiKH) VALUES (?,?,?,?,?)";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getMaKH());
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            ps.setInt(4, kh.getDiemTichLuy());
            ps.setString(5, kh.getLoaiKH());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

	public boolean them(KhachHang kh) {
		// TODO Auto-generated method stub
		return false;
	}

	public KhachHang timTheoMa(String ma) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean capNhat(KhachHang kh) {
		// TODO Auto-generated method stub
		return false;
	}
	
}