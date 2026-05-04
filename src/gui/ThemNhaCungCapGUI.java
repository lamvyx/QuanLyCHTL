package gui;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.regex.Pattern;

public class ThemNhaCungCapGUI extends JDialog {

    // ── màu theo style HomeGUI ──────────────────
    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BG   = new Color(248, 250, 252);
    private static final Font  FNT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font  FNT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField txtMaNCC, txtTenNCC, txtSDT, txtDiaChi, txtEmail;
    private JButton    btnThem, btnLamMoi, btnDong;

    private final NhaCungCapDAO dao;
    private final JFrame        parent;

    public ThemNhaCungCapGUI(JFrame parent) {
        super(parent, "Thêm Nhà Cung Cấp", true);
        this.parent = parent;
        this.dao    = new NhaCungCapDAO();
        buildUI();
        autoFillMa();
        setSize(480, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    // ════════════════════════════════════════════
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ── Header ──────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setBackground(NAVY);
        JLabel lblTitle = new JLabel("➕  Thêm Nhà Cung Cấp Mới");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(Color.WHITE);
        header.add(lblTitle);
        root.add(header, BorderLayout.NORTH);

        // ── Form ────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG);
        form.setBorder(new EmptyBorder(18, 28, 10, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;

        // Mã NCC (auto, không sửa)
        txtMaNCC  = field(); txtMaNCC.setEditable(false);
        txtMaNCC.setBackground(new Color(229, 231, 235));
        addRow(form, g, 0, "Mã NCC",             txtMaNCC);

        // Tên NCC *
        txtTenNCC = field();
        addRow(form, g, 1, "Tên nhà cung cấp *", txtTenNCC);

        // Số điện thoại
        txtSDT    = field();
        addRow(form, g, 2, "Số điện thoại",      txtSDT);

        // Địa chỉ
        txtDiaChi = field();
        addRow(form, g, 3, "Địa chỉ",            txtDiaChi);

        // Email
        txtEmail  = field();
        addRow(form, g, 4, "Email",               txtEmail);

        root.add(form, BorderLayout.CENTER);

        // ── Buttons ─────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        btnBar.setBackground(BG);
        btnBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        btnThem   = btn("✔  Thêm",     new Color(22, 163, 74),  Color.WHITE);
        btnLamMoi = btn("↺  Làm mới",  new Color(234, 88,  12),  Color.WHITE);
        btnDong   = btn("✖  Đóng",     new Color(185, 28,  28),  Color.WHITE);

        btnThem  .addActionListener(e -> them());
        btnLamMoi.addActionListener(e -> { lamMoi(); autoFillMa(); });
        btnDong  .addActionListener(e -> dispose());

        btnBar.add(btnThem);
        btnBar.add(btnLamMoi);
        btnBar.add(btnDong);
        root.add(btnBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ════════════════════════════════════════════
    private void them() {
        String ma   = txtMaNCC .getText().trim();
        String ten  = txtTenNCC.getText().trim();
        String sdt  = txtSDT   .getText().trim();
        String dia  = txtDiaChi.getText().trim();
        String mail = txtEmail .getText().trim();

        // Validate
        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtTenNCC.requestFocus();
            return;
        }
        if (!sdt.isEmpty() && !Pattern.matches("^(0|\\+84)\\d{9,10}$", sdt)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus();
            return;
        }
        if (!mail.isEmpty() && !mail.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return;
        }

        NhaCungCap ncc = new NhaCungCap(ma, ten, sdt, dia, mail);
        if (dao.them(ncc)) {
            JOptionPane.showMessageDialog(this,
                    "✔  Thêm nhà cung cấp " + ma + " thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            lamMoi();
            autoFillMa();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại! Vui lòng thử lại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void lamMoi() {
        txtTenNCC.setText("");
        txtSDT   .setText("");
        txtDiaChi.setText("");
        txtEmail .setText("");
    }

    private void autoFillMa() {
        txtMaNCC.setText(dao.sinhMaMoi());
    }

    // ── helpers ─────────────────────────────────
    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent comp) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setFont(FNT_BOLD);
        lbl.setForeground(NAVY);
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        p.add(lbl, g);
        g.gridx = 1; g.weightx = 1.0;
        p.add(comp, g);
    }

    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setFont(FNT_PLAIN);
        tf.setPreferredSize(new Dimension(260, 30));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        return tf;
    }

    private JButton btn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(FNT_BOLD);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(7, 18, 7, 18));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}