package gui;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

/**
 * Màn hình Đánh Giá Giao Nhận.
 * Cho phép chọn NCC → chọn phiếu nhập → chấm điểm 1-5 sao + nhận xét.
 *
 * ⚠ Lưu ý: chức năng lưu đánh giá cần bảng DanhGiaGiaoNhan trong DB.
 *   Script SQL để tạo bảng được comment phía dưới class.
 */
public class DanhGiaGiaoNhanGUI extends JDialog {

    private static final Color NAVY   = new Color(15, 23, 42);
    private static final Color BLUE   = new Color(37, 99, 235);
    private static final Color AMBER  = new Color(245, 158, 11);
    private static final Color BG     = new Color(248, 250, 252);
    private static final Font  FNT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font  FNT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    // ── Bước 1: chọn NCC ────────────────────────
    private JComboBox<NhaCungCap> cboNCC;
    private JButton btnXemPhieu;

    // ── Bước 2: chọn phiếu nhập ─────────────────
    private JTable tblPhieu;
    private DefaultTableModel mdlPhieu;

    // ── Bước 3: đánh giá ────────────────────────
    private JLabel       lblMaPhieuChon;
    private JToggleButton[] starBtns;   // 5 nút sao
    private int          diemChon = 0;
    private JTextArea    txtNhanXet;
    private JButton      btnLuu, btnDong;

    private final NhaCungCapDAO dao;

    public DanhGiaGiaoNhanGUI(JFrame parent) {
        super(parent, "Đánh Giá Giao Nhận", true);
        this.dao = new NhaCungCapDAO();
        buildUI();
        loadCombo();
        setSize(760, 560);
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    // ════════════════════════════════════════════
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);

        // ── Header ──────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setBackground(NAVY);
        JLabel lbl = new JLabel("⭐  Đánh Giá Giao Nhận Nhà Cung Cấp");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        root.add(header, BorderLayout.NORTH);

        // ── Body: left (chọn phiếu) | right (đánh giá) ──
        JPanel body = new JPanel(new GridLayout(1, 2, 12, 0));
        body.setBackground(BG);
        body.setBorder(new EmptyBorder(14, 14, 10, 14));

        body.add(buildLeftPanel());
        body.add(buildRightPanel());
        root.add(body, BorderLayout.CENTER);

        // ── Bottom ──────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        btnBar.setBackground(BG);
        btnBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));

        btnLuu  = btn("💾  Lưu đánh giá", new Color(22, 163, 74));
        btnDong = btn("✖  Đóng",          new Color(185, 28,  28));

        btnLuu .addActionListener(e -> luuDanhGia());
        btnDong.addActionListener(e -> dispose());

        btnBar.add(btnLuu);
        btnBar.add(btnDong);
        root.add(btnBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Left panel: chọn NCC + phiếu ───────────
    private JPanel buildLeftPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(BG);
        p.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(12, 12, 12, 12)));

        // NCC selector
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setBackground(BG);
        JLabel lblNCC = new JLabel("Nhà cung cấp:");
        lblNCC.setFont(FNT_BOLD);
        lblNCC.setForeground(NAVY);
        cboNCC = new JComboBox<>();
        cboNCC.setFont(FNT_PLAIN);
        cboNCC.setPreferredSize(new Dimension(180, 28));
        btnXemPhieu = btn("Xem phiếu", BLUE);
        btnXemPhieu.addActionListener(e -> loadPhieu());
        top.add(lblNCC);
        top.add(cboNCC);
        top.add(btnXemPhieu);
        p.add(top, BorderLayout.NORTH);

        // Table phiếu nhập
        JLabel lblTitle = new JLabel("Danh sách phiếu nhập");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(new Color(75, 85, 99));

        String[] cols = {"Mã phiếu", "Ngày nhập", "Nhân viên"};
        mdlPhieu = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPhieu = new JTable(mdlPhieu);
        tblPhieu.setFont(FNT_PLAIN);
        tblPhieu.setRowHeight(26);
        tblPhieu.setGridColor(new Color(229, 231, 235));
        tblPhieu.setSelectionBackground(new Color(219, 234, 254));
        tblPhieu.setSelectionForeground(NAVY);
        tblPhieu.getTableHeader().setFont(FNT_BOLD);
        tblPhieu.getTableHeader().setBackground(new Color(241, 245, 249));
        tblPhieu.getTableHeader().setForeground(NAVY);
        tblPhieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPhieu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) syncMaPhieu();
        });

        int[] widths = {90, 95, 140};
        for (int i = 0; i < widths.length; i++)
            tblPhieu.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(tblPhieu);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));

        JPanel tableWrap = new JPanel(new BorderLayout(0, 4));
        tableWrap.setBackground(BG);
        tableWrap.add(lblTitle, BorderLayout.NORTH);
        tableWrap.add(scroll,   BorderLayout.CENTER);

        p.add(tableWrap, BorderLayout.CENTER);
        return p;
    }

    // ── Right panel: đánh giá ───────────────────
    private JPanel buildRightPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        p.setBorder(new CompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            new EmptyBorder(16, 16, 16, 16)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 4, 8, 4);
        g.fill   = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;

        // Tiêu đề
        JLabel lblTitle = new JLabel("📝  Đánh giá");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitle.setForeground(NAVY);
        g.gridx = 0; g.gridy = 0; g.gridwidth = 2; g.weightx = 1.0;
        p.add(lblTitle, g);
        g.gridwidth = 1;

        // Mã phiếu đang chọn
        JLabel lblMaLabel = new JLabel("Phiếu nhập:");
        lblMaLabel.setFont(FNT_BOLD); lblMaLabel.setForeground(NAVY);
        g.gridx = 0; g.gridy = 1; g.weightx = 0;
        p.add(lblMaLabel, g);

        lblMaPhieuChon = new JLabel("(chưa chọn)");
        lblMaPhieuChon.setFont(FNT_PLAIN);
        lblMaPhieuChon.setForeground(new Color(107, 114, 128));
        g.gridx = 1; g.weightx = 1.0;
        p.add(lblMaPhieuChon, g);

        // Sao đánh giá
        JLabel lblSao = new JLabel("Điểm đánh giá:");
        lblSao.setFont(FNT_BOLD); lblSao.setForeground(NAVY);
        g.gridx = 0; g.gridy = 2; g.weightx = 0;
        p.add(lblSao, g);

        JPanel starPanel = buildStarPanel();
        g.gridx = 1; g.weightx = 1.0;
        p.add(starPanel, g);

        // Nhận xét
        JLabel lblNX = new JLabel("Nhận xét:");
        lblNX.setFont(FNT_BOLD); lblNX.setForeground(NAVY);
        lblNX.setVerticalAlignment(SwingConstants.TOP);
        g.gridx = 0; g.gridy = 3; g.weightx = 0; g.anchor = GridBagConstraints.NORTHWEST;
        p.add(lblNX, g);

        txtNhanXet = new JTextArea(6, 18);
        txtNhanXet.setFont(FNT_PLAIN);
        txtNhanXet.setLineWrap(true);
        txtNhanXet.setWrapStyleWord(true);
        txtNhanXet.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        JScrollPane scrollNX = new JScrollPane(txtNhanXet);
        scrollNX.setBorder(null);
        g.gridx = 1; g.weightx = 1.0; g.weighty = 1.0;
        g.fill = GridBagConstraints.BOTH;
        p.add(scrollNX, g);

        return p;
    }

    private JPanel buildStarPanel() {
        JPanel sp = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        sp.setBackground(BG);
        starBtns = new JToggleButton[5];
        for (int i = 0; i < 5; i++) {
            final int idx = i + 1;
            JToggleButton star = new JToggleButton("★");
            star.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            star.setForeground(new Color(209, 213, 219));  // mặc định xám
            star.setBackground(BG);
            star.setFocusPainted(false);
            star.setBorderPainted(false);
            star.setContentAreaFilled(false);
            star.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            star.addActionListener(e -> selectStars(idx));
            starBtns[i] = star;
            sp.add(star);
        }
        return sp;
    }

    private void selectStars(int diem) {
        diemChon = diem;
        for (int i = 0; i < 5; i++) {
            starBtns[i].setForeground(i < diem ? AMBER : new Color(209, 213, 219));
        }
    }

    // ════════════════════════════════════════════
    private void loadCombo() {
        cboNCC.removeAllItems();
        for (NhaCungCap ncc : dao.getAll()) cboNCC.addItem(ncc);
    }

    private void loadPhieu() {
        NhaCungCap ncc = (NhaCungCap) cboNCC.getSelectedItem();
        if (ncc == null) return;
        mdlPhieu.setRowCount(0);
        for (Object[] r : dao.getPhieuNhapByNCC(ncc.getMaNCC())) {
            mdlPhieu.addRow(new Object[]{r[0], r[1], r[2]});
        }
        lblMaPhieuChon.setText("(chưa chọn)");
        lblMaPhieuChon.setForeground(new Color(107, 114, 128));
        diemChon = 0;
        selectStars(0);
        txtNhanXet.setText("");
    }

    private void syncMaPhieu() {
        int row = tblPhieu.getSelectedRow();
        if (row < 0) return;
        String ma = (String) mdlPhieu.getValueAt(row, 0);
        lblMaPhieuChon.setText(ma);
        lblMaPhieuChon.setForeground(NAVY);
    }

    private void luuDanhGia() {
        NhaCungCap ncc = (NhaCungCap) cboNCC.getSelectedItem();
        if (ncc == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp!",
                    "Lưu ý", JOptionPane.WARNING_MESSAGE); return;
        }
        int row = tblPhieu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập!",
                    "Lưu ý", JOptionPane.WARNING_MESSAGE); return;
        }
        if (diemChon == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn điểm đánh giá (1-5 sao)!",
                    "Lưu ý", JOptionPane.WARNING_MESSAGE); return;
        }

        String maPhieu  = (String) mdlPhieu.getValueAt(row, 0);
        String nhanXet  = txtNhanXet.getText().trim();

        /*
         * Gọi DAO lưu xuống DB.
         * Nếu bảng DanhGiaGiaoNhan chưa có, chạy script SQL ở cuối file này.
         */
        boolean ok = dao.luuDanhGia(ncc.getMaNCC(), maPhieu, diemChon, nhanXet);
        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "✔  Lưu đánh giá thành công!\n" +
                    "NCC: " + ncc.getTenNCC() + "\n" +
                    "Phiếu: " + maPhieu + " | Điểm: " + diemChon + " sao",
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            // Reset form sau khi lưu
            diemChon = 0;
            selectStars(0);
            txtNhanXet.setText("");
            lblMaPhieuChon.setText("(chưa chọn)");
            tblPhieu.clearSelection();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Lưu thất bại!\nKiểm tra xem bảng DanhGiaGiaoNhan đã tồn tại trong DB chưa.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── helper ──────────────────────────────────
    private JButton btn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(FNT_BOLD);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}

/*
 * ══════════════════════════════════════════════════════
 * SQL TẠO BẢNG DanhGiaGiaoNhan (chạy 1 lần trong SQL Server)
 * ══════════════════════════════════════════════════════
 *
 * USE QLCuaHang;
 * GO
 *
 * CREATE TABLE DanhGiaGiaoNhan (
 *     maDanhGia    INT PRIMARY KEY IDENTITY(1,1),
 *     maNCC        NVARCHAR(10) NOT NULL,
 *     maPhieu      NVARCHAR(10) NOT NULL,
 *     diemDanhGia  INT NOT NULL CHECK (diemDanhGia BETWEEN 1 AND 5),
 *     nhanXet      NVARCHAR(500),
 *     ngayDanhGia  DATE DEFAULT GETDATE(),
 *
 *     FOREIGN KEY (maNCC)   REFERENCES NhaCungCap(maNCC),
 *     FOREIGN KEY (maPhieu) REFERENCES PhieuNhap(maPhieu)
 * );
 * GO
 */