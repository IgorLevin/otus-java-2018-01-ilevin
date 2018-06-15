package ru.otus.l16.cache;

import ru.otus.l16.app.CacheInfo;

import java.util.List;

public interface CacheEngine<K, V> {

    void put(CacheElement<K, V> element);

    void put(K key, V value);

    V get(K key);

    List<V> getAll();

    int getHitCount();

    int getMissCount();

    void dispose();

    int size();

    int capacity();

    void setCapacity(int size);

    CacheInfo getInfo();
}