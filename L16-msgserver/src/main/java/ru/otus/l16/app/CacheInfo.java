package ru.otus.l16.app;

public interface CacheInfo {
    int getCapacity();
    int getSize();
    int getHitCount();
    int getMissCount();
}
