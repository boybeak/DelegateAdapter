package com.github.boybeak.timepaper.db;

import android.os.Environment;
import android.util.Log;

import com.github.boybeak.timepaper.model.Photo;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by gaoyunfei on 2017/9/14.
 */

public class PhotoManager {

    private static final String TAG = PhotoManager.class.getSimpleName();

    private static PhotoManager sManager = null;

    public static final PhotoManager getInstance () {
        if (sManager == null) {
            sManager = new PhotoManager();
        }
        return sManager;
    }
    private DbManager.DaoConfig mConfig;
    private DbManager mDb;

    private PhotoManager () {
        mConfig = new DbManager.DaoConfig();
        mDb = x.getDb(mConfig);
    }

    public void onStart (Photo photo) {
        Log.v (TAG, "onStart photo.id=" + photo.id);
        PhotoEntity entity = new PhotoEntity(photo);
        entity.state = PhotoEntity.STATE_STARTED;
        try {
            mDb.save(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public void onFinish (Photo photo) {
        Log.v (TAG, "onFinish photo.id=" + photo.id);
        PhotoEntity entity = new PhotoEntity(photo);
        entity.state = PhotoEntity.STATE_FINISHED;
        try {
            mDb.saveOrUpdate(entity);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public boolean isDownloadComplete (Photo photo) {
        try {
            PhotoEntity entity = mDb.selector(PhotoEntity.class).where("id", "=", photo.id).findFirst();
            List<PhotoEntity> entities = mDb.selector(PhotoEntity.class).findAll();
            if (entities != null) {
                for (PhotoEntity p : entities) {
                    Log.v(TAG, "isDownloadComplete p.id=" + p.id);
                }
            }
            Log.v(TAG, "isDownloadComplete entity=" + entity);
            if (entity == null) {
                return false;
            }
            if (entity.state == PhotoEntity.STATE_FINISHED) {
                return getFile(photo).exists();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return false;
    }

    public File getFile (Photo photo) {
        String name = photo.id + ".jpg";
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "TimePaper" + File.separator + name);
    }
}
