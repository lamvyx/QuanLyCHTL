package gui;

import dao.KhachHangDAO;
import entity.KhachHang;
import entity.HoaDon;
import entity.ChiTietHoaDon;
import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class LichSuMuaHangGUI extends JFrame {

    private JTextField txtMaKH, txtTimKiem;
    private JLabel lblTenKH, lblSoDT, lblEmail, lblDiemTichLuy, lblTongChiTieu;
    private JTable tblHoaDon, tblChiTiet;
    private DefaultTableModel modelHoaDon, modelChiTiet;
    private JButton btnTimKiem, btnXemChiTiet, btnDong;
    private JComboBox<String> cboLoc;
    private DecimalFormat df = new DecimalFormat("#,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public LichSuMuaHangGUI() {
        initComponents();
        setTitle("Lịch Sử Mua Hàng");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 247, 250));

        //HEADER 
        JPanel pnHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnHeader.setBackground(new Color(41, 128, 185));
        pnHeader.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel lblTitle = new JLabel("LỊCH SỬ MUA HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        pnHeader.add(lblTitle);

        add(pnHeader, BorderLayout.NORTH);

        //PANEL TÌM KIẾM
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnSearch.setBackground(Color.WHITE);
        pnSearch.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(8, 10, 8, 10),
            new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)), 
                "Tìm kiếm khách hàng ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(41, 128, 185))
        ));

        pnSearch.add(new JLabel("Mã / SĐT / Tên KH:"));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        pnSearch.add(txtTimKiem);

        btnTimKiem = new JButton("Tìm kiếm");
        styleButton(btnTimKiem, new Color(41, 128, 185));
        pnSearch.add(btnTimKiem);

        pnSearch.add(new JLabel("  Lọc theo:"));
        cboLoc = new JComboBox<>(new String[]{"Tất cả", "Tháng này", "Quý này", "Năm này"});
        cboLoc.setPreferredSize(new Dimension(120, 30));
        pnSearch.add(cboLoc);

        add(pnSearch, BorderLayout.BEFORE_FIRST_LINE);

        //MAIN CONTENT
        JPanel pnMain = new JPanel(new BorderLayout(10, 10));
        pnMain.setBackground(new Color(245, 247, 250));
        pnMain.setBorder(new EmptyBorder(0, 10, 10, 10));

        // Panel thông tin khách hàng
        JPanel pnKhachHang = new JPanel(new GridLayout(2, 4, 15, 8));
        pnKhachHang.setBackground(Color.WHITE);
        pnKhachHang.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(5, 0, 5, 0),
            new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
                "Thông tin khách hàng ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(41, 128, 185))
        ));

        lblTenKH = createInfoLabel("Họ tên: -");
        lblSoDT = createInfoLabel("SĐT: -");
        lblEmail = createInfoLabel("Email: -");
        lblDiemTichLuy = createInfoLabel("Điểm tích lũy: -");
        lblTongChiTieu = createInfoLabel("Tổng chi tiêu: -");

        pnKhachHang.add(wrapLabel("Họ tên:", lblTenKH));
        pnKhachHang.add(wrapLabel("SĐT:", lblSoDT));
        pnKhachHang.add(wrapLabel("Email:", lblEmail));
        pnKhachHang.add(new JLabel());
        pnKhachHang.add(wrapLabel("Điểm tích lũy:", lblDiemTichLuy));
        pnKhachHang.add(wrapLabel("Tổng chi tiêu:", lblTongChiTieu));
        pnKhachHang.add(new JLabel());
        pnKhachHang.add(new JLabel());

        // Split pane giữa bảng hóa đơn và chi tiết
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(450);
        splitPane.setResizeWeight(0.45);

        // Bảng danh sách hóa đơn
        String[] colsHD = {"Mã HĐ", "Ngày mua", "Tổng tiền", "Giảm giá", "Thanh toán", "NV lập"};
        modelHoaDon = new DefaultTableModel(colsHD, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblHoaDon = new JTable(modelHoaDon);
        styleTable(tblHoaDon);
        tblHoaDon.getColumnModel().getColumn(0).setPreferredWidth(60);
        tblHoaDon.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblHoaDon.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblHoaDon.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblHoaDon.getColumnModel().getColumn(5).setPreferredWidth(80);

        JPanel pnHoaDon = new JPanel(new BorderLayout());
        pnHoaDon.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
            "Danh sách hóa đơn ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(41, 128, 185)));
        pnHoaDon.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        JPanel pnThongKe = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnThongKe.setBackground(new Color(236, 240, 241));
        JLabel lblThongKe = new JLabel("  Tổng số hóa đơn: 0  |  Tổng chi tiêu: 0 đ");
        lblThongKe.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        pnThongKe.add(lblThongKe);
        pnHoaDon.add(pnThongKe, BorderLayout.SOUTH);

        // Bảng chi tiết hóa đơn
        String[] colsCT = {"Tên sản phẩm", "ĐVT", "SL", "Đơn giá", "Thành tiền"};
        modelChiTiet = new DefaultTableModel(colsCT, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = new JTable(modelChiTiet);
        styleTable(tblChiTiet);

        JPanel pnChiTiet = new JPanel(new BorderLayout());
        pnChiTiet.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " 📦 Chi tiết hóa đơn ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(41, 128, 185)));
        pnChiTiet.add(new JScrollPane(tblChiTiet), BorderLayout.CENTER);

        splitPane.setLeftComponent(pnHoaDon);
        splitPane.setRightComponent(pnChiTiet);

        JPanel pnTop = new JPanel(new BorderLayout(0, 5));
        pnTop.setBackground(new Color(245, 247, 250));
        pnTop.add(pnKhachHang, BorderLayout.NORTH);
        pnTop.add(splitPane, BorderLayout.CENTER);

        pnMain.add(pnTop, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel pnButton = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnButton.setBackground(new Color(245, 247, 250));

        btnXemChiTiet = new JButton("Xem chi tiết");
        styleButton(btnXemChiTiet, new Color(39, 174, 96));
        pnButton.add(btnXemChiTiet);

        btnDong = new JButton("Đóng");
        styleButton(btnDong, new Color(192, 57, 43));
        pnButton.add(btnDong);

        pnMain.add(pnButton, BorderLayout.SOUTH);
        add(pnMain, BorderLayout.CENTER);

        // SỰ KIỆN 
        btnTimKiem.addActionListener(e -> timKiemKhachHang());
        txtTimKiem.addActionListener(e -> timKiemKhachHang());

        tblHoaDon.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblHoaDon.getSelectedRow();
                if (row >= 0) {
                    String maHD = modelHoaDon.getValueAt(row, 0).toString();
                    loadChiTietHoaDon(maHD);
                }
            }
        });

        btnXemChiTiet.addActionListener(e -> {
            int row = tblHoaDon.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một hóa đơn!", 
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDong.addActionListener(e -> dispose());
    }

    private void timKiemKhachHang() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KH, SĐT hoặc tên để tìm kiếm!",
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection conn = ConnectDB.getConnection();
            String sql = "SELECT maKH, hoTen, soDienThoai, email, diemTichLuy " +
                         "FROM KhachHang " +
                         "WHERE maKH LIKE ? OR soDienThoai LIKE ? OR hoTen LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            String kw = "%" + keyword + "%";
            ps.setString(1, kw);
            ps.setString(2, kw);
            ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String maKH = rs.getString("maKH");
                lblTenKH.setText(rs.getString("hoTen"));
                lblSoDT.setText(rs.getString("soDienThoai"));
                lblEmail.setText(rs.getString("email") != null ? rs.getString("email") : "Chưa có");
                lblDiemTichLuy.setText(df.format(rs.getInt("diemTichLuy")) + " điểm");

                loadHoaDon(maKH, conn);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối CSDL: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadHoaDon(String maKH, Connection conn) throws SQLException {
        modelHoaDon.setRowCount(0);
        modelChiTiet.setRowCount(0);

        String locTheoThoiGian = "";
        String locStr = cboLoc.getSelectedItem().toString();
        if (locStr.equals("Tháng này")) {
            locTheoThoiGian = " AND MONTH(hd.ngayLap) = MONTH(GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())";
        } else if (locStr.equals("Quý này")) {
            locTheoThoiGian = " AND DATEPART(QUARTER, hd.ngayLap) = DATEPART(QUARTER, GETDATE()) AND YEAR(hd.ngayLap) = YEAR(GETDATE())";
        } else if (locStr.equals("Năm này")) {
            locTheoThoiGian = " AND YEAR(hd.ngayLap) = YEAR(GETDATE())";
        }

        String sql = "SELECT hd.maHoaDon, hd.ngayLap, hd.tongTien, hd.giamGia, " +
                     "(hd.tongTien - ISNULL(hd.giamGia, 0)) as thanhToan, " +
                     "nv.hoTen as tenNV " +
                     "FROM HoaDon hd " +
                     "LEFT JOIN NhanVien nv ON hd.maNhanVien = nv.maNhanVien " +
                     "WHERE hd.maKhachHang = ?" + locTheoThoiGian +
                     " ORDER BY hd.ngayLap DESC";

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maKH);
        ResultSet rs = ps.executeQuery();

        long tongChiTieu = 0;
        int soHoaDon = 0;

        while (rs.next()) {
            long thanhToan = rs.getLong("thanhToan");
            tongChiTieu += thanhToan;
            soHoaDon++;

            modelHoaDon.addRow(new Object[]{
                rs.getString("maHoaDon"),
                sdf.format(rs.getTimestamp("ngayLap")),
                df.format(rs.getLong("tongTien")) + " đ",
                df.format(rs.getLong("giamGia")) + " đ",
                df.format(thanhToan) + " đ",
                rs.getString("tenNV")
            });
        }

        lblTongChiTieu.setText(df.format(tongChiTieu) + " đ");
        rs.close();
        ps.close();

        if (soHoaDon == 0) {
            JOptionPane.showMessageDialog(this, "Khách hàng này chưa có hóa đơn nào!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadChiTietHoaDon(String maHD) {
        modelChiTiet.setRowCount(0);
        try {
            Connection conn = ConnectDB.getConnection();
            String sql = "SELECT sp.tenSanPham, sp.donViTinh, ct.soLuong, ct.donGia, " +
                         "(ct.soLuong * ct.donGia) as thanhTien " +
                         "FROM ChiTietHoaDon ct " +
                         "JOIN SanPham sp ON ct.maSanPham = sp.maSanPham " +
                         "WHERE ct.maHoaDon = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                modelChiTiet.addRow(new Object[]{
                    rs.getString("tenSanPham"),
                    rs.getString("donViTinh"),
                    rs.getInt("soLuong"),
                    df.format(rs.getLong("donGia")) + " đ",
                    df.format(rs.getLong("thanhTien")) + " đ"
                });
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi tải chi tiết hóa đơn: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        lblTenKH.setText("-");
        lblSoDT.setText("-");
        lblEmail.setText("-");
        lblDiemTichLuy.setText("-");
        lblTongChiTieu.setText("-");
        modelHoaDon.setRowCount(0);
        modelChiTiet.setRowCount(0);
    }

    private JLabel createInfoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(41, 128, 185));
        return lbl;
    }

    private JPanel wrapLabel(String caption, JLabel value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        p.setBackground(Color.WHITE);
        JLabel cap = new JLabel(caption);
        cap.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cap.setForeground(Color.GRAY);
        p.add(cap);
        p.add(value);
        return p;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(173, 216, 230));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Căn giữa header
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(
                new DefaultTableCellRenderer() {{
                    setHorizontalAlignment(JLabel.CENTER);
                    setBackground(new Color(41, 128, 185));
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
                    setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(100, 160, 200)));
                }}
            );
        }
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(140, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            Color original = color;
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(original.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(original);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LichSuMuaHangGUI().setVisible(true);
        });
    }
}