import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean addStudent(String name, String surname) {
        String query = "INSERT INTO userstable (name, surname, access_level) VALUES (?, ?, 1)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, surname);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }

    public boolean deleteStudent(String name, String surname) {
        String query = "DELETE FROM userstable WHERE name = ? AND surname = ? AND access_level = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setString(2, surname);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }

    public List<String> getStudentNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT name, surname FROM userstable WHERE access_level = 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String fullName = rs.getString("name") + " " + rs.getString("surname");
                names.add(fullName);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return names;
    }

    public void updateStudentDropdown(JComboBox<String> studentDropdown) {
        studentDropdown.removeAllItems();
        List<String> studentNames = getStudentNames();
        for (String name : studentNames) {
            studentDropdown.addItem(name);
        }
    }

    public JPanel createAddStudentPanel() {
        JPanel addPanel = new JPanel(new GridLayout(0, 2));
        addPanel.add(new JLabel("Student Name:"));
        JTextField studentNameField = new JTextField(10);
        addPanel.add(studentNameField);

        addPanel.add(new JLabel("Student Surname:"));
        JTextField studentSurnameField = new JTextField(10);
        addPanel.add(studentSurnameField);

        JButton addButton = new JButton("Add Student");
        addButton.addActionListener(e -> {
            String name = studentNameField.getText();
            String surname = studentSurnameField.getText();
            if (addStudent(name, surname)) {
                JOptionPane.showMessageDialog(addPanel, "Student added successfully!");
            } else {
                JOptionPane.showMessageDialog(addPanel, "Failed to add student.");
            }
        });
        addPanel.add(addButton);
        return addPanel;
    }

    public JPanel createDeleteStudentPanel() {
        JPanel panel = new JPanel();
        JButton deleteButton = new JButton("Delete Student");

        deleteButton.addActionListener(e -> showDeleteStudentDialog());

        panel.add(deleteButton);
        return panel;
    }

    private void showDeleteStudentDialog() {
        JComboBox<String> studentDropdown = new JComboBox<>();
        updateStudentDropdown(studentDropdown);

        int result = JOptionPane.showConfirmDialog(null, studentDropdown,
                "Select Student to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedName = (String) studentDropdown.getSelectedItem();
            if (selectedName != null && !selectedName.isEmpty()) {
                String[] parts = selectedName.split(" ");
                String name = parts[0];
                String surname = parts.length > 1 ? parts[1] : "";
                if (deleteStudent(name, surname)) {
                    JOptionPane.showMessageDialog(null, "Student deleted successfully!");
                    updateStudentDropdown(studentDropdown);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete student.");
                }
            }
        }
    }
}