package com.nulldreams.adapter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class DelegateAdapter extends RecyclerView.Adapter<AbsViewHolder>{

    private Context mContext = null;
    private List<DelegateImpl> mDelegateImplList = null;
    private TypeLayoutConvert mConvert;

    public DelegateAdapter (Context context) {
        this (context, new SimpleConvert());
    }

    public DelegateAdapter (Context context, TypeLayoutConvert convert) {
        mContext = context;
        mDelegateImplList = new ArrayList<>();
        mConvert = convert;
    }

    @Override
    public final AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(mConvert.convert(viewType), null);
        return onCreateAbsViewHolder(parent, viewType, itemView);
    }

    public abstract AbsViewHolder onCreateAbsViewHolder (ViewGroup parent, int viewType, View itemView);

    @Override
    public void onBindViewHolder(AbsViewHolder holder, int position) {
        holder.onBindView(mContext, mDelegateImplList.get(position), position, this);
    }

    @Override
    public int getItemCount() {
        return mDelegateImplList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegateImplList.get(position).getType();
    }

    @Override
    public void onViewAttachedToWindow(AbsViewHolder holder) {
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(AbsViewHolder holder) {
        holder.onViewDetachedFromWindow();
    }

    public boolean isEmpty () {
        return mDelegateImplList.isEmpty();
    }

    public void clear () {
        mDelegateImplList.clear();
    }

    public void add(DelegateImpl impl) {
        mDelegateImplList.add(impl);
    }

    public void addIfNotExist (DelegateImpl impl) {
        if (impl == null) {
            throw new NullPointerException("impl shouldn't be null");
        }
        if (contains(impl)) {
            return;
        }
        mDelegateImplList.add(impl);
    }

    public void addIfNotExist (int position, DelegateImpl impl) {
        if (impl == null) {
            throw new NullPointerException("impl shouldn't be null");
        }
        if (contains(impl)) {
            return;
        }
        mDelegateImplList.add(position, impl);
    }

    public void add(int position, DelegateImpl impl) {
        if (impl == null) {
            throw new NullPointerException("impl shouldn't be null");
        }
        mDelegateImplList.add(position, impl);
    }

    public void addAll(Collection<? extends DelegateImpl> list) {
        if (list == null) {
            throw new NullPointerException("list shouldn't be null");
        }
        mDelegateImplList.addAll(list);
    }

    public void addAll(int position, Collection<? extends DelegateImpl> list) {
        if (list == null) {
            throw new NullPointerException("list shouldn't be null");
        }
        mDelegateImplList.addAll(position, list);
    }

    public void addAll(DelegateImpl[] array) {
        mDelegateImplList.addAll(Arrays.asList(array));
    }

    public void addAll(int position, DelegateImpl[] array) {
        mDelegateImplList.addAll(position, Arrays.asList(array));
    }

    public <T> void addAll (T[] tArray, DelegateParser<T> parser) {
        Collection<T> collection = Arrays.asList(tArray);
        List<DelegateImpl> delegates = generateDelegateImpls(collection, parser);
        if (delegates != null) {
            addAll(delegates);
        }
    }

    public <T> void addAll (int position, T[] tArray, DelegateParser<T> parser) {
        Collection<T> collection = Arrays.asList(tArray);
        List<DelegateImpl> delegates = generateDelegateImpls(collection, parser);
        if (delegates != null) {
            addAll(position, delegates);
        }
    }

    public <T> void addAll(Collection<T> data, DelegateParser<T> parser) {
        List<DelegateImpl> delegates = generateDelegateImpls(data, parser);
        if (delegates != null) {
            addAll(delegates);
        }
    }

    public <T> void addAll(int position, Collection<T> data, DelegateParser<T> parser) {
        List<DelegateImpl> delegates = generateDelegateImpls(data, parser);
        if (delegates != null) {
            addAll(position, delegates);
        }
    }

    public <T> void addAllAtFirst (DelegateFilter filter, Collection<T> data, DelegateParser<T> parser) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            addAll(index, data, parser);
        } else {
            addAll(0, data, parser);
        }
    }

    public <T> void addAllAtLast (DelegateFilter filter, Collection<T> data, DelegateParser<T> parser) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            addAll(index, data, parser);
        } else {
            addAll(data, parser);
        }
    }

    public List<? extends DelegateImpl> getList() {
        return mDelegateImplList;
    }

    public DelegateImpl get (int position) {
        return mDelegateImplList.get(position);
    }

    public <T> ArrayList<T> getDataSourceArrayList (SimpleFilter<T> filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        ArrayList<T> dataList = new ArrayList<>();
        for (DelegateImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                dataList.add(((DelegateImpl<T>)impl).getSource());
            }
        }
        return dataList;
    }

    public List<DelegateImpl> getSubList (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        List<DelegateImpl> delegates = new ArrayList<>();
        for (DelegateImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                delegates.add(impl);
            }
        }
        return delegates;
    }

    public <T> List<DelegateImpl> generateDelegateImpls (Collection<T> data, DelegateParser<T> parser) {
        if (parser == null) {
            throw new NullPointerException("parser shouldn't be null");
        }
        if (data == null || data.isEmpty()) {
            return null;
        }
        List<DelegateImpl> delegates = new ArrayList<>();
        for (T obj : data) {
            DelegateImpl delegate = parser.parse(obj);
            if (delegate == null) {
                continue;
            }
            delegates.add(delegate);
        }
        return delegates;
    }

    public int firstIndexOf (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        final int length = mDelegateImplList.size();
        for (int i = 0; i < length; i++) {
            if (filter.accept(mDelegateImplList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        final int length = mDelegateImplList.size();
        for (int i = length - 1; i >= 0; i--) {
            if (filter.accept(mDelegateImplList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public void addAtLast (DelegateFilter filter, DelegateImpl delegate) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            add(index, delegate);
        } else {
            add(delegate);
        }
    }

    public void addAllAtLast (DelegateFilter filter, Collection<DelegateImpl> delegate) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            addAll(index, delegate);
        } else {
            addAll(delegate);
        }
    }

    public void addAtFirst (DelegateFilter filter, DelegateImpl delegate) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            add(index, delegate);
        } else {
            add(0, delegate);
        }
    }

    public void addAllAtFirst (DelegateFilter filter, Collection<DelegateImpl> delegate) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            addAll(index, delegate);
        } else {
            addAll(0, delegate);
        }
    }

    public DelegateImpl getFirst (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        for (DelegateImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                return impl;
            }
        }
        return null;
    }

    public DelegateImpl getLast (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        final int length = mDelegateImplList.size();
        for (int i = length - 1; i >= 0; i--) {
            DelegateImpl impl = mDelegateImplList.get(i);
            if (filter.accept(impl)) {
                return impl;
            }
        }
        return null;
    }

    public int getCount (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        int count = 0;
        for (DelegateImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                count++;
            }
        }
        return count;
    }

    public void remove (int position) {
        mDelegateImplList.remove(position);
    }

    public int remove (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        Iterator<? extends DelegateImpl> iterator = mDelegateImplList.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            DelegateImpl impl = iterator.next();
            if (filter.accept(impl)) {
                iterator.remove();
                count++;
            }
        }
        return count;

    }

    public int actionWith (DelegateAction action) {
        if (action == null) {
            throw new NullPointerException("action shouldn't be null");
        }
        int count = 0;
        for (DelegateImpl impl : mDelegateImplList) {
            action.onAction(impl);
            count++;
        }
        return count;
    }

    public int actionWith (DelegateFilter filter, DelegateAction action) {
        if (filter == null || action == null) {
            throw new NullPointerException("filter or action is null");
        }
        int count = 0;
        for (DelegateImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                action.onAction(impl);
                count++;
            }
        }
        return count;
    }

    public int replaceWith(DelegateFilter filter, DelegateReplace replace) {
        if (filter == null || replace == null) {
            throw new NullPointerException("filter or action is null");
        }
        int count = 0;
        final int length = mDelegateImplList.size();
        for (int i = 0; i < length; i++) {
            DelegateImpl impl = mDelegateImplList.get(i);
            if (filter.accept(impl)) {
                mDelegateImplList.set(i, replace.replaceWith(impl));
                count++;
            }
        }
        return count;
    }

    public boolean contains (DelegateImpl delegate) {
        return mDelegateImplList.contains(delegate);
    }

    public boolean endWith (DelegateImpl delegate) {
        if (mDelegateImplList.isEmpty()) {
            return false;
        }
        return mDelegateImplList.get(getItemCount() - 1).equals(delegate);
    }

    public boolean startWith (DelegateImpl delegate) {
        if (mDelegateImplList.isEmpty()) {
            return false;
        }
        return mDelegateImplList.get(0).equals(delegate);
    }

}
