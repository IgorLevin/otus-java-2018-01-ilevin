package ru.otus.l08;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomJsonWriter {

    private static Logger log = LoggerFactory.getLogger(CustomJsonWriter.class);

    private static final String ALLIGNER = "  ";
    private static final String INNER_ALLIGNER = ALLIGNER + ALLIGNER;
    private static boolean prettyOutput = true;
    private static boolean printNullValues = false;

    public static String writeObject(Object object) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(prettyOutput ? "{\n" : "{");
            printObjectFields(object, sb);
            sb.append(prettyOutput ? "\n}" : "}");
        } catch (Exception e) {
            log.error("WTF ", e);
        }
        return sb.toString();
    }


    private static void printObjectFields(Object object, StringBuilder sb) throws Exception {
        if (object == null) return;
        List<String> fieldNames = new ArrayList<>();
        printClassNonStaticFields(object, object.getClass(), fieldNames);
        if (fieldNames.isEmpty()) {
            log.info("Element doesn't have fields");
        } else {
            if (prettyOutput) {
                sb.append(ALLIGNER);
            }
            sb.append(String.join((prettyOutput ? (",\n" + ALLIGNER) : ","), fieldNames));
        }
    }

    private static void printClassNonStaticFields(Object o, Class clazz, List<String> fieldNameValue) throws Exception {
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    String value = printField(o, f);
                    if (value != null) {
                        fieldNameValue.add("\"" + f.getName() + "\":" + (prettyOutput ? " " : "") + value);
                    } else if (printNullValues) {
                        fieldNameValue.add("\"" + f.getName() + "\":" + (prettyOutput ? " " : "") + "null");
                    }
                }
            }
        }
        if (!clazz.getName().contains("Object")) {
            printClassNonStaticFields(o, clazz.getSuperclass(), fieldNameValue);
        }
    }

    private static boolean isSimpleTypeWrapper(Field f) {
        return f.getType().getTypeName().equals("Boolean") ||
                f.getType().getTypeName().equals("Byte") ||
                f.getType().getTypeName().equals("Short") ||
                f.getType().getTypeName().equals("Integer") ||
                f.getType().getTypeName().equals("Long") ||
                f.getType().getTypeName().equals("Float") ||
                f.getType().getTypeName().equals("Double");
    }

    private static String printField(Object o, Field f) throws Exception {
        if (isSimpleTypeField(f)) {
            return printSimpleTypeField(o, f);
        } else {
            return printComplexType(o, f);
        }
    }

    private static boolean isSimpleTypeField(Field f) {
        return f.getType().getTypeName().equals("boolean") ||
                f.getType().getTypeName().equals("byte") ||
                f.getType().getTypeName().equals("short") ||
                f.getType().getTypeName().equals("int") ||
                f.getType().getTypeName().equals("long") ||
                f.getType().getTypeName().equals("float") ||
                f.getType().getTypeName().equals("double") ||
                f.getType().getTypeName().equals("java.lang.String") ||
                isSimpleTypeWrapper(f);
    }

    private static boolean isStringCollectionField(Field f) {
        return f.getGenericType().getTypeName().contains("String");
    }

    private static String printSimpleTypeField(Object o, Field f) throws Exception {
        String typeName = f.getType().getTypeName();
        switch (typeName) {
            case "boolean":
            case "Boolean":
                return printBoolean(f.getBoolean(o));
            case "byte":
            case "Byte":
                return printByte(f.getByte(o));
            case "short":
            case "Short":
                return printShort(f.getShort(o));
            case "int":
            case "Integer":
                return printInt(f.getInt(o));
            case "long":
            case "Long":
                return printLong(f.getLong(o));
            case "float":
            case "Float":
                return printFloat(f.getFloat(o));
            case "double":
            case "Double":
                return printDouble(f.getDouble(o));
            case "java.lang.String":
                return printString((String)f.get(o));
            default:
                return null;
        }
    }

    @SuppressWarnings("unchecked")
    private static String printComplexType(Object o, Field f) throws Exception {
        String typeName = f.getType().getTypeName();
        StringBuilder sb = new StringBuilder();
        if (typeName.contains("[]")) {
            return printArrayValues(o, f);
        }
        else if (typeName.contains("List")) {
            List<Object> lst = (List<Object>)f.get(o);
            if (lst == null) return null;
            if (isStringCollectionField(f)) {
                String[] arr = lst.toArray(new String[]{});
                sb.append(printArray(arr, true));
            } else {
                for (Object obj : lst) {
                    return writeObject(obj);
                }
            }
        } else if (typeName.contains("Map")) {
//            Map<Object, Object> map = (Map<Object, Object>)f.get(o);
//            if (map == null) return null;
//            Set<Map.Entry<Object, Object>> set = map.entrySet();
//            for (Map.Entry<Object, Object> entry : set) {
//                sb.append(entry.getKey().getClass().getCanonicalName() + "=" + entry.getValue().getClass().getCanonicalName());
//            }

        } else if (typeName.contains("Set")) {
            Set<Object> set = (Set<Object>)f.get(o);
            if (set == null) return null;
            if (isStringCollectionField(f)) {
                String[] arr = set.toArray(new String[]{});
                sb.append(printArray(arr, true));
            } else {
                for (Object obj : set) {
                    return writeObject(obj);
                }
            }
        }
        return sb.toString();
    }


    private static String printArrayValues(Object o, Field f) throws Exception {
        String typeName = f.getType().getTypeName();

        StringBuilder sb = new StringBuilder();
        if (typeName.contains("boolean")) {
            boolean[] arr = (boolean[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("byte")) {
            byte[] arr = (byte[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("short")) {
            short[] arr = (short[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("int")) {
            int[] arr = (int[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("long")) {
            long[] arr = (long[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("float")) {
            float[] arr = (float[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("double")) {
            double[] arr = (double[])f.get(o);
            sb.append(printArray(wrapArray(arr), false));
        } else if (typeName.contains("String")) {
            String[] arr = (String[])f.get(o);
            sb.append(printArray(arr, true));
        } else {
            Object[] arr = (Object[])f.get(o);
            for (Object obj : arr) {
                sb.append(writeObject(obj));
            }
        }
        return sb.toString();
    }

    private static String printBoolean(boolean val) {
        return String.valueOf(val);
    }

    private static String printByte(byte val) {
        return String.valueOf(val);
    }

    private static String printShort(short val) {
        return String.valueOf(val);
    }

    private static String printInt(int val) {
        return String.valueOf(val);
    }

    private static String printLong(long val) {
        return String.valueOf(val);
    }

    private static String printFloat(float val) {
        return String.valueOf(val);
    }

    private static String printDouble(double val) {
        return String.valueOf(val);
    }

    private static String printString(String val) {
        if (val == null) {
            return null;
        } else {
            return "\"" + String.valueOf(val) + "\"";
        }
    }


    private static <T> String printArray(T[] arr, boolean isValueQuoted) {
        if (arr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(prettyOutput ? "[\n" : "[");
        String[] values = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            if (isValueQuoted) {
                values[i] = "\"" + String.valueOf(arr[i]) + "\"";
            } else {
                values[i] = String.valueOf(arr[i]);
            }
        }
        if (values.length > 0) {
            if (prettyOutput) {
                sb.append(INNER_ALLIGNER);
            }
            sb.append(String.join((prettyOutput ? (",\n" + INNER_ALLIGNER) : ","), values));
        }
        sb.append(prettyOutput ? ("\n" + ALLIGNER + "]") : "]");
        return sb.toString();
    }

    private static Boolean[] wrapArray(boolean[] input) {
        if (input == null) return null;
        Boolean[] out = new Boolean[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    private static Byte[] wrapArray(byte[] input) {
        if (input == null) return null;
        Byte[] out = new Byte[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    private static Short[] wrapArray(short[] input) {
        if (input == null) return null;
        Short[] out = new Short[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    private static Integer[] wrapArray(int[] input) {
        if (input == null) return null;
        Integer[] out = new Integer[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    private static Long[] wrapArray(long[] input) {
        if (input == null) return null;
        Long[] out = new Long[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    private static Float[] wrapArray(float[] input) {
        if (input == null) return null;
        Float[] out = new Float[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    private static Double[] wrapArray(double[] input) {
        if (input == null) return null;
        Double[] out = new Double[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }
}
