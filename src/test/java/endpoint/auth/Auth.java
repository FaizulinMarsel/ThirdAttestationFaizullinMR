package endpoint.auth;

import db.connection.ConnectionDataBase;
import db.helpers.users.UserAdmin;
import helpers.UrlHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;

public class Auth {
    UrlHelper urlHelper = new UrlHelper();
    static ConnectionDataBase connectionDataBase;
    UserAdmin userAdmin;
    AuthRequest authRequest;
    String adminLogin;

    public void connection() throws SQLException {
        connectionDataBase = new ConnectionDataBase();
        userAdmin = new UserAdmin(connectionDataBase.getConnection());
        authRequest = userAdmin.createUserAdmin();
        this.adminLogin = authRequest.login();
    }

    public String getAdminLogin() {
        return this.adminLogin;
    }

    public String authAndGetTokenAdmin() throws SQLException {
        connection();
        RestAssured.baseURI = urlHelper.getUrl();
        AuthResponse authResponse = given()
                .basePath("auth/login")
                .body(authRequest)
                .contentType(ContentType.JSON)
                .when()
                .post()
                .as(AuthResponse.class);
        return authResponse.userToken();
    }
}
