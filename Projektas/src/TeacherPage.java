import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TeacherPage {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    public static void displayTeacherPage() {
        JFrame teacherFrame = new JFrame("Teacher Control Panel");
        teacherFrame.setSize(600, 400);
        teacherFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        teacherFrame.setLayout(new BorderLayout());

        teacherFrame.add(createGradePanel(), BorderLayout.CENTER);

        teacherFrame.setVisible(true);
    }

    private static JPanel createGradePanel() {
        JPanel gradePanel = new JPanel(new BorderLayout());
        JPanel selectionPanel = new JPanel(new GridLayout(0, 2));
        selectionPanel.add(new JLabel("Select Subject:"));
        JComboBox<String> subjectDropdown = new JComboBox<>();
        populateSubjectDropdown(subjectDropdown);
        selectionPanel.add(subjectDropdown);

        JPanel gradeEntryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradeEntryPanel.add(new JLabel("Enter/Edit Grade (0-10):"));
        JTextField gradeField = new JTextField(5);
        gradeEntryPanel.add(gradeField);

        JButton submitButton = new JButton("Submit Grade");
        JButton viewGradesButton = new JButton("View Grades");
        gradeEntryPanel.add(submitButton);
        gradeEntryPanel.add(viewGradesButton);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSubject = (String) subjectDropdown.getSelectedItem();
                String grade = gradeField.getText();
                submitGrade(selectedSubject, grade);
            }
        });

        viewGradesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSubject = (String) subjectDropdown.getSelectedItem();
                viewGrades(selectedSubject);
            }
        });

        gradePanel.add(selectionPanel, BorderLayout.NORTH);
        gradePanel.add(gradeEntryPanel, BorderLayout.CENTER);

        return gradePanel;
    }

    private static void populateSubjectDropdown(JComboBox<String> subjectDropdown) {
        String query = "SELECT DISTINCT subject_name FROM grades";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                subjectDropdown.addItem(rs.getString("subject_name"));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    private static void submitGrade(String subject, String grade) {

        try {
            int gradeValue = Integer.parseInt(grade);
            if (gradeValue < 0 || gradeValue > 10) {
                JOptionPane.showMessageDialog(null, "Grade must be between 0 and 10.");
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid grade format. Please enter a number.");
            return;
        }

        String query = "UPDATE grades SET grade = ? WHERE subject_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(grade));
            stmt.setString(2, subject);
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(null, "Grade updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Grade update failed.");
            }

        } catch (SQLException se) {
            se.printStackTrace();

        }
    }
            private static void viewGrades (String subject){
                String query = "SELECT grade FROM grades WHERE subject_name = ?";
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setString(1, subject);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            String grade = rs.getString("grade");
                            JOptionPane.showMessageDialog(null, "Grade for " + subject + ": " + grade);
                        } else {
                            JOptionPane.showMessageDialog(null, "No grade found for " + subject);
                        }
                    }
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
        }