import java.sql.*;

public class DBManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/todo_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "rooot"; // replace with your password

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void insertTask(String taskText) {
        String query = "INSERT INTO tasks (task_text) VALUES (?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, taskText);
            stmt.executeUpdate();
            System.out.println("✅ Task inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void clearTasks() {
        String sql = "DELETE FROM tasks"; // or your actual table name
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("All tasks cleared from database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
