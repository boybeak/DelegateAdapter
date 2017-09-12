package com.nulldreams.delegateadapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.boybeak.adapter.AbsDelegate;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.adapter.DelegateParser;
import com.github.boybeak.adapter.extention.Checkable;
import com.github.boybeak.adapter.extention.MultipleController;
import com.github.boybeak.adapter.extention.SuperAdapter;
import com.github.boybeak.adapter.impl.LayoutImpl;
import com.github.boybeak.selector.ListSelector;
import com.github.boybeak.selector.Operator;
import com.github.boybeak.selector.Path;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private AbsDelegate.OnViewEventListener<SimpleImage, SimpleImageHolder> eventListener =
            new AbsDelegate.OnViewEventListener<SimpleImage, SimpleImageHolder>() {
        @Override
        public void onViewEvent(int eventCode, View view, SimpleImage simpleImage, SimpleImageHolder viewHolder, int position, DelegateAdapter adapter) {
            switch (eventCode) {
                case 0:
                    Toast.makeText(view.getContext(), simpleImage.getData(), Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    mAdapter.multipleControl().start();
                    invalidateOptionsMenu();
                    break;
            }
        }
    };

    private MultipleController.OnMultipleCheckedListener checkedListener = new MultipleController.OnMultipleCheckedListener() {

        private boolean lastAllChecked = false;

        @Override
        public void onControlStart() {
            invalidateOptionsMenu();
        }

        @Override
        public void onCheckChanged(Checkable checkable, boolean isAllChecked) {
            if (isAllChecked != lastAllChecked) {
                invalidateOptionsMenu();
            }
            lastAllChecked = isAllChecked;
        }

        @Override
        public void onAllChecked(List<Checkable> checkables) {
            invalidateOptionsMenu();
            lastAllChecked = true;
        }

        @Override
        public void onAllUnchecked(List<Checkable> checkables) {
            invalidateOptionsMenu();
            lastAllChecked = false;
        }

        @Override
        public void onControlStop() {
            invalidateOptionsMenu();
            lastAllChecked = false;
        }
    };

    private SuperAdapter<EmptyDelegate, FooterDelegate> mAdapter;

    private RecyclerView mRv;

    private EmptyDelegate mEmptyDelegate;
    private FooterDelegate mFooterDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdapter = new SuperAdapter<EmptyDelegate, FooterDelegate>(this);
        mEmptyDelegate = new EmptyDelegate("");
        mFooterDelegate = new FooterDelegate("", new AbsDelegate.OnViewEventListener<String, FooterHolder>() {
            @Override
            public void onViewEvent(int eventCode, View view, String s, FooterHolder viewHolder, int position, DelegateAdapter adapter) {
                Intent it = new Intent(MainActivity.this, SelectActivity.class);
                startActivityForResult(it, 100);
            }
        });
//        mAdapter.setEmptyItem(mEmptyDelegate);
        mAdapter.setTailItem(mFooterDelegate);
        mRv = (RecyclerView) findViewById(R.id.main_rv);
        mRv.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        if (mAdapter.multipleControl().isStarted()) {
            mAdapter.multipleControl().stop();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mAdapter.multipleControl().isStarted()) {
            getMenuInflater().inflate(R.menu.menu_delete, menu);
            menu.findItem(R.id.check_all).setIcon(mAdapter.multipleControl().isAllChecked() ?
                    R.drawable.ic_checkbox_blank_outline : R.drawable.ic_checkbox_multiple_marked);
        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.plus:
                Intent it = new Intent(this, SelectActivity.class);
                startActivityForResult(it, 100);
                break;
            case R.id.delete:
                new AlertDialog.Builder(this)
                        .setMessage(R.string.text_dialog_delete_instruction)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mAdapter.selector(Checkable.class).where(
                                        Path.with(Checkable.class, Boolean.class).methodWith("isChecked"),
                                        Operator.OPERATOR_EQUAL,
                                        true
                                ).remove(new ListSelector.OnRemoveCallback<Checkable>() {
                                    @Override
                                    public void onRemoved(int i, Checkable checkable) {

                                    }

                                    @Override
                                    public void onRemoveComplete(int i) {
                                        mAdapter.notifyDataSetChangedSafety();
                                        if (mAdapter.isEmptyExceptEmptyAndTail()) {
                                            mAdapter.multipleControl().stop();
                                            invalidateOptionsMenu();
                                        }
                                    }
                                });
                            }
                        })
                        .show();
                break;
            case R.id.check_all:
                if (mAdapter.multipleControl().isAllChecked()) {
                    mAdapter.multipleControl().uncheckAll();
                } else {
                    mAdapter.multipleControl().checkAll();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode == RESULT_OK) {
            List<SimpleImage> simpleImages = data.getParcelableArrayListExtra("paths");
            mAdapter.addAll(simpleImages, new DelegateParser<SimpleImage>() {
                @Override
                public LayoutImpl parse(DelegateAdapter adapter, SimpleImage data) {
                    return new SimpleImageDelegate(data, eventListener);
                }
            }).autoNotify();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}