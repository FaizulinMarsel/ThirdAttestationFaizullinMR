package endpoint.employee.request.path;

public interface ConfigEmployeePath {
    static ConfigEmployeePath getInstance() {
        return LocalConfigEmployeePath.LOCAL_CONFIG_EMPLOYEE_PATH;
    }

    String getLastName();

    String getEmail();

    String getUrl();

    String getPhone();

    boolean isActive();
}
