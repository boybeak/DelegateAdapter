package com.nulldreams.adapter.adapter;

import com.nulldreams.adapter.annotation.DelegateInfo;
import com.nulldreams.adapter.annotation.HolderClass;
import com.nulldreams.adapter.annotation.LayoutID;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * An advanced subclass of AbsDelegate, work with {@link AnnotationAdapter}.<br/>
 * In a subclass, you must provide {@link DelegateInfo} for fields with annotation
 * {@link LayoutID} and {@link HolderClass}.<br/>
 * In library project, you can not use {@link DelegateInfo}<br/>
 * For example, a subclass as below:<br/>
 * @DelegateInfo(layoutID=R.layout.xxx, holderClass=XXXViewHolder.class)<br/>
 * public class DemoClass extends AnnotationDelegate<String> {<br/>
 *      @LayoutID<br/>
 *      public int layoutId = R.layout.xxx;<br/>
 *      @HolderClass<br/>
 *      public Class<XXXViewHolder> holderClass;<br/>
 * }<br/>
 * Created by gaoyunfei on 2016/12/17.<br/>
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
        Class clz = this.getClass();

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
        layoutID = getLayoutId(clz);
        return layoutID;
    }

    private int getLayoutId (Class<AnnotationDelegate<T>> clz) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            LayoutID anno = field.getAnnotation(LayoutID.class);
            if (anno != null) {
                try {
                    return field.getInt(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    /**
     * find holder class in {@link DelegateInfo} first, if not found, then with {@link HolderClass}
     * @return
     */
    public final Class<? extends AbsViewHolder> getHolderClass () {
        if (holderClass != null) {
            return holderClass;
        }
        Class clz = this.getClass();

        Annotation anno = clz.getAnnotation(DelegateInfo.class);

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
        holderClass = getHolderClassFromField(clz);
        return holderClass;
    }

    private Class<? extends AbsViewHolder> getHolderClassFromField (Class<? extends AnnotationDelegate> clz) {
        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            HolderClass anno = field.getAnnotation(HolderClass.class);
            if (anno != null) {
                try {
                    return (Class<? extends AbsViewHolder>)field.get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
