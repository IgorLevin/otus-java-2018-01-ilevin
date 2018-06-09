package ru.otus.l15.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.executor.BaseExecutor;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.MessageSystem;
import ru.otus.l15.messageSystem.MessageSystemContext;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class DBServiceImpl implements DBService {

    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint auto_increment, name varchar(256), age int, primary key (id))";
    private static final String DELETE_TABLE_USER = "drop table user if exists";

    private static final String INSERT_INTO_USER = "insert into user (name, age) values(?,?)";

    private static final String UPDATE_USER = "update user set name='%s', age=%s where id=%s";

    private static final String SELECT_USER = "select * from user where id=%s";
    private static final String SELECT_USER_BY_NAME = "select * from user where name='%s'";
    private static final String SELECT_ALL_USERS = "select * from user";

    private static final String SELECT_USER_NAME = "select name from user where id=%s";
    private static final String SELECT_ALL_USERS_NAME = "select name from user";

    private final static int NUM_OF_DUMMY_RECORDS = 2000;

    private final Address address;
    private final MessageSystemContext context;
    private final Connection connection;
    private final boolean emulateLoad;
    private ExecutorService executor;

    private Logger log = LoggerFactory.getLogger(DBServiceImpl.class);

    public DBServiceImpl(MessageSystemContext context, Address address, Connection connection, boolean emulateLoad) {
        this.context = context;
        this.address = address;
        this.connection = connection;
        this.emulateLoad = emulateLoad;
    }

    public void init() {
        log.info("init()");
        log.trace("Address: {}", address.getId());

        context.setDbAddress(address);
        context.getMessageSystem().addAddressee(this);

        try {
            prepareTables();
            addUsers(new UserDataSet("admin", 40));
        } catch (SQLException e) {
            log.error("Tables initialization error ", e);
        }

        if (emulateLoad && executor == null) {
            executor = Executors.newSingleThreadExecutor();
            try {
                addUsers(createBunchOfUsers(NUM_OF_DUMMY_RECORDS));
            } catch (SQLException e) {
                log.error("Can't create tables in DB");
            }
            executor.execute(new DBAccessTask(this));
        }
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
    public UserDataSet getUser(String name) throws SQLException {
        BaseExecutor exec = new BaseExecutor(getConnection());
        return exec.execQuery(String.format(SELECT_USER_BY_NAME, name),
                result -> {
                    result.next();
                    UserDataSet user = new UserDataSet();
                    user.setId(result.getLong("id"));
                    user.setName(result.getString("name"));
                    user.setAge(result.getInt("age"));
                    log.trace("User[{}] name: {}; age: {}", user.getId(), user.getName(), user.getAge());
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
        log.info("close");
        if (executor != null) {
            executor.shutdownNow();
        }
        connection.close();
        log.info("Connection closed.");
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }

    private Connection getConnection() {
        return connection;
    }


    private static UserDataSet[] createBunchOfUsers(final int numberOfUsers) {
        UserDataSet[] users = new UserDataSet[numberOfUsers];
        for (int i = 0; i < numberOfUsers; i++) {
            users[i] = createUser();
        }
        return users;
    }

    private static UserDataSet createUser() {
        Random random = new Random();
        int index = random.nextInt(10000);
        int age = 20 + random.nextInt(80);
        return new UserDataSet("User_" + index, age);
    }


    /**
     * Класс эмулирующий нагрузку на DB и кэш для тестовой демонстрации работы этих сервисов
     */
    private static class DBAccessTask implements Runnable {

        private DBService dbService;

        private Logger log = LoggerFactory.getLogger(DBAccessTask.class);

        DBAccessTask(DBService dbService) {
            this.dbService = dbService;
        }

        @Override
        public void run() {

            while(!Thread.currentThread().isInterrupted()) {

                long userId = new Random().nextInt(NUM_OF_DUMMY_RECORDS);

                try {
                    log.trace("Pull user: {}", userId);
                    dbService.getUserName(userId);
                } catch (SQLException e) {
                    log.warn("Can't get user with id={}", userId);
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
