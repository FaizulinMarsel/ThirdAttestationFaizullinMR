package endpoint.employee.request.post;

import helpers.PropertiesUtil;

import java.util.Properties;

public enum LocalConfigEmployeePost implements ConfigEmployeePost {
    LOCAL_CONFIG_EMPLOYEE;
    private static final Properties propsEmployee = PropertiesUtil.readProperties();

    @Override
    public int getIdEmployee() {
        return Integer.parseInt(propsEmployee.getProperty("employee.id"));
    }

    @Override
    public String getFirstNameEmployee() {
        return propsEmployee.getProperty("employee.firstName");
    }

    @Override
    public String getLastNameEmployee() {
        return propsEmployee.getProperty("employee.lastName");
    }

    @Override
    public String getMiddleNameEmployee() {
        return propsEmployee.getProperty("employee.middleName");
    }

    @Override
    public String getEmailEmployee() {
        return propsEmployee.getProperty("employee.email");
    }

    @Override
    public String getUrlEmployee() {
        return propsEmployee.getProperty("employee.url");
    }

    @Override
    public String getPhoneEmployee() {
        return propsEmployee.getProperty("employee.phone");
    }

    @Override
    public String getBirthdateEmployee() {
        return propsEmployee.getProperty("employee.birthdate");
    }

    @Override
    public boolean isActiveEmployee() {
        return Boolean.parseBoolean(propsEmployee.getProperty("employee.isActive"));
    }
}
