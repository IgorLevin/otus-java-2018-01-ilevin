package ru.otus.l13.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l13.dataset.UserDataSet;
import ru.otus.l13.executor.BaseExecutor;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DBServiceImpl implements DBService {

    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint auto_increment, name varchar(256), age int, primary key (id))";
    private static final String DELETE_TABLE_USER = "drop table user if exists";

    private static final String INSERT_INTO_USER = "insert into user (name, age) values(?,?)";

    private static final String UPDATE_USER = "update user set name=%s, age=%s where id=%s";

    private static final String SELECT_USER = "select * from user where id=%s";
    private static final String SELECT_ALL_USERS = "select * from user";

    private static final String SELECT_USER_NAME = "select name from user where id=%s";
    private static final String SELECT_ALL_USERS_NAME = "select name from user";

    private final Connection connection;

    private Logger log = LoggerFactory.getLogger(DBServiceImpl.class);

    public DBServiceImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String getMetaData() {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: "     + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: "  + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: "      + getConnection().getMetaData().getDriverName();
        } catch (SQLException e) {
            log.error("Getting metadata error ", e);
            return e.getMessage();
        }
    }

    @Override
    public void prepareTables() throws SQLException {
        BaseExecutor exec = new BaseExecutor(getConnection());
        exec.execUpdate(DELETE_TABLE_USER);
        exec.execUpdate(CREATE_TABLE_USER);
        log.info("Table created");
    }

    @Override
    public void addUsers(UserDataSet... users) throws SQLException {
        try {
            BaseExecutor exec = new BaseExecutor(getConnection());
            getConnection().setAutoCommit(false);
            exec.execUpdate(INSERT_INTO_USER, statement -> {
                for (UserDataSet user : users) {
                    statement.setString(1, user.getName());
                    statement.setInt(2, user.getAge());
                    statement.executeUpdate();
                    ResultSet rs = statement.getGeneratedKeys();
                    if (rs.next()) {
                        long id = rs.getLong(1);
                        user.setId(id);
                    } else {
                        throw new RuntimeException("Can't get inserted row id");
                    }
                }
            });
            getConnection().commit();
        } catch (SQLException e){
            getConnection().rollback();
        } finally {
            getConnection().setAutoCommit(true);
        }
    }

    @Override
    public String getUserName(long id) throws SQLException {
        BaseExecutor exec = new BaseExecutor(getConnection());
        return exec.execQuery(String.format(SELECT_USER_NAME, id),
                result -> {
                    result.next();
                    String name = result.getString("name");
                    log.trace("User[{}] name: {}", id, name);
                    return name;
                });
    }

    @Override
    public List<String> getAllNames() throws SQLException {
        BaseExecutor executor = new BaseExecutor(getConnection());
        return executor.execQuery(SELECT_ALL_USERS_NAME, result -> {
            List<String> names = new ArrayList<>();

            while (!result.isLast()) {
                result.next();
                names.add(result.getString("name"));
            }
            return names;
        });
    }

    @Override
    public void setUser(long id, UserDataSet user) throws SQLException {
        BaseExecutor executor = new BaseExecutor(getConnection());
        int updated = executor.execUpdate(String.format(UPDATE_USER, user.getName(), user.getAge(), id));
        log.trace("{} rows updated", updated);
    }

    @Override
    public UserDataSet getUser(long id) throws SQLException {
        BaseExecutor exec = new BaseExecutor(getConnection());
        return exec.execQuery(String.format(SELECT_USER, id),
                result -> {
                    result.next();
                    UserDataSet user = new UserDataSet();
                    user.setName(result.getString("name"));
                    user.setAge(result.getInt("age"));
                    log.trace("User[{}] name: {}; age: {}", id, user.getName(), user.getAge());
                    return user;
                });
    }

    @Override
    public List<UserDataSet> getAllUsers() throws SQLException {
        BaseExecutor executor = new BaseExecutor(getConnection());
        return executor.execQuery(SELECT_ALL_USERS, result -> {
            List<UserDataSet> users = new ArrayList<>();

            while (!result.isLast()) {
                result.next();
                UserDataSet user = new UserDataSet();
                user.setId(result.getLong("id"));
                user.setName(result.getString("name"));
                user.setAge(result.getInt("age"));
                users.add(user);
            }
            return users;
        });
    }

    @Override
    public void deleteTables() throws SQLException {
        BaseExecutor exec = new BaseExecutor(getConnection());
        exec.execUpdate(DELETE_TABLE_USER);
        log.info("Table dropped");
    }

    @Override
    public void close() throws Exception {
        connection.close();
        log.info("Connection closed.");
    }

    private Connection getConnection() {
        return connection;
    }
}
