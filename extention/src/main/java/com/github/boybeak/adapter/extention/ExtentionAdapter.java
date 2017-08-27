package com.github.boybeak.adapter.extention;

import android.content.Context;
import android.os.Bundle;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.Action;

import java.util.List;

/**
 * Created by gaoyunfei on 2017/8/28.
 */

public class ExtentionAdapter extends DelegateAdapter {

    private Controller mController;

    public ExtentionAdapter(Context context) {
        super(context);
    }

    public ExtentionAdapter(Context context, Bundle bundle) {
        super(context, bundle);
    }

    public ExtentionAdapter(Context context, Bundle bundle, List<LayoutImpl> dataLayoutList) {
        super(context, bundle, dataLayoutList);
    }

    /**
     * go into under control single selection mode, you can choose items.
     * @return {@link SingleController}
     */
    public SingleController singleControl () {
        if (mController != null && mController instanceof SingleController) {
            return (SingleController)mController;
        }
        SingleController singleController = new SingleController(this);
        mController = singleController;
        notifyDataSetChanged();
        return singleController;
    }

    /**
     * go into under control multiple selection mode, you can choose items.
     * @return {@link MultipleController}
     */
    public MultipleController multipleControl () {
        if (mController != null && mController instanceof SingleController) {
            return (MultipleController)mController;
        }
        MultipleController multipleController = new MultipleController(this);
        mController = multipleController;
        notifyDataSetChanged();
        return multipleController;
    }

    /**
     * check if already under control
     * @return true if already under control.
     */
    public boolean isUnderControl () {
        return mController != null;
    }

    /*private  <C extends Controller> C takeControlWith (C c) {
        if (isUnderControl() && c.getClass().isInstance(mController)) {
            return (C)mController;
        }
        mController = c;
        notifyDataSetChanged();
        return c;
    }*/

    /**
     * release the control mode
     */
    public void dismissControl () {
        mController = null;
        selector(Checkable.class).map(new Action<Checkable>() {
            @Override
            public void action(int i, Checkable checkable) {
                checkable.setChecked(false);
            }
        });
        notifyDataSetChanged();
    }
}
