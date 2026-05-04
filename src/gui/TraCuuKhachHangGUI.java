package gui;

import dao.KhachHangDAO;
import entity.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TraCuuKhachHangGUI extends JDialog {

    private JTextField txtMaKH = new JTextField(15);
    private DefaultTableModel tableModel;
    private KhachHangDAO dao = new KhachHangDAO();

    public TraCuuKhachHangGUI(JFrame parent) {
        super(parent, "Tra cứu khách hàng", true);
        setSize(620, 420);
        setLocationRelativeTo(parent);

        // ── Panel tìm kiếm ───────────────────────────────────────────
        JPanel panelTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTim.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));

        panelTim.add(new JLabel("Nhập mã KH:"));
        panelTim.add(txtMaKH);

        JButton btnTim = new JButton("Tìm");
        JButton btnXoaTrang = new JButton("Xóa trắng");
        panelTim.add(btnTim);
        panelTim.add(btnXoaTrang);

        // ── Bảng kết quả ─────────────────────────────────────────────
        String[] cols = {"Mã KH", "Tên KH", "Số điện thoại", "Điểm tích lũy", "Loại KH"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setRowHeight(24);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Kết quả"));

        // ── Gộp lại ──────────────────────────────────────────────────
        setLayout(new BorderLayout(0, 8));
        add(panelTim, BorderLayout.NORTH);
        add(scroll,   BorderLayout.CENTER);

        // ── Xử lý nút Tìm ────────────────────────────────────────────
        btnTim.addActionListener(e -> {
            String ma = txtMaKH.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã KH cần tìm!");
                return;
            }

            tableModel.setRowCount(0); // xóa kết quả cũ

            KhachHang kh = dao.timTheoMa(ma);
            if (kh != null) {
                tableModel.addRow(new Object[]{
                    kh.getMaKH(), kh.getTenKH(), kh.getSdt(),
                    kh.getDiemTichLuy(), kh.getLoaiKH()
                });
            } else {
                JOptionPane.showMessageDialog(this,
                    "Không tìm thấy khách hàng với mã: " + ma,
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // ── Xử lý nút Xóa trắng ──────────────────────────────────────
        btnXoaTrang.addActionListener(e -> {
            txtMaKH.setText("");
            tableModel.setRowCount(0);
            txtMaKH.requestFocus();
        });

        // Nhấn Enter trong ô tìm cũng kích hoạt tìm
        txtMaKH.addActionListener(e -> btnTim.doClick());

        setVisible(true);
    }
}
