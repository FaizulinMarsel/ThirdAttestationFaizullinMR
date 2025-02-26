package db.helpers.users;

public interface ConfigUser {

    static ConfigUser getInstance() {
        return LocalConfigUser.LOCAL_CONFIG_USER;
    }

    boolean getAdminIsActive();

    String getAdminLogin();

    String getAdminPassword();

    String getAdminDisplayName();

    String getAdminRole();

}
