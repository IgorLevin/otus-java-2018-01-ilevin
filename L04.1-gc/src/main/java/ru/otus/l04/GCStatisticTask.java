package ru.otus.l04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;

/**
 * Собирает статистику работы GC и пишет её в лог.
 *
 * Запрашивает через заданные интервалы времени {@link GarbageCollectorMXBean}
 */
public class GCStatisticTask implements Runnable {

    private final static int ONE_SECOND_GC_BEAN_POLLING_INTERVAL = 1000;
    private final static int ONE_MINUTE_STATISTIC_WINDOW_SIZE = 60000/ ONE_SECOND_GC_BEAN_POLLING_INTERVAL;

    private Map<String, GCBeanMetrics> gcData = new HashMap<>();
    private Logger log = LoggerFactory.getLogger(GCStatisticTask.class);

    public void run() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();

        log.info("Start getting GC statistic for {} GC's", gcBeans.size());

        for(;;) {
            for (GarbageCollectorMXBean bean : gcBeans) {
                GCBeanMetrics metrics = gcData.getOrDefault(bean.getName(), new GCBeanMetrics());
                metrics.addData(bean.getCollectionCount(), bean.getCollectionTime());
                gcData.put(bean.getName(), metrics);
            }
            pause();
            logStatistic();
        }
    }

    private void pause() {
        try {
            Thread.sleep(ONE_SECOND_GC_BEAN_POLLING_INTERVAL);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void logStatistic() {
        int index = 0;
        for (String name : gcData.keySet()) {
            log.info("[{}] {}", ++index, name);
            GCBeanMetrics metrics = gcData.get(name);
            log.info("     Collections = {}/s; {}/min", metrics.getLastCount(), metrics.getTotalCount());
            log.info("     Time spent  = {}ms/s; {}ms/min", metrics.getLastTime(), metrics.getTotalTime());
        }
        log.info("");
    }

    private static class GCBeanMetrics {

        Deque<Long> countBuffer = new ArrayDeque<>();
        Deque<Long> timeBuffer = new ArrayDeque<>();

        private long prevCount;
        private long prevTime;

        GCBeanMetrics() {

        }

        void addData(long count, long time) {
            long countDelta = count - prevCount;
            if (countBuffer.size() >= ONE_MINUTE_STATISTIC_WINDOW_SIZE) {
                countBuffer.pollFirst();
            }
            countBuffer.addLast(countDelta);
            prevCount = count;

            long timeDelta = time - prevTime;
            if (timeBuffer.size() >= ONE_MINUTE_STATISTIC_WINDOW_SIZE) {
                timeBuffer.pollFirst();
            }
            timeBuffer.addLast(timeDelta);
            prevTime = time;
        }

        long getTotalCount() {
            long result = 0L;
            for (Long c : countBuffer) {
                result += c;
            }
            return result;
        }

        long getTotalTime() {
            long result = 0L;
            for (Long t : timeBuffer) {
                result += t;
            }
            return result;
        }

        long getLastCount() {
            return countBuffer.peekLast();
        }

        long getLastTime() {
            return timeBuffer.peekLast();
        }
    }
}
