package ru.otus.l12;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l12.cache.CacheEngine;
import ru.otus.l12.cache.CacheEngineImpl;
import ru.otus.l12.dataset.UserDataSet;
import ru.otus.l12.db.DBService;
import ru.otus.l12.db.DBServiceCachedImpl;
import ru.otus.l12.db.H2DBConnectionHelper;
import ru.otus.l12.servlet.AdminServlet;
import ru.otus.l12.servlet.CacheStateServlet;
import ru.otus.l12.servlet.LoginServlet;
import ru.otus.l12.servlet.TemplateProcessor;

import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Jetty web server as cache web interface
 *
 * To run H2 browser console run $PROJECT_DIR/h2db/h2_web_console.sh(bat)
 */

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private final static int PORT = 8090;
    private final static String PUBLIC_HTML = "public_html";

    private final static int cacheSize = 1000;
    private final static int recordInDB = 2000;
    private static ExecutorService executor = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {

        CacheEngine<Long, UserDataSet> cacheEngine = new CacheEngineImpl<>(cacheSize, 0, 0, true);

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(PUBLIC_HTML);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        TemplateProcessor templateProcessor = new TemplateProcessor();

        context.addServlet(new ServletHolder(new LoginServlet(templateProcessor)), "/login");
        context.addServlet(new ServletHolder(new AdminServlet(templateProcessor, cacheEngine)), "/admin");
        context.addServlet(new ServletHolder(new CacheStateServlet(templateProcessor, cacheEngine)), "/cache_state");

        Server server = new Server(PORT);
        server.setHandler(new HandlerList(resourceHandler, context));

        try (DBService dbService = new DBServiceCachedImpl(H2DBConnectionHelper.getConnection(), cacheEngine)) {

            dbService.prepareTables();
            dbService.addUsers(createBunchOfUsers(recordInDB));

            executor.execute(new DBAccessTask(dbService, cacheEngine));

            server.start();
            server.join();

            executor.shutdownNow();
            dbService.deleteTables();
        }
    }

    private static class DBAccessTask implements Runnable {

        private DBService dbService;
        private CacheEngine<?, ?> cacheEngine;

        DBAccessTask(DBService dbService, CacheEngine<?, ?> cacheEngine) {
            this.dbService = dbService;
            this.cacheEngine = cacheEngine;
        }

        @Override
        public void run() {

            while(!Thread.currentThread().isInterrupted()) {

                long userId = new Random().nextInt(recordInDB);

                try {
                    log.info("User[{}] name: {}", userId, dbService.getUserName(userId));
                } catch (SQLException e) {
                    log.warn("Can't get user with id={}", userId);
                }

                log.info("Cache hits: " + cacheEngine.getHitCount());
                log.info("Cache misses: " + cacheEngine.getMissCount());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
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
}
