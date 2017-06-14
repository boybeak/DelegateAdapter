package com.github.boybeak.adapter;

import java.util.List;

/**
 * Created by gaoyunfei on 2017/6/13.
 */

public class Order {

    public List mDataList = null;
    //public DelegateAdapter mAdapter = null;


    public Order(List dataList) {
        this.mDataList = dataList;
    }

    public <T> Selector<T> selector (Class<T> tClass) {
        return new Selector<T>(tClass, this);
    }

    public List getDataList () {
        return mDataList;
    }
}
