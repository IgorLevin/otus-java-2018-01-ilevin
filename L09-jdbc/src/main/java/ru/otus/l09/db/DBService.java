package ru.otus.l09.db;


import ru.otus.l09.dataset.UserDataSet;

import java.sql.SQLException;
import java.util.List;

public interface DBService extends AutoCloseable {

    String getMetaData();

    void prepareTables() throws SQLException;

    void addUsers(UserDataSet... users) throws SQLException;

    String getUserName(long id) throws SQLException;

    List<String> getAllNames() throws SQLException;

    UserDataSet getUser(long id) throws SQLException;

    List<UserDataSet> getAllUsers() throws SQLException;

    void deleteTables() throws SQLException;
}
