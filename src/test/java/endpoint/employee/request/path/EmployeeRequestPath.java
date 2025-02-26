package endpoint.employee.request.path;

public class EmployeeRequestPath {
    static ConfigEmployeePath config;

    public CreateEmployeeRequestPath createRequestPath(String lastName, String email, String url, String phone, boolean isActive) {
        config = ConfigEmployeePath.getInstance();
        return new CreateEmployeeRequestPath(lastName, email, url, phone, isActive);
    }
}
