package db.helpers.company;

public interface ConfigCompany {
    static LocalConfigCompany getInstance() {
        return LocalConfigCompany.LOCAL_CONFIG_COMPANY;
    }

    boolean getIsActiveCompany();

    String getNameCompany();

    String getDescriptionCompany();
}
