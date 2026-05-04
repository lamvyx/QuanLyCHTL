package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

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
import javax.swing.border.EmptyBorder;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;

public class DangNhapGUI extends JFrame {
	private static final Color NAVY = new Color(17, 24, 39);
	private static final Color BLUE = new Color(37, 99, 235);
	private static final Color GREEN = new Color(16, 185, 129);
	private static final Color BG = new Color(243, 244, 246);
	private static final Color CARD = Color.WHITE;
	private static final Color TEXT = new Color(31, 41, 55);
	private static final Color MUTED = new Color(107, 114, 128);

	private final JTextField txtUsername;
	private final JPasswordField txtPassword;
	private final TaiKhoanDAO taiKhoanDAO;
	private boolean batDangNhap = true;

	public DangNhapGUI() {
		super("Đăng nhập hệ thống");
		this.taiKhoanDAO = new TaiKhoanDAO();

		txtUsername = new JTextField();
		txtPassword = new JPasswordField();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		initUI();
		setMinimumSize(new Dimension(560, 420));
		pack();
		setLocationRelativeTo(null);
	}

	private void initUI() {
		JPanel root = new JPanel(new BorderLayout(10, 10));
		root.setBackground(BG);
		root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

		JPanel header = new JPanel(new BorderLayout());
		header.setBackground(NAVY);
		header.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

		JLabel lblTitle = new JLabel("HỆ THỐNG QUẢN LÝ CỬA HÀNG", JLabel.CENTER);
		lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitle.setForeground(Color.WHITE);
		header.add(lblTitle, BorderLayout.CENTER);
		root.add(header, BorderLayout.NORTH);

		JPanel form = new JPanel(new GridBagLayout());
		form.setBackground(CARD);
		form.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(229, 231, 235)),
				new EmptyBorder(20, 20, 20, 20)
		));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(8, 8, 8, 8);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		JLabel lblUser = new JLabel("Tên đăng nhập:");
		lblUser.setForeground(TEXT);
		lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		form.add(lblUser, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		form.add(txtUsername, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		JLabel lblPass = new JLabel("Mật khẩu:");
		lblPass.setForeground(TEXT);
		lblPass.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		form.add(lblPass, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		form.add(txtPassword, gbc);

		JCheckBox chkShowPassword = new JCheckBox("Hiện mật khẩu");
		chkShowPassword.setBackground(CARD);
		chkShowPassword.setForeground(MUTED);
		chkShowPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		chkShowPassword.addActionListener(e -> {
			if (chkShowPassword.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar('\u2022');
			}
		});
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		form.add(chkShowPassword, gbc);

		JButton btnLogin = new JButton("Đăng nhập");
		stylePrimaryButton(btnLogin, BLUE);
		btnLogin.addActionListener(e -> xuLyDangNhap());

		JButton btnDoiMatKhau = new JButton("Đổi mật khẩu");
		styleSecondaryButton(btnDoiMatKhau, GREEN);
		btnDoiMatKhau.addActionListener(e -> new DoiMatKhauGUI(this).setVisible(true));

		JPanel actionRow = new JPanel(new GridLayout(1, 2, 10, 0));
		actionRow.setBackground(CARD);
		actionRow.add(btnLogin);
		actionRow.add(btnDoiMatKhau);

		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		form.add(actionRow, gbc);

		root.add(form, BorderLayout.CENTER);

		JButton btnExit = new JButton("Thoát");
		styleGhostButton(btnExit);
		btnExit.addActionListener(e -> dispose());
		JPanel south = new JPanel(new BorderLayout());
		south.setBackground(BG);
		south.add(btnExit, BorderLayout.EAST);
		root.add(south, BorderLayout.SOUTH);

		txtUsername.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(209, 213, 219)),
				new EmptyBorder(8, 10, 8, 10)
		));
		txtPassword.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(209, 213, 219)),
				new EmptyBorder(8, 10, 8, 10)
		));

		setContentPane(root);
	}

	private void stylePrimaryButton(JButton button, Color background) {
		button.setBackground(background);
		button.setForeground(Color.BLACK);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
		button.setOpaque(true);
		button.setContentAreaFilled(true);
	}

	private void styleSecondaryButton(JButton button, Color background) {
		button.setBackground(background);
		button.setForeground(Color.BLACK);
		button.setFont(new Font("Segoe UI", Font.BOLD, 14));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
		button.setOpaque(true);
		button.setContentAreaFilled(true);
	}

	private void styleGhostButton(JButton button) {
		button.setBackground(new Color(229, 231, 235));
		button.setForeground(TEXT);
		button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		button.setFocusPainted(false);
		button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
		button.setOpaque(true);
		button.setContentAreaFilled(true);
	}

	private void xuLyDangNhap() {
		String username = txtUsername.getText().trim();
		String password = new String(txtPassword.getPassword());

		if (username.isEmpty()) {
			JOptionPane.showMessageDialog(
					this,
					"Vui lòng nhập tên đăng nhập.",
					"Cảnh báo",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		if (password.isEmpty()) {
			JOptionPane.showMessageDialog(
					this,
					"Vui lòng nhập mật khẩu.",
					"Cảnh báo",
					JOptionPane.WARNING_MESSAGE
			);
			return;
		}

		try {
			TaiKhoan taiKhoan = taiKhoanDAO.dangNhap(username, password);
			if (taiKhoan == null) {
				JOptionPane.showMessageDialog(
						this,
						"Sai thông tin đăng nhập hoặc tài khoản đã bị khóa.",
						"Đăng nhập thất bại",
						JOptionPane.ERROR_MESSAGE
				);
				return;
			}

			JOptionPane.showMessageDialog(
					this,
					"Đăng nhập thành công. Xin chào " + taiKhoan.getUsername() + " (" + taiKhoan.getRole() + ")",
					"Thành công",
					JOptionPane.INFORMATION_MESSAGE
			);

			new HomeGUI(taiKhoan).setVisible(true);
			dispose();
		} catch (IllegalStateException ex) {
			JOptionPane.showMessageDialog(
					this,
					"Không thể kết nối hoặc truy vấn CSDL: " + ex.getMessage(),
					"Lỗi hệ thống",
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	public void setDangNhapVisible(boolean visible) {
		setVisible(visible);
	}

	public void khoiDong(boolean hienThiManHinhDangNhap) {
		this.batDangNhap = hienThiManHinhDangNhap;
		if (this.batDangNhap) {
			setDangNhapVisible(true);
			return;
		}

		TaiKhoan taiKhoanMacDinh = new TaiKhoan();
		taiKhoanMacDinh.setMaTK(0);
		taiKhoanMacDinh.setUsername("admin");
		taiKhoanMacDinh.setPassword("");
		taiKhoanMacDinh.setRole("ADMIN");
		taiKhoanMacDinh.setTrangThai(true);

		dispose();
		new HomeGUI(taiKhoanMacDinh).setVisible(true);
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
			// Giữ giao diện mặc định nếu không dùng được giao diện của hệ điều hành.
		}

		SwingUtilities.invokeLater(() -> {
			DangNhapGUI dangNhapGUI = new DangNhapGUI();
			dangNhapGUI.khoiDong(false);
		});
	}
}
