package gui;

import connectDB.ConnectDB;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CapNhatDiemTichLuyGUI extends JFrame {

    
    private static final int TI_GIA_TICH_DIEM = 10000;  
    private static final int TI_GIA_DOI_DIEM  = 1000;   

    private JTextField txtTimKiem;
    private JTextField txtMaKH, txtTenKH, txtSoDT, txtEmail;
    private JTextField txtDiemHienTai, txtTongChiTieu;
    private JTextField txtSoTienHoaDon, txtDiemSeThem, txtDiemSeDoi, txtGiamGia;
    private JLabel lblXepHang, lblMoTaXepHang;
    private JTable tblLichSuDiem;
    private DefaultTableModel modelLichSu;
    private JButton btnTimKiem, btnCongDiem, btnDoiDiem, btnResetDiem, btnDong;
    private JComboBox<String> cboLoaiCapNhat;
    private JTextArea txtGhiChu;
    private DecimalFormat df = new DecimalFormat("#,###");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private String maKHHienTai = null;
    private int diemHienTai = 0;

    public CapNhatDiemTichLuyGUI() {
        initComponents();
        setTitle("Cập Nhật Điểm Tích Lũy");
        setSize(1050, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(new Color(245, 247, 250));

        //  HEADER 
        JPanel pnHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        pnHeader.setBackground(new Color(155, 89, 182));
        JLabel lblTitle = new JLabel("CẬP NHẬT ĐIỂM TÍCH LŨY KHÁCH HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblTitle.setForeground(Color.WHITE);
        pnHeader.add(lblTitle);
        add(pnHeader, BorderLayout.NORTH);

        // PANEL TÌM KIẾM 
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        pnSearch.setBackground(Color.WHITE);
        pnSearch.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(8, 10, 0, 10),
            new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
                " Tìm kiếm khách hàng ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(155, 89, 182))
        ));
        pnSearch.add(new JLabel("Mã KH / SĐT:"));
        txtTimKiem = new JTextField(20);
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setPreferredSize(new Dimension(200, 30));
        pnSearch.add(txtTimKiem);
        btnTimKiem = new JButton("Tìm kiếm");
        styleButton(btnTimKiem, new Color(155, 89, 182));
        pnSearch.add(btnTimKiem);
        add(pnSearch, BorderLayout.BEFORE_FIRST_LINE);

        // MAIN PANEL 
        JPanel pnMain = new JPanel(new BorderLayout(10, 8));
        pnMain.setBackground(new Color(245, 247, 250));
        pnMain.setBorder(new EmptyBorder(5, 10, 10, 10));

     
        JPanel pnLeft = new JPanel(new BorderLayout(0, 8));
        pnLeft.setBackground(new Color(245, 247, 250));
        pnLeft.setPreferredSize(new Dimension(420, 0));

      
        JPanel pnKH = new JPanel(new GridBagLayout());
        pnKH.setBackground(Color.WHITE);
        pnKH.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Thông tin khách hàng ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(155, 89, 182)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        txtMaKH = createReadonlyField();
        txtTenKH = createReadonlyField();
        txtSoDT = createReadonlyField();
        txtEmail = createReadonlyField();
        txtDiemHienTai = createReadonlyField();
        txtDiemHienTai.setForeground(new Color(155, 89, 182));
        txtDiemHienTai.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtTongChiTieu = createReadonlyField();

        addRow(pnKH, gbc, 0, "Mã KH:", txtMaKH);
        addRow(pnKH, gbc, 1, "Họ tên:", txtTenKH);
        addRow(pnKH, gbc, 2, "SĐT:", txtSoDT);
        addRow(pnKH, gbc, 3, "Email:", txtEmail);

        gbc.gridy = 4; gbc.gridx = 0;
        pnKH.add(new JLabel("Điểm hiện tại:"), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        pnKH.add(txtDiemHienTai, gbc);
        gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;

        addRow(pnKH, gbc, 5, "Tổng chi tiêu:", txtTongChiTieu);

   
        JPanel pnXepHang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnXepHang.setBackground(Color.WHITE);
        lblXepHang = new JLabel("Thành viên");
        lblXepHang.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblXepHang.setForeground(Color.GRAY);
        lblMoTaXepHang = new JLabel("Chưa tìm khách hàng");
        lblMoTaXepHang.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        pnXepHang.add(new JLabel("Hạng:"));
        pnXepHang.add(lblXepHang);
        pnXepHang.add(lblMoTaXepHang);

        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        pnKH.add(pnXepHang, gbc);

        // Panel thao tác cập nhật điểm
        JPanel pnThaoTac = new JPanel(new GridBagLayout());
        pnThaoTac.setBackground(Color.WHITE);
        pnThaoTac.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Thao tác điểm tích lũy ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(155, 89, 182)));

        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(6, 10, 6, 10);
        gbc2.anchor = GridBagConstraints.WEST;

        // Loại cập nhật
        gbc2.gridy = 0; gbc2.gridx = 0;
        pnThaoTac.add(new JLabel("Loại thao tác:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1.0;
        cboLoaiCapNhat = new JComboBox<>(new String[]{
            "Cộng điểm từ hóa đơn", "Đổi điểm lấy ưu đãi", "Điều chỉnh thủ công"
        });
        cboLoaiCapNhat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pnThaoTac.add(cboLoaiCapNhat, gbc2);

        // Số tiền hóa đơn
        gbc2.gridy = 1; gbc2.gridx = 0; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        pnThaoTac.add(new JLabel("Số tiền HĐ (đ):"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1.0;
        txtSoTienHoaDon = new JTextField();
        txtSoTienHoaDon.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnThaoTac.add(txtSoTienHoaDon, gbc2);

        // Điểm sẽ thêm
        gbc2.gridy = 2; gbc2.gridx = 0; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        pnThaoTac.add(new JLabel("Điểm sẽ cộng:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1.0;
        txtDiemSeThem = createReadonlyField();
        txtDiemSeThem.setForeground(new Color(39, 174, 96));
        txtDiemSeThem.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnThaoTac.add(txtDiemSeThem, gbc2);

        // Điểm muốn đổi
        gbc2.gridy = 3; gbc2.gridx = 0; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        pnThaoTac.add(new JLabel("Điểm muốn đổi:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1.0;
        txtDiemSeDoi = new JTextField();
        txtDiemSeDoi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pnThaoTac.add(txtDiemSeDoi, gbc2);

        // Số tiền giảm giá khi đổi điểm
        gbc2.gridy = 4; gbc2.gridx = 0; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        pnThaoTac.add(new JLabel("Giảm giá được:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1.0;
        txtGiamGia = createReadonlyField();
        txtGiamGia.setForeground(new Color(192, 57, 43));
        txtGiamGia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pnThaoTac.add(txtGiamGia, gbc2);

        // Ghi chú
        gbc2.gridy = 5; gbc2.gridx = 0; gbc2.fill = GridBagConstraints.NONE; gbc2.weightx = 0;
        pnThaoTac.add(new JLabel("Ghi chú:"), gbc2);
        gbc2.gridx = 1; gbc2.fill = GridBagConstraints.HORIZONTAL; gbc2.weightx = 1.0;
        txtGhiChu = new JTextArea(2, 10);
        txtGhiChu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtGhiChu.setLineWrap(true);
        txtGhiChu.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        pnThaoTac.add(new JScrollPane(txtGhiChu), gbc2);

        // Nút thao tác
        JPanel pnBtnThaoTac = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnBtnThaoTac.setBackground(Color.WHITE);
        btnCongDiem = new JButton("Cộng điểm");
        styleButton(btnCongDiem, new Color(39, 174, 96));
        btnDoiDiem = new JButton(" Đổi điểm");
        styleButton(btnDoiDiem, new Color(230, 126, 34));
        btnResetDiem = new JButton(" Reset");
        styleButton(btnResetDiem, new Color(127, 140, 141));
        pnBtnThaoTac.add(btnCongDiem);
        pnBtnThaoTac.add(btnDoiDiem);
        pnBtnThaoTac.add(btnResetDiem);

        gbc2.gridy = 6; gbc2.gridx = 0; gbc2.gridwidth = 2; gbc2.fill = GridBagConstraints.HORIZONTAL;
        pnThaoTac.add(pnBtnThaoTac, gbc2);

        // Chú thích tỷ lệ
        JPanel pnChuThich = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        pnChuThich.setBackground(new Color(236, 240, 241));
        pnChuThich.setBorder(new EmptyBorder(4, 8, 4, 8));
        JLabel lblChuThich = new JLabel(
            "Tỷ lệ: " + df.format(TI_GIA_TICH_DIEM) + "đ = 1 điểm  |  1 điểm = " + df.format(TI_GIA_DOI_DIEM) + "đ"
        );
        lblChuThich.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblChuThich.setForeground(new Color(100, 100, 100));
        pnChuThich.add(lblChuThich);

        pnLeft.add(pnKH, BorderLayout.NORTH);
        pnLeft.add(pnThaoTac, BorderLayout.CENTER);
        pnLeft.add(pnChuThich, BorderLayout.SOUTH);

        // RIGHT: Lịch sử điểm 
        JPanel pnRight = new JPanel(new BorderLayout());
        pnRight.setBackground(new Color(245, 247, 250));

        String[] cols = {"Thời gian", "Loại", "Điểm thay đổi", "Điểm sau", "Ghi chú"};
        modelLichSu = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblLichSuDiem = new JTable(modelLichSu);
        styleTable(tblLichSuDiem);
        tblLichSuDiem.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblLichSuDiem.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblLichSuDiem.getColumnModel().getColumn(2).setPreferredWidth(90);
        tblLichSuDiem.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblLichSuDiem.getColumnModel().getColumn(4).setPreferredWidth(150);

        // Màu cho điểm tăng/giảm
        tblLichSuDiem.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected && col == 2) {
                    String val = value != null ? value.toString() : "";
                    if (val.startsWith("+")) c.setForeground(new Color(39, 174, 96));
                    else if (val.startsWith("-")) c.setForeground(new Color(192, 57, 43));
                    else c.setForeground(Color.BLACK);
                }
                return c;
            }
        });

        pnRight.setBorder(new TitledBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)),
            " Lịch sử điểm tích lũy ", TitledBorder.LEFT, TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12), new Color(155, 89, 182)));
        pnRight.add(new JScrollPane(tblLichSuDiem), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeft, pnRight);
        splitPane.setDividerLocation(430);
        splitPane.setResizeWeight(0.4);

        pnMain.add(splitPane, BorderLayout.CENTER);

        // Nút đóng
        JPanel pnBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        pnBottom.setBackground(new Color(245, 247, 250));
        btnDong = new JButton("Đóng");
        styleButton(btnDong, new Color(192, 57, 43));
        pnBottom.add(btnDong);
        pnMain.add(pnBottom, BorderLayout.SOUTH);

        add(pnMain, BorderLayout.CENTER);

        //SỰ KIỆN
        btnTimKiem.addActionListener(e -> timKiemKhachHang());
        txtTimKiem.addActionListener(e -> timKiemKhachHang());

        txtSoTienHoaDon.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { tinhDiemSeThem(); }
        });

        txtDiemSeDoi.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { tinhGiamGia(); }
        });

        btnCongDiem.addActionListener(e -> congDiem());
        btnDoiDiem.addActionListener(e -> doiDiem());
        btnResetDiem.addActionListener(e -> resetForm());
        btnDong.addActionListener(e -> dispose());
    }

    private void timKiemKhachHang() {
        String keyword = txtTimKiem.getText().trim();
        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KH hoặc SĐT!", 
                "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Connection conn = ConnectDB.getConnection();
            String sql = "SELECT maKH, hoTen, soDienThoai, email, diemTichLuy, " +
                         "(SELECT ISNULL(SUM(tongTien - ISNULL(giamGia,0)),0) FROM HoaDon WHERE maKhachHang = kh.maKH) as tongChiTieu " +
                         "FROM KhachHang kh WHERE maKH = ? OR soDienThoai = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, keyword);
            ps.setString(2, keyword);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                maKHHienTai = rs.getString("maKH");
                diemHienTai = rs.getInt("diemTichLuy");
                txtMaKH.setText(maKHHienTai);
                txtTenKH.setText(rs.getString("hoTen"));
                txtSoDT.setText(rs.getString("soDienThoai"));
                txtEmail.setText(rs.getString("email") != null ? rs.getString("email") : "Chưa có");
                txtDiemHienTai.setText(df.format(diemHienTai) + " điểm");
                txtTongChiTieu.setText(df.format(rs.getLong("tongChiTieu")) + " đ");
                capNhatXepHang(diemHienTai);
                loadLichSuDiem(maKHHienTai, conn);
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                resetForm();
            }
            rs.close(); ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi CSDL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void congDiem() {
        if (maKHHienTai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng tìm khách hàng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String soTienStr = txtSoTienHoaDon.getText().trim().replaceAll(",", "");
        if (soTienStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền hóa đơn!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            long soTien = Long.parseLong(soTienStr);
            if (soTien <= 0) throw new NumberFormatException();
            int diemThem = (int)(soTien / TI_GIA_TICH_DIEM);
            if (diemThem == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Số tiền quá nhỏ để tích điểm!\nTối thiểu " + df.format(TI_GIA_TICH_DIEM) + "đ = 1 điểm",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Cộng %d điểm cho khách hàng %s?\nHóa đơn: %s đ", 
                    diemThem, txtTenKH.getText(), df.format(soTien)),
                "Xác nhận", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                capNhatDiemTrongDB(diemThem, "Cộng điểm", 
                    "HĐ " + df.format(soTien) + "đ. " + txtGhiChu.getText().trim());
                JOptionPane.showMessageDialog(this, 
                    "Cộng thành công " + diemThem + " điểm!\nTổng điểm hiện tại: " + df.format(diemHienTai) + " điểm",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doiDiem() {
        if (maKHHienTai == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng tìm khách hàng trước!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String diemStr = txtDiemSeDoi.getText().trim();
        if (diemStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điểm muốn đổi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int diemDoi = Integer.parseInt(diemStr);
            if (diemDoi <= 0) throw new NumberFormatException();
            if (diemDoi > diemHienTai) {
                JOptionPane.showMessageDialog(this, 
                    "Số điểm đổi (" + diemDoi + ") vượt quá điểm hiện có (" + diemHienTai + ")!",
                    "Không đủ điểm", JOptionPane.WARNING_MESSAGE);
                return;
            }
            long giamGia = (long) diemDoi * TI_GIA_DOI_DIEM;
            int confirm = JOptionPane.showConfirmDialog(this,
                String.format("Đổi %d điểm để được giảm giá %s đ?\nKhách hàng: %s\nĐiểm sau khi đổi: %d",
                    diemDoi, df.format(giamGia), txtTenKH.getText(), diemHienTai - diemDoi),
                "Xác nhận đổi điểm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                capNhatDiemTrongDB(-diemDoi, "Đổi điểm",
                    "Đổi " + diemDoi + " điểm = " + df.format(giamGia) + "đ. " + txtGhiChu.getText().trim());
                JOptionPane.showMessageDialog(this,
                    "✅ Đổi thành công " + diemDoi + " điểm!\nGiảm giá: " + df.format(giamGia) + " đ\nĐiểm còn lại: " + df.format(diemHienTai),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số điểm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatDiemTrongDB(int thayDoi, String loai, String ghiChu) {
        try {
            Connection conn = ConnectDB.getConnection();
            conn.setAutoCommit(false);

            // Cập nhật điểm trong bảng KhachHang
            String sqlUpdate = "UPDATE KhachHang SET diemTichLuy = diemTichLuy + ? WHERE maKH = ?";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, thayDoi);
            psUpdate.setString(2, maKHHienTai);
            psUpdate.executeUpdate();

            // Ghi lịch sử vào bảng LichSuDiemTichLuy (nếu có)
            try {
                String sqlHistory = "INSERT INTO LichSuDiemTichLuy (maKH, thoiGian, loaiThaoTac, diemThayDoi, diemSau, ghiChu) " +
                                    "VALUES (?, GETDATE(), ?, ?, ?, ?)";
                PreparedStatement psHistory = conn.prepareStatement(sqlHistory);
                psHistory.setString(1, maKHHienTai);
                psHistory.setString(2, loai);
                psHistory.setInt(3, thayDoi);
                psHistory.setInt(4, diemHienTai + thayDoi);
                psHistory.setString(5, ghiChu);
                psHistory.executeUpdate();
                psHistory.close();
            } catch (SQLException ignored) {
                // Bảng lịch sử có thể chưa tồn tại, bỏ qua
            }

            conn.commit();
            conn.setAutoCommit(true);
            psUpdate.close();

            // Cập nhật giao diện
            diemHienTai += thayDoi;
            txtDiemHienTai.setText(df.format(diemHienTai) + " điểm");
            capNhatXepHang(diemHienTai);
            loadLichSuDiem(maKHHienTai, conn);
            txtSoTienHoaDon.setText("");
            txtDiemSeThem.setText("");
            txtDiemSeDoi.setText("");
            txtGiamGia.setText("");
            txtGhiChu.setText("");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật CSDL: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLichSuDiem(String maKH, Connection conn) {
        modelLichSu.setRowCount(0);
        try {
            // Thử load từ bảng lịch sử nếu có
            String sql = "SELECT thoiGian, loaiThaoTac, diemThayDoi, diemSau, ghiChu " +
                         "FROM LichSuDiemTichLuy WHERE maKH = ? ORDER BY thoiGian DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, maKH);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int thayDoi = rs.getInt("diemThayDoi");
                modelLichSu.addRow(new Object[]{
                    sdf.format(rs.getTimestamp("thoiGian")),
                    rs.getString("loaiThaoTac"),
                    (thayDoi >= 0 ? "+" : "") + thayDoi,
                    df.format(rs.getInt("diemSau")),
                    rs.getString("ghiChu")
                });
            }
            rs.close(); ps.close();
        } catch (SQLException e) {
            // Bảng chưa có, tạo SQL gợi ý
            modelLichSu.addRow(new Object[]{
                sdf.format(new Date()), "Truy vấn", "-", df.format(diemHienTai), "Điểm hiện tại"
            });
        }
    }

    private void capNhatXepHang(int diem) {
        if (diem >= 5000) {
            lblXepHang.setText("Kim Cương");
            lblXepHang.setForeground(new Color(0, 150, 255));
            lblMoTaXepHang.setText("(≥ 5.000 điểm - Ưu đãi đặc biệt)");
        } else if (diem >= 2000) {
            lblXepHang.setText("Vàng");
            lblXepHang.setForeground(new Color(218, 165, 32));
            lblMoTaXepHang.setText("(≥ 2.000 điểm - Giảm 10%)");
        } else if (diem >= 500) {
            lblXepHang.setText("Bạc");
            lblXepHang.setForeground(new Color(150, 150, 150));
            lblMoTaXepHang.setText("(≥ 500 điểm - Giảm 5%)");
        } else {
            lblXepHang.setText("Đồng");
            lblXepHang.setForeground(new Color(176, 141, 87));
            lblMoTaXepHang.setText("(< 500 điểm - Thành viên cơ bản)");
        }
    }

    private void tinhDiemSeThem() {
        try {
            String s = txtSoTienHoaDon.getText().trim().replaceAll(",", "");
            if (!s.isEmpty()) {
                long soTien = Long.parseLong(s);
                int diem = (int)(soTien / TI_GIA_TICH_DIEM);
                txtDiemSeThem.setText("+ " + diem + " điểm");
            }
        } catch (NumberFormatException ignored) {}
    }

    private void tinhGiamGia() {
        try {
            String s = txtDiemSeDoi.getText().trim();
            if (!s.isEmpty()) {
                int diem = Integer.parseInt(s);
                long giamGia = (long) diem * TI_GIA_DOI_DIEM;
                txtGiamGia.setText("- " + df.format(giamGia) + " đ");
            }
        } catch (NumberFormatException ignored) {}
    }

    private void resetForm() {
        maKHHienTai = null;
        diemHienTai = 0;
        txtMaKH.setText(""); txtTenKH.setText(""); txtSoDT.setText("");
        txtEmail.setText(""); txtDiemHienTai.setText(""); txtTongChiTieu.setText("");
        txtSoTienHoaDon.setText(""); txtDiemSeThem.setText("");
        txtDiemSeDoi.setText(""); txtGiamGia.setText(""); txtGhiChu.setText("");
        lblXepHang.setText("🥉 Thành viên"); lblXepHang.setForeground(Color.GRAY);
        lblMoTaXepHang.setText("Chưa tìm khách hàng");
        modelLichSu.setRowCount(0);
    }

    private JTextField createReadonlyField() {
        JTextField tf = new JTextField();
        tf.setEditable(false);
        tf.setBackground(new Color(236, 240, 241));
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(2, 5, 2, 5)));
        return tf;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridy = row; gbc.gridx = 0; gbc.fill = GridBagConstraints.NONE; gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(216, 191, 216));
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(155, 89, 182));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(
                new DefaultTableCellRenderer() {{
                    setHorizontalAlignment(JLabel.CENTER);
                    setBackground(new Color(155, 89, 182));
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));
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
        btn.setPreferredSize(new Dimension(135, 33));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(color.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(color); }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CapNhatDiemTichLuyGUI().setVisible(true));
    }
}