package com.github.boybeak.adapter.annotation;

import android.os.Bundle;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.adapter.throwable.ConstructorException;
import com.github.boybeak.adapter.widget.OnItemClickListener;
import com.github.boybeak.adapter.widget.OnItemLongClickListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.github.boybeak.adapter.DelegateAdapter.getLayoutFromAnnotation;

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

public abstract class AnnotationDelegate<T, AVH extends AbsViewHolder> extends AbsDelegate<T, AVH> {

    private static final String TAG = AnnotationDelegate.class.getSimpleName();

    private int layoutID = 0;
    private Class<AVH> holderClass;

    public AnnotationDelegate(T t) {
        super(t);
    }

    public AnnotationDelegate(T t, Bundle bundle) {
        super(t, bundle);
    }

    public AnnotationDelegate(T t, OnViewEventListener<T, AVH> onViewEventListener) {
        super(t, onViewEventListener);
    }

    public AnnotationDelegate(T t, Bundle bundle, OnViewEventListener<T, AVH> onViewEventListener) {
        super(t, bundle, onViewEventListener);
    }

    /**
     * find layout res id in {@link DelegateInfo}, if not found, then with {@link LayoutID}
     * @return
     */
    @Override
    public int getLayout() {
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
    public Class<AVH> getHolderClass () {
        if (holderClass != null) {
            return holderClass;
        }
        holderClass = DelegateAdapter.getHolderClassFromAnnotation(this);
        return holderClass;
    }

    /*@Override
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
    }*/

    /*@Override
    public int[] getOnClickIds() {
        if (clickIds == null) {
            clickIds = getClickIdsFromAnnotation(this);
        }
        return clickIds;
    }*/

    /*@Override
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
    }*/

    /*@Override
    public int[] getOnLongClickIds() {
        if (longClickIds == null) {
            longClickIds = getLongClickIdsFromAnnotation(this);
        }
        return longClickIds;
    }*/

    /*public static Class<? extends OnItemClickListener> getOnItemClickListenerFromAnnotation (LayoutImpl impl) {
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
    }*/
}
