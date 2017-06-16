package com.nulldreams.delegateadapter.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.nulldreams.delegateadapter.R;
import com.nulldreams.delegateadapter.adapter.delegate.YtbDelegate;
import com.nulldreams.delegateadapter.model.Ytb;

/**
 * Created by gaoyunfei on 2016/12/22.
 */

public class YtbHolder extends AbsViewHolder<YtbDelegate> {

    private ImageView profile, thumb;
    private TextView title, caption;

    public YtbHolder(View itemView) {
        super(itemView);
        profile = (ImageView)findViewById(R.id.profile);
        thumb = (ImageView)findViewById(R.id.thumb);
        title = (TextView)findViewById(R.id.title);
        caption = (TextView)findViewById(R.id.caption);

    }

    @Override
    public void onBindView(final Context context, YtbDelegate ytbDelegate, int position, DelegateAdapter adapter) {
        Ytb ytb = ytbDelegate.getSource();
        profile.setImageResource(ytb.getProfile());
        thumb.setImageResource(ytb.getThumb());
        title.setText(ytb.getTitle());
        caption.setText(ytb.getCaption());
        /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, YtbHolder.class.getSimpleName(), Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
