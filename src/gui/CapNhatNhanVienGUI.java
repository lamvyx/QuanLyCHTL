package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import dao.NhanVienDAO;
import entity.NhanVien;

public class CapNhatNhanVienGUI extends JFrame {
    private final NhanVienDAO nhanVienDAO;

    private final JTextField txtMaNV = new JTextField();
    private final JTextField txtTenNV = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final DatePickerField txtNgaySinh = NhanVienUiHelper.createDatePicker();
    private final DatePickerField txtNgayVaoLam = NhanVienUiHelper.createDatePicker();
    private final JTextField txtMaTK = new JTextField();

    public CapNhatNhanVienGUI() {
        this(null);
    }

    public CapNhatNhanVienGUI(String maNVCanTai) {
        this.nhanVienDAO = new NhanVienDAO();
    }

    public static void moCuaSo() {
        moCuaSo(null);
    }

    public static void moCuaSo(String maNVCanTai) {
        CapNhatNhanVienGUI gui = new CapNhatNhanVienGUI(maNVCanTai);
        gui.initUI();
        gui.setTitle("Cập nhật nhân viên");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(760, 540);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        if (!NhanVienUiHelper.isBlank(maNVCanTai)) {
            gui.txtMaNV.setText(maNVCanTai);
            gui.taiDuLieuAsync();
        }
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(new javax.swing.border.EmptyBorder(14, 14, 14, 14));

        JPanel card = NhanVienUiHelper.createCardPanel();

        txtMaNV.setColumns(24);
        txtTenNV.setColumns(24);
        txtSdt.setColumns(24);
        txtDiaChi.setColumns(24);
        txtMaTK.setColumns(24);

        NhanVienUiHelper.addRow(card, 0, "Mã nhân viên:", txtMaNV);
        NhanVienUiHelper.addRow(card, 1, "Họ và tên:", txtTenNV);
        NhanVienUiHelper.addRow(card, 2, "Số điện thoại:", txtSdt);
        NhanVienUiHelper.addRow(card, 3, "Địa chỉ:", txtDiaChi);
        NhanVienUiHelper.addRow(card, 4, "Ngày sinh (dd/MM/yyyy):", txtNgaySinh);
        NhanVienUiHelper.addRow(card, 5, "Ngày vào làm (dd/MM/yyyy):", txtNgayVaoLam);
        NhanVienUiHelper.addRow(card, 6, "Mã tài khoản:", txtMaTK);

        JButton btnTai = new JButton("Tải dữ liệu");
        NhanVienUiHelper.styleButton(btnTai, NhanVienUiHelper.BLUE);
        btnTai.addActionListener(e -> taiDuLieu());

        JButton btnCapNhat = new JButton("Cập nhật");
        NhanVienUiHelper.styleButton(btnCapNhat, NhanVienUiHelper.TEAL);
        btnCapNhat.addActionListener(e -> capNhat());

        JButton btnDong = new JButton("Đóng");
        NhanVienUiHelper.styleGhostButton(btnDong);
        btnDong.addActionListener(e -> dispose());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnTai);
        actions.add(btnCapNhat);
        actions.add(btnDong);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(12, 6, 6, 6);
        card.add(actions, gbc);

        root.add(NhanVienUiHelper.createTitle("Cập nhật thông tin nhân viên"), BorderLayout.NORTH);
        root.add(card, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void taiDuLieu() {
        String maNV = txtMaNV.getText().trim();
        if (NhanVienUiHelper.isBlank(maNV)) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên.");
            return;
        }

        NhanVien nhanVien = nhanVienDAO.timTheoMa(maNV);
        if (nhanVien == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên.");
            return;
        }

        txtTenNV.setText(NhanVienUiHelper.textOf(nhanVien.getTenNV()));
        txtSdt.setText(NhanVienUiHelper.textOf(nhanVien.getSdt()));
        txtDiaChi.setText(NhanVienUiHelper.textOf(nhanVien.getDiaChi()));
        NhanVienUiHelper.setDatePickerValue(txtNgaySinh, nhanVien.getNgaySinh());
        NhanVienUiHelper.setDatePickerValue(txtNgayVaoLam, nhanVien.getNgayVaoLam());
        txtMaTK.setText(nhanVien.getMaTK() != null ? String.valueOf(nhanVien.getMaTK()) : "");
    }

    private void taiDuLieuAsync() {
        new SwingWorker<NhanVien, Void>() {
            @Override
            protected NhanVien doInBackground() {
                return nhanVienDAO.timTheoMa(txtMaNV.getText().trim());
            }

            @Override
            protected void done() {
                try {
                    NhanVien nhanVien = get();
                    if (nhanVien == null) {
                        JOptionPane.showMessageDialog(CapNhatNhanVienGUI.this, "Không tìm thấy nhân viên.");
                        return;
                    }

                    txtTenNV.setText(NhanVienUiHelper.textOf(nhanVien.getTenNV()));
                    txtSdt.setText(NhanVienUiHelper.textOf(nhanVien.getSdt()));
                    txtDiaChi.setText(NhanVienUiHelper.textOf(nhanVien.getDiaChi()));
                    NhanVienUiHelper.setDatePickerValue(txtNgaySinh, nhanVien.getNgaySinh());
                    NhanVienUiHelper.setDatePickerValue(txtNgayVaoLam, nhanVien.getNgayVaoLam());
                    txtMaTK.setText(nhanVien.getMaTK() != null ? String.valueOf(nhanVien.getMaTK()) : "");
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(
                            CapNhatNhanVienGUI.this,
                            "Không thể tải dữ liệu nhân viên: " + ex.getMessage()
                    );
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(
                            CapNhatNhanVienGUI.this,
                            "Không thể tải dữ liệu nhân viên: " + ex.getMessage()
                    );
                }
            }
        }.execute();
    }

    private void capNhat() {
        try {
            String maNV = txtMaNV.getText().trim();
            String tenNV = txtTenNV.getText().trim();
            LocalDate ngaySinh = NhanVienUiHelper.getDatePickerValue(txtNgaySinh);
            LocalDate ngayVaoLam = NhanVienUiHelper.getDatePickerValue(txtNgayVaoLam);
            Integer maTK = NhanVienUiHelper.parseIntegerNullable(txtMaTK.getText(), "Mã tài khoản");

            if (NhanVienUiHelper.isBlank(maNV) || NhanVienUiHelper.isBlank(tenNV)) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên và họ tên không được để trống.");
                return;
            }

            boolean thanhCong = nhanVienDAO.capNhatNhanVien(new NhanVien(
                    maNV,
                    tenNV,
                    txtSdt.getText().trim(),
                    txtDiaChi.getText().trim(),
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
}