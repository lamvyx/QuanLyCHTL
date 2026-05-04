package gui;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TraCuuKhuyenMaiGUI extends JDialog {

    private static final Color NAVY  = new Color(15, 23, 42);
    private static final Color BLUE  = new Color(37, 99, 235);
    private static final Color BG    = new Color(248, 250, 252);
    private static final Color CARD  = Color.WHITE;
    private static final Color BORDER_CLR = new Color(226, 232, 240);
    private static final Color GREEN = new Color(5, 150, 105);
    private static final Color RED   = new Color(220, 38, 38);
    private static final Color YELLOW = new Color(217, 119, 6);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String[] COT = {
        "Mã KM", "Tên khuyến mãi", "Giảm (%)", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái", "Mô tả"
    };

    private JTextField txtTimKiem;
    private DefaultTableModel tableModel;
    private JTable table;
    private JLabel lblSoLuong;

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public TraCuuKhuyenMaiGUI(JFrame parent) {
        super(parent, "Tra cứu khuyến mãi", true);
        setSize(900, 540);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initUI();
        taiDuLieu(khuyenMaiDAO.layTatCa());
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);

        // ── Header ──────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));
        JLabel lblTitle = new JLabel("🔍  Tra cứu khuyến mãi");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(lblTitle, BorderLayout.WEST);
        root.add(header, BorderLayout.NORTH);

        // ── Thanh tìm kiếm ───────────────────────────────
        JPanel searchBar = new JPanel(new BorderLayout(10, 0));
        searchBar.setBackground(CARD);
        searchBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR),
                new EmptyBorder(12, 18, 12, 18)
        ));

        txtTimKiem = new JTextField();
        txtTimKiem.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR),
                new EmptyBorder(7, 10, 7, 10)
        ));
        txtTimKiem.setToolTipText("Nhập mã hoặc tên khuyến mãi...");

        JButton btnTimKiem = taoNut("Tìm kiếm", BLUE, Color.WHITE);
        JButton btnTatCa   = taoNut("Tất cả", new Color(107, 114, 128), Color.WHITE);

        btnTimKiem.addActionListener(e -> timKiem());
        btnTatCa.addActionListener(e -> {
            txtTimKiem.setText("");
            taiDuLieu(khuyenMaiDAO.layTatCa());
        });
        txtTimKiem.addActionListener(e -> timKiem());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        buttons.setOpaque(false);
        buttons.add(btnTimKiem);
        buttons.add(btnTatCa);

        searchBar.add(new JLabel("Tìm theo mã / tên:  ") {{
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setForeground(new Color(55, 65, 81));
        }}, BorderLayout.WEST);
        searchBar.add(txtTimKiem, BorderLayout.CENTER);
        searchBar.add(buttons, BorderLayout.EAST);
        root.add(searchBar, BorderLayout.NORTH);

        // ── Bảng dữ liệu ────────────────────────────────
        tableModel = new DefaultTableModel(COT, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(NAVY);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(241, 245, 249));
        table.getTableHeader().setForeground(NAVY);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_CLR));

        // Căn giữa cột số & trạng thái
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

        // Renderer màu cho trạng thái
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean focus, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, focus, row, col);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                String status = val == null ? "" : val.toString();
                switch (status) {
                    case "Đang diễn ra" -> { setForeground(GREEN); setBackground(sel ? new Color(219,234,254) : new Color(236,253,245)); }
                    case "Chưa bắt đầu" -> { setForeground(YELLOW); setBackground(sel ? new Color(219,234,254) : new Color(255,251,235)); }
                    default             -> { setForeground(RED);   setBackground(sel ? new Color(219,234,254) : new Color(254,242,242)); }
                }
                return this;
            }
        });

        // Độ rộng cột
        int[] widths = {80, 200, 90, 110, 110, 120, 180};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.setRowSorter(new TableRowSorter<>(tableModel));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        scroll.getViewport().setBackground(CARD);

        // ── Footer với số lượng ──────────────────────────
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG);
        footer.setBorder(new EmptyBorder(8, 18, 10, 18));

        lblSoLuong = new JLabel("Tổng: 0 bản ghi");
        lblSoLuong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSoLuong.setForeground(new Color(107, 114, 128));

        JButton btnDong = taoNut("Đóng", new Color(229, 231, 235), NAVY);
        btnDong.addActionListener(e -> dispose());

        footer.add(lblSoLuong, BorderLayout.WEST);
        footer.add(btnDong, BorderLayout.EAST);

        // ── Gộp layout ──────────────────────────────────
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.setBorder(new EmptyBorder(12, 12, 0, 12));
        center.add(searchBar, BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);

        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Tải dữ liệu vào bảng ────────────────────────────

    private void taiDuLieu(List<KhuyenMai> danhSach) {
        tableModel.setRowCount(0);
        for (KhuyenMai km : danhSach) {
            String trangThai = tinhTrangThai(km);
            tableModel.addRow(new Object[]{
                km.getMaKM(),
                km.getTenKM(),
                String.format("%.1f%%", km.getPhanTramGiam()),
                km.getNgayBD() != null ? km.getNgayBD().format(FMT) : "",
                km.getNgayKT() != null ? km.getNgayKT().format(FMT) : "",
                trangThai,
                km.getMoTa() != null ? km.getMoTa() : ""
            });
        }
        lblSoLuong.setText("Tổng: " + danhSach.size() + " bản ghi");
    }

    private String tinhTrangThai(KhuyenMai km) {
        if (km.getNgayBD() == null || km.getNgayKT() == null) return "Không xác định";
        if (km.conHieuLuc()) return "Đang diễn ra";
        if (java.time.LocalDate.now().isBefore(km.getNgayBD())) return "Chưa bắt đầu";
        return "Đã kết thúc";
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            taiDuLieu(khuyenMaiDAO.layTatCa());
        } else {
            taiDuLieu(khuyenMaiDAO.timKiem(tuKhoa));
        }
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