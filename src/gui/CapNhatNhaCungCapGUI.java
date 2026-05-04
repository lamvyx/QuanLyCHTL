package gui;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.regex.Pattern;

public class CapNhatNhaCungCapGUI extends JDialog {

    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BG   = new Color(248, 250, 252);
    private static final Font  FNT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font  FNT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    // ── Tìm kiếm ────────────────────────────────
    private JTextField txtTimMaNCC;
    private JButton    btnTim;

    // ── Form cập nhật ────────────────────────────
    private JTextField txtMaNCC, txtTenNCC, txtSDT, txtDiaChi, txtEmail;
    private JButton    btnLuu, btnXoa, btnLamMoi, btnDong;
    private JPanel     formPanel;

    private final NhaCungCapDAO dao;
    private NhaCungCap          current;  // NCC đang được chọn

    public CapNhatNhaCungCapGUI(JFrame parent) {
        super(parent, "Cập Nhật Nhà Cung Cấp", true);
        this.dao = new NhaCungCapDAO();
        buildUI();
        setSize(500, 500);
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
        JLabel lbl = new JLabel("✏  Cập Nhật Nhà Cung Cấp");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        root.add(header, BorderLayout.NORTH);

        // ── Search bar ──────────────────────────
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        searchBar.setBackground(new Color(241, 245, 249));
        searchBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel lblTim = new JLabel("Nhập mã NCC:");
        lblTim.setFont(FNT_BOLD);
        lblTim.setForeground(NAVY);

        txtTimMaNCC = new JTextField(14);
        txtTimMaNCC.setFont(FNT_PLAIN);
        txtTimMaNCC.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(4, 7, 4, 7)));

        btnTim = btn("🔍  Tìm", BLUE, Color.WHITE);
        btnTim.addActionListener(e -> timNCC());

        // Nhấn Enter trong ô tìm cũng kích hoạt tìm
        txtTimMaNCC.addActionListener(e -> timNCC());

        searchBar.add(lblTim);
        searchBar.add(txtTimMaNCC);
        searchBar.add(btnTim);
        root.add(searchBar, BorderLayout.AFTER_LAST_LINE); // sẽ gắn vào center sau

        // ── Form ────────────────────────────────
        formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG);
        formPanel.setBorder(new EmptyBorder(16, 28, 8, 28));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 4, 6, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;

        txtMaNCC  = field(); txtMaNCC.setEditable(false);
        txtMaNCC.setBackground(new Color(229, 231, 235));
        addRow(formPanel, g, 0, "Mã NCC",             txtMaNCC);

        txtTenNCC = field();
        addRow(formPanel, g, 1, "Tên nhà cung cấp *", txtTenNCC);

        txtSDT    = field();
        addRow(formPanel, g, 2, "Số điện thoại",      txtSDT);

        txtDiaChi = field();
        addRow(formPanel, g, 3, "Địa chỉ",            txtDiaChi);

        txtEmail  = field();
        addRow(formPanel, g, 4, "Email",               txtEmail);

        setFormEnabled(false); // disable đến khi tìm được NCC

        // ── Gộp search + form ────────────────────
        JPanel mid = new JPanel(new BorderLayout());
        mid.setBackground(BG);
        mid.add(searchBar, BorderLayout.NORTH);
        mid.add(formPanel, BorderLayout.CENTER);
        root.add(mid, BorderLayout.CENTER);

        // ── Buttons ─────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 12));
        btnBar.setBackground(BG);
        btnBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        btnLuu    = btn("💾  Lưu",       new Color(22, 163, 74),  Color.WHITE);
        btnXoa    = btn("🗑  Xóa",       new Color(220, 38,  38),  Color.WHITE);
        btnLamMoi = btn("↺  Làm mới",   new Color(234, 88,  12),  Color.WHITE);
        btnDong   = btn("✖  Đóng",      new Color(107, 114, 128),  Color.WHITE);

        btnLuu   .addActionListener(e -> luu());
        btnXoa   .addActionListener(e -> xoa());
        btnLamMoi.addActionListener(e -> lamMoi());
        btnDong  .addActionListener(e -> dispose());

        btnLuu .setEnabled(false);
        btnXoa .setEnabled(false);

        btnBar.add(btnLuu);
        btnBar.add(btnXoa);
        btnBar.add(btnLamMoi);
        btnBar.add(btnDong);
        root.add(btnBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ════════════════════════════════════════════
    private void timNCC() {
        String ma = txtTimMaNCC.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã NCC cần tìm!",
                    "Lưu ý", JOptionPane.WARNING_MESSAGE);
            return;
        }
        current = dao.getByMa(ma);
        if (current == null) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy nhà cung cấp có mã: " + ma,
                    "Không tìm thấy", JOptionPane.WARNING_MESSAGE);
            setFormEnabled(false);
            clearForm();
            btnLuu.setEnabled(false);
            btnXoa.setEnabled(false);
        } else {
            fillForm(current);
            setFormEnabled(true);
            btnLuu.setEnabled(true);
            btnXoa.setEnabled(true);
        }
    }

    private void luu() {
        if (current == null) return;
        String ten  = txtTenNCC.getText().trim();
        String sdt  = txtSDT   .getText().trim();
        String dia  = txtDiaChi.getText().trim();
        String mail = txtEmail .getText().trim();

        if (ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên nhà cung cấp không được để trống!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtTenNCC.requestFocus(); return;
        }
        if (!sdt.isEmpty() && !Pattern.matches("^(0|\\+84)\\d{9,10}$", sdt)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtSDT.requestFocus(); return;
        }
        if (!mail.isEmpty() && !mail.matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus(); return;
        }

        current.setTenNCC(ten);
        current.setSdt(sdt);
        current.setDiaChi(dia);
        current.setEmail(mail);

        if (dao.capNhat(current)) {
            JOptionPane.showMessageDialog(this,
                    "✔  Cập nhật thành công!",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoa() {
        if (current == null) return;
        int ok = JOptionPane.showConfirmDialog(this,
                "Xóa nhà cung cấp " + current.getMaNCC() + " - " + current.getTenNCC() + "?\n"
              + "⚠ Hành động này không thể hoàn tác!",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            if (dao.xoa(current.getMaNCC())) {
                JOptionPane.showMessageDialog(this, "✔  Đã xóa thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                lamMoi();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Xóa thất bại!\nNhà cung cấp này có thể đang liên kết với sản phẩm hoặc phiếu nhập.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lamMoi() {
        txtTimMaNCC.setText("");
        clearForm();
        setFormEnabled(false);
        btnLuu.setEnabled(false);
        btnXoa.setEnabled(false);
        current = null;
        txtTimMaNCC.requestFocus();
    }

    // ── helpers ─────────────────────────────────
    private void fillForm(NhaCungCap ncc) {
        txtMaNCC .setText(ncc.getMaNCC());
        txtTenNCC.setText(ncc.getTenNCC());
        txtSDT   .setText(ncc.getSdt()    != null ? ncc.getSdt()    : "");
        txtDiaChi.setText(ncc.getDiaChi() != null ? ncc.getDiaChi() : "");
        txtEmail .setText(ncc.getEmail()  != null ? ncc.getEmail()  : "");
    }

    private void clearForm() {
        txtMaNCC .setText("");
        txtTenNCC.setText("");
        txtSDT   .setText("");
        txtDiaChi.setText("");
        txtEmail .setText("");
    }

    private void setFormEnabled(boolean enabled) {
        txtTenNCC.setEnabled(enabled);
        txtSDT   .setEnabled(enabled);
        txtDiaChi.setEnabled(enabled);
        txtEmail .setEnabled(enabled);
    }

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
        b.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}