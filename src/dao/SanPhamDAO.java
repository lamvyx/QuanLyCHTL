package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.SanPham;

public class SanPhamDAO {
    private static final String FIND_ALL_SQL =
            "SELECT maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh FROM SanPham ORDER BY maSP";

    private static final String SEARCH_SQL =
            "SELECT maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh " +
            "FROM SanPham " +
            "WHERE maSP LIKE ? OR tenSP LIKE ? OR loaiSP LIKE ? OR maNCC LIKE ? " +
            "ORDER BY maSP";

    private static final String FIND_BY_ID_SQL =
            "SELECT maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh FROM SanPham WHERE maSP = ?";

    private static final String INSERT_SQL =
            "INSERT INTO SanPham(maSP, tenSP, gia, hanSuDung, soLuongTon, loaiSP, maNCC, hinhAnh) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
            "UPDATE SanPham SET tenSP = ?, gia = ?, hanSuDung = ?, soLuongTon = ?, loaiSP = ?, maNCC = ?, hinhAnh = ? WHERE maSP = ?";

    private static final String DELETE_SQL =
            "DELETE FROM SanPham WHERE maSP = ?";

    public List<SanPham> timTatCa() {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<SanPham> danhSach = new ArrayList<>();
            while (rs.next()) {
                danhSach.add(mapSanPham(rs));
            }
            return danhSach;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn danh sách sản phẩm: " + e.getMessage(), e);
        }
    }

    public List<SanPham> timTheoTuKhoa(String tuKhoa) {
        String keyword = tuKhoa == null ? "" : tuKhoa.trim();
        String likeValue = "%" + keyword + "%";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, likeValue);
            ps.setString(2, likeValue);
            ps.setString(3, likeValue);
            ps.setString(4, likeValue);

            try (ResultSet rs = ps.executeQuery()) {
                List<SanPham> danhSach = new ArrayList<>();
                while (rs.next()) {
                    danhSach.add(mapSanPham(rs));
                }
                return danhSach;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi tra cứu sản phẩm: " + e.getMessage(), e);
        }
    }

    public SanPham timTheoMa(String maSP) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {

            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapSanPham(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn sản phẩm theo mã: " + e.getMessage(), e);
        }
    }

    public boolean themSanPham(SanPham sanPham) {
        if (sanPham == null) {
            return false;
        }

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, sanPham.getMaSP());
            ps.setString(2, sanPham.getTenSP());
            if (sanPham.getGia() != null) {
                ps.setDouble(3, sanPham.getGia());
            } else {
                ps.setNull(3, java.sql.Types.FLOAT);
            }
            ps.setDate(4, toSqlDate(sanPham.getHanSuDung()));
            if (sanPham.getSoLuongTon() != null) {
                ps.setInt(5, sanPham.getSoLuongTon());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setString(6, sanPham.getLoaiSP());
            ps.setString(7, sanPham.getMaNCC());
            ps.setString(8, sanPham.getHinhAnh());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi thêm sản phẩm: " + e.getMessage(), e);
        }
    }

    public boolean capNhatSanPham(SanPham sanPham) {
        if (sanPham == null) {
            return false;
        }

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, sanPham.getTenSP());
            if (sanPham.getGia() != null) {
                ps.setDouble(2, sanPham.getGia());
            } else {
                ps.setNull(2, java.sql.Types.FLOAT);
            }
            ps.setDate(3, toSqlDate(sanPham.getHanSuDung()));
            if (sanPham.getSoLuongTon() != null) {
                ps.setInt(4, sanPham.getSoLuongTon());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setString(5, sanPham.getLoaiSP());
            ps.setString(6, sanPham.getMaNCC());
            ps.setString(7, sanPham.getHinhAnh());
            ps.setString(8, sanPham.getMaSP());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi cập nhật sản phẩm: " + e.getMessage(), e);
        }
    }

    public boolean xoaSanPham(String maSP) {
        if (maSP == null || maSP.trim().isEmpty()) {
            return false;
        }

        Connection con = null;
        try {
            con = ConnectDB.getInstance().getConnection();
            con.setAutoCommit(false);
            
            try (PreparedStatement psCTHD = con.prepareStatement("DELETE FROM ChiTietHoaDon WHERE maSP = ?")) {
                psCTHD.setString(1, maSP.trim());
                psCTHD.executeUpdate();
            } catch (SQLException e) { /* ignore */ }
            
            try (PreparedStatement psCTPN = con.prepareStatement("DELETE FROM ChiTietPhieuNhap WHERE maSP = ?")) {
                psCTPN.setString(1, maSP.trim());
                psCTPN.executeUpdate();
            } catch (SQLException e) { /* ignore */ }

            try (PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {
                ps.setString(1, maSP.trim());
                int rows = ps.executeUpdate();
                con.commit();
                return rows > 0;
            }
        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) {}
            }
            throw new IllegalStateException("Lỗi xóa sản phẩm: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); con.close(); } catch (SQLException e) {}
            }
        }
    }

    public String taoMaSPMoi() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maSP, 3, LEN(maSP)) AS INT)) AS maxNum FROM SanPham WHERE maSP LIKE 'SP%';";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int max = 0;
            if (rs.next()) {
                max = rs.getInt("maxNum");
            }
            int next = max + 1;
            return String.format("SP%03d", next);
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi khi tạo mã sản phẩm mới: " + e.getMessage(), e);
        }
    }

    private SanPham mapSanPham(ResultSet rs) throws SQLException {
        Date hanSuDungSql = rs.getDate("hanSuDung");
        return new SanPham(
                rs.getString("maSP"),
                rs.getString("tenSP"),
                rs.getDouble("gia"),
                hanSuDungSql != null ? hanSuDungSql.toLocalDate() : null,
                rs.getInt("soLuongTon"),
                rs.getString("loaiSP"),
                rs.getString("maNCC"),
                rs.getString("hinhAnh")
        );
    }

    private Date toSqlDate(java.time.LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }
}