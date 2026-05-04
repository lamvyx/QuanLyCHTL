package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public final class NhanVienUiHelper {
    // --- Bảng màu Premium ---
    public static final Color NAVY = new Color(15, 23, 42);
    public static final Color PRIMARY = new Color(79, 70, 229);
    public static final Color PRIMARY_HOVER = new Color(67, 56, 202);
    public static final Color SECONDARY = new Color(241, 245, 249);
    public static final Color TEXT_MAIN = new Color(30, 41, 59);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color BG = new Color(248, 250, 252);
    public static final Color BORDER = new Color(226, 232, 240);

    // Khôi phục các hằng số màu cũ để tương thích ngược
    public static final Color BLUE = PRIMARY;
    public static final Color TEAL = new Color(20, 184, 166);
    public static final Color CARD = Color.WHITE;

    public static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private NhanVienUiHelper() {}

    // --- Các phương thức mới (Premium) ---

    public static JPanel createHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
        header.setPreferredSize(new Dimension(0, 100));

        JPanel textWrapper = new JPanel(new GridLayout(2, 1, 0, 5));
        textWrapper.setOpaque(false);
        textWrapper.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(NAVY);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(TEXT_MUTED);

        textWrapper.add(lblTitle);
        textWrapper.add(lblSub);

        header.add(textWrapper, BorderLayout.WEST);
        return header;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(238, 242, 255));
        table.setSelectionForeground(PRIMARY);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        table.getTableHeader().setPreferredSize(new Dimension(0, 45));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(SECONDARY);
        table.getTableHeader().setForeground(TEXT_MAIN);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));
    }

    public static JButton createPrimaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.BLACK); // Thay đổi thành màu đen theo yêu cầu
        btn.setBackground(PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(PRIMARY_HOVER); }
            public void mouseExited(MouseEvent e) { btn.setBackground(PRIMARY); }
        });
        return btn;
    }

    public static JButton createSecondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(TEXT_MAIN);
        btn.setBackground(SECONDARY);
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(BORDER, 1));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(226, 232, 240)); }
            public void mouseExited(MouseEvent e) { btn.setBackground(SECONDARY); }
        });
        return btn;
    }

    public static JLabel createSectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(NAVY);
        lbl.setBorder(new EmptyBorder(10, 0, 15, 0));
        return lbl;
    }

    public static JPanel createContentCard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        return panel;
    }

    // --- KHÔI PHỤC CÁC PHƯƠNG THỨC CŨ ĐỂ TƯƠNG THÍCH ---

    static JPanel createCardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }

    static JLabel createTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setForeground(NAVY);
        return label;
    }

    static void styleButton(JButton button, Color background) {
        button.setBackground(background);
        button.setForeground(Color.BLACK); // Luôn sử dụng chữ màu đen theo yêu cầu người dùng
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(9, 14, 9, 14));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    static void styleGhostButton(JButton button) {
        button.setBackground(new Color(229, 231, 235));
        button.setForeground(new Color(31, 41, 55));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    static void addRow(JPanel panel, int row, String label, JComponent component) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
        panel.add(component, gbc);
    }

    static DatePickerField createDatePicker() {
        return new DatePickerField();
    }

    static LocalDate getDatePickerValue(DatePickerField picker) {
        return picker.getDate();
    }

    static void setDatePickerValue(DatePickerField picker, LocalDate date) {
        picker.setDate(date);
    }

    static String formatDate(LocalDate date) {
        return date == null ? "" : date.format(UI_DATE_FORMATTER);
    }

    static Integer parseIntegerNullable(String text, String fieldName) {
        if (isBlank(text)) return null;
        try {
            return Integer.valueOf(text.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(fieldName + " phải là số nguyên.");
        }
    }

    static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    static String textOf(Object value) {
        return value == null ? "" : String.valueOf(value);
    }

    public static void addPlaceholder(javax.swing.JTextField textField, String placeholder) {
        textField.setText(placeholder);
        textField.setForeground(TEXT_MUTED);

        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(TEXT_MAIN);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setText(placeholder);
                    textField.setForeground(TEXT_MUTED);
                }
            }
        });
    }
}