package gui;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

public class DangNhapGUI extends JFrame {
	private final JTextField txtUsername;
	private final JPasswordField txtPassword;
	private final TaiKhoanDAO taiKhoanDAO;

	public DangNhapGUI() {
		super("Dang nhap he thong");
		this.taiKhoanDAO = new TaiKhoanDAO();

		txtUsername = new JTextField();
		txtPassword = new JPasswordField();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(460, 240);
		setLocationRelativeTo(null);

		initUI();
	}

	private void initUI() {
		JPanel root = new JPanel(new BorderLayout(10, 10));
		root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

		JLabel lblTitle = new JLabel("HE THONG QUAN LY CUA HANG", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
		root.add(lblTitle, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridLayout(3, 2, 8, 8));
		form.add(new JLabel("Username:"));
		form.add(txtUsername);

		form.add(new JLabel("Password:"));
		form.add(txtPassword);

		JCheckBox chkShowPassword = new JCheckBox("Hien mat khau");
		chkShowPassword.addActionListener(e -> {
			if (chkShowPassword.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar('\u2022');
			}
		});
		form.add(chkShowPassword);

		JButton btnLogin = new JButton("Dang nhap");
		btnLogin.addActionListener(e -> xuLyDangNhap());
		form.add(btnLogin);

		root.add(form, BorderLayout.CENTER);

		JButton btnExit = new JButton("Thoat");
		btnExit.addActionListener(e -> dispose());
		JPanel south = new JPanel(new BorderLayout());
		south.add(btnExit, BorderLayout.EAST);
		root.add(south, BorderLayout.SOUTH);

		setContentPane(root);
	}

	private void xuLyDangNhap() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());

		if (username.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(
					this,
					"Vui long nhap day du username va password",
					"Canh bao",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		try {
			TaiKhoan taiKhoan = taiKhoanDAO.dangNhap(username, password);
			if (taiKhoan == null) {
				JOptionPane.showMessageDialog(
						this,
						"Sai thong tin dang nhap hoac tai khoan da bi khoa",
						"Dang nhap that bai",
						JOptionPane.ERROR_MESSAGE
				);
				return;
			}

			JOptionPane.showMessageDialog(
					this,
					"Dang nhap thanh cong. Xin chao " + taiKhoan.getUsername() + " (" + taiKhoan.getRole() + ")",
					"Thanh cong",
					JOptionPane.INFORMATION_MESSAGE
			);
		} catch (IllegalStateException ex) {
			JOptionPane.showMessageDialog(
					this,
					"Khong the ket noi hoac truy van CSDL: " + ex.getMessage(),
					"Loi he thong",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// Keep default look and feel if system look and feel is unavailable.
		}

		SwingUtilities.invokeLater(() -> new DangNhapGUI().setVisible(true));
	}
}
