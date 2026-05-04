package gui;

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;

public class TraCuuNhaCungCapGUI extends JDialog {

    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color BG   = new Color(248, 250, 252);
    private static final Font  FNT_BOLD  = new Font("Segoe UI", Font.BOLD,  13);
    private static final Font  FNT_PLAIN = new Font("Segoe UI", Font.PLAIN, 13);

    private JTextField txtMaNCC, txtTenNCC, txtSDT;
    private JTable     table;
    private DefaultTableModel model;
    private JLabel     lblCount;

    private final NhaCungCapDAO dao;

    public TraCuuNhaCungCapGUI(JFrame parent) {
        super(parent, "Tra Cứu Nhà Cung Cấp", false); // non-modal để có thể dùng song song
        this.dao = new NhaCungCapDAO();
        buildUI();
        loadAll();
        setSize(780, 540);
        setLocationRelativeTo(parent);
    }

    // ════════════════════════════════════════════
    private void buildUI() {
        JPanel root = new JPanel(new BorderLayout(0, 8));
        root.setBackground(BG);

        // ── Header ──────────────────────────────
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        header.setBackground(NAVY);
        JLabel lbl = new JLabel("🔍  Tra Cứu Nhà Cung Cấp");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        header.add(lbl);
        root.add(header, BorderLayout.NORTH);

        // ── Filter bar ──────────────────────────
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(BG);
        filterPanel.setBorder(new EmptyBorder(4, 14, 0, 14));

        filterPanel.add(boldLabel("Mã NCC:"));
        txtMaNCC  = filterField();
        filterPanel.add(txtMaNCC);

        filterPanel.add(boldLabel("Tên NCC:"));
        txtTenNCC = filterField();
        filterPanel.add(txtTenNCC);

        filterPanel.add(boldLabel("SĐT:"));
        txtSDT    = filterField();
        filterPanel.add(txtSDT);

        JButton btnTimKiem = actionBtn("🔍  Tìm kiếm", BLUE);
        JButton btnLamMoi  = actionBtn("↺  Làm mới",   new Color(107, 114, 128));
        btnTimKiem.addActionListener(e -> search());
        btnLamMoi .addActionListener(e -> { clearFilter(); loadAll(); });

        filterPanel.add(btnTimKiem);
        filterPanel.add(btnLamMoi);
        root.add(filterPanel, BorderLayout.BEFORE_FIRST_LINE); // nằm sau header

        // ── Gộp filter + table vào center ───────
        JPanel center = new JPanel(new BorderLayout(0, 6));
        center.setBackground(BG);
        center.add(filterPanel, BorderLayout.NORTH);

        // ── Table ───────────────────────────────
        String[] cols = {"Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ", "Email"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setFont(FNT_PLAIN);
        table.setRowHeight(26);
        table.setGridColor(new Color(229, 231, 235));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(NAVY);
        table.getTableHeader().setFont(FNT_BOLD);
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(NAVY);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Độ rộng cột
        int[] widths = {80, 200, 110, 220, 160};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        scroll.getViewport().setBackground(Color.WHITE);

        center.add(scroll, BorderLayout.CENTER);

        // ── Status bar ──────────────────────────
        lblCount = new JLabel("Tổng: 0 nhà cung cấp");
        lblCount.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblCount.setForeground(new Color(107, 114, 128));
        lblCount.setBorder(new EmptyBorder(4, 14, 6, 0));
        center.add(lblCount, BorderLayout.SOUTH);

        root.add(center, BorderLayout.CENTER);

        // ── Bottom button ────────────────────────
        JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 10));
        btnBar.setBackground(BG);
        btnBar.setBorder(new MatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        JButton btnDong = actionBtn("✖  Đóng", new Color(185, 28, 28));
        btnDong.setForeground(Color.WHITE);
        btnDong.addActionListener(e -> dispose());
        btnBar.add(btnDong);
        root.add(btnBar, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ════════════════════════════════════════════
    private void loadAll() {
        fillTable(dao.getAll());
    }

    private void search() {
        List<NhaCungCap> result = dao.search(
            txtMaNCC .getText().trim(),
            txtTenNCC.getText().trim(),
            txtSDT   .getText().trim()
        );
        fillTable(result);
    }

    private void fillTable(List<NhaCungCap> list) {
        model.setRowCount(0);
        for (NhaCungCap ncc : list) {
            model.addRow(new Object[]{
                ncc.getMaNCC(),
                ncc.getTenNCC(),
                ncc.getSdt(),
                ncc.getDiaChi(),
                ncc.getEmail()
            });
        }
        lblCount.setText("Tổng: " + list.size() + " nhà cung cấp");
    }

    private void clearFilter() {
        txtMaNCC .setText("");
        txtTenNCC.setText("");
        txtSDT   .setText("");
    }

    // ── helpers ─────────────────────────────────
    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FNT_BOLD);
        l.setForeground(NAVY);
        return l;
    }

    private JTextField filterField() {
        JTextField tf = new JTextField(10);
        tf.setFont(FNT_PLAIN);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        return tf;
    }

    private JButton actionBtn(String text, Color bg) {
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