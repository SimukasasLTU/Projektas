import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SubjectManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public boolean addSubject(String subjectName) {
        String query = "INSERT INTO subjects (subject_name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, subjectName);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }

    public boolean deleteSubject(String subjectName) {
        String query = "DELETE FROM subjects WHERE subject_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, subjectName);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }

    public List<String> getSubjectNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT subject_name FROM subjects";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("subject_name"));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return names;
    }

    public void updateSubjectDropdown(JComboBox<String> subjectDropdown) {
        subjectDropdown.removeAllItems();
        List<String> subjectNames = getSubjectNames();
        for (String name : subjectNames) {
            subjectDropdown.addItem(name);
        }
    }

    public JPanel createAddSubjectPanel() {
        JPanel addPanel = new JPanel(new GridLayout(0, 2));
        addPanel.add(new JLabel("Subject Name:"));
        JTextField subjectNameField = new JTextField(10);
        addPanel.add(subjectNameField);

        JButton addButton = new JButton("Add Subject");
        addButton.addActionListener(e -> {
            String subjectName = subjectNameField.getText();
            if (addSubject(subjectName)) {
                JOptionPane.showMessageDialog(addPanel, "Subject added successfully!");
            } else {
                JOptionPane.showMessageDialog(addPanel, "Failed to add subject.");
            }
        });
        addPanel.add(addButton);
        return addPanel;
    }

    public JPanel createDeleteSubjectPanel() {
        JPanel deletePanel = new JPanel();
        JButton deleteButton = new JButton("Delete Subject");

        deleteButton.addActionListener(e -> showDeleteSubjectDialog());

        deletePanel.add(deleteButton);
        return deletePanel;
    }

    private void showDeleteSubjectDialog() {
        JComboBox<String> subjectDropdown = new JComboBox<>();
        updateSubjectDropdown(subjectDropdown);

        int result = JOptionPane.showConfirmDialog(null, subjectDropdown,
                "Select Subject to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedSubjectName = (String) subjectDropdown.getSelectedItem();
            if (selectedSubjectName != null && !selectedSubjectName.isEmpty()) {
                if (deleteSubject(selectedSubjectName)) {
                    JOptionPane.showMessageDialog(null, "Subject deleted successfully!");
                    updateSubjectDropdown(subjectDropdown);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete subject.");
                }
            }
        }
    }
}