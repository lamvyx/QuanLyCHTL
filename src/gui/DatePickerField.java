package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

final class DatePickerField extends JPanel {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final JTextField textField = new JTextField();
    private final JButton btnPick = new JButton("...");
    private LocalDate value = LocalDate.now();

    DatePickerField() {
        setLayout(new BorderLayout(6, 0));
        textField.setEditable(false);

        btnPick.addActionListener(e -> {
            LocalDate selected = DatePickerDialog.pickDate(this, value);
            if (selected != null) {
                setDate(selected);
            }
        });

        add(textField, BorderLayout.CENTER);
        add(btnPick, BorderLayout.EAST);
        setDate(LocalDate.now());
    }

    LocalDate getDate() {
        return value;
    }

    void setDate(LocalDate date) {
        value = date != null ? date : LocalDate.now();
        textField.setText(value.format(FORMATTER));
    }

    private static final class DatePickerDialog extends JDialog {
        private final JComboBox<String> cboMonth = new JComboBox<>();
        private final JComboBox<Integer> cboYear = new JComboBox<>();
        private final JButton[] dayButtons = new JButton[42];
        private boolean syncingControls;

        private LocalDate selectedDate;
        private YearMonth currentMonth;

        private DatePickerDialog(java.awt.Window owner, LocalDate initialDate) {
            super(owner, "Chọn ngày", ModalityType.APPLICATION_MODAL);
            this.selectedDate = initialDate != null ? initialDate : LocalDate.now();
            this.currentMonth = YearMonth.from(this.selectedDate);

            setLayout(new BorderLayout(8, 8));
            ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

            add(createHeader(), BorderLayout.NORTH);
            add(createCalendarGrid(), BorderLayout.CENTER);
            add(createFooter(), BorderLayout.SOUTH);

            refreshCalendar();
            pack();
            setResizable(false);
            setLocationRelativeTo(owner);
        }

        private JPanel createHeader() {
            JPanel header = new JPanel(new BorderLayout(6, 0));
            JButton btnPrev = new JButton("<");
            JButton btnNext = new JButton(">");
            JPanel selectors = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));

            String[] monthNames = {
                    "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
                    "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
            };
            for (String month : monthNames) {
                cboMonth.addItem(month);
            }

            int currentYear = LocalDate.now().getYear();
            for (int year = currentYear - 100; year <= currentYear + 20; year++) {
                cboYear.addItem(year);
            }

            cboMonth.addActionListener(e -> onMonthYearChanged());
            cboYear.addActionListener(e -> onMonthYearChanged());

            selectors.add(cboMonth);
            selectors.add(cboYear);

            btnPrev.addActionListener(e -> {
                currentMonth = currentMonth.minusMonths(1);
                refreshCalendar();
            });
            btnNext.addActionListener(e -> {
                currentMonth = currentMonth.plusMonths(1);
                refreshCalendar();
            });

            header.add(btnPrev, BorderLayout.WEST);
            header.add(selectors, BorderLayout.CENTER);
            header.add(btnNext, BorderLayout.EAST);
            return header;
        }

        private void onMonthYearChanged() {
            if (syncingControls) {
                return;
            }
            int month = cboMonth.getSelectedIndex() + 1;
            Object yearObj = cboYear.getSelectedItem();
            if (month <= 0 || yearObj == null) {
                return;
            }
            int year = (Integer) yearObj;
            currentMonth = YearMonth.of(year, month);

            if (selectedDate.getYear() != year || selectedDate.getMonthValue() != month) {
                int day = Math.min(selectedDate.getDayOfMonth(), currentMonth.lengthOfMonth());
                selectedDate = currentMonth.atDay(day);
            }
            refreshCalendar();
        }

        private JPanel createCalendarGrid() {
            JPanel panel = new JPanel(new GridLayout(7, 7, 4, 4));
            String[] weekdays = { "T2", "T3", "T4", "T5", "T6", "T7", "CN" };
            for (String day : weekdays) {
                JLabel label = new JLabel(day, SwingConstants.CENTER);
                panel.add(label);
            }

            for (int i = 0; i < dayButtons.length; i++) {
                JButton button = new JButton();
                button.setFocusPainted(false);
                final int index = i;
                button.addActionListener(e -> onSelectDate(index));
                dayButtons[i] = button;
                panel.add(button);
            }

            return panel;
        }

        private JPanel createFooter() {
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));

            JButton btnToday = new JButton("Hôm nay");
            btnToday.addActionListener(e -> {
                selectedDate = LocalDate.now();
                currentMonth = YearMonth.from(selectedDate);
                refreshCalendar();
            });

            JButton btnChoose = new JButton("Chọn");
            btnChoose.addActionListener(e -> dispose());

            footer.add(btnToday);
            footer.add(btnChoose);
            return footer;
        }

        private void onSelectDate(int index) {
            LocalDate firstDay = currentMonth.atDay(1);
            int offset = firstDay.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            LocalDate dateAtCell = firstDay.minusDays(offset).plusDays(index);
            selectedDate = dateAtCell;
            currentMonth = YearMonth.from(selectedDate);
            refreshCalendar();
        }

        private void refreshCalendar() {
            syncingControls = true;
            cboMonth.setSelectedIndex(currentMonth.getMonthValue() - 1);
            cboYear.setSelectedItem(currentMonth.getYear());
            syncingControls = false;

            LocalDate firstDay = currentMonth.atDay(1);
            int offset = firstDay.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            LocalDate cursor = firstDay.minusDays(offset);

            for (JButton button : dayButtons) {
                button.setText(String.valueOf(cursor.getDayOfMonth()));
                boolean inCurrentMonth = YearMonth.from(cursor).equals(currentMonth);
                button.setEnabled(inCurrentMonth);
                if (cursor.equals(selectedDate)) {
                    button.setBackground(new java.awt.Color(37, 99, 235));
                    button.setForeground(java.awt.Color.WHITE);
                } else {
                    button.setBackground(null);
                    button.setForeground(java.awt.Color.BLACK);
                }
                cursor = cursor.plusDays(1);
            }
        }

        private static LocalDate pickDate(java.awt.Component parent, LocalDate initialDate) {
            java.awt.Window owner = SwingUtilities.getWindowAncestor(parent);
            DatePickerDialog dialog = new DatePickerDialog(owner, initialDate);
            dialog.setVisible(true);
            return dialog.selectedDate;
        }
    }
}
