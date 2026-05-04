package connectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=QLCuaHang;encrypt=true;trustServerCertificate=true";
    private static final String USER = "sa";
    private static final String PASSWORD = "sapassword";

    private static ConnectDB instance;

    private ConnectDB() {
    }

    public static ConnectDB getInstance() {
        if (instance == null) {
            instance = new ConnectDB();
        }
        return instance;
    }

<<<<<<< HEAD
    public static Connection getConnection() throws SQLException {
=======
    public Connection getConnection() throws SQLException {
>>>>>>> dcf7c0fe2cf37d3adb70b3c0f09771caa7965032
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new IllegalStateException("Khong the dong ket noi CSDL", e);
            }
        }
    }
}
