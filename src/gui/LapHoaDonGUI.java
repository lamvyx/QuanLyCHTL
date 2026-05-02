package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import dao.HoaDonDAO;
import entity.HoaDon;

public class LapHoaDonGUI extends JFrame {
    private final HoaDonDAO hoaDonDAO;
    private final DefaultTableModel model;

    private final JTextField txtMaHD = new JTextField();
    private final DatePickerField txtNgayLap = NhanVienUiHelper.createDatePicker();
    private final JTextField txtMaNV = new JTextField();
    private final JTextField txtMaKH = new JTextField();
    private final JTextField txtMaThue = new JTextField();
    private final JTextField txtMaKM = new JTextField();
    private final JTextField txtLocMaNV = new JTextField(18);

    public LapHoaDonGUI() {
        this.hoaDonDAO = new HoaDonDAO();

        model = new DefaultTableModel(new Object[] { "Mã HD", "Ngày lập", "Mã NV", "Mã KH", "Mã thuế", "Mã KM" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static void moCuaSo() {
        LapHoaDonGUI gui = new LapHoaDonGUI();
        gui.initUI();
        gui.setTitle("Lập hóa đơn");
        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gui.setSize(1080, 640);
        gui.setLocationRelativeTo(null);
        NhanVienUiHelper.setDatePickerValue(gui.txtNgayLap, LocalDate.now());
        gui.setVisible(true);
        gui.taiBangAsync();
    }

    private void initUI() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(new javax.swing.border.EmptyBorder(14, 14, 14, 14));

        JPanel form = NhanVienUiHelper.createCardPanel();
        txtMaHD.setColumns(24);
        txtMaNV.setColumns(24);
        txtMaKH.setColumns(24);
        txtMaThue.setColumns(24);
        txtMaKM.setColumns(24);

        NhanVienUiHelper.addRow(form, 0, "Mã hóa đơn:", txtMaHD);
        NhanVienUiHelper.addRow(form, 1, "Ngày lập (dd/MM/yyyy):", txtNgayLap);
        NhanVienUiHelper.addRow(form, 2, "Mã nhân viên:", txtMaNV);
        NhanVienUiHelper.addRow(form, 3, "Mã khách hàng (để trống nếu không có):", txtMaKH);
        NhanVienUiHelper.addRow(form, 4, "Mã thuế (để trống nếu không có):", txtMaThue);
        NhanVienUiHelper.addRow(form, 5, "Mã khuyến mãi (để trống nếu không có):", txtMaKM);

        JButton btnLap = new JButton("Lập hóa đơn");
        NhanVienUiHelper.styleButton(btnLap, NhanVienUiHelper.TEAL);
        btnLap.addActionListener(e -> lapHoaDon());

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> taiBang(hoaDonDAO.timTatCa()));

        JPanel action = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        action.setOpaque(false);
        action.add(btnLap);
        action.add(btnTaiLai);

        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        gbc.insets = new java.awt.Insets(12, 6, 6, 6);
        form.add(action, gbc);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(new JLabel("Lọc theo mã NV:"));
        top.add(txtLocMaNV);
        JButton btnLoc = new JButton("Lọc");
        NhanVienUiHelper.styleButton(btnLoc, NhanVienUiHelper.BLUE);
        btnLoc.addActionListener(e -> taiBang(hoaDonDAO.timTheoMaNhanVien(txtLocMaNV.getText())));
        top.add(btnLoc);

        root.add(NhanVienUiHelper.createTitle("Lập hóa đơn và xem danh sách gần nhất"), BorderLayout.NORTH);
        root.add(form, BorderLayout.WEST);
        root.add(top, BorderLayout.CENTER);
        root.add(new JScrollPane(new JTable(model)), BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void lapHoaDon() {
        try {
            String maHD = txtMaHD.getText().trim();
            String maNV = txtMaNV.getText().trim();
            LocalDate ngayLap = NhanVienUiHelper.getDatePickerValue(txtNgayLap);

            if (NhanVienUiHelper.isBlank(maHD) || NhanVienUiHelper.isBlank(maNV) || ngayLap == null) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn, ngày lập và mã nhân viên không được để trống.");
                return;
            }

            boolean thanhCong = hoaDonDAO.themHoaDon(new HoaDon(
                    maHD,
                    ngayLap,
                    maNV,
                    txtMaKH.getText().trim(),
                    txtMaThue.getText().trim(),
                    txtMaKM.getText().trim()
            ));

            if (thanhCong) {
                JOptionPane.showMessageDialog(this, "Đã lập hóa đơn thành công.");
                taiBang(hoaDonDAO.timTatCa());
            } else {
                JOptionPane.showMessageDialog(this, "Không thể lập hóa đơn.");
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, "Lỗi cơ sở dữ liệu: " + ex.getMessage());
        }
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
                    JOptionPane.showMessageDialog(
                            LapHoaDonGUI.this,
                            "Không thể tải danh sách hóa đơn: " + ex.getMessage()
                    );
                } catch (java.util.concurrent.ExecutionException ex) {
                    JOptionPane.showMessageDialog(
                            LapHoaDonGUI.this,
                            "Không thể tải danh sách hóa đơn: " + ex.getMessage()
                    );
                }
            }
        }.execute();
    }
}