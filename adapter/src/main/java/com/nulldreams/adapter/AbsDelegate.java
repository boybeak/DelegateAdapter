package com.nulldreams.adapter;

import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;

import com.nulldreams.adapter.impl.DelegateImpl;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class AbsDelegate<T> implements DelegateImpl {

    private T t;

    private Bundle bundle;

    /**
     * @param t the source data item
     */
    public AbsDelegate (T t) {
        this.t = t;
    }

    public AbsDelegate (T t, Bundle bundle) {
        this.t = t;
        this.bundle = bundle;
    }

    @Override
    public T getSource () {
        return t;
    }

    public void setSource (T t) {
        this.t = t;
    }

    private void ensureBundle () {
        if (bundle == null) {
            bundle = new Bundle();
        }
    }

    public void setBundle (Bundle bundle) {
        this.bundle = bundle;
    }

    public Bundle getBundle() {
        ensureBundle();
        return bundle;
    }

    public AbsDelegate putAll (Bundle bundle) {
        ensureBundle();
        bundle.putAll(bundle);
        return this;
    }

    public AbsDelegate putInt (String key, int value) {
        ensureBundle();
        bundle.putInt(key, value);
        return this;
    }

    public AbsDelegate putIntArray (String key, int[] value) {
        ensureBundle();
        bundle.putIntArray(key, value);
        return this;
    }

    public AbsDelegate putIntegerArrayList (String key, ArrayList<Integer> value) {
        ensureBundle();
        bundle.putIntegerArrayList(key, value);
        return this;
    }

    public AbsDelegate putBoolean (String key, boolean value) {
        ensureBundle();
        bundle.putBoolean(key, value);
        return this;
    }

    public AbsDelegate putBooleanArray (String key, boolean[] value) {
        ensureBundle();
        bundle.putBooleanArray(key, value);
        return this;
    }

    public AbsDelegate putString (String key, String value) {
        ensureBundle();
        bundle.putString(key, value);
        return this;
    }

    public AbsDelegate putStringArray (String key, String[] value) {
        ensureBundle();
        bundle.putStringArray(key, value);
        return this;
    }

    public AbsDelegate putStringArrayList (String key, ArrayList<String> value) {
        ensureBundle();
        bundle.putStringArrayList(key, value);
        return this;
    }

    public AbsDelegate putLong (String key, long value) {
        ensureBundle();
        bundle.putLong(key, value);
        return this;
    }

    public AbsDelegate putLongArray (String key, long[] value) {
        ensureBundle();
        bundle.putLongArray(key, value);
        return this;
    }

    public AbsDelegate putFloat (String key, float value) {
        ensureBundle();
        bundle.putFloat(key, value);
        return this;
    }

    public AbsDelegate putFloatArray (String key, float[] value) {
        ensureBundle();
        bundle.putFloatArray(key, value);
        return this;
    }

    public AbsDelegate putDouble (String key, double value) {
        ensureBundle();
        bundle.putDouble(key, value);
        return this;
    }

    public AbsDelegate putDoubleArray (String key, double[] value) {
        ensureBundle();
        bundle.putDoubleArray(key, value);
        return this;
    }

    public AbsDelegate putByte (String key, byte value) {
        ensureBundle();
        bundle.putByte(key, value);
        return this;
    }

    public AbsDelegate putByteArray (String key, byte[] value) {
        ensureBundle();
        bundle.putByteArray(key, value);
        return this;
    }

    public AbsDelegate putChar (String key, char value) {
        ensureBundle();
        bundle.putChar(key, value);
        return this;
    }

    public AbsDelegate putCharArray (String key, char[] value) {
        ensureBundle();
        bundle.putCharArray(key, value);
        return this;
    }

    public AbsDelegate putCharSequence (String key, CharSequence value) {
        ensureBundle();
        bundle.putCharSequence(key, value);
        return this;
    }

    public AbsDelegate putCharSequenceArray (String key, CharSequence[] value) {
        ensureBundle();
        bundle.putCharSequenceArray(key, value);
        return this;
    }

    public AbsDelegate putCharSequenceArrayList (String key, ArrayList<CharSequence> value) {
        ensureBundle();
        bundle.putCharSequenceArrayList(key, value);
        return this;
    }


    public AbsDelegate putShort (String key, short value) {
        ensureBundle();
        bundle.putShort(key, value);
        return this;
    }

    public AbsDelegate putShortArray (String key, short[] value) {
        ensureBundle();
        bundle.putShortArray(key, value);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsDelegate putSize (String key, Size value) {
        ensureBundle();
        bundle.putSize(key, value);
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public AbsDelegate putSizeF (String key, SizeF value) {
        ensureBundle();
        bundle.putSizeF(key, value);
        return this;
    }

    public AbsDelegate putParcelable (String key, Parcelable value) {
        ensureBundle();
        bundle.putParcelable(key, value);
        return this;
    }

    public AbsDelegate putParcelableArray (String key, Parcelable[] value) {
        ensureBundle();
        bundle.putParcelableArray(key, value);
        return this;
    }

    public AbsDelegate putParcelableArrayList (String key, ArrayList<? extends Parcelable> value) {
        ensureBundle();
        bundle.putParcelableArrayList(key, value);
        return this;
    }

    public AbsDelegate putSparseParcelableArray (String key, SparseArray<? extends Parcelable> value) {
        ensureBundle();
        bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public AbsDelegate putSerializable (String key, Serializable value) {
        ensureBundle();
        bundle.putSerializable(key, value);
        return this;
    }

    public int getInt (String key) {
        ensureBundle();
        return bundle.getInt(key);
    }

    public int getInt (String key, int defaultValue) {
        ensureBundle();
        return bundle.getInt(key, defaultValue);
    }

    public int[] getIntegerArray (String key) {
        ensureBundle();
        return bundle.getIntArray(key);
    }

    public ArrayList<Integer> getIntegerArrayList (String key) {
        ensureBundle();
        return bundle.getIntegerArrayList(key);
    }

    public boolean getBoolean (String key) {
        ensureBundle();
        return bundle.getBoolean(key);
    }

    public boolean getBoolean (String key, boolean defaultValue) {
        ensureBundle();
        return bundle.getBoolean(key, defaultValue);
    }

    public boolean[] getBooleanArray (String key) {
        ensureBundle();
        return bundle.getBooleanArray(key);
    }

    public byte getByte (String key) {
        ensureBundle();
        return bundle.getByte(key);
    }

    public byte getByte (String key, byte defaultValue) {
        ensureBundle();
        return bundle.getByte(key, defaultValue);
    }

    public byte[] getByteArray (String key) {
        ensureBundle();
        return bundle.getByteArray(key);
    }

    public short getShort (String key) {
        ensureBundle();
        return bundle.getShort(key);
    }

    public short getShort (String key, short defaultValue) {
        ensureBundle();
        return bundle.getShort(key, defaultValue);
    }

    public short[] getShortArray (String key) {
        ensureBundle();
        return bundle.getShortArray(key);
    }

    public long getLong (String key) {
        ensureBundle();
        return bundle.getLong(key);
    }

    public long getLong (String key, long defaultValue) {
        ensureBundle();
        return bundle.getLong(key, defaultValue);
    }

    public long[] getLongArray (String key) {
        ensureBundle();
        return bundle.getLongArray(key);
    }

    public float getFloat (String key) {
        ensureBundle();
        return bundle.getFloat(key);
    }

    public float getFloat (String key, float defaultValue) {
        ensureBundle();
        return bundle.getFloat(key, defaultValue);
    }

    public float[] getFloatArray (String key) {
        ensureBundle();
        return bundle.getFloatArray(key);
    }

    public double getDouble (String key) {
        ensureBundle();
        return bundle.getDouble(key);
    }

    public double getDouble (String key, double defaultValue) {
        ensureBundle();
        return bundle.getDouble(key, defaultValue);
    }

    public double[] getDoubleArray (String key) {
        ensureBundle();
        return bundle.getDoubleArray(key);
    }

    public char getChar (String key) {
        ensureBundle();
        return bundle.getChar(key);
    }

    public char getChar (String key, char defaultValue) {
        ensureBundle();
        return bundle.getChar(key, defaultValue);
    }

    public char[] getCharArray (String key) {
        ensureBundle();
        return bundle.getCharArray(key);
    }

    public CharSequence getCharSequence (String key) {
        ensureBundle();
        return bundle.getCharSequence(key);
    }

    public CharSequence getCharSequence (String key, CharSequence defaultValue) {
        ensureBundle();
        return bundle.getCharSequence(key, defaultValue);
    }

    public CharSequence[] getCharSequenceArray (String key) {
        ensureBundle();
        return bundle.getCharSequenceArray(key);
    }

    public ArrayList<CharSequence> getCharSequenceArrayList (String key) {
        ensureBundle();
        return bundle.getCharSequenceArrayList(key);
    }

    public String getString (String key) {
        ensureBundle();
        return bundle.getString(key);
    }

    public String getString (String key, String defaultValue) {
        ensureBundle();
        return bundle.getString(key, defaultValue);
    }

    public String[] getStringArray (String key) {
        ensureBundle();
        return bundle.getStringArray(key);
    }

    public ArrayList<String> getStringArrayList (String key) {
        ensureBundle();
        return bundle.getStringArrayList(key);
    }

    public Parcelable getParcelable (String key) {
        ensureBundle();
        return bundle.getParcelable(key);
    }

    public Parcelable[] getParcelableArray (String key) {
        ensureBundle();
        return bundle.getParcelableArray(key);
    }

    public ArrayList<Parcelable> getParcelableArrayList (String key) {
        ensureBundle();
        return bundle.getParcelableArrayList(key);
    }

    public SparseArray<Parcelable> getSparseParcelableArray (String key) {
        ensureBundle();
        return bundle.getSparseParcelableArray(key);
    }

    public Serializable getSerializable (String key) {
        ensureBundle();
        return bundle.getSerializable(key);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Size getSize (String key) {
        ensureBundle();
        return bundle.getSize(key);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SizeF getSizeF (String key) {
        ensureBundle();
        return bundle.getSizeF(key);
    }

    public Bundle remove (String key) {
        ensureBundle();
        bundle.remove(key);
        return bundle;
    }

}
