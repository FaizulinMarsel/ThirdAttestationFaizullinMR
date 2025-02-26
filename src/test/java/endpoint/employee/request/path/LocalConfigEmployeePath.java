package endpoint.employee.request.path;


import endpoint.main.helpers.PropertiesUtil;

import java.util.Properties;

public enum LocalConfigEmployeePath implements ConfigEmployeePath {
    LOCAL_CONFIG_EMPLOYEE_PATH;
    private static final Properties propsEmployee = PropertiesUtil.readProperties();

    @Override
    public String getLastName() {
        return propsEmployee.getProperty("employee.change.lastName");
    }

    @Override
    public String getEmail() {
        return propsEmployee.getProperty("employee.change.email");
    }

    @Override
    public String getUrl() {
        return propsEmployee.getProperty("employee.change.url");
    }

    @Override
    public String getPhone() {
        return propsEmployee.getProperty("employee.change.phone");
    }

    @Override
    public boolean isActive() {
        return Boolean.parseBoolean(propsEmployee.getProperty("employee.change.isActive"));
    }
}
