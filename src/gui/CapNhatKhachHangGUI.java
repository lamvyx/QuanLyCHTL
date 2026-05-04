package gui;

import dao.KhachHangDAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class CapNhatKhachHangGUI extends JDialog {

    private static final Color C_BG     = new Color(245, 247, 250);
    private static final Color C_WHITE  = Color.WHITE;
    private static final Color C_BLUE   = new Color(37, 99, 235);
    private static final Color C_GREEN  = new Color(22, 163, 74);
    private static final Color C_BORDER = new Color(203, 213, 225);
    private static final Color C_DARK   = new Color(15, 23, 42);
    private static final Color C_TEXT   = new Color(30, 41, 59);
    private static final Color C_GRAY   = new Color(241, 245, 249);
    private static final Font  F_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  F_BOLD   = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font  F_TITLE  = new Font("Segoe UI", Font.BOLD,  15);

    private JTextField txtMaKH  = new JTextField();
    private JTextField txtTenKH = new JTextField();
    private JTextField txtSdt   = new JTextField();
    private JTextField txtDiem  = new JTextField();
    private JComboBox<String> cboLoai =
        new JComboBox<>(new String[]{"Thường", "Thân thiết", "VIP"});

    private JPanel  panelInfo;   // ẩn/hiện
    private JButton btnLuu;

    private KhachHangDAO dao = new KhachHangDAO();

    public CapNhatKhachHangGUI(JFrame parent) {
        super(parent, "Cập nhật khách hàng", true);
        setSize(480, 200);          // nhỏ ban đầu, chỉ có ô nhập mã
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(C_BG);
        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildContent(), BorderLayout.CENTER);
        setContentPane(root);
        setVisible(true);
    }

    /* ── Tiêu đề ── */
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_DARK);
        p.setBorder(new EmptyBorder(13, 20, 13, 20));
        JLabel lbl = new JLabel("✏️  Cập nhật khách hàng");
        lbl.setFont(F_TITLE);
        lbl.setForeground(Color.WHITE);
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    /* ── Nội dung: ô tìm + panel thông tin ── */
    private JPanel buildContent() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(16, 16, 16, 16));

        p.add(buildSearchRow());
        p.add(Box.createVerticalStrut(12));
        p.add(buildInfoPanel());   // ẩn lúc đầu

        return p;
    }

    /* ── Hàng nhập mã ── */
    private JPanel buildSearchRow() {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(C_WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            new EmptyBorder(12, 14, 12, 14)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 56));

        JLabel lbl = new JLabel("Mã khách hàng:");
        lbl.setFont(F_NORMAL);
        lbl.setForeground(C_TEXT);
        lbl.setPreferredSize(new Dimension(130, 32));

        styleField(txtMaKH);

        JButton btnTim = mkButton("Tìm", C_BLUE);
        btnTim.setPreferredSize(new Dimension(80, 32));

        row.add(lbl,      BorderLayout.WEST);
        row.add(txtMaKH,  BorderLayout.CENTER);
        row.add(btnTim,   BorderLayout.EAST);

        btnTim.addActionListener(e -> handleTim());
        txtMaKH.addActionListener(e -> handleTim());

        return row;
    }

    /* ── Panel thông tin (ẩn lúc đầu) ── */
    private JPanel buildInfoPanel() {
        panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBackground(C_WHITE);
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            new EmptyBorder(14, 14, 14, 14)
        ));
        panelInfo.setVisible(false);

        GridBagConstraints g = new GridBagConstraints();
        g.anchor = GridBagConstraints.WEST;
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 4, 5, 8);

        addRow(panelInfo, g, 0, "Tên khách hàng", txtTenKH);
        addRow(panelInfo, g, 1, "Số điện thoại",  txtSdt);
        addRow(panelInfo, g, 2, "Điểm tích lũy",  txtDiem);

        // Loại KH
        g.gridx = 0; g.gridy = 3; g.weightx = 0;
        JLabel lblLoai = mkLabel("Loại khách hàng");
        panelInfo.add(lblLoai, g);
        g.gridx = 1; g.weightx = 1;
        cboLoai.setFont(F_NORMAL);
        cboLoai.setBackground(C_WHITE);
        cboLoai.setPreferredSize(new Dimension(0, 32));
        panelInfo.add(cboLoai, g);

        // Nút Lưu
        g.gridx = 1; g.gridy = 4; g.weightx = 1;
        g.anchor = GridBagConstraints.EAST;
        g.insets = new Insets(12, 4, 0, 8);
        btnLuu = mkButton("💾  Lưu thay đổi", C_GREEN);
        panelInfo.add(btnLuu, g);

        btnLuu.addActionListener(e -> handleLuu());

        return panelInfo;
    }

    /* ── Xử lý TÌM ── */
    private void handleTim() {
        String ma = txtMaKH.getText().trim();
        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KH!");
            return;
        }

        KhachHang kh = dao.timTheoMa(ma.toUpperCase());
        if (kh != null) {
            txtTenKH.setText(kh.getTenKH());
            txtSdt.setText(kh.getSdt());
            txtDiem.setText(String.valueOf(kh.getDiemTichLuy()));
            cboLoai.setSelectedItem(kh.getLoaiKH() != null ? kh.getLoaiKH() : "Thường");

            // Hiện panel thông tin + mở rộng cửa sổ
            panelInfo.setVisible(true);
            setSize(480, 430);
            setLocationRelativeTo(getOwner());
            txtTenKH.requestFocus();
        } else {
            panelInfo.setVisible(false);
            setSize(480, 200);
            JOptionPane.showMessageDialog(this,
                "Không tìm thấy mã: " + ma,
                "Không có kết quả", JOptionPane.WARNING_MESSAGE);
        }
    }

    /* ── Xử lý LƯU ── */
    private void handleLuu() {
        String ten = txtTenKH.getText().trim();
        String sdt = txtSdt.getText().trim();
        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên và SĐT không được để trống!");
            return;
        }
        int diem = 0;
        try { diem = Integer.parseInt(txtDiem.getText().trim()); }
        catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số!");
            return;
        }

        KhachHang kh = new KhachHang(
            txtMaKH.getText().trim().toUpperCase(),
            ten, sdt, diem, (String) cboLoai.getSelectedItem()
        );

        if (dao.capNhat(kh)) {
            JOptionPane.showMessageDialog(this,
                "Cập nhật thành công!", "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            // Reset về trạng thái ban đầu
            txtMaKH.setText("");
            panelInfo.setVisible(false);
            setSize(480, 200);
            setLocationRelativeTo(getOwner());
            txtMaKH.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this,
                "Cập nhật thất bại! Kiểm tra kết nối database.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ── Helpers ── */
    private void addRow(JPanel p, GridBagConstraints g,
                        int row, String labelText, JTextField tf) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;
        g.anchor = GridBagConstraints.WEST;
        p.add(mkLabel(labelText), g);
        g.gridx = 1; g.weightx = 1;
        styleField(tf);
        p.add(tf, g);
    }

    private JLabel mkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_NORMAL);
        l.setForeground(C_TEXT);
        l.setPreferredSize(new Dimension(140, 32));
        return l;
    }

    private void styleField(JTextField tf) {
        tf.setFont(F_NORMAL);
        tf.setPreferredSize(new Dimension(0, 32));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(C_BORDER),
            new EmptyBorder(2, 8, 2, 8)
        ));
    }

    private JButton mkButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(F_BOLD);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(8, 20, 8, 20));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
