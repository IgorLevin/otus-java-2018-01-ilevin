package ru.otus.l12.cache;

import java.util.List;

public interface CacheEngine<K, V> {

    void put(CacheElement<K, V> element);

    void put(K key, V value);

    V get(K key);

    List<V> getAll();

    int getHitCount();

    int getMissCount();

    void dispose();
}