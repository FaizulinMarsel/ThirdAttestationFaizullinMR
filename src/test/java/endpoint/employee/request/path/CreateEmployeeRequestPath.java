package endpoint.employee.request.path;

public record CreateEmployeeRequestPath(
        String lastName,
        String email,
        String url,
        String phone,
        boolean isActive) {
}
