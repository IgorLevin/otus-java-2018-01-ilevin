package ru.otus.l09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l09.dataset.UserDataSet;
import ru.otus.l09.db.DBService;
import ru.otus.l09.db.DBServiceImpl;
import ru.otus.l09.db.H2DBConnectionHelper;

import java.util.List;
import java.util.Random;

/**
 * Simple example for JDBC and ORM studying
 *
 * To run H2 browser console run $PROJECT_DIR/h2db/h2_web_console.sh(bat)
 */

public class Main {

    private static final int NUM_OF_USERS = 1000;
    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        try (DBService dbService = new DBServiceImpl(H2DBConnectionHelper.getConnection())) {

            log.info(dbService.getMetaData());

            dbService.prepareTables();

            dbService.addUsers(createBunchOfUsers(NUM_OF_USERS));

            long id = 1;
            log.info("User[{}] name: {}", id, dbService.getUserName(id));

            List<String> names = dbService.getAllNames();
            log.info("All names: {}", names.toString());

            List<UserDataSet> allUsers = dbService.getAllUsers();
            log.info("All users:");
            for (UserDataSet user : allUsers) {
                log.info("    {}", user.toString());
            }

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
}
