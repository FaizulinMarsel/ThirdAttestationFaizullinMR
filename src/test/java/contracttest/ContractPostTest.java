package contracttest;

import db.connection.ConnectionDataBase;
import db.helpers.company.WorkCompanyDb;
import db.helpers.users.UserAdmin;
import endpoint.auth.Auth;
import endpoint.employee.request.post.ConfigEmployeePost;
import endpoint.employee.request.post.CreateEmployeeRequestPost;
import endpoint.employee.request.post.EmployeeRequestPost;
import helpers.IncorrectTestData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContractPostTest {
    String incorrectEmployeeEmail;
    String nonexistentUserToken;
    int nonexistentId;
    Response response;
    static String tokenAdmin;
    static String adminLogin;
    static int companyId;
    static String employeeEmail;
    static IncorrectTestData incorrectTestData;
    static ConnectionDataBase connectionDataBase;
    static UserAdmin userAdmin;
    static Auth auth;
    static WorkCompanyDb workCompanyDb;
    static CreateEmployeeRequestPost bodyRequest;

    @BeforeAll
    public static void setUp() throws SQLException {
        connectionDataBase = new ConnectionDataBase();
        workCompanyDb = new WorkCompanyDb(connectionDataBase.getConnection());
        userAdmin = new UserAdmin(connectionDataBase.getConnection());
        auth = new Auth();
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
    }

    @Test
    @DisplayName("Статус 201. Сотрудник добавлен")
    public void addEmployeeStatusOk() {
        response.then().statusCode(201);
    }

    @Test
    @DisplayName("Статус 401. Передача неверного токена")
    public void nonexistentUserToken() {
        nonexistentUserToken = incorrectTestData.getIncorrectUserToken();
        Response response = sendPostRequest(bodyRequest, nonexistentUserToken);
        response.then().statusCode(401);
    }

    @Test
    @DisplayName("Статус 400. Неверный e-mail")
    public void nonexistentEmail() {
        incorrectEmployeeEmail = incorrectTestData.getIncorrectEmail();
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(companyId, incorrectEmployeeEmail);
        Response response = sendPostRequest(bodyRequest, tokenAdmin);
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Статус 400. Сломанный JSON")
    public void brokenJson() {
        incorrectEmployeeEmail = incorrectTestData.getBrokenJson();
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(companyId, incorrectEmployeeEmail);
        Response response = sendPostRequest(bodyRequest, tokenAdmin);
        response.then().statusCode(400);
    }

    @Test
    @DisplayName("Статус 500. Передача несуществующего id компании")
    public void nonexistentCompanyId() {
        nonexistentId = incorrectTestData.getIncorrectCompanyId();
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(nonexistentId, employeeEmail);
        Response response = sendPostRequest(bodyRequest, tokenAdmin);
        response.then().statusCode(500);
    }

    @Test
    @DisplayName("Проверяем, что в ответе приходит JSON-файл")
    public void correctJson() {
        response.then().contentType(ContentType.JSON);
    }

    @Test
    @DisplayName("Проверяем формат JSON-файла")
    public void checkFormatJson() {
        String responseBody = response.getBody().asString();
        assertTrue(responseBody.startsWith("{"));
        assertTrue(responseBody.endsWith("}"));
    }

    @Test
    @DisplayName("Проверяем поля JSON-файла")
    public void checkFiledJsonFile() {
        assertNotNull(response.jsonPath().get("id"));
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
}
