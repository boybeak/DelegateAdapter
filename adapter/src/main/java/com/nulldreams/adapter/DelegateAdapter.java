package com.nulldreams.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nulldreams.adapter.annotation.AnnotationDelegate;
import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.adapter.annotation.HolderClass;
import com.nulldreams.adapter.annotation.LayoutID;
import com.nulldreams.adapter.annotation.NullHolder;
import com.nulldreams.adapter.impl.DelegateImpl;
import com.nulldreams.adapter.impl.LayoutImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public class DelegateAdapter extends RecyclerView.Adapter<AbsViewHolder>{

    public static final int ITEM_VIEW_ID = Integer.MIN_VALUE;

    private Context mContext = null;
    private List<LayoutImpl> mDelegateImplList = null;  //DataSource
    private SparseArrayCompat<Class<? extends AbsViewHolder>> mTypeHolderMap = null; // key -- layout, value -- holderClass

    public DelegateAdapter (Context context) {
        mContext = context;
        mDelegateImplList = new ArrayList<>();
        mTypeHolderMap = new SparseArrayCompat<Class<? extends AbsViewHolder>>();
    }

    public Context getContext () {
        return mContext;
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
        if (mTypeHolderMap.indexOfKey(viewType) >= 0) {
            Class<? extends AbsViewHolder> clz = mTypeHolderMap.get(viewType);
            if (clz != null) {
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
            }
        }
        return onHolderClassNotFound(viewType, itemView);
    }

    public AbsViewHolder onHolderClassNotFound (int viewType, View itemView) {
        throw new IllegalStateException("no holder found for viewType(0x" + Integer.toHexString(viewType) + ") at getHolder in " + this.getClass().getName());
    }

    @Override
    public void onBindViewHolder(final AbsViewHolder holder, final int position) {
        final LayoutImpl layoutImpl = mDelegateImplList.get(position);
        holder.onBindView(mContext, layoutImpl, position, this);
        if (layoutImpl.getOnItemClickListener() != null) {
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layoutImpl.getOnItemClickListener()
                            .onClick(v, mContext, layoutImpl, holder, position, DelegateAdapter.this);
                }
            };
            int[] ids = layoutImpl.getOnClickIds();
            if (ids != null) {
                final int length = ids.length;
                for (int i = 0; i < length; i++) {
                    int id = ids[i];
                    View v = null;
                    if (id == ITEM_VIEW_ID) {
                        v = holder.itemView;
                    } else {
                        v = holder.itemView.findViewById(id);
                    }
                    if (v != null) {
                        v.setOnClickListener(clickListener);
                    }
                }
            } else {
                holder.itemView.setOnClickListener(clickListener);
            }

        }
        if (layoutImpl.getOnItemLongClickListener() != null) {
            View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return layoutImpl.getOnItemLongClickListener().onLongClick(
                            v, mContext, layoutImpl, holder, position, DelegateAdapter.this
                    );
                }
            };
            int[] ids = layoutImpl.getOnLongClickIds();
            if (ids != null) {
                final int length = ids.length;
                for (int i = 0; i < length; i++) {
                    int id = ids[i];
                    View v = null;
                    if (id == ITEM_VIEW_ID) {
                        v = holder.itemView;
                    } else {
                        v = holder.itemView.findViewById(id);
                    }
                    if (v != null) {
                        v.setOnLongClickListener(longClickListener);
                    }
                }
            } else {
                holder.itemView.setOnLongClickListener(longClickListener);
            }

        }
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
            type = getLayoutFromAnnotation(impl);
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
            holderClass = getHolderClassFromAnnotation(impl);
        }
        /*if (holderClass == null) {
            throw new IllegalStateException("holderClass not be defined by class(" + impl.getClass().getName()
                    + "), please define a holderClass by getHolderClass or HolderClass or LayoutInfo");
        }*/
        if (mTypeHolderMap.indexOfKey(type) < 0 && holderClass != null) {
            mTypeHolderMap.put(type, holderClass);
        }
        return type;
    }

    @Override
    public void onViewAttachedToWindow(AbsViewHolder holder) {
        holder.onViewAttachedToWindow(mContext);
    }

    @Override
    public void onViewDetachedFromWindow(AbsViewHolder holder) {
        holder.onViewDetachedFromWindow(mContext);
    }

    @Override
    public void onViewRecycled(AbsViewHolder holder) {
        holder.onViewRecycled(mContext);
    }

    @Override
    public boolean onFailedToRecycleView(AbsViewHolder holder) {
        return holder.onFailedToRecycleView(mContext);
    }

    public boolean isEmpty () {
        return mDelegateImplList.isEmpty();
    }

    public DataChange clear () {
        int count = getItemCount();
        mDelegateImplList.clear();
        return new DataChange(this, 0, count, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public DataChange add(DelegateImpl impl) {
        mDelegateImplList.add(impl);
        return new DataChange(this, 0, DataChange.TYPE_ITEM_INSERTED);
    }

    public <T> DataChange add (T t, DelegateListParser<T> parser) {
        return addAll(parser.parse(this, t));
    }

    public <T> DataChange add (int position, T t, DelegateListParser<T> parser) {
        return addAll(position, parser.parse(this, t));
    }

    public DataChange addIfNotExist (@NonNull DelegateImpl impl) {
        if (contains(impl)) {
            return DataChange.doNothingInstance();
        }
        return add(impl);
    }

    public DataChange addIfNotExist (int position, @NonNull DelegateImpl impl) {
        if (contains(impl)) {
            return DataChange.doNothingInstance();
        }
        return add(position, impl);
    }

    public DataChange add(int position, @NonNull LayoutImpl impl) {
        mDelegateImplList.add(position, impl);
        return new DataChange(this, position, DataChange.TYPE_ITEM_INSERTED);
    }

    public DataChange addAll(@NonNull Collection<? extends LayoutImpl> list) {
        mDelegateImplList.addAll(list);
        return new DataChange(this, 0, list.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public DataChange addAll(int position, @NonNull Collection<? extends LayoutImpl> list) {
        mDelegateImplList.addAll(position, list);
        return new DataChange(this, position, list.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public DataChange addAll(LayoutImpl[] array) {
        mDelegateImplList.addAll(Arrays.asList(array));
        return new DataChange(this, 0, array.length, DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public DataChange addAll(int position, LayoutImpl[] array) {
        mDelegateImplList.addAll(position, Arrays.asList(array));
        return new DataChange(this, position, array.length, DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public <T> DataChange addAll (T[] tArray, DelegateParser<T> parser) {
        Collection<T> collection = Arrays.asList(tArray);
        List<LayoutImpl> delegates = generateDelegateImpls(collection, parser);
        if (delegates != null) {
            return addAll(delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAll (int position, T[] tArray, DelegateParser<T> parser) {
        Collection<T> collection = Arrays.asList(tArray);
        List<LayoutImpl> delegates = generateDelegateImpls(collection, parser);
        if (delegates != null) {
            return addAll(position, delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAll(Collection<T> data, DelegateParser<T> parser) {
        List<LayoutImpl> delegates = generateDelegateImpls(data, parser);
        if (delegates != null) {
            return addAll(delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAll(int position, Collection<T> data, DelegateParser<T> parser) {
        List<LayoutImpl> delegates = generateDelegateImpls(data, parser);
        if (delegates != null) {
            return addAll(position, delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAllAtFirst (DelegateFilter filter, Collection<T> data, DelegateParser<T> parser) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            return addAll(index, data, parser);
        } else {
            return addAll(0, data, parser);
        }
    }

    public <T> DataChange addAllAtLast (DelegateFilter filter, Collection<T> data, DelegateParser<T> parser) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            return addAll(index, data, parser);
        } else {
            return addAll(data, parser);
        }
    }

    public <T> DataChange addAll (T[] tArray, DelegateListParser<T> parser) {
        Collection<T> collection = Arrays.asList(tArray);
        List<LayoutImpl> delegates = generateDelegateImpls(collection, parser);
        if (delegates != null) {
            return addAll(delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAll (int position, T[] tArray, DelegateListParser<T> parser) {
        Collection<T> collection = Arrays.asList(tArray);
        List<LayoutImpl> delegates = generateDelegateImpls(collection, parser);
        if (delegates != null) {
            return addAll(position, delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAll(Collection<T> data, DelegateListParser<T> parser) {
        List<LayoutImpl> delegates = generateDelegateImpls(data, parser);
        if (delegates != null) {
            return addAll(delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAll(int position, Collection<T> data, DelegateListParser<T> parser) {
        List<LayoutImpl> delegates = generateDelegateImpls(data, parser);
        if (delegates != null) {
            return addAll(position, delegates);
        }
        return DataChange.doNothingInstance();
    }

    public <T> DataChange addAllAtFirst (DelegateFilter filter, Collection<T> data, DelegateListParser<T> parser) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            return addAll(index, data, parser);
        } else {
            return addAll(0, data, parser);
        }
    }

    public <T> DataChange addAllAtLast (DelegateFilter filter, Collection<T> data, DelegateListParser<T> parser) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            return addAll(index, data, parser);
        } else {
            return addAll(data, parser);
        }
    }

    public List<? extends LayoutImpl> getList() {
        return mDelegateImplList;
    }

    public LayoutImpl get (int position) {
        return mDelegateImplList.get(position);
    }

    public <T> ArrayList<T> getDataSourceArrayList (@NonNull SimpleFilter<T> filter) {
        ArrayList<T> dataList = new ArrayList<>();
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(this, impl)) {
                dataList.add(((DelegateImpl<T>)impl).getSource());
            }
        }
        return dataList;
    }

    public List<LayoutImpl> getSubList (@NonNull DelegateFilter filter) {
        List<LayoutImpl> delegates = new ArrayList<>();
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(this, impl)) {
                delegates.add(impl);
            }
        }
        return delegates;
    }

    /**
     * @param indexFrom start index, include this index;
     * @param indexTo end index
     * @return return a sub collection of [indexFrom, indexTo)
     */
    public List<LayoutImpl> getSubList (int indexFrom, int indexTo) {
        return mDelegateImplList.subList(indexFrom, indexTo);
    }

    /**
     * @param indexFrom start index, include this index;
     * @param indexTo end index
     * @param filter a filter between indexFrom and indexTo
     * @return return a sub collection, the max collection is [indexFrom, indexTo)
     */
    public List<LayoutImpl> getSubList (int indexFrom, int indexTo, @NonNull DelegateFilter filter) {
        List<LayoutImpl> delegates = new ArrayList<>();
        for (int i = indexFrom; i < indexTo; i++) {
            LayoutImpl impl = mDelegateImplList.get(i);
            if (filter.accept(this, impl)) {
                delegates.add(impl);
            }
        }
        return delegates;
    }

    public <T> List<LayoutImpl> generateDelegateImpls (Collection<T> data, @NonNull DelegateParser<T> parser) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        List<LayoutImpl> delegates = new ArrayList<>();
        for (T obj : data) {
            LayoutImpl delegate = parser.parse(this, obj);
            if (delegate == null) {
                continue;
            }
            delegates.add(delegate);
        }
        return delegates;
    }

    public <T> List<LayoutImpl> generateDelegateImpls (Collection<T> data, @NonNull DelegateListParser<T> parser) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        List<LayoutImpl> delegates = new ArrayList<>();
        for (T obj : data) {
            List<LayoutImpl> ds = parser.parse(this, obj);
            if (ds == null) {
                continue;
            }
            delegates.addAll(ds);
        }
        
        return delegates;
    }

    public int indexOf (LayoutImpl impl) {
        if (impl == null) {
            return -1;
        }
        return mDelegateImplList.indexOf(impl);
    }

    public int firstIndexOf (DelegateFilter filter) {
        return firstIndexOf(-1, filter);
    }

    public int firstIndexOf (int index, @NonNull DelegateFilter filter) {
        final int length = mDelegateImplList.size();
        for (int i = index + 1; i < length; i++) {
            if (filter.accept(this, mDelegateImplList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf (DelegateFilter filter) {
        final int length = mDelegateImplList.size();
        return lastIndexOf(length, filter);
    }

    public int lastIndexOf (int index, @NonNull DelegateFilter filter) {
        for (int i = index - 1; i >= 0; i--) {
            if (filter.accept(this, mDelegateImplList.get(i))) {
                return i;
            }
        }
        return -1;
    }

    public DataChange addAtLast (DelegateFilter filter, DelegateImpl delegate) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            return add(index, delegate);
        } else {
            return add(delegate);
        }
    }

    public DataChange addAllAtLast (DelegateFilter filter, Collection<DelegateImpl> delegate) {
        final int index = lastIndexOf(filter);
        if (index >= 0) {
            return addAll(index, delegate);
        } else {
            return addAll(delegate);
        }
    }

    public DataChange addAtFirst (DelegateFilter filter, DelegateImpl delegate) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            return add(index, delegate);
        } else {
            return add(0, delegate);
        }
    }

    public DataChange addAllAtFirst (DelegateFilter filter, Collection<DelegateImpl> delegate) {
        final int index = firstIndexOf(filter);
        if (index >= 0) {
            return addAll(index, delegate);
        } else {
            return addAll(0, delegate);
        }
    }

    public LayoutImpl getFirst (@NonNull DelegateFilter filter) {
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(this, impl)) {
                return impl;
            }
        }
        return null;
    }

    public LayoutImpl getLast (@NonNull DelegateFilter filter) {
        final int length = mDelegateImplList.size();
        for (int i = length - 1; i >= 0; i--) {
            LayoutImpl impl = mDelegateImplList.get(i);
            if (filter.accept(this, impl)) {
                return impl;
            }
        }
        return null;
    }

    public int getCount (@NonNull DelegateFilter filter) {
        int count = 0;
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(this, impl)) {
                count++;
            }
        }
        return count;
    }

    public DataChange remove (int position) {
        mDelegateImplList.remove(position);
        return new DataChange(this, position, 0, DataChange.TYPE_ITEM_REMOVED);
    }

    public DataChange remove (int position, @NonNull DoAfter after) {
        DataChange change = remove(position);
        after.doAfter(this);
        return change;
    }

    public int remove (@NonNull DelegateFilter filter) {
        Iterator<? extends LayoutImpl> iterator = mDelegateImplList.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            LayoutImpl impl = iterator.next();
            if (filter.accept(this, impl)) {
                iterator.remove();
                count++;
            }
        }
        return count;
    }

    public int remove (DelegateFilter filter, @NonNull DoAfter after) {
        int count = remove(filter);
        after.doAfter(this);
        return count;
    }

    public boolean remove (LayoutImpl impl) {
        return mDelegateImplList.remove(impl);
    }

    public boolean remove (LayoutImpl impl, @NonNull DoAfter after) {
        boolean result = remove(impl);
        after.doAfter(this);
        return result;
    }

    public int actionWith (@NonNull DelegateAction action) {
        int count = 0;
        for (LayoutImpl impl : mDelegateImplList) {
            action.onAction(impl);
            count++;
        }
        return count;
    }

    public int actionWith (@NonNull DelegateFilter filter, @NonNull DelegateAction action) {
        int count = 0;
        for (LayoutImpl impl : mDelegateImplList) {
            if (filter.accept(this, impl)) {
                action.onAction(impl);
                count++;
            }
        }
        return count;
    }

    public int replaceWith(@NonNull DelegateFilter filter, @NonNull DelegateReplace replace) {
        int count = 0;
        final int length = mDelegateImplList.size();
        for (int i = 0; i < length; i++) {
            LayoutImpl impl = mDelegateImplList.get(i);
            if (filter.accept(this, impl)) {
                mDelegateImplList.set(i, replace.replaceWith(this, impl));
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

    public static int getLayoutFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();
        int layoutID = 0;
        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("layoutID");
                layoutID = (int)method.invoke(anno);
                if (layoutID != 0) {
                    return layoutID;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        layoutID = getLayoutFromAnnotation(clz, impl);
        return layoutID;
    }

    private static int getLayoutFromAnnotation (Class clz, LayoutImpl impl) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            LayoutID anno = field.getAnnotation(LayoutID.class);
            if (anno != null) {
                try {
                    return field.getInt(impl);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static Class<? extends AbsViewHolder> getHolderClassFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        Class<? extends AbsViewHolder> holderClass;
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("holderClass");
                holderClass = (Class<? extends AbsViewHolder>)method.invoke(anno);
                if (holderClass != null && !holderClass.equals(NullHolder.class)) {
                    return holderClass;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        holderClass = getHolderClassFromAnnotation(clz, impl);
        return holderClass;
    }

    private static Class<? extends AbsViewHolder> getHolderClassFromAnnotation (Class<? extends AnnotationDelegate> clz, LayoutImpl impl) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            HolderClass anno = field.getAnnotation(HolderClass.class);
            if (anno != null) {
                try {
                    return (Class<? extends AbsViewHolder>)field.get(impl);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
