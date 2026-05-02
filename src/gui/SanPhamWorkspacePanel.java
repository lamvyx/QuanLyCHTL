package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.File;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import dao.SanPhamDAO;
import entity.SanPham;

public class SanPhamWorkspacePanel extends JPanel {
    private static final String CARD_THEM = "them";
    private static final String CARD_TRA_CUU = "traCuu";
    private static final String CARD_CAP_NHAT = "capNhat";
    private static final String CARD_BAN_NHANH = "banNhanh";

    private final java.awt.CardLayout cardLayout = new java.awt.CardLayout();
    private final JPanel cardContainer = new JPanel(cardLayout);
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final dao.NhaCungCapDAO nhaCungCapDAO = new dao.NhaCungCapDAO();

    private final JTextField txtThemMaSP = new JTextField();
    private final JTextField txtThemTenSP = new JTextField();
    private final JTextField txtThemGia = new JTextField();
    private final DatePickerField txtThemHanSuDung = NhanVienUiHelper.createDatePicker();
    private final JTextField txtThemSoLuongTon = new JTextField();
    private final JComboBox<String> cboThemLoaiSP = taoComboLoaiSanPham();
    private final javax.swing.JComboBox<String> cboThemMaNCC = new javax.swing.JComboBox<>();
    private final JTextField txtThemHinhAnh = new JTextField();
    private final JLabel lblThemAnh = taoLabelAnh();

    // Filter components for consolidated inquiry
    private final JTextField txtLocTenSP = new JTextField(15);
    private final JComboBox<String> cboLocLoaiSP = taoComboLocLoaiSanPham();
    private final DefaultTableModel modelTraCuu = new DefaultTableModel(new Object[] {
            "Mã SP", "Tên SP", "Giá", "Hạn sử dụng", "Tồn kho", "Loại SP", "Mã NCC"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblTraCuu = new JTable(modelTraCuu);
    private final JLabel lblTraCuuAnh = taoLabelAnh();
    private final JComboBox<String> cboLocTonKho = new JComboBox<>(new String[] {
            "Tất cả", "Thấp (< 5)", "Vừa (5-20)", "Cao (> 20)"
    });
    private final JComboBox<String> cboLocHanHSD = new JComboBox<>(new String[] {
            "Tất cả", "Sắp hết hạn (≤ 30 ngày)", "Hết hạn"
    });
    private final JComboBox<String> cboLocMaNCC = new JComboBox<>();

    private final JTextField txtCapNhatMaSP = new JTextField();
    private final JTextField txtCapNhatTenSP = new JTextField();
    private final JTextField txtCapNhatGia = new JTextField();
    private final DatePickerField txtCapNhatHanSuDung = NhanVienUiHelper.createDatePicker();
    private final JTextField txtCapNhatSoLuongTon = new JTextField();
    private final JComboBox<String> cboCapNhatLoaiSP = taoComboLoaiSanPham();
    private final javax.swing.JComboBox<String> cboCapNhatMaNCC = new javax.swing.JComboBox<>();
    private final JTextField txtCapNhatHinhAnh = new JTextField();
    private final JLabel lblCapNhatAnh = taoLabelAnh();

    private final DefaultTableModel modelBanNhanh = new DefaultTableModel(new Object[] {
            "Mã SP", "Tên SP", "Giá", "Tồn kho", "Loại SP", "Mã NCC"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblBanNhanh = new JTable(modelBanNhanh);

    public SanPhamWorkspacePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(NhanVienUiHelper.BG);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(taoThanhTieuDe(), BorderLayout.NORTH);
        add(cardContainer, BorderLayout.CENTER);

        cardContainer.add(taoManHinhThem(), CARD_THEM);
        cardContainer.add(taoManHinhTraCuu(), CARD_TRA_CUU);
        cardContainer.add(taoManHinhCapNhat(), CARD_CAP_NHAT);
        cardContainer.add(taoManHinhBanNhanh(), CARD_BAN_NHANH);

        cardLayout.show(cardContainer, CARD_THEM);
        taiDanhSachNhaCungCapAsync();
    }

    private void taiDanhSachNhaCungCapAsync() {
        new SwingWorker<java.util.List<entity.NhaCungCap>, Void>() {
            @Override
            protected java.util.List<entity.NhaCungCap> doInBackground() {
                return nhaCungCapDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    java.util.List<entity.NhaCungCap> list = get();
                    cboThemMaNCC.removeAllItems();
                    cboCapNhatMaNCC.removeAllItems();
                    cboLocMaNCC.removeAllItems();
                    cboLocMaNCC.addItem("Tất cả");
                    for (entity.NhaCungCap ncc : list) {
                        String display = ncc.getMaNCC() + " - " + ncc.getTenNCC();
                        cboThemMaNCC.addItem(display);
                        cboCapNhatMaNCC.addItem(display);
                        cboLocMaNCC.addItem(display);
                    }
                    // set default selection if exists
                    if (cboThemMaNCC.getItemCount() > 0) cboThemMaNCC.setSelectedIndex(0);
                } catch (InterruptedException | java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(SanPhamWorkspacePanel.this, "Không thể tải danh sách nhà cung cấp: " + ex.getMessage());
                }
            }
        }.execute();
    }

    public void showTraCuuSanPham() {
        cardLayout.show(cardContainer, CARD_TRA_CUU);
        xoaTrangLocFilter();
        taiTraCuuAsync();
    }

    public void showCapNhatSanPham() {
        cardLayout.show(cardContainer, CARD_CAP_NHAT);
    }

    public void showBanHangNhanh() {
        cardLayout.show(cardContainer, CARD_BAN_NHANH);
        taiBanNhanhAsync();
    }

    private void xoaTrangLocFilter() {
        txtLocTenSP.setText("");
        cboLocLoaiSP.setSelectedIndex(0);
        cboLocTonKho.setSelectedIndex(0);
        cboLocHanHSD.setSelectedIndex(0);
        cboLocMaNCC.setSelectedIndex(0);
    }

    public void showThemSanPham() {
        cardLayout.show(cardContainer, CARD_THEM);
        try {
            txtThemMaSP.setText(sanPhamDAO.taoMaSPMoi());
        } catch (Exception ex) {
            // ignore generation error, leave field empty for manual entry
        }
    }

    private JPanel taoThanhTieuDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel tieuDe = NhanVienUiHelper.createTitle("Sản phẩm");
        JLabel moTa = new JLabel("Quản lý sản phẩm, ảnh theo loại hàng và các màn hình tra cứu nhanh");
        moTa.setForeground(new Color(75, 85, 99));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(tieuDe);
        left.add(moTa);

        panel.add(left, BorderLayout.WEST);
        return panel;
    }

    private JPanel taoCardNen() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(NhanVienUiHelper.BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return panel;
    }

    private JPanel taoManHinhThem() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Thêm sản phẩm mới"), BorderLayout.NORTH);

        JPanel form = NhanVienUiHelper.createCardPanel();
        form.setBorder(new EmptyBorder(18, 18, 18, 18));

        txtThemMaSP.setColumns(26);
        txtThemMaSP.setEditable(false);
        txtThemTenSP.setColumns(26);
        txtThemGia.setColumns(26);
        txtThemSoLuongTon.setColumns(26);
        cboThemMaNCC.setPrototypeDisplayValue("NCC000 - Tên nhà cung cấp dài");
        txtThemHinhAnh.setColumns(26);
        txtThemHinhAnh.setEditable(false);

        cboThemLoaiSP.setEditable(true);

        NhanVienUiHelper.addRow(form, 0, "Mã sản phẩm:", txtThemMaSP);
        NhanVienUiHelper.addRow(form, 1, "Tên sản phẩm:", txtThemTenSP);
        NhanVienUiHelper.addRow(form, 2, "Giá:", txtThemGia);
        NhanVienUiHelper.addRow(form, 3, "Hạn sử dụng (dd/MM/yyyy):", txtThemHanSuDung);
        NhanVienUiHelper.addRow(form, 4, "Tồn kho:", txtThemSoLuongTon);
        NhanVienUiHelper.addRow(form, 5, "Loại sản phẩm:", cboThemLoaiSP);
        NhanVienUiHelper.addRow(form, 6, "Mã nhà cung cấp:", cboThemMaNCC);

        JButton btnChonAnh = new JButton("Chọn ảnh");
        NhanVienUiHelper.styleGhostButton(btnChonAnh);
        btnChonAnh.addActionListener(e -> chonAnhSanPham(txtThemHinhAnh, lblThemAnh, cboThemLoaiSP.getSelectedItem()));

        JPanel anhPanel = new JPanel(new BorderLayout(8, 0));
        anhPanel.setOpaque(false);
        anhPanel.add(txtThemHinhAnh, BorderLayout.CENTER);
        anhPanel.add(btnChonAnh, BorderLayout.EAST);
        NhanVienUiHelper.addRow(form, 7, "Ảnh sản phẩm:", anhPanel);

        JButton btnLuu = new JButton("Lưu sản phẩm");
        NhanVienUiHelper.styleButton(btnLuu, NhanVienUiHelper.BLUE);
        btnLuu.addActionListener(e -> luuSanPham());

        JButton btnXoa = new JButton("Xóa trắng");
        NhanVienUiHelper.styleGhostButton(btnXoa);
        btnXoa.addActionListener(e -> xoaTrangThem());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnLuu);
        actions.add(btnXoa);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(12, 6, 6, 6);
        form.add(actions, gbc);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setOpaque(false);
        left.add(form, BorderLayout.CENTER);

        JPanel right = taoPreviewPanel(lblThemAnh, "Ảnh sản phẩm theo loại hàng");

        root.add(left, BorderLayout.CENTER);
        root.add(right, BorderLayout.EAST);
        return root;
    }

    private JPanel taoManHinhTraCuu() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Tra cứu & Quản lý Sản phẩm"), BorderLayout.NORTH);

        // Create filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        filterPanel.setOpaque(false);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Bộ lọc"),
                new EmptyBorder(8, 8, 8, 8)
        ));

        // Text search filter
        filterPanel.add(new JLabel("Tên/Mã SP:"));
        txtLocTenSP.setColumns(12);
        filterPanel.add(txtLocTenSP);

        // Category filter
        filterPanel.add(new JLabel("Loại SP:"));
        filterPanel.add(cboLocLoaiSP);

        // Stock filter
        filterPanel.add(new JLabel("Tồn kho:"));
        filterPanel.add(cboLocTonKho);

        // Expiry date filter
        filterPanel.add(new JLabel("Hạn SD:"));
        filterPanel.add(cboLocHanHSD);

        // Supplier filter
        filterPanel.add(new JLabel("Nhà CC:"));
        cboLocMaNCC.setPrototypeDisplayValue("NCC000 - Tên nhà cung cấp dài");
        filterPanel.add(cboLocMaNCC);

        // Action buttons
        JButton btnTim = new JButton("Tìm kiếm");
        NhanVienUiHelper.styleButton(btnTim, NhanVienUiHelper.BLUE);
        btnTim.addActionListener(e -> applyFiltersAndSearch());
        filterPanel.add(btnTim);

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> {
            xoaTrangLocFilter();
            taiTraCuuAsync();
        });
        filterPanel.add(btnTaiLai);

        tblTraCuu.setRowSelectionAllowed(true);
        tblTraCuu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTraCuu.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblTraCuu.getSelectedRow();
                if (row >= 0) {
                    capNhatAnhSanPham(lblTraCuuAnh, modelTraCuu.getValueAt(row, 5));
                }
            }
        });

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(new JScrollPane(tblTraCuu), BorderLayout.CENTER);
        center.add(taoPreviewPanel(lblTraCuuAnh, "Xem ảnh theo loại sản phẩm"), BorderLayout.EAST);

        root.add(filterPanel, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private void applyFiltersAndSearch() {
        String keyword = txtLocTenSP.getText().trim();
        String selectedCategory = (String) cboLocLoaiSP.getSelectedItem();
        String selectedStock = (String) cboLocTonKho.getSelectedItem();
        String selectedExpiry = (String) cboLocHanHSD.getSelectedItem();
        String selectedSupplier = (String) cboLocMaNCC.getSelectedItem();

        taiTraCuu(new SwingWorker<List<SanPham>, Void>() {
            @Override
            protected List<SanPham> doInBackground() {
                // Get all or search by keyword
                List<SanPham> results;
                if (!keyword.isEmpty()) {
                    results = sanPhamDAO.timTheoTuKhoa(keyword);
                } else {
                    results = sanPhamDAO.timTatCa();
                }

                // Apply filters
                results = applyFilterLogic(results, selectedCategory, selectedStock, selectedExpiry, selectedSupplier);
                return results;
            }
        });
    }

    private List<SanPham> applyFilterLogic(List<SanPham> danhSach, String category, String stock, String expiry, String supplier) {
        List<SanPham> filtered = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (SanPham sp : danhSach) {
            // Category filter
            if (!category.equals("Tất cả") && !category.equals(sp.getLoaiSP())) {
                continue;
            }

            // Stock filter
            Integer soLuong = sp.getSoLuongTon();
            if (soLuong == null) soLuong = 0;
            if (stock.contains("Thấp")) {
                if (soLuong >= 5) continue;
            } else if (stock.contains("Vừa")) {
                if (soLuong < 5 || soLuong > 20) continue;
            } else if (stock.contains("Cao")) {
                if (soLuong <= 20) continue;
            }

            // Expiry filter
            if (!expiry.equals("Tất cả")) {
                LocalDate hanSD = sp.getHanSuDung();
                if (hanSD == null) hanSD = LocalDate.now().plusYears(100);
                if (expiry.contains("Sắp hết hạn")) {
                    LocalDate gioiHan = now.plusDays(30);
                    if (hanSD.isAfter(gioiHan)) continue;
                } else if (expiry.contains("Hết hạn")) {
                    if (!hanSD.isBefore(now)) continue;
                }
            }

            // Supplier filter
            if (!supplier.equals("Tất cả")) {
                String maNCC = extractMaFromSupplier(supplier);
                if (!maNCC.equals(sp.getMaNCC())) {
                    continue;
                }
            }

            filtered.add(sp);
        }

        return filtered;
    }

    private String extractMaFromSupplier(String supplier) {
        if (supplier.contains(" - ")) {
            return supplier.substring(0, supplier.indexOf(" - ")).trim();
        }
        return supplier;
    }

    private JPanel taoManHinhCapNhat() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Cập nhật sản phẩm"), BorderLayout.NORTH);

        JPanel form = NhanVienUiHelper.createCardPanel();
        form.setBorder(new EmptyBorder(18, 18, 18, 18));

        txtCapNhatMaSP.setColumns(26);
        txtCapNhatMaSP.setEditable(true);
        txtCapNhatTenSP.setColumns(26);
        txtCapNhatGia.setColumns(26);
        txtCapNhatSoLuongTon.setColumns(26);
        cboCapNhatMaNCC.setPrototypeDisplayValue("NCC000 - Tên nhà cung cấp dài");
        txtCapNhatHinhAnh.setColumns(26);
        txtCapNhatHinhAnh.setEditable(false);

        cboCapNhatLoaiSP.setEditable(true);

        NhanVienUiHelper.addRow(form, 0, "Mã sản phẩm:", txtCapNhatMaSP);
        NhanVienUiHelper.addRow(form, 1, "Tên sản phẩm:", txtCapNhatTenSP);
        NhanVienUiHelper.addRow(form, 2, "Giá:", txtCapNhatGia);
        NhanVienUiHelper.addRow(form, 3, "Hạn sử dụng (dd/MM/yyyy):", txtCapNhatHanSuDung);
        NhanVienUiHelper.addRow(form, 4, "Tồn kho:", txtCapNhatSoLuongTon);
        NhanVienUiHelper.addRow(form, 5, "Loại sản phẩm:", cboCapNhatLoaiSP);
        NhanVienUiHelper.addRow(form, 6, "Mã nhà cung cấp:", cboCapNhatMaNCC);

        JButton btnChonAnh = new JButton("Chọn ảnh");
        NhanVienUiHelper.styleGhostButton(btnChonAnh);
        btnChonAnh.addActionListener(e -> chonAnhSanPham(txtCapNhatHinhAnh, lblCapNhatAnh, cboCapNhatLoaiSP.getSelectedItem()));

        JPanel anhPanel = new JPanel(new BorderLayout(8, 0));
        anhPanel.setOpaque(false);
        anhPanel.add(txtCapNhatHinhAnh, BorderLayout.CENTER);
        anhPanel.add(btnChonAnh, BorderLayout.EAST);
        NhanVienUiHelper.addRow(form, 7, "Ảnh sản phẩm:", anhPanel);

        JButton btnTai = new JButton("Tải dữ liệu");
        NhanVienUiHelper.styleButton(btnTai, NhanVienUiHelper.BLUE);
        btnTai.addActionListener(e -> taiSanPhamCapNhat());

        JButton btnCapNhat = new JButton("Cập nhật");
        NhanVienUiHelper.styleButton(btnCapNhat, NhanVienUiHelper.TEAL);
        btnCapNhat.addActionListener(e -> capNhatSanPham());

        JButton btnXoaSP = new JButton("Xóa sản phẩm");
        btnXoaSP.setBackground(new Color(239, 68, 68));
        btnXoaSP.setForeground(Color.black);
        btnXoaSP.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        btnXoaSP.setFocusPainted(false);
        btnXoaSP.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        btnXoaSP.setOpaque(true);
        btnXoaSP.setContentAreaFilled(true);
        btnXoaSP.addActionListener(e -> xoaSanPham());

        JButton btnXoa = new JButton("Xóa trắng");
        NhanVienUiHelper.styleGhostButton(btnXoa);
        btnXoa.addActionListener(e -> xoaTrangCapNhat());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnTai);
        actions.add(btnCapNhat);
        actions.add(btnXoaSP);
        actions.add(btnXoa);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(12, 6, 6, 6);
        form.add(actions, gbc);

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setOpaque(false);
        left.add(form, BorderLayout.CENTER);

        JPanel right = taoPreviewPanel(lblCapNhatAnh, "Ảnh sản phẩm khi cập nhật");

        root.add(left, BorderLayout.CENTER);
        root.add(right, BorderLayout.EAST);
        return root;
    }

    private JPanel taoManHinhBanNhanh() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Bán hàng nhanh"), BorderLayout.NORTH);

        tblBanNhanh.setRowSelectionAllowed(true);
        tblBanNhanh.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JLabel moTa = new JLabel("Danh sách sản phẩm có ảnh minh họa theo loại hàng để chọn nhanh.");
        moTa.setForeground(new Color(75, 85, 99));

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> taiBanNhanhAsync());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(moTa);
        top.add(btnTaiLai);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tblBanNhanh), BorderLayout.CENTER);
        return root;
    }

    private void luuSanPham() {
        try {
            String maSP = txtThemMaSP.getText().trim();
            String tenSP = txtThemTenSP.getText().trim();
            if (NhanVienUiHelper.isBlank(maSP) || NhanVienUiHelper.isBlank(tenSP)) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm và tên sản phẩm không được để trống.");
                return;
            }

            String maNCC = extractMaNCCFromCombo(cboThemMaNCC);

            boolean thanhCong = sanPhamDAO.themSanPham(new SanPham(
                    maSP,
                    tenSP,
                    parseDouble(txtThemGia.getText(), "Giá"),
                    NhanVienUiHelper.getDatePickerValue(txtThemHanSuDung),
                    NhanVienUiHelper.parseIntegerNullable(txtThemSoLuongTon.getText(), "Tồn kho"),
                        extractComboText(cboThemLoaiSP),
                        maNCC,
                    chuanHoaDuongDanAnh(txtThemHinhAnh.getText())
            ));

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm thành công.");
                xoaTrangThem();
                taiTraCuuAsync();
                taiBanNhanhAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm sản phẩm.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void taiSanPhamCapNhat() {
        String maSP = txtCapNhatMaSP.getText().trim();
        if (NhanVienUiHelper.isBlank(maSP)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm.");
            return;
        }

        SanPham sanPham = sanPhamDAO.timTheoMa(maSP);
        if (sanPham == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm.");
            return;
        }

        txtCapNhatTenSP.setText(NhanVienUiHelper.textOf(sanPham.getTenSP()));
        txtCapNhatGia.setText(sanPham.getGia() != null ? String.valueOf(sanPham.getGia()) : "");
        NhanVienUiHelper.setDatePickerValue(txtCapNhatHanSuDung, sanPham.getHanSuDung());
        txtCapNhatSoLuongTon.setText(sanPham.getSoLuongTon() != null ? String.valueOf(sanPham.getSoLuongTon()) : "");
        setComboText(cboCapNhatLoaiSP, sanPham.getLoaiSP());
        // select supplier in combo if present
        String maNCC = NhanVienUiHelper.textOf(sanPham.getMaNCC());
        for (int i = 0; i < cboCapNhatMaNCC.getItemCount(); i++) {
            String item = (String) cboCapNhatMaNCC.getItemAt(i);
            if (item != null && item.startsWith(maNCC)) {
                cboCapNhatMaNCC.setSelectedIndex(i);
                break;
            }
        }
        txtCapNhatHinhAnh.setText(NhanVienUiHelper.textOf(sanPham.getHinhAnh()));
        capNhatAnhSanPham(lblCapNhatAnh, sanPham.getLoaiSP(), sanPham.getHinhAnh());
    }

    private void capNhatSanPham() {
        try {
            String maSP = txtCapNhatMaSP.getText().trim();
            String tenSP = txtCapNhatTenSP.getText().trim();
            if (NhanVienUiHelper.isBlank(maSP) || NhanVienUiHelper.isBlank(tenSP)) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm và tên sản phẩm không được để trống.");
                return;
            }

                String maNCC = extractMaNCCFromCombo(cboCapNhatMaNCC);

                boolean thanhCong = sanPhamDAO.capNhatSanPham(new SanPham(
                    maSP,
                    tenSP,
                    parseDouble(txtCapNhatGia.getText(), "Giá"),
                    NhanVienUiHelper.getDatePickerValue(txtCapNhatHanSuDung),
                    NhanVienUiHelper.parseIntegerNullable(txtCapNhatSoLuongTon.getText(), "Tồn kho"),
                    extractComboText(cboCapNhatLoaiSP),
                    maNCC,
                    chuanHoaDuongDanAnh(txtCapNhatHinhAnh.getText())
            ));

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật sản phẩm.");
                capNhatAnhSanPham(lblCapNhatAnh, extractComboText(cboCapNhatLoaiSP), chuanHoaDuongDanAnh(txtCapNhatHinhAnh.getText()));
                taiTraCuuAsync();
                taiBanNhanhAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật sản phẩm.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void taiTraCuuAsync() {
        taiTraCuu(new SwingWorker<List<SanPham>, Void>() {
            @Override
            protected List<SanPham> doInBackground() {
                return sanPhamDAO.timTatCa();
            }
        });
    }

    private void taiTraCuu(SwingWorker<List<SanPham>, Void> worker) {
        worker.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                try {
                    napBangSanPham(modelTraCuu, worker.get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(this, "Không thể tải danh sách sản phẩm: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(this, "Không thể tải danh sách sản phẩm: " + ex.getMessage());
                }
            }
        });
        worker.execute();
    }

    private void taiBanNhanhAsync() {
        new SwingWorker<List<SanPham>, Void>() {
            @Override
            protected List<SanPham> doInBackground() {
                return sanPhamDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    napBangSanPham(modelBanNhanh, get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(SanPhamWorkspacePanel.this, "Không thể tải danh sách bán nhanh: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(SanPhamWorkspacePanel.this, "Không thể tải danh sách bán nhanh: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void napBangSanPham(DefaultTableModel model, List<SanPham> danhSach) {
        model.setRowCount(0);
        for (SanPham sp : danhSach) {
            model.addRow(new Object[] {
                    sp.getMaSP(),
                    sp.getTenSP(),
                    sp.getGia(),
                    NhanVienUiHelper.formatDate(sp.getHanSuDung()),
                    sp.getSoLuongTon(),
                    sp.getLoaiSP(),
                    sp.getMaNCC()
            });
        }
    }

    private JPanel taoPreviewPanel(JLabel labelAnh, String tieuDe) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setPreferredSize(new java.awt.Dimension(260, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(12, 12, 12, 12)
        ));

        JLabel title = new JLabel(tieuDe, JLabel.CENTER);
        title.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        title.setForeground(NhanVienUiHelper.NAVY);

        panel.add(title, BorderLayout.NORTH);
        panel.add(labelAnh, BorderLayout.CENTER);
        return panel;
    }

    private JLabel taoLabelAnh() {
        JLabel label = new JLabel("Chưa có ảnh", JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(249, 250, 251));
        label.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
        label.setPreferredSize(new java.awt.Dimension(230, 240));
        return label;
    }

    private JComboBox<String> taoComboLoaiSanPham() {
        return new JComboBox<>(new String[] {
                "Bánh ngọt",
                "Bánh hấp",
                "Đồ chiên",
                "Hàng tiêu dùng",
                "Lẩu",
                "Mì",
                "Omusubi",
                "Sandwich",
                "Sushi",
                "Thức uống",
                "Tráng miệng"
        });
    }

    private JComboBox<String> taoComboLocLoaiSanPham() {
        return new JComboBox<>(new String[] {
                "Tất cả",
                "Bánh ngọt",
                "Bánh hấp",
                "Đồ chiên",
                "Hàng tiêu dùng",
                "Lẩu",
                "Mì",
                "Omusubi",
                "Sandwich",
                "Sushi",
                "Thức uống",
                "Tráng miệng"
        });
    }

    private void xoaTrangThem() {
        try {
            txtThemMaSP.setText(sanPhamDAO.taoMaSPMoi());
        } catch (Exception ex) {
            txtThemMaSP.setText("");
        }
        txtThemTenSP.setText("");
        txtThemGia.setText("");
        NhanVienUiHelper.setDatePickerValue(txtThemHanSuDung, LocalDate.now());
        txtThemSoLuongTon.setText("");
        if (cboThemMaNCC.getItemCount() > 0) cboThemMaNCC.setSelectedIndex(0);
        txtThemHinhAnh.setText("");
        setComboText(cboThemLoaiSP, "Bánh ngọt");
        lblThemAnh.setIcon(null);
        lblThemAnh.setText("Chưa có ảnh");
    }

    private void xoaTrangCapNhat() {
        txtCapNhatMaSP.setText("");
        txtCapNhatTenSP.setText("");
        txtCapNhatGia.setText("");
        NhanVienUiHelper.setDatePickerValue(txtCapNhatHanSuDung, LocalDate.now());
        txtCapNhatSoLuongTon.setText("");
        if (cboCapNhatMaNCC.getItemCount() > 0) cboCapNhatMaNCC.setSelectedIndex(0);
        txtCapNhatHinhAnh.setText("");
        setComboText(cboCapNhatLoaiSP, "Bánh ngọt");
        lblCapNhatAnh.setIcon(null);
        lblCapNhatAnh.setText("Chưa có ảnh");
    }

    private void setComboText(JComboBox<String> comboBox, String value) {
        comboBox.setSelectedItem(value);
        if (comboBox.getSelectedItem() == null && comboBox.isEditable()) {
            comboBox.getEditor().setItem(value);
        }
    }

    private String extractComboText(JComboBox<String> comboBox) {
        Object item = comboBox.isEditable() ? comboBox.getEditor().getItem() : comboBox.getSelectedItem();
        return item == null ? "" : String.valueOf(item).trim();
    }

    private Double parseDouble(String text, String fieldName) {
        if (NhanVienUiHelper.isBlank(text)) {
            return null;
        }
        try {
            return Double.valueOf(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " phải là số hợp lệ.");
        }
    }

    private void capNhatAnhSanPham(JLabel label, Object loaiSPValue) {
        capNhatAnhSanPham(label, loaiSPValue, null);
    }

    private void capNhatAnhSanPham(JLabel label, Object loaiSPValue, String duongDanAnh) {
        String loaiSP = loaiSPValue == null ? "" : String.valueOf(loaiSPValue);
        File imageFile = timAnhSanPham(loaiSP, duongDanAnh);
        if (imageFile != null && imageFile.exists()) {
            ImageIcon icon = new ImageIcon(imageFile.getAbsolutePath());
            Image scaled = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaled));
            label.setText("");
        } else {
            label.setIcon(null);
            label.setText(loaiSP.isBlank() ? "Chưa có ảnh" : "Không tìm thấy ảnh");
        }
    }

    private File timAnhSanPham(String loaiSP, String duongDanAnh) {
        File fileTheoDuongDan = moFileAnh(duongDanAnh);
        if (fileTheoDuongDan != null) {
            return fileTheoDuongDan;
        }

        String folderName = mapLoaiSanPhamToFolder(loaiSP);
        File folder = new File("src/images/" + folderName);
        if (!folder.exists() || !folder.isDirectory()) {
            return timAnhFallback(loaiSP);
        }

        File[] files = folder.listFiles(file -> {
            String name = file.getName().toLowerCase();
            return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".gif") || name.endsWith(".bmp") || name.endsWith(".webp");
        });
        if (files == null || files.length == 0) {
            return timAnhFallback(loaiSP);
        }

        Arrays.sort(files, Comparator.comparing(File::getName));
        return files[0];
    }

    private File moFileAnh(String duongDanAnh) {
        if (NhanVienUiHelper.isBlank(duongDanAnh)) {
            return null;
        }

        File file = new File(duongDanAnh.trim());
        if (file.exists()) {
            return file;
        }

        File relative = new File("src/images", duongDanAnh.trim());
        if (relative.exists()) {
            return relative;
        }

        return null;
    }

    private void chonAnhSanPham(JTextField textField, JLabel previewLabel, Object loaiSPValue) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn ảnh sản phẩm");
        chooser.setFileFilter(new FileNameExtensionFilter("Ảnh (*.png, *.jpg, *.jpeg, *.webp, *.gif)", "png", "jpg", "jpeg", "webp", "gif"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            textField.setText(selected.getPath());
            capNhatAnhSanPham(previewLabel, loaiSPValue, selected.getPath());
        }
    }

    private String chuanHoaDuongDanAnh(String duongDanAnh) {
        if (NhanVienUiHelper.isBlank(duongDanAnh)) {
            return null;
        }
        return duongDanAnh.trim();
    }

    private String extractMaNCCFromCombo(javax.swing.JComboBox<String> combo) {
        if (combo == null) return "";
        Object sel = combo.getSelectedItem();
        if (sel == null) return "";
        String s = String.valueOf(sel).trim();
        if (s.contains(" - ")) {
            return s.substring(0, s.indexOf(" - ")).trim();
        }
        return s;
    }

    private File timAnhFallback(String loaiSP) {
        String normalized = normalize(loaiSP);
        if (normalized.contains("banh mi")) {
            return new File("src/images/banhmi.png");
        }
        if (normalized.contains("trasua") || normalized.contains("tra sua") || normalized.contains("thuc uong")) {
            return new File("src/images/trasua.png");
        }
        if (normalized.contains("xuc xich") || normalized.contains("do chien")) {
            return new File("src/images/xucxich.png");
        }
        return new File("src/images/banner1.jpg");
    }

    private String mapLoaiSanPhamToFolder(String loaiSP) {
        String normalized = normalize(loaiSP);
        if (normalized.contains("banh hap")) return "banhhap";
        if (normalized.contains("banh ngot")) return "banhngot";
        if (normalized.contains("do chien")) return "dochien";
        if (normalized.contains("hang tieu dung")) return "hangtieudung";
        if (normalized.contains("lau")) return "lau";
        if (normalized.contains("omusubi")) return "omusubi";
        if (normalized.contains("sandwich")) return "sandwich";
        if (normalized.contains("sushi")) return "sushi";
        if (normalized.contains("thuc uong") || normalized.contains("do uong") || normalized.contains("tra sua")) return "thucuong";
        if (normalized.contains("trang mieng")) return "trangmieng";
        if (normalized.contains("mi")) return "mi";
        return "hangtieudung";
    }

    private String normalize(String text) {
        if (text == null) {
            return "";
        }
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.toLowerCase().trim();
    }

    private void xoaSanPham() {
        String maSP = txtCapNhatMaSP.getText().trim();
        if (NhanVienUiHelper.isBlank(maSP)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm để xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn chắc chắn muốn xóa sản phẩm " + maSP + "?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean thanhCong = sanPhamDAO.xoaSanPham(maSP);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm thành công.");
                xoaTrangCapNhat();
                taiTraCuuAsync();
                taiBanNhanhAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa sản phẩm.");
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }
}