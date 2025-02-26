package db.helpers.users;

import endpoint.auth.AuthRequest;

import java.sql.*;

public abstract class UserService {
    Connection connection;

    public UserService(Connection connection) {
        this.connection = connection;
    }

    public AuthRequest createUser(boolean isActive, String login, String password, String displayName, String role) throws SQLException {
        String INSERT_USER = """
                insert into app_users (is_active, login, "password", display_name, "role")
                values (?, ?, ?, ?, ?)
                 """;
        PreparedStatement preparedStatement = connection.prepareStatement(
                INSERT_USER,
                Statement.RETURN_GENERATED_KEYS
        );
        preparedStatement.setBoolean(1, isActive);
        preparedStatement.setString(2, login);
        preparedStatement.setString(3, password);
        preparedStatement.setString(4, displayName);
        preparedStatement.setObject(5, role, Types.OTHER);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        return new AuthRequest(resultSet.getString("login"), resultSet.getString("password"));

    }

    public void deleteUser(String login) throws SQLException {
        String DELETE_USER = """
                DELETE FROM app_users WHERE login = ?;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER);
        preparedStatement.setString(1, login);
        preparedStatement.executeUpdate();
    }
}
