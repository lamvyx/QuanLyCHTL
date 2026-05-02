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
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import dao.HoaDonDAO;
import entity.HoaDon;

public class HoaDonDaLapGUI extends JFrame {
    private final HoaDonDAO hoaDonDAO;
    private final DefaultTableModel model;
    private final JTextField txtMaNV = new JTextField(18);

    public HoaDonDaLapGUI() {
        this.hoaDonDAO = new HoaDonDAO();

        model = new DefaultTableModel(new Object[] { "Mã HD", "Ngày lập", "Mã NV", "Mã KH", "Mã thuế", "Mã KM" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static void moCuaSo() {
        HoaDonDaLapGUI gui = new HoaDonDaLapGUI();
        gui.initUI();
        gui.setTitle("Hóa đơn đã lập");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(980, 600);
        gui.setLocationRelativeTo(null);
        gui.setVisible(true);
        gui.taiBangAsync();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(new javax.swing.border.EmptyBorder(14, 14, 14, 14));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(new JLabel("Lọc theo mã NV:"));
        top.add(txtMaNV);

        JButton btnLoc = new JButton("Lọc");
        NhanVienUiHelper.styleButton(btnLoc, NhanVienUiHelper.BLUE);
        btnLoc.addActionListener(e -> taiBang(hoaDonDAO.timTheoMaNhanVien(txtMaNV.getText())));

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> {
            txtMaNV.setText("");
            taiBang(hoaDonDAO.timTatCa());
        });

        top.add(btnLoc);
        top.add(btnTaiLai);

        root.add(NhanVienUiHelper.createTitle("Hóa đơn đã lập"), BorderLayout.NORTH);
        root.add(top, BorderLayout.WEST);
        root.add(new JScrollPane(new JTable(model)), BorderLayout.CENTER);
        setContentPane(root);
    }

    private void taiBang(List<HoaDon> danhSach) {
        model.setRowCount(0);
        for (HoaDon hd : danhSach) {
            model.addRow(new Object[] {
                    hd.getMaHD(),
                    NhanVienUiHelper.formatDate(hd.getNgayLap()),
                    hd.getMaNV(),
                    hd.getMaKH(),
                    hd.getMaThue(),
                    hd.getMaKM()
            });
        }
    }

    private void taiBangAsync() {
        new SwingWorker<List<HoaDon>, Void>() {
            @Override
            protected List<HoaDon> doInBackground() {
                return hoaDonDAO.timTatCa();
            }

            @Override
            protected void done() {
                try {
                    taiBang(get());
                } catch (java.lang.InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    javax.swing.JOptionPane.showMessageDialog(
                            HoaDonDaLapGUI.this,
                            "Không thể tải danh sách hóa đơn: " + ex.getMessage()
                    );
                } catch (java.util.concurrent.ExecutionException ex) {
                    javax.swing.JOptionPane.showMessageDialog(
                            HoaDonDaLapGUI.this,
                            "Không thể tải danh sách hóa đơn: " + ex.getMessage()
                    );
                }
            }
        }.execute();
    }
}