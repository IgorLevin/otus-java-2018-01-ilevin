package ru.otus.l13.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

/**
 * Cache engine with {@link SoftReference} based storage mechanism.
 * Uses {@link CacheElement} wrapper for stored values.
 * <p>
 * Provides next settings:
 *   - max cache size
 *   - idle time
 *   - lifetime
 * </p>
 * @param <K> type of keys to access values
 * @param <V> type of values
 */
public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {

    private static final int TIME_THRESHOLD_MS = 5;

    private int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    private final Map<K, SoftReference<CacheElement<K, V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    private int hit = 0;
    private int miss = 0;

    private Logger log = LoggerFactory.getLogger(CacheEngineImpl.class);

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;
    }

    @Override
    public synchronized void put(CacheElement<K, V> element) {

        log.trace("Put element: {}-{}", element.getKey(), element.getValue());

        if (elements.size() >= maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
            log.warn("Cache size exceeded. Remove element: {}", firstKey);
        }

        K key = element.getKey();
        elements.put(key, new SoftReference<>(element));

        if (!isEternal) {
            if (lifeTimeMs != 0) {
                TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
                timer.schedule(lifeTimerTask, lifeTimeMs);
            }
            if (idleTimeMs != 0) {
                TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
                timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
            }
        }
    }

    @Override
    public void put(K key, V value) {
        put(new CacheElement<>(key, value));
    }

    @Override
    public synchronized V get(K key) {
        SoftReference<CacheElement<K, V>> softRef = elements.get(key);

        CacheElement<K, V> element = softRef != null ? softRef.get() : null;
        if (element != null) {
            log.trace("Hit element: {}", key);
            hit++;
            element.setAccessed();
            return element.getValue();
        } else {
            log.trace("Miss element: {}", key);
            miss++;
            return null;
        }
    }

    @Override
    public synchronized List<V> getAll() {
        List<V> values = new ArrayList<>();
        for(K key : elements.keySet()) {
            V value = get(key);
            if (value != null) {
                values.add(value);
            }
        }
        return values;
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
    public void dispose() {
        timer.cancel();
        elements.clear();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public int capacity() {
        return maxElements;
    }

    @Override
    public synchronized void setCapacity(int size) {
        if (size > 0) {
            maxElements = size;
            while (elements.size() >= maxElements) {
                K firstKey = elements.keySet().iterator().next();
                elements.remove(firstKey);
            }
        }
    }

    private TimerTask getTimerTask(final K key, Function<CacheElement<K, V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                CacheElement<K, V> element = elements.get(key).get();
                if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                    log.trace("Time to remove element: {}", key);
                    elements.remove(key);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
