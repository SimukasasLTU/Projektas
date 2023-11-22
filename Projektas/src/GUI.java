import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {
    private static JTextField userText;
    private static JPasswordField passwordText;
    private static JLabel statusLabel;

    public void createAndShowGUI() {
        JFrame frame = new JFrame("Login Form");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 400, 300);
        panel.setBackground(new Color(141, 231, 138));

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(50, 50, 80, 25);
        panel.add(usernameLabel);

        userText = new JTextField();
        userText.setBounds(150, 50, 150, 25);
        panel.add(userText);

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(50, 100, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(150, 100, 150, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 80, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        statusLabel = new JLabel("");
        statusLabel.setBounds(50, 200, 250, 25);
        panel.add(statusLabel);

        frame.add(panel);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = userText.getText();
        String password = new String(passwordText.getPassword());
        String role = MyJDBC.checkLogin(username, password);

        if (role != null) {
            switch (role) {
                case "admin":
                    AdminPage.displayAdminPage();
                    break;
                case "student":
                    StudentPage.displayStudentPage();
                    break;
                case "teacher":
                    TeacherPage.displayTeacherPage();
                    break;
                default:
                    statusLabel.setText("Invalid role");
                    statusLabel.setForeground(Color.RED);
                    break;
            }
        } else {
            statusLabel.setText("Incorrect username or password");
            statusLabel.setForeground(Color.RED);
        }
    }
}