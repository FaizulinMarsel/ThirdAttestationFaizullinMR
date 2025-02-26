package db.connection;

public interface ConfigConnection {

    static ConfigConnection getInstance() {
        return LocalConfigConnection.LOCAL_CONFIG_CONNECTION;
    }

    String dbHost();

    String dbUserName();

    String dbUserPassword();

}
