package com.nulldreams.delegateadapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nulldreams.adapter.DelegateAdapter;
import com.nulldreams.adapter.DelegateParser;
import com.nulldreams.adapter.impl.DelegateImpl;
import com.nulldreams.delegateadapter.adapter.TextDelegate;
import com.nulldreams.delegateadapter.adapter.UserDelegate;
import com.nulldreams.delegateadapter.adapter.delegate.TwitterDelegate;
import com.nulldreams.delegateadapter.adapter.delegate.YtbDelegate;
import com.nulldreams.delegateadapter.model.Twitter;
import com.nulldreams.delegateadapter.model.Ytb;

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

    private void addData () {
        int f = 5;
        for (int i = 0; i < 100; i++) {
            int v = i % f;
            if (v == 0) {
                mAdapter.add(new TextDelegate(i + ""));
            } else if (v == 1) {
            } else if (v == 2) {
                mAdapter.add(new UserDelegate(users[i % users.length]));
            } else if (v == 3) {
                mAdapter.addAll(Data.getYtbList(this, v * 2), new DelegateParser<Ytb>() {
                    @Override
                    public DelegateImpl parse(Ytb data) {
                        return new YtbDelegate(data);
                    }
                });
            } else if (v == 4) {
                mAdapter.addAll(Data.getTwitterList(v), new DelegateParser<Twitter>() {
                    @Override
                    public DelegateImpl parse(Twitter data) {
                        return new TwitterDelegate(data);
                    }
                });
            }

        }
        mAdapter.notifyDataSetChanged();
    }
}
