package db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDataBase {
    public Connection getConnection() throws SQLException {
        String connectionString = ConfigConnection.getInstance().dbHost();
        String login = ConfigConnection.getInstance().dbUserName();
        String password = ConfigConnection.getInstance().dbUserPassword();

        return DriverManager.getConnection(connectionString, login, password);
    }
}
