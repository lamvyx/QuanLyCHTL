package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

final class NhanVienUiHelper {
    static final Color NAVY = new Color(15, 23, 42);
    static final Color BLUE = new Color(37, 99, 235);
    static final Color TEAL = new Color(20, 184, 166);
    static final Color BG = new Color(248, 250, 252);
    static final Color CARD = Color.WHITE;
    static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private NhanVienUiHelper() {
    }

    static JPanel createCardPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD);
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
        button.setForeground(Color.BLACK);
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
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
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
        if (isBlank(text)) {
            return null;
        }
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
}