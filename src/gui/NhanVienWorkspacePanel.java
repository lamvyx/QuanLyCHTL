package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.table.DefaultTableModel;

import dao.HoaDonDAO;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.HoaDon;
import entity.NhanVien;
import entity.TaiKhoan;

public class NhanVienWorkspacePanel extends JPanel {
    private static final String CARD_THEM = "them";
    private static final String CARD_TRA_CUU = "traCuu";
    private static final String CARD_CAP_NHAT = "capNhat";
    private static final String CARD_PHAN_QUYEN = "phanQuyen";
    private static final String CARD_LAP_HOA_DON = "lapHoaDon";
    private static final String CARD_HOA_DON_DA_LAP = "hoaDonDaLap";

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardContainer = new JPanel(cardLayout);
    private final NhanVienDAO nhanVienDAO = new NhanVienDAO();
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();

    private final JTextField txtThemMaNV = new JTextField();
    private final JTextField txtThemTenNV = new JTextField();
    private final JTextField txtThemSdt = new JTextField();
    private final JTextField txtThemDiaChi = new JTextField();
    private final DatePickerField txtThemNgaySinh = NhanVienUiHelper.createDatePicker();
    private final DatePickerField txtThemNgayVaoLam = NhanVienUiHelper.createDatePicker();
    private final JTextField txtThemMaTK = new JTextField();

    private final JTextField txtTraCuu = new JTextField(26);
    private final DefaultTableModel modelTraCuu = new DefaultTableModel(new Object[] {
            "Mã NV", "Họ tên", "SĐT", "Địa chỉ", "Ngày sinh", "Ngày vào làm", "Mã TK"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblTraCuu = new JTable(modelTraCuu);

    private final JTextField txtCapNhatMaNV = new JTextField();
    private final JTextField txtCapNhatTenNV = new JTextField();
    private final JTextField txtCapNhatSdt = new JTextField();
    private final JTextField txtCapNhatDiaChi = new JTextField();
    private final DatePickerField txtCapNhatNgaySinh = NhanVienUiHelper.createDatePicker();
    private final DatePickerField txtCapNhatNgayVaoLam = NhanVienUiHelper.createDatePicker();
    private final JTextField txtCapNhatMaTK = new JTextField();

    private final DefaultTableModel modelPhanQuyen = new DefaultTableModel(new Object[] {
            "Mã TK", "Username", "Vai trò", "Trạng thái"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblPhanQuyen = new JTable(modelPhanQuyen);
    private final JComboBox<String> cboRole = new JComboBox<>(new String[] { "Nhân viên", "Quản lý" });
    private final JComboBox<String> cboTrangThai = new JComboBox<>(new String[] { "Hoạt động", "Khóa" });

    private final JTextField txtLapMaHD = new JTextField();
    private final DatePickerField txtLapNgayLap = NhanVienUiHelper.createDatePicker();
    private final JTextField txtLapMaNV = new JTextField();
    private final JTextField txtLapMaKH = new JTextField();
    private final JTextField txtLapMaThue = new JTextField();
    private final JTextField txtLapMaKM = new JTextField();
    private final JTextField txtLocHoaDonTheoNV = new JTextField(18);
    private final DefaultTableModel modelHoaDon = new DefaultTableModel(new Object[] {
            "Mã HD", "Ngày lập", "Mã NV", "Mã KH", "Mã thuế", "Mã KM"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblHoaDon = new JTable(modelHoaDon);

    public NhanVienWorkspacePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(NhanVienUiHelper.BG);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        add(taoThanhTieuDe(), BorderLayout.NORTH);
        add(cardContainer, BorderLayout.CENTER);

        cardContainer.add(taoManHinhThem(), CARD_THEM);
        cardContainer.add(taoManHinhTraCuu(), CARD_TRA_CUU);
        cardContainer.add(taoManHinhCapNhat(), CARD_CAP_NHAT);
        cardContainer.add(taoManHinhPhanQuyen(), CARD_PHAN_QUYEN);
        cardContainer.add(taoManHinhLapHoaDon(), CARD_LAP_HOA_DON);
        cardContainer.add(taoManHinhHoaDonDaLap(), CARD_HOA_DON_DA_LAP);

        cardLayout.show(cardContainer, CARD_THEM);
    }

    public void showThemNhanVien() {
        cardLayout.show(cardContainer, CARD_THEM);
    }

    public void showTraCuuNhanVien() {
        cardLayout.show(cardContainer, CARD_TRA_CUU);
        taiTraCuuAsync();
    }

    public void showCapNhatNhanVien() {
        cardLayout.show(cardContainer, CARD_CAP_NHAT);
    }

    public void showPhanQuyen() {
        cardLayout.show(cardContainer, CARD_PHAN_QUYEN);
        taiPhanQuyenAsync();
    }

    public void showLapHoaDon() {
        cardLayout.show(cardContainer, CARD_LAP_HOA_DON);
        NhanVienUiHelper.setDatePickerValue(txtLapNgayLap, LocalDate.now());
        taiHoaDonAsync();
    }

    public void showHoaDonDaLap() {
        cardLayout.show(cardContainer, CARD_HOA_DON_DA_LAP);
        taiHoaDonAsync();
    }

    public void showCapNhatNhanVien(String maNV) {
        showCapNhatNhanVien();
        txtCapNhatMaNV.setText(maNV);
        taiDuLieuCapNhatAsync();
    }

    private JPanel taoThanhTieuDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel tieuDe = NhanVienUiHelper.createTitle("Nhân viên");
        JLabel moTa = new JLabel("Quản lý nhân viên, phân quyền và hóa đơn ngay trong cửa sổ chính");
        moTa.setForeground(new java.awt.Color(75, 85, 99));

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
                BorderFactory.createLineBorder(new java.awt.Color(229, 231, 235)),
                new EmptyBorder(14, 14, 14, 14)
        ));
        return panel;
    }

    private JPanel taoManHinhThem() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Thêm nhân viên mới"), BorderLayout.NORTH);

        JPanel form = NhanVienUiHelper.createCardPanel();
        form.setBorder(new EmptyBorder(18, 18, 18, 18));

        txtThemMaNV.setColumns(26);
        txtThemTenNV.setColumns(26);
        txtThemSdt.setColumns(26);
        txtThemDiaChi.setColumns(26);
        txtThemMaTK.setColumns(26);

        txtThemMaNV.setEditable(false);
        txtThemMaNV.setText("Tự động sau khi lưu");
        txtThemMaTK.setEditable(false);
        txtThemMaTK.setText("Tự động sau khi lưu");

        NhanVienUiHelper.addRow(form, 0, "Mã nhân viên:", txtThemMaNV);
        NhanVienUiHelper.addRow(form, 1, "Họ và tên:", txtThemTenNV);
        NhanVienUiHelper.addRow(form, 2, "Số điện thoại:", txtThemSdt);
        NhanVienUiHelper.addRow(form, 3, "Địa chỉ:", txtThemDiaChi);
        NhanVienUiHelper.addRow(form, 4, "Ngày sinh (dd/MM/yyyy):", txtThemNgaySinh);
        NhanVienUiHelper.addRow(form, 5, "Ngày vào làm (dd/MM/yyyy):", txtThemNgayVaoLam);

        NhanVienUiHelper.addRow(form, 6, "Mã tài khoản:", txtThemMaTK);

        JButton btnLuu = new JButton("Lưu nhân viên");
        NhanVienUiHelper.styleButton(btnLuu, NhanVienUiHelper.BLUE);
        btnLuu.addActionListener(e -> luuNhanVien());

        JButton btnXoa = new JButton("Xóa trắng");
        NhanVienUiHelper.styleGhostButton(btnXoa);
        btnXoa.addActionListener(e -> xoaTrangThem());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnLuu);
        actions.add(btnXoa);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(12, 6, 6, 6);
        form.add(actions, gbc);

        root.add(form, BorderLayout.CENTER);
        return root;
    }

    private JPanel taoManHinhTraCuu() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Tra cứu nhân viên"), BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(new JLabel("Từ khóa (mã, tên, SĐT):"));
        top.add(txtTraCuu);

        JButton btnTim = new JButton("Tìm kiếm");
        NhanVienUiHelper.styleButton(btnTim, NhanVienUiHelper.BLUE);
        btnTim.addActionListener(e -> taiTraCuuTheoTuKhoa());

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> {
            txtTraCuu.setText("");
            taiTraCuuAsync();
        });

        JButton btnSua = new JButton("Chuyển sang cập nhật");
        NhanVienUiHelper.styleGhostButton(btnSua);
        btnSua.addActionListener(e -> chuyenSangCapNhatTuTraCuu());

        top.add(btnTim);
        top.add(btnTaiLai);
        top.add(btnSua);

        tblTraCuu.setRowSelectionAllowed(true);
        tblTraCuu.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tblTraCuu), BorderLayout.CENTER);
        return root;
    }

    private JPanel taoManHinhCapNhat() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Cập nhật nhân viên"), BorderLayout.NORTH);

        JPanel form = NhanVienUiHelper.createCardPanel();
        form.setBorder(new EmptyBorder(18, 18, 18, 18));

        txtCapNhatMaNV.setColumns(26);
        txtCapNhatTenNV.setColumns(26);
        txtCapNhatSdt.setColumns(26);
        txtCapNhatDiaChi.setColumns(26);
        txtCapNhatMaTK.setColumns(26);

        NhanVienUiHelper.addRow(form, 0, "Mã nhân viên (ưu tiên):", txtCapNhatMaNV);
        NhanVienUiHelper.addRow(form, 1, "Họ và tên:", txtCapNhatTenNV);
        NhanVienUiHelper.addRow(form, 2, "Số điện thoại (dùng để tải nếu không nhập mã):", txtCapNhatSdt);
        NhanVienUiHelper.addRow(form, 3, "Địa chỉ:", txtCapNhatDiaChi);
        NhanVienUiHelper.addRow(form, 4, "Ngày sinh (dd/MM/yyyy):", txtCapNhatNgaySinh);
        NhanVienUiHelper.addRow(form, 5, "Ngày vào làm (dd/MM/yyyy):", txtCapNhatNgayVaoLam);
        NhanVienUiHelper.addRow(form, 6, "Mã tài khoản:", txtCapNhatMaTK);

        JButton btnTai = new JButton("Tải dữ liệu");
        NhanVienUiHelper.styleButton(btnTai, NhanVienUiHelper.BLUE);
        btnTai.addActionListener(e -> taiDuLieuCapNhatAsync());

        JButton btnCapNhat = new JButton("Cập nhật");
        NhanVienUiHelper.styleButton(btnCapNhat, NhanVienUiHelper.TEAL);
        btnCapNhat.addActionListener(e -> capNhatNhanVien());

        JButton btnXoaKhoiCSDL = new JButton("Xóa khỏi CSDL");
        NhanVienUiHelper.styleButton(btnXoaKhoiCSDL, new java.awt.Color(220, 38, 38));
        btnXoaKhoiCSDL.addActionListener(e -> xoaNhanVienKhoiCSDL());

        JButton btnXoa = new JButton("Xóa trắng");
        NhanVienUiHelper.styleGhostButton(btnXoa);
        btnXoa.addActionListener(e -> xoaTrangCapNhat());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnTai);
        actions.add(btnCapNhat);
        actions.add(btnXoaKhoiCSDL);
        actions.add(btnXoa);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(12, 6, 6, 6);
        form.add(actions, gbc);

        root.add(form, BorderLayout.CENTER);
        return root;
    }

    private JPanel taoManHinhPhanQuyen() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Phân quyền tài khoản"), BorderLayout.NORTH);

        tblPhanQuyen.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottom.setOpaque(false);
        bottom.add(new JLabel("Vai trò:"));
        bottom.add(cboRole);
        bottom.add(new JLabel("Trạng thái:"));
        bottom.add(cboTrangThai);

        JButton btnCapNhat = new JButton("Cập nhật phân quyền");
        NhanVienUiHelper.styleButton(btnCapNhat, NhanVienUiHelper.BLUE);
        btnCapNhat.addActionListener(e -> capNhatPhanQuyen());

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> taiPhanQuyenAsync());

        bottom.add(btnCapNhat);
        bottom.add(btnTaiLai);

        root.add(new JScrollPane(tblPhanQuyen), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        return root;
    }

    private JPanel taoManHinhLapHoaDon() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Lập hóa đơn"), BorderLayout.NORTH);

        JPanel form = NhanVienUiHelper.createCardPanel();
        form.setBorder(new EmptyBorder(18, 18, 18, 18));

        txtLapMaHD.setColumns(26);
        txtLapMaNV.setColumns(26);
        txtLapMaKH.setColumns(26);
        txtLapMaThue.setColumns(26);
        txtLapMaKM.setColumns(26);

        NhanVienUiHelper.addRow(form, 0, "Mã hóa đơn:", txtLapMaHD);
        NhanVienUiHelper.addRow(form, 1, "Ngày lập (dd/MM/yyyy):", txtLapNgayLap);
        NhanVienUiHelper.addRow(form, 2, "Mã nhân viên:", txtLapMaNV);
        NhanVienUiHelper.addRow(form, 3, "Mã khách hàng (để trống nếu không có):", txtLapMaKH);
        NhanVienUiHelper.addRow(form, 4, "Mã thuế (để trống nếu không có):", txtLapMaThue);
        NhanVienUiHelper.addRow(form, 5, "Mã khuyến mãi (để trống nếu không có):", txtLapMaKM);

        JButton btnLap = new JButton("Lập hóa đơn");
        NhanVienUiHelper.styleButton(btnLap, NhanVienUiHelper.TEAL);
        btnLap.addActionListener(e -> lapHoaDon());

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> taiHoaDonAsync());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnLap);
        actions.add(btnTaiLai);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(12, 6, 6, 6);
        form.add(actions, gbc);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(new JLabel("Lọc theo mã NV:"));
        top.add(txtLocHoaDonTheoNV);
        JButton btnLoc = new JButton("Lọc");
        NhanVienUiHelper.styleButton(btnLoc, NhanVienUiHelper.BLUE);
        btnLoc.addActionListener(e -> taiHoaDonTheoNhanVien());
        top.add(btnLoc);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(form, BorderLayout.WEST);
        center.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        center.add(top, BorderLayout.NORTH);

        root.add(center, BorderLayout.CENTER);
        return root;
    }

    private JPanel taoManHinhHoaDonDaLap() {
        JPanel root = taoCardNen();
        root.add(NhanVienUiHelper.createTitle("Hóa đơn đã lập"), BorderLayout.NORTH);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(new JLabel("Lọc theo mã NV:"));
        top.add(txtLocHoaDonTheoNV);

        JButton btnLoc = new JButton("Lọc");
        NhanVienUiHelper.styleButton(btnLoc, NhanVienUiHelper.BLUE);
        btnLoc.addActionListener(e -> taiHoaDonTheoNhanVien());

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> taiHoaDonAsync());

        top.add(btnLoc);
        top.add(btnTaiLai);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        return root;
    }

    private void luuNhanVien() {
        try {
            String tenNV = txtThemTenNV.getText().trim();
            LocalDate ngaySinh = NhanVienUiHelper.getDatePickerValue(txtThemNgaySinh);
            LocalDate ngayVaoLam = NhanVienUiHelper.getDatePickerValue(txtThemNgayVaoLam);
            if (NhanVienUiHelper.isBlank(tenNV)) {
                JOptionPane.showMessageDialog(this, "Họ và tên không được để trống.");
                return;
            }

                NhanVienDAO.KetQuaTaoNhanVien ketQua = nhanVienDAO.themNhanVienTuDong(
                    tenNV,
                    txtThemSdt.getText().trim(),
                    txtThemDiaChi.getText().trim(),
                    ngaySinh,
                    ngayVaoLam
                );

            if (ketQua != null) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đã thêm nhân viên thành công.\nMã NV: " + ketQua.getMaNV() + "\nMã TK: " + ketQua.getMaTK() + "\nUsername: " + ketQua.getUsername() + "\nMật khẩu mặc định: 123456"
                );
                xoaTrangThem();
                txtThemMaNV.setText("Tự động sau khi lưu");
                txtThemMaTK.setText("Tự động sau khi lưu");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void taiTraCuuTheoTuKhoa() {
        taiTraCuu(new SwingWorker<List<NhanVien>, Void>() {
            @Override
            protected List<NhanVien> doInBackground() {
                return nhanVienDAO.timTheoTuKhoa(txtTraCuu.getText());
            }
        });
    }

    private void taiTraCuuAsync() {
        taiTraCuu(new SwingWorker<List<NhanVien>, Void>() {
            @Override
            protected List<NhanVien> doInBackground() {
                return nhanVienDAO.timTatCa();
            }
        });
    }

    private void taiTraCuu(SwingWorker<List<NhanVien>, Void> worker) {
        worker.addPropertyChangeListener(evt -> {
            if ("state".equals(evt.getPropertyName()) && SwingWorker.StateValue.DONE == evt.getNewValue()) {
                try {
                    napBangNhanVien(modelTraCuu, worker.get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(this, "Không thể tải danh sách nhân viên: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(this, "Không thể tải danh sách nhân viên: " + ex.getMessage());
                }
            }
        });
        worker.execute();
    }

    private void chuyenSangCapNhatTuTraCuu() {
        int row = tblTraCuu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.");
            return;
        }
        String maNV = String.valueOf(modelTraCuu.getValueAt(row, 0));
        txtCapNhatMaNV.setText(maNV);
        showCapNhatNhanVien();
        taiDuLieuCapNhatAsync();
    }

    private void taiDuLieuCapNhatAsync() {
        final String maNV = txtCapNhatMaNV.getText().trim();
        final String sdt = txtCapNhatSdt.getText().trim();
        if (NhanVienUiHelper.isBlank(maNV) && NhanVienUiHelper.isBlank(sdt)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên hoặc số điện thoại.");
            return;
        }

        new SwingWorker<NhanVien, Void>() {
            @Override
            protected NhanVien doInBackground() {
                if (!NhanVienUiHelper.isBlank(maNV)) {
                    return nhanVienDAO.timTheoMa(maNV);
                }

                List<NhanVien> danhSach = nhanVienDAO.timTheoSoDienThoai(sdt);
                if (danhSach.isEmpty()) {
                    return null;
                }
                if (danhSach.size() > 1) {
                    throw new IllegalStateException("Có nhiều nhân viên trùng số điện thoại. Vui lòng nhập mã nhân viên để tải chính xác.");
                }
                return danhSach.get(0);
            }

            @Override
            protected void done() {
                try {
                    NhanVien nv = get();
                    if (nv == null) {
                        JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không tìm thấy nhân viên.");
                        return;
                    }
                    txtCapNhatMaNV.setText(NhanVienUiHelper.textOf(nv.getMaNV()));
                    txtCapNhatTenNV.setText(NhanVienUiHelper.textOf(nv.getTenNV()));
                    txtCapNhatSdt.setText(NhanVienUiHelper.textOf(nv.getSdt()));
                    txtCapNhatDiaChi.setText(NhanVienUiHelper.textOf(nv.getDiaChi()));
                    NhanVienUiHelper.setDatePickerValue(txtCapNhatNgaySinh, nv.getNgaySinh());
                    NhanVienUiHelper.setDatePickerValue(txtCapNhatNgayVaoLam, nv.getNgayVaoLam());
                    txtCapNhatMaTK.setText(nv.getMaTK() != null ? String.valueOf(nv.getMaTK()) : "");
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải dữ liệu nhân viên: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    Throwable cause = ex.getCause();
                    String message = (cause != null && cause.getMessage() != null) ? cause.getMessage() : ex.getMessage();
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải dữ liệu nhân viên: " + message);
                }
            }
        }.execute();
    }

    private void xoaNhanVienKhoiCSDL() {
        String maNV = txtCapNhatMaNV.getText().trim();
        if (NhanVienUiHelper.isBlank(maNV)) {
            JOptionPane.showMessageDialog(this, "Vui lòng tải nhân viên trước khi xóa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc muốn xóa nhân viên " + maNV + " khỏi CSDL?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean thanhCong = nhanVienDAO.xoaNhanVien(maNV);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã xóa nhân viên khỏi CSDL.");
                xoaTrangCapNhat();
                taiTraCuuAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên.");
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void capNhatNhanVien() {
        try {
            String maNV = txtCapNhatMaNV.getText().trim();
            String tenNV = txtCapNhatTenNV.getText().trim();
            LocalDate ngaySinh = NhanVienUiHelper.getDatePickerValue(txtCapNhatNgaySinh);
            LocalDate ngayVaoLam = NhanVienUiHelper.getDatePickerValue(txtCapNhatNgayVaoLam);
            Integer maTK = NhanVienUiHelper.parseIntegerNullable(txtCapNhatMaTK.getText(), "Mã tài khoản");

            if (NhanVienUiHelper.isBlank(maNV) || NhanVienUiHelper.isBlank(tenNV)) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên và họ tên không được để trống.");
                return;
            }

            boolean thanhCong = nhanVienDAO.capNhatNhanVien(new NhanVien(
                    maNV,
                    tenNV,
                    txtCapNhatSdt.getText().trim(),
                    txtCapNhatDiaChi.getText().trim(),
                    ngaySinh,
                    ngayVaoLam,
                    maTK
            ));

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật nhân viên.");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật nhân viên.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void taiPhanQuyenAsync() {
        new SwingWorker<List<TaiKhoan>, Void>() {
            @Override
            protected List<TaiKhoan> doInBackground() {
                return taiKhoanDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    napBangTaiKhoan(get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải danh sách tài khoản: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải danh sách tài khoản: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void napBangTaiKhoan(List<TaiKhoan> danhSach) {
        modelPhanQuyen.setRowCount(0);
        for (TaiKhoan tk : danhSach) {
            modelPhanQuyen.addRow(new Object[] {
                    tk.getMaTK(),
                    tk.getUsername(),
                    tk.getRole(),
                    tk.getTrangThai() != null && tk.getTrangThai() ? "Hoạt động" : "Khóa"
            });
        }
    }

    private void capNhatPhanQuyen() {
        int row = tblPhanQuyen.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản.");
            return;
        }

        int maTK = Integer.parseInt(String.valueOf(modelPhanQuyen.getValueAt(row, 0)));
        String role = "Quản lý".equals(String.valueOf(cboRole.getSelectedItem())) ? "QUAN_LY" : "NHAN_VIEN";
        boolean trangThai = "Hoạt động".equals(String.valueOf(cboTrangThai.getSelectedItem()));

        try {
            boolean thanhCong = taiKhoanDAO.capNhatPhanQuyen(maTK, role, trangThai);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật phân quyền.");
                taiPhanQuyenAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật tài khoản.");
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void lapHoaDon() {
        try {
            String maHD = txtLapMaHD.getText().trim();
            String maNV = txtLapMaNV.getText().trim();
            LocalDate ngayLap = NhanVienUiHelper.getDatePickerValue(txtLapNgayLap);

            if (NhanVienUiHelper.isBlank(maHD) || NhanVienUiHelper.isBlank(maNV) || ngayLap == null) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn, ngày lập và mã nhân viên không được để trống.");
                return;
            }

            boolean thanhCong = hoaDonDAO.themHoaDon(new HoaDon(
                    maHD,
                    ngayLap,
                    maNV,
                    txtLapMaKH.getText().trim(),
                    txtLapMaThue.getText().trim(),
                    txtLapMaKM.getText().trim()
            ));

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã lập hóa đơn thành công.");
                taiHoaDonAsync();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể lập hóa đơn.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void taiHoaDonTheoNhanVien() {
        new SwingWorker<List<HoaDon>, Void>() {
            @Override
            protected List<HoaDon> doInBackground() {
                return hoaDonDAO.timTheoMaNhanVien(txtLocHoaDonTheoNV.getText());
            }

            @Override
            protected void done() {
                try {
                    napBangHoaDon(get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải danh sách hóa đơn: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải danh sách hóa đơn: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void taiHoaDonAsync() {
        new SwingWorker<List<HoaDon>, Void>() {
            @Override
            protected List<HoaDon> doInBackground() {
                return hoaDonDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    napBangHoaDon(get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải danh sách hóa đơn: " + ex.getMessage());
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(NhanVienWorkspacePanel.this, "Không thể tải danh sách hóa đơn: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private void napBangHoaDon(List<HoaDon> danhSach) {
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : danhSach) {
            modelHoaDon.addRow(new Object[] {
                    hd.getMaHD(),
                    NhanVienUiHelper.formatDate(hd.getNgayLap()),
                    hd.getMaNV(),
                    hd.getMaKH(),
                    hd.getMaThue(),
                    hd.getMaKM()
            });
        }
    }

    private void napBangNhanVien(DefaultTableModel model, List<NhanVien> danhSach) {
        model.setRowCount(0);
        for (NhanVien nv : danhSach) {
            model.addRow(new Object[] {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getDiaChi(),
                    NhanVienUiHelper.formatDate(nv.getNgaySinh()),
                    NhanVienUiHelper.formatDate(nv.getNgayVaoLam()),
                    nv.getMaTK()
            });
        }
    }

    private void xoaTrangThem() {
        txtThemMaNV.setText("");
        txtThemTenNV.setText("");
        txtThemSdt.setText("");
        txtThemDiaChi.setText("");
        NhanVienUiHelper.setDatePickerValue(txtThemNgaySinh, null);
        NhanVienUiHelper.setDatePickerValue(txtThemNgayVaoLam, null);
        txtThemMaNV.setText("Tự động sau khi lưu");
        txtThemMaTK.setText("Tự động sau khi lưu");
    }

    private void xoaTrangCapNhat() {
        txtCapNhatMaNV.setText("");
        txtCapNhatTenNV.setText("");
        txtCapNhatSdt.setText("");
        txtCapNhatDiaChi.setText("");
        NhanVienUiHelper.setDatePickerValue(txtCapNhatNgaySinh, null);
        NhanVienUiHelper.setDatePickerValue(txtCapNhatNgayVaoLam, null);
        txtCapNhatMaTK.setText("");
    }
}