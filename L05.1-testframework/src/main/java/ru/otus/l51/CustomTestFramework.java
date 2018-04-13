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
        if (runTests(testSuiteClassName)) {
            log.info("[Success]\n");
        } else {
            log.info("[Failure!]\n");
        }
    }

    public static boolean runTests(String testSuiteClassName) throws Exception {
        Class<?> testSuiteClazz = Class.forName(testSuiteClassName);

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
            log.info("[No tests to run in '{}']", testSuiteClassName);
            return true;
        }

        log.info("[Run tests in: '{}']", testSuiteClassName);

        int testNumber = 1;

        for (Method testMethod : testMethods) {

            try {
                log.info("Run test #[{}]", testNumber++);

                Object testSuiteObject = testSuiteClazz.getDeclaredConstructor().newInstance();

                for (Method beforeMethod : beforeMethods) {
                    beforeMethod.invoke(testSuiteObject);
                }

                testMethod.invoke(testSuiteObject);

                for (Method afterMethod : afterMethods) {
                    afterMethod.invoke(testSuiteObject);
                }
            } catch (Exception e) {
                log.error("Test failure ", e);
                testNumber--;
            }
        }

        log.info("Total tests passed: {}", testNumber - 1);

        return testMethods.size() == testNumber - 1;
    }

    public static void runTestsInPackage(String packageName) throws Exception {

        log.info("[PACKAGE: '{}']", packageName);

        String path = packageName.replace('.', '/');

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = cl.getResources(path);

        boolean success = true;
        while(resources.hasMoreElements()) {
            URL url = resources.nextElement();
            List<String> fileNames = listFileNames(url);

            for (String fileName : fileNames) {
                success &= runTests(packageName + "." + fileName);
            }
        }
        if (success) {
            log.info("[SUCCESS. Package: '{}']\n", packageName);
        } else {
            log.info("[FAILURE. Package: '{}']\n", packageName);
        }
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
