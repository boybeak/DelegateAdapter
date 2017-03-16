package com.nulldreams.adapter.annotation;

import android.os.Bundle;
import android.util.Log;

import com.nulldreams.adapter.AbsDelegate;
import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.impl.LayoutImpl;
import com.nulldreams.adapter.throwable.ConstructorException;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.adapter.widget.OnItemLongClickListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.nulldreams.adapter.DelegateAdapter.getLayoutFromAnnotation;

/**
 *
 * In a subclass of this, you must provide {@link DelegateInfo} for fields with annotation
 * {@link LayoutID} and {@link HolderClass}.
 * In library project, you can not use {@link DelegateInfo}
 * For example, a subclass as below:
 * {@link DelegateInfo()}
 * public class DemoClass extends AnnotationDelegate<String> {
 *      \@{@link LayoutID}
 *      public int layoutId = R.layout.xxx;
 *      \@{@link HolderClass}
 *      public Class<XXXViewHolder> holderClass;
 * }
 * Created by gaoyunfei on 2016/12/17.
 */

public abstract class AnnotationDelegate<T> extends AbsDelegate<T> {

    private static final String TAG = AnnotationDelegate.class.getSimpleName();

    private int layoutID = 0;
    private Class<? extends AbsViewHolder> holderClass;

    private OnItemClickListener onItemClickListener;
    private boolean doNotGetOnClickListener = false;

    private OnItemLongClickListener onItemLongClickListener;
    private boolean doNotGetOnLongClickListener = false;

    private int[] clickIds, longClickIds;

    private Field[] fields = null;

    public AnnotationDelegate(T t) {
        super(t);
    }

    public AnnotationDelegate(T t, Bundle bundle) {
        super(t, bundle);
    }

    /*private void init () {
        getFieldsFromDelegateInfo();
    }*/

    /**
     * find layout res id in {@link DelegateInfo}, if not found, then with {@link LayoutID}
     * @return
     */
    @Override
    public final int getLayout() {
        if (layoutID > 0) {
            return layoutID;
        }

        layoutID = getLayoutFromAnnotation(this);
        return layoutID;
    }

    /**
     * find holder class in {@link DelegateInfo} first, if not found, then with {@link HolderClass}
     * @return
     */
    public final Class<? extends AbsViewHolder> getHolderClass () {
        if (holderClass != null) {
            return holderClass;
        }
        holderClass = DelegateAdapter.getHolderClassFromAnnotation(this);
        return holderClass;
    }

    @Override
    public OnItemClickListener<LayoutImpl, AbsViewHolder> getOnItemClickListener() {
        if (onItemClickListener == null && !doNotGetOnClickListener) {
            Class<? extends OnItemClickListener> clz = getOnItemClickListenerFromAnnotation(this);
            if (clz != null) {
                if (clz.equals(NullOnItemClickListener.class)) {
                    doNotGetOnClickListener = true;
                    return null;
                }
                try {
                    onItemClickListener = clz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    doNotGetOnClickListener = true;
                    throw new ConstructorException();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    doNotGetOnClickListener = true;
                    throw new ConstructorException();
                }
            } else {
                doNotGetOnClickListener = true;
            }
        }
        return onItemClickListener;
    }

    @Override
    public int[] getOnClickIds() {
        if (clickIds == null) {
            clickIds = getClickIdsFromAnnotation(this);
        }
        return clickIds;
    }

    @Override
    public OnItemLongClickListener<LayoutImpl, AbsViewHolder> getOnItemLongClickListener() {
        if (onItemLongClickListener == null && !doNotGetOnLongClickListener) {
            Class<? extends OnItemLongClickListener> clz = getOnItemLongClickListenerFromAnnotation(this);
            if (clz != null) {
                if (clz.equals(NullOnItemLongClickListener.class)) {
                    doNotGetOnLongClickListener = true;
                    return null;
                }
                try {
                    onItemLongClickListener = clz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    doNotGetOnLongClickListener = true;
                    throw new ConstructorException();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    doNotGetOnLongClickListener = true;
                    throw new ConstructorException();
                }
            } else {
                doNotGetOnLongClickListener = true;
            }
        }
        return onItemLongClickListener;
    }

    @Override
    public int[] getOnLongClickIds() {
        if (longClickIds == null) {
            longClickIds = getLongClickIdsFromAnnotation(this);
        }
        return longClickIds;
    }

/*    private void makeLayoutByDelegateInfo (Annotation delegateInfo, Class<? extends Annotation> annoClz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method layoutIdMethod = annoClz.getMethod("layoutID");
        if (layoutIdMethod != null) {
            layoutID = (int)layoutIdMethod.invoke(delegateInfo);
        }
//        return layoutID > 0;
    }

    private void makeHolderClassByDelegateInfo (Annotation delegateInfo, Class<? extends Annotation> annoClz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method holderClassMethod = annoClz.getMethod("holderClass");

        if (holderClassMethod != null) {
            holderClass = (Class<? extends AbsViewHolder>)holderClassMethod.invoke(delegateInfo);
        }
//        return holderClass != null;
    }

    private void makeOnItemClickListenerByDelegateInfo (Annotation delegateInfo, Class<? extends Annotation> annoClz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Method clickMethod = annoClz.getMethod("onClick");
        if (clickMethod != null) {
            Class<? extends OnItemClickListener> clickClass =
                    (Class<? extends OnItemClickListener>)clickMethod.invoke(delegateInfo);
            if (clickClass != null) {
                if (clickClass.equals(NullOnItemClickListener.class)) {
                    doNotGetOnClickListener = true;
                } else {
                    onItemClickListener = clickClass.newInstance();
                }
            }
//            return clickClass != null && !clickClass.equals(NullOnItemClickListener.class);
        }
    }

    private void makeOnItemLongClickListenerByDelegateInfo (Annotation delegateInfo, Class<? extends Annotation> annoClz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Method longClickMethod = annoClz.getMethod("onLongClick");
        if (longClickMethod != null) {
            Class<? extends OnItemLongClickListener> longClickClass =
                    (Class<? extends OnItemLongClickListener>)longClickMethod.invoke(delegateInfo);
            if (longClickClass != null) {
                if (longClickClass.equals(NullOnItemLongClickListener.class)) {
                    doNotGetOnLongClickListener = true;
                } else {
                    onItemLongClickListener = longClickClass.newInstance();
                }
            }
//            return longClickClass != null && !longClickClass.equals(NullOnItemLongClickListener.class);
        }
    }

    private void makeClickIds (Annotation delegateInfo, Class<? extends Annotation> annoClz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method clickIdsMethod = annoClz.getMethod("onClickIds");
        clickIds = (int[])clickIdsMethod.invoke(delegateInfo);
    }

    private void makeLongClickIds (Annotation delegateInfo, Class<? extends Annotation> annoClz)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method longClickIdsMethod = annoClz.getMethod("onLongClickIds");
        longClickIds = (int[])longClickIdsMethod.invoke(delegateInfo);
    }*/

    /*private void getFieldsFromDelegateInfo () {
        Annotation delegateInfo = getClass().getAnnotation(DelegateInfo.class);

        try {
            if (delegateInfo != null) {
                Class<? extends Annotation> annoClz = delegateInfo.annotationType();
                if (annoClz != null) {
                    makeLayoutByDelegateInfo(delegateInfo, annoClz);
                    makeHolderClassByDelegateInfo(delegateInfo, annoClz);
                    makeOnItemClickListenerByDelegateInfo(delegateInfo, annoClz);
                    makeOnItemLongClickListenerByDelegateInfo(delegateInfo, annoClz);
                    makeClickIds(delegateInfo, annoClz);
                    makeLongClickIds(delegateInfo, annoClz);
                }
            }

            fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                LayoutID layoutAnno = field.getAnnotation(LayoutID.class);
                if (layoutAnno != null) {
                    int layout = field.getInt(this);
                    if (layout > 0) {
                        layoutID = layout;
                    }
                }

                HolderClass holderAnno = field.getAnnotation(HolderClass.class);
                if (holderAnno != null) {
                    Class<? extends AbsViewHolder> holderClass
                            = (Class<? extends AbsViewHolder>)field.get(this);
                    if (holderClass != null) {
                        this.holderClass = holderClass;
                    }
                }

                OnClick onClickAnno = field.getAnnotation(OnClick.class);
                if (onClickAnno != null) {
                    Class<? extends OnItemClickListener> clickClass
                            = (Class<? extends OnItemClickListener>)field.get(this);
                    if (clickClass != null) {
                        if (clickClass.equals(NullOnItemClickListener.class)) {
                            doNotGetOnClickListener = true;
                        } else {
                            onItemClickListener = clickClass.newInstance();
                        }
                        Method clickIdsMethod = clickClass.getMethod("ids");
                        int[] clickIds = (int[])clickIdsMethod.invoke(onClickAnno);
                        if (clickIds == null || (clickIds.length == 1 && clickIds[0] == DelegateAdapter.ITEM_VIEW_ID)) {
                            this.clickIds = clickIds;
                        }
                    }
                }

                OnLongClick onLongClickAnno = field.getAnnotation(OnLongClick.class);
                if (onLongClickAnno != null) {
                    Class<? extends OnItemLongClickListener> longClickClass
                            = (Class<? extends OnItemLongClickListener>)field.get(this);
                    if (longClickClass != null) {
                        if (longClickClass.equals(NullOnItemLongClickListener.class)) {
                            doNotGetOnLongClickListener = true;
                        } else {
                            onItemLongClickListener = longClickClass.newInstance();
                        }
                        Method longClickIdsMethod = longClickClass.getMethod("ids");
                        int[] longClickIds = (int[])longClickIdsMethod.invoke(onLongClickAnno);
                        if (longClickIds == null || (longClickIds.length == 1 && longClickIds[0] == DelegateAdapter.ITEM_VIEW_ID)) {
                            this.longClickIds = longClickIds;
                        }
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }*/

    public static Class<? extends OnItemClickListener> getOnItemClickListenerFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        Class<? extends OnItemClickListener> listenerClass;
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("onClick");
                listenerClass = (Class<? extends OnItemClickListener>)method.invoke(anno);
                if (listenerClass != null && !listenerClass.equals(NullOnItemClickListener.class)) {
                    return listenerClass;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        listenerClass = getOnItemClickListenerFromAnnotation(clz, impl);
        return listenerClass;
    }

    private static Class<? extends OnItemClickListener> getOnItemClickListenerFromAnnotation (Class<? extends AnnotationDelegate> clz, LayoutImpl impl) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            OnClick anno = field.getAnnotation(OnClick.class);
            if (anno != null) {
                try {
                    return (Class<? extends OnItemClickListener>)field.get(impl);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Class<? extends OnItemLongClickListener> getOnItemLongClickListenerFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        Class<? extends OnItemLongClickListener> listenerClass;
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("onLongClick");
                listenerClass = (Class<? extends OnItemLongClickListener>)method.invoke(anno);
                if (listenerClass != null && !listenerClass.equals(NullOnItemLongClickListener.class)) {
                    return listenerClass;
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        listenerClass = getOnItemLongClickListenerFromAnnotation(clz, impl);
        return listenerClass;
    }

    private static Class<? extends OnItemLongClickListener> getOnItemLongClickListenerFromAnnotation (Class<? extends AnnotationDelegate> clz, LayoutImpl impl) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            OnLongClick anno = field.getAnnotation(OnLongClick.class);
            if (anno != null) {
                try {
                    return (Class<? extends OnItemLongClickListener>)field.get(impl);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static int[] getClickIdsFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        //Class<? extends OnItemLongClickListener> listenerClass;
        int[] ids = null;
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("onClickIds");
                ids = (int[])method.invoke(anno);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (ids == null || (ids.length == 1 && ids[0] == DelegateAdapter.ITEM_VIEW_ID)) {
            ids = getClickIdsFromAnnotation(clz, impl);
        }
        return ids;
    }

    private static int[] getClickIdsFromAnnotation (Class<? extends AnnotationDelegate> clz, LayoutImpl impl) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            OnClick anno = field.getAnnotation(OnClick.class);
            if (anno != null) {
                int[] clickIds = (int[])anno.ids();
                return clickIds;
            }
        }
        return new int[]{DelegateAdapter.ITEM_VIEW_ID};
    }

    public static int[] getLongClickIdsFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        //Class<? extends OnItemLongClickListener> listenerClass;
        int[] ids = null;
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("onLongClickIds");
                ids = (int[])method.invoke(anno);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (ids == null || (ids.length == 1 && ids[0] == DelegateAdapter.ITEM_VIEW_ID)) {
            ids = getLongClickIdsFromAnnotation(clz, impl);
        }
        return ids;
    }

    private static int[] getLongClickIdsFromAnnotation (Class<? extends AnnotationDelegate> clz, LayoutImpl impl) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            OnLongClick anno = field.getAnnotation(OnLongClick.class);
            if (anno != null) {
                int[] longClickIds = anno.ids();
                return longClickIds;
            }
        }
        return new int[]{DelegateAdapter.ITEM_VIEW_ID};
    }
}
