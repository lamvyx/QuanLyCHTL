package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import dao.NhanVienDAO;
import entity.NhanVien;

public class TraCuuNhanVienGUI extends JFrame {
    private final NhanVienDAO nhanVienDAO;
    private final DefaultTableModel model;
    private final JTable table;
    private final JTextField txtTuKhoa = new JTextField(26);

    public TraCuuNhanVienGUI() {
        this.nhanVienDAO = new NhanVienDAO();

        model = new DefaultTableModel(new Object[] {
                "Mã NV", "Họ tên", "SĐT", "Địa chỉ", "Ngày sinh", "Ngày vào làm", "Mã TK"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public static void moCuaSo() {
        TraCuuNhanVienGUI gui = new TraCuuNhanVienGUI();
        gui.initUI();
        gui.setTitle("Tra cứu nhân viên");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(980, 620);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.napDuLieuAsync();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(new javax.swing.border.EmptyBorder(14, 14, 14, 14));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(new JLabel("Từ khóa (mã, tên, SĐT):"));
        top.add(txtTuKhoa);

        JButton btnTim = new JButton("Tìm kiếm");
        NhanVienUiHelper.styleButton(btnTim, NhanVienUiHelper.BLUE);
        btnTim.addActionListener(e -> napDuLieu(nhanVienDAO.timTheoTuKhoa(txtTuKhoa.getText())));

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> {
            txtTuKhoa.setText("");
            napDuLieu(nhanVienDAO.timTatCa());
        });

        JButton btnMoCapNhat = new JButton("Mở màn hình cập nhật");
        NhanVienUiHelper.styleGhostButton(btnMoCapNhat);
        btnMoCapNhat.addActionListener(e -> moCapNhat());

        top.add(btnTim);
        top.add(btnTaiLai);
        top.add(btnMoCapNhat);

        root.add(NhanVienUiHelper.createTitle("Tra cứu nhân viên"), BorderLayout.NORTH);
        root.add(top, BorderLayout.WEST);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        setContentPane(root);
    }

    private void napDuLieu(List<NhanVien> danhSach) {
        model.setRowCount(0);
        for (NhanVien nv : danhSach) {
            model.addRow(new Object[] {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getDiaChi(),
                    NhanVienUiHelper.formatDate(nv.getNgaySinh()),
                    NhanVienUiHelper.formatDate(nv.getNgayVaoLam()),
                    nv.getMaTK()
            });
        }
    }

    private void moCapNhat() {
        int row = table.getSelectedRow();
        if (row < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn một nhân viên.");
            return;
        }

        String maNV = String.valueOf(model.getValueAt(row, 0));
        CapNhatNhanVienGUI.moCuaSo(maNV);
    }

    private void napDuLieuAsync() {
        new SwingWorker<List<NhanVien>, Void>() {
            @Override
            protected List<NhanVien> doInBackground() {
                return nhanVienDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    napDuLieu(get());
                } catch (java.lang.InterruptedException | java.util.concurrent.ExecutionException ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                            TraCuuNhanVienGUI.this,
                            "Không thể tải danh sách nhân viên: " + ex.getMessage()
                    );
                }
            }
        }.execute();
    }
}