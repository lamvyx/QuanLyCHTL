package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;

public class PhanQuyenTaiKhoanGUI extends JFrame {
    private final TaiKhoanDAO taiKhoanDAO;
    private final DefaultTableModel model;
    private final JTable table;
    private final JComboBox<String> cboRole = new JComboBox<>(new String[] { "NHÂN VIÊN", "QUẢN LÝ" });
    private final JComboBox<String> cboTrangThai = new JComboBox<>(new String[] { "Hoạt động", "Khóa" });

    public PhanQuyenTaiKhoanGUI() {
        this.taiKhoanDAO = new TaiKhoanDAO();

        model = new DefaultTableModel(new Object[] { "Mã TK", "Username", "Vai trò", "Trạng thái" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static void moCuaSo() {
        PhanQuyenTaiKhoanGUI gui = new PhanQuyenTaiKhoanGUI();
        gui.initUI();
        gui.setTitle("Phân quyền tài khoản");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(960, 600);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.taiDuLieuAsync();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(new javax.swing.border.EmptyBorder(14, 14, 14, 14));

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
        btnTaiLai.addActionListener(e -> taiDuLieu());

        bottom.add(btnCapNhat);
        bottom.add(btnTaiLai);

        root.add(NhanVienUiHelper.createTitle("Phân quyền và trạng thái tài khoản"), BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void taiDuLieu() {
        model.setRowCount(0);
        for (TaiKhoan tk : taiKhoanDAO.timTatCa()) {
            model.addRow(new Object[] {
                    tk.getMaTK(),
                    tk.getUsername(),
                    tk.getRole(),
                    tk.getTrangThai() != null && tk.getTrangThai() ? "Hoạt động" : "Khóa"
            });
        }
    }

    private void taiDuLieuAsync() {
        new SwingWorker<java.util.List<TaiKhoan>, Void>() {
            @Override
            protected java.util.List<TaiKhoan> doInBackground() {
                return taiKhoanDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    model.setRowCount(0);
                    for (TaiKhoan tk : get()) {
                        model.addRow(new Object[] {
                                tk.getMaTK(),
                                tk.getUsername(),
                                tk.getRole(),
                                tk.getTrangThai() != null && tk.getTrangThai() ? "Hoạt động" : "Khóa"
                        });
                    }
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    JOptionPane.showMessageDialog(
                            PhanQuyenTaiKhoanGUI.this,
                            "Không thể tải danh sách tài khoản: " + ex.getMessage()
                    );
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(
                            PhanQuyenTaiKhoanGUI.this,
                            "Không thể tải danh sách tài khoản: " + ex.getMessage()
                    );
                }
            }
        }.execute();
    }

    private void capNhatPhanQuyen() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một tài khoản.");
            return;
        }

        int maTK = Integer.parseInt(String.valueOf(model.getValueAt(row, 0)));
        String role = "QUẢN LÝ".equals(String.valueOf(cboRole.getSelectedItem())) ? "QUAN_LY" : "NHAN_VIEN";
        boolean trangThai = "Hoạt động".equals(String.valueOf(cboTrangThai.getSelectedItem()));

        try {
            boolean thanhCong = taiKhoanDAO.capNhatPhanQuyen(maTK, role, trangThai);
            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã cập nhật phân quyền.");
                taiDuLieu();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật tài khoản.");
            }
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
    }
}