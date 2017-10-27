package com.nulldreams.delegateadapter;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.DelegateParser;
import com.github.boybeak.adapter.extension.SuperAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.Operator;
import com.github.boybeak.selector.Path;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {

    private RecyclerView mRv;

    private SuperAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        mRv = (RecyclerView)findViewById(R.id.select_rv);
        mRv.setHasFixedSize(true);
        mAdapter = new SuperAdapter(this);
        mAdapter.setHasStableIds(true);

        mRv.setAdapter(mAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.text_dialog_permission_instruction)
                    .setPositiveButton(R.string.text_btn_forward, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(SelectActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNeutralButton(R.string.text_btn_see_detail, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        } else {
            loadImages();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                finish();
            }
        }
    }

    private void loadImages () {
        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.WIDTH, MediaStore.Images.Media.HEIGHT,
                MediaStore.Images.Media.DATA

        };
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        List<SimpleImage> images = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            int width = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.WIDTH));
            int height = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT));
            String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            images.add(new SimpleImage(id, name, width, height, data));
        }
        cursor.close();

        mAdapter.addAll(images, new DelegateParser<SimpleImage>() {
            @Override
            public LayoutImpl parse(DelegateAdapter adapter, SimpleImage data) {
                return new SimpleImageDelegate(data);
            }
        }).autoNotify();
        mAdapter.multipleControl().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.select_check) {
            List<SimpleImage> paths = mAdapter.selector(SimpleImageDelegate.class).where(
                    Path.with(SimpleImageDelegate.class, Boolean.class).methodWith("isChecked"),
                    Operator.OPERATOR_EQUAL, true)
                    .extractAll(
                            Path.with(SimpleImageDelegate.class, SimpleImage.class).methodWith("getSource")
                    );
            Intent it = new Intent();
            if (paths != null && !paths.isEmpty()) {
                ArrayList<SimpleImage> pathArrayList = new ArrayList<>();
                pathArrayList.addAll(paths);
                it.putParcelableArrayListExtra("paths", pathArrayList);
                setResult(RESULT_OK, it);
            } else {
                setResult(RESULT_CANCELED);
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        /*if (mAdapter.multipleControl().isStarted()) {
            mAdapter.multipleControl().stop();
            return;
        }*/
        super.onBackPressed();
    }
}
