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
    private static final String CARD_BAN_CHAY = "banChay";
    private static final String CARD_HOA_DON = "hoaDon";
    private static final String CARD_PHIEU_NHAP = "phieuNhap";

    private final java.awt.CardLayout cardLayout = new java.awt.CardLayout();
    private final JPanel cardContainer = new JPanel(cardLayout);
    private final ThongKeDAO thongKeDAO = new ThongKeDAO();
    private final BarChartPanel barDoanhThuNgay = new BarChartPanel();
    private final BarChartPanel barDoanhThuThangChart = new BarChartPanel();
    private final PieChartPanel pieBanChay = new PieChartPanel();

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
        center.add(cardContainer, BorderLayout.CENTER);

        body.add(center, BorderLayout.CENTER);

        add(body, BorderLayout.CENTER);

        cardContainer.add(taoCardBaoCao("Doanh thu theo ngày", "Theo dõi doanh thu và số hóa đơn theo ngày", tblDoanhThuNgay, barDoanhThuNgay, this::taiDoanhThuNgayAsync), CARD_DOANH_THU_NGAY);
        cardContainer.add(taoCardBaoCao("Doanh thu theo tháng", "Tổng hợp doanh thu theo từng tháng", tblDoanhThuThang, barDoanhThuThangChart, this::taiDoanhThuThangAsync), CARD_DOANH_THU_THANG);
        cardContainer.add(taoCardBaoCao("Sản phẩm bán chạy", "Top sản phẩm theo số lượng bán", tblBanChay, pieBanChay, this::taiBanChayAsync), CARD_BAN_CHAY);
        cardContainer.add(taoCardBaoCao("Hóa đơn bán", "Danh sách hóa đơn bán và tổng tiền", tblHoaDon, null, this::taiHoaDonAsync), CARD_HOA_DON);
        cardContainer.add(taoCardBaoCao("Phiếu nhập", "Danh sách phiếu nhập và giá trị nhập", tblPhieuNhap, null, this::taiPhieuNhapAsync), CARD_PHIEU_NHAP);

        cardLayout.show(cardContainer, CARD_DOANH_THU_NGAY);
        taiDoanhThuNgayAsync();
    }

    public void showDoanhThuNgay() {
        cardLayout.show(cardContainer, CARD_DOANH_THU_NGAY);
        taiDoanhThuNgayAsync();
    }

    public void showDoanhThuThang() {
        cardLayout.show(cardContainer, CARD_DOANH_THU_THANG);
        taiDoanhThuThangAsync();
    }

    public void showSanPhamBanChay() {
        cardLayout.show(cardContainer, CARD_BAN_CHAY);
        taiBanChayAsync();
    }

    public void showHoaDonBan() {
        cardLayout.show(cardContainer, CARD_HOA_DON);
        taiHoaDonAsync();
    }

    public void showPhieuNhap() {
        cardLayout.show(cardContainer, CARD_PHIEU_NHAP);
        taiPhieuNhapAsync();
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


    private JPanel taoCardBaoCao(String tieuDe, String moTa, JTable table, JPanel chart, Runnable reloadAction) {
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

        JPanel contentPanel = new JPanel(new BorderLayout(0, 10));
        contentPanel.setOpaque(false);
        if (chart != null) {
            chart.setPreferredSize(new java.awt.Dimension(0, 260));
            contentPanel.add(chart, BorderLayout.NORTH);
        }
        contentPanel.add(new JScrollPane(table), BorderLayout.CENTER);

        root.add(contentPanel, BorderLayout.CENTER);
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
                List<Object[]> rows = napBang(modelDoanhThuNgay, this);
                List<String> labels = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                int start = Math.max(0, rows.size() - 7);
                for (int i = start; i < rows.size(); i++) {
                    Object[] r = rows.get(i);
                    labels.add(NhanVienUiHelper.formatDate((LocalDate) r[0]));
                    values.add(toDouble(r[2]));
                }
                barDoanhThuNgay.setChart("Doanh thu 7 ngày gần nhất", labels, values);
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
                List<Object[]> rows = napBang(modelDoanhThuThang, this);
                List<String> labels = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                for (Object[] r : rows) {
                    labels.add(String.valueOf(r[0]));
                    values.add(toDouble(r[2]));
                }
                barDoanhThuThangChart.setChart("Doanh thu các tháng", labels, values);
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
                List<Object[]> rows = napBang(modelBanChay, this);
                List<String> labels = new ArrayList<>();
                List<Double> values = new ArrayList<>();
                int limit = Math.min(6, rows.size());
                for (int i = 0; i < limit; i++) {
                    Object[] r = rows.get(i);
                    String tenSP = String.valueOf(r[1]);
                    if (tenSP.length() > 15) tenSP = tenSP.substring(0, 15) + "...";
                    labels.add(tenSP);
                    values.add(toDouble(r[3]));
                }
                pieBanChay.setChart("Top 6 sản phẩm bán nhiều nhất", labels, values);
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

    private List<Object[]> napBang(DefaultTableModel model, SwingWorker<List<Object[]>, Void> worker) {
        try {
            List<Object[]> rows = worker.get();
            model.setRowCount(0);
            if (rows != null) {
                for (Object[] row : rows) {
                    model.addRow(formatRow(row));
                }
                return rows;
            }
        } catch (java.lang.InterruptedException ex) {
            Thread.currentThread().interrupt();
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thống kê: " + ex.getMessage());
        } catch (java.util.concurrent.ExecutionException ex) {
            JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu thống kê: " + ex.getMessage());
        }
        return new ArrayList<>();
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