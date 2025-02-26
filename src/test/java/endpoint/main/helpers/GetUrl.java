package endpoint.main.helpers;

import java.util.Properties;

public class GetUrl {
    private static final Properties propsUrl = PropertiesUtil.readProperties();

    public String getUrl() {
        return propsUrl.getProperty("URL");
    }
}
