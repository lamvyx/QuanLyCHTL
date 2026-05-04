package gui;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class XemPhieuNhapNhaCungCapGUI extends JDialog {

    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color TEAL = new Color(20, 184, 166);
    private static final Color BG   = new Color(248, 250, 252);
    private static final Font  FNT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font  FNT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    // ── Filter ──────────────────────────────────
    private JComboBox<NhaCungCap> cboNCC;
    private JButton               btnXem;

    // ── Bảng phiếu nhập ─────────────────────────
    private JTable         tblPhieu;
    private DefaultTableModel mdlPhieu;
    private JLabel         lblCount;

    // ── Bảng chi tiết ───────────────────────────
    private JTable         tblChiTiet;
    private DefaultTableModel mdlChiTiet;
    private JLabel         lblTong;

    private final NhaCungCapDAO dao;

    public XemPhieuNhapNhaCungCapGUI(JFrame parent) {
        super(parent, "Xem Phiếu Nhập Theo Nhà Cung Cấp", false);
        this.dao = new NhaCungCapDAO();
        buildUI();
        loadCombo();
        setSize(860, 580);
        setLocationRelativeTo(parent);
    }

    // ════════════════════════════════════════════
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);

        // ── Header ──────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setBackground(NAVY);
        JLabel lbl = new JLabel("📋  Xem Phiếu Nhập Theo Nhà Cung Cấp");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        root.add(header, BorderLayout.NORTH);

        // ── Filter ──────────────────────────────
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        filter.setBackground(new Color(241, 245, 249));
        filter.setBorder(new MatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JLabel lblNCC = new JLabel("Nhà cung cấp:");
        lblNCC.setFont(FNT_BOLD);
        lblNCC.setForeground(NAVY);

        cboNCC = new JComboBox<>();
        cboNCC.setFont(FNT_PLAIN);
        cboNCC.setPreferredSize(new Dimension(280, 30));

        btnXem = btn("📋  Xem phiếu nhập", BLUE);
        btnXem.addActionListener(e -> xemPhieuNhap());

        filter.add(lblNCC);
        filter.add(cboNCC);
        filter.add(btnXem);

        // ── Split pane: phiếu (trên) + chi tiết (dưới) ──
        // Bảng phiếu nhập
        String[] colsPhieu = {"Mã phiếu", "Ngày nhập", "Nhân viên", "Ghi chú"};
        mdlPhieu = new DefaultTableModel(colsPhieu, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblPhieu = makeTable(mdlPhieu);
        tblPhieu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) xemChiTiet();
        });
        int[] wPhieu = {90, 100, 150, 300};
        for (int i = 0; i < wPhieu.length; i++)
            tblPhieu.getColumnModel().getColumn(i).setPreferredWidth(wPhieu[i]);

        lblCount = new JLabel("  Tổng: 0 phiếu nhập");
        lblCount.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCount.setForeground(new Color(107, 114, 128));

        JPanel topPanel = new JPanel(new BorderLayout(0, 4));
        topPanel.setBackground(BG);
        topPanel.setBorder(new EmptyBorder(8, 14, 4, 14));
        JLabel lblPhieuTitle = sectionLabel("Danh sách phiếu nhập");
        topPanel.add(lblPhieuTitle, BorderLayout.NORTH);
        JScrollPane scrollPhieu = new JScrollPane(tblPhieu);
        styleScroll(scrollPhieu);
        topPanel.add(scrollPhieu, BorderLayout.CENTER);
        topPanel.add(lblCount, BorderLayout.SOUTH);

        // Bảng chi tiết phiếu
        String[] colsCT = {"Mã SP", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"};
        mdlChiTiet = new DefaultTableModel(colsCT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblChiTiet = makeTable(mdlChiTiet);
        int[] wCT = {80, 230, 80, 110, 120};
        for (int i = 0; i < wCT.length; i++)
            tblChiTiet.getColumnModel().getColumn(i).setPreferredWidth(wCT[i]);

        // Căn phải cột số
        DefaultTableCellRenderer rightR = new DefaultTableCellRenderer();
        rightR.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i : new int[]{2, 3, 4}) tblChiTiet.getColumnModel().getColumn(i).setCellRenderer(rightR);

        lblTong = new JLabel("  Tổng tiền phiếu: 0 đ");
        lblTong.setFont(FNT_BOLD);
        lblTong.setForeground(TEAL);

        JPanel botPanel = new JPanel(new BorderLayout(0, 4));
        botPanel.setBackground(BG);
        botPanel.setBorder(new EmptyBorder(4, 14, 8, 14));
        JLabel lblCTTitle = sectionLabel("Chi tiết phiếu nhập (chọn phiếu bên trên)");
        botPanel.add(lblCTTitle, BorderLayout.NORTH);
        JScrollPane scrollCT = new JScrollPane(tblChiTiet);
        styleScroll(scrollCT);
        botPanel.add(scrollCT, BorderLayout.CENTER);
        botPanel.add(lblTong, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, botPanel);
        split.setDividerLocation(240);
        split.setDividerSize(6);
        split.setBackground(BG);

        // ── Center ──────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.add(filter, BorderLayout.NORTH);
        center.add(split,  BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // ── Bottom ──────────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
        btnBar.setBackground(BG);
        btnBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        JButton btnDong = btn("✖  Đóng", new Color(185, 28, 28));
        btnDong.addActionListener(e -> dispose());
        btnBar.add(btnDong);
        root.add(btnBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ════════════════════════════════════════════
    private void loadCombo() {
        cboNCC.removeAllItems();
        for (NhaCungCap ncc : dao.getAll()) cboNCC.addItem(ncc);
    }

    private void xemPhieuNhap() {
        NhaCungCap ncc = (NhaCungCap) cboNCC.getSelectedItem();
        if (ncc == null) return;

        List<Object[]> rows = dao.getPhieuNhapByNCC(ncc.getMaNCC());
        mdlPhieu.setRowCount(0);
        mdlChiTiet.setRowCount(0);
        lblTong.setText("  Tổng tiền phiếu: 0 đ");

        for (Object[] r : rows) mdlPhieu.addRow(r);
        lblCount.setText("  Tổng: " + rows.size() + " phiếu nhập");
    }

    private void xemChiTiet() {
        int row = tblPhieu.getSelectedRow();
        if (row < 0) return;
        String maPhieu = (String) mdlPhieu.getValueAt(row, 0);

        List<Object[]> rows = dao.getChiTietPhieuNhap(maPhieu);
        mdlChiTiet.setRowCount(0);

        NumberFormat fmt = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        double tong = 0;
        for (Object[] r : rows) {
            double thanhTien = (double) r[4];
            tong += thanhTien;
            mdlChiTiet.addRow(new Object[]{
                r[0], r[1], r[2],
                fmt.format(r[3]) + " đ",
                fmt.format(thanhTien) + " đ"
            });
        }
        lblTong.setText("  Tổng tiền phiếu: " + fmt.format(tong) + " đ");
    }

    // ── helpers ─────────────────────────────────
    private JTable makeTable(DefaultTableModel m) {
        JTable t = new JTable(m);
        t.setFont(FNT_PLAIN);
        t.setRowHeight(26);
        t.setGridColor(new Color(229, 231, 235));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(NAVY);
        t.getTableHeader().setFont(FNT_BOLD);
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(NAVY);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return t;
    }

    private void styleScroll(JScrollPane s) {
        s.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        s.getViewport().setBackground(Color.WHITE);
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(75, 85, 99));
        l.setBorder(new EmptyBorder(0, 0, 4, 0));
        return l;
    }

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