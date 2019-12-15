package com.onmobile.rbt.baseline.activities.base;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.BuildConfig;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.DiscoverActivity;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.MusicLanguageActivity;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.activities.VideoActivity;
import com.onmobile.rbt.baseline.application.AppLocaleHelper;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.listener.SpeechRecognizerListener;
import com.onmobile.rbt.baseline.listener.ToolbarSearchListener;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.FontUtils;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.PermissionUtil;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG;
import static com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public abstract class BaseActivity extends AppCompatActivity {

    private String mTag;
    private Context mContext;
    private Toolbar mToolbar;
    private AppCompatTextView mToolbarTitle;
    private CoordinatorLayout mSnackBarLayout;

    private boolean mTransitionSupported;

    private BaselineMusicPlayer mBaselineMusicPlayer;
    public IHandlePermissionCallback permissionListener;

    private AppLocaleHelper mAppLocaleHelper;

    @NonNull
    protected abstract String initTag();

    @LayoutRes
    protected abstract int initLayout();

    protected abstract void unbindExtras(Intent intent);

    protected abstract void initViews();

    protected abstract void setupToolbar();

    protected abstract void bindViews();

    protected abstract void onPreOnCreate(@Nullable Bundle savedInstanceState);

    protected abstract void onPostOnCreate(@Nullable Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mAppLocaleHelper = BaselineApplication.getApplication().getAppLocaleManager();
//
//        List<String> languagesList= BuildConfig.SUPPORTED_LANGUAGES;
//        mAppLocaleHelper.changeLocale(getActivityContext(), languagesList.get(0));
//        this.getWindow().getDecorView().setTextDirection(View.TEXT_DIRECTION_LTR);


        onPreOnCreate(savedInstanceState);
        mTag = initTag();
        setContentView(initLayout());
        unbindInternalExtras(getIntent());
        unbindExtras(getIntent());
        initViews();
        initInternal();
        setupToolbar();
        bindViews();
        onPostOnCreate(savedInstanceState);
    }

    /**
     * To provide abstraction for data extraction from intent
     *
     * @param intent Intent to unbind
     */
    private void unbindInternalExtras(Intent intent) {
        if (intent != null) {
            mTransitionSupported = intent.getBooleanExtra(AppConstant.KEY_TRANSITION_SUPPORTED, false);
        }
    }

    private void initInternal() {
        initInternalToolbar();
    }

    private void initInternalToolbar() {
        try {
            mToolbar = findViewById(R.id.toolbar);
            if (mToolbar != null) {
                setSupportActionBar(mToolbar);
                if ((getSupportActionBar() != null))
                    getSupportActionBar().setTitle(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showShortToast(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(BaseActivity.this, message, Toast.LENGTH_LONG).show();
    }

    public void showShortSnackBar(String message) {
        findSnackBarLayout();
        if (mSnackBarLayout != null && !TextUtils.isEmpty(message)) {
            Snackbar.make(mSnackBarLayout,message,Snackbar.LENGTH_SHORT).show();
        } else if (mSnackBarLayout == null && !TextUtils.isEmpty(message)) {
            showShortToast(message);
        }
    }

    public void showShortSnackBar(String message, String actionLabel, View.OnClickListener actionListener) {
        findSnackBarLayout();
        if (mSnackBarLayout != null && !TextUtils.isEmpty(message) && !TextUtils.isEmpty(actionLabel) && actionListener != null) {
            Snackbar.make(mSnackBarLayout,message,Snackbar.LENGTH_LONG)
                    .setAction(actionLabel,actionListener)
                    .show();
        } else {
            showShortSnackBar(message);
        }
    }

    public void showLongSnackBar(String message) {
        findSnackBarLayout();
        if (mSnackBarLayout != null && !TextUtils.isEmpty(message)) {
            Snackbar.make(mSnackBarLayout,message,Snackbar.LENGTH_LONG).show();
        } else if (mSnackBarLayout == null && !TextUtils.isEmpty(message)) {
            showLongToast(message);
        }
    }

    public void showLongSnackBar(String message, String actionLabel, View.OnClickListener actionListener) {
        findSnackBarLayout();
        if (mSnackBarLayout != null && !TextUtils.isEmpty(message) && !TextUtils.isEmpty(actionLabel) && actionListener != null) {
            Snackbar.make(mSnackBarLayout, message, Snackbar.LENGTH_LONG)
                    .setAction(actionLabel, actionListener)
                    .show();
        } else {
            showLongSnackBar(message);
        }
    }

    private void findSnackBarLayout() {
        if (mSnackBarLayout == null) {
            try {
                mSnackBarLayout = findViewById(R.id.snackBar_layout);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CoordinatorLayout getSnackBarLayout() {
        findSnackBarLayout();
        return mSnackBarLayout;
    }

    public void redirect(@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask) {
        if (nextActivity == PreBuyActivity.class && AppConfigurationValues.isPrebuyVisualizerEnabled()) {
            permissionListener = new IHandlePermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    moveNext(nextActivity, bundle, finishCurrent, clearTask);
                }

                @Override
                public void onPermissionDenied() {
                    moveNext(nextActivity, bundle, finishCurrent, clearTask);
                }
            };
        } else {
            moveNext(nextActivity, bundle, finishCurrent, clearTask);
        }
    }

    public void redirectInFlow() {
        if (!SharedPrefProvider.getInstance(getActivityContext()).isLanguageSelected()) {
            Map<String, String> languageMap = BaselineApplication.getApplication().getRbtConnector().getLanguageToDisplay();
            if (languageMap.size() == 1) {
                List<String> mSelectedLanguageList = new ArrayList<>();
                for (Map.Entry<String, String> entry : languageMap.entrySet()) {
                    String languageCode = entry.getKey();
                    mSelectedLanguageList.add(languageCode);
                    SharedPrefProvider.getInstance(getActivityContext()).setUserLanguageCode(mSelectedLanguageList);
                    SharedPrefProvider.getInstance(getActivityContext()).setLanguageSelected(true);
                }

            }
        }

        if (!SharedPrefProvider.getInstance(this).isTourShown()
                && AppConfigurationValues.isShowVideoTutorial()) {
            redirect(VideoActivity.class, null, true, false);
        } else {

            if (SharedPrefProvider.getInstance(getActivityContext()).isLanguageSelected()) {
                if (SharedPrefProvider.getInstance(getActivityContext()).isLoggedIn()) {
                    redirect(HomeActivity.class, null, true, true);
                } else {
                    redirect(DiscoverActivity.class, null, true, true);
                }
            } else {
                redirect(MusicLanguageActivity.class, null, true, true);
            }
        }
    }

    private void moveNext(@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask) {
        Intent intent = new Intent(BaseActivity.this, nextActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        if (clearTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        if (finishCurrent) finish();
    }

    @SafeVarargs
    public final void redirectSceneTransitionAnimation(@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask, Pair<View, String>... sharedElements) {
        if (nextActivity == PreBuyActivity.class && AppConfigurationValues.isPrebuyVisualizerEnabled()) {
            permissionListener = new IHandlePermissionCallback() {
                @Override
                public void onPermissionGranted() {
                    moveNext(nextActivity, bundle, finishCurrent, clearTask, sharedElements);
                }

                @Override
                public void onPermissionDenied() {
                    moveNext(nextActivity, bundle, finishCurrent, clearTask, sharedElements);
                }
            };
            if (!SharedPrefProvider.getInstance(BaseActivity.this).isPermissionRecordExplained()) {
                SharedPrefProvider.getInstance(BaseActivity.this).setPermissionRecord(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.permission_record_info)
                        .setCancelable(false)
                        .setPositiveButton(R.string.permission_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                handlePermission(PermissionUtil.RequestCode.RECORD_AUDIO, permissionListener, PermissionUtil.Permission.RECORD_AUDIO);
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                handlePermission(PermissionUtil.RequestCode.RECORD_AUDIO, permissionListener, PermissionUtil.Permission.RECORD_AUDIO);
            }
        } else {
            moveNext(nextActivity, bundle, finishCurrent, clearTask, sharedElements);
        }
    }

    @SafeVarargs
    public final void moveNext(@NonNull Class<?> nextActivity, Bundle bundle, boolean finishCurrent, boolean clearTask, Pair<View, String>... sharedElements) {
        Intent intent = new Intent(BaseActivity.this, nextActivity);
        if (bundle != null)
            intent.putExtras(bundle);
        if (clearTask)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        //TODO Stop transition for now
        /*ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(BaseActivity.this, sharedElements);
        ActivityCompat.startActivity(BaseActivity.this, intent, options.toBundle());*/
        startActivity(intent);
        if (finishCurrent) finish();
    }

    protected Context getActivityContext() {
        if (mContext == null)
            mContext = this;
        return mContext;
    }

    public String getTag() {
        return mTag;
    }

    protected Toolbar getToolbar() {
        if (mToolbar == null)
            initInternalToolbar();
        return mToolbar;
    }

    protected void enableDefaultToolbarIndicator() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void enableToolbarIndicator(@DrawableRes int indicatorRes, @ColorRes int indicatorResColor) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            Drawable indicator = ContextCompat.getDrawable(BaseActivity.this, indicatorRes);
            if (indicator != null)
                indicator.setColorFilter(ContextCompat.getColor(BaseActivity.this, indicatorResColor), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(indicator);
        }
    }

    protected void disableToolbarIndicator() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    protected void setToolbarColor(@ColorRes int color, boolean includeStatusBar) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(BaseActivity.this, color)));
            if (includeStatusBar) setStatusBarColor(color);
        }
    }

    protected void setToolbarBackground(@DrawableRes int resource) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(ContextCompat.getDrawable(BaseActivity.this, resource));
        }
    }

    protected void setToolbarElevation(float elevation) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                actionBar.setElevation(elevation);
        }
    }

    protected AppCompatTextView getToolbarTitleTextView() {
        if (mToolbar == null)
            return null;
        if (mToolbarTitle == null)
            mToolbarTitle = getToolbar().findViewById(R.id.tv_toolbar_title);
        return mToolbarTitle;
    }

    public AppCompatEditText getToolbarSearchEditText() {
        if (mToolbar == null)
            return null;
        try {
            return getToolbar().findViewById(R.id.et_search);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void setToolbarTitleColor(@ColorRes int color) {
        AppCompatTextView toolbarTitle = getToolbarTitleTextView();
        if (toolbarTitle != null) {
            toolbarTitle.setTextColor(ContextCompat.getColor(BaseActivity.this, color));
        }
    }

    protected void setToolbarTitleSize(@IntRange(from = 11, to = 30) int size, @IntRange(from = 1, to = 2) int maxLine) {
        AppCompatTextView toolbarTitle = getToolbarTitleTextView();
        if (toolbarTitle != null) {
            toolbarTitle.setTextSize(size);
            toolbarTitle.setMaxLines(maxLine);
        }
    }

    protected void setToolbarTitle(String title) {
        AppCompatTextView toolbarTitle = getToolbarTitleTextView();
        if (toolbarTitle != null) {
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText(title);
        }
    }

    protected void setToolbarTitlePadding(int left, int top, int right, int bottom) {
        AppCompatTextView toolbarTitle = getToolbarTitleTextView();
        if (toolbarTitle != null) {
            /*LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(left, top, right, bottom);*/
            toolbarTitle.setPadding(left, top, right, bottom);
        }
    }

    protected void disableToolbarTitle() {
        AppCompatTextView toolbarTitle = getToolbarTitleTextView();
        if (toolbarTitle != null) {
            toolbarTitle.setVisibility(View.GONE);
        }
    }

    protected void enableToolbarScrolling() {
        ViewGroup.LayoutParams toolbarParams = getToolbar().getLayoutParams();
        if (toolbarParams != null) {
            AppBarLayout.LayoutParams appbarParams = (AppBarLayout.LayoutParams) toolbarParams;
            appbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        }
    }

    protected void disableToolbarScrolling() {
        ViewGroup.LayoutParams toolbarParams = getToolbar().getLayoutParams();
        if (toolbarParams != null) {
            AppBarLayout.LayoutParams appbarParams = (AppBarLayout.LayoutParams) toolbarParams;
            appbarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }
    }

    /**
     * An anchor to push all handling for search in a toolbar. Need to include @layout/toolbar_search in your activity.
     *
     * @param listener listener for search callback
     */
    protected void anchorOnToolbarSearch(@NonNull ToolbarSearchListener listener) {
        try {
            final AppCompatImageButton back = getToolbar().findViewById(R.id.ib_search_back);
            final AppCompatEditText searchEditText = getToolbarSearchEditText();
            final AppCompatImageButton voiceOrClear = getToolbar().findViewById(R.id.ib_search_voice_clear);

            int colorWhite = ContextCompat.getColor(getActivityContext(), R.color.white);
            int colorBlack = ContextCompat.getColor(getActivityContext(), R.color.black);

            searchEditText.setGravity(Gravity.CENTER_VERTICAL);
            searchEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            //WidgetUtils.setDrawable(getActivityContext(), searchEditText, R.drawable.ic_search_white_16dp, colorWhite,FunkyAnnotation.DRAWABLE_LEFT);
            searchEditText.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.activity_padding_half));
            FontUtils.setRegularFont(getActivityContext(), searchEditText);

            Drawable voiceDrawable = WidgetUtils.getDrawable(R.drawable.ic_keyboard_voice_white_24dp, getActivityContext());
            voiceDrawable.setColorFilter(colorWhite, PorterDuff.Mode.SRC_ATOP);

            Drawable clearDrawable = WidgetUtils.getDrawable(R.drawable.ic_clear_white_24dp, getActivityContext());
            clearDrawable.setColorFilter(colorBlack, PorterDuff.Mode.SRC_ATOP);

            voiceOrClear.setImageDrawable(voiceDrawable);

            back.setOnClickListener(view -> listener.onBackPressed());
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listener.beforeTextChanged(charSequence != null ? charSequence.toString() : null, i, i1, i2);
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    listener.onTextChanged(charSequence != null ? charSequence.toString() : null, i, i1, i2);
                    boolean nonEmpty = !TextUtils.isEmpty(charSequence) && charSequence.toString().length() > 0;
                    WidgetUtils.setDrawable(getActivityContext(), searchEditText, R.drawable.ic_search_white_16dp,
                            nonEmpty ? colorBlack : colorWhite, getWindow().getDecorView().getLayoutDirection()==View.LAYOUT_DIRECTION_LTR ? FunkyAnnotation.DRAWABLE_LEFT : FunkyAnnotation.DRAWABLE_RIGHT);
                    voiceOrClear.setImageDrawable(nonEmpty ? clearDrawable : voiceDrawable);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    listener.afterTextChanged(editable);
                }
            });
            searchEditText.setOnKeyListener((view, keyCode, keyEvent) -> {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                    case 7892:
                        listener.onSubmitQuery(searchEditText.getText().toString());
                        Util.hideKeyboard(getActivityContext(), searchEditText);
                        return true;
                }
                return false;
            });
            voiceOrClear.setOnClickListener(view -> {
                String typedText = searchEditText.getText().toString();
                if (!TextUtils.isEmpty(typedText) && typedText.length() > 0) {
                    searchEditText.setText(null);
                    listener.onTextClear();
                } else
                    listener.onVoiceClick();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Submit the message to search edit text view.
     *
     * @param voiceText String text voice message
     */
    protected void submitVoiceSearch(String voiceText) {
        if (TextUtils.isEmpty(voiceText))
            return;
        final AppCompatEditText searchEditText = getToolbarSearchEditText();
        if (searchEditText != null) {
            searchEditText.setText(voiceText);
            searchEditText.setSelection(searchEditText.getText().length());
        }
    }

    protected void setStatusBarColor(@ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
                // clear FLAG_TRANSLUCENT_STATUS flag:
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(Util.getOscuredColor(ContextCompat.getColor(BaseActivity.this, color)));
            }
        }
    }

    /**
     * Remove status bar color and add FLAG_TRANSLUCENT_STATUS for LOLLIPOP and above
     */
    protected void removeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
                // add FLAG_TRANSLUCENT_STATUS flag:
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // clear FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
        }
    }

    public boolean isTransitionSupported() {
        return mTransitionSupported;
    }

    public BaselineMusicPlayer getMusicPlayer() {
        if (mBaselineMusicPlayer == null)
            mBaselineMusicPlayer = BaselineMusicPlayer.getInstance();
        return mBaselineMusicPlayer;
    }

    public void handlePermission(int requestCode, IHandlePermissionCallback permissionListener, String... permissions) {
        if (PermissionUtil.hasPermission(this, permissions))
            permissionListener.onPermissionGranted();
        else if (PermissionUtil.shouldShowRequestPermissionRationale(this, permissions))
            PermissionUtil.requestPermission(this, requestCode, permissions);
        else
            PermissionUtil.requestPermission(this, requestCode, permissions);
        //permissionListener.onPermissionDenied();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionListener != null)
            if (PermissionUtil.isPermissionGranted(grantResults))
                permissionListener.onPermissionGranted();
            else
                permissionListener.onPermissionDenied();
    }

    public interface IHandlePermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaselineApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        BaselineApplication.activityPaused();
    }

    @Override
    public void onBackPressed() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            String redirectActivity = bundle.getString(AppConstant.KEY_CLASS_REDIRECT_ACTIVITY, null);
            if (!TextUtils.isEmpty(redirectActivity)) {
                Class next = null;
                if (HomeActivity.class.getName().equals(redirectActivity))
                    next = HomeActivity.class;
                else if (DiscoverActivity.class.getName().equals(redirectActivity))
                    next = DiscoverActivity.class;
                if (next != null) {
                    redirect(next, null, true, true);
                    return;
                }
            }
        }
        if (AppConfigurationValues.isShowExitPopup() && (this instanceof HomeActivity || this instanceof DiscoverActivity)) {
            AppDialog.showExitDialog(getActivityContext(), true, new DialogListener() {
                @Override
                public void PositiveButton(DialogInterface dialog, int id) {
                    System.exit(0);
                }

                @Override
                public void NegativeButton(DialogInterface dialog, int id) {

                }
            });
            return;
        }
        super.onBackPressed();
    }

    private int mSpeechRecognizerRequestCode;
    private SpeechRecognizerListener mSpeechRecognizerListener;

    public void openSpeechInput(int speechRecognizerRequestCode, SpeechRecognizerListener listener) {
        try {
            getMusicPlayer().stopMusic();
        } catch (Exception ignored) {

        }
        this.mSpeechRecognizerRequestCode = speechRecognizerRequestCode;
        this.mSpeechRecognizerListener = listener;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_recognizer_ready));
        try {
            startActivityForResult(intent, mSpeechRecognizerRequestCode);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mSpeechRecognizerRequestCode) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (mSpeechRecognizerListener != null && result != null && result.size() > 0) {
                    mSpeechRecognizerListener.onSpeechResult(result.get(0));
                }
            }
        }
    }

    /**
     * Returns true if the activity is alive
     *
     * @return boolean Alive/Dead
     */
    protected boolean isActivityAlive() {
        return !isDestroyed() && !isFinishing();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = BaselineApplication.getApplication().getAppLocaleManager().setLocale(newBase);
        super.attachBaseContext(context);
    }


}
