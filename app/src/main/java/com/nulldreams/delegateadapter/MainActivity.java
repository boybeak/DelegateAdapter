package com.nulldreams.delegateadapter;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.boybeak.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.adapter.UserDelegate;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;

    private DelegateAdapter mAdapter = null;

    private User[] users = {
            new User(R.drawable.img1, "Jack", "Jack slow f**k"),
            new User(R.drawable.img2, "The Beatles", "Yesterday"),
            new User(R.drawable.img3, "Michael Jackson", "Earth Song"),
            new User(R.drawable.img4, "蒼井 そら", "复仇者之死")
    };

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRv = (RecyclerView)findViewById(R.id.main_rv);
        final Paint paint = new Paint(Color.LTGRAY);
        mRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                final int count = parent.getChildCount() - 1;
                for (int i = 0; i < count; i++) {
                    View child = parent.getChildAt(i);
                    c.drawLine(child.getLeft(), child.getBottom(), child.getRight(), child.getBottom(), paint);
                }
            }
        });
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new DelegateAdapter(this);
        mRv.setAdapter(mAdapter);

        addData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.github) {
            Intent it = new Intent(Intent.ACTION_VIEW);
            it.setData(Uri.parse("https://github.com/boybeak/DelegateAdapter"));
            startActivity(it);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addData () {
        for (int i = 0; i < 10; i++) {
            mAdapter.add(new UserDelegate(users[i % users.length]));
        }
//        mAdapter.add(new TextDelegate(0 + ""));
        mAdapter.notifyDataSetChanged();
    }
}
