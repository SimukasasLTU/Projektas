import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MyJDBC {

        private static final String URL = "jdbc:mysql://localhost:3306/sistema";
        private static final String DB_USER = "root";
        private static final String DB_PASSWORD = "";

        public static String checkLogin(String username, String password) {
            String query = "SELECT access_level FROM userstable WHERE name = ? AND surname = ?";
            try (Connection conn = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                stmt.setString(1, username);
                stmt.setString(2, password);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int accessLevel = rs.getInt("access_level");
                        switch (accessLevel) {
                            case 1: return "student";
                            case 2: return "teacher";
                            default: return "admin";
                        }
                    }
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
            return null;
        }
    }