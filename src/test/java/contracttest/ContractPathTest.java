package contracttest;

import db.connection.ConnectionDataBase;
import db.helpers.company.WorkCompanyDb;
import db.helpers.users.UserAdmin;
import endpoint.auth.Auth;
import endpoint.employee.request.path.ConfigEmployeePath;
import endpoint.employee.request.path.CreateEmployeeRequestPath;
import endpoint.employee.request.path.EmployeeRequestPath;
import endpoint.employee.request.post.ConfigEmployeePost;
import endpoint.employee.request.post.CreateEmployeeRequestPost;
import endpoint.employee.request.post.EmployeeRequestPost;
import helpers.IncorrectTestData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContractPathTest {

    int incorrectEmployeeId;
    boolean isActive;
    String phone;
    String url;
    String email;
    String lastName;
    int employeeId;
    String nonexistentUserToken;
    Response response;
    static String tokenAdmin;
    static String adminLogin;
    static int companyId;
    static String employeeEmail;
    static ConnectionDataBase connectionDataBase;
    static UserAdmin userAdmin;
    static Auth auth;
    static WorkCompanyDb workCompanyDb;
    static CreateEmployeeRequestPost bodyRequest;
    static CreateEmployeeRequestPath createEmployeeRequestPath;
    static ConfigEmployeePath config;
    static EmployeeRequestPath employeeRequestPath;
    static IncorrectTestData incorrectTestData;

    @BeforeAll
    public static void setUp() throws SQLException {
        connectionDataBase = new ConnectionDataBase();
        workCompanyDb = new WorkCompanyDb(connectionDataBase.getConnection());
        userAdmin = new UserAdmin(connectionDataBase.getConnection());
        auth = new Auth();
        employeeRequestPath = new EmployeeRequestPath();
        config = ConfigEmployeePath.getInstance();
        incorrectTestData = new IncorrectTestData();

        tokenAdmin = auth.authAndGetTokenAdmin();
        adminLogin = auth.getAdminLogin();
        companyId = workCompanyDb.createCompany();
        employeeEmail = ConfigEmployeePost.getInstance().getEmailEmployee();
    }

    @BeforeEach
    public void prepareRequest() {
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(companyId, employeeEmail);
        response = sendPostRequest(bodyRequest, tokenAdmin);

        employeeId = response.jsonPath().get("id");

        lastName = config.getLastName();
        email = config.getEmail();
        url = config.getUrl();
        phone = config.getPhone();
        isActive = config.isActive();

        createEmployeeRequestPath = employeeRequestPath.createRequestPath(lastName, email, url, phone, isActive);

    }

    @Test
    @DisplayName("Статус 200. Изменение данных сотрудника")
    public void checkChangeEmployeeStatusOk() {
        sendPatchRequest(employeeId, createEmployeeRequestPath, tokenAdmin)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверяем, что в ответе приходит JSON-файл")
    public void checkCorrectJson() {
        String responseBody = sendPatchRequest(employeeId, createEmployeeRequestPath, tokenAdmin)
                .then()
                .contentType(ContentType.JSON)
                .extract().asString();
        assertTrue(responseBody.startsWith("{"));
        assertTrue(responseBody.endsWith("}"));
    }

    @Test
    @DisplayName("Статус 500. Передан несуществующий id Employee")
    public void checkErrorStatusNonexistentIdEmployee() {
        incorrectEmployeeId = incorrectTestData.getIncorrectEmployeeId();
        sendPatchRequest(incorrectEmployeeId, createEmployeeRequestPath, tokenAdmin)
                .then()
                .statusCode(500)
                .body("message", equalTo("Internal server error"));
    }

    @Test
    @DisplayName("Статус 401. Неверный токен")
    public void checkNonexistentUserToken() {
        nonexistentUserToken = incorrectTestData.getIncorrectUserToken();
        sendPatchRequest(employeeId, createEmployeeRequestPath, nonexistentUserToken)
                .then()
                .statusCode(401);
    }

    @Test
    @DisplayName("Статус 400. Передан пустой lastName")
    public void checkNullLastName() {
        lastName = incorrectTestData.getIncorrectLastName();
        createEmployeeRequestPath = employeeRequestPath.createRequestPath(lastName, email, url, phone, isActive);
        sendPatchRequest(employeeId, createEmployeeRequestPath, tokenAdmin)
                .then()
                .statusCode(400);
    }

    @Test
    @DisplayName("Статус 400. Передан некорректный email")
    public void checkNonexistentEmailErrorStatus() {
        email = incorrectTestData.getIncorrectEmail();
        createEmployeeRequestPath = employeeRequestPath.createRequestPath(lastName, email, url, phone, isActive);
        sendPatchRequest(employeeId, createEmployeeRequestPath, tokenAdmin)
                .then()
                .statusCode(400);
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

    private Response sendPatchRequest(int employeeId, Object body, String token) {
        return given()
                .basePath("employee/{id}")
                .pathParam("id", employeeId)
                .header("x-client-token", token)
                .body(body)
                .contentType(ContentType.JSON)
                .when()
                .patch();
    }

}
