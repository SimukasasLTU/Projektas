import javax.swing.*;
import java.awt.*;

public class AdminPage {
    private static final StudentManager studentManager = new StudentManager();
    private static final GroupManager groupManager = new GroupManager();
    private static final SubjectManager subjectManager = new SubjectManager();
    private static final TeacherManager teacherManager = new TeacherManager();
    private static TeacherSubjectManager teacherSubjectManager;

    public static void displayAdminPage() {
        JFrame adminFrame = new JFrame("Administratoriaus meniu");
        adminFrame.setSize(600, 400);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        adminFrame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Students", createStudentPanel());
        tabbedPane.addTab("Groups", createGroupPanel());
        tabbedPane.addTab("Subjects", createSubjectPanel());
        tabbedPane.addTab("Teachers", createTeacherPanel());
        tabbedPane.addTab("Teacher-Subject", createTeacherSubjectPanel());

        adminFrame.add(tabbedPane, BorderLayout.CENTER);
        adminFrame.setVisible(true);
    }

    private static JPanel createStudentPanel() {
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new BorderLayout());

        JPanel addPanel = studentManager.createAddStudentPanel();
        JPanel deletePanel = studentManager.createDeleteStudentPanel();

        studentPanel.add(addPanel, BorderLayout.NORTH);
        studentPanel.add(deletePanel, BorderLayout.CENTER);

        return studentPanel;
    }

    private static JPanel createGroupPanel() {
        JPanel groupPanel = new JPanel();
        groupPanel.setLayout(new BorderLayout());

        JPanel addPanel = groupManager.createAddGroupPanel();
        JPanel deletePanel = groupManager.createDeleteGroupPanel();

        groupPanel.add(addPanel, BorderLayout.NORTH);
        groupPanel.add(deletePanel, BorderLayout.CENTER);

        return groupPanel;
    }

    private static JPanel createSubjectPanel() {
        JPanel subjectPanel = new JPanel();
        subjectPanel.setLayout(new BorderLayout());

        JPanel addPanel = subjectManager.createAddSubjectPanel();
        JPanel deletePanel = subjectManager.createDeleteSubjectPanel();

        subjectPanel.add(addPanel, BorderLayout.NORTH);
        subjectPanel.add(deletePanel, BorderLayout.CENTER);

        return subjectPanel;
    }

    private static JPanel createTeacherPanel() {
        JPanel teacherPanel = new JPanel();
        teacherPanel.setLayout(new BorderLayout());

        JPanel addPanel = teacherManager.createAddTeacherPanel();
        JPanel deletePanel = teacherManager.createDeleteTeacherPanel();

        teacherPanel.add(addPanel, BorderLayout.NORTH);
        teacherPanel.add(deletePanel, BorderLayout.CENTER);

        return teacherPanel;
    }

    private static JPanel createTeacherSubjectPanel() {
        JComboBox<String> teacherDropdown = new JComboBox<>();
        JComboBox<String> subjectDropdown = new JComboBox<>();
        teacherSubjectManager = new TeacherSubjectManager(teacherDropdown, subjectDropdown, teacherManager, subjectManager);
        return teacherSubjectManager.getAssociationPanel();
    }
}