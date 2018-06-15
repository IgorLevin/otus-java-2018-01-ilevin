package ru.otus.l16.cache;

import ru.otus.l16.app.CacheInfo;

public class CacheInfoImpl implements CacheInfo {

    private int capacity;
    private int size;
    private int hit;
    private int miss;

    CacheInfoImpl(int capacity, int size, int hit, int miss) {
        this.capacity = capacity;
        this.size = size;
        this.hit = hit;
        this.miss = miss;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public String toString() {
        return "[CI: capacity=" + capacity + "; size=" + size + "; hit=" + hit + "; miss=" + miss + "]";
    }
}
