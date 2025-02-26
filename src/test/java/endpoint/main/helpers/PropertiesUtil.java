package endpoint.main.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private final static String PROPERTIES_FILE_PATH = "src/test/resources/env.properties";

    public static Properties readProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
