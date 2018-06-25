package ru.otus.l08.data;

import java.util.*;

class InnerObject {

    private int i = 2;
    private String s1 = "s1";

    private List<String> mStrList = new ArrayList<>();
    private Set<Integer> mIntSet = new HashSet<>();
    private Map<String, String> mStrStrMap = new HashMap<>();
    private Map<Integer, String> mIntStrMap = new HashMap<>();

    InnerObject() {

        mStrList.add("Str1");
        mStrList.add("Str2");
        mStrList.add("Str3");

        mIntSet.add(1);
        mIntSet.add(2);
        mIntSet.add(3);

        mStrStrMap.put("k1", "v1");
        mStrStrMap.put("k2", "v2");
        mStrStrMap.put("k3", "v3");

        mIntStrMap.put(1, "s1");
        mIntStrMap.put(2, "s2");
        mIntStrMap.put(3, "s3");
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) return true;

        if (!(obj instanceof InnerObject)) return false;

        InnerObject io = (InnerObject)obj;

        if (i != io.i || !s1.equals(io.s1)) return false;

        if (!mStrList.equals(io.mStrList)) {
            return false;
        }
        if (!mIntSet.equals(io.mIntSet)) {
            return false;
        }
        if (!mStrStrMap.equals(io.mStrStrMap)) {
            return false;
        }
        if (!mIntStrMap.equals(io.mIntStrMap)) {
            return false;
        }
        return true;
    }
}
