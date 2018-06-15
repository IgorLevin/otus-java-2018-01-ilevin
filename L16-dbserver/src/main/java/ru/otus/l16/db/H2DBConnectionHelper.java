package ru.otus.l16.db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class H2DBConnectionHelper {

    private H2DBConnectionHelper() {
    }

    public static Connection getConnection() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            return DriverManager.getConnection("jdbc:h2:file:./h2db/test", "sa", "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
