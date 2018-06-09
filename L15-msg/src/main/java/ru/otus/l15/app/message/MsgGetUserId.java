package ru.otus.l15.app.message;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.dataset.UserDataSet;
import ru.otus.l15.db.DBService;
import ru.otus.l15.messageSystem.Address;

import java.sql.SQLException;

/**
 * Created by tully.
 */
public class MsgGetUserId extends MsgToDB {
    private final String login;
    private final String password;

    private Logger log = LoggerFactory.getLogger(MsgGetUserId.class);

    public MsgGetUserId(Address from, Address to, String login, String password) {
        super(from, to);
        this.login = login;
        this.password = password;
    }

    @Override
    public void exec(DBService dbService) {
        int id = -1;
        try {
            UserDataSet user = dbService.getUser(login);
            id = (int)user.getId();
        } catch (SQLException e) {
            log.error("Getting user error ", e);
        }
        dbService.getMS().sendMessage(new MsgGetUserIdAnswer(getTo(), getFrom(), login, id));
    }
}
