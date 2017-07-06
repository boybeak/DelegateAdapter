package com.nulldreams.delegateadapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.DelegateParser;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.adapter.touch.SimpleItemTouchHelperCallback;
import com.github.boybeak.adapter.touch.TouchableAdapter;
import com.nulldreams.delegateadapter.adapter.delegate.StatusDelegate;
import com.nulldreams.delegateadapter.model.Status;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRv;

    private TouchableAdapter mAdapter = null;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private SwipeRefreshLayout mSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRv = (RecyclerView)findViewById(R.id.main_rv);

        mAdapter = new TouchableAdapter(this);
        mRv.setAdapter(mAdapter);

        ItemTouchHelper helper = new ItemTouchHelper(new SimpleItemTouchHelperCallback(mAdapter,
                ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.END));
        helper.attachToRecyclerView(mRv);

        mSrl = (SwipeRefreshLayout)findViewById(R.id.activity_main);
        mSrl.setOnRefreshListener(this);

        mSrl.post(new Runnable() {
            @Override
            public void run() {
                mSrl.setRefreshing(true);
                onRefresh();
            }
        });
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
        } else if (item.getItemId() == R.id.add) {
            final EditText editTv = new EditText(this);
            editTv.setHint("add content here");
            new AlertDialog.Builder(this)
                    .setView(editTv)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mAdapter.notifyDataSetChanged();
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.add(0, new StatusDelegate(new Status(
                                    DataManager.getInstance().getMe(),
                                    editTv.getText().toString(),
                                    true
                                    )
                            )).autoNotify();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRefresh() {
        DataManager.getInstance().getStatus(new DataManager.OnStatusListener() {
            @Override
            public void onGet(List<Status> statuses) {
                mAdapter.addAll(statuses, new DelegateParser<Status>() {
                    @Override
                    public LayoutImpl parse(DelegateAdapter adapter, Status data) {
                        return new StatusDelegate(data);
                    }
                }).autoNotify();
                mSrl.setRefreshing(false);
            }
        });
    }
}
