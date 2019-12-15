package com.onmobile.rbt.baseline.bottomsheet.action;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.model.UserActivityRbtDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;
import com.onmobile.rbt.baseline.util.Logger;
import com.onmobile.rbt.baseline.util.MarginDividerItemDecoration;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 30-11-2018.
 */

@SuppressLint("ValidFragment")
public class BottomSheetListActionFragment extends BottomSheetDialogFragment {

    private List<BSLAModel> mList;
    @FunkyAnnotation.RegistrationBSItemType
    private int mItemType;
    private Object mData;
    private BSLListener mBslListener;
    private RootViewHolder mItemHolder;

    private FrameLayout shuffleContainer = null;
    private View otherViews = null;

    private CardView cardView;
    private ViewGroup layoutPlayer;
    private AppCompatImageButton imgPlayPause;
    private ContentLoadingProgressBar playerProgress;

    public interface BSLListener {
        void onItemClicked(int id, String label);
    }

    @SuppressLint("ValidFragment")
    public BottomSheetListActionFragment(List<BSLAModel> list, BSLListener bslListener) {
        this.mList = list;
        this.mBslListener = bslListener;
    }

    @SuppressLint("ValidFragment")
    public BottomSheetListActionFragment(@FunkyAnnotation.RegistrationBSItemType int itemType, Object data, List<BSLAModel> list, BSLListener bslListener) {
        this.mItemType = itemType;
        this.mData = data;
        this.mList = list;
        this.mBslListener = bslListener;
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismissAllowingStateLoss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        LinearLayout view = new LinearLayout(getContext());
        view.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(viewParams);

        if (mData != null && mData instanceof UserActivityRbtDTO) {
            //FrameLayout shuffleContainer = null;
            switch (mItemType) {
                case FunkyAnnotation.TYPE_BS_REG_SET_TUNES:
                case FunkyAnnotation.TYPE_BS_REG_PROFILE_TUNES:
                case FunkyAnnotation.TYPE_BS_REG_NAME_TUNES:
                    otherViews = View.inflate(getContext(), R.layout.layout_activity_item_ringback, null);
                    mItemHolder = new RingbackViewHolder(otherViews);
                    break;
                case FunkyAnnotation.TYPE_BS_REG_SHUFFLE_TUNES:
                    shuffleContainer = new FrameLayout(getContext());
                    FrameLayout.LayoutParams shuffleParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    //shuffleParams.height = (int) (view.getResources().getDimension(R.dimen.horizontal_music_item_card_size) * 1.2);
                    shuffleContainer.setLayoutParams(shuffleParams);
                    shuffleContainer.setId(AppConstant.NUMBER_TO_GENERATE_RANDOM_ID + mItemType);
                    break;
            }
            if (otherViews != null) {
                addSpace(view);
                try {
                    View divider = otherViews.findViewById(R.id.divider);
                    View rightLayout = otherViews.findViewById(R.id.right_layout);
                    divider.setVisibility(View.GONE);
                    rightLayout.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                view.addView(otherViews);
                if (mItemHolder != null)
                    mItemHolder.bind(mData, 0);
                //addSpace(view);
            } else if (shuffleContainer != null) {
                addSpace(view);
                view.addView(shuffleContainer);
            }
        }

        if (mList.size() > 0) {
            RecyclerView recyclerView = new RecyclerView(getContext());
            RecyclerView.LayoutParams recyclerParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            recyclerView.setLayoutParams(recyclerParams);
            view.addView(recyclerView);
            setupRecyclerView(recyclerView);
        }

        dialog.setContentView(view);

        try {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) view.getParent()).getLayoutParams();
            CoordinatorLayout.Behavior behavior = params.getBehavior();

            if (behavior instanceof BottomSheetBehavior) {
                ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*if (shuffleContainer != null && mData instanceof UserActivityRbtDTO) {
            UserActivityRbtDTO userActivityRbtDTO = (UserActivityRbtDTO) mData;
            if (userActivityRbtDTO.getUserActivityItemDTO().getData() instanceof ChartItemDTO) {
                ChartItemDTO chartItemDTO = (ChartItemDTO) userActivityRbtDTO.getUserActivityItemDTO().getData();
                RingBackToneDTO ringBackToneDTO = new RingBackToneDTO();
                ringBackToneDTO.setId(String.valueOf(chartItemDTO.getId()));
                FragmentHorizontalMusic shuffleFragment = FragmentHorizontalMusic.newInstance(FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SHUFFLE, ringBackToneDTO, true, false);
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(shuffleContainer.getId(), shuffleFragment, shuffleFragment.getFragmentTag()).commitAllowingStateLoss();
            }
        }*/
    }

    private void addSpace(LinearLayout view) {
        Space space = new Space(getContext());
        int height = (int) getResources().getDimension(R.dimen.activity_margin_half);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        space.setLayoutParams(params);
        view.addView(space);
    }

    /*@Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_sheet_list, null);

        if (mList.size() > 0) {
            RecyclerView recyclerView = contentView.findViewById(R.id.bsl_recycler_view);
            setupRecyclerView(recyclerView);
        }

        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }*/

    private void setupRecyclerView(RecyclerView recView) {
        recView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(layoutManager);
        recView.addItemDecoration(new MarginDividerItemDecoration(getContext()));
        recView.setItemAnimator(new DefaultItemAnimator());
        BSLARecyclerViewAdapter BSLARecyclerViewAdapter = new BSLARecyclerViewAdapter(getContext(), mList);
        recView.setAdapter(BSLARecyclerViewAdapter);
        BSLARecyclerViewAdapter.setOnItemClickListener((position, v) -> {
            if (mBslListener != null)
                mBslListener.onItemClicked(mList.get(position).getId(), mList.get(position).getLabel());
            dismissAllowingStateLoss();
        });
    }

    public class RingbackViewHolder extends RootViewHolder<UserActivityRbtDTO> implements View.OnClickListener {
        ImageView rbtImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        TextView callerTextView;
        TextView setDateTextView;
        RelativeLayout parentLayout;
        TextView endDateTextView;

        RingbackViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            titleTextView = view.findViewById(R.id.title);
            subTitleTextView = view.findViewById(R.id.subtitle);
            callerTextView = view.findViewById(R.id.caller_textview);
            setDateTextView = view.findViewById(R.id.setdate_textview);
            parentLayout = view.findViewById(R.id.parent_layout);
            rbtImageView = view.findViewById(R.id.rbt_imageview);
            endDateTextView = view.findViewById(R.id.end_date_textview);

            cardView = view.findViewById(R.id.card_rbt_imageview);
            layoutPlayer = view.findViewById(R.id.layout_root_player_prebuy);
            imgPlayPause = view.findViewById(R.id.ib_play_activity_rbt);
            playerProgress = view.findViewById(R.id.progress_play_activity_rbt);

            cardView.setOnClickListener(this);
            layoutPlayer.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            setTrackStop();
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            String fitableImage = AppUtils.getFitableImage(getContext(), data.getUserActivityItemDTO().getPreviewImgUrl(), imageSize);
            Glide.with(getContext())
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rbtImageView);

            if (data.isHistory()) {
                if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getEndDate())) {
                    endDateTextView.setText(data.getUserActivityItemDTO().getEndDate());
                    endDateTextView.setVisibility(View.VISIBLE);
                } else {
                    endDateTextView.setVisibility(View.GONE);
                }
            } else {
                endDateTextView.setVisibility(View.GONE);
            }

            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
            subTitleTextView.setText(data.getUserActivityItemDTO().getSubTitle());
//            callerTextView.setText(String.format(getString(!data.isHistory() ? R.string.activity_caller_text_active : R.string.activity_caller_text_history), TextUtils.isEmpty(data.getUserActivityItemDTO().getPlayingFor()) ?
//                    getString(R.string.all_callers) : data.getUserActivityItemDTO().getPlayingFor()));

            if(!AppConfigurationValues.isSelectionModel() && data.getUserActivityItemDTO().isDownloadedOnly()) {
                callerTextView.setText(R.string.download_model_not_playing_anyone);
            }
            else{
                callerTextView.setText(String.format(getString(!data.isHistory() ? R.string.activity_caller_text_active : R.string.activity_caller_text_history), TextUtils.isEmpty(data.getUserActivityItemDTO().getPlayingFor()) ?
                        getString(R.string.all_callers) : data.getUserActivityItemDTO().getPlayingFor()));
            }

            if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getSetDate())) {
                setDateTextView.setText(String.format(getString(R.string.activity_setdate_text), data.getUserActivityItemDTO().getSetDate()));
                setDateTextView.setVisibility(View.VISIBLE);
            } else {
                setDateTextView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == cardView.getId() || v.getId() == layoutPlayer.getId())
                playToggle();
        }
    }

    private BaselineMusicPlayer getMusicPlayer() {
        return BaselineMusicPlayer.getInstance();
    }

    private BaselineMusicPlayer.MusicPreviewListener mMusicPreviewListener = new BaselineMusicPlayer.MusicPreviewListener() {
        @Override
        public void onPreviewPlaying(int progressStatus) {
            Logger.d(getTag(), progressStatus + "");
            if (progressStatus < AppConstant.MAX_PROGRESS_TO_UPDATE_PLAYER)
                setTrackPlaying();
            if (getMusicPlayer() != null && AppUtils.isRecommendationQueueDelayLapsed(getMusicPlayer().getMediaPlayedInSeconds())) {
                RingBackToneDTO ringBackToneDTO = getRingBackToneIfAvailable();
                if (ringBackToneDTO != null && ringBackToneDTO.isRingBackMusic())
                    BaselineApplication.getApplication().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
            }
        }

        @Override
        public void onPreviewBuffering() {
            setTrackLoading();
        }

        @Override
        public void onPreviewStopped() {
            setTrackStop();
        }

        @Override
        public void onPreviewCompleted() {
            setTrackStop();
        }

        @Override
        public void onPreviewError() {
            setTrackStop();
        }
    };

    private void playToggle() {
        if (stopTrack())
            return;
        RingBackToneDTO ringBackToneDTO = getRingBackToneIfAvailable();
        if (ringBackToneDTO != null)
            playTrack(ringBackToneDTO.getPreviewStreamUrl());

    }

    private void playTrack(String url) {
        if (TextUtils.isEmpty(url))
            return;
        stopTrack();
        getMusicPlayer().setMusicUrl(url);
        getMusicPlayer().setPreviewListener(mMusicPreviewListener);
        getMusicPlayer().startMusic(getActivity());
    }

    private boolean stopTrack() {
        if (getMusicPlayer().isMediaPlaying()) {
            getMusicPlayer().stopMusic();
            return true;
        } else {
            try {
                getMusicPlayer().stopMusic();
            } catch (Exception e) {

            }
        }
        return false;
    }

    private void setTrackLoading() {
        if (otherViews != null) {
            imgPlayPause.setVisibility(View.INVISIBLE);
            playerProgress.setVisibility(View.VISIBLE);
        }
    }

    private void setTrackPlaying() {
        if (otherViews != null) {
            imgPlayPause.setVisibility(View.VISIBLE);
            playerProgress.setVisibility(View.INVISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_pause_accent_10dp);
        }
    }

    private void setTrackStop() {
        if (otherViews != null) {
            imgPlayPause.setVisibility(View.VISIBLE);
            playerProgress.setVisibility(View.INVISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_play_accent_10dp);
        }
    }

    @Override
    public void onDestroy() {
        stopTrack();
        super.onDestroy();
    }

    private RingBackToneDTO getRingBackToneIfAvailable() {
        if (mData != null && mData instanceof UserActivityRbtDTO) {
            Object obj = ((UserActivityRbtDTO) mData).getUserActivityItemDTO().getData();
            if (obj instanceof RingBackToneDTO)
                return (RingBackToneDTO) obj;
        }
        return null;
    }
}
