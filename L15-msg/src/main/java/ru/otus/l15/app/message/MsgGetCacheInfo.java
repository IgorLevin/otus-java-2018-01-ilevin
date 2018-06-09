package ru.otus.l15.app.message;

import ru.otus.l15.app.CacheInfoListener;
import ru.otus.l15.cache.CacheEngine;
import ru.otus.l15.messageSystem.Address;

public class MsgGetCacheInfo extends MsgToCacheEngine {

    private final CacheInfoListener listener;

    public MsgGetCacheInfo(Address from, Address to, CacheInfoListener listener) {
        super(from, to);
        this.listener = listener;
    }

    @Override
    public void exec(CacheEngine cacheEngine) {
        cacheEngine.getMS().sendMessage(new MsgGetCacheInfoAnswer(getTo(), getFrom(), cacheEngine.getInfo(), listener));
    }
}
