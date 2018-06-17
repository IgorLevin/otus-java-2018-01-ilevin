package ru.otus.l16.cache;

import ru.otus.l16.app.CacheInfo;

public class CacheInfoImpl extends CacheInfo {

    public CacheInfoImpl(int capacity, int size, int hit, int miss) {
        setCapacity(capacity);
        setSize(size);
        setHit(hit);
        setMiss(miss);
    }

    @Override
    public String toString() {
        return "[CI: capacity=" + getCapacity() + "; " +
                "size=" + getSize() + "; " +
                "hit=" + getHitCount() + "; " +
                "miss=" + getMissCount() + "]";
    }
}
