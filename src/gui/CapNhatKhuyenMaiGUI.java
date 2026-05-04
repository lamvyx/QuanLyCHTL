package gui;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CapNhatKhuyenMaiGUI extends JDialog {

    private static final Color NAVY   = new Color(15, 23, 42);
    private static final Color BLUE   = new Color(37, 99, 235);
    private static final Color ORANGE = new Color(234, 88, 12);
    private static final Color BG     = new Color(248, 250, 252);
    private static final Color CARD   = Color.WHITE;
    private static final Color BORDER_CLR  = new Color(226, 232, 240);
    private static final Color LABEL_CLR   = new Color(55, 65, 81);
    private static final Color ERROR_CLR   = new Color(220, 38, 38);
    private static final Color SUCCESS_CLR = new Color(5, 150, 105);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Tra cứu ────────────────────────────────────────
    private JTextField txtTimMa;

    // ── Form chỉnh sửa ─────────────────────────────────
    private JTextField txtMaKM, txtTenKM, txtPhanTram, txtNgayBD, txtNgayKT;
    private JTextArea  txtMoTa;
    private JLabel     lblThongBao;

    // ── Nút ─────────────────────────────────────────────
    private JButton btnCapNhat, btnXoa;

    private KhuyenMai kmDangChon;
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public CapNhatKhuyenMaiGUI(JFrame parent) {
        super(parent, "Cập nhật khuyến mãi", true);
        setSize(520, 620);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        toggleFormEnabled(false);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ── Header ──────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lblTitle = new JLabel("✎  Cập nhật khuyến mãi");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblTitle, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // ── Tra cứu theo mã ─────────────────────────────
        JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
        searchPanel.setBackground(CARD);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
                new EmptyBorder(12, 20, 12, 20)
        ));

        JLabel lblSearch = new JLabel("Nhập mã khuyến mãi cần sửa:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSearch.setForeground(LABEL_CLR);

        txtTimMa = new JTextField();
        txtTimMa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimMa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                new EmptyBorder(7, 10, 7, 10)
        ));

        JButton btnTim = taoNut("Tìm", BLUE, Color.WHITE);
        btnTim.addActionListener(e -> timKiemVaHienThi());
        txtTimMa.addActionListener(e -> timKiemVaHienThi());

        searchPanel.add(lblSearch, BorderLayout.NORTH);
        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setOpaque(false);
        searchRow.setBorder(new EmptyBorder(6, 0, 0, 0));
        searchRow.add(txtTimMa, BorderLayout.CENTER);
        searchRow.add(btnTim, BorderLayout.EAST);
        searchPanel.add(searchRow, BorderLayout.CENTER);

        // ── Form chỉnh sửa ───────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD);
        form.setBorder(new EmptyBorder(16, 24, 8, 24));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        // Mã KM (chỉ đọc sau khi tìm)
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel lblMa = new JLabel("Mã khuyến mãi (chỉ đọc)");
        styleLabel(lblMa); form.add(lblMa, gbc);
        gbc.gridy = row++;
        txtMaKM = new JTextField();
        styleField(txtMaKM, true);
        form.add(txtMaKM, gbc);

        gbc.gridy = row++;
        JLabel lblTen = new JLabel("Tên khuyến mãi *");
        styleLabel(lblTen); form.add(lblTen, gbc);
        gbc.gridy = row++;
        txtTenKM = new JTextField();
        styleField(txtTenKM, false);
        form.add(txtTenKM, gbc);

        gbc.gridy = row++;
        JLabel lblPt = new JLabel("Phần trăm giảm (0–100) *");
        styleLabel(lblPt); form.add(lblPt, gbc);
        gbc.gridy = row++;
        txtPhanTram = new JTextField();
        styleField(txtPhanTram, false);
        form.add(txtPhanTram, gbc);

        gbc.gridy = row++;
        JLabel lblBD = new JLabel("Ngày bắt đầu * (dd/MM/yyyy)");
        styleLabel(lblBD); form.add(lblBD, gbc);
        gbc.gridy = row++;
        txtNgayBD = new JTextField();
        styleField(txtNgayBD, false);
        form.add(txtNgayBD, gbc);

        gbc.gridy = row++;
        JLabel lblKT = new JLabel("Ngày kết thúc * (dd/MM/yyyy)");
        styleLabel(lblKT); form.add(lblKT, gbc);
        gbc.gridy = row++;
        txtNgayKT = new JTextField();
        styleField(txtNgayKT, false);
        form.add(txtNgayKT, gbc);

        gbc.gridy = row++;
        JLabel lblMoTa = new JLabel("Mô tả");
        styleLabel(lblMoTa); form.add(lblMoTa, gbc);
        gbc.gridy = row++;
        txtMoTa = new JTextArea(3, 20);
        txtMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtMoTa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                new EmptyBorder(6, 8, 6, 8)
        ));
        JScrollPane scrollMoTa = new JScrollPane(txtMoTa);
        scrollMoTa.setBorder(null);
        form.add(scrollMoTa, gbc);

        // Thông báo
        gbc.gridy = row++;
        lblThongBao = new JLabel(" ");
        lblThongBao.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblThongBao.setHorizontalAlignment(SwingConstants.CENTER);
        form.add(lblThongBao, gbc);

        // ── Cuộn form ────────────────────────────────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(CARD);
        body.add(searchPanel, BorderLayout.NORTH);
        body.add(form, BorderLayout.CENTER);

        JScrollPane scrollBody = new JScrollPane(body);
        scrollBody.setBorder(null);
        scrollBody.getViewport().setBackground(CARD);
        root.add(scrollBody, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        footer.setBackground(BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));

        btnXoa     = taoNut("Xóa khuyến mãi", new Color(220, 38, 38), Color.WHITE);
        btnCapNhat = taoNut("Cập nhật", BLUE, Color.WHITE);
        JButton btnDong = taoNut("Đóng", new Color(229, 231, 235), NAVY);

        btnCapNhat.addActionListener(e -> xuLyCapNhat());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnDong.addActionListener(e -> dispose());

        footer.add(btnXoa);
        footer.add(btnDong);
        footer.add(btnCapNhat);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Tìm kiếm và điền form ────────────────────────────

    private void timKiemVaHienThi() {
        String maKM = txtTimMa.getText().trim();
        if (maKM.isEmpty()) {
            hienThiThongBao("⚠ Vui lòng nhập mã khuyến mãi", true);
            return;
        }
        KhuyenMai km = khuyenMaiDAO.timTheoMa(maKM);
        if (km == null) {
            hienThiThongBao("⚠ Không tìm thấy khuyến mãi với mã \"" + maKM + "\"", true);
            toggleFormEnabled(false);
            xoaForm();
            return;
        }
        kmDangChon = km;
        dienForm(km);
        toggleFormEnabled(true);
        hienThiThongBao("✔ Đã tải thông tin. Chỉnh sửa và bấm Cập nhật.", false);
    }

    private void dienForm(KhuyenMai km) {
        txtMaKM.setText(km.getMaKM());
        txtTenKM.setText(km.getTenKM());
        txtPhanTram.setText(String.valueOf(km.getPhanTramGiam()));
        txtNgayBD.setText(km.getNgayBD() != null ? km.getNgayBD().format(FMT) : "");
        txtNgayKT.setText(km.getNgayKT() != null ? km.getNgayKT().format(FMT) : "");
        txtMoTa.setText(km.getMoTa() != null ? km.getMoTa() : "");
    }

    // ── Xử lý cập nhật ──────────────────────────────────

    private void xuLyCapNhat() {
        if (kmDangChon == null) return;

        String tenKM      = txtTenKM.getText().trim();
        String phanTramStr = txtPhanTram.getText().trim();
        String ngayBDStr   = txtNgayBD.getText().trim();
        String ngayKTStr   = txtNgayKT.getText().trim();
        String moTa        = txtMoTa.getText().trim();

        if (tenKM.isEmpty() || phanTramStr.isEmpty() || ngayBDStr.isEmpty() || ngayKTStr.isEmpty()) {
            hienThiThongBao("⚠ Vui lòng điền đầy đủ các trường bắt buộc (*)", true);
            return;
        }

        double phanTram;
        try {
            phanTram = Double.parseDouble(phanTramStr);
            if (phanTram < 0 || phanTram > 100) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            hienThiThongBao("⚠ Phần trăm giảm phải là số từ 0 đến 100", true);
            return;
        }

        LocalDate ngayBD, ngayKT;
        try {
            ngayBD = LocalDate.parse(ngayBDStr, FMT);
        } catch (DateTimeParseException ex) {
            hienThiThongBao("⚠ Ngày bắt đầu sai định dạng dd/MM/yyyy", true);
            return;
        }
        try {
            ngayKT = LocalDate.parse(ngayKTStr, FMT);
        } catch (DateTimeParseException ex) {
            hienThiThongBao("⚠ Ngày kết thúc sai định dạng dd/MM/yyyy", true);
            return;
        }
        if (!ngayKT.isAfter(ngayBD)) {
            hienThiThongBao("⚠ Ngày kết thúc phải sau ngày bắt đầu", true);
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận cập nhật khuyến mãi \"" + kmDangChon.getMaKM() + "\"?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        kmDangChon.setTenKM(tenKM);
        kmDangChon.setPhanTramGiam(phanTram);
        kmDangChon.setNgayBD(ngayBD);
        kmDangChon.setNgayKT(ngayKT);
        kmDangChon.setMoTa(moTa);

        if (khuyenMaiDAO.capNhat(kmDangChon)) {
            hienThiThongBao("✔ Cập nhật thành công!", false);
        } else {
            hienThiThongBao("✖ Lỗi khi cập nhật cơ sở dữ liệu", true);
        }
    }

    // ── Xử lý xóa ───────────────────────────────────────

    private void xuLyXoa() {
        if (kmDangChon == null) return;
        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn XÓA khuyến mãi \"" + kmDangChon.getMaKM() + "\" không?\n" +
                "Hành động này không thể hoàn tác!",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        if (khuyenMaiDAO.xoa(kmDangChon.getMaKM())) {
            JOptionPane.showMessageDialog(this, "Đã xóa khuyến mãi thành công.",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            kmDangChon = null;
            xoaForm();
            toggleFormEnabled(false);
            txtTimMa.setText("");
            lblThongBao.setText(" ");
        } else {
            hienThiThongBao("✖ Không thể xóa (có thể đang dùng trong hóa đơn)", true);
        }
    }

    // ── Helpers ─────────────────────────────────────────

    private void toggleFormEnabled(boolean enabled) {
        txtTenKM.setEnabled(enabled);
        txtPhanTram.setEnabled(enabled);
        txtNgayBD.setEnabled(enabled);
        txtNgayKT.setEnabled(enabled);
        txtMoTa.setEnabled(enabled);
        btnCapNhat.setEnabled(enabled);
        btnXoa.setEnabled(enabled);
    }

    private void xoaForm() {
        txtMaKM.setText("");
        txtTenKM.setText("");
        txtPhanTram.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        txtMoTa.setText("");
    }

    private void styleField(JTextField tf, boolean readOnly) {
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setEditable(!readOnly);
        tf.setBackground(readOnly ? new Color(243, 244, 246) : Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                new EmptyBorder(7, 10, 7, 10)
        ));
    }

    private void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(LABEL_CLR);
    }

    private void hienThiThongBao(String msg, boolean isError) {
        lblThongBao.setText(msg);
        lblThongBao.setForeground(isError ? ERROR_CLR : SUCCESS_CLR);
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}