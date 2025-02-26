package businessTest;

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
import endpoint.main.helpers.GetIncorrectData;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class BusinessPathTest {
    boolean isActive;
    String phone;
    String url;
    String email;
    String lastName;
    private int idEmployee;
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
    }

    @BeforeEach
    public void prepareRequest() {
        bodyRequest = EmployeeRequestPost.getEmployeeRequest(companyId, employeeEmail);
        response = sendPostRequest(bodyRequest, tokenAdmin);

        idEmployee = response.jsonPath().get("id");

        lastName = config.getLastName();
        email = config.getEmail();
        url = config.getUrl();
        phone = config.getPhone();
        isActive = config.isActive();

        createEmployeeRequestPath = employeeRequestPath.createRequestPath(lastName, email, url, phone, isActive);
    }

    @Test
    @DisplayName("Валидация измененных полей")
    public void checkChangeEmployee() {
        sendPatchRequest(idEmployee, createEmployeeRequestPath, tokenAdmin)
                .then()
                .body("id", equalTo(idEmployee))
                .body("isActive", equalTo(isActive))
                .body("email", equalTo(email))
                .body("url", equalTo(url));
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
