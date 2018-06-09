package ru.otus.l15.app;

public interface CacheInfo {
    int getCapacity();
    int getSize();
    int getHitCount();
    int getMissCount();
}
