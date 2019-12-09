package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.Logger;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CGWebViewActivity extends BaseActivity {

    private WebView mConsentWebView;
    private String mCGURL;
    private String mFinalRedirectionUrl;

    @NonNull
    @Override
    protected String initTag() {
        return CGWebViewActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.cg_webview_activity;
    }

    @Override
    protected void unbindExtras(Intent intent) {
        mCGURL = getIntent().getStringExtra(AppConstant.OnlineCGExtras.EXTRA_CONSCENT_URL);
        mFinalRedirectionUrl = getIntent().getStringExtra(AppConstant.OnlineCGExtras.EXTRA_R_URL);
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        if (mCGURL == null) {


        } else {

            mConsentWebView = findViewById(R.id.consent_web_view);
            WebSettings webSettings = mConsentWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            mConsentWebView.setWebViewClient(new myWebClient());
            mConsentWebView.loadUrl("javascript:window.location.reload( true )");
            mConsentWebView.loadUrl(mCGURL);

        }

    }

    private class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (AppConstant.ENABLE_LOGS) {
                Logger.d("WebViewClient", "onPageStarted...... " + url);
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            if (AppConstant.ENABLE_LOGS) {
                Logger.d("WebViewClient", "onReceivedError: 2");
            }
            Intent i = new Intent();
            i.putExtra(AppConstant.OnlineCGExtras.EXTRA_CG_ERROR, AppConstant.OnlineCGExtras.EXTRA_CG_ERROR);
            setResult(RESULT_OK, i);
            //handleWebViewPageLoadError(errorCode);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (AppConstant.ENABLE_LOGS) {
                Logger.d("WebViewClient", "shouldOverrideUrlLoading: " + url);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mWebContentLyt.setVisibility(View.VISIBLE);
                }
            });

            if ((mFinalRedirectionUrl != null) && ((url.startsWith(mFinalRedirectionUrl.trim())))) {
                if (AppConstant.ENABLE_LOGS) {
                    Logger.d("WebView---SAME-",
                            "SAME URL found and going to close browser with new task for message.");
                }

                Intent i = new Intent();
                i.putExtra(AppConstant.OnlineCGExtras.EXTRA_CG_RURL, url);
                setResult(RESULT_OK, i);
                finish();

                return true;
            } else {
                view.loadUrl(url);
                if (AppConstant.ENABLE_LOGS) {
                    Logger.d("WebViewClient", "shouldOverrideUrlLoading >>>> " + url);
                }
                return false;
            }
        }

    }
}
