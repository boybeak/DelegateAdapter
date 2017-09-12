package com.github.boybeak.timepaper;

import android.app.Application;

import org.xutils.x;

/**
 * Created by gaoyunfei on 2017/9/8.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        x.Ext.init(this);
    }
}
