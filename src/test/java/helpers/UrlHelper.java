package helpers;

import java.util.Properties;

public class UrlHelper {
    private static final Properties propsUrl = PropertiesUtil.readProperties();

    public String getUrl() {
        return propsUrl.getProperty("URL");
    }
}
