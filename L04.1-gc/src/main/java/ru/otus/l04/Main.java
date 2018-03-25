package ru.otus.l04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application for studying GC lifecycle and heap dump creation
 * Heap dump analyzed with tool "jvisualvm".
 *
 * VM parameters for Idea run configuration:
     -Xms128m
     -Xmx128m
     -XX:+UseG1GC
     -verbose:gc
     -Xloggc:./logs/gc_pid_%p.log
     -XX:+UseGCLogFileRotation
     -XX:NumberOfGCLogFiles=10
     -XX:GCLogFileSize=1M
     -XX:+PrintGCDetails
     -XX:+PrintGCDateStamps
     -XX:+HeapDumpOnOutOfMemoryError
     -XX:HeapDumpPath=./dumps/
     -Dcom.sun.management.jmxremote.port=15000
     -Dcom.sun.management.jmxremote.authenticate=false
     -Dcom.sun.management.jmxremote.ssl=false
 */
public class Main {
	
	private static Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String... args) {
        log.info("Starting pid: {}", ManagementFactory.getRuntimeMXBean().getName());

        ExecutorService es = Executors.newFixedThreadPool(2);
        log.info("Submit memory consumption");
        es.submit(new MemoryConsumeTask());
        log.info("Submit GC statistic");
        es.submit(new GCStatisticTask());

//        log.info("Submit dump creation");
//        es.submit(new HeapDumpCauseTask(10));

    }
}
