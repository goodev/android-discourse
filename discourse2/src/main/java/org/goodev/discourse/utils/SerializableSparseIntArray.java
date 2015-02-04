package org.goodev.discourse.utils;

import android.util.SparseIntArray;

import java.io.Serializable;

public class SerializableSparseIntArray extends SparseIntArray implements Serializable {

    public SerializableSparseIntArray() {
        super();
    }

    public SerializableSparseIntArray(int initialCapacity) {
        super(initialCapacity);
    }

}
