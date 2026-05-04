package gui;

import java.awt.BorderLayout;
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
        add(taoNoiDungChinh(), BorderLayout.CENTER);

        setJMenuBar(taoMenuBar());
    }

    private JPanel taoBanner() {
        JPanel banner = new JPanel(new BorderLayout());
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

    private JMenuBar taoMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(226, 232, 240)));

        JButton btnHome = new JButton("Trang chủ");
        styleMenuButton(btnHome, BLUE);
        btnHome.addActionListener(e -> {
            dispose();
            new HomeGUI(taiKhoan).setVisible(true);
        });
        menuBar.add(btnHome);

        menuBar.add(taoMenuCrudVaTacVu(
            "Sản phẩm",
            "Lọc theo mã, tên, loại hàng, giá, tồn kho, nhà cung cấp",
            new String[] {
                "Bán hàng nhanh",
                "Kiểm tra tồn kho",
                "Xem sản phẩm sắp hết hạn"
            }
        ));
        menuBar.add(taoMenuCrudVaTacVu(
            "Khách hàng",
            "Lọc theo mã, tên, số điện thoại, loại khách, điểm tích lũy",
            new String[] {
                "Xem lịch sử mua hàng",
                "Cập nhật điểm tích lũy"
            }
        ));
        menuBar.add(taoMenuCrudVaTacVu(
            "Nhân viên",
            "Lọc theo mã, tên, số điện thoại, tài khoản, ngày vào làm",
            new String[] {
                "Phân quyền tài khoản",
                "Lập hóa đơn",
                "Xem hóa đơn đã lập"
            }
        ));
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

<<<<<<< HEAD
//    private JMenu taoMenuCrudVaTacVu(String tenMenu, String boLocGoiY, String[] tacVuThem) {
//        JMenu menu = new JMenu(tenMenu);
//        styleMenu(menu, BLUE);
//
//        //menu.add(taoMenuItemChucNang("Thêm " + tenMenu.toLowerCase(), "Mở màn hình thêm " + tenMenu.toLowerCase()));
//        JMenuItem itemThem = new JMenuItem("Thêm khách hàng");
//        itemThem.addActionListener(e -> new ThemKhachHangGUI(this).setVisible(true));
//        menu.add(itemThem);
//        
//        //menu.add(taoMenuItemChucNang("Tra cứu " + tenMenu.toLowerCase(), "Màn hình tra cứu " + tenMenu.toLowerCase() + " - " + boLocGoiY));
//        JMenuItem itemTraCuu = new JMenuItem("Tra cứu khách hàng");
//        itemTraCuu.addActionListener(e -> new TraCuuKhachHangGUI(this).setVisible(true));
//        menu.add(itemTraCuu);
//        
//        //menu.add(taoMenuItemChucNang("Cập nhật " + tenMenu.toLowerCase(), "Mở màn hình cập nhật " + tenMenu.toLowerCase()));
//        JMenuItem itemCapNhat = new JMenuItem("Cập nhật khách hàng");
//        itemCapNhat.addActionListener(e -> new CapNhatKhachHangGUI(this).setVisible(true));
//        menu.add(itemCapNhat);
//        
//     // Menu "Xem lịch sử mua hàng"
//        new LichSuMuaHangGUI().setVisible(true);
//        
//
//        if (tacVuThem != null && tacVuThem.length > 0) {
//            menu.addSeparator();
//            for (String tacVu : tacVuThem) {
//                menu.add(taoMenuItemChucNang(tacVu, "Tác vụ " + tacVu.toLowerCase() + " cho " + tenMenu.toLowerCase()));
//            }
//        }
//
//        return menu;
//    }
    
=======
>>>>>>> dcf7c0fe2cf37d3adb70b3c0f09771caa7965032
    private JMenu taoMenuCrudVaTacVu(String tenMenu, String boLocGoiY, String[] tacVuThem) {
        JMenu menu = new JMenu(tenMenu);
        styleMenu(menu, BLUE);

<<<<<<< HEAD
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
=======
        menu.add(taoMenuItemChucNang("Thêm " + tenMenu.toLowerCase(), "Mở màn hình thêm " + tenMenu.toLowerCase()));
        menu.add(taoMenuItemChucNang("Tra cứu " + tenMenu.toLowerCase(), "Màn hình tra cứu " + tenMenu.toLowerCase() + " - " + boLocGoiY));
        menu.add(taoMenuItemChucNang("Cập nhật " + tenMenu.toLowerCase(), "Mở màn hình cập nhật " + tenMenu.toLowerCase()));

        if (tacVuThem != null && tacVuThem.length > 0) {
            menu.addSeparator();
            for (String tacVu : tacVuThem) {
                menu.add(taoMenuItemChucNang(tacVu, "Tác vụ " + tacVu.toLowerCase() + " cho " + tenMenu.toLowerCase()));
>>>>>>> dcf7c0fe2cf37d3adb70b3c0f09771caa7965032
            }
        }

        return menu;
    }

    private JMenu taoMenuThongKe() {
        JMenu menu = new JMenu("Thống kê");
        styleMenu(menu, TEAL);
        menu.add(taoMenuItemChucNang("Doanh thu theo ngày", "Màn hình thống kê doanh thu theo ngày"));
        menu.add(taoMenuItemChucNang("Doanh thu theo tháng", "Màn hình thống kê doanh thu theo tháng"));
        menu.add(taoMenuItemChucNang("Tồn kho", "Màn hình thống kê tồn kho"));
        menu.add(taoMenuItemChucNang("Sản phẩm bán chạy", "Màn hình thống kê sản phẩm bán chạy"));
        menu.add(taoMenuItemChucNang("Hóa đơn bán", "Màn hình thống kê hóa đơn bán"));
        menu.add(taoMenuItemChucNang("Phiếu nhập", "Màn hình thống kê phiếu nhập"));
        return menu;
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
