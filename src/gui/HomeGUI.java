package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import dao.NhanVienDAO;
import entity.NhanVien;
import entity.TaiKhoan;

public class HomeGUI extends JFrame {
    private static final Color NAVY = new Color(15, 23, 42);
    private static final Color BLUE = new Color(37, 99, 235);
    private static final Color TEAL = new Color(20, 184, 166);
    private static final Color BG = new Color(248, 250, 252);
    private static final Color CARD = Color.WHITE;

    private final TaiKhoan taiKhoan;
    private final NhanVienDAO nhanVienDAO;
    private final CardLayout mainLayout = new CardLayout();
    private final JPanel mainContent = new JPanel(mainLayout);

    // Toàn bộ các Workspace Panels
    private final NhanVienWorkspacePanel nhanVienWorkspace = new NhanVienWorkspacePanel();
    private final SanPhamWorkspacePanel sanPhamWorkspace = new SanPhamWorkspacePanel();
    private final ThongKeWorkspacePanel thongKeWorkspace = new ThongKeWorkspacePanel();
    private final KhachHangWorkspacePanel khachHangWorkspace = new KhachHangWorkspacePanel();
    private final NhaCungCapWorkspacePanel nhaCungCapWorkspace = new NhaCungCapWorkspacePanel();
    private final KhuyenMaiWorkspacePanel khuyenMaiWorkspace = new KhuyenMaiWorkspacePanel();

    public HomeGUI(TaiKhoan taiKhoan) {
        super("Trang chủ - POS System");
        this.taiKhoan = taiKhoan;
        this.nhanVienDAO = new NhanVienDAO();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(taoBanner(), BorderLayout.NORTH);
        
        mainContent.setBackground(BG);
        mainContent.add(taoNoiDungChinh(), "home");
        mainContent.add(nhanVienWorkspace, "nhanvien");
        mainContent.add(sanPhamWorkspace, "sanpham");
        mainContent.add(thongKeWorkspace, "thongke");
        mainContent.add(khachHangWorkspace, "khachhang");
        mainContent.add(nhaCungCapWorkspace, "nhacungcap");
        mainContent.add(khuyenMaiWorkspace, "khuyenmai");
        
        add(mainContent, BorderLayout.CENTER);
        setJMenuBar(taoMenuBar());
    }

    private JPanel taoBanner() {
        JPanel banner = new JPanel(new BorderLayout(12, 0));
        banner.setBackground(NAVY);
        banner.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        try {
            ImageIcon logoIcon = new ImageIcon("src/images/logo.jpg");
            if (logoIcon.getIconWidth() > 0 && logoIcon.getIconHeight() > 0) {
                Image scaledLogo = logoIcon.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(scaledLogo));
            }
        } catch (Exception e) {
            System.err.println("Lỗi tải logo: " + e.getMessage());
        }

        DateTimeFormatter dinhDangNgay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String ngayLamViec = LocalDate.now().format(dinhDangNgay);

        JLabel lblThongTin = new JLabel(
                "Chào mừng " + taiKhoan.getUsername() + " | Ngày: " + ngayLamViec
        );
        lblThongTin.setForeground(Color.WHITE);
        lblThongTin.setFont(new Font("Segoe UI", Font.BOLD, 17));

        JLabel lblBadge = new JLabel("CỬA HÀNG TIỆN LỢI");
        lblBadge.setForeground(new Color(191, 219, 254));
        lblBadge.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JPanel left = new JPanel(new BorderLayout(10, 0));
        left.setOpaque(false);
        left.add(lblLogo, BorderLayout.WEST);

        JPanel textBlock = new JPanel(new GridLayout(2, 1, 0, 2));
        textBlock.setOpaque(false);
        textBlock.add(lblBadge);
        textBlock.add(lblThongTin);

        left.add(textBlock, BorderLayout.CENTER);
        banner.add(left, BorderLayout.WEST);

        return banner;
    }

    private JPanel taoNoiDungChinh() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(BG);
        center.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            new EmptyBorder(14, 14, 14, 14)
        ));

        ImageIcon imageIcon = new ImageIcon("src/images/banner1.jpg");
        JLabel lblBanner = new JLabel();
        lblBanner.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            if (imageIcon.getIconWidth() > 0 && imageIcon.getIconHeight() > 0) {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int w = Math.max(900, screenSize.width - 100);
                int h = Math.max(400, screenSize.height - 250);
                lblBanner.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH)));
            } else {
                lblBanner.setText("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG POS");
                lblBanner.setFont(new Font("Segoe UI", Font.BOLD, 32));
            }
        } catch (Exception e) {
            lblBanner.setText("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG POS");
            lblBanner.setFont(new Font("Segoe UI", Font.BOLD, 32));
            System.err.println("Lỗi tải banner: " + e.getMessage());
        }

        card.add(lblBanner, BorderLayout.CENTER);
        center.add(card, BorderLayout.CENTER);
        return center;
    }

    // --- CÁC PHƯƠNG THỨC CHUYỂN TRANG (KHÔNG POP-UP) ---

    private void hienThiTrangChu() { mainLayout.show(mainContent, "home"); }

    private void hienThiNhanVien(String s) {
        mainLayout.show(mainContent, "nhanvien");
        switch (s) {
            case "them" -> nhanVienWorkspace.showThemNhanVien();
            case "traCuu" -> nhanVienWorkspace.showTraCuuNhanVien();
            case "capNhat" -> nhanVienWorkspace.showCapNhatNhanVien();
            case "phanQuyen" -> nhanVienWorkspace.showPhanQuyen();
        }
    }

    private void hienThiSanPham(String s) {
        mainLayout.show(mainContent, "sanpham");
        switch (s) {
            case "them" -> sanPhamWorkspace.showThemSanPham();
            case "traCuu" -> sanPhamWorkspace.showTraCuuSanPham();
            case "capNhat" -> sanPhamWorkspace.showCapNhatSanPham();
            case "banNhanh" -> sanPhamWorkspace.showBanHangNhanh();
            case "nhapHang" -> sanPhamWorkspace.showNhapHang();
        }
    }

    private void hienThiKhachHang(String s) {
        mainLayout.show(mainContent, "khachhang");
        switch (s) {
            case "them" -> khachHangWorkspace.showThemKhachHang();
            case "traCuu" -> khachHangWorkspace.showTraCuuKhachHang();
            case "capNhat" -> khachHangWorkspace.showCapNhatKhachHang();
            case "history" -> khachHangWorkspace.showLichSuMuaHang();
        }
    }

    private void hienThiNhaCungCap(String s) {
        mainLayout.show(mainContent, "nhacungcap");
        switch (s) {
            case "them" -> nhaCungCapWorkspace.showThemNCC();
            case "traCuu" -> nhaCungCapWorkspace.showTraCuuNCC();
            case "capNhat" -> nhaCungCapWorkspace.showCapNhatNCC();
            case "phieuNhap" -> nhaCungCapWorkspace.showPhieuNhap();
        }
    }

    private void hienThiKhuyenMai(String s) {
        mainLayout.show(mainContent, "khuyenmai");
        switch (s) {
            case "them" -> khuyenMaiWorkspace.showThemKM();
            case "traCuu" -> khuyenMaiWorkspace.showTraCuuKM();
            case "capNhat" -> khuyenMaiWorkspace.showCapNhatKM();
        }
    }

    private void hienThiThongKe(String s) {
        mainLayout.show(mainContent, "thongke");
        switch (s) {
            case "doanhThuNgay" -> thongKeWorkspace.showDoanhThuNgay();
            case "doanhThuThang" -> thongKeWorkspace.showDoanhThuThang();
            case "banChay" -> thongKeWorkspace.showSanPhamBanChay();
            case "hoaDon" -> thongKeWorkspace.showHoaDonBan();
        }
    }

    private JMenuBar taoMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        boolean isQuanLy = "QUAN_LY".equalsIgnoreCase(taiKhoan.getRole());

        JButton btnHome = new JButton("Trang chủ");
        styleMenuButton(btnHome, BLUE);
        btnHome.addActionListener(e -> hienThiTrangChu());
        menuBar.add(btnHome);

        // Menu Sản phẩm
        JMenu menuSP = new JMenu("Sản phẩm");
        styleMenu(menuSP, BLUE);
        if (isQuanLy) {
            menuSP.add(taoItem("Thêm sản phẩm", e -> hienThiSanPham("them")));
        }
        menuSP.add(taoItem("Tra cứu sản phẩm", e -> hienThiSanPham("traCuu")));
        if (isQuanLy) {
            menuSP.add(taoItem("Cập nhật sản phẩm", e -> hienThiSanPham("capNhat")));
        }
        menuSP.addSeparator();
        menuSP.add(taoItem("Bán hàng nhanh (POS)", e -> hienThiSanPham("banNhanh")));
        if (isQuanLy) {
            menuSP.add(taoItem("Thêm hàng vào kho", e -> hienThiSanPham("nhapHang")));
        }
        menuBar.add(menuSP);

        // Menu Khách hàng
        JMenu menuKH = new JMenu("Khách hàng");
        styleMenu(menuKH, BLUE);
        menuKH.add(taoItem("Thêm khách hàng", e -> hienThiKhachHang("them")));
        menuKH.add(taoItem("Tra cứu khách hàng", e -> hienThiKhachHang("traCuu")));
        menuKH.add(taoItem("Cập nhật khách hàng", e -> hienThiKhachHang("capNhat")));
        menuKH.addSeparator();
        menuKH.add(taoItem("Xem lịch sử mua hàng", e -> hienThiKhachHang("history")));
        menuBar.add(menuKH);

        // Menu Nhân viên - Chỉ Quản lý mới thấy
        if (isQuanLy) {
            JMenu menuNV = new JMenu("Nhân viên");
            styleMenu(menuNV, BLUE);
            menuNV.add(taoItem("Thêm nhân viên", e -> hienThiNhanVien("them")));
            menuNV.add(taoItem("Tra cứu nhân viên", e -> hienThiNhanVien("traCuu")));
            menuNV.add(taoItem("Cập nhật nhân viên", e -> hienThiNhanVien("capNhat")));
            menuNV.addSeparator();
            menuNV.add(taoItem("Phân quyền tài khoản", e -> hienThiNhanVien("phanQuyen")));
            menuBar.add(menuNV);
        }

        // Menu Khuyến mãi
        JMenu menuKM = new JMenu("Khuyến mãi");
        styleMenu(menuKM, BLUE);
        if (isQuanLy) {
            menuKM.add(taoItem("Thêm khuyến mãi", e -> hienThiKhuyenMai("them")));
        }
        menuKM.add(taoItem("Tra cứu khuyến mãi", e -> hienThiKhuyenMai("traCuu")));
        if (isQuanLy) {
            menuKM.add(taoItem("Cập nhật khuyến mãi", e -> hienThiKhuyenMai("capNhat")));
        }
        menuBar.add(menuKM);

        // Menu Nhà cung cấp
        JMenu menuNCC = new JMenu("Nhà cung cấp");
        styleMenu(menuNCC, BLUE);
        if (isQuanLy) {
            menuNCC.add(taoItem("Thêm nhà cung cấp", e -> hienThiNhaCungCap("them")));
        }
        menuNCC.add(taoItem("Tra cứu nhà cung cấp", e -> hienThiNhaCungCap("traCuu")));
        if (isQuanLy) {
            menuNCC.add(taoItem("Cập nhật nhà cung cấp", e -> hienThiNhaCungCap("capNhat")));
            menuNCC.addSeparator();
            menuNCC.add(taoItem("Xem phiếu nhập theo nhà cung cấp", e -> hienThiNhaCungCap("phieuNhap")));
        }
        menuBar.add(menuNCC);

        // Menu Thống kê - Chỉ Quản lý mới thấy
        if (isQuanLy) {
            JMenu menuTK = new JMenu("Thống kê");
            styleMenu(menuTK, TEAL);
            menuTK.add(taoItem("Doanh thu ngày", e -> hienThiThongKe("doanhThuNgay")));
            menuTK.add(taoItem("Doanh thu tháng", e -> hienThiThongKe("doanhThuThang")));
            menuTK.add(taoItem("Sản phẩm bán chạy", e -> hienThiThongKe("banChay")));
            menuTK.add(taoItem("Hóa đơn bán", e -> hienThiThongKe("hoaDon")));
            menuBar.add(menuTK);
        }

        // Hệ thống
        JMenu menuSys = new JMenu("Hệ thống");
        styleMenu(menuSys, NAVY);
        menuSys.add(taoItem("Thông tin của tôi", e -> {
            NhanVien nv = nhanVienDAO.timTheoMaTaiKhoan(taiKhoan.getMaTK());
            new ThongTinNhanVienGUI(this, taiKhoan, nv).setVisible(true);
        }));
        menuSys.add(taoItem("Đổi mật khẩu", e -> new DoiMatKhauGUI(this, taiKhoan).setVisible(true)));
        menuSys.add(taoItem("Đăng xuất", e -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có muốn đăng xuất?") == 0) {
                dispose(); new DangNhapGUI().setVisible(true);
            }
        }));
        menuBar.add(menuSys);

        return menuBar;
    }

    private JMenuItem taoItem(String t, java.awt.event.ActionListener l) {
        JMenuItem item = new JMenuItem(t);
        item.addActionListener(l);
        return item;
    }

    private void styleMenu(JMenu menu, Color foreground) {
        menu.setForeground(foreground);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private void showAboutDialog() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG TIỆN LỢI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(30, 58, 138));
        panel.add(title, BorderLayout.NORTH);

        String html = "<html><body style='width: 400px; font-family: Segoe UI; font-size: 11pt;'>" +
            "<h3 style='color: #2563eb;'>ĐỘI NGŨ PHÁT TRIỂN</h3>" +
            "<div style='margin-bottom: 15px;'>" +
            "<b>1. Lâm Phú Vỹ (MSSV: 23712311 - Trưởng nhóm)</b><br/>" +
            "• Thiết kế hệ thống xác thực bảo mật (Đăng nhập, Đổi mật khẩu).<br/>" +
            "• Phát triển module Bán hàng thông minh (POS) và Quản lý nhập kho.<br/>" +
            "• Xây dựng hệ thống Báo cáo & Thống kê kinh doanh trực quan." +
            "</div>" +
            "<div>" +
            "<b>2. Ngô Tấn Tài (MSSV: 22001545)</b><br/>" +
            "• Phát triển hệ thống quản lý Khách hàng và Lịch sử giao dịch.<br/>" +
            "• Thiết kế module Quản lý Đối tác & Nhà cung cấp và Phiếu nhập.<br/>" +
            "• Triển khai công cụ Quản lý chương trình Ưu đãi & Khuyến mãi." +
            "</div>" +
            "<hr/><p style='font-size: 10pt; color: #64748b;'>Phiên bản 1.0.0 © 2026 QLCuaHang Project</p>" +
            "</body></html>";
        
        JLabel content = new JLabel(html);
        panel.add(new JScrollPane(content), BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Giới thiệu dự án", JOptionPane.PLAIN_MESSAGE);
    }

    private void showAboutDialog() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG TIỆN LỢI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 58, 138));
        panel.add(title, BorderLayout.NORTH);

        String html = "<html><body style='width: 450px; font-family: Segoe UI; font-size: 11pt; line-height: 1.5;'>" +
            "<div style='text-align: center; color: #64748b; margin-bottom: 20px;'>Học phần: Phát triển ứng dụng hướng đối tượng</div>" +
            
            "<div style='background-color: #f8fafc; padding: 15px; border-radius: 8px; margin-bottom: 15px; border-left: 5px solid #2563eb;'>" +
            "<b style='font-size: 12pt; color: #1e3a8a;'>Lâm Phú Vỹ (MSSV: 23712311 - Trưởng nhóm)</b><br/>" +
            "<div style='margin-top: 5px; color: #334155;'>" +
            "• Nghiên cứu và triển khai hệ thống xác thực người dùng bảo mật, quản trị tài khoản tập trung.<br/>" +
            "• Thiết kế giải pháp bán hàng thời gian thực (POS) tích hợp quy trình điều phối hàng hóa nhập kho.<br/>" +
            "• Phát triển hệ thống báo cáo quản trị và thống kê doanh thu đa chiều kết hợp trực quan hóa dữ liệu." +
            "</div></div>" +
            
            "<div style='background-color: #f8fafc; padding: 15px; border-radius: 8px; margin-bottom: 20px; border-left: 5px solid #0891b2;'>" +
            "<b style='font-size: 12pt; color: #164e63;'>Ngô Tấn Tài (MSSV: 22001545)</b><br/>" +
            "<div style='margin-top: 5px; color: #334155;'>" +
            "• Phân tích và xây dựng phân hệ quản lý quan hệ khách hàng (CRM), theo dõi lịch sử giao dịch chi tiết.<br/>" +
            "• Thiết lập quy trình quản lý chuỗi cung ứng, chuẩn hóa dữ liệu nhà cung cấp và chứng từ nhập kho.<br/>" +
            "• Phát triển công cụ quản trị chiến dịch ưu đãi, tối ưu hóa các chương trình khuyến mãi kích cầu." +
            "</div></div>" +
            
            "<hr style='border: 0; border-top: 1px solid #e2e8f0;'/>" +
            "<p style='font-size: 10pt; color: #94a3b8; text-align: center;'>Phiên bản 1.0.0 © 2026 QLCuaHang Project Team</p>" +
            "</body></html>";
        
        JLabel content = new JLabel(html);
        panel.add(content, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Thông tin dự án & Thành viên", JOptionPane.PLAIN_MESSAGE);
    }

    private void showAboutDialog() {
        JPanel panel = new JPanel(new BorderLayout(10, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        panel.setBackground(Color.WHITE);

        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG TIỆN LỢI", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(30, 58, 138));
        panel.add(title, BorderLayout.NORTH);

        String html = "<html><body style='width: 480px; font-family: Segoe UI; font-size: 11pt; line-height: 1.6;'>" +
            "<div style='text-align: center; color: #64748b; margin-bottom: 20px; font-style: italic;'>Học phần: Phát triển ứng dụng hướng đối tượng</div>" +
            
            "<div style='background-color: #f0f9ff; padding: 15px; border-radius: 10px; margin-bottom: 15px; border: 1px solid #bae6fd;'>" +
            "<b style='font-size: 12pt; color: #0369a1;'>Lâm Phú Vỹ (MSSV: 23712311 - Trưởng nhóm)</b><br/>" +
            "<div style='margin-top: 8px; color: #334155;'>" +
            "• <b>Kiến trúc hệ thống:</b> Thiết kế và triển khai cơ chế xác thực bảo mật đa tầng, quản trị phiên làm việc và bảo mật tài khoản.<br/>" +
            "• <b>Giải pháp nghiệp vụ:</b> Phát triển module Bán hàng (POS) thời gian thực và quy trình điều phối hàng hóa nhập kho tự động.<br/>" +
            "• <b>Quản trị dữ liệu:</b> Xây dựng hệ thống báo cáo BI (Business Intelligence), phân tích doanh thu và trực quan hóa dữ liệu kinh doanh." +
            "</div></div>" +
            
            "<div style='background-color: #fdfaf1; padding: 15px; border-radius: 10px; margin-bottom: 20px; border: 1px solid #fef3c7;'>" +
            "<b style='font-size: 12pt; color: #b45309;'>Ngô Tấn Tài (MSSV: 22001545)</b><br/>" +
            "<div style='margin-top: 8px; color: #334155;'>" +
            "• <b>Quản trị khách hàng:</b> Xây dựng phân hệ CRM chuyên sâu, theo dõi hành vi người dùng và quản lý lịch sử giao dịch đa kênh.<br/>" +
            "• <b>Chuỗi cung ứng:</b> Thiết lập quy trình quản lý đối tác & nhà cung cấp, chuẩn hóa hệ thống chứng từ và kiểm soát phiếu nhập kho.<br/>" +
            "• <b>Marketing & Sales:</b> Phát triển công cụ quản trị chiến dịch ưu đãi, tối ưu hóa các chương trình khuyến mãi và kích cầu mua sắm." +
            "</div></div>" +
            
            "<div style='text-align: center; font-size: 10pt; color: #94a3b8; border-top: 1px solid #f1f5f9; padding-top: 10px;'>" +
            "Phiên bản 1.0.0 Stable • © 2026 QLCuaHang Development Team" +
            "</div>" +
            "</body></html>";
        
        JLabel content = new JLabel(html);
        panel.add(content, BorderLayout.CENTER);

        JOptionPane.showMessageDialog(this, panel, "Thông tin dự án & Phân công nhiệm vụ", JOptionPane.PLAIN_MESSAGE);
    }

    private void styleMenuButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.BLACK); // Thay đổi thành chữ đen theo yêu cầu
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }
}
