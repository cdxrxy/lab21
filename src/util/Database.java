package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private final String url;
    private final String user;
    private final String password;
    private static Database database;

    private Database(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public static Database getDB(String url, String user, String password) {
        if(database == null) {
            database = new Database(url, user, password);
        }

        return database;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
