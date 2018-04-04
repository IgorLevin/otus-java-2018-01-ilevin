package ru.otus.l51;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        try {
            CustomTestFramework.runTestSuite(TestSuite.class.getName());
            CustomTestFramework.runTestsInPackage("ru.otus.l51");
        } catch (Exception e) {
            log.error("Tests failed ", e);
        }
    }
}
