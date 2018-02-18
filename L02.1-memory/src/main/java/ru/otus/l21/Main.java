package ru.otus.l21;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

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

    private static int SIZE = 20_000_000;
    private static Logger log = LoggerFactory.getLogger(Main.class);

    private interface InstanceCreator {
        Object newInstance();
    }

    public static void main(String... args) throws Exception {

        log.info("PID: {}", ManagementFactory.getRuntimeMXBean().getName());

        freeMem();

        Runtime runtime = Runtime.getRuntime();
        log.info("Free memory: {}", runtime.freeMemory());

        log.info("");
        log.info("[ Object() ]");
        printClassInstanceInfo(Object::new);

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ Integer(0) ]");
        printClassInstanceInfo(() -> (0));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ Integer(129) ]");
        printClassInstanceInfo(() -> (129));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ Double(0.0) ]");
        printClassInstanceInfo(() -> (0.0d));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ String(\"\") ]");
        printClassInstanceInfo(() -> new String(""));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ String(new char[0]) ]");
        printClassInstanceInfo(() -> new String(new char[0]));

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ MyClass() ]");
        printClassInstanceInfo(MyClass::new);

        Thread.sleep(1000); //wait for 1 sec
        freeMem();

        log.info("");
        log.info("[ MyClass2() ]");
        printClassInstanceInfo(MyClass2::new);

        Thread.sleep(1000); //wait for 1 sec
        freeMem();
    }

    private static void printClassInstanceInfo(InstanceCreator instr) {

        Object[] array = new Object[SIZE];

        Runtime runtime = Runtime.getRuntime();
        long mem1 = runtime.totalMemory() - runtime.freeMemory();

        for (int i = 0; i < array.length; i++) {
            array[i] = instr.newInstance();
        }

        long mem2 = runtime.totalMemory() - runtime.freeMemory();

        log.info("Element size: {}",(float)(mem2 - mem1)/array.length);
        printObjectFields(array[0]);
    }

    private static void printObjectFields(Object object) {
        if (object == null) return;
        List<String> fieldNames = new ArrayList<>();
        listClassNonStaticFields(object.getClass(), fieldNames);
        if (fieldNames.isEmpty()) {
            log.info("Element doesn't have fields");
        } else {
            log.info("Element fields :");
            for (String s : fieldNames) {
                log.info("   {}", s);
            }
        }
    }

    private static void listClassNonStaticFields(Class clazz, List<String> listOfNames) {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    listOfNames.add(f.getType().getTypeName());
                }
            }
        }
        if (!clazz.getName().contains("Object")) {
            listClassNonStaticFields(clazz.getSuperclass(), listOfNames);
        }
    }

    private static void freeMem() throws Exception {
        System.gc();
        Thread.sleep(10);
    }

    private static class MyClass {
        private int i = 0;
        private long l = 1;
    }

    private static class MyClass2 extends MyClass {
        private double d = 123.456d;
    }
}
