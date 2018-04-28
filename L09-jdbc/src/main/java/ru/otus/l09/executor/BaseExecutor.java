package ru.otus.l09.executor;

import java.sql.*;

public class BaseExecutor {

    private final Connection connection;

    public BaseExecutor(Connection connection) {
        this.connection = connection;
    }

    public void execQuery(String query, ResultHandler handler) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            handler.handle(result);
        }
    }

    public <T> T execQuery(String query, TResultHandler<T> handler) throws SQLException {
        try(Statement stmt = connection.createStatement()) {
            stmt.execute(query);
            ResultSet result = stmt.getResultSet();
            return handler.handle(result);
        }
    }


    public int execUpdate(String update) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(update);
            return stmt.getUpdateCount();
        }
    }

    public void execUpdate(String update, PStatementHandler prepare) {
        try {
            PreparedStatement stmt = getConnection().prepareStatement(update, Statement.RETURN_GENERATED_KEYS);
            prepare.handle(stmt);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    Connection getConnection() {
        return connection;
    }

    @FunctionalInterface
    public interface PStatementHandler {
        void handle(PreparedStatement statement) throws SQLException;
    }
}
