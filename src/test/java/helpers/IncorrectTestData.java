package helpers;

import java.util.Properties;

public class IncorrectTestData {
    private static final Properties propsIncorrectData = PropertiesUtil.readProperties();

    public int getIncorrectCompanyId() {
        return Integer.parseInt(propsIncorrectData.getProperty("incorrect.company.id"));
    }

    public String getIncorrectUserToken() {
        return propsIncorrectData.getProperty("incorrect.user.token");
    }

    public String getIncorrectEmail() {
        return propsIncorrectData.getProperty("incorrect.employee.email");
    }

    public String getBrokenJson() {
        return propsIncorrectData.getProperty("incorrect.emloyee.json");
    }

    public int getIncorrectEmployeeId() {
        return Integer.parseInt(propsIncorrectData.getProperty("incorrect.employee.id"));
    }

    public String getIncorrectLastName() {
        return propsIncorrectData.getProperty("incorrect.emloyee.lastname");
    }

}
