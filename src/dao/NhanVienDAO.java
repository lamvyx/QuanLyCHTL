package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVienDAO {
    private static final String FIND_BY_TAI_KHOAN_SQL =
            "SELECT maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK " +
            "FROM NhanVien " +
            "WHERE maTK = ?";

    private static final String FIND_ALL_SQL =
        "SELECT maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK " +
        "FROM NhanVien ORDER BY maNV";

    private static final String SEARCH_SQL =
        "SELECT maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK " +
        "FROM NhanVien " +
        "WHERE maNV LIKE ? OR tenNV LIKE ? OR sdt LIKE ? " +
        "ORDER BY maNV";

    private static final String INSERT_SQL =
        "INSERT INTO NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL =
        "UPDATE NhanVien SET tenNV = ?, sdt = ?, diaChi = ?, ngaySinh = ?, ngayVaoLam = ?, maTK = ? " +
        "WHERE maNV = ?";

    private static final String FIND_BY_ID_SQL =
        "SELECT maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK " +
        "FROM NhanVien WHERE maNV = ?";

    private static final String FIND_BY_PHONE_SQL =
        "SELECT maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK " +
        "FROM NhanVien WHERE sdt = ? ORDER BY maNV";

    private static final String DELETE_SQL =
        "DELETE FROM NhanVien WHERE maNV = ?";

    private static final String LIST_MA_NV_SQL =
        "SELECT maNV FROM NhanVien ORDER BY maNV";

    private static final String EXISTS_MA_NV_SQL =
        "SELECT 1 FROM NhanVien WHERE maNV = ?";

    private static final String INSERT_TAI_KHOAN_SQL =
            "INSERT INTO TaiKhoan([username], [password], [role], trangThai) VALUES (?, ?, ?, ?)";

    public NhanVien timTheoMaTaiKhoan(int maTK) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_TAI_KHOAN_SQL)) {

            ps.setInt(1, maTK);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    LocalDate ngaySinh = rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null;
                    LocalDate ngayVaoLam = rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null;

                    return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("tenNV"),
                            rs.getString("sdt"),
                            rs.getString("diaChi"),
                            ngaySinh,
                            ngayVaoLam,
                            rs.getInt("maTK")
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn thông tin nhân viên: " + e.getMessage(), e);
        }
    }

    public List<NhanVien> timTatCa() {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<NhanVien> ds = new ArrayList<>();
            while (rs.next()) {
                ds.add(mapNhanVien(rs));
            }
            return ds;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn danh sách nhân viên: " + e.getMessage(), e);
        }
    }

    public List<NhanVien> timTheoTuKhoa(String tuKhoa) {
        String keyword = tuKhoa == null ? "" : tuKhoa.trim();
        String likeValue = "%" + keyword + "%";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_SQL)) {

            ps.setString(1, likeValue);
            ps.setString(2, likeValue);
            ps.setString(3, likeValue);

            try (ResultSet rs = ps.executeQuery()) {
                List<NhanVien> ds = new ArrayList<>();
                while (rs.next()) {
                    ds.add(mapNhanVien(rs));
                }
                return ds;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi tra cứu nhân viên: " + e.getMessage(), e);
        }
    }

    public NhanVien timTheoMa(String maNV) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_ID_SQL)) {
            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapNhanVien(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn nhân viên theo mã: " + e.getMessage(), e);
        }
    }

    public List<NhanVien> timTheoSoDienThoai(String sdt) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(FIND_BY_PHONE_SQL)) {
            ps.setString(1, sdt);

            try (ResultSet rs = ps.executeQuery()) {
                List<NhanVien> ds = new ArrayList<>();
                while (rs.next()) {
                    ds.add(mapNhanVien(rs));
                }
                return ds;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi truy vấn nhân viên theo số điện thoại: " + e.getMessage(), e);
        }
    }

    public boolean xoaNhanVien(String maNV) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi xóa nhân viên: " + e.getMessage(), e);
        }
    }

    public boolean themNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            return false;
        }

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, nhanVien.getMaNV());
            ps.setString(2, nhanVien.getTenNV());
            ps.setString(3, nhanVien.getSdt());
            ps.setString(4, nhanVien.getDiaChi());
            ps.setDate(5, toSqlDate(nhanVien.getNgaySinh()));
            ps.setDate(6, toSqlDate(nhanVien.getNgayVaoLam()));
            if (nhanVien.getMaTK() != null) {
                ps.setInt(7, nhanVien.getMaTK());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi thêm nhân viên: " + e.getMessage(), e);
        }
    }

    public boolean capNhatNhanVien(NhanVien nhanVien) {
        if (nhanVien == null) {
            return false;
        }

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, nhanVien.getTenNV());
            ps.setString(2, nhanVien.getSdt());
            ps.setString(3, nhanVien.getDiaChi());
            ps.setDate(4, toSqlDate(nhanVien.getNgaySinh()));
            ps.setDate(5, toSqlDate(nhanVien.getNgayVaoLam()));
            if (nhanVien.getMaTK() != null) {
                ps.setInt(6, nhanVien.getMaTK());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.setString(7, nhanVien.getMaNV());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi cập nhật nhân viên: " + e.getMessage(), e);
        }
    }

    public List<String> layDanhSachMaNhanVien() {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(LIST_MA_NV_SQL);
             ResultSet rs = ps.executeQuery()) {

            List<String> ds = new ArrayList<>();
            while (rs.next()) {
                ds.add(rs.getString("maNV"));
            }
            return ds;
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi lấy danh sách mã nhân viên: " + e.getMessage(), e);
        }
    }

    public boolean tonTaiMaNhanVien(String maNV) {
        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(EXISTS_MA_NV_SQL)) {

            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi kiểm tra mã nhân viên: " + e.getMessage(), e);
        }
    }

    public KetQuaTaoNhanVien themNhanVienTuDong(String tenNV, String sdt, String diaChi, LocalDate ngaySinh, LocalDate ngayVaoLam) {
        try (Connection con = ConnectDB.getInstance().getConnection()) {
            con.setAutoCommit(false);

            try {
                String maNV = sinhMaNhanVienMoi(con);
                String username = sinhUsernameMoi(maNV);

                int maTK;
                try (PreparedStatement psTaiKhoan = con.prepareStatement(INSERT_TAI_KHOAN_SQL, Statement.RETURN_GENERATED_KEYS)) {
                    psTaiKhoan.setString(1, username);
                    psTaiKhoan.setString(2, "123456");
                    psTaiKhoan.setString(3, "NHAN_VIEN");
                    psTaiKhoan.setBoolean(4, true);
                    psTaiKhoan.executeUpdate();

                    try (ResultSet generatedKeys = psTaiKhoan.getGeneratedKeys()) {
                        if (!generatedKeys.next()) {
                            throw new SQLException("Không thể tạo mã tài khoản tự động.");
                        }
                        maTK = generatedKeys.getInt(1);
                    }
                }

                try (PreparedStatement psNhanVien = con.prepareStatement(INSERT_SQL)) {
                    psNhanVien.setString(1, maNV);
                    psNhanVien.setString(2, tenNV);
                    psNhanVien.setString(3, sdt);
                    psNhanVien.setString(4, diaChi);
                    psNhanVien.setDate(5, toSqlDate(ngaySinh));
                    psNhanVien.setDate(6, toSqlDate(ngayVaoLam));
                    psNhanVien.setInt(7, maTK);
                    psNhanVien.executeUpdate();
                }

                con.commit();
                return new KetQuaTaoNhanVien(maNV, maTK, username);
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Lỗi thêm nhân viên tự động: " + e.getMessage(), e);
        }
    }

    private String sinhMaNhanVienMoi(Connection con) throws SQLException {
        String sql = "SELECT maNV FROM NhanVien";
        int soLonNhat = 0;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String maNV = rs.getString("maNV");
                int soHienTai = parseSoCuoi(maNV);
                if (soHienTai > soLonNhat) {
                    soLonNhat = soHienTai;
                }
            }
        }

        return String.format("NV%03d", soLonNhat + 1);
    }

    private String sinhUsernameMoi(String maNV) {
        String so = maNV.length() > 2 ? maNV.substring(2) : maNV;
        return "nv" + so;
    }

    private int parseSoCuoi(String giaTri) {
        if (giaTri == null) {
            return 0;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = giaTri.length() - 1; i >= 0; i--) {
            char ch = giaTri.charAt(i);
            if (Character.isDigit(ch)) {
                sb.insert(0, ch);
            } else {
                break;
            }
        }

        if (sb.length() == 0) {
            return 0;
        }

        return Integer.parseInt(sb.toString());
    }

    private NhanVien mapNhanVien(ResultSet rs) throws SQLException {
        LocalDate ngaySinh = rs.getDate("ngaySinh") != null ? rs.getDate("ngaySinh").toLocalDate() : null;
        LocalDate ngayVaoLam = rs.getDate("ngayVaoLam") != null ? rs.getDate("ngayVaoLam").toLocalDate() : null;

        return new NhanVien(
                rs.getString("maNV"),
                rs.getString("tenNV"),
                rs.getString("sdt"),
                rs.getString("diaChi"),
                ngaySinh,
                ngayVaoLam,
                rs.getObject("maTK") != null ? rs.getInt("maTK") : null
        );
    }

    private Date toSqlDate(LocalDate localDate) {
        return localDate == null ? null : Date.valueOf(localDate);
    }

    public static class KetQuaTaoNhanVien {
        private final String maNV;
        private final int maTK;
        private final String username;

        public KetQuaTaoNhanVien(String maNV, int maTK, String username) {
            this.maNV = maNV;
            this.maTK = maTK;
            this.username = username;
        }

        public String getMaNV() {
            return maNV;
        }

        public int getMaTK() {
            return maTK;
        }

        public String getUsername() {
            return username;
        }
    }
}