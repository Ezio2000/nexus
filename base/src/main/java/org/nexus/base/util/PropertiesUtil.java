package org.nexus.base.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Xieningjun
 * @date 2024/3/10 23:56
 * @description
 */
@Slf4j
public class PropertiesUtil {

    public static Properties getProperties(String filePath) {
        Properties properties = new Properties();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(filePath)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                log.error("File not found: {}.", filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

}
