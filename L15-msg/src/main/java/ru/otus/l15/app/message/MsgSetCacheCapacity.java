package ru.otus.l15.app.message;

import ru.otus.l15.cache.CacheEngine;
import ru.otus.l15.messageSystem.Address;

public class MsgSetCacheCapacity extends MsgToCacheEngine {

    private final int capacity;

    public MsgSetCacheCapacity(Address from, Address to, int capacity) {
        super(from, to);
        this.capacity = capacity;
    }

    @Override
    public void exec(CacheEngine cacheEngine) {
        cacheEngine.setCapacity(capacity);
    }
}