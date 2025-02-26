package contracttest;

import db.connection.ConnectionDataBase;
import db.helpers.company.WorkCompanyDb;
import db.helpers.users.UserAdmin;
import endpoint.auth.Auth;
import endpoint.employee.request.path.ConfigEmployeePath;
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

public class ContractGetTest {
    int incorrectEmployeeId;
    int incorrectCompanyId;
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
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(companyId, employeeEmail);
        System.out.println(bodyRequest);
        response = sendPostRequest(bodyRequest, tokenAdmin);
        System.out.println(response);
        employeeId = response.jsonPath().getInt("id");
    }

    @Test
    @DisplayName("Статус 200. Получение сотрудника по id")
    public void checkGetEmployeeId() {
        System.out.println(employeeId);
        sendGetRequestEmployeeId(employeeId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Статус 200. Несуществующий id")
    public void checkNonexistentEmployeeId() {
        incorrectEmployeeId = incorrectTestData.getIncorrectEmployeeId();
        System.out.println(incorrectEmployeeId);
        sendGetRequestEmployeeId(incorrectEmployeeId)
                .then()
                .statusCode(200)
                .body(equalTo(""));
    }

    @Test
    @DisplayName("Запрос по сотруднику. Проверяем, что в ответе приходит JSON-файл")
    public void checkGetJsonEmployeeId() {
        System.out.println(employeeId);
        String responseBody = sendGetRequestEmployeeId(employeeId)
                .then()
                .contentType(ContentType.JSON)
                .extract().asString();
        assertTrue(responseBody.startsWith("{"));
        assertTrue(responseBody.endsWith("}"));
    }

    @Test
    @DisplayName("Статус 200. Получить список сотрудников для компании")
    public void checkGetListEmployeeCompanyIdStatusOk() {
        System.out.println(companyId);
        sendGetRequestCompanyId(companyId)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("Статус 200. Несуществующий id")
    public void checkNonexistentCompanyId() {
        incorrectCompanyId = incorrectTestData.getIncorrectCompanyId();
        System.out.println(incorrectCompanyId);
        sendGetRequestCompanyId(incorrectCompanyId)
                .then()
                .statusCode(200)
                .body(equalTo(""));
    }

    @Disabled("Проблема с contentType. Тест работает не стабильно,иногда отрабатывает, иногда ошибку выдает")
    @Test
    @DisplayName("Запрос по компании. Проверяем, что в ответе приходит JSON-файл")
    public void checkGetJsonCompanyId() {
        System.out.println(companyId);
        String responseBody =
                sendGetRequestCompanyId(companyId)
                        .then()
                        .contentType(ContentType.JSON)
                        .extract().asString();
        assertTrue(responseBody.startsWith("{"));
        assertTrue(responseBody.endsWith("}"));
        System.out.println(responseBody);
    }

    @AfterAll
    public static void ternDown() throws SQLException {
        //workCompanyDb.deleteEmployeesByCompanyId();
        //workCompanyDb.deleteCompany();
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
