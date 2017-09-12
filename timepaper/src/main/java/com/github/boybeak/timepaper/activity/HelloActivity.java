package com.github.boybeak.timepaper.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.boybeak.timepaper.model.User;
import com.github.boybeak.timepaper.retrofit.Auth;
import com.github.boybeak.timepaper.R;
import com.github.boybeak.timepaper.model.TokenInfo;
import com.github.boybeak.timepaper.utils.MetaData;
import com.github.boybeak.timepaper.utils.SizeUtils;

public class HelloActivity extends BaseActivity {

    private static final String TAG = HelloActivity.class.getSimpleName();

    private static final String AUTHORIZATION_CODE_FILTER = "https://unsplash.com/oauth/authorize/";

    private WebView mWv;

    private WebViewClient mClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mPb.setVisibility(View.VISIBLE);
//            https://unsplash.com/oauth/authorize/0dd89d28df18df3ddab1f8673f35d7b26a9b0eaaa4e339545c31a104e12c0354
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mPb.setVisibility(View.GONE);
            if (url.startsWith(AUTHORIZATION_CODE_FILTER)) {
                String code = url.substring(url.lastIndexOf("/") + 1);
                Auth.getAuthToken(HelloActivity.this, code, new Auth.AuthListener() {
                    @Override
                    public void onSuccess(TokenInfo tokenInfo) {
                        jumpToMain();
                    }

                    @Override
                    public void onFailed(String message) {

                    }

                    @Override
                    public void onUserInfo(User user) {
                    }

                    @Override
                    public void onUserNotExist() {
                    }
                });
            }

        }
    };

    WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            mPb.setProgress(newProgress);
        }
    };

    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);

        mPb = (ProgressBar)findViewById(R.id.hello_progress);

        final View decorView = getWindow().getDecorView();

        decorView.postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 1000);

        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                decorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                View rootView = decorView.getRootView();
                SizeUtils.saveScreenSize(HelloActivity.this, rootView.getWidth(), rootView.getHeight());


                if (Auth.isTokenExist(HelloActivity.this)) {
                    jumpToMain();
                } else {
                    mWv = (WebView) findViewById(R.id.hello_wv);
                    mWv.setWebViewClient(mClient);

                    mWv.setWebChromeClient(mChromeClient);

                    String url = "https://unsplash.com/oauth/authorize?client_id=" + MetaData.getClientId(HelloActivity.this)
                            + "&redirect_uri=" + MetaData.getRedirectUri(HelloActivity.this)
                            + "&response_type=code&scope=" + getString(R.string.scopes);
                    mWv.loadUrl(url);
                }
            }
        });

    }

    private void jumpToMain () {
        Intent it = new Intent(HelloActivity.this, MainActivity.class);
        startActivity(it);
        finish();
    }
}
