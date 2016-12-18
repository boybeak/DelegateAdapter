package com.nulldreams.delegateadapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nulldreams.adapter.adapter.AnnotationAdapter;
import com.nulldreams.delegateadapter.adapter.ColorDelegate;
import com.nulldreams.delegateadapter.adapter.TextDelegate;
import com.nulldreams.delegateadapter.adapter.UserDelegate;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRv;

    private AnnotationAdapter mAdapter = null;

    private int[] colors = {
            android.R.color.holo_blue_bright, android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark, android.R.color.holo_purple,
            android.R.color.holo_red_dark
    };

    private User[] users = {
            new User(R.drawable.img1, "Jack", "Jack slow f**k"),
            new User(R.drawable.img2, "The Beatles", "Yesterday"),
            new User(R.drawable.img3, "Michael Jackson", "Earth Song"),
            new User(R.drawable.img4, "蒼井 そら", "复仇者之死")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRv = (RecyclerView)findViewById(R.id.main_rv);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AnnotationAdapter(this);
        mRv.setAdapter(mAdapter);

        addData();
    }

    private void addData () {
        for (int i = 0; i < 100; i++) {
            if (i % 3 == 0) {
                mAdapter.add(new TextDelegate(i + ""));
            } else if (i % 3 == 1) {
                mAdapter.add(new ColorDelegate(getResources().getColor(colors[i % colors.length])));
            } else if (i % 3 == 2) {
                mAdapter.add(new UserDelegate(users[i % users.length]));
            }

        }
        mAdapter.notifyDataSetChanged();
    }
}
