import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentPage {

    private static final String URL = "jdbc:mysql://localhost:3306/sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void displayStudentPage() {
        JFrame studentFrame = new JFrame("Student Grade View");
        studentFrame.setSize(600, 400);
        studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentFrame.setLayout(new BorderLayout());

        studentFrame.add(createGradesPanel(), BorderLayout.CENTER);

        studentFrame.setVisible(true);
    }

    private static JPanel createGradesPanel() {
        JPanel gradesPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Subject Name", "Grade"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        populateGradesTable(model);

        JScrollPane scrollPane = new JScrollPane(table);
        gradesPanel.add(scrollPane, BorderLayout.CENTER);

        return gradesPanel;
    }

    private static void populateGradesTable(DefaultTableModel model) {
        String query = "SELECT subject_name, grade FROM grades";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String gradeName = rs.getString("subject_name");
                String grade = rs.getString("grade");
                model.addRow(new Object[]{gradeName, grade});
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}