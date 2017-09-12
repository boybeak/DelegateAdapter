package com.github.boybeak.timepaper.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StreamService extends Service {

    private static final String TAG = StreamService.class.getSimpleName();

    public class StreamBinder extends Binder {
        public StreamService getService () {
            return StreamService.this;
        }
    }

    private StreamBinder mBinder = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new StreamBinder();
        }
        return mBinder;
    }
//    @IntDef(value = {Service.START_FLAG_REDELIVERY, Service.START_FLAG_RETRY}, flag = true)
    @Override
    public int onStartCommand(Intent intent,
                               int flags,
                              int startId) {

        return START_STICKY;
    }

    public org.xutils.common.Callback.Cancelable download (String url, final String output,
                                                           org.xutils.common.Callback.CommonCallback<File> callback) {

        Log.v(TAG, "download url=" + url + " output=" + output);
        File outputFile = new File(output);
        if (!outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
        }
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true);
        params.setSaveFilePath(output);
        return x.http().get(params, callback);
    }

}
