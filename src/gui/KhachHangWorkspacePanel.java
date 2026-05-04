package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhachHangDAO;
import entity.KhachHang;

public class KhachHangWorkspacePanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    // Components cho phần Tra cứu
    private DefaultTableModel modelTraCuu, modelHistory;
    private JTable tableTraCuu, tableHistory;
    private JTextField txtSearch, txtSearchHistory;

    // Components cho phần Thêm
    private JTextField txtThemMa, txtThemTen, txtThemSdt;
    private JComboBox<String> cboThemLoai;

    // Components cho phần Cập nhật
    private JTextField txtCapNhatMa, txtCapNhatTen, txtCapNhatSdt, txtCapNhatDiem;
    private JComboBox<String> cboCapNhatLoai;

    public KhachHangWorkspacePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Header tiêu đề
        add(NhanVienUiHelper.createHeader("Quản lý khách hàng", "Xem lịch sử mua hàng, cập nhật điểm tích lũy và thông tin khách hàng"), BorderLayout.NORTH);

        // Nội dung chính
        container.setOpaque(false);
        container.add(createTraCuuPanel(), "traCuu");
        container.add(createThemPanel(), "them");
        container.add(createCapNhatPanel(), "capNhat");
        container.add(createHistoryPanel(), "history");

        add(container, BorderLayout.CENTER);
    }

    private JPanel createTraCuuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Thanh công cụ tìm kiếm cao cấp
        JPanel searchCard = NhanVienUiHelper.createContentCard();
        searchCard.setLayout(new BorderLayout(15, 0));
        searchCard.setPreferredSize(new Dimension(0, 75));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(NhanVienUiHelper.BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm theo mã, tên hoặc số điện thoại...");

        JButton btnSearch = NhanVienUiHelper.createPrimaryButton("Tìm kiếm khách hàng");
        btnSearch.addActionListener(e -> loadDataToTable());

        searchCard.add(txtSearch, BorderLayout.CENTER);
        searchCard.add(btnSearch, BorderLayout.EAST);

        panel.add(searchCard, BorderLayout.NORTH);

        // Bảng dữ liệu trong Card
        JPanel tableCard = NhanVienUiHelper.createContentCard();
        String[] cols = {"Mã KH", "Tên khách hàng", "Số điện thoại", "Điểm tích lũy", "Loại khách hàng"};
        modelTraCuu = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableTraCuu = new JTable(modelTraCuu);
        NhanVienUiHelper.styleTable(tableTraCuu);
        tableCard.add(new JScrollPane(tableTraCuu), BorderLayout.CENTER);

        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createThemPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 0, 40, 0));

        JPanel card = NhanVienUiHelper.createContentCard();
        card.setLayout(new BorderLayout(0, 25));
        
        JPanel form = new JPanel(new GridLayout(4, 2, 30, 20));
        form.setOpaque(false);

        form.add(createFieldWrapper("Mã khách hàng:", txtThemMa = new JTextField()));
        form.add(createFieldWrapper("Tên khách hàng:", txtThemTen = new JTextField()));
        form.add(createFieldWrapper("Số điện thoại:", txtThemSdt = new JTextField()));
        form.add(createFieldWrapper("Loại khách hàng:", cboThemLoai = new JComboBox<>(new String[]{"Thường", "Thân thiết", "VIP"})));

        JButton btnLuu = NhanVienUiHelper.createPrimaryButton("Lưu thông tin khách hàng");
        btnLuu.addActionListener(e -> handleThemKhachHang());

        card.add(NhanVienUiHelper.createSectionTitle("Đăng ký khách hàng mới"), BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        card.add(btnLuu, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(800, 450));
        wrapper.add(card);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCapNhatPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(40, 0, 40, 0));

        JPanel card = NhanVienUiHelper.createContentCard();
        card.setLayout(new BorderLayout(0, 20));

        // Phần tìm kiếm để cập nhật
        JPanel findBox = new JPanel(new BorderLayout(15, 0));
        findBox.setOpaque(false);
        txtCapNhatMa = new JTextField();
        JButton btnFind = NhanVienUiHelper.createSecondaryButton("Tìm mã");
        findBox.add(txtCapNhatMa, BorderLayout.CENTER);
        findBox.add(btnFind, BorderLayout.EAST);

        JPanel form = new JPanel(new GridLayout(4, 2, 30, 15));
        form.setOpaque(false);
        form.add(createFieldWrapper("Tên khách hàng:", txtCapNhatTen = new JTextField()));
        form.add(createFieldWrapper("Số điện thoại:", txtCapNhatSdt = new JTextField()));
        form.add(createFieldWrapper("Điểm tích lũy:", txtCapNhatDiem = new JTextField()));
        form.add(createFieldWrapper("Loại khách hàng:", cboCapNhatLoai = new JComboBox<>(new String[]{"Thường", "Thân thiết", "VIP"})));

        JButton btnCapNhat = NhanVienUiHelper.createPrimaryButton("Xác nhận cập nhật");
        btnCapNhat.addActionListener(e -> handleCapNhatKhachHang());
        btnFind.addActionListener(e -> handleTimDeCapNhat());

        JPanel centerBody = new JPanel(new BorderLayout(0, 25));
        centerBody.setOpaque(false);
        centerBody.add(findBox, BorderLayout.NORTH);
        centerBody.add(form, BorderLayout.CENTER);

        card.add(NhanVienUiHelper.createSectionTitle("Chỉnh sửa thông tin khách hàng"), BorderLayout.NORTH);
        card.add(centerBody, BorderLayout.CENTER);
        card.add(btnCapNhat, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(850, 500));
        wrapper.add(card);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel searchCard = NhanVienUiHelper.createContentCard();
        searchCard.setLayout(new BorderLayout(15, 0));
        searchCard.setPreferredSize(new Dimension(0, 75));

        txtSearchHistory = new JTextField();
        txtSearchHistory.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearchHistory.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(NhanVienUiHelper.BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtSearchHistory.putClientProperty("JTextField.placeholderText", "Nhập mã khách hàng để xem lịch sử...");

        JButton btnSearch = NhanVienUiHelper.createPrimaryButton("Xem lịch sử");
        btnSearch.addActionListener(e -> loadHistoryData());

        searchCard.add(txtSearchHistory, BorderLayout.CENTER);
        searchCard.add(btnSearch, BorderLayout.EAST);

        JPanel tableCard = NhanVienUiHelper.createContentCard();
        String[] cols = {"Mã hóa đơn", "Ngày lập", "Tổng tiền", "Điểm cộng", "Nhân viên"};
        modelHistory = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableHistory = new JTable(modelHistory);
        NhanVienUiHelper.styleTable(tableHistory);
        tableCard.add(new JScrollPane(tableHistory), BorderLayout.CENTER);

        panel.add(searchCard, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFieldWrapper(String label, JComponent comp) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(NhanVienUiHelper.TEXT_MAIN);
        p.add(lbl, BorderLayout.NORTH);
        p.add(comp, BorderLayout.CENTER);
        if (comp instanceof JTextField) {
            comp.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(NhanVienUiHelper.BORDER, 1),
                new EmptyBorder(8, 10, 8, 10)
            ));
        }
        return p;
    }

    public void showThemKhachHang() {
        cardLayout.show(container, "them");
    }

    public void showTraCuuKhachHang() {
        cardLayout.show(container, "traCuu");
        loadDataToTable();
    }

    public void showCapNhatKhachHang() {
        cardLayout.show(container, "capNhat");
    }

    public void showLichSuMuaHang() {
        cardLayout.show(container, "history");
    }

    private void loadHistoryData() {
        String maKH = txtSearchHistory.getText().trim();
        if (maKH.isEmpty()) return;
        // Logic load history using DAO
        new SwingWorker<List<Object[]>, Void>() {
            @Override protected List<Object[]> doInBackground() {
                return khachHangDAO.layLichSuMuaHang(maKH);
            }
            @Override protected void done() {
                try {
                    modelHistory.setRowCount(0);
                    for (Object[] row : get()) modelHistory.addRow(row);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void loadDataToTable() {
        String keyword = txtSearch.getText().trim();
        new SwingWorker<List<KhachHang>, Void>() {
            @Override
            protected List<KhachHang> doInBackground() {
                if (keyword.isEmpty()) return khachHangDAO.timTatCa();
                return khachHangDAO.timTheoTuKhoa(keyword);
            }

            @Override
            protected void done() {
                try {
                    modelTraCuu.setRowCount(0);
                    for (KhachHang kh : get()) {
                        modelTraCuu.addRow(new Object[]{
                            kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getDiemTichLuy(), kh.getLoaiKH()
                        });
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void handleThemKhachHang() {
        String ma = txtThemMa.getText().trim();
        String ten = txtThemTen.getText().trim();
        String sdt = txtThemSdt.getText().trim();
        String loai = (String) cboThemLoai.getSelectedItem();

        if (ma.isEmpty() || ten.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã và tên!");
            return;
        }

        KhachHang kh = new KhachHang(ma, ten, sdt, 0, loai);
        if (khachHangDAO.themKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
            txtThemMa.setText(""); txtThemTen.setText(""); txtThemSdt.setText("");
            showTraCuuKhachHang();
        } else {
            JOptionPane.showMessageDialog(this, "Lỗi: Mã khách hàng đã tồn tại!");
        }
    }

    private void handleTimDeCapNhat() {
        String ma = txtCapNhatMa.getText().trim();
        KhachHang kh = khachHangDAO.timTheoMa(ma);
        if (kh != null) {
            txtCapNhatTen.setText(kh.getTenKH());
            txtCapNhatSdt.setText(kh.getSdt());
            txtCapNhatDiem.setText(String.valueOf(kh.getDiemTichLuy()));
            cboCapNhatLoai.setSelectedItem(kh.getLoaiKH());
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng mã: " + ma);
        }
    }

    private void handleCapNhatKhachHang() {
        String ma = txtCapNhatMa.getText().trim();
        String ten = txtCapNhatTen.getText().trim();
        String sdt = txtCapNhatSdt.getText().trim();
        int diem = Integer.parseInt(txtCapNhatDiem.getText().trim());
        String loai = (String) cboCapNhatLoai.getSelectedItem();

        KhachHang kh = new KhachHang(ma, ten, sdt, diem, loai);
        if (khachHangDAO.capNhatKhachHang(kh)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            showTraCuuKhachHang();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }
}
