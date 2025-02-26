package db.connection;

import helpers.PropertiesUtil;

import java.util.Properties;

public enum LocalConfigConnection implements ConfigConnection {

    LOCAL_CONFIG_CONNECTION;

    private static final Properties propsConnect = PropertiesUtil.readProperties();

    @Override
    public String dbHost() {
        return propsConnect.getProperty("db.connectionHost");
    }

    @Override
    public String dbUserName() {
        return propsConnect.getProperty("db.login");
    }

    @Override
    public String dbUserPassword() {
        return propsConnect.getProperty("db.password");
    }
}
