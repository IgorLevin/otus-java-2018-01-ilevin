package ru.otus.l51;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l51.annotations.After;
import ru.otus.l51.annotations.Before;
import ru.otus.l51.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class CustomTestFramework {

    private static Logger log = LoggerFactory.getLogger(CustomTestFramework.class);

    public static void runTestSuite(String testSuiteClassName) throws Exception {

        log.info("Check test suite: {}", testSuiteClassName);

        Class<?> testSuiteClazz = Class.forName(testSuiteClassName);

        Object testSuiteObject = testSuiteClazz.getDeclaredConstructor().newInstance();

        Method[] methods = testSuiteClazz.getDeclaredMethods();

        List<Method> beforeMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();

        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Before) {
                    beforeMethods.add(method);
                    break;
                } else if (annotation instanceof After) {
                    afterMethods.add(method);
                    break;
                } else if (annotation instanceof Test) {
                    testMethods.add(method);
                    break;
                }
            }
        }

        if (testMethods.isEmpty()) {
            log.info("No tests to run");
            return;
        }

        log.info("Run tests");

        int testNumber = 1;

        try {
            for (Method testMethod : testMethods) {

                log.info("Run test #[{}]", testNumber++);

                for (Method beforeMethod : beforeMethods) {
                    beforeMethod.invoke(testSuiteObject);
                }

                testMethod.invoke(testSuiteObject);

                for (Method afterMethod : afterMethods) {
                    afterMethod.invoke(testSuiteObject);
                }
            }
        } finally {
            log.info("Total tests passed: {}", testNumber - 1);
        }
    }

    public static void runTestsInPackage(String packageName) throws Exception {

        log.info("Run tests in package: {}", packageName);

        String path = packageName.replace('.', '/');

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = cl.getResources(path);

        while(resources.hasMoreElements()) {
            URL url = resources.nextElement();
            List<String> fileNames = listFileNames(url);

            for (String fileName : fileNames) {
                runTestSuite(packageName + "." + fileName);
            }
        }

        log.info("Done");
    }

    private static List<String> listFileNames(URL url) throws Exception {
        log.trace("List class files for URL: {}", url);
        Path path = Paths.get(url.toURI());
        return Files.list(path)
                    .filter(p -> p.getFileName().toString().endsWith(".class"))
                    .map(p -> p.getFileName().toString())
                    .map(name -> name.split("\\.")[0])
                    .peek(name -> log.trace("  " + name))
                    .collect(Collectors.toList());
    }
}
