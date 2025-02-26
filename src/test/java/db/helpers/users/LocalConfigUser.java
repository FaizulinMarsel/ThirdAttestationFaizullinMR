package db.helpers.users;

import endpoint.main.helpers.PropertiesUtil;

import java.util.Properties;

public enum LocalConfigUser implements ConfigUser {
    LOCAL_CONFIG_USER;
    private static final Properties propsValue = PropertiesUtil.readProperties();

    @Override
    public boolean getAdminIsActive() {
        return Boolean.parseBoolean(propsValue.getProperty("admin.isActive"));
    }

    @Override
    public String getAdminLogin() {
        return propsValue.getProperty("admin.login");
    }

    @Override
    public String getAdminPassword() {
        return propsValue.getProperty("admin.password");
    }

    @Override
    public String getAdminDisplayName() {
        return propsValue.getProperty("admin.displayName");
    }

    @Override
    public String getAdminRole() {
        return propsValue.getProperty("admin.role");
    }
}
