package com.nulldreams.delegateadapter.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.delegate.StatusDelegate;
import com.nulldreams.delegateadapter.model.Status;
import com.nulldreams.delegateadapter.model.User;

/**
 * Created by gaoyunfei on 2017/6/29.
 */

public class StatusHolder extends AbsViewHolder<StatusDelegate> {

    private ImageView mProfileIv;
    private TextView mNameTv, mDescTv, mContentTv;
    private Button mDelBtn;

    public StatusHolder(View itemView) {
        super(itemView);

        mProfileIv = (ImageView)findViewById(R.id.avatar);
        mNameTv = (TextView)findViewById(R.id.name);
        mDescTv = (TextView)findViewById(R.id.desc);
        mContentTv = (TextView)findViewById(R.id.status_content);
        mDelBtn = (Button)findViewById(R.id.status_delete);
    }

    @Override
    public void onBindView(Context context, final StatusDelegate statusDelegate, final int position, final DelegateAdapter adapter) {
        Status status = statusDelegate.getSource();
        User user = status.author;

        mProfileIv.setImageResource(user.getAvatar());
        mNameTv.setText(user.getName());
        mDescTv.setText(user.getDescription());
        mContentTv.setText(status.content);
        mDelBtn.setVisibility(status.deletable ? View.VISIBLE : View.GONE);
        mDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(statusDelegate).autoNotify();
            }
        });
    }
}
