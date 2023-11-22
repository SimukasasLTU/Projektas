import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TeacherSubjectManager {
    private JComboBox<String> teacherDropdown;
    private JComboBox<String> subjectDropdown;
    private List<String> associations = new ArrayList<>();
    private JPanel associationPanel;

    public TeacherSubjectManager(JComboBox<String> teacherDropdown, JComboBox<String> subjectDropdown,
                                 TeacherManager teacherManager, SubjectManager subjectManager) {
        this.teacherDropdown = teacherDropdown;
        this.subjectDropdown = subjectDropdown;
        populateTeacherDropdown(teacherManager);
        populateSubjectDropdown(subjectManager);
        this.associationPanel = createAssociationPanel();
    }

    private void populateTeacherDropdown(TeacherManager teacherManager) {
        teacherDropdown.removeAllItems();
        List<String> teacherNames = teacherManager.getTeacherNames();
        for (String name : teacherNames) {
            teacherDropdown.addItem(name);
        }
    }

    private void populateSubjectDropdown(SubjectManager subjectManager) {
        subjectDropdown.removeAllItems();
        List<String> subjectNames = subjectManager.getSubjectNames();
        for (String name : subjectNames) {
            subjectDropdown.addItem(name);
        }
    }
    public JPanel getAssociationPanel() {
        return associationPanel;
    }

    private JPanel createAssociationPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton addAssociationButton = new JButton("Add Association");
        JButton deleteAssociationButton = new JButton("Delete Association");
        JButton viewAssociationsButton = new JButton("View Associations");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addAssociationButton);
        buttonPanel.add(deleteAssociationButton);
        buttonPanel.add(viewAssociationsButton);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(2, 2));
        formPanel.add(new JLabel("Select Teacher:"));
        formPanel.add(teacherDropdown);
        formPanel.add(new JLabel("Select Subject:"));
        formPanel.add(subjectDropdown);

        JTextArea associationsText = new JTextArea(10, 30);
        associationsText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(associationsText);

        addAssociationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String teacher = (String) teacherDropdown.getSelectedItem();
                String subject = (String) subjectDropdown.getSelectedItem();

                if (teacher != null && subject != null) {
                    String association = teacher + " - " + subject;
                    associations.add(association);
                    JOptionPane.showMessageDialog(panel, "Association added successfully!");
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select both a teacher and a subject.");
                }
            }
        });

        deleteAssociationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAssociation = (String) JOptionPane.showInputDialog(panel,
                        "Select Association to Delete:", "Delete Association", JOptionPane.PLAIN_MESSAGE, null,
                        associations.toArray(), null);

                if (selectedAssociation != null) {
                    associations.remove(selectedAssociation);
                    JOptionPane.showMessageDialog(panel, "Association deleted successfully!");
                } else {
                    JOptionPane.showMessageDialog(panel, "Please select an association to delete.");
                }
            }
        });

        viewAssociationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder sb = new StringBuilder();
                for (String association : associations) {
                    sb.append(association).append("\n");
                }
                associationsText.setText(sb.toString());
            }
        });

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(scrollPane, BorderLayout.SOUTH);

        return panel;
    }
}