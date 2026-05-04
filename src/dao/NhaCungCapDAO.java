package dao;

import connectDB.ConnectDB;
import entity.NhaCungCap;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NhaCungCapDAO {

    public NhaCungCapDAO() {
    }

    public List<NhaCungCap> timTatCa() {
        List<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap ORDER BY maNCC";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public NhaCungCap timTheoMa(String maNCC) {
        String sql = "SELECT * FROM NhaCungCap WHERE maNCC = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<NhaCungCap> search(String maNCC, String tenNCC, String sdt) {
        List<NhaCungCap> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM NhaCungCap WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (maNCC != null && !maNCC.trim().isEmpty()) {
            sql.append(" AND maNCC LIKE ?");
            params.add("%" + maNCC.trim() + "%");
        }
        if (tenNCC != null && !tenNCC.trim().isEmpty()) {
            sql.append(" AND tenNCC LIKE ?");
            params.add("%" + tenNCC.trim() + "%");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql.append(" AND sdt LIKE ?");
            params.add("%" + sdt.trim() + "%");
        }
        sql.append(" ORDER BY maNCC");

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++)
                ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<NhaCungCap> timTheoTuKhoa(String tuKhoa) {
        List<NhaCungCap> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap WHERE maNCC LIKE ? OR tenNCC LIKE ? OR sdt LIKE ? OR email LIKE ?";
        try (Connection con = ConnectDB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String pattern = tuKhoa == null ? "%%" : "%" + tuKhoa + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapRow(rs));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }

    public boolean themNhaCungCap(NhaCungCap ncc) {
        String sql = "INSERT INTO NhaCungCap(maNCC,tenNCC,sdt,diaChi,email) VALUES(?,?,?,?,?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getMaNCC());
            ps.setString(2, ncc.getTenNCC());
            ps.setString(3, ncc.getSdt());
            ps.setString(4, ncc.getDiaChi());
            ps.setString(5, ncc.getEmail());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean capNhatNhaCungCap(NhaCungCap ncc) {
        String sql = "UPDATE NhaCungCap SET tenNCC=?,sdt=?,diaChi=?,email=? WHERE maNCC=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getSdt());
            ps.setString(3, ncc.getDiaChi());
            ps.setString(4, ncc.getEmail());
            ps.setString(5, ncc.getMaNCC());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean xoaNhaCungCap(String maNCC) {
        String sql = "DELETE FROM NhaCungCap WHERE maNCC=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean isMaExist(String maNCC) {
        String sql = "SELECT COUNT(*) FROM NhaCungCap WHERE maNCC=?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public String sinhMaMoi() {
        String sql = "SELECT MAX(maNCC) FROM NhaCungCap";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getString(1) != null) {
                String last = rs.getString(1);
                int num = Integer.parseInt(last.replaceAll("\\D+", "")) + 1;
                return String.format("NCC%03d", num);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return "NCC001";
    }

    public List<Object[]> getPhieuNhapByNCC(String maNCC) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT pn.maPhieu, pn.ngayNhap, nv.tenNV, pn.ghiChu " +
                     "FROM PhieuNhap pn " +
                     "JOIN NhanVien nv ON pn.maNV = nv.maNV " +
                     "WHERE pn.maNCC = ? " +
                     "ORDER BY pn.ngayNhap DESC";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("maPhieu"),
                        rs.getDate("ngayNhap"),
                        rs.getString("tenNV"),
                        rs.getString("ghiChu")
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Object[]> getChiTietPhieuNhap(String maPhieu) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT sp.maSP, sp.tenSP, ct.soLuong, ct.giaNhap, " +
                     "(ct.soLuong * ct.giaNhap) AS thanhTien " +
                     "FROM ChiTietPhieuNhap ct " +
                     "JOIN SanPham sp ON ct.maSP = sp.maSP " +
                     "WHERE ct.maPhieu = ?";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Object[]{
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("giaNhap"),
                        rs.getDouble("thanhTien")
                    });
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean luuDanhGia(String maNCC, String maPhieu, int diem, String nhanXet) {
        String sql = "INSERT INTO DanhGiaGiaoNhan(maNCC,maPhieu,diemDanhGia,nhanXet,ngayDanhGia) " +
                     "VALUES(?,?,?,?,GETDATE())";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maNCC);
            ps.setString(2, maPhieu);
            ps.setInt(3, diem);
            ps.setString(4, (nhanXet == null || nhanXet.isEmpty()) ? null : nhanXet);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private NhaCungCap mapRow(ResultSet rs) throws SQLException {
        return new NhaCungCap(
            rs.getString("maNCC"),
            rs.getString("tenNCC"),
            rs.getString("sdt"),
            rs.getString("diaChi"),
            rs.getString("email")
        );
    }
}
