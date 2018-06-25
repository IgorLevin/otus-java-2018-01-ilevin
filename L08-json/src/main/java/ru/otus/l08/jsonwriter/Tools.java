package ru.otus.l08.jsonwriter;

class Tools {

    static Boolean[] wrapArray(boolean[] input) {
        if (input == null) return null;
        Boolean[] out = new Boolean[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    static Byte[] wrapArray(byte[] input) {
        if (input == null) return null;
        Byte[] out = new Byte[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    static Short[] wrapArray(short[] input) {
        if (input == null) return null;
        Short[] out = new Short[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    static Integer[] wrapArray(int[] input) {
        if (input == null) return null;
        Integer[] out = new Integer[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    static Long[] wrapArray(long[] input) {
        if (input == null) return null;
        Long[] out = new Long[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    static Float[] wrapArray(float[] input) {
        if (input == null) return null;
        Float[] out = new Float[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }

    static Double[] wrapArray(double[] input) {
        if (input == null) return null;
        Double[] out = new Double[input.length];
        for (int i = 0; i < input.length; i++) {
            out[i] = input[i];
        }
        return out;
    }
}
