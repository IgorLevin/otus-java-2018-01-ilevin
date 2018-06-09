package ru.otus.l15.app.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.front.FrontendServiceImpl;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Addressee;
import ru.otus.l15.messageSystem.Message;

/**
 * Created by tully.
 */
public abstract class MsgToFrontend extends Message {

    private Logger log = LoggerFactory.getLogger(MsgToFrontend.class);

    public MsgToFrontend(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        log.trace("exec(): {}", addressee);
        if (addressee instanceof FrontendServiceImpl) {
            exec((FrontendServiceImpl) addressee);
        } else {
            //todo error!
        }
    }

    public abstract void exec(FrontendServiceImpl front);
}