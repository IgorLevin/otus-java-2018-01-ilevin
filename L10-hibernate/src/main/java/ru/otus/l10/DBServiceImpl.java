package ru.otus.l10;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.otus.l10.dao.AddressDataSetDAO;
import ru.otus.l10.dao.PhoneDataSetDAO;
import ru.otus.l10.dataset.AddressDataSet;
import ru.otus.l10.dataset.PhoneDataSet;
import ru.otus.l10.dataset.UserDataSet;
import ru.otus.l10.dao.UserDataSetDAO;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class DBServiceImpl implements DBService {

    private final SessionFactory sessionFactory;

    public DBServiceImpl() {
        Configuration c = new Configuration();

        c.addAnnotatedClass(UserDataSet.class);
        c.addAnnotatedClass(PhoneDataSet.class);
        c.addAnnotatedClass(AddressDataSet.class);

        c.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        c.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        c.setProperty("hibernate.connection.url", "jdbc:h2:file:./h2db/test");
        c.setProperty("hibernate.connection.username", "sa");
        c.setProperty("hibernate.connection.password", "");
        c.setProperty("hibernate.show_sql", "true");
        c.setProperty("hibernate.hbm2ddl.auto", "create");
        c.setProperty("hibernate.connection.useSSL", "false");
        c.setProperty("hibernate.enable_lazy_load_no_trans", "true");

        sessionFactory = createSessionFactory(c);
    }

    public DBServiceImpl(Configuration configuration) {
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Override
    public String getLocalStatus() {
        return runInSession(session -> {
            return session.getTransaction().getStatus().name();
        });
    }

    @Override
    public void save(UserDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.save(dataSet);
        }
    }

    @Override
    public UserDataSet read(long id) {
        return runInSession(session -> {
           UserDataSetDAO dao = new UserDataSetDAO(session);
           return dao.read(id);
        });
    }

    @Override
    public UserDataSet readByName(String name) {
        return runInSession(session -> {
           UserDataSetDAO dao = new UserDataSetDAO(session);
           return dao.readByName(name);
        });
    }

    @Override
    public List<UserDataSet> readAllUsers() {
        return runInSession(session -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            return dao.readAll();
        });
    }

    @Override
    public List<PhoneDataSet> readAllPhones() {
        return runInSession(session -> {
            PhoneDataSetDAO dao = new PhoneDataSetDAO(session);
            return dao.readAll();
        });
    }

    @Override
    public List<AddressDataSet> readAllAddresses() {
        return runInSession(session -> {
            AddressDataSetDAO dao = new AddressDataSetDAO(session);
            return dao.readAll();
        });
    }

    @Override
    public void delete(UserDataSet dataSet) {
        runInSession((session) -> {
            UserDataSetDAO dao = new UserDataSetDAO(session);
            dao.delete(dataSet);
        });
    }

    @Override
    public void delete(PhoneDataSet dataSet) {
        runInSession((session) -> {
            PhoneDataSetDAO dao = new PhoneDataSetDAO(session);
            dao.delete(dataSet);
        });
    }

    @Override
    public void delete(AddressDataSet dataSet) {
        runInSession((session) -> {
            AddressDataSetDAO dao = new AddressDataSetDAO(session);
            dao.delete(dataSet);
        });
    }

    @Override
    public void shutdown() {
        sessionFactory.close();
    }

    @Override
    public void close() throws Exception {
        shutdown();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }

    private void runInSession(Consumer<Session> consumer) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
        }
    }
}
