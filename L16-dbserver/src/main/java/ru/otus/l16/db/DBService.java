package ru.otus.l16.db;


import ru.otus.l16.dataset.UserDataSet;

import java.sql.SQLException;
import java.util.List;

public interface DBService extends AutoCloseable {

    void init();

    String getMetaData();

    void prepareTables() throws SQLException;

    void addUsers(UserDataSet... users) throws SQLException;

    String getUserName(long id) throws SQLException;

    List<String> getAllNames() throws SQLException;

    void setUser(long id, UserDataSet user) throws SQLException;

    UserDataSet getUser(long id) throws SQLException;

    UserDataSet getUser(String name) throws SQLException;

    List<UserDataSet> getAllUsers() throws SQLException;

    void deleteTables() throws SQLException;
}
