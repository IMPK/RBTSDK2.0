package com.onmobile.rbt.baseline.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UserDefinedPlaylistDTO;
import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.AddRbt2UdpBSChildFragment;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.dialog.listeners.SelectionDialogListener;
import com.onmobile.rbt.baseline.event.RBTStatus;
import com.onmobile.rbt.baseline.fragment.FragmentPreBuyAudio;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
import com.onmobile.rbt.baseline.listener.LifeCycleCallback;
import com.onmobile.rbt.baseline.listener.OnBottomSheetChangeListener;
import com.onmobile.rbt.baseline.listener.OnItemClickListener;
import com.onmobile.rbt.baseline.listener.OnLoadMoreListener;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 19/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class StoreChildItemRecyclerAdapter extends RecyclerView.Adapter<RootViewHolder> implements LifeCycleCallback {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROGRESS = 0;

    private Context mContext;
    private LayoutInflater mInflater;
    private Resources mResources;

    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;

    private FragmentManager mFragmentManager;
    private List<RingBackToneDTO> mList;
    private OnItemClickListener<RingBackToneDTO> mListener;

    private ProgressDialog mProgressDialog;
    private boolean isRecommendation = false;

    private int mCardMaxHeightWidth, mCardColumnCount = 0;
    private boolean mLoginStatus;
    private String type;

    public StoreChildItemRecyclerAdapter(FragmentManager fragmentManager, @NonNull List<RingBackToneDTO> list, OnItemClickListener<RingBackToneDTO> listener) {
        this.mFragmentManager = fragmentManager;
        this.mList = list;
        this.mListener = listener;
    }

    public StoreChildItemRecyclerAdapter(FragmentManager fragmentManager, @NonNull List<RingBackToneDTO> list, int cardMaxHeightWidth, int cardColumnCount, OnItemClickListener<RingBackToneDTO> listener) {
        this.mFragmentManager = fragmentManager;
        this.mList = list;
        this.mCardMaxHeightWidth = cardMaxHeightWidth;
        this.mCardColumnCount = cardColumnCount;
        this.mListener = listener;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRecommendation(boolean recommendation) {
        isRecommendation = recommendation;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    @NonNull
    @Override
    public RootViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mResources = mContext.getResources();
            mInflater = LayoutInflater.from(parent.getContext());
        }
        if (viewType == VIEW_ITEM)
            return new ViewHolder(mInflater.inflate(R.layout.layout_store_child_item, parent, false));
        else
            return new ProgressViewHolder(mInflater.inflate(R.layout.layout_store_child_loading_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final RootViewHolder holder, final int position) {
        holder.bind(mList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RootViewHolder<RingBackToneDTO> implements View.OnClickListener {

        private LinearLayout layoutRoot;
        private CardView card;
        private AppCompatImageView imgPreview;
        private AppCompatTextView artist, track;
        private AppCompatImageView imgRbtSeleted;
        private AppCompatTextView setTune;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            layoutRoot = view.findViewById(R.id.layout_store_item_child);
            card = view.findViewById(R.id.card_store_item_child);
            imgPreview = view.findViewById(R.id.iv_preview_store_item_child);
            artist = view.findViewById(R.id.tv_artist_store_item_child);
            track = view.findViewById(R.id.tv_track_store_item_child);
            imgRbtSeleted = view.findViewById(R.id.iv_rbt_selected_store_item_child);
            setTune = view.findViewById(R.id.tv_set_store_item_child);

        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            layoutRoot.setOnClickListener(this);
            card.setOnClickListener(this);
            setTune.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull RingBackToneDTO data, int position) {

            setMargin(position, layoutRoot, card);

            int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
            String fitableImage = AppUtils.getFitableImage(mContext, data.getPrimaryImage(), imageSize);

            Glide.with(mContext)
                    .load(fitableImage)
                    .placeholder(R.drawable.default_album_art)
                    .error(R.drawable.default_album_art)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgPreview);

            String title = data.getPrimaryArtistName();
            if(type != null && type.equals(ContentSearchAdapter.TYPE_ALBUM)
                    && !TextUtils.isEmpty(data.getAlbumName())){
                title = data.getAlbumName();
            }

            if (TextUtils.isEmpty(title) && !TextUtils.isEmpty(data.getType())
                    && data.getType().equals(APIRequestParameters.EMode.CHART.value()))
                title = data.getName();
            artist.setText(title);
            track.setText(data.getTrackName());

            if (!isRecommendation) {
                setTune.setVisibility(View.VISIBLE);
            } else {
                /*if (AppConfigurationValues.isUDPEnabled()) {
                    setTune.setVisibility(APIRequestParameters.EMode.RINGBACK.value().equals(data.getType()) ? View.VISIBLE : View.GONE);
                } else {
                    setTune.setVisibility(View.GONE);
                }*/
                setTune.setVisibility(View.GONE);
            }
            updateRBTStatus(AppManager.getInstance().getRbtConnector().isRingbackSelected(data.getId()));
        }

        private void updateRBTStatus(boolean status) {
            imgRbtSeleted.setVisibility(status ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == layoutRoot.getId() || id == card.getId()) {
                if (mListener != null && getAdapterPosition() >= 0)
                    mListener.onItemClick(imgPreview, mList.get(getAdapterPosition()), getAdapterPosition(),
                            new Pair<>(view, FragmentPreBuyAudio.IMAGE_TRANSITION_NAME));
            } else if (id == setTune.getId() && getAdapterPosition() >= 0) {
                //addToUdp(mList.get(getAdapterPosition()), null);
                mLoginStatus = SharedPrefProvider.getInstance(mContext).isLoggedIn();
                WidgetUtils.getSetCallerTuneBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_TRENDING_CARD, mList.get(getAdapterPosition())).setCallback(new OnBottomSheetChangeListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                    }

                    @Override
                    public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                        handleBottomSheetResult(success, message);
                    }

                    @Override
                    public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                        handleBottomSheetResult(success, message);
                    }
                }).showSheet(mFragmentManager);
            }
        }
    }

    private void addToUdp(final RingBackToneDTO ringBackToneDTO, String autoAddUdpId) {
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
        }).showSheet(mFragmentManager);
    }

    private void handleAddToShuffleBSResponse(RingBackToneDTO itemToAdd, boolean success, String message, Object result) {
        if (!success && result != null && result instanceof String) {
            String resp = (String) result;
            if (AddRbt2UdpBSChildFragment.KEY_CREATE_UDP.equals(resp))
                createUserDefinedPlayList(itemToAdd, null, mContext.getString(R.string.create_udp_description));
        }
    }

    public class ProgressViewHolder extends RootViewHolder implements View.OnClickListener {

        private LinearLayout layoutRoot;
        private CardView card;

        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            layoutRoot = view.findViewById(R.id.layout_store_item_child);
            card = view.findViewById(R.id.card_store_item_child);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {

        }

        @Override
        public void bind(Object data, int position) {
            setMargin(position, layoutRoot, card);
        }

        @Override
        public void onClick(View view) {

        }
    }

    private void setMargin(int position, ViewGroup root, CardView card) {
        if (!isRecommendation) {
            int margin = (int) mResources.getDimension(R.dimen.track_child_item_margin);
            //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            GridLayoutManager.LayoutParams rootParams = (GridLayoutManager.LayoutParams) root.getLayoutParams();
            LinearLayout.LayoutParams cardParams = (LinearLayout.LayoutParams) card.getLayoutParams();
            if (mCardMaxHeightWidth > 0 && mCardColumnCount > 0) {
                int elevation = (int) mResources.getDimension(R.dimen.track_card_elevation);
                int headerMargin = (int) mResources.getDimension(R.dimen.track_child_margin);
                //rootParams.height = rootParams.width = mCardMaxHeightWidth;
                rootParams.setMargins(position < mCardColumnCount ? headerMargin - 2 * elevation : 0, 0, margin, margin);
                cardParams.height = rootParams.width = mCardMaxHeightWidth - margin;
            } else {
                /*if (mIsUnderViewPager) {
                    if (position == 0 || position == 2) {
                        rootParams.setMargins(margin, 0, (margin / 2), margin);
                    } else {
                        rootParams.setMargins((margin / 2), 0, margin, margin);
                    }
                } else {*/
                if (position % 2 == 0) {
                    rootParams.setMargins(margin, 0, (margin / 2), margin);
                } else {
                    rootParams.setMargins((margin / 2), 0, margin, margin);
                }
            }
            root.setLayoutParams(rootParams);
            card.setLayoutParams(cardParams);
        }
    }

    public void setOnLoadMoreListener(@NonNull RecyclerView recyclerView, @NonNull OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
        recyclerView.addOnScrollListener(mScrollListener);
    }

    public void setLoaded() {
        mLoading = false;
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

    private void createUserDefinedPlayList(final RingBackToneDTO itemToAdd, String input, String description) {
        if (mProgressDialog == null) {
            mProgressDialog = AppDialog.getProgressDialog(mContext, mContext.getString(R.string.creating_shuffle_message));
            mProgressDialog.setCancelable(false);
        }
        AppDialog.getSingleInputDialog(mContext,
                mContext.getString(R.string.title_create_udp), mContext.getString(R.string.ok), mContext.getString(R.string.cancel),
                true, true, input, description, new SelectionDialogListener<String>() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id, final String data) {
                        dialog.dismiss();
                        mProgressDialog.show();
                        AppManager.getInstance().getRbtConnector().createUserDefinedPlaylist(data, new AppBaselineCallback<UserDefinedPlaylistDTO>() {
                            @Override
                            public void success(UserDefinedPlaylistDTO result) {
                                mProgressDialog.dismiss();
                                //((BaseActivity) mContext).showShortToast(mContext.getString(R.string.shuffle_create_success_msg, data));
                                addToUdp(itemToAdd, result.getId());
                            }

                            @Override
                            public void failure(String errMsg) {
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

    /*@Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RBTStatus event) {
        if (event != null && mList != null) {
            int counter = 0;
            for (RingBackToneDTO item : mList) {
                if (item.getId().equals(event.getId())) {
                    mList.set()
                    break;
                }
                counter++;
            }
        }
    }*/

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RBTStatus event) {
        if (event != null && mList != null) {
            int position = 0;
            for (RingBackToneDTO item : mList) {
                if (event.getIds().contains(item.getId()))
                    notifyItemChanged(position);
                position++;
            }
        }
    }

    @Override
    public void onLifeCycleStart() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void onLifeCycleStop() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success) {
            new AppRatingPopup(mContext, () -> {
                if (!mLoginStatus && SharedPrefProvider.getInstance(mContext).isLoggedIn())
                    ((BaseActivity) mContext).redirect(HomeActivity.class, null, true, true);
            }).show();
        }
    }

}
