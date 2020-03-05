package pt.isel.ls.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String url;
    private boolean isValid;

    public Database(String host, String port, String username, String password, String database) {
        this.url = "jdbc:postgresql://" + host + ":" + port + "/"
                + database + "?user=" + username + "&password=" + password;

        this.isValid = testConnection();
    }

    public Connection getNewConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private boolean testConnection() {
        try (Connection conn = DriverManager.getConnection(url)) {
            return conn.isValid(3000);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isValid() {
        return isValid;
    }

}
