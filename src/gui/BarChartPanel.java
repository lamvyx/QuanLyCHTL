package gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

final class BarChartPanel extends JPanel {
    private final JLabel titleLabel = new JLabel("", SwingConstants.LEFT);
    private final ChartSurface chartSurface = new ChartSurface();

    BarChartPanel() {
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
                int left = 42;
                int top = 18;
                int bottom = 42;
                int chartWidth = width - left - 20;
                int chartHeight = height - top - bottom;

                g2.setColor(new Color(229, 231, 235));
                g2.drawLine(left, top + chartHeight, left + chartWidth, top + chartHeight);
                g2.drawLine(left, top, left, top + chartHeight);

                if (values.isEmpty()) {
                    g2.setColor(new Color(107, 114, 128));
                    g2.drawString("Không có dữ liệu", left + chartWidth / 2 - 40, top + chartHeight / 2);
                    return;
                }

                double max = values.stream().mapToDouble(v -> v != null ? v : 0.0).max().orElse(1.0);
                int barCount = Math.max(1, values.size());
                int gap = 10;
                int barWidth = Math.max(18, (chartWidth - (barCount + 1) * gap) / barCount);
                int availableHeight = chartHeight - 18;

                for (int i = 0; i < values.size(); i++) {
                    double value = values.get(i) != null ? values.get(i) : 0.0;
                    int barHeight = (int) Math.round((value / max) * availableHeight);
                    int x = left + gap + i * (barWidth + gap);
                    int y = top + chartHeight - barHeight;
                    Color color = palette[i % palette.length];
                    g2.setColor(color);
                    g2.fillRoundRect(x, y, barWidth, barHeight, 10, 10);
                    g2.setColor(color.darker());
                    g2.setStroke(new BasicStroke(1.2f));
                    g2.drawRoundRect(x, y, barWidth, barHeight, 10, 10);

                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    g2.setColor(new Color(31, 41, 55));
                    String valueText = String.format("%,.0f", value);
                    int valueWidth = g2.getFontMetrics().stringWidth(valueText);
                    g2.drawString(valueText, x + Math.max(0, (barWidth - valueWidth) / 2), y - 4);

                    String label = i < labels.size() ? labels.get(i) : "";
                    String shortLabel = label.length() > 10 ? label.substring(0, 10) + "..." : label;
                    int labelWidth = g2.getFontMetrics().stringWidth(shortLabel);
                    g2.drawString(shortLabel, x + Math.max(0, (barWidth - labelWidth) / 2), top + chartHeight + 18);
                }
            } finally {
                g2.dispose();
            }
        }
    }
}
