package ru.otus.l13.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ru.otus.l13.cache.CacheEngine;
import ru.otus.l13.dataset.UserDataSet;
import ru.otus.l13.db.DBService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CacheStateServlet extends HttpServlet {

    private static final String CACHE_SIZE = "size";
    private static final String CACHE_HIT = "hit";
    private static final String CACHE_MISS = "miss";

    private final static int recordInDB = 2000;

    private ExecutorService executor;

    @Autowired
    private TemplateProcessor templateProcessor;
    @Autowired
    private CacheEngine<?, ?> cacheEngine;
    @Autowired
    private DBService dbService;

    private Logger log = LoggerFactory.getLogger(CacheStateServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        if (executor == null) {
            executor = Executors.newSingleThreadExecutor();
            try {
                dbService.prepareTables();
                dbService.addUsers(createBunchOfUsers(recordInDB));
            } catch (SQLException e) {
                log.error("Can't create tables in DB");
            }
            executor.execute(new DBAccessTask(dbService, cacheEngine));
        }

    }

    @Override
    @SuppressWarnings("EmptyCatchBlock")
    public void destroy() {
        log.trace("Destroy");
        super.destroy();
        if (executor != null) {
            executor.shutdownNow();
        }
        if (dbService != null) {
            try {
                dbService.deleteTables();
            } catch (SQLException e) {
            }
        }
    }

    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = (String)request.getSession().getAttribute(LoginServlet.SESSION_ATTRIBUTE_NAME);
        if (login == null || login.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put(CACHE_SIZE, cacheEngine.size());
        pageVariables.put(CACHE_HIT, cacheEngine.getHitCount());
        pageVariables.put(CACHE_MISS, cacheEngine.getMissCount());
        response.getWriter().println(templateProcessor.getPage("cache_state.json", pageVariables));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
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


    private class DBAccessTask implements Runnable {

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
}

