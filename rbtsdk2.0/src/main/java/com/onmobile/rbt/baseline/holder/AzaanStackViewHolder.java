package com.onmobile.rbt.baseline.holder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.musicplayback.models.MusicPlaybackStateModel;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.base.SimpleFragmentPagerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.AppLocaleHelper;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.fragment.FragmentPreBuyAudio;
import com.onmobile.rbt.baseline.fragment.FragmentTrendingStack;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.widget.EnhancedWrapContentViewPager;
import com.onmobile.rbt.baseline.widget.pageindicator.PageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import static android.view.View.OVER_SCROLL_NEVER;

/**
 * Created by Shahbaz Akhtar on 12/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class AzaanStackViewHolder extends StackViewHolder<List<RingBackToneDTO>> {

    private EnhancedWrapContentViewPager viewPager;
    private SimpleFragmentPagerAdapter<FragmentTrendingStack> sliderAdapter;
    private PageIndicator pagerIndicator;
    private FragmentManager fragmentManager;

    private ViewPagerListener pagerListener;
    private Timer timer;
    private TimerTask timerTask;

    private boolean startCancelToggle;
    private boolean isUIVisible = true;

    private BaselineMusicPlayer mBaselineMusicPlayer;
    private List<MusicPlaybackStateModel> mMusicPlaybackStateModelList;
    private int lastPlayedPosition = -1;

    private boolean mBottomSheetOpened;

    private int previousState;
    private boolean userScrollChange;
    private boolean mUserClickedPlay = false;
    private AppLocaleHelper mAppLocaleHelper;

    public AzaanStackViewHolder(Context context, View root, View loading, View content, FragmentManager fragmentManager) {
        super(context, root, loading, content);
        this.fragmentManager = fragmentManager;
        sliderAdapter = new SimpleFragmentPagerAdapter<>(this.fragmentManager);
        pagerListener = new ViewPagerListener();
        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void initViews() {
        mAppLocaleHelper=BaselineApplication.getApplication().getAppLocaleManager();
        if (contentLayout != null) {
            viewPager = contentLayout.findViewById(R.id.viewpager_azaan_stack_discover);
            pagerIndicator = contentLayout.findViewById(R.id.indicator_azaan_stack_discover);
            //viewPager.enableSmoothScroller();
        }
    }

    @Override
    protected void bindViews() {
        setupPlayer();
        register(getContext());
    }

    private void setupPlayer() {
        mMusicPlaybackStateModelList = new ArrayList<>();
        mBaselineMusicPlayer = BaselineMusicPlayer.getInstance();
    }

    @Override
    public void bindHolder(List<RingBackToneDTO> ringBackToneDTOS) {
        showContent();
        if (ringBackToneDTOS != null && ringBackToneDTOS.size() > 0) {

            viewPager.setOverScrollMode(OVER_SCROLL_NEVER);
            viewPager.addOnPageChangeListener(pagerListener);

            //Don't change it
            viewPager.setOffscreenPageLimit(ringBackToneDTOS.size());

            mMusicPlaybackStateModelList.addAll(MusicPlaybackStateModel.generateMusicStateListenerModelList(ringBackToneDTOS.size()));
            sliderAdapter.clearItems();
            for (RingBackToneDTO ringBackToneDTO : ringBackToneDTOS) {
                sliderAdapter.addFragment(FragmentTrendingStack.newInstance(ringBackToneDTO).addListener(sliderAdapter.getCount(), mMusicPlaybackStateModelList.get(sliderAdapter.getCount()), mItemClickListener));
            }
            viewPager.setAdapter(sliderAdapter);

            pagerIndicator.attachToPager(viewPager);

            /**
             * Change made by Appolla Sreedhar
             */

            if (sliderAdapter.getCount() > 0) {
                int currentItemCount;
                if ((mAppLocaleHelper.setAppLocalForDeviceLanguage(BaselineApplication.getApplication().getApplicationContext(),true,false)).getLanguage().contains("ar")){
                    currentItemCount=sliderAdapter.getCount();
                }else {
                    currentItemCount=viewPager.getCurrentItem();
                }
                viewPager.post(() -> {
                    pagerListener.onPageSelected(currentItemCount);
                    if (isUIVisible && !mUserClickedPlay)
                        startAutoSlider(true);
                });
            }

            //startAutoSlider(true);
        }
    }

    @Override
    public void unbind() {

    }

    private class ViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(final int position) {
            //startAutoSlider(false);
            //scheduleAutoScroll(sliderAdapter.getCount());
            //toggleMusic(sliderAdapter.getItem(position).getItem(), position);
            updatePlayerItem(false, false, 0);
            if (isUIVisible) {
                stopMusic(true);
                /*if (userScrollChange) {
                    startAutoSlider(true);
                }*/
                if (SharedPrefProvider.getInstance(getContext()).isAppMusicAutoPlayEnabled()
                        && AppConfigurationValues.isAutoPlayEnabled() && mUserClickedPlay) {
                    toggleMusic(sliderAdapter.getItem(viewPager.getCurrentItem()).getRingBackToneDTO(), viewPager.getCurrentItem()); //Auto play
                    cancelAutoSlider();
                }
            } else {
                cancelAutoSlider();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_DRAGGING)
                cancelAutoSlider();

            if (previousState == ViewPager.SCROLL_STATE_DRAGGING
                    && state == ViewPager.SCROLL_STATE_SETTLING) {
                userScrollChange = true;
                cancelAutoSlider();
            } else if (previousState == ViewPager.SCROLL_STATE_SETTLING
                    && state == ViewPager.SCROLL_STATE_IDLE) {
                userScrollChange = false;
                if (mBaselineMusicPlayer != null && !mBaselineMusicPlayer.isMediaPlaying())
                    startAutoSlider(true);
            }

            previousState = state;
        }
    }

    private OnItemClickListener<RingBackToneDTO> mItemClickListener = new OnItemClickListener<RingBackToneDTO>() {
        @SafeVarargs
        @Override
        public final void onItemClick(View view, RingBackToneDTO ringBackToneDTO, int position, Pair<View, String>... sharedElements) {
            if(view.getId() == R.id.ib_play_trending_stack_discover) {
                cancelAutoSlider();
                mUserClickedPlay = !mUserClickedPlay;
                toggleMusic(ringBackToneDTO, position);
            }else if(view.getId() == R.id.tv_set_trending_stack_discover) {
                WidgetUtils.getSetAzaanBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_AZAAN_CARD, ringBackToneDTO).setCallback(new OnBottomSheetChangeListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        mBottomSheetOpened = true;
                        stopMusic(false);
                    }

                    @Override
                    public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                        mBottomSheetOpened = false;
                        startCancelToggle = true;
                        handleBottomSheetResult(success, message);
                    }

                    @Override
                    public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                        mBottomSheetOpened = false;
                        startCancelToggle = true;
                        handleBottomSheetResult(success, message);
                    }
                }).showSheet(fragmentManager);
            }
        }
    };

    private void toggleMusic(RingBackToneDTO ringBackToneDTO, int position) {
        if (ringBackToneDTO == null || mBottomSheetOpened)
            return;
        if (mBaselineMusicPlayer == null)
            mBaselineMusicPlayer = BaselineMusicPlayer.getInstance();
        if (mBaselineMusicPlayer.isMediaPlaying()) {
            if (lastPlayedPosition == position) {
                stopMusic(true);
                return;
            }
            //stopMusic(false);
        }
        startCancelToggle = true;
        lastPlayedPosition = position; //viewPager.getCurrentItem();
        mBaselineMusicPlayer.setPreviewListener(mMusicPreviewListener);
        mBaselineMusicPlayer.setMusicUrl(ringBackToneDTO.getPreviewStreamUrl());

        //mBaselineMusicPlayer.startMusic(getContext());
        scheduleMusicSettlingTimeOut();
    }

    private void stopMusic(boolean startCancelToggle) {
        if (mBaselineMusicPlayer == null)
            mBaselineMusicPlayer = BaselineMusicPlayer.getInstance();
        if (!startCancelToggle)
            cancelAutoSlider();
        this.startCancelToggle = startCancelToggle;
        if (mBaselineMusicPlayer.isMediaPlaying()) {
            mBaselineMusicPlayer.stopMusic();
        } else {
            try {
                mBaselineMusicPlayer.stopMusic();
            } catch (Exception ignored) {

            }
        }
    }

    private BaselineMusicPlayer.MusicPreviewListener mMusicPreviewListener = new BaselineMusicPlayer.MusicPreviewListener() {
        @Override
        public void onPreviewPlaying(int progressStatus) {
            updatePlayerItem(true, false, progressStatus);
            if (!isUIVisible || mBottomSheetOpened)
                stopMusic(false);
            if (mBaselineMusicPlayer != null && mBaselineMusicPlayer.getMediaPlayedInSeconds() == AppConstant.RECOMMENDATION_QUEUE_MIN_DELAY_TO_ADD) {
                RingBackToneDTO ringBackToneDTO = sliderAdapter.getItem(lastPlayedPosition).getRingBackToneDTO();
                if (ringBackToneDTO != null)
                    BaselineApplication.getApplication().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
            }
        }

        @Override
        public void onPreviewBuffering() {
            updatePlayerItem(false, true, 0);
            cancelAutoSlider();
        }

        @Override
        public void onPreviewStopped() {
            updateOnPlayerStatusChanged();
            /*if (viewPager != null)
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewPager.getCurrentItem();
                        viewPager.setCurrentItem((position < sliderAdapter.getCount() - 1) ? ++position : 0, true);
                    }
                }, 1000);*/
        }

        @Override
        public void onPreviewCompleted() {
            updateOnPlayerStatusChanged();
            /*if (viewPager != null)
                viewPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewPager.getCurrentItem();
                        viewPager.setCurrentItem((position < sliderAdapter.getCount() - 1) ? ++position : 0, true);
                    }
                }, 1000);*/
        }

        @Override
        public void onPreviewError() {
            updateOnPlayerStatusChanged();
        }
    };

    private void updateOnPlayerStatusChanged() {
        updatePlayerItem(false, false, 0);
        if (startCancelToggle && isUIVisible && !mBottomSheetOpened)
            startAutoSlider(true);
        else
            cancelAutoSlider();
    }

    private void updatePlayerItem(boolean playing, boolean buffering, int progress) {
        if (lastPlayedPosition > -1 && lastPlayedPosition < sliderAdapter.getCount()) {
            MusicPlaybackStateModel musicTune = mMusicPlaybackStateModelList.get(lastPlayedPosition);
            musicTune.setPreviewPlaying(playing);
            musicTune.setPreviewBuffering(buffering);
            musicTune.setPreviewProgress(!buffering ? progress : 0);
            sliderAdapter.getItem(lastPlayedPosition).updatePlayer();
        }
    }

    private void startAutoSlider(boolean restart) {
        if (mBottomSheetOpened) {
            cancelAutoSlider();
            return;
        }
        if (sliderAdapter != null) {
            cancelAutoSlider();
            long period = sliderAdapter.getCount() * 1000;
            long SHOW_DELAY = 5 * 1000;
            if (period > 1000) {
                if (restart) {
                    timer = new Timer();
                    timerTask = new SliderTimer();
                }
                timer.scheduleAtFixedRate(timerTask, SHOW_DELAY, period);
            }
        }
    }

    private void cancelAutoSlider() {
        if (timer != null)
            timer.cancel();
        if (timerTask != null)
            timerTask.cancel();
    }

    /**
     * Change made by Appolla Sreedhar
     */

    public class SliderTimer extends TimerTask {
        @Override
        public void run() {
            if (mBaselineMusicPlayer != null && mBaselineMusicPlayer.isMediaPlaying()) {
                cancelAutoSlider();
                return;
            }
            if (getContext() != null)
                ((Activity) getContext()).runOnUiThread(() -> {
                    final int currentItemCount = viewPager.getCurrentItem();
                    new Handler().post(() -> {
                        if (viewPager != null)
                            if ((mAppLocaleHelper.setAppLocalForDeviceLanguage(BaselineApplication.getApplication().getApplicationContext(),true,false)).getLanguage().contains("ar")){
                                viewPager.setCurrentItem((0 < currentItemCount) ? currentItemCount-1 : sliderAdapter.getCount(), true);
                            }else {
                                viewPager.setCurrentItem((currentItemCount < sliderAdapter.getCount() - 1) ? currentItemCount + 1 : 0, true);
                            }
                    });
                });
        }
    }

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getContext(), () -> {
                if (getContext() instanceof HomeActivity) {
                    startAutoSlider(true);
                    return;
                }
                if (success && getContext() != null)
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                else {
                    if (SharedPrefProvider.getInstance(getContext()).isLoggedIn()) {
                        ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                    } else {
                        startAutoSlider(true);
                    }
                }
            });
            appRatingPopup.show();
        } else {
            if (getContext() instanceof HomeActivity) {
                startAutoSlider(true);
                return;
            }
            if (success && getContext() != null)
                ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
            else {
                if (SharedPrefProvider.getInstance(getContext()).isLoggedIn()) {
                    ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true);
                } else {
                    startAutoSlider(true);
                }
            }
        }
    }

    private BroadcastReceiver mVisibilityChangeBroadcastReceiver, mDestroyBroadcastReceiver;

    private void register(Context context) {
        if (context != null) {
            LocalBroadcastManager.getInstance(context).registerReceiver(getOnDestroyBroadcastReceiver(), new IntentFilter(AppConstant.KEY_BROADCAST_ACTION_ON_DESTROY));
            LocalBroadcastManager.getInstance(context).registerReceiver(getVisibilityChangeBroadcastReceiver(), new IntentFilter(AppConstant.KEY_BROADCAST_ACTION_VISIBILITY_CHANGE_AZAN_STACK));
        }
    }

    private void unregister(Context context) {
        if (context != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(getOnDestroyBroadcastReceiver());
            LocalBroadcastManager.getInstance(context).unregisterReceiver(getVisibilityChangeBroadcastReceiver());
        }
    }

    private BroadcastReceiver getVisibilityChangeBroadcastReceiver() {
        if (mVisibilityChangeBroadcastReceiver == null)
            mVisibilityChangeBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent != null) {
                        isUIVisible = intent.getBooleanExtra(AppConstant.KEY_BROADCAST_DATA_BOOL_VISIBILITY_CHANGE, false);
                        if (isUIVisible) {
                            startAutoSlider(true);
                        } else {
                            mUserClickedPlay = false;
                            cancelMusicSettlingTimeOut();
                            stopMusic(false);
                        }
                    }
                }
            };
        return mVisibilityChangeBroadcastReceiver;
    }

    private BroadcastReceiver getOnDestroyBroadcastReceiver() {
        if (mDestroyBroadcastReceiver == null)
            mDestroyBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    unregister(context);
                }
            };
        return mDestroyBroadcastReceiver;
    }

    private Handler mHandlerScheduleMusic;
    private Runnable mRunnableScheduleMusic;

    private void scheduleMusicSettlingTimeOut() {
        cancelMusicSettlingTimeOut();
        mRunnableScheduleMusic = () -> {
            if (mBaselineMusicPlayer != null && mUserClickedPlay && AppUtils.isInternetAvailable())
                mBaselineMusicPlayer.startMusic(getContext());
            else
                mUserClickedPlay = false;
        };
        mHandlerScheduleMusic = new Handler();
        mHandlerScheduleMusic.postDelayed(mRunnableScheduleMusic, FragmentPreBuyAudio.MUSIC_SETTLING_DELAY);
    }

    private void cancelMusicSettlingTimeOut() {
        if (mRunnableScheduleMusic != null) {
            if (mHandlerScheduleMusic != null) {
                mHandlerScheduleMusic.removeCallbacks(mRunnableScheduleMusic);
                mHandlerScheduleMusic = null;
            }
            mRunnableScheduleMusic = null;
        }
    }

}
