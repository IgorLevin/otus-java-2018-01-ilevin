package ru.otus.l15.app.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.cache.CacheEngine;
import ru.otus.l15.messageSystem.Address;
import ru.otus.l15.messageSystem.Addressee;
import ru.otus.l15.messageSystem.Message;

public abstract class MsgToCacheEngine extends Message {

    private Logger log = LoggerFactory.getLogger(MsgToCacheEngine.class);

    public MsgToCacheEngine(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(Addressee addressee) {
        log.trace("exec(): {}", addressee);
        if (addressee instanceof CacheEngine) {
            exec((CacheEngine) addressee);
        }
    }

    public abstract void exec(CacheEngine engine);
}
