package org.nexus.web.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Xieningjun
 * @date 2024/2/27 11:17
 * @description 包扫描
 */
@Slf4j
public class PackageScannerUtil {

    public static List<Class<?>> scan(String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> scanList = new ArrayList<>();
        // 获取系统类加载器
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        // 获取指定包下的所有类资源
        String packagePath = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(packagePath);
        // 遍历资源并加载类
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().endsWith(".class") && !file.getName().contains("$")) {
                            String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                            Class<?> clazz = Class.forName(className);
                            scanList.add(clazz);
                            log.info("Class Load:  " + clazz.getName());
                        }
                    }
                }
            }
        }
        return scanList;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        PackageScannerUtil.scan("org.nexus.server.client");
    }

}
