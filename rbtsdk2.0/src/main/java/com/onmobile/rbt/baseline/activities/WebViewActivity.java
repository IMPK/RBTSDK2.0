package com.onmobile.rbt.baseline.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.FAQDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.TnCDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.util.AppConstant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class WebViewActivity extends BaseActivity {

    private WebView mWebView;
    private String mHeading, mURL;
    private AppConstant.WebViewType mValue;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    @NonNull
    @Override
    protected String initTag() {
        mHeading = getIntent().getStringExtra(AppConstant.WebViewConstant.DRAWER_HEADING);
        mURL = getIntent().getStringExtra(AppConstant.WebViewConstant.WEBVIEW);
        mValue = (AppConstant.WebViewType) getIntent().getSerializableExtra(AppConstant.WebViewConstant.LOAD);
        return WebViewActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {

        mProgressBar = findViewById(R.id.progress_bar);
        mErrorTextView = findViewById(R.id.error_view);
        mWebView = findViewById(R.id.webView);

        mWebView.getSettings().setAppCacheEnabled(false);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                mProgressBar.setVisibility(View.GONE);
                mWebView.setVisibility(View.GONE);
                if (!isConnectionAvailable()) {
                    mErrorTextView.setText(R.string.error_handler_mobile_network);
                } else {
                    mErrorTextView.setText(R.string.err_webview);
                }
                mErrorTextView.setVisibility(View.VISIBLE);

            }
        });

        switch (mValue) {
            case FAQ:
                mProgressBar.setVisibility(View.VISIBLE);
                AppManager.getInstance().getRbtConnector().getFAQRequest(new AppBaselineCallback<FAQDTO>() {
                    @Override
                    public void success(FAQDTO result) {

                        mErrorTextView.setVisibility(View.GONE);
                        mURL = result.getFaq();
                        if (mURL == null || mURL.isEmpty()) {
                            mURL = "http://onmobile.com/";
                        }

                        mWebView.loadUrl(mURL);

                        mWebView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(String errMsg) {
                        mProgressBar.setVisibility(View.GONE);
                        mWebView.setVisibility(View.GONE);
                        if (!isConnectionAvailable()) {
                            mErrorTextView.setText(R.string.error_handler_mobile_network);
                        } else {
                            mErrorTextView.setText(R.string.err_webview);
                        }
                        mErrorTextView.setVisibility(View.VISIBLE);
                    }
                });
                break;
            case TNC:
                mProgressBar.setVisibility(View.VISIBLE);
                AppManager.getInstance().getRbtConnector().getTnCRequest(new AppBaselineCallback<TnCDTO>() {
                    @Override
                    public void success(TnCDTO result) {
                        mErrorTextView.setVisibility(View.GONE);
                        mURL = result.getTerms();
                        if (mURL == null || mURL.isEmpty()) {
                            mURL = "http://onmobile.com/";
                        }

                        mWebView.loadUrl(mURL);
                        mWebView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(String errMsg) {
                        mProgressBar.setVisibility(View.GONE);
                        mWebView.setVisibility(View.GONE);
                        if (!isConnectionAvailable()) {
                            mErrorTextView.setText(R.string.error_handler_mobile_network);
                        } else {
                            mErrorTextView.setText(R.string.err_webview);
                        }
                        mErrorTextView.setVisibility(View.VISIBLE);
                    }
                });
                break;

        }
    }


    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.white);
        setToolbarColor(R.color.colorAccent, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.white);
        setToolbarTitle(mHeading);

    }


    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean isConnectionAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}