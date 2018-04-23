package ru.otus.l10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l10.dataset.AddressDataSet;
import ru.otus.l10.dataset.PhoneDataSet;
import ru.otus.l10.dataset.UserDataSet;

import java.util.List;

/**
 * Simple example for JDBC and ORM studying
 *
 * To run H2 browser console run $PROJECT_DIR/h2db/h2_web_console.sh(bat)
 */

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        try (DBService dbService = new DBServiceImpl()) {

            String status = dbService.getLocalStatus();
            log.info("Status: {}", status);

            dbService.save(new UserDataSet("user_1", new PhoneDataSet("1111111"), new AddressDataSet("Krivaya street")));
            dbService.save(new UserDataSet("user_2", new PhoneDataSet("2222222"), new AddressDataSet("Shirokaya avenue")));

            UserDataSet user = new UserDataSet("user_3", new PhoneDataSet("3333333"), new AddressDataSet("Shirokaya avenue"));
            user.addPhone(new PhoneDataSet("4444444"));
            user.addPhone(new PhoneDataSet("5555555"));
            dbService.save(user);

            UserDataSet dataSet = dbService.read(1);
            log.info("UserDataSet for id = 1 - {}", dataSet);

            dataSet = dbService.readByName("user_1");
            log.info("UserDataSet for name= 'user_1' - {}", dataSet);

            printDBData(dbService);

            dbService.delete(user);

            printDBData(dbService);
        }
    }

    private static void printDBData(DBService dbService) {
        log.info("All users");
        List<UserDataSet> userDataSets = dbService.readAllUsers();
        for (UserDataSet ds : userDataSets) {
            log.info("{}", ds);
        }

        log.info("All phones");
        List<PhoneDataSet> phoneDataSets = dbService.readAllPhones();
        for (PhoneDataSet ds : phoneDataSets) {
            log.info("{}", ds);
        }

        log.info("All addresses");
        List<AddressDataSet> addressDataSets = dbService.readAllAddresses();
        for (AddressDataSet ds : addressDataSets) {
            log.info("{}", ds);
        }
    }
}
