package com.onmobile.rbt.baseline.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onmobile.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.baseline.http.api_action.dtos.PlayRuleDTO;
import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.dtos.UpdateUserDefinedShuffleResponseDTO;
import com.onmobile.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.musicplayback.BaselineMusicPlayer;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.OnActivityRbtItemClickListener;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.model.ActivityRbtType;
import com.onmobile.rbt.baseline.model.UserActivityRbtDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.FontUtils;
import com.onmobile.rbt.baseline.util.WidgetUtils;
import com.onmobile.rbt.baseline.widget.LabeledView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ActivityRbtAdapter extends RecyclerView.Adapter<RootViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<UserActivityRbtDTO> mList;
    private OnActivityRbtItemClickListener mListener;
    /*private ArrayList<UserActivityRbtShuffleDTO> mShuffleList;
    private ActivityRbtShuffleAdapter activityRbtShuffleAdapter;*/

    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean mLoading;

    private BaselineMusicPlayer mPlayer;
    private int mLastPlayedPosition = -1;

    public ActivityRbtAdapter(@NonNull List<UserActivityRbtDTO> list, OnActivityRbtItemClickListener listener) {
        this.mList = list;
        this.mListener = listener;
//        mShuffleList = getShuffleList();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType();
    }

    @NonNull
    @Override
    public RootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }

        if (viewType == ActivityRbtType.TYPE_RINGBACK || viewType == ActivityRbtType.TYPE_PROFILE_TUNES || viewType == ActivityRbtType.TYPE_NAME_TUNES || viewType == ActivityRbtType.TYPE_AZAN) {
            return new ActivityRbtAdapter.RingbackViewHolder(mInflater.inflate(R.layout.layout_activity_item_ringback, parent, false));
        } else if (viewType == ActivityRbtType.TYPE_TITLE_SMALL) {
            return new ActivityRbtAdapter.TitleSmallViewHolder(mInflater.inflate(R.layout.layout_activity_item_title_small, parent, false));
        } else if (viewType == ActivityRbtType.TYPE_TITLE_BIG) {
            return new ActivityRbtAdapter.TitleBigViewHolder(mInflater.inflate(R.layout.layout_activity_item_title_big, parent, false));
        } else if (viewType == ActivityRbtType.TYPE_PERSONALIZED_SHUFFLE) {
            return new ActivityRbtAdapter.PersonalizedShuffleHolder(mInflater.inflate(R.layout.layout_activity_item_personalized_big, parent, false));
        } else if (viewType == ActivityRbtType.TYPE_RECORDING) {
            return new ActivityRbtAdapter.RecordingViewHolder(mInflater.inflate(R.layout.layout_activity_item_recording, parent, false));
        } else if (viewType == ActivityRbtType.TYPE_VIDEO) {
            return new ActivityRbtAdapter.VideoViewHolder(mInflater.inflate(R.layout.layout_activity_item_video, parent, false));
        } else if (viewType == ActivityRbtType.TYPE_MUSIC_SHUFFLES) {
            return new ActivityRbtAdapter.ShuffleViewHolder(mInflater.inflate(R.layout.layout_activity_item_shuffle, parent, false));
        } else {
            return new ProgressViewHolder(mInflater.inflate(R.layout.layout_progress_loading_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RootViewHolder holder, final int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public void onBindViewHolder(@NonNull RootViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (holder instanceof PersonalizedShuffleHolder) {
            PersonalizedShuffleHolder personalizedShuffleHolder = (PersonalizedShuffleHolder) holder;
            if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                if (BaselineApplication.getApplication().getRbtConnector().isUserUDPEnabled()) {
                    personalizedShuffleHolder.mLabelView.enableSwitchStatusSilently();
                } else {
                    personalizedShuffleHolder.mLabelView.disableSwitchStatusSilently();
                }
                personalizedShuffleHolder.mLabelView.setVisibility(View.VISIBLE);
            } else {
                personalizedShuffleHolder.mLabelView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class TitleSmallViewHolder extends RootViewHolder<UserActivityRbtDTO> {

        TextView titleTextView;
        RelativeLayout parentLayout;

        TitleSmallViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            titleTextView = view.findViewById(R.id.title_small);
            parentLayout = view.findViewById(R.id.parent_layout);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
            if (data.isHistory()) {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
            } else {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_current);
            }
        }
    }

    public class TitleBigViewHolder extends RootViewHolder<UserActivityRbtDTO> {

        TextView titleTextView;

        TitleBigViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            titleTextView = view.findViewById(R.id.title_big);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
        }
    }

    public class PersonalizedShuffleHolder extends RootViewHolder<UserActivityRbtDTO> {

        LabeledView mLabelView;
        View rootView;

        PersonalizedShuffleHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            mLabelView = view.findViewById(R.id.labeled_personalized_shuffle);
            TextView textView = mLabelView.getLabelTextView();
            FontUtils.setBoldFont(mContext, textView);

            rootView = view;
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
            if (BaselineApplication.getApplication().getRbtConnector().isActiveUser()) {
                if (BaselineApplication.getApplication().getRbtConnector().isUserUDPEnabled()) {
                    mLabelView.enableSwitchStatusSilently();
                } else {
                    mLabelView.disableSwitchStatusSilently();
                }
                mLabelView.setVisibility(View.VISIBLE);
                rootView.setVisibility(View.VISIBLE);
            } else {
                mLabelView.setVisibility(View.GONE);
                rootView.setVisibility(View.GONE);
            }

            mLabelView.setListener(new LabeledView.OnLabeledListener() {
                @Override
                public void onClick(LabeledView view) {

                }

                @Override
                public void onSwitch(LabeledView view, boolean checked) {
                    if (checked) {
                        mLabelView.disableSwitchStatusSilently();
                    } else {
                        mLabelView.enableSwitchStatusSilently();
                    }
                    updatePersonalizedShuffle(checked);
                }
            });
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            //titleTextView.setText(data.getUserActivityItemDTO().getTitle());
        }

        private void updatePersonalizedShuffle(boolean status) {
            showProgress(true);
            BaselineApplication.getApplication().getRbtConnector().updateUSerDefinedShuffleStatus(AnalyticsConstants.EVENT_PV_PERSONALIZED_SHUFFLE_SOURCE_ACTIVITY, status, new AppBaselineCallback<UpdateUserDefinedShuffleResponseDTO>() {
                @Override
                public void success(UpdateUserDefinedShuffleResponseDTO result) {
                    showProgress(false);
                    ShowPersonalizedShuffleConfirmation(status);
                }

                @Override
                public void failure(String message) {
                    showProgress(false);
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            });
        }

        private ProgressDialog progressDialog;

        public void showProgress(boolean showProgress) {
            if (progressDialog == null) {
                progressDialog = AppDialog.getProgressDialog(mContext);
                progressDialog.setCancelable(false);
            }
            if (showProgress) {
                progressDialog.show();
            } else {
                progressDialog.dismiss();
            }
        }

        private void ShowPersonalizedShuffleConfirmation(boolean checked) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            String message;
            if (checked) {
                message = mContext.getString(R.string.personalized_shuffle_enabled_success_message);
            } else {
                message = mContext.getString(R.string.personalized_shuffle_disable_success_message);
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.personalized_shuffle_ok_button, (dialog, id) -> {
                        if (checked) {
                            mLabelView.enableSwitchStatusSilently();
                        } else {
                            mLabelView.disableSwitchStatusSilently();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private abstract class MainViewHolder<T> extends RootViewHolder<T> {

        CardView cardView;
        private AppCompatImageButton imgPlayPause;
        private ContentLoadingProgressBar playerProgress;
        TextView rbtStatus;

        private AppConstant.PlayerStatus playerStatus = AppConstant.PlayerStatus.stop;

        MainViewHolder(View view) {
            super(view);
            initViews(view);
        }

        @Override
        protected void initViews(View view) {
            try {
                cardView = view.findViewById(R.id.card_rbt_imageview);
                imgPlayPause = view.findViewById(R.id.ib_play_activity_rbt);
                playerProgress = view.findViewById(R.id.progress_play_activity_rbt);
                rbtStatus = view.findViewById(R.id.rbt_status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Set player callback for clicked holder
         */
        void hookPlayer() {
            player().setPreviewListener(new BaselineMusicPlayer.MusicPreviewListener() {
                @Override
                public void onPreviewPlaying(int progressStatus) {
                    if (progressStatus < AppConstant.MAX_PROGRESS_TO_UPDATE_PLAYER)
                        playingTrack();
                    if (player() != null && AppUtils.isRecommendationQueueDelayLapsed(player().getMediaPlayedInSeconds())) {
                        if (mList != null && mList.size() - 1 >= mLastPlayedPosition && mList.get(mLastPlayedPosition).getUserActivityItemDTO().getData() != null &&
                                mList.get(mLastPlayedPosition).getUserActivityItemDTO().getData() instanceof RingBackToneDTO) {
                            RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) mList.get(mLastPlayedPosition).getUserActivityItemDTO().getData();
                            if (ringBackToneDTO != null && ringBackToneDTO.isRingBackMusic())
                                BaselineApplication.getApplication().getRbtConnector().addRecommendationId(ringBackToneDTO.getId());
                        }
                    }
                }

                @Override
                public void onPreviewBuffering() {
                    bufferingTrack();
                }

                @Override
                public void onPreviewStopped() {
                    resetTrack();
                }

                @Override
                public void onPreviewCompleted() {
                    resetTrack();
                }

                @Override
                public void onPreviewError() {
                    resetTrack();
                }
            });
            resetTrack();
        }

        /**
         * * Reset player layout to stopped/playing/buffering for current holder according to status
         *
         * @param status Status to update UI
         */
        void resetTrack(AppConstant.PlayerStatus status) {
            switch (status) {
                case playing:
                    playingTrack();
                    break;
                case loading:
                    bufferingTrack();
                    break;
                default:
                    resetTrack();
                    break;
            }
        }

        /**
         * Reset player layout to stopped for current holder
         */
        void resetTrack() {
            playerStatus = AppConstant.PlayerStatus.stop;
            playerProgress.setVisibility(View.GONE);
            imgPlayPause.setVisibility(View.VISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_play_accent_10dp);
        }

        /**
         * Show playing layout for current holder
         */
        void playingTrack() {
            playerStatus = AppConstant.PlayerStatus.playing;
            playerProgress.setVisibility(View.GONE);
            imgPlayPause.setVisibility(View.VISIBLE);
            imgPlayPause.setImageResource(R.drawable.ic_pause_accent_10dp);
        }

        /**
         * Show buffer layout for current holder
         */
        void bufferingTrack() {
            playerStatus = AppConstant.PlayerStatus.loading;
            playerProgress.setVisibility(View.VISIBLE);
            imgPlayPause.setVisibility(View.GONE);
        }

        /**
         * Get an instance of audio player
         *
         * @return BaselineMusicPlayer
         */
        protected BaselineMusicPlayer player() {
            if (mPlayer == null) {
                mPlayer = BaselineMusicPlayer.getInstance();
            }
            return mPlayer;
        }

        /**
         * Toggle play/pause for audio player reference to BaselineMusicPlayer
         *
         * @param position position of clicked adapter
         * @param holder   holder of clicked adapter
         */
        void playToggle(final int position, MainViewHolder holder) {
            long delay = 0;
            if (player().isMediaPlaying()) {
                player().stopMusic();
                if (mLastPlayedPosition == position)
                    return;
                delay = 200;
            } else {
                try {
                    player().stopMusic();
                } catch (Exception ignored) {
                }
            }
            new Handler().postDelayed(() -> {
                mLastPlayedPosition = position;
                String playURL = null;
                Object obj = mList.get(position).getUserActivityItemDTO().getData();
                if (obj != null) {
                    if (obj instanceof RingBackToneDTO)
                        playURL = ((RingBackToneDTO) obj).getPreviewStreamUrl();
                    else if (obj instanceof ChartItemDTO) {
                        List<RingBackToneDTO> ll = ((ChartItemDTO) obj).getRingBackToneDTOS();
                        if (ll != null && ll.size() > 0)
                            playURL = ll.get(0).getPreviewStreamUrl();
                    }
                }
                if (!TextUtils.isEmpty(playURL)) {
                    holder.hookPlayer(); //Important
                    player().setMusicUrl(playURL);
                    player().startMusic(mContext);
                }
            }, delay);
        }

        void stopPlayer() {
            if (player().isMediaPlaying())
                player().stopMusic();
        }
    }

    public class RingbackViewHolder extends MainViewHolder<UserActivityRbtDTO> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView rbtImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        TextView callerTextView;
        TextView setDateTextView;
        RelativeLayout parentLayout;
        SwitchCompat switchCompat;
        TextView endDateTextView;
        AppCompatImageView optionMenu;

        RingbackViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            super.initViews(view);
            titleTextView = view.findViewById(R.id.title);
            subTitleTextView = view.findViewById(R.id.subtitle);
            callerTextView = view.findViewById(R.id.caller_textview);
            setDateTextView = view.findViewById(R.id.setdate_textview);
            parentLayout = view.findViewById(R.id.parent_layout);
            rbtImageView = view.findViewById(R.id.rbt_imageview);
            switchCompat = view.findViewById(R.id.switch_button);
            endDateTextView = view.findViewById(R.id.end_date_textview);
            optionMenu = view.findViewById(R.id.img_overflow);
            parentLayout.setOnClickListener(this);
            if (cardView != null)
                cardView.setOnClickListener(this);
            /*if (layoutPlayer != null)
                layoutPlayer.setOnClickListener(this)*/
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            String fitableImage = AppUtils.getFitableImage(mContext, data.getUserActivityItemDTO().getPreviewImgUrl(), imageSize);
            Glide.with(mContext)
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rbtImageView);

            if (data.isHistory()) {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
                if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getEndDate())) {
                    endDateTextView.setText(data.getUserActivityItemDTO().getEndDate());
                    endDateTextView.setVisibility(View.VISIBLE);
                } else {
                    endDateTextView.setVisibility(View.GONE);
                }
            } else {
                if(!AppConfigurationValues.isSelectionModel() && data.getUserActivityItemDTO().isDownloadedOnly()){
                    parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
                }else{
                    parentLayout.setBackgroundResource(R.drawable.activity_rbt_current);
                }

                endDateTextView.setVisibility(View.GONE);
            }

            if(data.getType() == ActivityRbtType.TYPE_PROFILE_TUNES ||
                    data.getType() == ActivityRbtType.TYPE_AZAN){
                optionMenu.setVisibility(View.INVISIBLE);
            }
            else{
                optionMenu.setVisibility(View.VISIBLE);
            }

            if(!AppConfigurationValues.isSelectionModel()){
                switchCompat.setChecked(!data.isHistory()
                        && !data.getUserActivityItemDTO().isDownloadedOnly());
            }
            else{
                switchCompat.setChecked(!data.isHistory());
            }
            switchCompat.setOnCheckedChangeListener(this);

            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
            subTitleTextView.setText(data.getUserActivityItemDTO().getSubTitle());

            if(!AppConfigurationValues.isSelectionModel() && data.getUserActivityItemDTO().isDownloadedOnly()) {
                callerTextView.setText(R.string.download_model_not_playing_anyone);
            }
            else{
                callerTextView.setText(String.format(mContext.getString(!data.isHistory() ? R.string.activity_caller_text_active : R.string.activity_caller_text_history), TextUtils.isEmpty(data.getUserActivityItemDTO().getPlayingFor()) ?
                        mContext.getString(R.string.all_callers) : data.getUserActivityItemDTO().getPlayingFor()));
            }

            if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getSetDate())) {
                setDateTextView.setText(String.format(mContext.getString(R.string.activity_setdate_text), data.getUserActivityItemDTO().getSetDate()));
                setDateTextView.setVisibility(View.VISIBLE);
            } else {
                setDateTextView.setVisibility(View.INVISIBLE);
            }

            updateRbtStatusUI(data, switchCompat, rbtStatus);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.card_rbt_imageview)
                playToggle(getAdapterPosition(), this);
            else if (getAdapterPosition() >= 0) {
                stopPlayer();
                if (mListener != null)
                    mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (getAdapterPosition() >= 0) {
                stopPlayer();
                if (mListener != null)
                    mListener.onItemClick(switchCompat, mList.get(getAdapterPosition()), getAdapterPosition());
            }
        }
    }

    private void updateRbtStatusUI(UserActivityRbtDTO data, SwitchCompat switcher, TextView rbtStatus) {
        Object obj = data.getUserActivityItemDTO().getData();
        if (obj != null) {
            String playRuleId = null;
            if (obj instanceof RingBackToneDTO) {
                playRuleId = ((RingBackToneDTO) obj).getId();
            } else if (obj instanceof ChartItemDTO) {
                playRuleId = String.valueOf(((ChartItemDTO) obj).getId());
            }
            if (!TextUtils.isEmpty(playRuleId)) {
                List<PlayRuleDTO> playRuleDTOList = BaselineApplication.getApplication().getRbtConnector().getPlayRuleById(playRuleId);
                if (playRuleDTOList != null && playRuleDTOList.size() > 0) {
                    for (PlayRuleDTO playRuleDTO :
                            playRuleDTOList) {
                        if (playRuleDTO != null && !TextUtils.isEmpty(playRuleDTO.getStatus())
                                && playRuleDTO.getStatus().
                                equalsIgnoreCase(APIRequestParameters.UserType.ACTIVE.getStatusSecondary())) {
                            rbtStatus.setText(mContext.getString(R.string.label_activation_pending));
                            rbtStatus.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                            rbtStatus.setVisibility(View.VISIBLE);
                            switcher.setEnabled(false);
                            return;
                        }
                    }
                }
            }
        }
        rbtStatus.setVisibility(View.GONE);
        switcher.setEnabled(true);
    }

    public class RecordingViewHolder extends RootViewHolder<UserActivityRbtDTO> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView rbtImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        TextView callerTextView;
        TextView setDateTextView;
        RelativeLayout parentLayout;
        SwitchCompat switchCompat;
        TextView endDateTextView;

        RecordingViewHolder(View view) {
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
            switchCompat = view.findViewById(R.id.switch_button);
            endDateTextView = view.findViewById(R.id.end_date_textview);
            parentLayout.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            String fitableImage = AppUtils.getFitableImage(mContext, data.getUserActivityItemDTO().getPreviewImgUrl(), imageSize);
            Glide.with(mContext)
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rbtImageView);

            if (data.isHistory()) {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
                if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getEndDate())) {
                    endDateTextView.setText(data.getUserActivityItemDTO().getEndDate());
                    endDateTextView.setVisibility(View.VISIBLE);
                } else {
                    endDateTextView.setVisibility(View.GONE);
                }
            } else {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_current);
                endDateTextView.setVisibility(View.GONE);
            }
            switchCompat.setChecked(!data.isHistory());
            switchCompat.setOnCheckedChangeListener(this);

            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
            subTitleTextView.setText(data.getUserActivityItemDTO().getSubTitle());
            callerTextView.setText(String.format(mContext.getString(!data.isHistory() ? R.string.activity_caller_text_active : R.string.activity_caller_text_history), TextUtils.isEmpty(data.getUserActivityItemDTO().getPlayingFor()) ?
                    mContext.getString(R.string.all_callers) : data.getUserActivityItemDTO().getPlayingFor()));

            if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getSetDate())) {
                setDateTextView.setText(String.format(mContext.getString(R.string.activity_setdate_text), data.getUserActivityItemDTO().getSetDate()));
                setDateTextView.setVisibility(View.VISIBLE);
            } else {
                setDateTextView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            if (mListener != null && getAdapterPosition() >= 0)
                mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mListener != null && getAdapterPosition() >= 0)
                mListener.onItemClick(switchCompat, mList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    public class VideoViewHolder extends RootViewHolder<UserActivityRbtDTO> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView rbtImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        TextView callerTextView;
        TextView setDateTextView;
        RelativeLayout parentLayout;
        SwitchCompat switchCompat;
        TextView endDateTextView;

        VideoViewHolder(View view) {
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
            switchCompat = view.findViewById(R.id.switch_button);
            endDateTextView = view.findViewById(R.id.end_date_textview);
            parentLayout.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, int position) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            String fitableImage = AppUtils.getFitableImage(mContext, data.getUserActivityItemDTO().getPreviewImgUrl(), imageSize);
            Glide.with(mContext)
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(rbtImageView);

            if (data.isHistory()) {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
                if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getEndDate())) {
                    endDateTextView.setText(data.getUserActivityItemDTO().getEndDate());
                    endDateTextView.setVisibility(View.VISIBLE);
                } else {
                    endDateTextView.setVisibility(View.GONE);
                }
            } else {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_current);
                endDateTextView.setVisibility(View.GONE);
            }
            switchCompat.setChecked(!data.isHistory());
            switchCompat.setOnCheckedChangeListener(this);

            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
            subTitleTextView.setText(data.getUserActivityItemDTO().getSubTitle());
            callerTextView.setText(String.format(mContext.getString(!data.isHistory() ? R.string.activity_caller_text_active : R.string.activity_caller_text_history), TextUtils.isEmpty(data.getUserActivityItemDTO().getPlayingFor()) ?
                    mContext.getString(R.string.all_callers) : data.getUserActivityItemDTO().getPlayingFor()));

            if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getSetDate())) {
                setDateTextView.setText(String.format(mContext.getString(R.string.activity_setdate_text), data.getUserActivityItemDTO().getSetDate()));
                setDateTextView.setVisibility(View.VISIBLE);
            } else {
                setDateTextView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View view) {
            if (mListener != null && getAdapterPosition() >= 0)
                mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mListener != null && getAdapterPosition() >= 0)
                mListener.onItemClick(switchCompat, mList.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    int mExpandPosition = -1;

    public class ShuffleViewHolder extends RootViewHolder<UserActivityRbtDTO> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        ImageView rbtDiskImageView;
        ImageView rbtImageView;
        TextView titleTextView;
        TextView subTitleTextView;
        TextView callerTextView;
        TextView setDateTextView;
        RelativeLayout parentLayout;
        SwitchCompat switchCompat;
        TextView endDateTextView;
        //        RelativeLayout expandLayout;
        /*AppCompatImageView expandButton;
        RecyclerView shuffleRecyclerView;*/
        //private FrameLayout shuffleFrameLayout;
        TextView rbtStatus;

        ShuffleViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            titleTextView = view.findViewById(R.id.title);
            subTitleTextView = view.findViewById(R.id.subtitle);
            callerTextView = view.findViewById(R.id.caller_textview);
            setDateTextView = view.findViewById(R.id.setdate_textview);
            parentLayout = view.findViewById(R.id.parent_layout);
            rbtImageView = view.findViewById(R.id.activity_rbt_shuffle_image);
            rbtDiskImageView = view.findViewById(R.id.activity_rbt_shuffle_disc_image);
            switchCompat = view.findViewById(R.id.switch_button);
            endDateTextView = view.findViewById(R.id.end_date_textview);
//            expandLayout = (RelativeLayout) view.findViewById(R.id.expand_layout);
//            expandButton = (AppCompatImageView) view.findViewById(R.id.expand_button);
//            shuffleRecyclerView = (RecyclerView) view.findViewById(R.id.activity_rbt_shuffle_recycler_view);
            //shuffleFrameLayout = view.findViewById(R.id.layout_shuffle_expansion);
            rbtStatus = view.findViewById(R.id.rbt_status);
            parentLayout.setOnClickListener(this);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(@NonNull UserActivityRbtDTO data, final int position) {
            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
            String fitableImage = AppUtils.getFitableImage(mContext, data.getUserActivityItemDTO().getPreviewImgUrl(), imageSize);
            Glide.with(mContext).load(fitableImage).asBitmap().centerCrop().placeholder(R.drawable.default_album_art).error(R.drawable.default_album_art).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(rbtImageView) {
                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                    super.onResourceReady(resource, glideAnimation);
                    WidgetUtils.setCircularImage(rbtImageView, rbtDiskImageView, resource, 0);
                }
            });

            if (data.isHistory()) {
                parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
                if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getEndDate())) {
                    endDateTextView.setText(data.getUserActivityItemDTO().getEndDate());
                    endDateTextView.setVisibility(View.VISIBLE);
                } else {
                    endDateTextView.setVisibility(View.GONE);
                }
            } else {
                if(!AppConfigurationValues.isSelectionModel() && data.getUserActivityItemDTO().isDownloadedOnly()) {
                    parentLayout.setBackgroundResource(R.drawable.activity_rbt_History);
                }
                else{
                    parentLayout.setBackgroundResource(R.drawable.activity_rbt_current);
                }
                endDateTextView.setVisibility(View.GONE);
            }
            if(!AppConfigurationValues.isSelectionModel()){
                switchCompat.setChecked(!data.isHistory() && !data.getUserActivityItemDTO().isDownloadedOnly());
            }
            else{
                switchCompat.setChecked(!data.isHistory());
            }

            switchCompat.setOnCheckedChangeListener(this);

            titleTextView.setText(data.getUserActivityItemDTO().getTitle());
            subTitleTextView.setText(data.getUserActivityItemDTO().getSubTitle());
            if(!AppConfigurationValues.isSelectionModel() && data.getUserActivityItemDTO().isDownloadedOnly()){
                callerTextView.setText(R.string.download_model_not_playing_anyone);
            }
            else{
                callerTextView.setText(String.format(mContext.getString(!data.isHistory() ? R.string.activity_caller_text_active : R.string.activity_caller_text_history), TextUtils.isEmpty(data.getUserActivityItemDTO().getPlayingFor()) ?
                        mContext.getString(R.string.all_callers) : data.getUserActivityItemDTO().getPlayingFor()));
            }

            if (!TextUtils.isEmpty(data.getUserActivityItemDTO().getSetDate())) {
                setDateTextView.setText(String.format(mContext.getString(R.string.activity_setdate_text), data.getUserActivityItemDTO().getSetDate()));
                setDateTextView.setVisibility(View.VISIBLE);
            } else {
                setDateTextView.setVisibility(View.INVISIBLE);
            }

            /*final boolean isExpanded = position == mExpandPosition;
            if (isExpanded) {
                expandButton.setImageResource(R.drawable.ic_arrow_up_activity_rbt_shuffle);
            } else {
                expandButton.setImageResource(R.drawable.ic_arrow_down_activity_rbt_shuffle);
            }*/

//            expandLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

            /*parentLayout.setOnClickListener(view -> {
                if (mListener != null) {
                    mListener.onItemClick(view, mList.get(position), position);
                    int oldPosition = mExpandPosition;
                    mExpandPosition = isExpanded ? -1 : position;
                    notifyItemChanged(oldPosition);
                    expandButton.animate().rotation(180);
                    notifyItemChanged(position);
                }
            });*/
            /*if (activityRbtShuffleAdapter == null) {
                activityRbtShuffleAdapter = new ActivityRbtShuffleAdapter(mShuffleList, null);
            }*/

            /*FragmentHorizontalMusic fragment = FragmentHorizontalMusic.newInstance(FunkyAnnotation.HORIZONTAL_MUSIC_CONTENT_TYPE_SHUFFLE, data.getUserActivityItemDTO().getData(), true);

            // Delete old fragment
            int containerId = shuffleFrameLayout.getId();// Get container id
            Fragment oldFragment = mFragmentManager.findFragmentById(containerId);
            if (oldFragment != null) {
                mFragmentManager.beginTransaction().remove(oldFragment).commitAllowingStateLoss();
            }

            int newContainerId = (position + 1) + (int) (Math.random() * AppConstant.SMALL_NUMBER_TO_GENERATE_RANDOM_ID);
            shuffleFrameLayout.setId(newContainerId);// Set container id

            // Add new fragment
            mFragmentManager.beginTransaction().replace(newContainerId, fragment).commitAllowingStateLoss();*/

            /*LinearLayoutManager llm = new LinearLayoutManager(mContext);
            shuffleRecyclerView.setLayoutManager(llm);
            shuffleRecyclerView.setAdapter(activityRbtShuffleAdapter);*/

            updateRbtStatusUI(data, switchCompat, rbtStatus);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null && getAdapterPosition() >= 0)
                mListener.onItemClick(view, mList.get(getAdapterPosition()), getAdapterPosition());
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mListener != null && getAdapterPosition() >= 0)
                mListener.onItemClick(switchCompat, mList.get(getAdapterPosition()), getAdapterPosition());
        }

    }

    public class ProgressViewHolder extends RootViewHolder implements View.OnClickListener {

        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {

        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(Object data, int position) {

        }

        @Override
        public void onClick(View view) {

        }
    }

    /*public ArrayList<UserActivityRbtShuffleDTO> getShuffleList() {
        mShuffleList = new ArrayList<>();
        mShuffleList.add(new UserActivityRbtShuffleDTO(R.drawable.image6, "Adele", "Set fire to the rain"));
        mShuffleList.add(new UserActivityRbtShuffleDTO(R.drawable.image7, "Justin Bieber", "As long as you love me"));
        mShuffleList.add(new UserActivityRbtShuffleDTO(R.drawable.image8, "Jennifir Lopez", "Playing with fire"));
        mShuffleList.add(new UserActivityRbtShuffleDTO(R.drawable.image9, "Weekend", "Cann't feel my face"));
        return mShuffleList;
    }*/

    public void setOnLoadMoreListener(@NonNull RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(mScrollListener);
    }

    public void setLoaded() {
        mLoading = false;
    }

    public boolean isLoading() {
        return mLoading;
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView,
                               int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && mOnLoadMoreListener != null) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                if (manager != null) {
                    int totalItemCount = manager.getItemCount();
                    int lastVisibleItem = 0;
                    boolean capable = false;
                    if (manager instanceof GridLayoutManager) {
                        GridLayoutManager layoutManager = (GridLayoutManager) manager;
                        lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                        capable = true;
                    } else if (manager instanceof LinearLayoutManager) {
                        LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                        lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                        capable = true;
                    }
                    if (capable && !mLoading && totalItemCount <= lastVisibleItem + AppConstant.LOAD_MORE_VISIBLE_LIST_THRESHOLD) {
                        mOnLoadMoreListener.onLoadMore();
                        mLoading = true;
                    }
                }
            }
        }
    };

}