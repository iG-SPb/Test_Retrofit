package ru.geekbrains.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

@UtilityClass
public class ConfigUtils {
    Properties prop = new Properties();
    private static InputStream configFile;
    public static Integer baseProductId;
    public static Integer minNegId;
    public static Integer maxNegId;

    static {
        try {
            configFile = new FileInputStream("src/test/resources/application.properties");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public String getBaseUrl() {
        prop.load(configFile);
        baseProductId = Integer.valueOf(prop.getProperty("baseProductId"));
        minNegId = Integer.valueOf(prop.getProperty("minNegId"));
        maxNegId = Integer.valueOf(prop.getProperty("maxNegId"));
        return prop.getProperty("url");
    }

}
