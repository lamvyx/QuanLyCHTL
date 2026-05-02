package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

final class PieChartPanel extends JPanel {
    private final JLabel titleLabel = new JLabel("", SwingConstants.LEFT);
    private final ChartSurface chartSurface = new ChartSurface();

    PieChartPanel() {
        setLayout(new BorderLayout(0, 8));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(12, 12, 12, 12));

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(NhanVienUiHelper.NAVY);
        add(titleLabel, BorderLayout.NORTH);
        add(chartSurface, BorderLayout.CENTER);
    }

    void setChart(String title, List<String> labels, List<Double> values) {
        titleLabel.setText(title);
        chartSurface.setData(labels, values);
    }

    private static final class ChartSurface extends JPanel {
        private List<String> labels = new ArrayList<>();
        private List<Double> values = new ArrayList<>();
        private final Color[] palette = new Color[] {
                new Color(37, 99, 235), new Color(20, 184, 166), new Color(245, 158, 11),
                new Color(239, 68, 68), new Color(168, 85, 247), new Color(16, 185, 129)
        };

        ChartSurface() {
            setPreferredSize(new Dimension(420, 260));
            setBackground(Color.WHITE);
        }

        void setData(List<String> labels, List<Double> values) {
            this.labels = labels != null ? new ArrayList<>(labels) : new ArrayList<>();
            this.values = values != null ? new ArrayList<>(values) : new ArrayList<>();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            try {
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int pieSize = Math.min(width, height) - 70;
                pieSize = Math.max(140, pieSize);
                int pieX = 20;
                int pieY = 20;

                if (values.isEmpty()) {
                    g2.setColor(new Color(107, 114, 128));
                    g2.drawString("Không có dữ liệu", width / 2 - 40, height / 2);
                    return;
                }

                double total = values.stream().mapToDouble(v -> v != null ? v : 0.0).sum();
                if (total <= 0) {
                    g2.setColor(new Color(107, 114, 128));
                    g2.drawString("Không có dữ liệu", width / 2 - 40, height / 2);
                    return;
                }

                double startAngle = 0;
                for (int i = 0; i < values.size(); i++) {
                    double value = values.get(i) != null ? values.get(i) : 0.0;
                    double angle = (value / total) * 360.0;
                    g2.setColor(palette[i % palette.length]);
                    g2.fill(new Arc2D.Double(pieX, pieY, pieSize, pieSize, startAngle, angle, Arc2D.PIE));
                    startAngle += angle;
                }

                g2.setColor(new Color(229, 231, 235));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawOval(pieX, pieY, pieSize, pieSize);

                int legendX = pieX + pieSize + 16;
                int legendY = pieY + 10;
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                for (int i = 0; i < labels.size(); i++) {
                    g2.setColor(palette[i % palette.length]);
                    g2.fillRoundRect(legendX, legendY + i * 24 - 10, 12, 12, 4, 4);
                    g2.setColor(new Color(31, 41, 55));
                    String label = labels.get(i);
                    double value = values.get(i) != null ? values.get(i) : 0.0;
                    g2.drawString(label + " - " + String.format("%,.0f", value), legendX + 18, legendY + i * 24);
                }
            } finally {
                g2.dispose();
            }
        }
    }
}
