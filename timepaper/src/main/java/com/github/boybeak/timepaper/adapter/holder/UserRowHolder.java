package com.github.boybeak.timepaper.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.boybeak.adapter.AbsViewHolder;
import com.github.boybeak.adapter.DelegateAdapter;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.activity.ProfileActivity;
import com.github.boybeak.timepaper.adapter.delegate.UserRowDelegate;
import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.utils.Intents;
import com.nulldreams.notify.toast.ToastCenter;

/**
 * Created by gaoyunfei on 2017/9/6.
 */

public class UserRowHolder extends AbsViewHolder<UserRowDelegate> {

    private AppCompatImageView profileIv, emailIv, twitterIv, insIv;
    public AppCompatTextView nameTv, locationTv, bioTv;
    private View moreView, socialView;

    private DrawableTransitionOptions fadeInOptions;
    private RequestOptions mCircleCropOption;

    public UserRowHolder(View itemView) {
        super(itemView);

        profileIv = findViewById(R.id.user_row_profile);
        nameTv = findViewById(R.id.user_row_name);
        locationTv = findViewById(R.id.user_row_location);
        bioTv = findViewById(R.id.user_row_bio);

        moreView = findViewById(R.id.user_row_more);
        socialView = findViewById(R.id.user_row_social_layout);
        emailIv = findViewById(R.id.user_row_email);
        twitterIv = findViewById(R.id.user_row_twitter);
        insIv = findViewById(R.id.user_row_instagram);

        fadeInOptions = DrawableTransitionOptions.withCrossFade();
        mCircleCropOption = RequestOptions.circleCropTransform();
    }

    @Override
    public void onBindView(final Context context, final UserRowDelegate t, final int position,
                           final DelegateAdapter adapter) {
        final User user = t.getSource();

        nameTv.setText(user.getFullName());
        locationTv.setText(user.location);

        final boolean showDetail = t.bundle().getBoolean("showDetail", false);

        moreView.setVisibility(showDetail ? View.VISIBLE : View.GONE);

        final boolean hasLocation = !TextUtils.isEmpty(user.location);
        locationTv.setVisibility(hasLocation && showDetail ? View.VISIBLE : View.GONE);

        final boolean hasBio = !TextUtils.isEmpty(user.bio);
        bioTv.setVisibility(hasBio && showDetail ? View.VISIBLE : View.GONE);
        bioTv.setText(hasBio ? user.bio.trim() : "");

        boolean showSocialMedia = user.hasSocialMedia() && showDetail;
        socialView.setVisibility(showSocialMedia ? View.VISIBLE : View.GONE);
        if (user.hasEmail()) {
            emailIv.setVisibility(View.VISIBLE);
            emailIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intents.emailTo(context, user.email, "", "");
                    } catch (Exception e) {
                        ToastCenter.with(context).text(R.string.toast_no_app_response_action).showShort();
                    }
                }
            });
        } else {
            emailIv.setVisibility(View.GONE);
            emailIv.setOnClickListener(null);
        }
        if (user.hasTwitter()) {
            twitterIv.setVisibility(View.VISIBLE);
            twitterIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intents.openUrl(context, user.getTwitterUrl());
                    } catch (Exception e) {
                        ToastCenter.with(context).text(R.string.toast_no_app_response_action).showShort();
                    }
                }
            });
        } else {
            twitterIv.setVisibility(View.GONE);
            twitterIv.setOnClickListener(null);
        }
        if (user.hasInstagram()) {
            insIv.setVisibility(View.VISIBLE);
            insIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intents.openUrl(context, user.getInstagramUrl());
                    } catch (Exception e) {
                        ToastCenter.with(context).text(R.string.toast_no_app_response_action).showShort();
                    }
                }
            });
        } else {
            insIv.setVisibility(View.GONE);
            insIv.setOnClickListener(null);
        }

        Glide.with(context).load(user.profile_image.large)
                .transition(fadeInOptions)
                .apply(mCircleCropOption)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_account_circle)).into(profileIv);

        profileIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.actionViewEvent(UserRowDelegate.PROFILE_CLICK, view, UserRowHolder.this, user, position, adapter);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t.bundle().putBoolean("showDetail", !showDetail);
                adapter.notifyItemChanged(position);
            }
        });
    }
}
