package gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;

public class DoiMatKhauGUI extends JDialog {
    private final TaiKhoan taiKhoan;
    private final TaiKhoanDAO taiKhoanDAO;

    private final JTextField txtUsername;
    private final JPasswordField txtMatKhauCu;
    private final JPasswordField txtMatKhauMoi;
    private final JPasswordField txtXacNhan;
    private final boolean cheDoDangNhap;

    public DoiMatKhauGUI(Frame owner, TaiKhoan taiKhoan) {
        super(owner, "Đổi mật khẩu", true);
        this.taiKhoan = taiKhoan;
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.cheDoDangNhap = false;

        txtUsername = null;

        txtMatKhauCu = new JPasswordField();
        txtMatKhauMoi = new JPasswordField();
        txtXacNhan = new JPasswordField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(420, 240);
        setLocationRelativeTo(owner);

        initUI();
    }

    public DoiMatKhauGUI(Frame owner) {
        super(owner, "Đổi mật khẩu", true);
        this.taiKhoan = null;
        this.taiKhoanDAO = new TaiKhoanDAO();
        this.cheDoDangNhap = true;

        txtUsername = new JTextField();
        txtMatKhauCu = new JPasswordField();
        txtMatKhauMoi = new JPasswordField();
        txtXacNhan = new JPasswordField();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(460, 290);
        setLocationRelativeTo(owner);

        initUI();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel topBar = new JPanel(new BorderLayout());
        JButton btnVeTrangChu = new JButton(cheDoDangNhap ? "Quay về đăng nhập" : "Quay về trang chủ");
        btnVeTrangChu.addActionListener(e -> dispose());
        topBar.add(btnVeTrangChu, BorderLayout.WEST);
        root.add(topBar, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(cheDoDangNhap ? 5 : 4, 2, 8, 8));
        if (cheDoDangNhap) {
            form.add(new JLabel("Tên đăng nhập:"));
            form.add(txtUsername);
        } else {
            form.add(new JLabel("Tài khoản:"));
            form.add(new JLabel(taiKhoan.getUsername()));
        }

        form.add(new JLabel("Mật khẩu cũ:"));
        form.add(txtMatKhauCu);

        form.add(new JLabel("Mật khẩu mới:"));
        form.add(txtMatKhauMoi);

        form.add(new JLabel("Xác nhận mật khẩu:"));
        form.add(txtXacNhan);

        root.add(form, BorderLayout.CENTER);

        JButton btnCapNhat = new JButton("Cập nhật");
        btnCapNhat.addActionListener(e -> xuLyDoiMatKhau());

        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());

        JPanel actions = new JPanel(new GridLayout(1, 2, 8, 8));
        actions.add(btnCapNhat);
        actions.add(btnDong);

        root.add(actions, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void xuLyDoiMatKhau() {
        String username = cheDoDangNhap ? txtUsername.getText().trim() : taiKhoan.getUsername();
        String matKhauCu = new String(txtMatKhauCu.getPassword());
        String matKhauMoi = new String(txtMatKhauMoi.getPassword());
        String xacNhan = new String(txtXacNhan.getPassword());

        if (cheDoDangNhap && username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập.");
            return;
        }

        if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhan.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (!matKhauMoi.equals(xacNhan)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới và xác nhận không khớp.");
            return;
        }

        if (matKhauMoi.length() < 6) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới tối thiểu 6 ký tự.");
            return;
        }

        try {
            boolean thanhCong = cheDoDangNhap
                    ? taiKhoanDAO.doiMatKhauTheoUsername(username, matKhauCu, matKhauMoi)
                    : taiKhoanDAO.doiMatKhau(taiKhoan.getMaTK(), matKhauCu, matKhauMoi);
            if (!thanhCong) {
                JOptionPane.showMessageDialog(this, "Mật khẩu cũ không đúng.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công.");
            dispose();
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Không thể đổi mật khẩu: " + ex.getMessage());
        }
    }
}
