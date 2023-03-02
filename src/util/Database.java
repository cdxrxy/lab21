package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if(connection == null || connection.isClosed()) {
            String url = "jdbc:postgresql://localhost:5432/code21";
            String user = "root";
            String password = "Qwerty1";
            connection = DriverManager.getConnection(url, user, password);
            return connection;
        }

        return connection;
    }
}
