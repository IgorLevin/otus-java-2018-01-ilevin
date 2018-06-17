package ru.otus.l16.app;

public class CacheInfo {

    private int capacity;
    private int size;
    private int hit;
    private int miss;

    public CacheInfo() {}

    public int getCapacity() {
        return capacity;
    }

    public int getSize() {
        return size;
    }

    public int getHitCount() {
        return hit;
    }

    public int getMissCount() {
        return miss;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public void setMiss(int miss) {
        this.miss = miss;
    }
}
