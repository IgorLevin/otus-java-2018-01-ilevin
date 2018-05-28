package ru.otus.l13.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l13.cache.CacheEngine;
import ru.otus.l13.dataset.UserDataSet;

import java.sql.SQLException;

public class DBServiceCachedImpl extends DBServiceImpl {

    private CacheEngine<Long, UserDataSet> cache;
    private Logger log = LoggerFactory.getLogger(DBServiceCachedImpl.class);

    public DBServiceCachedImpl(CacheEngine<Long, UserDataSet> cache) {
        super(H2DBConnectionHelper.getConnection());
        this.cache = cache;
    }

    @Override
    public void addUsers(UserDataSet... users) throws SQLException {
        super.addUsers(users);
        for (UserDataSet user : users) {
            cache.put(user.getId(), user);
        }
    }

    @Override
    public String getUserName(long id) throws SQLException {
        UserDataSet user = cache.get(id);
        if (user == null) {
            user = super.getUser(id);
            cache.put(id, user);
        }
        log.trace("User[{}] name: {}", id, user.getName());
        return user.getName();
    }

    @Override
    public void setUser(long id, UserDataSet user) throws SQLException {
        cache.put(id, user);
        super.setUser(id, user);
    }

    @Override
    public UserDataSet getUser(long id) throws SQLException {
        UserDataSet user = cache.get(id);
        if (user == null) {
            user = super.getUser(id);
            cache.put(id, user);
        }
        log.trace("User[{}] name: {}; age: {}", id, user.getName(), user.getAge());
        return user;
    }
}
