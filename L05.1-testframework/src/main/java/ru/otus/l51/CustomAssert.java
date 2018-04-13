package ru.otus.l51;

public final class CustomAssert {

    private CustomAssert() {
    }

    public static void isTrue(boolean resultIsTrue) throws CustomAssertException {
        if (!resultIsTrue) {
            throw new CustomAssertException("Value is not true");
        }
    }

    public static void isFalse(boolean resultIsFalse) throws CustomAssertException {
        if (resultIsFalse) {
            throw new CustomAssertException("Value is not false");
        }
    }

    public static void areEqual(Object val1, Object val2) throws CustomAssertException {
        if (val1 == null || val2 == null || !val1.equals(val2)) {
            throw new CustomAssertException("Values are not equal");
        }
    }

    public static void fail() throws CustomAssertException {
        throw new CustomAssertException("Failure");
    }
}
