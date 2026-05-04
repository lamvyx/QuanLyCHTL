package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import dao.KhuyenMaiDAO;
import entity.KhuyenMai;

public class KhuyenMaiWorkspacePanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    private DefaultTableModel modelTraCuu;
    private JTable tableTraCuu;
    private JTextField txtSearch;

    private JTextField txtThemMa, txtThemTen, txtThemPhanTram, txtThemBD, txtThemKT, txtThemMoTa;
    private JTextField txtCapNhatMa, txtCapNhatTen, txtCapNhatPhanTram, txtCapNhatBD, txtCapNhatKT, txtCapNhatMoTa;

    public KhuyenMaiWorkspacePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(NhanVienUiHelper.createHeader("Quản lý chương trình khuyến mãi", "Thiết lập các chương trình giảm giá và ưu đãi cho khách hàng"), BorderLayout.NORTH);

        container.setOpaque(false);
        container.add(createTraCuuPanel(), "traCuu");
        container.add(createThemPanel(), "them");
        container.add(createCapNhatPanel(), "capNhat");

        add(container, BorderLayout.CENTER);
    }

    private JPanel createTraCuuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel searchCard = NhanVienUiHelper.createContentCard();
        searchCard.setLayout(new BorderLayout(15, 0));
        searchCard.setPreferredSize(new Dimension(0, 85));
        searchCard.setBorder(new EmptyBorder(10, 15, 10, 15));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(NhanVienUiHelper.BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm chương trình khuyến mãi...");

        JPanel searchActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        searchActions.setOpaque(false);

        javax.swing.JCheckBox chkActiveOnly = new javax.swing.JCheckBox("Chỉ hiện KM còn hiệu lực");
        chkActiveOnly.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkActiveOnly.setForeground(NhanVienUiHelper.TEXT_MAIN);
        chkActiveOnly.setOpaque(false);
        chkActiveOnly.addActionListener(e -> loadDataToTable(chkActiveOnly.isSelected()));

        JButton btnSearch = NhanVienUiHelper.createPrimaryButton("Tìm kiếm");
        btnSearch.addActionListener(e -> loadDataToTable(chkActiveOnly.isSelected()));

        searchActions.add(chkActiveOnly);
        searchActions.add(btnSearch);

        searchCard.add(txtSearch, BorderLayout.CENTER);
        searchCard.add(searchActions, BorderLayout.EAST);

        panel.add(searchCard, BorderLayout.NORTH);

        JPanel tableCard = NhanVienUiHelper.createContentCard();
        String[] cols = {"Mã KM", "Tên chương trình", "% Giảm", "Ngày bắt đầu", "Ngày kết thúc", "Trạng thái"};
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

        JPanel form = new JPanel(new GridLayout(3, 2, 30, 20));
        form.setOpaque(false);

        form.add(createFieldWrapper("Mã khuyến mãi:", txtThemMa = new JTextField()));
        form.add(createFieldWrapper("Tên chương trình:", txtThemTen = new JTextField()));
        form.add(createFieldWrapper("Phần trăm giảm (%):", txtThemPhanTram = new JTextField()));
        form.add(createFieldWrapper("Mô tả:", txtThemMoTa = new JTextField()));
        form.add(createFieldWrapper("Ngày bắt đầu (yyyy-MM-dd):", txtThemBD = new JTextField()));
        form.add(createFieldWrapper("Ngày kết thúc (yyyy-MM-dd):", txtThemKT = new JTextField()));

        JButton btnLuu = NhanVienUiHelper.createPrimaryButton("Tạo chương trình mới");
        btnLuu.addActionListener(e -> handleThemKM());

        card.add(NhanVienUiHelper.createSectionTitle("Thiết lập ưu đãi mới"), BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        card.add(btnLuu, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(950, 480));
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

        JPanel findBox = new JPanel(new BorderLayout(15, 0));
        findBox.setOpaque(false);
        txtCapNhatMa = new JTextField();
        JButton btnFind = NhanVienUiHelper.createSecondaryButton("Tìm mã KM");
        findBox.add(txtCapNhatMa, BorderLayout.CENTER);
        findBox.add(btnFind, BorderLayout.EAST);

        JPanel form = new JPanel(new GridLayout(3, 2, 30, 15));
        form.setOpaque(false);
        form.add(createFieldWrapper("Tên chương trình:", txtCapNhatTen = new JTextField()));
        form.add(createFieldWrapper("Phần trăm giảm (%):", txtCapNhatPhanTram = new JTextField()));
        form.add(createFieldWrapper("Ngày bắt đầu:", txtCapNhatBD = new JTextField()));
        form.add(createFieldWrapper("Ngày kết thúc:", txtCapNhatKT = new JTextField()));
        form.add(createFieldWrapper("Mô tả:", txtCapNhatMoTa = new JTextField()));

        JButton btnCapNhat = NhanVienUiHelper.createPrimaryButton("Lưu chỉnh sửa");
        btnCapNhat.addActionListener(e -> handleCapNhatKM());
        btnFind.addActionListener(e -> handleTimDeCapNhat());

        JPanel centerBody = new JPanel(new BorderLayout(0, 25));
        centerBody.setOpaque(false);
        centerBody.add(findBox, BorderLayout.NORTH);
        centerBody.add(form, BorderLayout.CENTER);

        card.add(NhanVienUiHelper.createSectionTitle("Cập nhật thông tin khuyến mãi"), BorderLayout.NORTH);
        card.add(centerBody, BorderLayout.CENTER);
        card.add(btnCapNhat, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(1000, 520));
        wrapper.add(card);

        panel.add(wrapper, BorderLayout.CENTER);
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

    public void showThemKM() { cardLayout.show(container, "them"); }
    public void showTraCuuKM() { cardLayout.show(container, "traCuu"); loadDataToTable(false); }
    public void showCapNhatKM() { cardLayout.show(container, "capNhat"); }

    private void loadDataToTable(boolean activeOnly) {
        String keyword = txtSearch.getText().trim();
        new SwingWorker<List<KhuyenMai>, Void>() {
            @Override protected List<KhuyenMai> doInBackground() {
                if (activeOnly) return khuyenMaiDAO.layConHieuLuc();
                return keyword.isEmpty() ? khuyenMaiDAO.timTatCa() : khuyenMaiDAO.timKiem(keyword);
            }
            @Override protected void done() {
                try {
                    modelTraCuu.setRowCount(0);
                    for (KhuyenMai km : get()) {
                        String status = km.conHieuLuc() ? "Đang chạy" : "Hết hạn";
                        modelTraCuu.addRow(new Object[]{km.getMaKM(), km.getTenKM(), km.getPhanTramGiam(), km.getNgayBD(), km.getNgayKT(), status});
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void handleThemKM() {
        try {
            KhuyenMai km = new KhuyenMai(txtThemMa.getText(), txtThemTen.getText(), txtThemMoTa.getText(), 
                    Double.parseDouble(txtThemPhanTram.getText()), LocalDate.parse(txtThemBD.getText()), LocalDate.parse(txtThemKT.getText()));
            if (khuyenMaiDAO.themKhuyenMai(km)) { JOptionPane.showMessageDialog(this, "Thành công!"); showTraCuuKM(); }
            else { JOptionPane.showMessageDialog(this, "Trùng mã!"); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!"); }
    }

    private void handleTimDeCapNhat() {
        KhuyenMai km = khuyenMaiDAO.timTheoMa(txtCapNhatMa.getText());
        if (km != null) {
            txtCapNhatTen.setText(km.getTenKM()); txtCapNhatPhanTram.setText(String.valueOf(km.getPhanTramGiam()));
            txtCapNhatBD.setText(km.getNgayBD().toString()); txtCapNhatKT.setText(km.getNgayKT().toString());
            txtCapNhatMoTa.setText(km.getMoTa());
        } else { JOptionPane.showMessageDialog(this, "Không tìm thấy!"); }
    }

    private void handleCapNhatKM() {
        try {
            KhuyenMai km = new KhuyenMai(txtCapNhatMa.getText(), txtCapNhatTen.getText(), txtCapNhatMoTa.getText(), 
                    Double.parseDouble(txtCapNhatPhanTram.getText()), LocalDate.parse(txtCapNhatBD.getText()), LocalDate.parse(txtCapNhatKT.getText()));
            if (khuyenMaiDAO.capNhatKhuyenMai(km)) { JOptionPane.showMessageDialog(this, "Cập nhật thành công!"); showTraCuuKM(); }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Lỗi dữ liệu!"); }
    }
}
