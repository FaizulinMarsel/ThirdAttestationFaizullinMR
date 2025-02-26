package db.helpers.company;

import java.sql.*;

public class WorkCompanyDb {
    private int companyId;
    Connection connection;

    public WorkCompanyDb(Connection connection) {
        this.connection = connection;
    }

    public int createCompany() throws SQLException {

        String INSERT_COMPANY = """
                insert into company (is_active, "name", description)
                values (?, ?, ?)
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(
                INSERT_COMPANY, Statement.RETURN_GENERATED_KEYS
        );
        preparedStatement.setBoolean(1, ConfigCompany.getInstance().getIsActiveCompany());
        preparedStatement.setString(2, ConfigCompany.getInstance().getNameCompany());
        preparedStatement.setString(3, ConfigCompany.getInstance().getDescriptionCompany());
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        companyId = resultSet.getInt("id");
        return companyId;
    }

    public void deleteCompany() throws SQLException {
        String DELETE_COMPANY = """
                delete from company c
                where c.id = ?;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_COMPANY);
        preparedStatement.setInt(1, companyId);
        preparedStatement.executeUpdate();
    }

    public void deleteEmployeesByCompanyId() throws SQLException {
        String DELETE_EMPLOYEES = """
                DELETE FROM employee WHERE company_id = ?;
                """;
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEES);
        preparedStatement.setInt(1, companyId);
        preparedStatement.executeUpdate();
    }
}
