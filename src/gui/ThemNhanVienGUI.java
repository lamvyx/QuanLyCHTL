package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.NhanVienDAO;
import entity.NhanVien;

public class ThemNhanVienGUI extends JFrame {
    private final NhanVienDAO nhanVienDAO;

    private final JTextField txtMaNV = new JTextField();
    private final JTextField txtTenNV = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final DatePickerField txtNgaySinh = NhanVienUiHelper.createDatePicker();
    private final DatePickerField txtNgayVaoLam = NhanVienUiHelper.createDatePicker();
    private final JTextField txtMaTK = new JTextField();

    public ThemNhanVienGUI() {
        this.nhanVienDAO = new NhanVienDAO();
    }

    public static void moCuaSo() {
        ThemNhanVienGUI gui = new ThemNhanVienGUI();
        gui.initUI();
        gui.setTitle("Thêm nhân viên");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(720, 520);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(new EmptyBorder(14, 14, 14, 14));

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

        JButton btnLuu = new JButton("Lưu nhân viên");
        NhanVienUiHelper.styleButton(btnLuu, NhanVienUiHelper.BLUE);
        btnLuu.addActionListener(e -> luuNhanVien());

        JButton btnXoa = new JButton("Xóa trắng");
        NhanVienUiHelper.styleGhostButton(btnXoa);
        btnXoa.addActionListener(e -> xoaTrang());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        actions.setOpaque(false);
        actions.add(btnLuu);
        actions.add(btnXoa);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(12, 6, 6, 6);
        card.add(actions, gbc);

        root.add(NhanVienUiHelper.createTitle("Thêm nhân viên mới"), BorderLayout.NORTH);
        root.add(card, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void luuNhanVien() {
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

            if (nhanVienDAO.tonTaiMaNhanVien(maNV)) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên đã tồn tại.");
                return;
            }

            boolean thanhCong = nhanVienDAO.themNhanVien(new NhanVien(
                    maNV,
                    tenNV,
                    txtSdt.getText().trim(),
                    txtDiaChi.getText().trim(),
                    ngaySinh,
                    ngayVaoLam,
                    maTK
            ));

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã thêm nhân viên thành công.");
                xoaTrang();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }

    private void xoaTrang() {
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtSdt.setText("");
        txtDiaChi.setText("");
        NhanVienUiHelper.setDatePickerValue(txtNgaySinh, null);
        NhanVienUiHelper.setDatePickerValue(txtNgayVaoLam, null);
        txtMaTK.setText("");
    }
}