package com.onmobile.rbt.baseline.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.PreBuyActivity;
import com.onmobile.rbt.baseline.adapter.TransparentPagerAdapter;
import com.onmobile.rbt.baseline.adapter.base.SimpleFragmentPagerAdapter;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.AddRbt2UdpBSChildFragment;
import com.onmobile.rbt.baseline.bottomsheet.SetCallerTunePlansBSFragment;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.dialog.listeners.SelectionDialogListener;
import com.onmobile.rbt.baseline.exception.IllegalFragmentBindingException;
import com.onmobile.rbt.baseline.fragment.base.BaseFragment;
import com.onmobile.rbt.baseline.listener.ISetLayoutLoadedCoachMarks;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.model.ListItem;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.AudioPlayerVisualizer;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.Logger;
import com.onmobile.rbt.baseline.util.PermissionUtil;
import com.onmobile.rbt.baseline.util.TrackPagerTransformer;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.util.YouTubeHelper;
import com.onmobile.rbt.baseline.util.cut.ruler.CircularSeekBar;
import com.onmobile.rbt.baseline.util.cut.ruler.DjAudioPlayer;
import com.onmobile.rbt.baseline.util.cut.ruler.PreviewUrlGeneration;
import com.onmobile.rbt.baseline.util.cut.ruler.WheelRulerView;
import com.onmobile.rbt.baseline.widget.EnhancedWrapContentViewPager;
import com.onmobile.rbt.baseline.widget.chip.Chip;
import com.onmobile.rbt.baseline.widget.chip.OnChipClickListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.viewpager.widget.ViewPager;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Shahbaz Akhtar on 18/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class FragmentPreBuyAudio extends BaseFragment {

    public static final String IMAGE_TRANSITION_NAME = "transitionImage";
    private static final long DISPOSE_DELAY = 200;
    private static final int DEFAULT_ANIMATION_DURATION = 300;
    public static final long MUSIC_SETTLING_DELAY = 200;
    //private YouTubePlayer mYouTubePlayer;
    private EnhancedWrapContentViewPager mViewPagerTrack;
    private ViewPager mViewPagerTrackDummy;
    private AppCompatTextView mTvArtist, mTvTrack, mTvEdit, mTvSet, mTvShare, mTvVideoArtist, mTvVideoTrack, mTvSetVideo;
    private ViewGroup mLayoutPlayerControl, mLayoutParentAudio, mLayoutChildAudio, mLayoutParentVideo, mLayoutBlurImageAudioTrack, mLayoutBlurImageAudioTrackShadow; //, mLayoutContainerPlayerControl;
    private AppCompatImageView mIvBlur;
    private AppCompatImageButton mIbControlPrevious, mIbControlPlayPause, mIbControlNext, mIbBack, mIbShuffle;
    private ContentLoadingProgressBar mProgressPlayerContainer;
    private ContentLoadingProgressBar mProgressPlayer;
    private Chip mChipMusic, mChipVideo;
    private BlurView mBlurViewAudioTrack, mBlurViewAudioTrackShadow;

    private AppCompatImageView mIvTransitionImgBackGround;
    private SimpleFragmentPagerAdapter<FragmentPreBuyAudioCard> mTrackAdapter;
    private ListItem mListItem;
    private int mPosition, mCurrentPosition, mCurrentPositionDummy;

    private AudioPlayerVisualizer audioPlayerVisualizer;
    private InternalCallback<BaseFragment, RingBackToneDTO> mActivityCallback;

    private int mCurrentLayoutChipId;

    //cut starts
    private int TRACK_DURATION = 200000;
    private ImageView mCutBgBlurred;
    private BlurView mBlurView;
    private boolean mCutVisible = false;
    private CircularSeekBar mCircularSeekBar;
    private WheelRulerView mWheelRulerView;
    private ProgressBar mWheelProgressBar;
    private int mTypeToShow = AppConstant.TYPE_BOTH;
    private int mCurrentCutType = AppConstant.TYPE_RINGTONE;
    private TextView mCutTrackName;
    private TextView mCutArtistName;
    private ImageView mCutTrackImage;
    private ViewGroup mRootView;
    private View mDecorView;
    private View mCutView;
    private AppCompatImageView mCutCloseBtn;
    private ImageView mCutPrevButton;
    private ImageView mCutNextButton;
    private DjAudioPlayer mDjPlayer;
    private ImageView mCutPlayToggleImageView;
    private ContentLoadingProgressBar mCutProgressBar;
    private RingBackToneDTO mCutRingBackTone;
    private TextView mCutSetBtn;
    private ISetLayoutLoadedCoachMarks iDataLoadedCoachMarks;

    private ProgressDialog mProgressDialog;

    private TransitionDrawable mBackgroundCrossFader;

    private Handler mHandlerScheduleMusic, mHandlerAutoSlider;
    private Runnable mRunnableScheduleMusic, mRunnableAutoSlider;

    private boolean mUserClickedPlayToggle = true;
    @FunkyAnnotation.SlideDirection
    private int mSlideDirection = FunkyAnnotation.SLIDE_DIRECTION_FORWARD;

    private String mCallerSource;
    private boolean mPaginationSupported;

    private int LOAD_MORE_THREAD_SOLD = 2;
    private boolean mIsMusicLoading;
    private int mLoadMoreOffset;
    private boolean mLoadMoreOffsetLimitReached;
    private String mLoadMoreChartId;

    public static FragmentPreBuyAudio newInstance(String callerSource, @NonNull ListItem item, int position, boolean loadMoreSupported, String loadMoreChartId) {
        FragmentPreBuyAudio fragment = new FragmentPreBuyAudio();
        Bundle args = new Bundle();
        args.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, callerSource);
        args.putSerializable(AppConstant.KEY_DATA_LIST_ITEM, item);
        args.putInt(AppConstant.KEY_DATA_ITEM_POSITION, position);
        args.putBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, loadMoreSupported);
        args.putString(AppConstant.KEY_DATA_CHART_ID, loadMoreChartId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mActivityCallback = (InternalCallback<BaseFragment, RingBackToneDTO>) context;
        } catch (ClassCastException e) {
            throw new IllegalFragmentBindingException("Must implement AdapterInternalCallback");
        }
    }

    @NonNull
    @Override
    protected String initTag() {
        return FragmentPreBuyAudio.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_prebuy_audio;
    }

    @Override
    protected void unbindExtras(Bundle bundle) {
        if (bundle != null) {
            mCallerSource = bundle.getString(AppConstant.KEY_INTENT_CALLER_SOURCE, null);
            mListItem = (ListItem) bundle.getSerializable(AppConstant.KEY_DATA_LIST_ITEM);
            mCurrentPosition = mCurrentPositionDummy = mPosition = bundle.getInt(AppConstant.KEY_DATA_ITEM_POSITION);
            //mLoadMoreSupported = bundle.getBoolean(AppConstant.KEY_LOAD_MORE_SUPPORTED, false);
            mLoadMoreChartId = bundle.getString(AppConstant.KEY_DATA_CHART_ID, null);
        }
        mPaginationSupported = AppConfigurationValues.isPrebuyPaginationSupported() && !TextUtils.isEmpty(mLoadMoreChartId);
    }

    public void setLayoutLoadedCallback(ISetLayoutLoadedCoachMarks callback) {
        this.iDataLoadedCoachMarks = callback;
    }

    @Override
    protected void initComponents() {

    }

    @Override
    protected void initViews(View view) {
        mIvBlur = view.findViewById(R.id.iv_blur_aud_track);
        mLayoutParentAudio = view.findViewById(R.id.layout_parent_audio);
        mLayoutChildAudio = view.findViewById(R.id.layout_blur_aud_track);
        mLayoutParentVideo = view.findViewById(R.id.layout_parent_video);

        mLayoutBlurImageAudioTrack = view.findViewById(R.id.layout_blur_img_aud_track);
        mLayoutBlurImageAudioTrackShadow = view.findViewById(R.id.layout_aud_track_shadow);
        mBlurViewAudioTrack = view.findViewById(R.id.blur_view_aud_track);
        mBlurViewAudioTrackShadow = view.findViewById(R.id.blur_view_aud_track_shadow);

        mViewPagerTrack = view.findViewById(R.id.pager_aud_track);
        mViewPagerTrackDummy = view.findViewById(R.id.pager_aud_track_dummy);

        mTvArtist = view.findViewById(R.id.tv_artist_prebuy_aud_track);
        mTvTrack = view.findViewById(R.id.tv_track_prebuy_aud_track);

        mTvVideoArtist = view.findViewById(R.id.tv_artist_prebuy_vid_track);
        mTvVideoTrack = view.findViewById(R.id.tv_track_prebuy_vid_track);

        mTvEdit = view.findViewById(R.id.tv_edit_prebuy_aud_track);
        mTvEdit.setVisibility(View.GONE);
        mTvSet = view.findViewById(R.id.tv_set_prebuy_aud_track);
        mTvShare = view.findViewById(R.id.tv_share_prebuy_aud_track);

        //mLayoutContainerPlayerControl = view.findViewById(R.id.layout_container_control_aud_track);
        mProgressPlayerContainer = view.findViewById(R.id.progress_container_control_aud_track);
        mProgressPlayer = view.findViewById(R.id.progress_player_control_aud_track);
        mLayoutPlayerControl = view.findViewById(R.id.layout_control_aud_track);
        mIbControlPrevious = view.findViewById(R.id.ib_previous_control_aud_track);
        mIbControlPlayPause = view.findViewById(R.id.ib_play_control_aud_track);
        mIbControlNext = view.findViewById(R.id.ib_next_control_aud_track);
        mIbShuffle = view.findViewById(R.id.ib_shuffle_aud_track);
        if (AppConfigurationValues.isUDPEnabled()) {
            mIbShuffle.setVisibility(View.VISIBLE);
        } else {
            mIbShuffle.setVisibility(View.GONE);
        }

        mIbBack = view.findViewById(R.id.btn_iv_prebuy_back);
        mChipMusic = view.findViewById(R.id.chip_prebuy_music);
        mChipVideo = view.findViewById(R.id.chip_prebuy_video);

        mLayoutParentVideo.setVisibility(View.GONE);
        mTvSetVideo = view.findViewById(R.id.tv_set_prebuy_vid_track);

        if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
            mTvShare.setVisibility(View.VISIBLE);
        } else {
            mTvShare.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mTvSet.getLayoutParams();
        ViewTreeObserver vto = mTvSet.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(() -> {
            Rect rectf = new Rect();

            //For coordinates location relative to the parent
            mTvSet.getLocalVisibleRect(rectf);

            //For coordinates location relative to the screen/display
            mTvSet.getGlobalVisibleRect(rectf);

            /*Logger.d("WIDTH        :", String.valueOf(rectf.width()));
            Logger.d("HEIGHT       :", String.valueOf(rectf.height()));
            Logger.d("left         :", String.valueOf(rectf.left));
            Logger.d("right        :", String.valueOf(rectf.right));
            Logger.d("top          :", String.valueOf(rectf.top));
            Logger.d("bottom       :", String.valueOf(rectf.bottom));*/
            if (iDataLoadedCoachMarks != null) {
                iDataLoadedCoachMarks.layoutLoaded(rectf.left, rectf.top, rectf.right, rectf.bottom);
            }
        });
        mIbBack.setOnClickListener(mClickListener);
        mTvEdit.setOnClickListener(mClickListener);
        mTvSet.setOnClickListener(mClickListener);
        mTvShare.setOnClickListener(mClickListener);
        mIbShuffle.setOnClickListener(mClickListener);

        mIbControlPrevious.setOnClickListener(mClickListener);
        mIbControlPlayPause.setOnClickListener(mClickListener);
        mIbControlNext.setOnClickListener(mClickListener);

        mChipMusic.setOnChipClickListener(mChipClickListener);
        mChipVideo.setOnChipClickListener(mChipClickListener);

        mTvSetVideo.setOnClickListener(mClickListener);

//        WidgetUtils.enableMarquee(mTvArtist, mTvTrack);

        audioPlayerVisualizer = view.findViewById(R.id.visualizer_view);

        mDecorView = getRootActivity().getWindow().getDecorView();

        mIvTransitionImgBackGround = view.findViewById(R.id.iv_gradient_aud_track);

        //cut starts
        mCutView = ((PreBuyActivity) getRootActivity()).getCutView();
        if (mCutView != null) {
            mCutCloseBtn = mCutView.findViewById(R.id.cut_close_btn);
            mCutCloseBtn.setOnClickListener(mClickListener);
            mCircularSeekBar = mCutView.findViewById(R.id.circularSeekBar);
            mWheelRulerView = mCutView.findViewById(R.id.wheel_view);
            mWheelProgressBar = mCutView.findViewById(R.id.wheel_progress_bar);
            mCutBgBlurred = mCutView.findViewById(R.id.background_picture_blured);
            mCutTrackName = mCutView.findViewById(R.id.track_name2);
            mCutArtistName = mCutView.findViewById(R.id.artist_name2);
            mCutTrackImage = mCutView.findViewById(R.id.track_picture);
            mRootView = mCutView.findViewById(R.id.blur_view_root);
            mBlurView = mCutView.findViewById(R.id.blurView);
            mCutPrevButton = mCutView.findViewById(R.id.prev_button);
            mCutNextButton = mCutView.findViewById(R.id.next_button);
            mCutPlayToggleImageView = mCutView.findViewById(R.id.iv_play_cut);
            mCutProgressBar = mCutView.findViewById(R.id.progress_play_cut);
            mCutSetBtn = mCutView.findViewById(R.id.tv_set_cut);
            mCutSetBtn.setOnClickListener(mClickListener);
        }
        //cut ends

        setupBackgroundCrossFader();
        showAudioLayout();
        mBackgroundCrossFader.startTransition(DEFAULT_ANIMATION_DURATION);

    }

    private void setupBackgroundCrossFader() {
        Drawable backgrounds[] = new Drawable[2];
        //Resources res = getResources();
        ColorDrawable color1 = new ColorDrawable();
        color1.setColor(ContextCompat.getColor(getFragmentContext(), R.color.black));
        backgrounds[0] = color1;
        backgrounds[1] = WidgetUtils.getDrawable(R.drawable.bg_prebuy_bottom_gradient_white, getActivity());
        mBackgroundCrossFader = new TransitionDrawable(backgrounds);
        mBackgroundCrossFader.setCrossFadeEnabled(true);
        mIvTransitionImgBackGround.setImageDrawable(mBackgroundCrossFader);
    }

    @Override
    protected void bindViews(View view) {
        mDjPlayer = new DjAudioPlayer(getRootActivity());
        setupTrackBlur();
        setupTrackViewPager();
        showPlayerLoading();
        loadData();
        showPlayerLoaded();
        // initYouTubeView();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUserClickedPlayToggle = false;
        cancelMusicSettlingTimeOut();
        stopTrack();
        if (mDjPlayer != null)
            mDjPlayer.stop();
    }

    private void loadData() {
        for (RingBackToneDTO item :
                mListItem.getFilteredItems(APIRequestParameters.EMode.RINGBACK)) {
            mTrackAdapter.addFragment(FragmentPreBuyAudioCard.newInstance(item));
        }
        mTrackAdapter.notifyDataSetChanged();
        mViewPagerTrack.setOffscreenPageLimit(mTrackAdapter.getCount());
        setupDummyTrackViewPager(mTrackAdapter.getCount());

        mViewPagerTrack.postDelayed(() -> {
            mViewPagerTrack.setCurrentItem(mPosition, false);
            mPageChangeListener.onPageSelected(mPosition);
            if (getRootActivity().isTransitionSupported())
                mTrackAdapter.getItem(mPosition).setTransition(IMAGE_TRANSITION_NAME);
        }, DISPOSE_DELAY);
    }

    private void setupTrackViewPager() {
        mTrackAdapter = new SimpleFragmentPagerAdapter<>(getChildFragmentManager());
        mViewPagerTrack.setPageTransformer(false, new TrackPagerTransformer(getFragmentContext()));
        mViewPagerTrack.setPageMargin((int) -getResources().getDimension(R.dimen.track_aud_card_difference));
        mViewPagerTrack.addOnPageChangeListener(mPageChangeListener);
        mViewPagerTrack.setAdapter(mTrackAdapter);
        mViewPagerTrack.enableSmoothScroller();
    }

    private void setupDummyTrackViewPager(int itemCount) {
        mViewPagerTrackDummy.addOnPageChangeListener(mDummyPageChangeListener);
        mViewPagerTrackDummy.setOnTouchListener(new SyncScrollOnTouchListener(mViewPagerTrack));
        mViewPagerTrackDummy.setOffscreenPageLimit(itemCount);
        mViewPagerTrackDummy.setAdapter(new TransparentPagerAdapter(getFragmentContext(), itemCount));
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mCurrentPosition = position;
        }

        @Override
        public void onPageSelected(int position) {

            //mSlideDirection = position >= mCurrentPosition ? FunkyAnnotation.SLIDE_DIRECTION_FORWARD : FunkyAnnotation.SLIDE_DIRECTION_BACKWARD;
            mCurrentPosition = position;
            if (mCurrentPositionDummy != position)
                mViewPagerTrackDummy.setCurrentItem(position, false);
            if (mTrackAdapter != null) {
                setPlayerPrevious();
                setPlayerNext();

                FragmentPreBuyAudioCard fragment = mTrackAdapter.getItem(position);

                if (fragment.getItem() != null) {
                    mCutRingBackTone = fragment.getItem();

                    RingBackToneDTO ringBackToneDTO = fragment.getItem();
                    boolean isSelected = false;
                    if (ringBackToneDTO != null) {
                        isSelected = BaselineApplication.getApplication().getRbtConnector().isRingbackSelected(ringBackToneDTO.getId());
                    }
                    //mTvSet.setText(getString(!isSelected ? R.string.set_small : R.string.tune_update));
                    playTrack(fragment);
                    updateBlurImage(fragment);
                    updateTrack(fragment);
                    updateCard(position, isSelected, fragment);
                    updateVideoSupported(ringBackToneDTO);

                    if (ringBackToneDTO != null && !ringBackToneDTO.isDigitalStar()) {
                        if (mCutRingBackTone.getCutAllowed() == 1
                                && mCutRingBackTone.getFullTrack() != null &&
                                mCutRingBackTone.getFullTrack().getMadeReferenceId() != null) {
                            mTvEdit.setVisibility(View.VISIBLE);
                            setUpCut();
                        } else {
                            mTvEdit.setVisibility(View.GONE);
                        }
                    } else {
                        setCallerTune(ringBackToneDTO);
                    }
                } else {
                    mUserClickedPlayToggle = false;
                    playPrevious();
                }

                loadMoreMusic(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            /*if(state == ViewPager.SCROLL_STATE_DRAGGING && !mViewPagerTrack.isFakeDragging())
                mUserClickedPlayToggle = true;*/
            if (state == ViewPager.SCROLL_STATE_IDLE)
                showAnim(mLayoutBlurImageAudioTrackShadow);
            else
                hideAnimWithSpace(mLayoutBlurImageAudioTrackShadow);
        }
    };

    private ViewPager.OnPageChangeListener mDummyPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //mViewPagerTrack.offsetLeftAndRight(positionOffsetPixels / 2);
            mCurrentPositionDummy = position;
        }

        @Override
        public void onPageSelected(int position) {
            mCurrentPositionDummy = position;
            if (mCurrentPosition != position)
                mViewPagerTrack.setCurrentItem(position, true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            /*if(state == ViewPager.SCROLL_STATE_DRAGGING && !mViewPagerTrack.isFakeDragging())
                mUserClickedPlayToggle = true;*/
        }
    };

    /*public class ViewPagerScrollSync implements ViewPager.OnPageChangeListener {
        private ViewPager actor; // the one being scrolled
        private ViewPager target; // the one that needs to be scrolled in sync

        public ViewPagerScrollSync(ViewPager actor, ViewPager target) {
            this.actor = actor;
            this.target = target;
            actor.addOnPageChangeListener(this);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (actor.isFakeDragging()) {
                return;
            }
            if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                // actor has begun a drag
                target.beginFakeDrag();
            } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                // actor has finished settling
                target.endFakeDrag();
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (actor.isFakeDragging()) {
                return;
            }
            if (target.isFakeDragging()) {
                // calculate drag amount in pixels.
                // i don't have code for this off the top of my head, but you'll probably
                // have to store the last position and offset from the previous call to
                // this method and take the difference.
                float dx =
                target.fakeDragBy(dx);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (actor.isFakeDragging()) {
                return;
            }

            // Check isFakeDragging here because this callback also occurs when
            // the user lifts his finger on a drag. If it was a real drag, we will
            // have begun a fake drag of the target; otherwise it was probably a
            // programmatic change of the current page.
            if (!target.isFakeDragging()) {
                target.setCurrentItem(position);
            }
        }
    }*/

    private View.OnClickListener mClickListener = view -> {
        final int position = mViewPagerTrack.getCurrentItem();
        if(view.getId() == R.id.tv_set_prebuy_aud_track || view.getId() == R.id.tv_set_prebuy_vid_track) {
            RingBackToneDTO ringBackToneDTO = mTrackAdapter.getItem(position).getItem();
            ringBackToneDTO.setCutStart(-1);
            ringBackToneDTO.setCutEnd(-1);
            ringBackToneDTO.setCut(false);
            setCallerTune(ringBackToneDTO);
        }else if(view.getId() == R.id.ib_previous_control_aud_track) {
            //mUserClickedPlayToggle = true;
            //mSlideDirection = FunkyAnnotation.SLIDE_DIRECTION_BACKWARD;
            playPrevious();
        }else if(view.getId() == R.id.ib_play_control_aud_track) {
            mUserClickedPlayToggle = !mUserClickedPlayToggle;
            playToggle();
        }else if(view.getId() == R.id.ib_next_control_aud_track) {
            //mUserClickedPlayToggle = true;
            //mSlideDirection = FunkyAnnotation.SLIDE_DIRECTION_FORWARD;
            playNext();
        }else if(view.getId() == R.id.tv_share_prebuy_aud_track) {
            mActivityCallback.onItemClick(FragmentPreBuyAudio.this, view, mTrackAdapter.getItem(position).getItem(), position);
        }else if(view.getId() == R.id.tv_edit_prebuy_aud_track) {
            showCut();
        }else if(view.getId() == R.id.cut_close_btn) {
            hideCut();
        }else if(view.getId() == R.id.tv_set_cut) {
            stopTrack();
            if (mCutRingBackTone != null) {
                mCutRingBackTone.setCut(true);
                setCallerTune(mCutRingBackTone);
            }
        }else if(view.getId() == R.id.btn_iv_prebuy_back) {
            getRootActivity().onBackPressed();
        }else if(view.getId() == R.id.ib_shuffle_aud_track) {
            if (AppConfigurationValues.isUDPEnabled()) {
                stopTrack();
                RingBackToneDTO item = mTrackAdapter.getItem(position).getItem();
                addToUdp(item, null);
            }
        }
    };

    OnChipClickListener mChipClickListener = v -> {
        int id = v.getId();
        if (id != mCurrentLayoutChipId) {
            stopTrack();
            if (id == mChipMusic.getId()) {
                showAudioLayout();
            } else {
                mUserClickedPlayToggle = false;
                showVideoLayout();
            }
        }
    };

    private void updateBlurImage(final FragmentPreBuyAudioCard fragment) {
        /*if (fragment.getBlurImage() == null) {
            BitmapImageViewTarget newTarget = new BitmapImageViewTarget(mIvBlur) {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                    new LoadBlurBitmap(FragmentPreBuyAudio.this, fragment, bitmap).execute();
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    Bitmap bitmap = ((BitmapDrawable) errorDrawable).getBitmap();
                    new LoadBlurBitmap(FragmentPreBuyAudio.this, fragment, bitmap).execute();
                }

                @Override
                public void onLoadStarted(Drawable placeholder) {

                }
            };
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
            String imageUrl = fragment.getItem() != null ? fragment.getItem().getPrimaryImage() : "";
            String imageFinalUrl = AppUtils.getFitableImage(getFragmentContext(), imageUrl, imageSize);

            Glide.with(getFragmentContext())
                    .load(imageFinalUrl)
                    .asBitmap()
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(newTarget);
        } else {
            mIvBlur.setImageBitmap(fragment.getBlurImage());
        }*/

        int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
        String imageUrl = fragment.getItem() != null ? fragment.getItem().getPrimaryImage() : "";
        String imageFinalUrl = AppUtils.getFitableImage(getFragmentContext(), imageUrl, imageSize);

        Glide.with(getFragmentContext())
                .load(imageFinalUrl)
                .asBitmap()
                .placeholder(R.drawable.default_album_art)
                .error(R.drawable.default_album_art)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvBlur);
    }

    private void playTrack(FragmentPreBuyAudioCard fragment) {
        if (fragment == null || fragment.getItem() == null)
            return;
        final RingBackToneDTO ringBackToneDTO = fragment.getItem();
        String playURL = ringBackToneDTO.getPreviewStreamUrl();
        stopTrack();
        initVisualizer();
        getMusicPlayer().setMusicUrl(playURL);
        getMusicPlayer().setPreviewListener(new BaselineMusicPlayer.MusicPreviewListener() {
            @Override
            public void onPreviewPlaying(int progressStatus) {
                if (!isAdded()) return;
                Logger.d(getTag(), String.valueOf(progressStatus));
                if (progressStatus < AppConstant.MAX_PROGRESS_TO_UPDATE_PLAYER)
                    showTrackPlaying();
                if (AppUtils.isRecommendationQueueDelayLapsed(getMusicPlayer().getMediaPlayedInSeconds()))
                    BaselineApplication.getApplication().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
            }

            @Override
            public void onPreviewBuffering() {
                if (!isAdded()) return;
                Logger.d(getTag(), "onPreviewBuffering");
                showTrackLoading();
            }

            @Override
            public void onPreviewStopped() {
                if (!isAdded()) return;
                Logger.d(getTag(), "onPreviewStopped");
                showTrackStop();
            }

            @Override
            public void onPreviewCompleted() {
                if (!isAdded()) return;
                Logger.d(getTag(), "onPreviewCompleted");
                showTrackStop();
                if (AppUtils.isInternetAvailable())
                    scheduleAutoSlider();
            }

            @Override
            public void onPreviewError() {
                if (!isAdded()) return;
                Logger.d(getTag(), "onPreviewError");
                getRootActivity().showLongSnackBar(getString(R.string.preview_error));
                showTrackStop();
            }
        });
        //getMusicPlayer().startMusic(getFragmentContext());
        scheduleMusicSettlingTimeOut();
    }

    private void scheduleMusicSettlingTimeOut() {
        cancelMusicSettlingTimeOut();
        mRunnableScheduleMusic = () -> {
            if (isDetached() || isRemoving())
                return;
            if (mCurrentLayoutChipId == mChipMusic.getId() && mUserClickedPlayToggle)
                getMusicPlayer().startMusic(getFragmentContext());
        };
        mHandlerScheduleMusic = new Handler();
        mHandlerScheduleMusic.postDelayed(mRunnableScheduleMusic, MUSIC_SETTLING_DELAY);
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

    private boolean stopTrack() {
        if (getMusicPlayer().isMediaPlaying()) {
            stopVisualizer();
            getMusicPlayer().stopMusic();
            return true;
        }
        try {
            showTrackStop();
        } catch (Exception ignored) {
        }
        return false;
    }

    private void playPrevious() {
        if (mViewPagerTrack == null)
            return;
        int position = mViewPagerTrack.getCurrentItem();
        if (position > 0) {
            mViewPagerTrack.setCurrentItem(position - 1, true);
        }
        /*else {
            mUserClickedPlayToggle = false;
        }*/
    }

    private void playNext() {
        if (mViewPagerTrack == null)
            return;
        int position = mViewPagerTrack.getCurrentItem();
        if (position < mTrackAdapter.getCount() - 1) {
            mViewPagerTrack.setCurrentItem(position + 1, true);
        }
        /*else {
            mUserClickedPlayToggle = false;
        }*/
    }

    private void playToggle() {
        if (stopTrack())
            return;
        if (mViewPagerTrack != null) {
            int position = mViewPagerTrack.getCurrentItem();
            playTrack(mTrackAdapter.getItem(position));
        }
    }

    private void updateTrack(FragmentPreBuyAudioCard fragment) {
        if (fragment.getItem() != null) {
            RingBackToneDTO item = fragment.getItem();
            mTvArtist.setText(item.getPrimaryArtistName());
            mTvTrack.setText(item.getTrackName());
            mTvVideoArtist.setText(item.getPrimaryArtistName());
            mTvVideoTrack.setText(item.getTrackName());
        }
    }

    private void updateCard(final int position, final boolean isSelected, FragmentPreBuyAudioCard fragment) {
        fragment.showFavorite();
        fragment.toggleRbtSelected(isSelected);
        int left = position - 1;
        int right = position + 1;
        if (left >= 0) {
            FragmentPreBuyAudioCard leftFragment = mTrackAdapter.getItem(left);
            leftFragment.hideFavorite();
            leftFragment.toggleRbtSelected(false);
        }
        if (right < mTrackAdapter.getCount()) {
            FragmentPreBuyAudioCard rightFragment = mTrackAdapter.getItem(right);
            rightFragment.hideFavorite();
            rightFragment.toggleRbtSelected(false);
        }
    }

    private void showTrackLoading() {
        setTrackLoading();
        setPlayerPrevious();
        setPlayerNext();
    }

    private void showTrackPlaying() {
        setTrackPlaying();
        setPlayerPrevious();
        setPlayerNext();
    }

    private void showPlayerLoading() {
        showTrackLoading();
        mProgressPlayerContainer.setVisibility(View.VISIBLE);
        mLayoutPlayerControl.setVisibility(View.INVISIBLE);
    }

    private void showPlayerLoaded() {
        mProgressPlayerContainer.setVisibility(View.INVISIBLE);
        mLayoutPlayerControl.setVisibility(View.VISIBLE);
    }

    private void showTrackStop() {
        if (isDetached() || isRemoving())
            return;
        mProgressPlayer.setVisibility(View.INVISIBLE);
        mIbControlPlayPause.setVisibility(View.VISIBLE);
        mIbControlPlayPause.setImageResource(R.drawable.ic_play_accent_24dp);
    }

    /*private void showTrackPause() {
        showTrackStop();
    }*/

    private void setTrackLoading() {
        mProgressPlayer.setVisibility(View.VISIBLE);
        mIbControlPlayPause.setVisibility(View.INVISIBLE);
    }

    private void setTrackPlaying() {
        mProgressPlayer.setVisibility(View.INVISIBLE);
        mIbControlPlayPause.setVisibility(View.VISIBLE);
        mIbControlPlayPause.setImageResource(R.drawable.ic_pause_accent_24dp);
    }

    private void setPlayerPrevious() {
        if (mViewPagerTrack == null)
            return;
        int position = mViewPagerTrack.getCurrentItem();
        if (position <= 0) {
            togglePreviousNextButton(mIbControlPrevious, false);
            return;
        }
        togglePreviousNextButton(mIbControlPrevious, true);
    }

    private void setPlayerNext() {
        if (mViewPagerTrack == null)
            return;
        int position = mViewPagerTrack.getCurrentItem();
        if (position >= mTrackAdapter.getCount() - 1) {
            togglePreviousNextButton(mIbControlNext, false);
            return;
        }
        togglePreviousNextButton(mIbControlNext, true);
    }

    private void togglePreviousNextButton(AppCompatImageButton button, boolean enable) {
        button.setEnabled(enable);
        button.setAlpha(enable ? 1.0f : 0.3f);
        Drawable drawable = button.getDrawable();
        drawable.setColorFilter(ContextCompat.getColor(getFragmentContext(), R.color.black_transparent_60), PorterDuff.Mode.SRC_ATOP);
    }

    /**
     * Open bottom sheet to set caller tune
     *
     * @param item data model
     */
    private void setCallerTune(RingBackToneDTO item) {
        stopTrack();

        WidgetUtils.getSetCallerTuneBottomSheet(mCallerSource, item).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                mUserClickedPlayToggle = false;
            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                handleBottomSheetResult(success, message);
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                handleBottomSheetResult(success, message);
            }
        }).showSheet(getChildFragmentManager());
    }

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success) {
            if (SetCallerTunePlansBSFragment.fromPreBuyAudioShowPopUp) {
                getRootActivity().onBackPressed();
            } else {
                AppRatingPopup appRatingPopup = new AppRatingPopup(getRootActivity(), () -> getRootActivity().onBackPressed());
                appRatingPopup.show();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            stopVisualizer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUpCut() {
        mBlurView.setupWith(mRootView)
                .setFrameClearDrawable(mDecorView.getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(getFragmentContext()))
                .setBlurRadius(15f)
                .setHasFixedTransformationMatrix(true);

        int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
        String fitableImage = AppUtils.getFitableImage(getFragmentContext(), mCutRingBackTone.getPrimaryImage(), imageSize);

        Glide.with(getRootActivity())
                .load(fitableImage)
                .error(R.drawable.default_album_art)
                .placeholder(R.drawable.default_album_art)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCutBgBlurred);


        Glide.with(getRootActivity())
                .load(fitableImage)
                .error(R.drawable.default_album_art_circle)
                .placeholder(R.drawable.default_album_art_circle)
                .transform(new RoundTransform(getRootActivity()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mCutTrackImage);


        mCutTrackName.setText(mCutRingBackTone.getTrackName());
        mCutArtistName.setText(mCutRingBackTone.getPrimaryArtistName());


        if (mTypeToShow == AppConstant.TYPE_NOTIFICATION) {
            mCircularSeekBar.setMax(TRACK_DURATION - (int) AppConstant.DEFAULT_CUT_NOTIFICATION_TIME);
            mWheelProgressBar.setMax((int) AppConstant.DEFAULT_CUT_NOTIFICATION_TIME);
            mWheelRulerView.setMaxValue(TRACK_DURATION / 1000, AppConstant.TYPE_NOTIFICATION);
        } else {
            mCircularSeekBar.setMax(TRACK_DURATION - (int) AppConstant.DEFAULT_CUT_RINGTONE_TIME);
            mWheelProgressBar.setMax((int) AppConstant.DEFAULT_CUT_RINGTONE_TIME);
            mWheelRulerView.setMaxValue(TRACK_DURATION / 1000, AppConstant.TYPE_RINGTONE);
        }

        String path = BaselineApplication.getApplication().getRbtConnector().generateUrl(mCutRingBackTone, PreviewUrlGeneration.FT_PREVIEW, mCircularSeekBar.getProgress(), (int) (mCircularSeekBar.getProgress() + AppConstant.DEFAULT_CUT_RINGTONE_TIME), PreviewUrlGeneration.RINGTONE, "");

        makeMadeRequest(false, path);

        final CircularSeekBar.OnCircularSeekBarChangeListener circularListener = new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromWheelView) {


                circularSeekBar.setPlayedProgress(0);
                hideWheelProgressBar();

                if (!fromWheelView) {
                    float interval = mWheelRulerView.getLineInterval();
                    mWheelRulerView.getScrollView().scrollTo((int) (progress / 1000 * interval), 0);
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                if (mDjPlayer != null) {
                    mDjPlayer.stop();
                }
                mCutView.findViewById(R.id.background_overlay).setVisibility(View.GONE);
                mCutRingBackTone.setCutStart(mCircularSeekBar.getProgress());
                mCutRingBackTone.setCutEnd((int) (mCircularSeekBar.getProgress() + AppConstant.DEFAULT_CUT_RINGTONE_TIME));
                playSound(mCutRingBackTone);
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
                if (mDjPlayer != null) {
                    mDjPlayer.playAudioMade(R.raw.rewind);
                }
                mCircularSeekBar.setPlayedColor(getFragmentContext().getResources().getColor(R.color.transparent));
                mCutView.findViewById(R.id.background_overlay).setVisibility(View.VISIBLE);
                if (getMusicPlayer() != null) {
                    getMusicPlayer().stopMusic();
                }

            }
        };

        mCircularSeekBar.setOnSeekBarChangeListener(circularListener);

        mWheelRulerView.setOnScrollViewTouch(new WheelRulerView.OnScrollViewTouchListener() {
            @Override
            public void onProgressChanged(int scrollX) {

                //There is issue if track info are displayed on top of wheel ruler
                //when click on it it will be handle by the wheel ruler
                //Check if cut is visible

                if (mCutVisible) {
                    float interval = mWheelRulerView.getLineInterval();
                    mCircularSeekBar.setProgress((int) (scrollX / interval * 1000), true);
                    //circularListener.onProgressChanged(mCircularSeekBar, mCircularSeekBar.getProgress(), true);
                }

            }

            @Override
            public void onStopTrackingTouch(int scrollX) {
                if (mCutVisible) {
                    float interval = mWheelRulerView.getLineInterval();
                    mCircularSeekBar.setProgress((int) (scrollX / interval * 1000), true);
                    circularListener.onStopTrackingTouch(mCircularSeekBar);
                }

            }

            @Override
            public void onStartTrackingTouch() {

                if (mCutVisible) {
                    circularListener.onStartTrackingTouch(mCircularSeekBar);
                }

            }
        });


        mCutNextButton.setOnClickListener(view -> {
            if (mCutVisible) {
                float interval = mWheelRulerView.getLineInterval();

                if (mCurrentCutType == AppConstant.TYPE_RINGTONE) {
                    mWheelRulerView.getScrollView().scrollBy((int) (30 * interval), 0);
                } else {
                    mWheelRulerView.getScrollView().scrollBy((int) (5 * interval), 0);
                }

                circularListener.onStartTrackingTouch(mCircularSeekBar);
                mCircularSeekBar.setProgress((int) (mWheelRulerView.getScrollView().getScrollX() / interval * 1000), true);
                circularListener.onStopTrackingTouch(mCircularSeekBar);
            }
        });

        mCutPrevButton.setOnClickListener(view -> {
            if (mCutVisible) {
                float interval = mWheelRulerView.getLineInterval();
                if (mCurrentCutType == AppConstant.TYPE_RINGTONE) {
                    mWheelRulerView.getScrollView().scrollBy((int) (-30 * interval), 0);
                } else {
                    mWheelRulerView.getScrollView().scrollBy((int) (-5 * interval), 0);
                }


                circularListener.onStartTrackingTouch(mCircularSeekBar);
                mCircularSeekBar.setProgress((int) (mWheelRulerView.getScrollView().getScrollX() / interval * 1000), true);
                circularListener.onStopTrackingTouch(mCircularSeekBar);
            }
        });

        mCutPlayToggleImageView.setOnClickListener(view -> cutPlayToggle());

        mCircularSeekBar.setProgress(0, false);

    }

    private void makeMadeRequest(boolean autoPlay, String path) {
        if (TextUtils.isEmpty(path))
            return;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(path)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    response.close();
                    throw new IOException("Unexpected code " + response);
                } else {
                    final String duration = response.headers().get("Content-FT-Duration");
                    if (duration != null && !duration.isEmpty()) {

                        getRootActivity().runOnUiThread(() -> {
                            if (isAdded() && mCutVisible) {
                                TRACK_DURATION = Integer.parseInt(duration) * 1000;
                                if (autoPlay) {
                                    playCut(path);
                                } else {
                                    if (mTypeToShow == AppConstant.TYPE_NOTIFICATION) {
                                        mCircularSeekBar.setMax(Integer.parseInt(duration) * 1000 - (int) AppConstant.DEFAULT_CUT_NOTIFICATION_TIME);
                                        mWheelRulerView.setMaxValue(Integer.parseInt(duration), AppConstant.TYPE_NOTIFICATION);
                                    } else {
                                        mCircularSeekBar.setMax(Integer.parseInt(duration) * 1000 - (int) AppConstant.DEFAULT_CUT_RINGTONE_TIME);
                                        mWheelRulerView.setMaxValue(Integer.parseInt(duration), AppConstant.TYPE_RINGTONE);
                                    }
                                }
                            }
                        });

                    }
                }
                response.close();
            }
        });
    }

    public static class RoundTransform extends BitmapTransformation {
        RoundTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    private void hideWheelProgressBar() {
        ValueAnimator mHideAlphaAnimator = ValueAnimator
                .ofFloat(1, 0F)
                .setDuration(100);

        mHideAlphaAnimator.addUpdateListener(valueAnimator -> {
            Float value = (Float) valueAnimator.getAnimatedValue();
            mWheelProgressBar.setAlpha(value);
            if (value == 0) {
                mWheelProgressBar.setProgress(0);
                mWheelProgressBar.setAlpha(1);
            }
        });

        if (!mHideAlphaAnimator.isRunning() && mWheelProgressBar.getAlpha() > 0) {
            mHideAlphaAnimator.start();
        }
    }

    private void playSound(RingBackToneDTO ringBackToneDTO) {

        if (getMusicPlayer().isMediaPlaying()) {
            getMusicPlayer().stopMusic();
        } else {
            try {
                getMusicPlayer().stopMusic();
            } catch (Exception ignored) {
            }
        }

        int start = mCircularSeekBar.getProgress();
        int end = (int) (AppConstant.DEFAULT_CUT_RINGTONE_TIME + start);
        if (mCurrentCutType == AppConstant.TYPE_NOTIFICATION) {
            end = (int) (AppConstant.DEFAULT_CUT_NOTIFICATION_TIME + start);
        }
        //If user switch from ringtone / Notification update the end
        ringBackToneDTO.setCutEnd(end);
        if (mCurrentCutType == AppConstant.TYPE_RINGTONE) {
            mCutNextButton.setImageResource(R.drawable.ic_cut_next_30s);
            mCutPrevButton.setImageResource(R.drawable.ic_cut_prev_30s);
            mCurrentCutType = AppConstant.TYPE_RINGTONE;
            mCircularSeekBar.setType(mCurrentCutType);
            mWheelProgressBar.setProgressDrawable(ContextCompat.getDrawable(getRootActivity(), R.drawable.horizontal_wheel_progress_ringtone));

            mCircularSeekBar.setMax(TRACK_DURATION - (int) AppConstant.DEFAULT_CUT_RINGTONE_TIME);
            mWheelProgressBar.setMax((int) AppConstant.DEFAULT_CUT_RINGTONE_TIME);
            mWheelRulerView.setMaxValue(TRACK_DURATION / 1000, AppConstant.TYPE_RINGTONE);
        }/* else {

        }*/

        int ringtoneOrNotification = PreviewUrlGeneration.RINGTONE;
        if (mCurrentCutType == AppConstant.TYPE_NOTIFICATION) {
            ringtoneOrNotification = PreviewUrlGeneration.NOTIFICATION;
        }

        String url = BaselineApplication.getApplication().getRbtConnector().generateUrl(ringBackToneDTO, PreviewUrlGeneration.FT_PREVIEW, mCircularSeekBar.getProgress(), end, ringtoneOrNotification, "");
        playCut(url);

//        makeMadeRequest(true, url);
    }

    private void playCut(String url) {
        stopTrack();
        getMusicPlayer().setMusicUrl(url);
        getMusicPlayer().setPreviewListener(new BaselineMusicPlayer.MusicPreviewListener() {
            @Override
            public void onPreviewPlaying(int progressStatus) {
                if (!isAdded()) return;
                Logger.d(getTag(), progressStatus + "");
                if (progressStatus < AppConstant.MAX_PROGRESS_TO_UPDATE_PLAYER)
                    showCutPlaying();

                mCircularSeekBar.setPlayedColor(getFragmentContext().getResources().getColor(R.color.colorAccent));
                mCircularSeekBar.setPlayedProgress(getMusicPlayer().getCurrentPosition());
                mWheelProgressBar.setProgress(getMusicPlayer().getCurrentPosition());

                RingBackToneDTO ringBackToneDTO = mTrackAdapter.getItem(mCurrentPosition).getItem();
                if (ringBackToneDTO != null && AppUtils.isRecommendationQueueDelayLapsed(getMusicPlayer().getMediaPlayedInSeconds()))
                    BaselineApplication.getApplication().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
            }

            @Override
            public void onPreviewBuffering() {
                if (!isAdded()) return;
                mCircularSeekBar.setPlayedProgress(0);
                showCutLoading();
            }

            @Override
            public void onPreviewStopped() {
                if (!isAdded()) return;
                mCircularSeekBar.setPlayedProgress(0);
                mCircularSeekBar.setPlayedColor(getFragmentContext().getResources().getColor(R.color.transparent));
                hideWheelProgressBar();
                showCutStop();
            }

            @Override
            public void onPreviewCompleted() {
                if (!isAdded()) return;
                mCircularSeekBar.setPlayedProgress(0);
                mCircularSeekBar.setPlayedColor(getFragmentContext().getResources().getColor(R.color.transparent));
                hideWheelProgressBar();
                showCutStop();
            }

            @Override
            public void onPreviewError() {
                if (!isAdded()) return;
                getRootActivity().showLongSnackBar(getString(R.string.preview_error));
                mCircularSeekBar.setPlayedProgress(0);
                mCircularSeekBar.setPlayedColor(getFragmentContext().getResources().getColor(R.color.transparent));
                hideWheelProgressBar();
                showCutStop();
            }
        });
        getMusicPlayer().startMusic(getFragmentContext());
    }

    public void showCut() {
        stopVisualizer();
        stopTrack();
        mCutVisible = true;
        mCutView.setVisibility(View.VISIBLE);
        mCircularSeekBar.setProgress(0, false);
        playSound(mCutRingBackTone);
    }

    public void hideCut() {
        stopTrack();
        if (mDjPlayer != null) {
            mDjPlayer.stop();
        }
        mCutVisible = false;
        mCutView.setVisibility(View.INVISIBLE);
        initVisualizer();
    }

    private void showCutPlaying() {
        mCutProgressBar.setVisibility(View.INVISIBLE);
        mCutPlayToggleImageView.setVisibility(View.VISIBLE);
        mCutPlayToggleImageView.setImageResource(R.drawable.ic_pause_accent_18dp);
    }

    private void showCutLoading() {
        mCutProgressBar.setVisibility(View.VISIBLE);
        mCutPlayToggleImageView.setVisibility(View.INVISIBLE);
    }

    private void showCutStop() {
        if (isDetached() || isRemoving())
            return;
        mCutProgressBar.setVisibility(View.INVISIBLE);
        mCutPlayToggleImageView.setVisibility(View.VISIBLE);
        mCutPlayToggleImageView.setImageResource(R.drawable.ic_play_accent_18dp);
    }

    private void cutPlayToggle() {
        if (stopTrack())
            return;
        if (mCutVisible && mCutRingBackTone != null && mCutView != null) {
            playSound(mCutRingBackTone);
        }
    }

    private void showAudioLayout() {
        updateChipLayout(true);
        mCurrentLayoutChipId = mChipMusic.getId();

        hideVideoLayout();
        showAnim(mLayoutParentAudio);
        showAnim(mLayoutChildAudio);
        showAnim(mViewPagerTrackDummy);

        /*if (mBackgroundColorTransition == null)
            mBackgroundColorTransition = (TransitionDrawable) mIvTransitionImgBackGround.getBackground();
        mBackgroundColorTransition.startTransition((int) DEFAULT_ANIMATION_DURATION);*/
        if (mBackgroundCrossFader != null)
            mBackgroundCrossFader.startTransition(DEFAULT_ANIMATION_DURATION);
    }

    private void showVideoLayout() {
        updateChipLayout(false);
        mCurrentLayoutChipId = mChipVideo.getId();

        hideAudioLayout();
        showAnim(mLayoutParentVideo);
       /* mIvTransitionImgBackGround.setBackground(null);
        mIvTransitionImgBackGround.setBackgroundColor(ContextCompat.getColor(getFragmentContext(), R.color.black));*/

        /*if (mBackgroundColorTransition == null)
            mBackgroundColorTransition = (TransitionDrawable) mIvTransitionImgBackGround.getBackground();
        mBackgroundColorTransition.reverseTransition((int) DEFAULT_ANIMATION_DURATION);*/
        if (mBackgroundCrossFader != null)
            mBackgroundCrossFader.reverseTransition(DEFAULT_ANIMATION_DURATION);
    }

    /*private void playYouTube() {
        if (mYouTubePlayer != null && mTrackAdapter != null) {
            RingBackToneDTO ringBackToneDTO = mTrackAdapter.getItem(mCurrentPosition).getItem();
            if (ringBackToneDTO != null && !TextUtils.isEmpty(ringBackToneDTO.getYoutubeLink()))
                mYouTubePlayer.loadVideo(ringBackToneDTO.getYoutubeLink());
        }
    }*/

    private void hideAudioLayout() {
        hideAnim(mLayoutParentAudio);
        hideAnim(mLayoutChildAudio);
        hideAnim(mViewPagerTrackDummy);
    }

    private void hideVideoLayout() {
        hideAnim(mLayoutParentVideo);
    }

    private void updateChipLayout(boolean music) {
        updateChipLayout(mChipMusic, music);
        updateChipLayout(mChipVideo, !music);
    }

    private void updateChipLayout(Chip chip, boolean active) {
        chip.setBackgroundColor(ContextCompat.getColor(getFragmentContext(), active ? R.color.white : R.color.transparent));
        chip.setTextColor(ContextCompat.getColor(getFragmentContext(), active ? R.color.colorAccent : R.color.white_transparent_80));
        chip.setStrokeColor(ContextCompat.getColor(getFragmentContext(), active ? R.color.white : R.color.white_transparent_80));
    }

    private void updateVideoSupported(RingBackToneDTO ringBackToneDTO) {
        //mChipVideo.setVisibility(ringBackToneDTO != null && !TextUtils.isEmpty(ringBackToneDTO.getYoutubeLink()) ? View.VISIBLE : View.GONE);
        mChipVideo.setVisibility(View.GONE);
    }

    private void showAnim(View view) {
        if (view == null || view.getVisibility() == View.VISIBLE)
            return;
        view.animate()
                .alpha(1.0f)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void hideAnim(View view) {
        if (view == null || view.getVisibility() == View.GONE)
            return;
        view.animate()
                .alpha(0.0f)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private void hideAnimWithSpace(View view) {
        if (view == null || view.getVisibility() == View.INVISIBLE)
            return;
        view.animate()
                .alpha(0.0f)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                    }
                });
    }

    private void setupTrackBlur() {
        mBlurViewAudioTrack.setupWith(mLayoutBlurImageAudioTrack)
                .setFrameClearDrawable(mDecorView.getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(getFragmentContext()))
                .setBlurRadius(5f)
                .setHasFixedTransformationMatrix(true);

        mBlurViewAudioTrackShadow.setupWith(mLayoutBlurImageAudioTrackShadow)
                .setFrameClearDrawable(mDecorView.getBackground())
                .setBlurAlgorithm(new RenderScriptBlur(getFragmentContext()))
                .setBlurRadius(5f)
                .setHasFixedTransformationMatrix(true);
    }

    private void addToUdp(final RingBackToneDTO ringBackToneDTO, String autoAddUdpId) {
        if (ringBackToneDTO == null)
            return;
        WidgetUtils.addToUDP(ringBackToneDTO, autoAddUdpId).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                handleAddToShuffleBSResponse(ringBackToneDTO, success, message, result);
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                handleAddToShuffleBSResponse(ringBackToneDTO, success, message, null);
            }

            @Override
            public void dismissWithReason(DialogInterface dialogInterface, Object reason) {
                handleAddToShuffleBSResponse(ringBackToneDTO, false, null, reason);
            }
        }).showSheet(getChildFragmentManager());
    }

    private void handleAddToShuffleBSResponse(RingBackToneDTO itemToAdd, boolean success, String message, Object result) {
        if (result instanceof String) {
            String resp = (String) result;
            if (AddRbt2UdpBSChildFragment.KEY_CREATE_UDP.equals(resp))
                createUserDefinedPlayList(itemToAdd, null, getString(R.string.create_udp_description));
        }
    }

    private void createUserDefinedPlayList(final RingBackToneDTO itemToAdd, String input, String description) {
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(getActivity(), getString(R.string.creating_shuffle_message));
            mProgressDialog.setCancelable(false);
        }
        AppDialog.getSingleInputDialog(getFragmentContext(), getString(R.string.title_create_udp), getString(R.string.ok), getString(R.string.cancel),
                true, true, input, description, new SelectionDialogListener<String>() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id, final String data) {
                        dialog.dismiss();
                        mProgressDialog.show();
                        BaselineApplication.getApplication().getRbtConnector().createUserDefinedPlaylist(data, new AppBaselineCallback<UserDefinedPlaylistDTO>() {
                            @Override
                            public void success(UserDefinedPlaylistDTO result) {
                                if (!isAdded()) return;
                                mProgressDialog.dismiss();
                                //getRootActivity().showShortToast(getString(R.string.shuffle_create_success_msg, data));
                                addToUdp(itemToAdd, result.getId());
                            }

                            @Override
                            public void failure(String errMsg) {
                                if (!isAdded()) return;
                                mProgressDialog.dismiss();
                                createUserDefinedPlayList(itemToAdd, data, errMsg);
                            }
                        });
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public class SyncScrollOnTouchListener implements View.OnTouchListener {

        private final View syncedView;

        SyncScrollOnTouchListener(@NonNull View syncedView) {
            this.syncedView = syncedView;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            MotionEvent syncEvent = MotionEvent.obtain(motionEvent);
            float width1 = view.getWidth();
            float width2 = syncedView.getWidth();

            //sync motion of two view pagers by simulating a touch event
            //offset by its X position, and scaled by width ratio
            syncEvent.setLocation(syncedView.getX() + motionEvent.getX() * width2 / width1,
                    motionEvent.getY());
            syncedView.onTouchEvent(syncEvent);
            return false;
        }
    }

    private void scheduleAutoSlider() {
        if (mRunnableAutoSlider != null) {
            if (mHandlerAutoSlider != null) {
                mHandlerAutoSlider.removeCallbacks(mRunnableAutoSlider);
                mHandlerAutoSlider = null;
            }
            mRunnableAutoSlider = null;
        }
        mRunnableAutoSlider = () -> {
            if (isDetached() || isRemoving())
                return;
            if (!getMusicPlayer().isMediaPlaying() && mUserClickedPlayToggle) {
                if (mSlideDirection == FunkyAnnotation.SLIDE_DIRECTION_FORWARD)
                    playNext();
                else
                    playPrevious();
            }
        };
        mHandlerAutoSlider = new Handler();
        mHandlerAutoSlider.postDelayed(mRunnableAutoSlider, MUSIC_SETTLING_DELAY);
    }

    private void initVisualizer() {
        if (AppConfigurationValues.isPrebuyVisualizerEnabled() && PermissionUtil.hasPermission(getRootActivity(), PermissionUtil.Permission.RECORD_AUDIO)) {
            audioPlayerVisualizer.setVisibility(View.VISIBLE);
            audioPlayerVisualizer.setPlayer(getMusicPlayer().getAudioSessionId());
        } else {
            audioPlayerVisualizer.setVisibility(View.INVISIBLE);
        }
    }

    private void stopVisualizer() {
        if (AppConfigurationValues.isPrebuyVisualizerEnabled() && audioPlayerVisualizer != null)
            audioPlayerVisualizer.stop();
    }

    private void loadMoreMusic(int position) {
        if (mPaginationSupported && !mLoadMoreOffsetLimitReached && !mIsMusicLoading
                && mTrackAdapter != null && (position + 1) >= (mTrackAdapter.getCount() - LOAD_MORE_THREAD_SOLD)) {
            if (mLoadMoreOffset == mTrackAdapter.getCount())
                return;
            mLoadMoreOffset = mTrackAdapter.getCount();
            //addLoadingMusic();
            loadMoreMusicFromRemote(mLoadMoreOffset, mLoadMoreChartId);
        }
    }

    private synchronized void loadMoreMusicFromRemote(int offset, String chartId) {
        BaselineApplication.getApplication().getRbtConnector().getChartContents(offset, chartId, new AppBaselineCallback<ChartItemDTO>() {
            @Override
            public void success(ChartItemDTO result) {
                //removeLoadingMusic();
                addRemoteMusic(result.getTotalItemCount(), result.getRingBackToneDTOS());
            }

            @Override
            public void failure(String message) {
                mLoadMoreOffsetLimitReached = true;
                //removeLoadingMusic();
                mIsMusicLoading = false;
            }
        });
    }

    private void addLoadingMusic() {
        if (mTrackAdapter != null) {
            mIsMusicLoading = true;
            FragmentPreBuyAudioCard item = FragmentPreBuyAudioCard.newInstance(null);
            mTrackAdapter.addFragment(item);
            mTrackAdapter.notifyDataSetChanged();
            mViewPagerTrack.setOffscreenPageLimit(mTrackAdapter.getCount());
            setupDummyTrackViewPager(mTrackAdapter.getCount());
        }
    }

    private void removeLoadingMusic() {
        if (mTrackAdapter != null && mTrackAdapter.getItem(mTrackAdapter.getCount() - 1).getItem() == null) {
            mTrackAdapter.removeItem(mTrackAdapter.getCount() - 1);
            mTrackAdapter.notifyDataSetChanged();
            mViewPagerTrack.setOffscreenPageLimit(mTrackAdapter.getCount());
            setupDummyTrackViewPager(mTrackAdapter.getCount());
        }
    }

    private void addRemoteMusic(int totalItemCount, List<RingBackToneDTO> items) {
        if (mTrackAdapter != null) {
            List<FragmentPreBuyAudioCard> list = new ArrayList<>(items.size());
            for (RingBackToneDTO item : items)
                list.add(FragmentPreBuyAudioCard.newInstance(item));
            mTrackAdapter.addFragments(list);
            mTrackAdapter.notifyDataSetChanged();
            mViewPagerTrack.setOffscreenPageLimit(mTrackAdapter.getCount());
            setupDummyTrackViewPager(mTrackAdapter.getCount());
            if (totalItemCount <= mTrackAdapter.getCount())
                mLoadMoreOffsetLimitReached = true;
            mIsMusicLoading = false;
        }
    }
}
