package com.nulldreams.adapter.annotation;

import com.nulldreams.adapter.AbsDelegate;
import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.impl.LayoutImpl;
import com.nulldreams.adapter.throwable.ConstructorException;
import com.nulldreams.adapter.widget.OnItemClickListener;
import com.nulldreams.adapter.widget.OnItemLongClickListener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

public class AnnotationDelegate<T> extends AbsDelegate<T> {

    private int layoutID = 0;
    private Class<? extends AbsViewHolder> holderClass;

    private OnItemClickListener onItemClickListener;
    private boolean doNotGetOnClickListener = false;

    private OnItemLongClickListener onItemLongClickListener;
    private boolean doNotGetOnLongClickListener = false;

    public AnnotationDelegate(T t) {
        super(t);
    }

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
        holderClass = getHolderClassFromAnnotation(this);
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

    private static int getLayoutFromAnnotation (Class<? extends LayoutImpl> clz, LayoutImpl impl) {
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
}
