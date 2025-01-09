import java.sql.*;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/space_invaders";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Replace with your DB password

    public static void saveScore(int score) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "INSERT INTO scores (score) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, score);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getHighScore() {
        int highScore = 0;
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "SELECT MAX(score) AS highscore FROM scores";
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                if (rs.next()) {
                    highScore = rs.getInt("highscore");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScore;
    }
 
    public static void createTable() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "CREATE TABLE IF NOT EXISTS scores ("
                         + "id INT AUTO_INCREMENT PRIMARY KEY, "
                         + "score INT)";
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
