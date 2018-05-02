package ru.otus.l11;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l11.cache.CacheEngine;
import ru.otus.l11.cache.CacheEngineImpl;
import ru.otus.l11.dataset.UserDataSet;
import ru.otus.l11.db.DBService;
import ru.otus.l11.db.DBServiceCachedImpl;
import ru.otus.l11.db.H2DBConnectionHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Cached DBService example
 *
 * Run options: -Xms256m -Xmx256m
 *
 * To run H2 browser console run $PROJECT_DIR/h2db/h2_web_console.sh(bat)
 */

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        softReferenceScenario();
        //maxCacheSizeExceededScenario();
        //lifeTimeScenario();
        //idleTimeScenario();
    }

    private static void softReferenceScenario() throws Exception {
        final int cacheSize = 5000;
        final int numOfUsers = 1915;

        CacheEngine<Long, UserDataSet> cacheEngine = new CacheEngineImpl<>(cacheSize, 0, 0, true);

        try (DBService dbService = new DBServiceCachedImpl(H2DBConnectionHelper.getConnection(), cacheEngine)) {

            dbService.prepareTables();
            dbService.addUsers(createBunchOfUsers(numOfUsers));

            long userId = 1000;
            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            // Scenario activator
            wasteMemory();

            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            dbService.deleteTables();
        }
    }

    private static void maxCacheSizeExceededScenario() throws Exception {
        final int cacheSize = 1000;
        final int numOfUsers = 600;

        CacheEngine<Long, UserDataSet> cacheEngine = new CacheEngineImpl<>(cacheSize, 0, 0, true);

        try (DBService dbService = new DBServiceCachedImpl(H2DBConnectionHelper.getConnection(), cacheEngine)) {

            dbService.prepareTables();
            dbService.addUsers(createBunchOfUsers(numOfUsers));

            long userId = 1;
            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            // Scenario activator
            dbService.addUsers(createBunchOfUsers(numOfUsers));

            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            dbService.deleteTables();
        }
    }

    private static void lifeTimeScenario() throws Exception {
        final int cacheSize = 100;
        final int numOfUsers = 100;

        CacheEngine<Long, UserDataSet> cacheEngine = new CacheEngineImpl<>(cacheSize, 100, 0, false);

        try (DBService dbService = new DBServiceCachedImpl(H2DBConnectionHelper.getConnection(), cacheEngine)) {

            dbService.prepareTables();
            dbService.addUsers(createBunchOfUsers(numOfUsers));

            long userId = 1;
            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            // Scenario activator
            Thread.sleep(200);

            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            dbService.deleteTables();
        }
    }

    private static void idleTimeScenario() throws Exception {
        final int cacheSize = 100;
        final int numOfUsers = 100;

        CacheEngine<Long, UserDataSet> cacheEngine = new CacheEngineImpl<>(cacheSize, 0, 100, false);

        try (DBService dbService = new DBServiceCachedImpl(H2DBConnectionHelper.getConnection(), cacheEngine)) {

            dbService.prepareTables();
            dbService.addUsers(createBunchOfUsers(numOfUsers));

            long userId = 1;
            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            // Scenario activator
            Thread.sleep(200);

            log.info("User[{}] name: {}", userId, dbService.getUserName(userId));

            log.info("Cache hits: " + cacheEngine.getHitCount());
            log.info("Cache misses: " + cacheEngine.getMissCount());

            dbService.deleteTables();
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

    private static void wasteMemory() {
        int size = 229000;
        List<WasteMemObject> references = new ArrayList<>(size);
        for (int k = 0; k < size; k++) {
            references.add(new WasteMemObject());
        }
    }

    private static class WasteMemObject {
        byte[] data = new byte[1024];
        byte[] getData() {
            return data;
        }
    }
}
