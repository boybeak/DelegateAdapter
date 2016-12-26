package com.nulldreams.adapter.annotation;

import com.nulldreams.adapter.AbsDelegate;
import com.nulldreams.adapter.AbsViewHolder;
import com.nulldreams.adapter.impl.LayoutImpl;

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

    public static int getLayoutFromAnnotation (LayoutImpl impl) {
        Class clz = impl.getClass();
        int layoutID = 0;
        Annotation anno = clz.getAnnotation(DelegateInfo.class);
        if (anno != null) {
            Class<? extends Annotation> annoClz = anno.annotationType();
            try {
                Method method = annoClz.getMethod("layoutID");
                layoutID = (int)method.invoke(anno);
                return layoutID;
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
                return holderClass;
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
