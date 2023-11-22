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

public class GroupManager {
    private static final String URL = "jdbc:mysql://localhost:3306/sistema";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    public boolean addGroup(String groupName) {
        String query = "INSERT INTO groups (group_name) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, groupName);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    public boolean deleteGroup(String groupName) {
        String query = "DELETE FROM groups WHERE group_name = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, groupName);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException se) {
            se.printStackTrace();
            return false;
        }
    }
    public List<String> getGroupNames() {
        List<String> names = new ArrayList<>();
        String query = "SELECT group_name FROM groups";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("group_name"));
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return names;
    }
    public void updateGroupDropdown(JComboBox<String> groupDropdown) {
        groupDropdown.removeAllItems();
        List<String> groupNames = getGroupNames();
        for (String name : groupNames) {
            groupDropdown.addItem(name);
        }
    }
    public JPanel createAddGroupPanel() {
        JPanel addPanel = new JPanel(new GridLayout(0, 2));
        addPanel.add(new JLabel("Group Name:"));
        JTextField groupNameField = new JTextField(10);
        addPanel.add(groupNameField);

        JButton addButton = new JButton("Add Group");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String groupName = groupNameField.getText();
                if (addGroup(groupName)) {
                    JOptionPane.showMessageDialog(addPanel, "Group added successfully!" );
                } else {
                    JOptionPane.showMessageDialog(addPanel, "Failed to add group.");
                }
            }
        });
        addPanel.add(addButton);
        return addPanel;
    }
    public JPanel createDeleteGroupPanel() {
        JPanel deletePanel = new JPanel();
        JButton deleteButton = new JButton("Delete Group");

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteGroupDialog();
            }
        });

        deletePanel.add(deleteButton);
        return deletePanel;
    }
    private void showDeleteGroupDialog() {
        JComboBox<String> groupDropdown = new JComboBox<>();
        updateGroupDropdown(groupDropdown);

        int result = JOptionPane.showConfirmDialog(null, groupDropdown,
                "Select Group to Delete", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedGroupName = (String) groupDropdown.getSelectedItem();
            if (selectedGroupName != null && !selectedGroupName.isEmpty()) {
                if (deleteGroup(selectedGroupName)) {
                    JOptionPane.showMessageDialog(null, "Group deleted successfully!");
                    updateGroupDropdown(groupDropdown);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to delete group.");
                }
            }
        }
    }
}