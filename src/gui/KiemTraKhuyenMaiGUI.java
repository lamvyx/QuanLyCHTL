package gui;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class KiemTraKhuyenMaiGUI extends JDialog {

    private static final Color NAVY   = new Color(15, 23, 42);
    private static final Color TEAL   = new Color(20, 184, 166);
    private static final Color BG     = new Color(248, 250, 252);
    private static final Color CARD   = Color.WHITE;
    private static final Color BORDER_CLR = new Color(226, 232, 240);
    private static final Color GREEN  = new Color(5, 150, 105);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String[] COT = {
        "Mã KM", "Tên khuyến mãi", "Giảm (%)", "Ngày bắt đầu", "Ngày kết thúc", "Còn lại (ngày)", "Mô tả"
    };

    private DefaultTableModel tableModel;
    private JLabel lblSoLuong;
    private JLabel lblCapNhat;

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public KiemTraKhuyenMaiGUI(JFrame parent) {
        super(parent, "Khuyến mãi còn hiệu lực", true);
        setSize(860, 460);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        taiDuLieu();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        // ── Header ──────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(TEAL);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel lblTitle = new JLabel("✅  Khuyến mãi đang còn hiệu lực");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblCapNhat = new JLabel("Cập nhật lúc: --");
        lblCapNhat.setForeground(new Color(204, 251, 241));
        lblCapNhat.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        header.add(lblTitle, BorderLayout.WEST);
        header.add(lblCapNhat, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);

        // ── Thông tin tóm tắt ────────────────────────────
        JPanel infoBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        infoBar.setBackground(new Color(240, 253, 250));
        infoBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(167, 243, 208)));

        JLabel lblMoTa = new JLabel("Danh sách khuyến mãi đang áp dụng được (hôm nay nằm trong khoảng ngày bắt đầu – kết thúc)");
        lblMoTa.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblMoTa.setForeground(new Color(6, 78, 59));
        infoBar.add(lblMoTa);

        // ── Bảng dữ liệu ────────────────────────────────
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(204, 251, 241));
        table.setSelectionForeground(NAVY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 253, 250));
        table.getTableHeader().setForeground(new Color(6, 78, 59));
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, TEAL));

        // Renderer màu cho cột "Còn lại"
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 13));
                try {
                    int days = Integer.parseInt(val.toString());
                    if (days <= 3) {
                        setForeground(new Color(220, 38, 38));  // Sắp hết – đỏ
                    } else if (days <= 7) {
                        setForeground(new Color(217, 119, 6)); // Gần hết – vàng
                    } else {
                        setForeground(GREEN);                   // Còn lâu – xanh
                    }
                } catch (NumberFormatException ex) {
                    setForeground(NAVY);
                }
                setBackground(sel ? new Color(204, 251, 241) : CARD);
                return this;
            }
        });

        // Căn giữa
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i : new int[]{0, 2, 3, 4})
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);

        // Độ rộng cột
        int[] widths = {75, 200, 80, 110, 110, 110, 180};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        scroll.getViewport().setBackground(CARD);

        // ── Center wrapper ───────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(10, 12, 0, 12));
        center.add(infoBar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(8, 18, 10, 18));

        lblSoLuong = new JLabel("Đang tải...");
        lblSoLuong.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSoLuong.setForeground(GREEN);

        JPanel footerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        footerButtons.setOpaque(false);

        JButton btnLamMoi = taoNut("↻ Làm mới", TEAL, Color.WHITE);
        JButton btnDong   = taoNut("Đóng", new Color(229, 231, 235), NAVY);

        btnLamMoi.addActionListener(e -> taiDuLieu());
        btnDong.addActionListener(e -> dispose());

        footerButtons.add(btnLamMoi);
        footerButtons.add(btnDong);

        footer.add(lblSoLuong, BorderLayout.WEST);
        footer.add(footerButtons, BorderLayout.EAST);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Tải dữ liệu ─────────────────────────────────────

    private void taiDuLieu() {
        tableModel.setRowCount(0);
        List<KhuyenMai> danhSach = khuyenMaiDAO.layConHieuLuc();
        LocalDate homNay = LocalDate.now();

        for (KhuyenMai km : danhSach) {
            long conLai = java.time.temporal.ChronoUnit.DAYS.between(homNay, km.getNgayKT());
            tableModel.addRow(new Object[]{
                km.getMaKM(),
                km.getTenKM(),
                String.format("%.1f%%", km.getPhanTramGiam()),
                km.getNgayBD() != null ? km.getNgayBD().format(FMT) : "",
                km.getNgayKT() != null ? km.getNgayKT().format(FMT) : "",
                String.valueOf(conLai),
                km.getMoTa() != null ? km.getMoTa() : ""
            });
        }

        lblSoLuong.setText("✅  Có " + danhSach.size() + " khuyến mãi đang hoạt động hôm nay");
        lblCapNhat.setText("Cập nhật lúc: " + homNay.format(FMT));
    }

    private JButton taoNut(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(7, 16, 7, 16));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}