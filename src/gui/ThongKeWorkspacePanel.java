package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ThongKeDAO;

public class ThongKeWorkspacePanel extends JPanel {
    private static final String CARD_DOANH_THU_NGAY = "doanhThuNgay";
    private static final String CARD_DOANH_THU_THANG = "doanhThuThang";
    private static final String CARD_TON_KHO = "tonKho";
    private static final String CARD_BAN_CHAY = "banChay";
    private static final String CARD_HOA_DON = "hoaDon";
    private static final String CARD_PHIEU_NHAP = "phieuNhap";

    private final java.awt.CardLayout cardLayout = new java.awt.CardLayout();
    private final JPanel cardContainer = new JPanel(cardLayout);
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final BarChartPanel barDoanhThuThang = new BarChartPanel();
    private final PieChartPanel pieLoaiSanPham = new PieChartPanel();

    private final DefaultTableModel modelDoanhThuNgay = new DefaultTableModel(new Object[] {
            "Ngày", "Số hóa đơn", "Tổng doanh thu"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblDoanhThuNgay = new JTable(modelDoanhThuNgay);

    private final DefaultTableModel modelDoanhThuThang = new DefaultTableModel(new Object[] {
            "Tháng", "Số hóa đơn", "Tổng doanh thu"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblDoanhThuThang = new JTable(modelDoanhThuThang);

    private final DefaultTableModel modelTonKho = new DefaultTableModel(new Object[] {
            "Mã SP", "Tên SP", "Loại SP", "Tồn kho", "Giá", "Hạn sử dụng"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblTonKho = new JTable(modelTonKho);

    private final DefaultTableModel modelBanChay = new DefaultTableModel(new Object[] {
            "Mã SP", "Tên SP", "Loại SP", "Số lượng bán", "Doanh thu"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblBanChay = new JTable(modelBanChay);

    private final DefaultTableModel modelHoaDon = new DefaultTableModel(new Object[] {
            "Mã HD", "Ngày lập", "Mã NV", "Tên NV", "Mã KH", "Tên KH", "Số dòng SP", "Tổng tiền"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblHoaDon = new JTable(modelHoaDon);

    private final DefaultTableModel modelPhieuNhap = new DefaultTableModel(new Object[] {
            "Mã phiếu", "Ngày nhập", "Mã NV", "Tên NV", "Mã NCC", "Tên NCC", "Số dòng SP", "Tổng tiền"
    }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable tblPhieuNhap = new JTable(modelPhieuNhap);

    public ThongKeWorkspacePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(NhanVienUiHelper.BG);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.add(taoThanhTieuDe(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(taoDashboard(), BorderLayout.NORTH);
        center.add(cardContainer, BorderLayout.CENTER);

        body.add(center, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);

        cardContainer.add(taoCardBaoCao("Doanh thu theo ngày", "Theo dõi doanh thu và số hóa đơn theo ngày", tblDoanhThuNgay, this::taiDoanhThuNgayAsync), CARD_DOANH_THU_NGAY);
        cardContainer.add(taoCardBaoCao("Doanh thu theo tháng", "Tổng hợp doanh thu theo từng tháng", tblDoanhThuThang, this::taiDoanhThuThangAsync), CARD_DOANH_THU_THANG);
        cardContainer.add(taoCardBaoCao("Tồn kho", "Danh sách sản phẩm và số lượng tồn kho", tblTonKho, this::taiTonKhoAsync), CARD_TON_KHO);
        cardContainer.add(taoCardBaoCao("Sản phẩm bán chạy", "Top sản phẩm theo số lượng bán", tblBanChay, this::taiBanChayAsync), CARD_BAN_CHAY);
        cardContainer.add(taoCardBaoCao("Hóa đơn bán", "Danh sách hóa đơn bán và tổng tiền", tblHoaDon, this::taiHoaDonAsync), CARD_HOA_DON);
        cardContainer.add(taoCardBaoCao("Phiếu nhập", "Danh sách phiếu nhập và giá trị nhập", tblPhieuNhap, this::taiPhieuNhapAsync), CARD_PHIEU_NHAP);

        cardLayout.show(cardContainer, CARD_DOANH_THU_NGAY);
        taiDashboardAsync();
    }

    public void showDoanhThuNgay() {
        cardLayout.show(cardContainer, CARD_DOANH_THU_NGAY);
        taiDoanhThuNgayAsync();
        taiDashboardAsync();
    }

    public void showDoanhThuThang() {
        cardLayout.show(cardContainer, CARD_DOANH_THU_THANG);
        taiDoanhThuThangAsync();
        taiDashboardAsync();
    }

    public void showTonKho() {
        cardLayout.show(cardContainer, CARD_TON_KHO);
        taiTonKhoAsync();
        taiDashboardAsync();
    }

    public void showSanPhamBanChay() {
        cardLayout.show(cardContainer, CARD_BAN_CHAY);
        taiBanChayAsync();
        taiDashboardAsync();
    }

    public void showHoaDonBan() {
        cardLayout.show(cardContainer, CARD_HOA_DON);
        taiHoaDonAsync();
        taiDashboardAsync();
    }

    public void showPhieuNhap() {
        cardLayout.show(cardContainer, CARD_PHIEU_NHAP);
        taiPhieuNhapAsync();
        taiDashboardAsync();
    }

    private JPanel taoThanhTieuDe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel tieuDe = NhanVienUiHelper.createTitle("Thống kê");
        JLabel moTa = new JLabel("Các màn hình tổng hợp dữ liệu kinh doanh và vận hành");
        moTa.setForeground(new Color(75, 85, 99));

        JPanel left = new JPanel(new GridLayout(2, 1, 0, 2));
        left.setOpaque(false);
        left.add(tieuDe);
        left.add(moTa);

        panel.add(left, BorderLayout.WEST);
        return panel;
    }

    private JPanel taoDashboard() {
        JPanel dashboard = new JPanel(new GridLayout(1, 2, 10, 10));
        dashboard.setOpaque(false);
        dashboard.add(barDoanhThuThang);
        dashboard.add(pieLoaiSanPham);
        return dashboard;
    }

    private JPanel taoCardBaoCao(String tieuDe, String moTa, JTable table, Runnable reloadAction) {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(NhanVienUiHelper.BG);
        root.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel lblTieuDe = NhanVienUiHelper.createTitle(tieuDe);
        JLabel lblMoTa = new JLabel(moTa);
        lblMoTa.setForeground(new Color(75, 85, 99));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 2));
        header.setOpaque(false);
        header.add(lblTieuDe);
        header.add(lblMoTa);

        JButton btnTaiLai = new JButton("Tải lại");
        NhanVienUiHelper.styleGhostButton(btnTaiLai);
        btnTaiLai.addActionListener(e -> reloadAction.run());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        top.setOpaque(false);
        top.add(header);
        top.add(btnTaiLai);

        root.add(top, BorderLayout.NORTH);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        return root;
    }

    private void taiDoanhThuNgayAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                return thongKeDAO.doanhThuTheoNgay();
            }

            @Override
            protected void done() {
                napBang(modelDoanhThuNgay, this);
            }
        }.execute();
    }

    private void taiDoanhThuThangAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                return thongKeDAO.doanhThuTheoThang();
            }

            @Override
            protected void done() {
                napBang(modelDoanhThuThang, this);
            }
        }.execute();
    }

    private void taiTonKhoAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                return thongKeDAO.tonKho();
            }

            @Override
            protected void done() {
                napBang(modelTonKho, this);
            }
        }.execute();
    }

    private void taiBanChayAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                return thongKeDAO.sanPhamBanChay();
            }

            @Override
            protected void done() {
                napBang(modelBanChay, this);
            }
        }.execute();
    }

    private void taiHoaDonAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                return thongKeDAO.hoaDonBan();
            }

            @Override
            protected void done() {
                napBang(modelHoaDon, this);
            }
        }.execute();
    }

    private void taiPhieuNhapAsync() {
        new SwingWorker<List<Object[]>, Void>() {
            @Override
            protected List<Object[]> doInBackground() {
                return thongKeDAO.phieuNhap();
            }

            @Override
            protected void done() {
                napBang(modelPhieuNhap, this);
            }
        }.execute();
    }

    private void taiDashboardAsync() {
        new SwingWorker<Void, Void>() {
            private List<Object[]> doanhThuThang;
            private List<Object[]> soLuongTheoLoai;

            @Override
            protected Void doInBackground() {
                doanhThuThang = thongKeDAO.doanhThuTheoThang();
                soLuongTheoLoai = thongKeDAO.soLuongBanTheoLoaiSanPham();
                return null;
            }

            @Override
            protected void done() {
                capNhatDashboard(doanhThuThang, soLuongTheoLoai);
            }
        }.execute();
    }

    private void capNhatDashboard(List<Object[]> doanhThuThang, List<Object[]> soLuongTheoLoai) {
        List<String> labelsThang = new ArrayList<>();
        List<Double> valuesThang = new ArrayList<>();
        for (Object[] row : doanhThuThang) {
            labelsThang.add(String.valueOf(row[0]));
            valuesThang.add(toDouble(row[2]));
        }
        barDoanhThuThang.setChart("Doanh thu theo tháng", labelsThang, valuesThang);

        Map<String, Double> tongTheoLoai = new LinkedHashMap<>();
        for (Object[] row : soLuongTheoLoai) {
            String loai = String.valueOf(row[0]);
            double value = toDouble(row[1]);
            tongTheoLoai.merge(loai, value, Double::sum);
        }

        pieLoaiSanPham.setChart(
                "Tỷ trọng số lượng bán theo loại",
                new ArrayList<>(tongTheoLoai.keySet()),
                new ArrayList<>(tongTheoLoai.values())
        );
    }

    private double toDouble(Object value) {
        if (value instanceof Number number) {
            return number.doubleValue();
        }
        if (value == null) {
            return 0.0;
        }
        try {
            return Double.parseDouble(String.valueOf(value).replace(",", "").trim());
        } catch (NumberFormatException ex) {
            return 0.0;
        }
    }

    private void napBang(DefaultTableModel model, SwingWorker<List<Object[]>, Void> worker) {
        try {
            List<Object[]> rows = worker.get();
            model.setRowCount(0);
            for (Object[] row : rows) {
                model.addRow(formatRow(row));
            }
        } catch (java.lang.InterruptedException ex) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thống kê: " + ex.getMessage());
        } catch (java.util.concurrent.ExecutionException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thống kê: " + ex.getMessage());
        }
    }

    private Object[] formatRow(Object[] row) {
        Object[] formatted = new Object[row.length];
        for (int i = 0; i < row.length; i++) {
            Object value = row[i];
            if (value != null && value.getClass() == LocalDate.class) {
                formatted[i] = NhanVienUiHelper.formatDate((LocalDate) value);
            } else if (value instanceof Double number) {
                formatted[i] = String.format("%,.0f", number);
            } else {
                formatted[i] = value;
            }
        }
        return formatted;
    }
}