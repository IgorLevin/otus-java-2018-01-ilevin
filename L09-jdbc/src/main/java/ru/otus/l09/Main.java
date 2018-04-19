package ru.otus.l09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Simple example for JDBC and ORM studying
 *
 * To run H2 browser console run $PROJECT_DIR/h2db/h2_web_console.sh(bat)
 */

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        Driver driver = (Driver)Class.forName("org.h2.Driver").getConstructor().newInstance();
        DriverManager.registerDriver(driver);

        try (Connection connection = DriverManager.getConnection("jdbc:h2:file:./h2db/test", "sa", "")) {

            UserDataSet uds = new UserDataSet(1, "User 1", 25);

            CustomExecutor ce = new CustomExecutor(connection);
            ce.save(uds);

            UserDataSet dataSet = ce.load(1, UserDataSet.class);

            log.debug("UserDataSet: id={}; name={}; age={}", dataSet.getId(), dataSet.getName(), dataSet.getAge());
        }
    }
}
