package ru.otus.l15.cache;

import ru.otus.l15.app.CacheInfo;
import ru.otus.l15.messageSystem.Addressee;

import java.util.List;

public interface CacheEngine<K, V> extends Addressee {

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