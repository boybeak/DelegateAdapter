package com.nulldreams.delegateadapter.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.delegate.TwitterDelegate;
import com.nulldreams.delegateadapter.model.Twitter;

/**
 * Created by gaoyunfei on 2016/12/22.
 */

public class TwitterHolder extends AbsViewHolder<TwitterDelegate>{
    private ImageView profile;
    private TextView name, atName, text, forwardTv, commentTv, likeTv;
    public TwitterHolder(View itemView) {
        super(itemView);
        profile = (ImageView)findViewById(R.id.profile);
        name = (TextView)findViewById(R.id.name);
        atName = (TextView)findViewById(R.id.atName);
        text = (TextView)findViewById(R.id.text);
        forwardTv = (TextView)findViewById(R.id.forward_text);
        commentTv = (TextView)findViewById(R.id.comment_text);
        likeTv = (TextView)findViewById(R.id.like_text);
    }

    @Override
    public void onBindView(final Context context, TwitterDelegate twitterDelegate, int position, DelegateAdapter adapter) {
        Twitter twitter = twitterDelegate.getSource();
        profile.setImageResource(twitter.getProfile());
        name.setText(twitter.getName());
        atName.setText(twitter.getAtName());
        text.setText(twitter.getText());
        forwardTv.setText(twitter.getForwardCount() + "");
        commentTv.setText(twitter.getCommentCount() + "");
        likeTv.setText(twitter.getLikeCount() + "");
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, TwitterHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
