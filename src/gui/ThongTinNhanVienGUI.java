package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import entity.NhanVien;
import entity.TaiKhoan;

public class ThongTinNhanVienGUI extends JDialog {
    public ThongTinNhanVienGUI(Frame owner, TaiKhoan taiKhoan, NhanVien nhanVien) {
        super(owner, "Thông tin nhân viên", true);

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(460, 280);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));
        form.add(new JLabel("Tên đăng nhập:"));
        form.add(new JLabel(taiKhoan != null ? safeText(taiKhoan.getUsername()) : ""));

        form.add(new JLabel("Vai trò:"));
        form.add(new JLabel(taiKhoan != null ? safeText(taiKhoan.getRole()) : ""));

        form.add(new JLabel("Mã nhân viên:"));
        form.add(new JLabel(nhanVien != null ? safeText(nhanVien.getMaNV()) : "Chưa có hồ sơ nhân viên"));

        form.add(new JLabel("Họ tên:"));
        form.add(new JLabel(nhanVien != null ? safeText(nhanVien.getTenNV()) : ""));

        form.add(new JLabel("Số điện thoại:"));
        form.add(new JLabel(nhanVien != null ? safeText(nhanVien.getSdt()) : ""));

        form.add(new JLabel("Địa chỉ:"));
        form.add(new JLabel(nhanVien != null ? safeText(nhanVien.getDiaChi()) : ""));

        form.add(new JLabel("Ngày sinh:"));
        form.add(new JLabel(nhanVien != null && nhanVien.getNgaySinh() != null ? nhanVien.getNgaySinh().toString() : ""));

        form.add(new JLabel("Ngày vào làm:"));
        form.add(new JLabel(nhanVien != null && nhanVien.getNgayVaoLam() != null ? nhanVien.getNgayVaoLam().toString() : ""));

        root.add(form, BorderLayout.CENTER);
        setContentPane(root);
    }

    private String safeText(String value) {
        return value == null || value.trim().isEmpty() ? "" : value;
    }
}