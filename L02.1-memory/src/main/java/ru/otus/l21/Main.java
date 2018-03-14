package ru.otus.l21;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

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

        analyzeSimpleTypes();

        // Prepare data source for Collection and Map
        CollectionTestElement[] srcCollection = new CollectionTestElement[SIZE];
        for (int i = 0; i < SIZE; i++) {
            srcCollection[i] = new CollectionTestElement();
        }

        analyzeCollections(srcCollection);

        analyzeMaps(srcCollection);

        log.info("Done");
    }

    private static void analyzeSimpleTypes() throws Exception {
        log.info("");
        log.info(" *** Simple Types ***");

        freeMem();

        log.info("");
        log.info("[ Object() ]");
        printClassInstanceInfo(Object::new);

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ Integer(0) ]");
        printClassInstanceInfo(() -> (0));

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ Integer(129) ]");
        printClassInstanceInfo(() -> (129));

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ Double(0.0) ]");
        printClassInstanceInfo(() -> (0.0d));

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ String(\"\") ]");
        printClassInstanceInfo(() -> new String(""));

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ String(new char[0]) ]");
        printClassInstanceInfo(() -> new String(new char[0]));

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ MyClass() ]");
        printClassInstanceInfo(MyClass::new);

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ MyClass2() ]");
        printClassInstanceInfo(MyClass2::new);

        Thread.sleep(1000);
        freeMem();
    }

    private static void analyzeCollections(CollectionTestElement[] srcCollection) throws Exception {

        log.info("");
        log.info(" *** Collections ***");

        freeMem();

        log.info("");
        log.info("[ ArrayList() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new ArrayList<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ LinkedList() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new LinkedList<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ Vector() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new Vector<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ Stack() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new Stack<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ HashSet() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new HashSet<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ LinkedHashSet() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new LinkedHashSet<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ TreeSet() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new TreeSet<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ PriorityQueue() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new PriorityQueue<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ PriorityQueue() ]");
        printCollectionMemoryUsagePerElement(srcCollection, new ArrayDeque<>());

        Thread.sleep(1000);
        freeMem();
    }

    private static void analyzeMaps(CollectionTestElement[] srcCollection) throws Exception {

        log.info("");
        log.info(" *** Maps ***");

        freeMem();

        log.info("");
        log.info("[ HashMap() ]");
        printMapMemoryUsagePerElement(srcCollection, new HashMap<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ LinkedHashMap() ]");
        printMapMemoryUsagePerElement(srcCollection, new LinkedHashMap<>());

        Thread.sleep(1000);
        freeMem();

        log.info("");
        log.info("[ TreeMap() ]");
        printMapMemoryUsagePerElement(srcCollection, new TreeMap<>());

        Thread.sleep(1000);
        freeMem();
    }

    private static void printCollectionMemoryUsagePerElement(CollectionTestElement[] src, Collection<CollectionTestElement> collection) {

        Runtime runtime = Runtime.getRuntime();
        long mem1 = runtime.totalMemory() - runtime.freeMemory();
        collection.addAll(Arrays.asList(src).subList(0, SIZE));
        long mem2 = runtime.totalMemory() - runtime.freeMemory();

        log.info("Average {} element size: {}", collection.getClass().getName(), (float)(mem2 - mem1)/collection.size());
    }

    private static void printMapMemoryUsagePerElement(CollectionTestElement[] src, Map<CollectionTestElement, CollectionTestElement> map) {
        Runtime runtime = Runtime.getRuntime();
        long mem1 = runtime.totalMemory() - runtime.freeMemory();
        for (int i = 0; i < src.length; i++) {
               map.put(src[i], src[i]);
        }
        long mem2 = runtime.totalMemory() - runtime.freeMemory();

        log.info("Average {} element size: {}", map.getClass().getName(), (float)(mem2 - mem1)/map.size());
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

    private static class CollectionTestElement implements Comparable <CollectionTestElement> {
        private long l = 1;

        CollectionTestElement() {
            l = System.nanoTime();
        }

        @Override
        public int compareTo(CollectionTestElement o) {
            if (this == o) {
                return 0;
            }
            long diff = this.l - o.l;
            return (diff <= 0) ? -1 : 1;
        }
    }
}
