package businessTest;

import db.connection.ConnectionDataBase;
import db.helpers.company.WorkCompanyDb;
import db.helpers.users.UserAdmin;
import endpoint.auth.Auth;
import endpoint.employee.request.path.ConfigEmployeePath;
import endpoint.employee.request.path.EmployeeRequestPath;
import endpoint.employee.request.post.ConfigEmployeePost;
import endpoint.employee.request.post.CreateEmployeeRequestPost;
import endpoint.employee.request.post.EmployeeRequestPost;
import endpoint.main.helpers.GetIncorrectData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BusinessGetTest {
    static int employeeId;
    static Response response;
    static String tokenAdmin;
    static String adminLogin;
    private static int companyId;
    static String employeeEmail;
    static ConnectionDataBase connectionDataBase;
    static UserAdmin userAdmin;
    static Auth auth;
    static WorkCompanyDb workCompanyDb;
    static CreateEmployeeRequestPost bodyRequest;
    static ConfigEmployeePath config;
    static EmployeeRequestPath employeeRequestPath;
    static GetIncorrectData getIncorrectData;

    @BeforeAll
    public static void setUp() throws SQLException {
        connectionDataBase = new ConnectionDataBase();
        workCompanyDb = new WorkCompanyDb(connectionDataBase.getConnection());
        userAdmin = new UserAdmin(connectionDataBase.getConnection());
        auth = new Auth();
        employeeRequestPath = new EmployeeRequestPath();
        config = ConfigEmployeePath.getInstance();
        getIncorrectData = new GetIncorrectData();

        tokenAdmin = auth.authAndGetTokenAdmin();
        adminLogin = auth.getAdminLogin();
        companyId = workCompanyDb.createCompany();
        employeeEmail = ConfigEmployeePost.getInstance().getEmailEmployee();
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(companyId, employeeEmail);
        response = sendPostRequest(bodyRequest, tokenAdmin);
        employeeId = response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Валидация ответа запроса на получение сотрудника по id")
    public void checkValidateResponseGetEmployeeId() {
        sendGetRequestEmployeeId(employeeId)
                .then()
                .body("id", equalTo(employeeId))
                .body("isActive", equalTo(bodyRequest.isActive()))
                .body("firstName", equalTo(bodyRequest.firstName()))
                .body("lastName", equalTo(bodyRequest.lastName()))
                .body("middleName", equalTo(bodyRequest.middleName()))
                .body("phone", equalTo(bodyRequest.phone()))
                .body("email", equalTo(null))
                .body("birthdate", equalTo(bodyRequest.birthdate()))
                .body("avatar_url", equalTo(bodyRequest.url()))
                .body("companyId", equalTo(companyId));
    }

    @Disabled("Проблема с contentType. Тест работает не стабильно,иногда отрабатывает, иногда ошибку выдает")
    @Test
    @DisplayName("Валидация ответа запроса на получение cписка сотрудников по id компании")
    public void checkValidateResponseGetCompanyId() {
        sendGetRequestCompanyId(companyId)
                .then()
                .contentType(ContentType.JSON)
                .body("id", equalTo(employeeId))
                .body("isActive", equalTo(bodyRequest.isActive()))
                .body("firstName", equalTo(bodyRequest.firstName()))
                .body("lastName", equalTo(bodyRequest.lastName()))
                .body("middleName", equalTo(bodyRequest.middleName()))
                .body("phone", equalTo(bodyRequest.phone()))
                .body("email", equalTo(null))
                .body("birthdate", equalTo(bodyRequest.birthdate()))
                .body("avatar_url", equalTo(bodyRequest.url()))
                .body("companyId", equalTo(companyId));
    }

    @AfterAll
    public static void ternDown() throws SQLException {
        workCompanyDb.deleteEmployeesByCompanyId();
        workCompanyDb.deleteCompany();
        userAdmin.deleteUserAdmin(adminLogin);
    }

    private static Response sendPostRequest(Object bodyRequest, String token) {
        return given()
                .basePath("employee")
                .header("x-client-token", token)
                .body(bodyRequest)
                .contentType(ContentType.JSON)
                .when()
                .post();
    }

    private Response sendGetRequestCompanyId(int companyId) {
        return given()
                .basePath("employee/{id}")
                .pathParam("id", companyId)
                .when()
                .get();
    }

    private Response sendGetRequestEmployeeId(int employeeId) {
        return given()
                .basePath("employee/{id}")
                .pathParam("id", employeeId)
                .when()
                .get();
    }
}
