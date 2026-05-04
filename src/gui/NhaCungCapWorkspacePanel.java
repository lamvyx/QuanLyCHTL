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

import dao.NhaCungCapDAO;
import entity.NhaCungCap;

public class NhaCungCapWorkspacePanel extends JPanel {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);
    private final NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();

    private DefaultTableModel modelTraCuu, modelPhieu;
    private JTable tableTraCuu, tablePhieu;
    private JTextField txtSearch, txtSearchPhieu;

    private JTextField txtThemMa, txtThemTen, txtThemSdt, txtThemDiaChi, txtThemEmail;
    private JTextField txtCapNhatMa, txtCapNhatTen, txtCapNhatSdt, txtCapNhatDiaChi, txtCapNhatEmail;

    public NhaCungCapWorkspacePanel() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(NhanVienUiHelper.createHeader("Quản lý nhà cung cấp", "Quản lý thông tin đối tác và theo dõi giao dịch nhập hàng"), BorderLayout.NORTH);

        container.setOpaque(false);
        container.add(createTraCuuPanel(), "traCuu");
        container.add(createThemPanel(), "them");
        container.add(createCapNhatPanel(), "capNhat");
        container.add(createPhieuPanel(), "phieu");

        add(container, BorderLayout.CENTER);
    }

    private JPanel createTraCuuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel searchCard = NhanVienUiHelper.createContentCard();
        searchCard.setLayout(new BorderLayout(15, 0));
        searchCard.setPreferredSize(new Dimension(0, 75));

        txtSearch = new JTextField();
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(NhanVienUiHelper.BORDER, 1),
            new EmptyBorder(5, 10, 5, 10)
        ));
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm kiếm nhà cung cấp...");

        JButton btnSearch = NhanVienUiHelper.createPrimaryButton("Tìm kiếm");
        btnSearch.addActionListener(e -> loadDataToTable());

        searchCard.add(txtSearch, BorderLayout.CENTER);
        searchCard.add(btnSearch, BorderLayout.EAST);

        panel.add(searchCard, BorderLayout.NORTH);

        JPanel tableCard = NhanVienUiHelper.createContentCard();
        String[] cols = {"Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ", "Email"};
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

        form.add(createFieldWrapper("Mã NCC:", txtThemMa = new JTextField()));
        form.add(createFieldWrapper("Tên NCC:", txtThemTen = new JTextField()));
        form.add(createFieldWrapper("Số điện thoại:", txtThemSdt = new JTextField()));
        form.add(createFieldWrapper("Email:", txtThemEmail = new JTextField()));
        form.add(createFieldWrapper("Địa chỉ:", txtThemDiaChi = new JTextField()));

        JButton btnLuu = NhanVienUiHelper.createPrimaryButton("Đăng ký nhà cung cấp");
        btnLuu.addActionListener(e -> handleThemNCC());

        card.add(NhanVienUiHelper.createSectionTitle("Thêm đối tác cung ứng mới"), BorderLayout.NORTH);
        card.add(form, BorderLayout.CENTER);
        card.add(btnLuu, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(900, 480));
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
        JButton btnFind = NhanVienUiHelper.createSecondaryButton("Tìm mã NCC");
        findBox.add(txtCapNhatMa, BorderLayout.CENTER);
        findBox.add(btnFind, BorderLayout.EAST);

        JPanel form = new JPanel(new GridLayout(2, 2, 30, 15));
        form.setOpaque(false);
        form.add(createFieldWrapper("Tên nhà cung cấp:", txtCapNhatTen = new JTextField()));
        form.add(createFieldWrapper("Số điện thoại:", txtCapNhatSdt = new JTextField()));
        form.add(createFieldWrapper("Địa chỉ:", txtCapNhatDiaChi = new JTextField()));
        form.add(createFieldWrapper("Email:", txtCapNhatEmail = new JTextField()));

        JButton btnCapNhat = NhanVienUiHelper.createPrimaryButton("Cập nhật đối tác");
        btnCapNhat.addActionListener(e -> handleCapNhatNCC());
        btnFind.addActionListener(e -> handleTimDeCapNhat());

        JPanel centerBody = new JPanel(new BorderLayout(0, 25));
        centerBody.setOpaque(false);
        centerBody.add(findBox, BorderLayout.NORTH);
        centerBody.add(form, BorderLayout.CENTER);

        card.add(NhanVienUiHelper.createSectionTitle("Chỉnh sửa thông tin đối tác"), BorderLayout.NORTH);
        card.add(centerBody, BorderLayout.CENTER);
        card.add(btnCapNhat, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        card.setPreferredSize(new Dimension(950, 520));
        wrapper.add(card);

        panel.add(wrapper, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createPhieuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel searchCard = NhanVienUiHelper.createContentCard();
        searchCard.setLayout(new BorderLayout(15, 0));
        searchCard.setPreferredSize(new Dimension(0, 75));

        txtSearchPhieu = new JTextField();
        txtSearchPhieu.putClientProperty("JTextField.placeholderText", "Nhập mã NCC để xem phiếu nhập...");
        JButton btnSearch = NhanVienUiHelper.createPrimaryButton("Xem phiếu nhập");
        btnSearch.addActionListener(e -> loadPhieuData());

        searchCard.add(txtSearchPhieu, BorderLayout.CENTER);
        searchCard.add(btnSearch, BorderLayout.EAST);

        JPanel tableCard = NhanVienUiHelper.createContentCard();
        String[] cols = {"Mã phiếu", "Ngày nhập", "Nhân viên", "Tổng tiền"};
        modelPhieu = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablePhieu = new JTable(modelPhieu);
        NhanVienUiHelper.styleTable(tablePhieu);
        tableCard.add(new JScrollPane(tablePhieu), BorderLayout.CENTER);

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

    public void showThemNCC() { cardLayout.show(container, "them"); }
    public void showTraCuuNCC() { cardLayout.show(container, "traCuu"); loadDataToTable(); }
    public void showCapNhatNCC() { cardLayout.show(container, "capNhat"); }
    public void showPhieuNhap() { cardLayout.show(container, "phieu"); }

    private void loadPhieuData() {
        String maNCC = txtSearchPhieu.getText().trim();
        new SwingWorker<List<Object[]>, Void>() {
            @Override protected List<Object[]> doInBackground() {
                return nhaCungCapDAO.getPhieuNhapByNCC(maNCC);
            }
            @Override protected void done() {
                try {
                    modelPhieu.setRowCount(0);
                    for (Object[] r : get()) modelPhieu.addRow(r);
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }


    private void loadDataToTable() {
        String keyword = txtSearch.getText().trim();
        new SwingWorker<List<NhaCungCap>, Void>() {
            @Override protected List<NhaCungCap> doInBackground() {
                if (keyword.isEmpty()) return nhaCungCapDAO.timTatCa();
                return nhaCungCapDAO.timTheoTuKhoa(keyword);
            }
            @Override protected void done() {
                try {
                    modelTraCuu.setRowCount(0);
                    for (NhaCungCap ncc : get()) {
                        modelTraCuu.addRow(new Object[]{ncc.getMaNCC(), ncc.getTenNCC(), ncc.getSdt(), ncc.getDiaChi(), ncc.getEmail()});
                    }
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }

    private void handleThemNCC() {
        String ma = txtThemMa.getText().trim();
        String ten = txtThemTen.getText().trim();
        String sdt = txtThemSdt.getText().trim();
        String diaChi = txtThemDiaChi.getText().trim();
        String email = txtThemEmail.getText().trim();

        if (ma.isEmpty() || ten.isEmpty()) { JOptionPane.showMessageDialog(this, "Mã và tên không được để trống!"); return; }
        if (nhaCungCapDAO.themNhaCungCap(new NhaCungCap(ma, ten, sdt, diaChi, email))) {
            JOptionPane.showMessageDialog(this, "Thêm thành công!"); showTraCuuNCC();
        } else { JOptionPane.showMessageDialog(this, "Mã nhà cung cấp đã tồn tại!"); }
    }

    private void handleTimDeCapNhat() {
        NhaCungCap ncc = nhaCungCapDAO.timTheoMa(txtCapNhatMa.getText().trim());
        if (ncc != null) {
            txtCapNhatTen.setText(ncc.getTenNCC()); txtCapNhatSdt.setText(ncc.getSdt());
            txtCapNhatDiaChi.setText(ncc.getDiaChi()); txtCapNhatEmail.setText(ncc.getEmail());
        } else { JOptionPane.showMessageDialog(this, "Không tìm thấy!"); }
    }

    private void handleCapNhatNCC() {
        if (nhaCungCapDAO.capNhatNhaCungCap(new NhaCungCap(txtCapNhatMa.getText().trim(), txtCapNhatTen.getText().trim(), 
                txtCapNhatSdt.getText().trim(), txtCapNhatDiaChi.getText().trim(), txtCapNhatEmail.getText().trim()))) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!"); showTraCuuNCC();
        } else { JOptionPane.showMessageDialog(this, "Lỗi cập nhật!"); }
    }
}
