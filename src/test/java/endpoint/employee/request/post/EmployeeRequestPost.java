package endpoint.employee.request.post;

public class EmployeeRequestPost {
    static ConfigEmployeePost config;

    public static CreateEmployeeRequestPost getEmployeeRequest(int companyId, String email) {
        config = ConfigEmployeePost.getInstance();
        return new CreateEmployeeRequestPost(
                config.getIdEmployee(),
                config.getFirstNameEmployee(),
                config.getLastNameEmployee(),
                config.getMiddleNameEmployee(),
                companyId,
                email,
                config.getUrlEmployee(),
                config.getPhoneEmployee(),
                config.getBirthdateEmployee(),
                config.isActiveEmployee()
        );
    }
}
