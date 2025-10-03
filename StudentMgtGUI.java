import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentMgtGUI extends JFrame {
    private JTabbedPane tabs;
    private JTable studentTable;
    private JButton addBtn, updateBtn, deleteBtn, searchBtn;
    private DefaultTableModel stdTable;
    private JTextField name, age, per, enroll;
    private String filestd = "std.txt";

    StudentMgtGUI() {
        setTitle("Student Management System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Menu Bar =====
        JMenuBar mb = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        fileMenu.add(exit);
        mb.add(fileMenu);
        setJMenuBar(mb);

        // ===== Tabbed Pane =====
        tabs = new JTabbedPane();

        // ===== Panel for Students =====
        JPanel student = new JPanel(new BorderLayout());
        student.add(getForm(), BorderLayout.NORTH);

        stdTable = new DefaultTableModel(new String[]{"Enrollment", "Name", "Age", "Percentage"}, 0);
        studentTable = new JTable(stdTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        student.add(scrollPane, BorderLayout.CENTER);

        loadStd();

        tabs.add("Student Management", student);

        add(tabs);
    }

    // ===== Form for Adding Data + Buttons =====
    public JPanel getForm() {
        JPanel std = new JPanel(new GridLayout(3, 5, 5, 5));
        std.setBorder(BorderFactory.createTitledBorder("Manage Students"));

        enroll = new JTextField();
        name = new JTextField();
        age = new JTextField();
        per = new JTextField();

        addBtn = new JButton("Add");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete");
        searchBtn = new JButton("Search");

        addBtn.addActionListener(e -> addStd());
        updateBtn.addActionListener(e -> updateStd());
        deleteBtn.addActionListener(e -> deleteStd());
        searchBtn.addActionListener(e -> searchStd());

        std.add(new JLabel("Enrollment"));
        std.add(enroll);
        std.add(new JLabel("Name"));
        std.add(name);
        std.add(new JLabel("Age"));
        std.add(age);
        std.add(new JLabel("Percentage"));
        std.add(per);
        std.add(addBtn);
        std.add(updateBtn);
        std.add(deleteBtn);
        std.add(searchBtn);

        return std;
    }

    // ===== Add Student =====
    private void addStd() {
        String enrollTxt = enroll.getText();
        String nameTxt = name.getText();
        String ageTxt = age.getText();
        String perTxt = per.getText();

        if (enrollTxt.isEmpty() || nameTxt.isEmpty() || ageTxt.isEmpty() || perTxt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter all details!");
            return;
        }

        stdTable.addRow(new Object[]{enrollTxt, nameTxt, ageTxt, perTxt});
        saveStd();

        enroll.setText("");
        name.setText("");
        age.setText("");
        per.setText("");
    }

    // ===== Update Student =====
    private void updateStd() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to update");
            return;
        }
        stdTable.setValueAt(enroll.getText(), row, 0);
        stdTable.setValueAt(name.getText(), row, 1);
        stdTable.setValueAt(age.getText(), row, 2);
        stdTable.setValueAt(per.getText(), row, 3);
        saveStd();
    }

    // ===== Delete Student =====
    private void deleteStd() {
        int row = studentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete");
            return;
        }
        stdTable.removeRow(row);
        saveStd();
    }

    // ===== Search Student =====
    private void searchStd() {
        String search = JOptionPane.showInputDialog(this, "Enter Enrollment No:");
        if (search == null || search.isEmpty()) return;

        for (int i = 0; i < stdTable.getRowCount(); i++) {
            if (stdTable.getValueAt(i, 0).toString().equalsIgnoreCase(search)) {
                studentTable.setRowSelectionInterval(i, i);
                JOptionPane.showMessageDialog(this, "Student Found: " +
                        stdTable.getValueAt(i, 1));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Student not found!");
    }

    // ===== Load Students from File =====
    private void loadStd() {
        stdTable.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(filestd))) {
            String line;
            while ((line = br.readLine()) != null) {
                stdTable.addRow(line.split(","));
            }
        } catch (Exception ignored) {}
    }

    // ===== Save Students to File =====
    private void saveStd() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filestd))) {
            for (int i = 0; i < stdTable.getRowCount(); i++) {
                pw.println(stdTable.getValueAt(i, 0) + "," +
                        stdTable.getValueAt(i, 1) + "," +
                        stdTable.getValueAt(i, 2) + "," +
                        stdTable.getValueAt(i, 3));
            }
        } catch (Exception ignored) {}
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentMgtGUI().setVisible(true));
    }
}
