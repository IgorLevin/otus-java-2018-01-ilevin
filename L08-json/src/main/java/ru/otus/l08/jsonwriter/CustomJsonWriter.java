package ru.otus.l08.jsonwriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;

import static ru.otus.l08.jsonwriter.Tools.*;

public class CustomJsonWriter {

    private static Logger log = LoggerFactory.getLogger(CustomJsonWriter.class);
    private static boolean printNullValues = false;

    @SuppressWarnings("unchecked")
    public static String writeObject(Object object) throws Exception {
        if (object == null) return null;

        Type t = object.getClass();

        if (isSimpleType(t)) {
            return object.toString();
        }

        if (isStringType(t)) {
            return "\"" + object.toString() + "\"";
        }

        if (isArrayType(t)) {
            return "[" + printArray(object) + "]";
        }

        if (object instanceof Map) {
            return "{" + printMap((Map)object) + "}";
        }

        if (object instanceof List) {
            return "[" + printList((List)object) + "]";
        }

        if (object instanceof Set) {
            return "[" + printSet((Set)object) + "]";
        }

        return "{" + printObjectFields(object) + "}";
    }

    public static boolean isPrintNullValues() {
        return printNullValues;
    }

    public static void setPrintNullValues(boolean printNullValues) {
        CustomJsonWriter.printNullValues = printNullValues;
    }

    private static String printObjectFields(Object object) throws Exception {

        if (object == null) return null;

        StringBuilder sb = new StringBuilder();

        List<String> fields = printClassNonStaticFields(object.getClass(), object);

        if (fields.isEmpty()) {
            log.info("Element doesn't have fields");
        } else {
            sb.append(String.join(",", fields));
        }
        return sb.toString();
    }

    private static List<String> printClassNonStaticFields(Class clazz, Object obj) throws Exception {
        List<String> fieldList = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    f.setAccessible(true);
                    String value = writeObject(f.get(obj));
                    if (value != null) {
                        fieldList.add("\"" + f.getName() + "\":" + value);
                    } else if (printNullValues) {
                        fieldList.add("\"" + f.getName() + "\":" + "null");
                    }
                }
            }
        }
        if (!clazz.getName().contains("Object")) {
            printClassNonStaticFields(clazz.getSuperclass(), obj);
        }
        return fieldList;
    }

    private static boolean isSimpleType(Type t) {
        if (t == null) return false;
        String name = t.getTypeName();
        return name.equals("java.lang.Boolean") ||
                name.equals("java.lang.Byte") ||
                name.equals("java.lang.Short") ||
                name.equals("java.lang.Integer") ||
                name.equals("java.lang.Long") ||
                name.equals("java.lang.Float") ||
                name.equals("java.lang.Double");
    }

    private static boolean isStringType(Type t) {
        if (t == null) return false;
        String name = t.getTypeName();
        return name.equals("java.lang.String");
    }

    private static boolean isArrayType(Type t) {
        if (t == null) return false;
        return t.getTypeName().contains("[]");
    }

    private static String printMap(Map<Object, Object> map) throws Exception {
        Set<Map.Entry<Object,Object>> es = map.entrySet();
        List<String> lines = new ArrayList<>();
        for (Map.Entry<Object, Object> entry : es) {
            String key;
            if (isSimpleType(entry.getKey().getClass())) {
                key = "\"" + writeObject(entry.getKey()) + "\"";
            } else {
                key = writeObject(entry.getKey());
            }
            String value = writeObject(entry.getValue());
            lines.add(String.format("%1$s:%2$s", key, value));
        }
        String delimiter = ",";
        String result = String.join(delimiter, lines);
        return result;
    }

    private static String printList(List<Object> list) throws Exception {
        if (list == null) return null;
        return printArray(list.toArray(new Object[]{}));
    }

    private static String printSet(Set<Object> set) throws Exception {
        if (set == null) return null;
        return printArray(set.toArray(new Object[]{}));
    }

    private static String printArray(Object object) throws Exception {
        String typeName = object.getClass().getTypeName();

        StringBuilder sb = new StringBuilder();
        if (typeName.contains("boolean")) {
            boolean[] arr = (boolean[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("byte")) {
            byte[] arr = (byte[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("short")) {
            short[] arr = (short[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("int")) {
            int[] arr = (int[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("long")) {
            long[] arr = (long[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("float")) {
            float[] arr = (float[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("double")) {
            double[] arr = (double[]) object;
            sb.append(printArray(wrapArray(arr)));
        } else if (typeName.contains("String")) {
            String[] arr = (String[]) object;
            sb.append(printArray(arr));
        } else {
            Object[] arr = (Object[]) object;
            sb.append(printArray(arr));
        }
        return sb.toString();
    }


    private static <T> String printArray(T[] arr) throws Exception {
        if (arr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        List<String> lines = new ArrayList<>();
        for (T anArr : arr) {
            lines.add(writeObject(anArr));
        }
        if (!lines.isEmpty()) {
            sb.append(String.join(",", lines));
        }
        return sb.toString();
    }
}
