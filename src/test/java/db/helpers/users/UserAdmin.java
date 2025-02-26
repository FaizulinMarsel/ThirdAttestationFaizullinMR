package db.helpers.users;

import endpoint.auth.AuthRequest;

import java.sql.Connection;
import java.sql.SQLException;

public class UserAdmin extends UserService {

    AuthRequest authRequest;

    public UserAdmin(Connection connection) {
        super(connection);
    }

    public AuthRequest createUserAdmin() throws SQLException {
        authRequest = createUser(
                ConfigUser.getInstance().getAdminIsActive(),
                ConfigUser.getInstance().getAdminLogin(),
                ConfigUser.getInstance().getAdminPassword(),
                ConfigUser.getInstance().getAdminDisplayName(),
                ConfigUser.getInstance().getAdminRole()
        );
        return new AuthRequest(authRequest.login(), authRequest.password());
    }

    public void deleteUserAdmin(String adminLogin) throws SQLException {
        deleteUser(adminLogin);
    }
}
