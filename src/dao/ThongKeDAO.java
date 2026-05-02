package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;

public class ThongKeDAO {
    public List<Object[]> doanhThuTheoNgay() {
        String sql =
                "SELECT CAST(hd.ngayLap AS date) AS ngay, COUNT(DISTINCT hd.maHD) AS soHoaDon, " +
                "COALESCE(SUM(ct.soLuong * ct.donGia), 0) AS tongDoanhThu " +
                "FROM HoaDon hd " +
                "LEFT JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD " +
                "GROUP BY CAST(hd.ngayLap AS date) " +
                "ORDER BY ngay DESC";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getDate("ngay") != null ? rs.getDate("ngay").toLocalDate() : null,
                rs.getInt("soHoaDon"),
                rs.getDouble("tongDoanhThu")
        });
    }

    public List<Object[]> doanhThuTheoThang() {
        String sql =
                "SELECT YEAR(hd.ngayLap) AS nam, MONTH(hd.ngayLap) AS thang, COUNT(DISTINCT hd.maHD) AS soHoaDon, " +
                "COALESCE(SUM(ct.soLuong * ct.donGia), 0) AS tongDoanhThu " +
                "FROM HoaDon hd " +
                "LEFT JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD " +
                "GROUP BY YEAR(hd.ngayLap), MONTH(hd.ngayLap) " +
                "ORDER BY nam DESC, thang DESC";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getInt("nam") + "-" + String.format("%02d", rs.getInt("thang")),
                rs.getInt("soHoaDon"),
                rs.getDouble("tongDoanhThu")
        });
    }

    public List<Object[]> tonKho() {
        String sql =
                "SELECT maSP, tenSP, loaiSP, soLuongTon, gia, hanSuDung FROM SanPham ORDER BY soLuongTon ASC, tenSP";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getString("maSP"),
                rs.getString("tenSP"),
                rs.getString("loaiSP"),
                rs.getInt("soLuongTon"),
                rs.getDouble("gia"),
                rs.getDate("hanSuDung") != null ? rs.getDate("hanSuDung").toLocalDate() : null
        });
    }

    public List<Object[]> sanPhamBanChay() {
        String sql =
                "SELECT TOP 10 sp.maSP, sp.tenSP, sp.loaiSP, SUM(ct.soLuong) AS soLuongBan, " +
                "COALESCE(SUM(ct.soLuong * ct.donGia), 0) AS doanhThu " +
                "FROM ChiTietHoaDon ct " +
                "INNER JOIN SanPham sp ON ct.maSP = sp.maSP " +
                "GROUP BY sp.maSP, sp.tenSP, sp.loaiSP " +
                "ORDER BY soLuongBan DESC, doanhThu DESC";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getString("maSP"),
                rs.getString("tenSP"),
                rs.getString("loaiSP"),
                rs.getInt("soLuongBan"),
                rs.getDouble("doanhThu")
        });
    }

    public List<Object[]> soLuongBanTheoLoaiSanPham() {
        String sql =
                "SELECT sp.loaiSP, SUM(ct.soLuong) AS soLuongBan " +
                "FROM ChiTietHoaDon ct " +
                "INNER JOIN SanPham sp ON ct.maSP = sp.maSP " +
                "GROUP BY sp.loaiSP " +
                "ORDER BY soLuongBan DESC, sp.loaiSP";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getString("loaiSP"),
                rs.getDouble("soLuongBan")
        });
    }

    public List<Object[]> hoaDonBan() {
        String sql =
                "SELECT hd.maHD, hd.ngayLap, hd.maNV, nv.tenNV, hd.maKH, kh.tenKH, " +
                "COUNT(ct.maSP) AS soDongSP, COALESCE(SUM(ct.soLuong * ct.donGia), 0) AS tongTien " +
                "FROM HoaDon hd " +
                "LEFT JOIN NhanVien nv ON hd.maNV = nv.maNV " +
                "LEFT JOIN KhachHang kh ON hd.maKH = kh.maKH " +
                "LEFT JOIN ChiTietHoaDon ct ON hd.maHD = ct.maHD " +
                "GROUP BY hd.maHD, hd.ngayLap, hd.maNV, nv.tenNV, hd.maKH, kh.tenKH " +
                "ORDER BY hd.ngayLap DESC, hd.maHD DESC";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getString("maHD"),
                rs.getDate("ngayLap") != null ? rs.getDate("ngayLap").toLocalDate() : null,
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("maKH"),
                rs.getString("tenKH"),
                rs.getInt("soDongSP"),
                rs.getDouble("tongTien")
        });
    }

    public List<Object[]> phieuNhap() {
        String sql =
                "SELECT pn.maPhieu, pn.ngayNhap, pn.maNV, nv.tenNV, pn.maNCC, ncc.tenNCC, " +
                "COUNT(ct.maSP) AS soDongSP, COALESCE(SUM(ct.soLuong * ct.giaNhap), 0) AS tongTien " +
                "FROM PhieuNhap pn " +
                "LEFT JOIN NhanVien nv ON pn.maNV = nv.maNV " +
                "LEFT JOIN NhaCungCap ncc ON pn.maNCC = ncc.maNCC " +
                "LEFT JOIN ChiTietPhieuNhap ct ON pn.maPhieu = ct.maPhieu " +
                "GROUP BY pn.maPhieu, pn.ngayNhap, pn.maNV, nv.tenNV, pn.maNCC, ncc.tenNCC " +
                "ORDER BY pn.ngayNhap DESC, pn.maPhieu DESC";
        return truyVanThongKe(sql, rs -> new Object[] {
                rs.getString("maPhieu"),
                rs.getDate("ngayNhap") != null ? rs.getDate("ngayNhap").toLocalDate() : null,
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("maNCC"),
                rs.getString("tenNCC"),
                rs.getInt("soDongSP"),
                rs.getDouble("tongTien")
        });
    }

    private List<Object[]> truyVanThongKe(String sql, RowMapper mapper) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                rows.add(mapper.map(rs));
            }
            return rows;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn thống kê: " + e.getMessage(), e);
        }
    }

    @FunctionalInterface
    private interface RowMapper {
        Object[] map(ResultSet rs) throws SQLException;
    }
}