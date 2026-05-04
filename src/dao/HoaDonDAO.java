package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.HoaDon;

public class HoaDonDAO {
    private static final String FIND_ALL_SQL =
            "SELECT maHD, ngayLap, maNV, maKH, maThue, maKM FROM HoaDon ORDER BY ngayLap DESC, maHD DESC";

    private static final String FIND_BY_MA_NV_SQL =
            "SELECT maHD, ngayLap, maNV, maKH, maThue, maKM " +
            "FROM HoaDon " +
            "WHERE maNV LIKE ? " +
            "ORDER BY ngayLap DESC, maHD DESC";

    private static final String INSERT_SQL =
            "INSERT INTO HoaDon(maHD, ngayLap, maNV, maKH, maThue, maKM) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String INSERT_CTHD_SQL =
            "INSERT INTO ChiTietHoaDon(maHD, maSP, soLuong, donGia) VALUES (?, ?, ?, ?)";

    public List<HoaDon> timTatCa() {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<HoaDon> ds = new ArrayList<>();
            while (rs.next()) {
                ds.add(mapHoaDon(rs));
            }
            return ds;
        } catch (SQLException e) {
            throw new IllegalStateException("Loi truy van danh sach hoa don: " + e.getMessage(), e);
        }
    }

    public List<HoaDon> timTheoMaNhanVien(String maNV) {
        String keyword = maNV == null ? "" : maNV.trim();
        String likeValue = "%" + keyword + "%";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_MA_NV_SQL)) {

            ps.setString(1, likeValue);

            try (ResultSet rs = ps.executeQuery()) {
                List<HoaDon> ds = new ArrayList<>();
                while (rs.next()) {
                    ds.add(mapHoaDon(rs));
                }
                return ds;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Loi tra cuu hoa don theo ma nhan vien: " + e.getMessage(), e);
        }
    }

    public boolean themHoaDon(HoaDon hoaDon) {
        if (hoaDon == null) {
            return false;
        }

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, hoaDon.getMaHD());
            ps.setDate(2, toSqlDate(hoaDon.getNgayLap()));
            ps.setString(3, hoaDon.getMaNV());
            if (isBlank(hoaDon.getMaKH())) {
                ps.setNull(4, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(4, hoaDon.getMaKH());
            }
            if (isBlank(hoaDon.getMaThue())) {
                ps.setNull(5, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(5, hoaDon.getMaThue());
            }
            if (isBlank(hoaDon.getMaKM())) {
                ps.setNull(6, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(6, hoaDon.getMaKM());
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Loi lap hoa don: " + e.getMessage(), e);
        }
    }

    private HoaDon mapHoaDon(ResultSet rs) throws SQLException {
        Date ngayLapSql = rs.getDate("ngayLap");
        LocalDate ngayLap = ngayLapSql != null ? ngayLapSql.toLocalDate() : null;

        return new HoaDon(
                rs.getString("maHD"),
                ngayLap,
                rs.getString("maNV"),
                rs.getString("maKH"),
                rs.getString("maThue"),
                rs.getString("maKM")
        );
    }

    private Date toSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String taoMaHDMoi() {
        String sql = "SELECT MAX(CAST(SUBSTRING(maHD, 3, LEN(maHD)) AS INT)) AS maxNum FROM HoaDon WHERE maHD LIKE 'HD%';";
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int max = 0;
            if (rs.next()) {
                max = rs.getInt("maxNum");
            }
            int next = max + 1;
            return String.format("HD%03d", next);
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi khi tạo mã hóa đơn mới: " + e.getMessage(), e);
        }
    }

    public boolean themChiTietHoaDon(entity.ChiTietHoaDon cthd) {
        if (cthd == null) return false;
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_CTHD_SQL)) {
            ps.setString(1, cthd.getMaHD());
            ps.setString(2, cthd.getMaSP());
            if (cthd.getSoLuong() != null) ps.setInt(3, cthd.getSoLuong());
            else ps.setNull(3, java.sql.Types.INTEGER);
            if (cthd.getDonGia() != null) ps.setDouble(4, cthd.getDonGia());
            else ps.setNull(4, java.sql.Types.FLOAT);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi thêm chi tiết hóa đơn: " + e.getMessage(), e);
        }
    }
}
