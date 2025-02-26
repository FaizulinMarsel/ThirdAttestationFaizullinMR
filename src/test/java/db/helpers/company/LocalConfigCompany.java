package db.helpers.company;

import helpers.PropertiesUtil;

import java.util.Properties;

public enum LocalConfigCompany implements ConfigCompany {
    LOCAL_CONFIG_COMPANY;
    private static final Properties propsCompany = PropertiesUtil.readProperties();

    @Override
    public boolean getIsActiveCompany() {
        return Boolean.parseBoolean(propsCompany.getProperty("company.isActive"));
    }

    @Override
    public String getNameCompany() {
        return propsCompany.getProperty("company.name");
    }

    @Override
    public String getDescriptionCompany() {
        return propsCompany.getProperty("company.description");
    }
}
