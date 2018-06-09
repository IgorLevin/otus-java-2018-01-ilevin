package ru.otus.l15.app.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.front.FrontendServiceImpl;
import ru.otus.l15.messageSystem.Address;

/**
 * Created by tully.
 */
public class MsgGetUserIdAnswer extends MsgToFrontend {
    private final String name;
    private final int id;

    private Logger log = LoggerFactory.getLogger(MsgGetUserIdAnswer.class);

    public MsgGetUserIdAnswer(Address from, Address to, String name, int id) {
        super(from, to);
        this.name = name;
        this.id = id;
    }

    @Override
    public void exec(FrontendServiceImpl front) {
        log.trace("exec: name={}; id={}", name, id);
        if (name == null || name.isEmpty() || id == -1) {
            front.onWrongUser();
        } else {
            front.onUserLogged();
        }
    }
}
