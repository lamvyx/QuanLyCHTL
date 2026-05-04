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
import javax.swing.JDialog;
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
    private final NhanVienWorkspacePanel nhanVienWorkspace = new NhanVienWorkspacePanel();
    private final SanPhamWorkspacePanel sanPhamWorkspace = new SanPhamWorkspacePanel();
    private final ThongKeWorkspacePanel thongKeWorkspace = new ThongKeWorkspacePanel();

    public HomeGUI(TaiKhoan taiKhoan) {
        super("Trang chủ");
        this.taiKhoan = taiKhoan;
        this.nhanVienDAO = new NhanVienDAO();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 560);
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
        add(mainContent, BorderLayout.CENTER);

        setJMenuBar(taoMenuBar());
    }

    private JPanel taoBanner() {
        JPanel banner = new JPanel(new BorderLayout(12, 0));
        banner.setBackground(NAVY);
        banner.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        JLabel lblLogo = new JLabel();
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon logoIcon = new ImageIcon("src/images/logo.jpg");
        if (logoIcon.getIconWidth() > 0 && logoIcon.getIconHeight() > 0) {
            Image scaledLogo = logoIcon.getImage().getScaledInstance(44, 44, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(scaledLogo));
        }

        DateTimeFormatter dinhDangNgay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String ngayLamViec = LocalDate.now().format(dinhDangNgay);

        JLabel lblThongTin = new JLabel(
                "Chào mừng " + taiKhoan.getUsername() + " | Ngày làm việc: " + ngayLamViec
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

        if (imageIcon.getIconWidth() > 0 && imageIcon.getIconHeight() > 0) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int availableWidth = Math.max(900, screenSize.width - 120);
            int availableHeight = Math.max(420, screenSize.height - 260);
            Image scaledImage = imageIcon.getImage().getScaledInstance(availableWidth, availableHeight, Image.SCALE_SMOOTH);
            lblBanner.setIcon(new ImageIcon(scaledImage));
        } else {
            lblBanner.setText("Không tìm thấy ảnh banner1.jpg");
            lblBanner.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        }

        card.add(lblBanner, BorderLayout.CENTER);
        center.add(card, BorderLayout.CENTER);
        return center;
    }

    private void hienThiTrangChu() {
        mainLayout.show(mainContent, "home");
    }

    private void hienThiNhanVien(String section) {
        mainLayout.show(mainContent, "nhanvien");
        switch (section) {
            case "them" -> nhanVienWorkspace.showThemNhanVien();
            case "traCuu" -> nhanVienWorkspace.showTraCuuNhanVien();
            case "capNhat" -> nhanVienWorkspace.showCapNhatNhanVien();
            case "phanQuyen" -> nhanVienWorkspace.showPhanQuyen();
            case "lapHoaDon" -> nhanVienWorkspace.showLapHoaDon();
            case "hoaDonDaLap" -> nhanVienWorkspace.showHoaDonDaLap();
            default -> nhanVienWorkspace.showThemNhanVien();
        }
    }

    private void hienThiSanPham(String section) {
        mainLayout.show(mainContent, "sanpham");
        switch (section) {
            case "them" -> sanPhamWorkspace.showThemSanPham();
            case "traCuu" -> sanPhamWorkspace.showTraCuuSanPham();
            case "capNhat" -> sanPhamWorkspace.showCapNhatSanPham();
            case "banNhanh" -> sanPhamWorkspace.showBanHangNhanh();
            default -> sanPhamWorkspace.showThemSanPham();
        }
    }

    private void hienThiThongKe(String section) {
        mainLayout.show(mainContent, "thongke");
        switch (section) {
            case "doanhThuNgay" -> thongKeWorkspace.showDoanhThuNgay();
            case "doanhThuThang" -> thongKeWorkspace.showDoanhThuThang();
            case "banChay" -> thongKeWorkspace.showSanPhamBanChay();
            case "hoaDon" -> thongKeWorkspace.showHoaDonBan();
            case "phieuNhap" -> thongKeWorkspace.showPhieuNhap();
            default -> thongKeWorkspace.showDoanhThuNgay();
        }
    }

    private JMenuBar taoMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JButton btnHome = new JButton("Trang chủ");
        styleMenuButton(btnHome, BLUE);
        btnHome.addActionListener(e -> hienThiTrangChu());
        menuBar.add(btnHome);

        menuBar.add(taoMenuSanPham());
        menuBar.add(taoMenuCrudVaTacVu(
            "Khách hàng",
            "Lọc theo mã, tên, số điện thoại, loại khách, điểm tích lũy",
            new String[] {
                "Xem lịch sử mua hàng",
                "Cập nhật điểm tích lũy"
            }
        ));
        menuBar.add(taoMenuNhanVien());
        menuBar.add(taoMenuCrudVaTacVu(
            "Khuyến mãi",
            "Lọc theo mã, kiểu khuyến mãi, thời gian hiệu lực, trạng thái",
            new String[] {
                "Kiểm tra khuyến mãi còn hiệu lực"
            }
        ));
        menuBar.add(taoMenuCrudVaTacVu(
            "Nhà cung cấp",
            "Lọc theo mã, tên, số điện thoại, email, địa chỉ",
            new String[] {
                "Xem phiếu nhập theo nhà cung cấp",
                "Đánh giá giao nhận"
            }
        ));
        menuBar.add(taoMenuThongKe());

        JMenu menuHeThong = new JMenu("Hệ thống");
        styleMenu(menuHeThong, NAVY);

        JMenuItem itemDoiMatKhau = new JMenuItem("Đổi mật khẩu");
        itemDoiMatKhau.addActionListener(e -> new DoiMatKhauGUI(this, taiKhoan).setVisible(true));

        JMenuItem itemThongTinCuaToi = new JMenuItem("Thông tin của tôi");
        itemThongTinCuaToi.addActionListener(e -> {
            NhanVien nhanVien = nhanVienDAO.timTheoMaTaiKhoan(taiKhoan.getMaTK());
            new ThongTinNhanVienGUI(this, taiKhoan, nhanVien).setVisible(true);
        });

        JMenuItem itemDangXuat = new JMenuItem("Đăng xuất");
        itemDangXuat.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn đăng xuất không?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION) {
                dispose();
                new DangNhapGUI().setVisible(true);
            }
        });

        menuHeThong.add(itemThongTinCuaToi);
        menuHeThong.add(itemDoiMatKhau);
        menuHeThong.add(itemDangXuat);
        menuBar.add(menuHeThong);

        return menuBar;
    }

    private JMenu taoMenuCrudVaTacVu(String tenMenu, String boLocGoiY, String[] tacVuThem) {
        JMenu menu = new JMenu(tenMenu);
        styleMenu(menu, BLUE);

        // Thêm
        JMenuItem itemThem = new JMenuItem("Thêm " + tenMenu.toLowerCase());
        itemThem.addActionListener(e -> {
            switch (tenMenu) {
                case "Khách hàng" -> new ThemKhachHangGUI(this).setVisible(true);
                case "Khuyến mãi" -> new ThemKhuyenMaiGUI(this).setVisible(true);
                case "Nhà cung cấp" -> new ThemNhaCungCapGUI(this).setVisible(true);
                default -> moTrangChucNang("Thêm " + tenMenu.toLowerCase(), "Chức năng đang phát triển");
            }
        });
        menu.add(itemThem);

        // Tra cứu
        JMenuItem itemTraCuu = new JMenuItem("Tra cứu " + tenMenu.toLowerCase());
        itemTraCuu.addActionListener(e -> {
            switch (tenMenu) {
                case "Khách hàng" -> new TraCuuKhachHangGUI(this).setVisible(true);
                case "Khuyến mãi" -> new TraCuuKhuyenMaiGUI(this).setVisible(true);
                case "Nhà cung cấp" -> new TraCuuNhaCungCapGUI(this).setVisible(true);
                default -> moTrangChucNang("Tra cứu " + tenMenu.toLowerCase(), "Chức năng đang phát triển");
            }
        });
        menu.add(itemTraCuu);

        // Cập nhật
        JMenuItem itemCapNhat = new JMenuItem("Cập nhật " + tenMenu.toLowerCase());
        itemCapNhat.addActionListener(e -> {
            switch (tenMenu) {
                case "Khách hàng" -> new CapNhatKhachHangGUI(this).setVisible(true);
                case "Khuyến mãi" -> new CapNhatKhuyenMaiGUI(this).setVisible(true);
                case "Nhà cung cấp" -> new CapNhatNhaCungCapGUI(this).setVisible(true);
                default -> moTrangChucNang("Cập nhật " + tenMenu.toLowerCase(), "Chức năng đang phát triển");
            }
        });
        menu.add(itemCapNhat);

        // Các tác vụ thêm
        if (tacVuThem != null && tacVuThem.length > 0) {
            menu.addSeparator();
            for (String tacVu : tacVuThem) {
                JMenuItem item = new JMenuItem(tacVu);
                item.addActionListener(e -> {
                    switch (tacVu) {
                        case "Xem lịch sử mua hàng" -> new LichSuMuaHangGUI().setVisible(true);
                        case "Cập nhật điểm tích lũy" -> new CapNhatDiemTichLuyGUI().setVisible(true);
                        case "Kiểm tra khuyến mãi còn hiệu lực" -> new KiemTraKhuyenMaiGUI(this).setVisible(true);
                        case "Xem phiếu nhập theo nhà cung cấp" -> new XemPhieuNhapNhaCungCapGUI(this).setVisible(true);
                        case "Đánh giá giao nhận"               -> new DanhGiaGiaoNhanGUI(this).setVisible(true);
                        default -> moTrangChucNang(tacVu, "Chức năng đang phát triển");
                    }
                });
                menu.add(item);
            }
        }

        return menu;
    }

    private JMenu taoMenuSanPham() {
        JMenu menuSanPham = new JMenu("Sản phẩm");
        styleMenu(menuSanPham, BLUE);
        menuSanPham.add(taoMenuItemSanPham("Thêm sản phẩm", e -> hienThiSanPham("them")));
        menuSanPham.add(taoMenuItemSanPham("Tra cứu sản phẩm", e -> hienThiSanPham("traCuu")));
        menuSanPham.add(taoMenuItemSanPham("Cập nhật sản phẩm", e -> hienThiSanPham("capNhat")));
        menuSanPham.addSeparator();
        menuSanPham.add(taoMenuItemSanPham("Bán hàng nhanh", e -> hienThiSanPham("banNhanh")));
        return menuSanPham;
    }

    private JMenu taoMenuNhanVien() {
        JMenu menuNhanVien = new JMenu("Nhân viên");
        styleMenu(menuNhanVien, BLUE);

        JMenuItem itemThem = new JMenuItem("Thêm nhân viên");
        itemThem.addActionListener(e -> hienThiNhanVien("them"));

        JMenuItem itemTraCuu = new JMenuItem("Tra cứu nhân viên");
        itemTraCuu.addActionListener(e -> hienThiNhanVien("traCuu"));

        JMenuItem itemCapNhat = new JMenuItem("Cập nhật nhân viên");
        itemCapNhat.addActionListener(e -> hienThiNhanVien("capNhat"));

        JMenuItem itemPhanQuyen = new JMenuItem("Phân quyền tài khoản");
        itemPhanQuyen.addActionListener(e -> hienThiNhanVien("phanQuyen"));

        JMenuItem itemLapHoaDon = new JMenuItem("Lập hóa đơn");
        itemLapHoaDon.addActionListener(e -> hienThiNhanVien("lapHoaDon"));

        JMenuItem itemHoaDonDaLap = new JMenuItem("Xem hóa đơn đã lập");
        itemHoaDonDaLap.addActionListener(e -> hienThiNhanVien("hoaDonDaLap"));

        menuNhanVien.add(itemThem);
        menuNhanVien.add(itemTraCuu);
        menuNhanVien.add(itemCapNhat);
        menuNhanVien.addSeparator();
        menuNhanVien.add(itemPhanQuyen);
        menuNhanVien.add(itemLapHoaDon);
        menuNhanVien.add(itemHoaDonDaLap);

        return menuNhanVien;
    }

    private JMenu taoMenuThongKe() {
        JMenu menu = new JMenu("Thống kê");
        styleMenu(menu, TEAL);
        menu.add(taoMenuItemThongKe("Doanh thu theo ngày", e -> hienThiThongKe("doanhThuNgay")));
        menu.add(taoMenuItemThongKe("Doanh thu theo tháng", e -> hienThiThongKe("doanhThuThang")));
        menu.add(taoMenuItemThongKe("Sản phẩm bán chạy", e -> hienThiThongKe("banChay")));
        menu.add(taoMenuItemThongKe("Hóa đơn bán", e -> hienThiThongKe("hoaDon")));
        menu.add(taoMenuItemThongKe("Phiếu nhập", e -> hienThiThongKe("phieuNhap")));
        return menu;
    }

    private JMenuItem taoMenuItemSanPham(String tieuDe, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(tieuDe);
        item.addActionListener(listener);
        return item;
    }

    private JMenuItem taoMenuItemThongKe(String tieuDe, java.awt.event.ActionListener listener) {
        JMenuItem item = new JMenuItem(tieuDe);
        item.addActionListener(listener);
        return item;
    }

    private JMenuItem taoMenuItemChucNang(String tieuDe, String moTa) {
        JMenuItem item = new JMenuItem(tieuDe);
        item.addActionListener(e -> moTrangChucNang(tieuDe, moTa));
        return item;
    }

    private void styleMenu(JMenu menu, Color foreground) {
        menu.setForeground(foreground);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 13));
        menu.setOpaque(true);
        menu.setBackground(Color.WHITE);
    }

    private void styleMenuButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    private void moTrangChucNang(String tieuDe, String moTa) {
        JDialog dialog = new JDialog(this, tieuDe, true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(420, 220);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 12));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                new EmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTieuDe = new JLabel(tieuDe, SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTieuDe.setForeground(NAVY);

        JLabel lblMoTa = new JLabel(moTa, SwingConstants.CENTER);
        lblMoTa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMoTa.setForeground(new Color(75, 85, 99));

        panel.add(lblTieuDe);
        panel.add(lblMoTa);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }
}
