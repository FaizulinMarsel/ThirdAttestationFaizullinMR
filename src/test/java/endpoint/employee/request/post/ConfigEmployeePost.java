package endpoint.employee.request.post;

public interface ConfigEmployeePost {
    static ConfigEmployeePost getInstance() {
        return LocalConfigEmployeePost.LOCAL_CONFIG_EMPLOYEE;
    }

    int getIdEmployee();

    String getFirstNameEmployee();

    String getLastNameEmployee();

    String getMiddleNameEmployee();

    String getEmailEmployee();

    String getUrlEmployee();

    String getPhoneEmployee();

    String getBirthdateEmployee();

    boolean isActiveEmployee();
}
