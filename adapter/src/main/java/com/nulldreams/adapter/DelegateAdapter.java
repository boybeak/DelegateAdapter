package com.nulldreams.adapter;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.impl.DelegateImpl;
import com.nulldreams.adapter.impl.LayoutImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public abstract class DelegateAdapter extends RecyclerView.Adapter<AbsViewHolder>{

    private Context mContext = null;
    private List<LayoutImpl> mDelegateImplList = null;
    private SparseArrayCompat<Class<? extends AbsViewHolder>> mTypeHolderMap = null;

    public DelegateAdapter (Context context) {
        mContext = context;
        mDelegateImplList = new ArrayList<>();
        mTypeHolderMap = new SparseArrayCompat<Class<? extends AbsViewHolder>>();
        //mTypeHolderMap = new HashMap<Integer, Class<? extends AbsViewHolder>>();
    }

    @Override
    public final AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(viewType, null);
        return getHolder(viewType, itemView);
    }

    /**
     * make an {@link AbsViewHolder} instance for {@param viewType} with {@param itemView}
     * @param viewType
     * @param itemView
     * @return
     */
    private AbsViewHolder getHolder (int viewType, View itemView) {
        Class<? extends AbsViewHolder> clz = mTypeHolderMap.get(viewType);
        try {
            Constructor<? extends AbsViewHolder> constructor = clz.getConstructor(View.class);
            return constructor.newInstance(itemView);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("no holder found for viewType(0x" + Integer.toHexString(viewType) + ") at getHolder in " + this.getClass().getName());
    }

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
        LayoutImpl impl = mDelegateImplList.get(position);
        int type = impl.getLayout();
        if (type == 0) {
            type = AnnotationDelegate.getLayoutFromAnnotation(impl);
        }
        if (type <= 0) {
            throw new IllegalStateException("layout not be defined by class(" + impl.getClass().getName()
                    + "), please define a layout resource id by getLayout or LayoutID or LayoutInfo");
        }
        /*if (type != 0) {
            if (mTypeHolderMap.indexOfKey(type) > 0 && mTypeHolderMap.get(type) != null) {

            }
        }*/
        Class<? extends AbsViewHolder> holderClass = impl.getHolderClass();
        if (holderClass == null) {
            holderClass = AnnotationDelegate.getHolderClassFromAnnotation(impl);
        }
        if (holderClass == null) {
            throw new IllegalStateException("holderClass not be defined by class(" + impl.getClass().getName()
                    + "), please define a holderClass by getHolderClass or HolderClass or LayoutInfo");
        }
        if (mTypeHolderMap.indexOfKey(type) < 0) {
            mTypeHolderMap.put(type, holderClass);
        }
        return type;
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

    public List<? extends LayoutImpl> getList() {
        return mDelegateImplList;
    }

    public LayoutImpl get (int position) {
        return mDelegateImplList.get(position);
    }

    public <T> ArrayList<T> getDataSourceArrayList (SimpleFilter<T> filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        ArrayList<T> dataList = new ArrayList<>();
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                dataList.add(((DelegateImpl<T>)impl).getSource());
            }
        }
        return dataList;
    }

    public List<LayoutImpl> getSubList (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        List<LayoutImpl> delegates = new ArrayList<>();
        for (LayoutImpl impl : mDelegateImplList) {
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

    public LayoutImpl getFirst (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(impl)) {
                return impl;
            }
        }
        return null;
    }

    public LayoutImpl getLast (DelegateFilter filter) {
        if (filter == null) {
            throw new NullPointerException("filter shouldn't be null");
        }
        final int length = mDelegateImplList.size();
        for (int i = length - 1; i >= 0; i--) {
            LayoutImpl impl = mDelegateImplList.get(i);
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
        for (LayoutImpl impl : mDelegateImplList) {
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
        Iterator<? extends LayoutImpl> iterator = mDelegateImplList.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            LayoutImpl impl = iterator.next();
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
        for (LayoutImpl impl : mDelegateImplList) {
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
        for (LayoutImpl impl : mDelegateImplList) {
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
            LayoutImpl impl = mDelegateImplList.get(i);
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
