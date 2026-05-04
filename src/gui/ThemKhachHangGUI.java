package gui;

import dao.KhachHangDAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ThemKhachHangGUI extends JDialog {

    private JTextField txtMaKH   = new JTextField(15);
    private JTextField txtTenKH  = new JTextField(15);
    private JTextField txtSdt    = new JTextField(15);
    private JComboBox<String> cboLoai = new JComboBox<>(new String[]{"Thường", "Thân thiết", "VIP"});

    private DefaultTableModel tableModel;
    private KhachHangDAO dao = new KhachHangDAO();

    public ThemKhachHangGUI(JFrame parent) {
        super(parent, "Thêm khách hàng", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);

        // ── Panel nhập liệu ──────────────────────────────────────────
        JPanel panelNhap = new JPanel(new GridLayout(5, 2, 8, 8));
        panelNhap.setBorder(BorderFactory.createTitledBorder("Nhập thông tin"));

        panelNhap.add(new JLabel("Mã KH:"));        panelNhap.add(txtMaKH);
        panelNhap.add(new JLabel("Tên KH:"));        panelNhap.add(txtTenKH);
        panelNhap.add(new JLabel("Số điện thoại:")); panelNhap.add(txtSdt);
        panelNhap.add(new JLabel("Loại KH:"));       panelNhap.add(cboLoai);

        JButton btnLuu = new JButton("Lưu");
        panelNhap.add(new JLabel());
        panelNhap.add(btnLuu);

        // ── Bảng hiển thị ───────────────────────────────────────────
        String[] cols = {"Mã KH", "Tên KH", "Số điện thoại", "Loại KH"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách khách hàng"));

        // ── Gộp lại ─────────────────────────────────────────────────
        setLayout(new BorderLayout(0, 10));
        add(panelNhap, BorderLayout.NORTH);
        add(scroll,    BorderLayout.CENTER);

        // ── Xử lý nút Lưu ───────────────────────────────────────────
        btnLuu.addActionListener(e -> {
            String ma   = txtMaKH.getText().trim();
            String ten  = txtTenKH.getText().trim();
            String sdt  = txtSdt.getText().trim();
            String loai = (String) cboLoai.getSelectedItem();

            if (ma.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            KhachHang kh = new KhachHang(ma, ten, sdt, 0, loai);

            if (dao.them(kh)) {
                // Thêm dòng mới vào bảng bên dưới
                tableModel.addRow(new Object[]{ma, ten, sdt, loai});
                // Xóa trắng form
                txtMaKH.setText(""); txtTenKH.setText(""); txtSdt.setText("");
                cboLoai.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Lưu thất bại! Mã đã tồn tại hoặc lỗi kết nối.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }
}