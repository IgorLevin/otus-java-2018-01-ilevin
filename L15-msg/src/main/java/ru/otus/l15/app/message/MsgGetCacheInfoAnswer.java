package ru.otus.l15.app.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l15.app.CacheInfo;
import ru.otus.l15.app.CacheInfoListener;
import ru.otus.l15.front.FrontendServiceImpl;
import ru.otus.l15.messageSystem.Address;

public class MsgGetCacheInfoAnswer  extends MsgToFrontend {

    private final CacheInfo info;
    private final CacheInfoListener listener;

    private Logger log = LoggerFactory.getLogger(MsgGetCacheInfoAnswer.class);

    MsgGetCacheInfoAnswer(Address from, Address to, CacheInfo info, CacheInfoListener listener) {
        super(from, to);
        this.info = info;
        this.listener = listener;
    }

    @Override
    public void exec(FrontendServiceImpl front) {
        log.trace("exec: cacheInfo={}", info);
        front.onCacheInfo(info, listener);
    }
}
