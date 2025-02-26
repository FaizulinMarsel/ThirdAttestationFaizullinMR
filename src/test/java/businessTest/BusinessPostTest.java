package businessTest;

import db.connection.ConnectionDataBase;
import db.helpers.company.WorkCompanyDb;
import db.helpers.users.UserAdmin;
import endpoint.auth.Auth;
import endpoint.employee.request.post.ConfigEmployeePost;
import endpoint.employee.request.post.CreateEmployeeRequestPost;
import endpoint.employee.request.post.EmployeeRequestPost;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusinessPostTest {
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

    @BeforeAll
    public static void setUp() throws SQLException {
        connectionDataBase = new ConnectionDataBase();
        workCompanyDb = new WorkCompanyDb(connectionDataBase.getConnection());
        userAdmin = new UserAdmin(connectionDataBase.getConnection());
        auth = new Auth();

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
    @DisplayName("Проверяем, что возвращенный id > 0")
    public void addEmployeeCheckId() {
        assertTrue(response.jsonPath().getInt("id") > 0);
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
