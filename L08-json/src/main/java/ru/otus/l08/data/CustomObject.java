package ru.otus.l08.data;

import java.util.*;

public class CustomObject {

    private boolean mBoolean;
    private byte mByte;
    private short mShort;
    private int mInt;
    private long mLong;
    private float mFloat;
    private double mDouble;
    private String mString;

    private int[] mIntArray;
    private Integer[] mIntegerArray;
    private String[] mStringArray;

    private List<String> mStringList;
    private Map<String, String> mStrStrMap;
    private Map<Integer, String> mIntStrMap;
    private Set<String> mStringSet;
    private Set<Integer> mIntSet;

    private List<Object> mObjListNull;

    private InnerObject mIo = new InnerObject();
    private List<InnerObject> mIoList = new ArrayList<>();
    private Map<Integer, InnerObject> mIntIoMap = new HashMap<>();

    public CustomObject() {
        mBoolean = true;
        mByte = (byte)1;
        mShort = 2;
        mInt = 3;
        mLong = 4;
        mFloat = 5f;
        mDouble = 6.0;
        mString = "hello";

        mIntArray = new int[] {1,2,3};
        mIntegerArray = new Integer[] {4,5,6};
        mStringArray = new String[] {"a", "b", "c"};

        mStringList = new ArrayList<>();
        mStringList.add("str1");
        mStringList.add("str2");
        mStringList.add("str3");

        mStrStrMap = new HashMap<>();
        mStrStrMap.put("k1", "v1");
        mStrStrMap.put("k2", "v2");
        mStrStrMap.put("k3", "v3");

        mStringSet = new HashSet<>();
        mStringSet.add("s1");
        mStringSet.add("s2");
        mStringSet.add("s3");

        mIntStrMap = new HashMap<>();
        mIntStrMap.put(1, "i1");
        mIntStrMap.put(2, "i2");
        mIntStrMap.put(3, "i3");

        mIntSet = new HashSet<>();
        mIntSet.add(1);
        mIntSet.add(2);
        mIntSet.add(3);

        mIoList.add(new InnerObject());
        mIoList.add(new InnerObject());
        mIoList.add(new InnerObject());

        mIntIoMap.put(100, new InnerObject());
        mIntIoMap.put(200, new InnerObject());
        mIntIoMap.put(300, new InnerObject());
    }

    @Override
    public boolean equals(Object obj) {

        if (super.equals(obj)) return true;

        if (!(obj instanceof CustomObject)) return false;

        CustomObject co = (CustomObject)obj;

        if (mBoolean != co.mBoolean ||
            mByte != co.mByte ||
            mShort != co.mShort ||
            mInt != co.mInt ||
            mLong != co.mLong ||
            mFloat != co.mFloat ||
            mDouble != co.mDouble ||
            !mString.equals(co.mString)) {
            return false;
        }

        if (!Arrays.equals(mIntArray, co.mIntArray)) {
            return false;
        }
        if (!Arrays.equals(mIntegerArray, co.mIntegerArray)) {
            return false;
        }
        if (!Arrays.equals(mStringArray, co.mStringArray)) {
            return false;
        }

        if (!mStringList.equals(co.mStringList)) {
            return false;
        }
        if (!mStrStrMap.equals(co.mStrStrMap)) {
            return false;
        }
        if (!mIntStrMap.equals(co.mIntStrMap)) {
            return false;
        }
        if (!mStringSet.equals(co.mStringSet)) {
            return false;
        }
        if (!mIntSet.equals(co.mIntSet)) {
            return false;
        }
        if (mObjListNull != co.mObjListNull) {
            return false;
        }
        if (!mIo.equals(co.mIo)) {
            return false;
        }
        if (!mIoList.equals(co.mIoList)) {
            return false;
        }
        if (!mIntIoMap.equals(co.mIntIoMap)) {
            return false;
        }
        return true;
    }
}
