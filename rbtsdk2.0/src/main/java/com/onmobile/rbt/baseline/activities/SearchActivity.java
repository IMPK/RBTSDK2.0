package com.onmobile.rbt.baseline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.fragment.FragmentSearchContent;
import com.onmobile.rbt.baseline.fragment.FragmentSearchTag;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.ToolbarSearchListener;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Shahbaz Akhtar on 05/11/18.
 *
 * @author Shahbaz Akhtar
 */
public class SearchActivity extends BaseActivity implements BaseFragment.InternalCallback<BaseFragment, String> {

    public static final int REQ_CODE_SPEECH_INPUT = 209;

    private FragmentSearchTag mFragmentSearchTag;
    private FragmentSearchContent mFragmentSearchContent;

    private Map<String, BaseFragment> mFragmentMapQueue;

    private String mQuery;

    private Handler mHandler;
    private Runnable mRunnable;

    @NonNull
    @Override
    protected String initTag() {
        return SearchActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void setupToolbar() {
        setToolbarBackground(R.drawable.bg_gradient_trending);
        anchorOnToolbarSearch(mToolbarSearchListener);
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {
        mFragmentSearchTag = new FragmentSearchTag();
        mFragmentSearchContent = new FragmentSearchContent();
        initializeFragments(mFragmentSearchTag, mFragmentSearchContent);
        showMagic(mFragmentSearchTag, null);
    }

    @Override
    public void onStop() {
        stopMusic();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Creating a queue and adding fragments into that which supposed to be show/hide
     *
     * @param fragments fragments to be added
     */
    private void initializeFragments(BaseFragment... fragments) {
        mFragmentMapQueue = new HashMap<>(fragments.length);
        for (BaseFragment fragment : fragments) {
            addFragment(fragment);
            mFragmentMapQueue.put(fragment.getFragmentTag(), fragment);
        }
    }

    /**
     * Replace the current fragment with new fragment.
     *
     * @param fragment replacement fragment
     */
    private void replaceFragment(BaseFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragment.getFragmentTag()).commitAllowingStateLoss();
    }

    /**
     * Add fragment in a queue.
     *
     * @param fragment fragment to be added
     */
    private void addFragment(BaseFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, fragment.getFragmentTag()).commitAllowingStateLoss();
    }

    /**
     * Hide a fragment.
     *
     * @param fragment fragment to be hidden
     */
    private void hideFragment(BaseFragment fragment) {
        if (fragment.isHidden())
            return;
        if (fragment instanceof FragmentSearchContent)
            stopMusic();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(fragment).commitAllowingStateLoss();
    }

    /**
     * Show a fragment.
     *
     * @param fragment fragment to be shown
     */
    private void showFragment(BaseFragment fragment, String message) {
        if (fragment instanceof FragmentSearchTag) {
            if (!TextUtils.isEmpty(message))
                ((FragmentSearchTag) fragment).addMessage(message);
            else
                ((FragmentSearchTag) fragment).removeMessage();
        }
        if (!fragment.isHidden())
            return;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.show(fragment).commitAllowingStateLoss();
    }

    /**
     * Show a fragment and hide others
     *
     * @param fragment fragment to be shown
     */
    private void showMagic(BaseFragment fragment, String data) {
        Set<String> keys = mFragmentMapQueue.keySet();
        for (String key : keys) {
            if (key.equals(fragment.getFragmentTag()))
                showFragment(fragment, data);
            else
                hideFragment(mFragmentMapQueue.get(key));
        }
    }

    /**
     * Listener for Toolbar search
     */
    private ToolbarSearchListener mToolbarSearchListener = new ToolbarSearchListener() {
        @Override
        public void onBackPressed() {
            ((BaseActivity) getActivityContext()).onBackPressed();
        }

        @Override
        public void beforeTextChanged(String text, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(String text, int start, int before, int count) {
            mQuery = text;
            scheduleTypingTimeOut();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }

        @Override
        public void onSubmitQuery(String text) {
            queryChanged(text);
        }

        @Override
        public void onTextClear() {
            showMagic(mFragmentSearchTag, null);
        }

        @Override
        public void onVoiceClick() {
            openSpeechInput(REQ_CODE_SPEECH_INPUT, text -> submitVoiceSearch(text));
        }
    };

    /**
     * Change the fragment if query changed compared to last query
     *
     * @param query Query string to search
     */
    private void queryChanged(String query) {
        if (!TextUtils.isEmpty(query) && query.trim().length() >= AppConstant.QUERY_SEARCH_MIN_CHAR) {
            showMagic(mFragmentSearchContent, null);
            mFragmentSearchContent.setQuery(query);
        } else {
            showMagic(mFragmentSearchTag, null);
        }
    }

    @Override
    public void refreshData(BaseFragment fragment) {

    }

    @Override
    public void onItemClick(BaseFragment fragment, String data, int position) {

    }

    @Override
    public void onItemClick(BaseFragment fragment, View view, String data, int position) {

    }

    @Override
    public void changeFragment(BaseFragment fragment, Class replacementClazz, String data) {
        if (fragment instanceof FragmentSearchContent && replacementClazz == FragmentSearchTag.class && !TextUtils.isEmpty(data))
            showMagic(mFragmentSearchTag, data);
    }

    private void scheduleTypingTimeOut() {
        if(mHandler != null){
            if(mRunnable != null){
                mHandler.removeCallbacks(mRunnable);
                mRunnable = null;
            }
            mHandler = null;
        }


//        if (mRunnable != null) {
//            if (mHandler != null) {
//                mHandler.removeCallbacks(mRunnable);
//                mHandler = null;
//            }
//            mRunnable = null;
//        }
        mRunnable = () -> {
            if (isFinishing() || isDestroyed())
                return;
            queryChanged(mQuery);
        };
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, AppConstant.QUERY_SEARCH_DELAY);
    }

    private void stopMusic() {
        if (getMusicPlayer().isMediaPlaying()) {
            getMusicPlayer().stopMusic();
        } else {
            try {
                getMusicPlayer().stopMusic();
            } catch (Exception ignored) {

            }
        }
    }
}

