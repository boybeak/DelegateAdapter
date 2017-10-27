package com.github.boybeak.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.boybeak.adapter.annotation.AnnotationDelegate;
import com.github.boybeak.adapter.annotation.DelegateInfo;
import com.github.boybeak.adapter.annotation.HolderClass;
import com.github.boybeak.adapter.annotation.LayoutID;
import com.github.boybeak.adapter.annotation.NullHolder;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.ListSelector;
import com.github.boybeak.selector.Selector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by gaoyunfei on 16/7/29.
 */
public class DelegateAdapter extends RecyclerView.Adapter<AbsViewHolder>{

    public static final int ITEM_VIEW_ID = Integer.MIN_VALUE;

    private Context mContext = null;
    private List<LayoutImpl> mDelegateImplList = null;  //DataSource
    private SparseArrayCompat<Class<? extends AbsViewHolder>> mTypeHolderMap = null; // key -- layout, value -- holderClass

    private Bundle mBundle;

    public DelegateAdapter (Context context) {
        this(context, null);
    }

    public DelegateAdapter (Context context, Bundle bundle) {
        this (context, bundle, new ArrayList<LayoutImpl>());
    }

    public DelegateAdapter (Context context, Bundle bundle, List<LayoutImpl> dataLayoutList) {
        mContext = context;
        mDelegateImplList = dataLayoutList;
        mTypeHolderMap = new SparseArrayCompat<Class<? extends AbsViewHolder>>();
        mBundle = bundle;
    }

    public Context getContext () {
        return mContext;
    }

    public Bundle bundle () {
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        return mBundle;
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
        holder.onViewAttachedToWindow(this, mContext);
    }

    @Override
    public void onViewDetachedFromWindow(AbsViewHolder holder) {
        holder.onViewDetachedFromWindow(this, mContext);
    }

    @Override
    public void onViewRecycled(AbsViewHolder holder) {
        holder.onViewRecycled(this, mContext);
    }

    @Override
    public boolean onFailedToRecycleView(AbsViewHolder holder) {
        return holder.onFailedToRecycleView(this, mContext);
    }

    /**
     * return a very powerful query tool instance of {@link Selector}
     * @param tClass the item's class contains in this DelegateAdapter.
     * @param <T>
     * @return A {@link Selector} instance for querying data
     */
    public <T> ListSelector<T> selector (Class<T> tClass) {
        return Selector.selector(tClass, mDelegateImplList);
    }

    public boolean isEmpty () {
        return mDelegateImplList.isEmpty();
    }

    public DataChange clear () {
        int count = getItemCount();
        mDelegateImplList.clear();
        return new DataChange(this, 0, count, DataChange.TYPE_ITEM_RANGE_REMOVED);
    }

    public DataChange add(LayoutImpl impl) {
        int sizeBefore = getItemCount();
        mDelegateImplList.add(impl);
        return new DataChange(this, sizeBefore, DataChange.TYPE_ITEM_INSERTED);
    }

    public <T> DataChange add (T t, DelegateListParser<T> parser) {
        return addAll(parser.parse(this, t));
    }

    public <T> DataChange add (int position, T t, DelegateListParser<T> parser) {
        return addAll(position, parser.parse(this, t));
    }

    public DataChange addIfNotExist (@NonNull LayoutImpl impl) {
        if (contains(impl)) {
            return DataChange.doNothingInstance();
        }
        return add(impl);
    }

    public DataChange addIfNotExist (int position, @NonNull LayoutImpl impl) {
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
        int sizeBefore = getItemCount();
        mDelegateImplList.addAll(list);
        return new DataChange(this, sizeBefore, list.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public DataChange addAll(int position, @NonNull Collection<? extends LayoutImpl> list) {
        mDelegateImplList.addAll(position, list);
        return new DataChange(this, position, list.size(), DataChange.TYPE_ITEM_RANGE_INSERTED);
    }

    public DataChange addAll(LayoutImpl[] array) {
        return addAll(Arrays.asList(array));
        /*mDelegateImplList.addAll();
        return new DataChange(this, 0, array.length, DataChange.TYPE_ITEM_RANGE_INSERTED);*/
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

    public List<? extends LayoutImpl> getList() {
        return mDelegateImplList;
    }

    public LayoutImpl get (int position) {
        return mDelegateImplList.get(position);
    }

    /**
     * @param indexFrom start index, include this index;
     * @param indexTo end index
     * @return return a sub collection of [indexFrom, indexTo)
     */
    public List<LayoutImpl> getSubList (int indexFrom, int indexTo) {
        return mDelegateImplList.subList(indexFrom, indexTo);
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

    public DataChange remove (int position) {
        mDelegateImplList.remove(position);
        return new DataChange(this, position, 0, DataChange.TYPE_ITEM_REMOVED);
    }

    public DataChange remove (int position, @NonNull DoAfter after) {
        DataChange change = remove(position);
        after.doAfter(this);
        return change;
    }

    public DataChange remove (LayoutImpl impl) {
        int index = mDelegateImplList.indexOf(impl);

        if (index >= 0) {
            mDelegateImplList.remove(impl);
            return new DataChange(this, index, DataChange.TYPE_ITEM_REMOVED);
        }
        return DataChange.doNothingInstance();
    }

    public DataChange remove (LayoutImpl impl, @NonNull DoAfter after) {
        DataChange change = remove(impl);
        after.doAfter(this);
        return change;
    }

    public int actionWith (@NonNull DelegateAction action) {
        int count = 0;
        for (LayoutImpl impl : mDelegateImplList) {
            action.onAction(impl);
            count++;
        }
        return count;
    }

    public boolean contains (LayoutImpl delegate) {
        return mDelegateImplList.contains(delegate);
    }

    public boolean endWith (LayoutImpl delegate) {
        if (mDelegateImplList.isEmpty()) {
            return false;
        }
        return mDelegateImplList.get(getItemCount() - 1).equals(delegate);
    }

    public boolean startWith (LayoutImpl delegate) {
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

    public static <AVH extends AbsViewHolder> Class<AVH> getHolderClassFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        Class<AVH> holderClass;
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("holderClass");
                holderClass = (Class<AVH>)method.invoke(anno);
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
