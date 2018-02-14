package ru.otus.l21;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;

/**
 * VM options -Xmx512m -Xms512m
 * <p>
 * Runtime runtime = Runtime.getRuntime();
 * long mem = runtime.totalMemory() - runtime.freeMemory();
 * <p>
 * System.gc()
 * <p>
 * jconsole, connect to pid
 */
@SuppressWarnings({"RedundantStringConstructorCall", "InfiniteLoopStatement"})
public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    private interface ElementCreator {
        Object newInstance();
    }

    public static void main(String... args) throws Exception {

        log.info("PID: {}", ManagementFactory.getRuntimeMXBean().getName());

        int size = 20_000_000;

        freeMem();

        Runtime runtime = Runtime.getRuntime();
        log.info("Free memory: {}", runtime.freeMemory());

        log.info("");
        log.info("[ Object() ]");
        fillArrayAndCheckElementSize(size, Object::new);

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ String(\"\") ]");
        fillArrayAndCheckElementSize(size, () -> new String(""));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ String(new char[0]) ]");
        fillArrayAndCheckElementSize(size, () -> new String(new char[0]));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ MyClass() ]");
        fillArrayAndCheckElementSize(size, MyClass::new);

        Thread.sleep(1000); //wait for 1 sec
        freeMem();
    }

    private static void fillArrayAndCheckElementSize(int size, ElementCreator instr) throws Exception {

        Object[] array = new Object[size];

        Runtime runtime = Runtime.getRuntime();
        long mem1 = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < array.length; i++) {
            array[i] = instr.newInstance();
        }

        long mem2 = runtime.totalMemory() - runtime.freeMemory();

        log.info("Element size: {}",(float)(mem2 - mem1)/array.length);
    }

    private static void freeMem() throws Exception {
        System.gc();
        Thread.sleep(10);
    }

    private static class MyClass {
        private int i = 0;
        private long l = 1;
    }
}
