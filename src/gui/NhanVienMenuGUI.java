package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import dao.HoaDonDAO;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import entity.HoaDon;
import entity.NhanVien;
import entity.TaiKhoan;

public class NhanVienMenuGUI extends JDialog {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private final NhanVienDAO nhanVienDAO;
    private final TaiKhoanDAO taiKhoanDAO;
    private final HoaDonDAO hoaDonDAO;

    private final JTabbedPane tabbedPane;

    private JTextField txtThemMaNV;
    private JTextField txtThemTenNV;
    private JTextField txtThemSdt;
    private JTextField txtThemDiaChi;
    private JTextField txtThemNgaySinh;
    private JTextField txtThemNgayVaoLam;
    private JTextField txtThemMaTK;

    private JTextField txtTraCuu;
    private DefaultTableModel modelNhanVien;
    private JTable tblNhanVien;

    private JTextField txtCapNhatMaNV;
    private JTextField txtCapNhatTenNV;
    private JTextField txtCapNhatSdt;
    private JTextField txtCapNhatDiaChi;
    private JTextField txtCapNhatNgaySinh;
    private JTextField txtCapNhatNgayVaoLam;
    private JTextField txtCapNhatMaTK;

    private DefaultTableModel modelPhanQuyen;
    private JTable tblPhanQuyen;
    private JComboBox<String> cboRole;
    private JComboBox<String> cboTrangThai;

    private JTextField txtLapMaHD;
    private JTextField txtLapNgayLap;
    private JTextField txtLapMaNV;
    private JTextField txtLapMaKH;
    private JTextField txtLapMaThue;
    private JTextField txtLapMaKM;

    private JTextField txtLocHoaDonTheoNV;
    private DefaultTableModel modelHoaDon;

    public NhanVienMenuGUI(Frame owner, int selectedTabIndex) {
        super(owner, "Menu Nhan Vien", true);
        this.nhanVienDAO = new NhanVienDAO();
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.hoaDonDAO = new HoaDonDAO();
        this.tabbedPane = new JTabbedPane();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1100, 680);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(owner);

        initUI();

        int index = Math.max(0, Math.min(selectedTabIndex, tabbedPane.getTabCount() - 1));
        tabbedPane.setSelectedIndex(index);

        napDuLieuNhanVien();
        napDuLieuPhanQuyen();
        napDuLieuHoaDonTatCa();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.setBackground(new Color(248, 250, 252));

        tabbedPane.addTab("Them nhan vien", taoTabThemNhanVien());
        tabbedPane.addTab("Tra cuu nhan vien", taoTabTraCuuNhanVien());
        tabbedPane.addTab("Cap nhat nhan vien", taoTabCapNhatNhanVien());
        tabbedPane.addTab("Phan quyen tai khoan", taoTabPhanQuyenTaiKhoan());
        tabbedPane.addTab("Lap hoa don", taoTabLapHoaDon());
        tabbedPane.addTab("Hoa don da lap", taoTabHoaDonDaLap());

        root.add(tabbedPane, BorderLayout.CENTER);

        JButton btnDong = new JButton("Dong");
        btnDong.addActionListener(e -> dispose());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(btnDong);
        root.add(south, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel taoTabThemNhanVien() {
        JPanel panel = taoFormPanel();

        txtThemMaNV = new JTextField();
        txtThemTenNV = new JTextField();
        txtThemSdt = new JTextField();
        txtThemDiaChi = new JTextField();
        txtThemNgaySinh = new JTextField();
        txtThemNgayVaoLam = new JTextField();
        txtThemMaTK = new JTextField();

        addFormRow(panel, 0, "Ma NV:", txtThemMaNV);
        addFormRow(panel, 1, "Ten NV:", txtThemTenNV);
        addFormRow(panel, 2, "So dien thoai:", txtThemSdt);
        addFormRow(panel, 3, "Dia chi:", txtThemDiaChi);
        addFormRow(panel, 4, "Ngay sinh (yyyy-MM-dd):", txtThemNgaySinh);
        addFormRow(panel, 5, "Ngay vao lam (yyyy-MM-dd):", txtThemNgayVaoLam);
        addFormRow(panel, 6, "Ma tai khoan (so):", txtThemMaTK);

        JButton btnThem = new JButton("Them nhan vien");
        btnThem.addActionListener(e -> xuLyThemNhanVien());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnThem, gbc);

        return panel;
    }

    private JPanel taoTabTraCuuNhanVien() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtTraCuu = new JTextField(26);
        JButton btnTraCuu = new JButton("Tra cuu");
        JButton btnTaiLai = new JButton("Tai lai");

        btnTraCuu.addActionListener(e -> traCuuNhanVien());
        btnTaiLai.addActionListener(e -> napDuLieuNhanVien());

        top.add(new JLabel("Tu khoa (ma/ten/sdt):"));
        top.add(txtTraCuu);
        top.add(btnTraCuu);
        top.add(btnTaiLai);

        panel.add(top, BorderLayout.NORTH);

        modelNhanVien = new DefaultTableModel(new Object[] {
                "Ma NV", "Ten NV", "SDT", "Dia chi", "Ngay sinh", "Ngay vao lam", "Ma TK"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                doDuLieuTuBangSangFormCapNhat();
            }
        });

        panel.add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);
        return panel;
    }

    private JPanel taoTabCapNhatNhanVien() {
        JPanel panel = taoFormPanel();

        txtCapNhatMaNV = new JTextField();
        txtCapNhatTenNV = new JTextField();
        txtCapNhatSdt = new JTextField();
        txtCapNhatDiaChi = new JTextField();
        txtCapNhatNgaySinh = new JTextField();
        txtCapNhatNgayVaoLam = new JTextField();
        txtCapNhatMaTK = new JTextField();

        txtCapNhatMaNV.setEditable(false);

        addFormRow(panel, 0, "Ma NV:", txtCapNhatMaNV);
        addFormRow(panel, 1, "Ten NV:", txtCapNhatTenNV);
        addFormRow(panel, 2, "So dien thoai:", txtCapNhatSdt);
        addFormRow(panel, 3, "Dia chi:", txtCapNhatDiaChi);
        addFormRow(panel, 4, "Ngay sinh (yyyy-MM-dd):", txtCapNhatNgaySinh);
        addFormRow(panel, 5, "Ngay vao lam (yyyy-MM-dd):", txtCapNhatNgayVaoLam);
        addFormRow(panel, 6, "Ma tai khoan (so):", txtCapNhatMaTK);

        JButton btnCapNhat = new JButton("Cap nhat");
        btnCapNhat.addActionListener(e -> xuLyCapNhatNhanVien());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnCapNhat, gbc);

        JLabel huongDan = new JLabel("Meo: chon 1 dong trong tab Tra cuu nhan vien de nap du lieu cap nhat.");
        gbc.gridy = 8;
        panel.add(huongDan, gbc);

        return panel;
    }

    private JPanel taoTabPhanQuyenTaiKhoan() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        modelPhanQuyen = new DefaultTableModel(new Object[] {
                "Ma TK", "Username", "Role", "Trang thai"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblPhanQuyen = new JTable(modelPhanQuyen);
        tblPhanQuyen.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cboRole = new JComboBox<>(new String[] { "NHAN_VIEN", "QUAN_LY" });
        cboTrangThai = new JComboBox<>(new String[] { "Hoat dong", "Khoa" });
        JButton btnCapNhat = new JButton("Cap nhat phan quyen");
        btnCapNhat.addActionListener(e -> xuLyCapNhatPhanQuyen());

        bottom.add(new JLabel("Role:"));
        bottom.add(cboRole);
        bottom.add(new JLabel("Trang thai:"));
        bottom.add(cboTrangThai);
        bottom.add(btnCapNhat);

        panel.add(new JScrollPane(tblPhanQuyen), BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel taoTabLapHoaDon() {
        JPanel panel = taoFormPanel();

        txtLapMaHD = new JTextField();
        txtLapNgayLap = new JTextField();
        txtLapMaNV = new JTextField();
        txtLapMaKH = new JTextField();
        txtLapMaThue = new JTextField();
        txtLapMaKM = new JTextField();

        txtLapNgayLap.setText(LocalDate.now().toString());

        addFormRow(panel, 0, "Ma hoa don:", txtLapMaHD);
        addFormRow(panel, 1, "Ngay lap (yyyy-MM-dd):", txtLapNgayLap);
        addFormRow(panel, 2, "Ma NV:", txtLapMaNV);
        addFormRow(panel, 3, "Ma KH (de trong neu vang lai):", txtLapMaKH);
        addFormRow(panel, 4, "Ma thue (de trong neu khong ap dung):", txtLapMaThue);
        addFormRow(panel, 5, "Ma khuyen mai (de trong neu khong ap dung):", txtLapMaKM);

        JButton btnLapHoaDon = new JButton("Lap hoa don");
        btnLapHoaDon.addActionListener(e -> xuLyLapHoaDon());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(btnLapHoaDon, gbc);

        return panel;
    }

    private JPanel taoTabHoaDonDaLap() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtLocHoaDonTheoNV = new JTextField(20);
        JButton btnLoc = new JButton("Loc theo ma NV");
        JButton btnTaiLai = new JButton("Tai lai");

        btnLoc.addActionListener(e -> locHoaDonTheoNhanVien());
        btnTaiLai.addActionListener(e -> napDuLieuHoaDonTatCa());

        top.add(new JLabel("Ma NV:"));
        top.add(txtLocHoaDonTheoNV);
        top.add(btnLoc);
        top.add(btnTaiLai);

        modelHoaDon = new DefaultTableModel(new Object[] {
                "Ma HD", "Ngay lap", "Ma NV", "Ma KH", "Ma thue", "Ma KM"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tblHoaDon = new JTable(modelHoaDon);

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);
        return panel;
    }

    private JPanel taoFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private void addFormRow(JPanel panel, int row, String label, JTextField textField) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        panel.add(textField, gbc);
    }

    private void napDuLieuNhanVien() {
        List<NhanVien> ds = nhanVienDAO.timTatCa();
        modelNhanVien.setRowCount(0);
        for (NhanVien nv : ds) {
            modelNhanVien.addRow(new Object[] {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getDiaChi(),
                    nv.getNgaySinh(),
                    nv.getNgayVaoLam(),
                    nv.getMaTK()
            });
        }
    }

    private void traCuuNhanVien() {
        List<NhanVien> ds = nhanVienDAO.timTheoTuKhoa(txtTraCuu.getText());
        modelNhanVien.setRowCount(0);
        for (NhanVien nv : ds) {
            modelNhanVien.addRow(new Object[] {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getDiaChi(),
                    nv.getNgaySinh(),
                    nv.getNgayVaoLam(),
                    nv.getMaTK()
            });
        }
    }

    private void doDuLieuTuBangSangFormCapNhat() {
        int row = tblNhanVien.getSelectedRow();
        if (row < 0) {
            return;
        }

        txtCapNhatMaNV.setText(String.valueOf(modelNhanVien.getValueAt(row, 0)));
        txtCapNhatTenNV.setText(toText(modelNhanVien.getValueAt(row, 1)));
        txtCapNhatSdt.setText(toText(modelNhanVien.getValueAt(row, 2)));
        txtCapNhatDiaChi.setText(toText(modelNhanVien.getValueAt(row, 3)));
        txtCapNhatNgaySinh.setText(toText(modelNhanVien.getValueAt(row, 4)));
        txtCapNhatNgayVaoLam.setText(toText(modelNhanVien.getValueAt(row, 5)));
        txtCapNhatMaTK.setText(toText(modelNhanVien.getValueAt(row, 6)));
    }

    private void xuLyThemNhanVien() {
        try {
            NhanVien nv = taoNhanVienTuFormThem();

            if (nhanVienDAO.tonTaiMaNhanVien(nv.getMaNV())) {
                JOptionPane.showMessageDialog(this, "Ma nhan vien da ton tai.");
                return;
            }

            boolean thanhCong = nhanVienDAO.themNhanVien(nv);
            if (!thanhCong) {
                JOptionPane.showMessageDialog(this, "Khong the them nhan vien.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Them nhan vien thanh cong.");
            xoaFormThemNhanVien();
            napDuLieuNhanVien();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Loi CSDL: " + ex.getMessage());
        }
    }

    private void xuLyCapNhatNhanVien() {
        try {
            NhanVien nv = taoNhanVienTuFormCapNhat();
            if (isBlank(nv.getMaNV())) {
                JOptionPane.showMessageDialog(this, "Vui long chon nhan vien tu tab tra cuu.");
                return;
            }

            boolean thanhCong = nhanVienDAO.capNhatNhanVien(nv);
            if (!thanhCong) {
                JOptionPane.showMessageDialog(this, "Khong the cap nhat nhan vien.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Cap nhat nhan vien thanh cong.");
            napDuLieuNhanVien();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Loi CSDL: " + ex.getMessage());
        }
    }

    private void napDuLieuPhanQuyen() {
        List<TaiKhoan> ds = taiKhoanDAO.timTatCa();
        modelPhanQuyen.setRowCount(0);
        for (TaiKhoan tk : ds) {
            modelPhanQuyen.addRow(new Object[] {
                    tk.getMaTK(),
                    tk.getUsername(),
                    tk.getRole(),
                    tk.getTrangThai() != null && tk.getTrangThai() ? "Hoat dong" : "Khoa"
            });
        }
    }

    private void xuLyCapNhatPhanQuyen() {
        int row = tblPhanQuyen.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui long chon tai khoan can cap nhat.");
            return;
        }

        int maTK = Integer.parseInt(String.valueOf(modelPhanQuyen.getValueAt(row, 0)));
        String role = String.valueOf(cboRole.getSelectedItem());
        boolean trangThai = "Hoat dong".equals(String.valueOf(cboTrangThai.getSelectedItem()));

        try {
            boolean thanhCong = taiKhoanDAO.capNhatPhanQuyen(maTK, role, trangThai);
            if (!thanhCong) {
                JOptionPane.showMessageDialog(this, "Khong the cap nhat tai khoan.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Cap nhat phan quyen thanh cong.");
            napDuLieuPhanQuyen();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Loi CSDL: " + ex.getMessage());
        }
    }

    private void xuLyLapHoaDon() {
        try {
            HoaDon hoaDon = taoHoaDonTuForm();
            boolean thanhCong = hoaDonDAO.themHoaDon(hoaDon);
            if (!thanhCong) {
                JOptionPane.showMessageDialog(this, "Khong the lap hoa don.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Lap hoa don thanh cong.");
            txtLapMaHD.setText("");
            txtLapMaKH.setText("");
            txtLapMaThue.setText("");
            txtLapMaKM.setText("");
            napDuLieuHoaDonTatCa();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Loi CSDL: " + ex.getMessage());
        }
    }

    private void napDuLieuHoaDonTatCa() {
        napBangHoaDon(hoaDonDAO.timTatCa());
    }

    private void locHoaDonTheoNhanVien() {
        napBangHoaDon(hoaDonDAO.timTheoMaNhanVien(txtLocHoaDonTheoNV.getText()));
    }

    private void napBangHoaDon(List<HoaDon> dsHoaDon) {
        modelHoaDon.setRowCount(0);
        for (HoaDon hd : dsHoaDon) {
            modelHoaDon.addRow(new Object[] {
                    hd.getMaHD(),
                    hd.getNgayLap(),
                    hd.getMaNV(),
                    hd.getMaKH(),
                    hd.getMaThue(),
                    hd.getMaKM()
            });
        }
    }

    private NhanVien taoNhanVienTuFormThem() {
        String maNV = txtThemMaNV.getText().trim();
        String tenNV = txtThemTenNV.getText().trim();
        String sdt = txtThemSdt.getText().trim();
        String diaChi = txtThemDiaChi.getText().trim();
        LocalDate ngaySinh = parseDate(txtThemNgaySinh.getText().trim(), "Ngay sinh");
        LocalDate ngayVaoLam = parseDate(txtThemNgayVaoLam.getText().trim(), "Ngay vao lam");
        Integer maTK = parseIntegerNullable(txtThemMaTK.getText().trim(), "Ma tai khoan");

        validateNhanVien(maNV, tenNV, ngaySinh, ngayVaoLam);

        return new NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK);
    }

    private NhanVien taoNhanVienTuFormCapNhat() {
        String maNV = txtCapNhatMaNV.getText().trim();
        String tenNV = txtCapNhatTenNV.getText().trim();
        String sdt = txtCapNhatSdt.getText().trim();
        String diaChi = txtCapNhatDiaChi.getText().trim();
        LocalDate ngaySinh = parseDate(txtCapNhatNgaySinh.getText().trim(), "Ngay sinh");
        LocalDate ngayVaoLam = parseDate(txtCapNhatNgayVaoLam.getText().trim(), "Ngay vao lam");
        Integer maTK = parseIntegerNullable(txtCapNhatMaTK.getText().trim(), "Ma tai khoan");

        validateNhanVien(maNV, tenNV, ngaySinh, ngayVaoLam);

        return new NhanVien(maNV, tenNV, sdt, diaChi, ngaySinh, ngayVaoLam, maTK);
    }

    private HoaDon taoHoaDonTuForm() {
        String maHD = txtLapMaHD.getText().trim();
        String maNV = txtLapMaNV.getText().trim();
        LocalDate ngayLap = parseDate(txtLapNgayLap.getText().trim(), "Ngay lap");

        if (isBlank(maHD) || isBlank(maNV) || ngayLap == null) {
            throw new IllegalArgumentException("Ma hoa don, ngay lap, ma NV khong duoc de trong.");
        }

        return new HoaDon(
                maHD,
                ngayLap,
                maNV,
                txtLapMaKH.getText().trim(),
                txtLapMaThue.getText().trim(),
                txtLapMaKM.getText().trim()
        );
    }

    private void validateNhanVien(String maNV, String tenNV, LocalDate ngaySinh, LocalDate ngayVaoLam) {
        if (isBlank(maNV)) {
            throw new IllegalArgumentException("Ma NV khong duoc de trong.");
        }
        if (isBlank(tenNV)) {
            throw new IllegalArgumentException("Ten NV khong duoc de trong.");
        }
        if (ngaySinh == null || ngayVaoLam == null) {
            throw new IllegalArgumentException("Ngay sinh va ngay vao lam phai dung dinh dang " + DATE_FORMAT + ".");
        }
    }

    private LocalDate parseDate(String text, String fieldName) {
        if (isBlank(text)) {
            return null;
        }
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(fieldName + " phai co dinh dang " + DATE_FORMAT + ".");
        }
    }

    private Integer parseIntegerNullable(String text, String fieldName) {
        if (isBlank(text)) {
            return null;
        }
        try {
            return Integer.valueOf(text);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " phai la so nguyen.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String toText(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    private void xoaFormThemNhanVien() {
        txtThemMaNV.setText("");
        txtThemTenNV.setText("");
        txtThemSdt.setText("");
        txtThemDiaChi.setText("");
        txtThemNgaySinh.setText("");
        txtThemNgayVaoLam.setText("");
        txtThemMaTK.setText("");
    }
}
