package com.github.boybeak.adapter;

import android.text.BoringLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/13.
 */

public class Order<T> {

    public List<T> mDataList = null;
    //public DelegateAdapter mAdapter = null;


    public Order(List<T> dataList) {
        this.mDataList = dataList;
    }

    public <E> List<E> findAll (Class<E> eClass) {
        List<E> eList = new ArrayList<>();
        for (T t : mDataList) {
            if (eClass.isInstance(t)) {
                eList.add((E)t);
            }

        }
        return eList;

    }
}
