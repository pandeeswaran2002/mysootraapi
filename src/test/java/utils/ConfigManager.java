package utils;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    static Properties props = new Properties();

    static {
        try {
            String env = System.getProperty("env", "dev");
            String fileName = "config-" + env + ".properties";
            InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream(fileName);
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading config file");
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}
