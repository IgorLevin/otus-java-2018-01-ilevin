package ru.otus.l11.cache;

/**
 * Wrapper for cache {@link CacheEngineImpl} stored values.
 * Provides creation and access time marks for cache cleaning mechanism
 * @param <K>
 * @param <V>
 */
public class CacheElement<K, V> {

    private final K key;
    private final V value;
    private final long creationTime;
    private long lastAccessTime;


    public CacheElement(K key, V value) {
        this.key = key;
        this.value = value;
        this.creationTime = getCurrentTime();
        this.lastAccessTime = getCurrentTime();
    }

    protected long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setAccessed() {
        lastAccessTime = getCurrentTime();
    }
}
