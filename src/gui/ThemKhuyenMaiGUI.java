package gui;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ThemKhuyenMaiGUI extends JDialog {

    // ── Màu sắc đồng bộ HomeGUI ──────────────────────────
    private static final Color NAVY  = new Color(15, 23, 42);
    private static final Color BLUE  = new Color(37, 99, 235);
    private static final Color BG    = new Color(248, 250, 252);
    private static final Color CARD  = Color.WHITE;
    private static final Color BORDER_CLR = new Color(226, 232, 240);
    private static final Color LABEL_CLR  = new Color(55, 65, 81);
    private static final Color ERROR_CLR  = new Color(220, 38, 38);
    private static final Color SUCCESS_CLR = new Color(5, 150, 105);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Các trường nhập liệu ─────────────────────────────
    private JTextField txtMaKM, txtTenKM, txtPhanTram, txtNgayBD, txtNgayKT;
    private JTextArea  txtMoTa;
    private JLabel     lblThongBao;

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public ThemKhuyenMaiGUI(JFrame parent) {
        super(parent, "Thêm khuyến mãi", true);
        setSize(500, 540);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ── Header ──────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel("✦  Thêm khuyến mãi mới");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblTitle, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // ── Form ────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                new EmptyBorder(20, 24, 16, 24)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 0, 7, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        int row = 0;

        txtMaKM    = addFormRow(form, gbc, row++, "Mã khuyến mãi *", "", false);
        txtTenKM   = addFormRow(form, gbc, row++, "Tên khuyến mãi *", "", false);
        txtPhanTram = addFormRow(form, gbc, row++, "Phần trăm giảm (0–100) *", "", false);
        txtNgayBD  = addFormRow(form, gbc, row++, "Ngày bắt đầu * (dd/MM/yyyy)", "", false);
        txtNgayKT  = addFormRow(form, gbc, row++, "Ngày kết thúc * (dd/MM/yyyy)", "", false);

        // Mô tả (textarea riêng)
        gbc.gridx = 0; gbc.gridy = row++;
        JLabel lblMoTa = new JLabel("Mô tả");
        styleLabel(lblMoTa);
        form.add(lblMoTa, gbc);

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

        JScrollPane scrollForm = new JScrollPane(form);
        scrollForm.setBorder(null);
        scrollForm.getViewport().setBackground(CARD);
        root.add(scrollForm, BorderLayout.CENTER);

        // ── Nút hành động ───────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        footer.setBackground(BG);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR));

        JButton btnLuu   = taoNut("Lưu", BLUE, Color.WHITE);
        JButton btnXoa   = taoNut("Xóa trắng", new Color(107, 114, 128), Color.WHITE);
        JButton btnDong  = taoNut("Đóng", new Color(229, 231, 235), NAVY);

        btnLuu.addActionListener(e -> xuLyLuu());
        btnXoa.addActionListener(e -> xoaTrang());
        btnDong.addActionListener(e -> dispose());

        footer.add(btnXoa);
        footer.add(btnDong);
        footer.add(btnLuu);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Helpers ─────────────────────────────────────────

    private JTextField addFormRow(JPanel form, GridBagConstraints gbc,
                                   int row, String labelText,
                                   String placeholder, boolean readOnly) {
        gbc.gridx = 0; gbc.gridy = row * 2;
        JLabel lbl = new JLabel(labelText);
        styleLabel(lbl);
        form.add(lbl, gbc);

        gbc.gridy = row * 2 + 1;
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setEditable(!readOnly);
        tf.setBackground(readOnly ? new Color(243, 244, 246) : Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                new EmptyBorder(7, 10, 7, 10)
        ));
        if (!placeholder.isEmpty()) tf.setText(placeholder);
        form.add(tf, gbc);
        return tf;
    }

    private void styleLabel(JLabel lbl) {
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(LABEL_CLR);
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

    private void hienThiThongBao(String msg, boolean isError) {
        lblThongBao.setText(msg);
        lblThongBao.setForeground(isError ? ERROR_CLR : SUCCESS_CLR);
    }

    // ── Xử lý nghiệp vụ ─────────────────────────────────

    private void xuLyLuu() {
        String maKM   = txtMaKM.getText().trim();
        String tenKM  = txtTenKM.getText().trim();
        String phanTramStr = txtPhanTram.getText().trim();
        String ngayBDStr   = txtNgayBD.getText().trim();
        String ngayKTStr   = txtNgayKT.getText().trim();
        String moTa   = txtMoTa.getText().trim();

        // Validate bắt buộc
        if (maKM.isEmpty() || tenKM.isEmpty() || phanTramStr.isEmpty()
                || ngayBDStr.isEmpty() || ngayKTStr.isEmpty()) {
            hienThiThongBao("⚠ Vui lòng điền đầy đủ các trường bắt buộc (*)", true);
            return;
        }

        // Validate mã dài tối đa 10
        if (maKM.length() > 10) {
            hienThiThongBao("⚠ Mã khuyến mãi tối đa 10 ký tự", true);
            return;
        }

        // Validate phần trăm
        double phanTram;
        try {
            phanTram = Double.parseDouble(phanTramStr);
            if (phanTram < 0 || phanTram > 100) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            hienThiThongBao("⚠ Phần trăm giảm phải là số từ 0 đến 100", true);
            return;
        }

        // Validate ngày
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

        // Kiểm tra mã trùng
        if (khuyenMaiDAO.maDaTonTai(maKM)) {
            hienThiThongBao("⚠ Mã khuyến mãi \"" + maKM + "\" đã tồn tại", true);
            return;
        }

        // Lưu
        KhuyenMai km = new KhuyenMai(maKM, tenKM, moTa, phanTram, ngayBD, ngayKT);
        if (khuyenMaiDAO.them(km)) {
            hienThiThongBao("✔ Thêm khuyến mãi thành công!", false);
            xoaTrang();
        } else {
            hienThiThongBao("✖ Lỗi khi lưu vào cơ sở dữ liệu", true);
        }
    }

    private void xoaTrang() {
        txtMaKM.setText("");
        txtTenKM.setText("");
        txtPhanTram.setText("");
        txtNgayBD.setText("");
        txtNgayKT.setText("");
        txtMoTa.setText("");
        lblThongBao.setText(" ");
        txtMaKM.requestFocus();
    }
}