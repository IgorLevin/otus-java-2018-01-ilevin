package ru.otus.l15.app.message;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.db.DBService;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Addressee;
import ru.otus.l15.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToDB extends Message {

    private Logger log = LoggerFactory.getLogger(MsgToDB.class);

    public MsgToDB(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        log.trace("exec(): {}", addressee);
        if (addressee instanceof DBService) {
            exec((DBService) addressee);
        }
    }

    public abstract void exec(DBService dbService);
}
