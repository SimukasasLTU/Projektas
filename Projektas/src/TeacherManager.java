import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean addTeacher(String name, String surname) {
        String query = "INSERT INTO userstable (name, surname, access_level) VALUES (?, ?, 2)";
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

    public boolean deleteTeacher(String name, String surname) {
        String query = "DELETE FROM userstable WHERE name = ? AND surname = ? AND access_level = 2";
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

    public List<String> getTeacherNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT name, surname FROM userstable WHERE access_level = 2";
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

    public void updateTeacherDropdown(JComboBox<String> teacherDropdown) {
        teacherDropdown.removeAllItems();
        List<String> teacherNames = getTeacherNames();
        for (String name : teacherNames) {
            teacherDropdown.addItem(name);
        }
    }

    public JPanel createAddTeacherPanel() {
        JPanel addPanel = new JPanel(new GridLayout(0, 2));
        addPanel.add(new JLabel("Teacher Name:"));
        JTextField teacherNameField = new JTextField(10);
        addPanel.add(teacherNameField);

        addPanel.add(new JLabel("Teacher Surname:"));
        JTextField teacherSurnameField = new JTextField(10);
        addPanel.add(teacherSurnameField);

        JButton addButton = new JButton("Add Teacher");
        addButton.addActionListener(e -> {
            String name = teacherNameField.getText();
            String surname = teacherSurnameField.getText();
            if (addTeacher(name, surname)) {
                JOptionPane.showMessageDialog(addPanel, "Teacher added successfully!");
            } else {
                JOptionPane.showMessageDialog(addPanel, "Failed to add teacher.");
            }
        });
        addPanel.add(addButton);
        return addPanel;
    }

    public JPanel createDeleteTeacherPanel() {
        JPanel deletePanel = new JPanel();
        JButton deleteButton = new JButton("Delete Teacher");

        deleteButton.addActionListener(e -> showDeleteTeacherDialog());

        deletePanel.add(deleteButton);
        return deletePanel;
    }

    private void showDeleteTeacherDialog() {
        JComboBox<String> teacherDropdown = new JComboBox<>();
        updateTeacherDropdown(teacherDropdown);

        int result = JOptionPane.showConfirmDialog(null, teacherDropdown,
                "Select Teacher to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedName = (String) teacherDropdown.getSelectedItem();
            if (selectedName != null && !selectedName.isEmpty()) {
                String[] parts = selectedName.split(" ");
                String name = parts[0];
                String surname = parts.length > 1 ? parts[1] : "";
                if (deleteTeacher(name, surname)) {
                    JOptionPane.showMessageDialog(null, "Teacher deleted successfully!");
                    updateTeacherDropdown(teacherDropdown);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete teacher.");
                }
            }
        }
    }
}