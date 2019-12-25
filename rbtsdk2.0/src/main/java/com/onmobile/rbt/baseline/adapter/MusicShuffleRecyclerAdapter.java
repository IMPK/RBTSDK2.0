package com.onmobile.rbt.baseline.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.onmobile.rbt.baseline.AppManager;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.udp.UdpAssetDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.StoreActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.base.SimpleRecyclerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.basecallback.AppBaselineCallback;
import com.onmobile.rbt.baseline.bottomsheet.SetShuffleMainBSFragment;
import com.onmobile.rbt.baseline.dialog.AppDialog;
import com.onmobile.rbt.baseline.dialog.custom.AppRatingPopup;
import com.onmobile.rbt.baseline.dialog.listeners.DialogListener;
import com.onmobile.rbt.baseline.event.ContentCountChangeEvent;
import com.onmobile.rbt.baseline.holder.RootViewHolder;
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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 11/10/2018.
 *
 * @author Shahbaz Akhtar
 */
public class MusicShuffleRecyclerAdapter extends SimpleRecyclerAdapter<RootViewHolder, Object> {

    private final int VIEW_ITEM = 1;

    private boolean mLoading;
    private OnLoadMoreListener mOnLoadMoreListener;
    private AdapterInternalCallback mAdapterInternalCallback;

    private FragmentManager mFragmentManager;
    private ProgressDialog mProgressDialog;

    public MusicShuffleRecyclerAdapter(FragmentManager fragmentManager, @NonNull List<Object> list, AdapterInternalCallback callback, OnItemClickListener<Object> listener) {
        super(list, listener);
        this.mFragmentManager = fragmentManager;
        this.mAdapterInternalCallback = callback;
    }

    @Override
    protected RootViewHolder onSimpleCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM)
            return new ViewHolder(getInflater().inflate(R.layout.layout_shuffle_item, parent, false));
        else
            return new ProgressViewHolder(getInflater().inflate(R.layout.layout_progress_loading_item, parent, false));
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RootViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    protected void onSimpleBindViewHolder(@NonNull RootViewHolder holder, int position) {
        holder.bind(getList().get(position), position);
    }

    @Override
    public int getItemViewType(int position) {
        return getList().get(position) != null ? VIEW_ITEM : 0;
    }

    public class ViewHolder extends RootViewHolder<Object> implements View.OnClickListener {

        private LinearLayout layoutRoot;
        private AppCompatImageView ivCardPreview, ivDiscPreview;
        private AppCompatTextView tvMoodTitle, tvTrackTitle, tvSet;
        private AppCompatImageButton ibOption;
        private AppCompatImageView imgRbtSeleted;

        ViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            layoutRoot = view.findViewById(R.id.layout_music_shuffle_item_child);
            ivCardPreview = view.findViewById(R.id.iv_preview_music_shuffle_item_child);
            ivDiscPreview = view.findViewById(R.id.iv_preview_disc_music_shuffle_item_child);
            tvMoodTitle = view.findViewById(R.id.tv_mood_title_music_shuffle_item_child);
            tvTrackTitle = view.findViewById(R.id.tv_track_title_music_shuffle_item_child);
            tvSet = view.findViewById(R.id.tv_set_music_shuffle_item_child);
            ibOption = view.findViewById(R.id.ib_option_shuffle_item_child);
            imgRbtSeleted = view.findViewById(R.id.iv_rbt_selected_music_shuffle_item_child);
        }

        @Override
        protected void initComponents() {

        }

        @Override
        protected void bindViews() {
            //layoutRoot.setOnClickListener(this);
        }

        @Override
        public void bind(@NonNull Object data, int position) {
            if (data instanceof RingBackToneDTO) {
                RingBackToneDTO ringBackToneDTO = (RingBackToneDTO) data;
                int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SMALL_SIZE);
                final String imageUrl = AppUtils.getFitableImage(getContext(), ringBackToneDTO.getPrimaryImage(), imageSize);
                Glide.with(getContext()).load(imageUrl).asBitmap().centerCrop().placeholder(R.drawable.default_album_art).error(R.drawable.default_album_art).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(ivCardPreview) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        WidgetUtils.setCircularImage(ivCardPreview, ivDiscPreview, resource, 0);
                    }
                });
                tvMoodTitle.setText(getCaption(ringBackToneDTO.getCaption(), ringBackToneDTO.getItems() != null ? ringBackToneDTO.getItems().size() : 0));
                tvTrackTitle.setText(ringBackToneDTO.getAlbumName());
                updateCommonFields(true, ringBackToneDTO.getId());
            } else if (data instanceof UdpAssetDTO) {
                UdpAssetDTO udpAssetDTO = (UdpAssetDTO) data;
                int imageSize = AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_SIZE);
                final String imageUrl = AppUtils.getFitableImage(getContext(), udpAssetDTO.getExtra_info(), imageSize);
                Glide.with(getContext()).load(imageUrl).asBitmap().centerCrop().placeholder(R.drawable.default_album_art).error(R.drawable.default_album_art).diskCacheStrategy(DiskCacheStrategy.ALL).into(new BitmapImageViewTarget(ivCardPreview) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        WidgetUtils.setCircularImage(ivCardPreview, ivDiscPreview, resource, 0);
                    }
                });
                tvMoodTitle.setText(getCaption(udpAssetDTO.getName(), udpAssetDTO.getCount()));
                tvTrackTitle.setText(udpAssetDTO.getName());
                updateCommonFields(false, udpAssetDTO.getId());
            }
        }

        @Override
        public void onClick(View view) {
            if (getAdapterPosition() < 0)
                return;
            if (getListener() != null) {
                getListener().onItemClick(view, getList().get(getAdapterPosition()), getAdapterPosition());
            } else {
                int id = view.getId();
                Object data = getList().get(getAdapterPosition());
                if (data == null)
                    return;
                if (id == R.id.tv_set_music_shuffle_item_child || id == R.id.layout_music_shuffle_item_child) {
                    RingBackToneDTO ringBackToneDTO;
                    SetShuffleMainBSFragment shuffleMainBSFragment = null;
                    if (data instanceof RingBackToneDTO) {
                        ringBackToneDTO = (RingBackToneDTO) data;
                        if (ringBackToneDTO.getItems() != null && ringBackToneDTO.getItems().size() > 0)
                            shuffleMainBSFragment = WidgetUtils.getSetShuffleBottomSheet(AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_SHUFFLE_CARD, ringBackToneDTO);
                    } else if (data instanceof UdpAssetDTO) {
                        UdpAssetDTO udpAssetDTO = (UdpAssetDTO) data;
                        if (Integer.parseInt(udpAssetDTO.getCount()) > 0) {
                            shuffleMainBSFragment = WidgetUtils.getUdpShuffleBottomSheet(udpAssetDTO.convert(), false);
                        } else {
                            BaseActivity rootActivity = ((BaseActivity) getContext());
                            rootActivity.showLongSnackBar(getContext().getString(R.string.error_msg_set_shuffle_count), getContext().getString(R.string.shuffle_add_label), v -> rootActivity.redirect(StoreActivity.class, null, false, false));
                            return;
                        }
                    }
                    if (shuffleMainBSFragment != null) {
                        shuffleMainBSFragment.setCallback(new OnBottomSheetChangeListener() {
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
                    } else {
                        ((BaseActivity) getContext()).showShortSnackBar(getContext().getString(R.string.something_went_wrong));
                    }
                } else if (id == R.id.ib_option_shuffle_item_child) {
                    if (data instanceof UdpAssetDTO) {
                        UdpAssetDTO udpAssetDTO = (UdpAssetDTO) data;
                        boolean editRequired = !AppManager.getInstance().getRbtConnector().isRingbackSelected(udpAssetDTO.getId())
                                && (Integer.parseInt(udpAssetDTO.getCount()) > 0);
                        AppDialog.showUserShuffleContextOptionMenu(view, editRequired, false, item -> {
                            if(item.getItemId() == R.id.menu_udp_delete) {
                                deleteUdp(udpAssetDTO);
                                return true;
                            }else if(item.getItemId() == R.id.menu_udp_edit) {
                                editUdp(udpAssetDTO);
                                return true;
                            }else{
                                return false;
                            }
                        });
                    }
                }
            }
        }

        private void updateCommonFields(boolean system, String id) {
            if (TextUtils.isEmpty(id))
                return;
            boolean rbtStatus = AppManager.getInstance().getRbtConnector().isRingbackSelected(id);
            ibOption.setVisibility(system ? View.GONE : (!rbtStatus ? View.VISIBLE : View.GONE));
            imgRbtSeleted.setVisibility(rbtStatus ? View.VISIBLE : View.GONE);
            if (!system && rbtStatus) {
                layoutRoot.setOnClickListener(null);
                tvSet.setOnClickListener(null);
                ibOption.setOnClickListener(null);
                tvSet.setEnabled(false);
            } else {
                layoutRoot.setOnClickListener(this);
                tvSet.setOnClickListener(this);
                ibOption.setOnClickListener(this);
                tvSet.setEnabled(true);
            }
        }
    }

    public class ProgressViewHolder extends RootViewHolder implements View.OnClickListener {

        //private LinearLayout layoutRoot;

        ProgressViewHolder(View view) {
            super(view);
        }

        @Override
        protected void initViews(View view) {
            //layoutRoot = view.findViewById(R.id.layout_store_item_child);
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

    private void deleteUdp(UdpAssetDTO udpAssetDTO) {
        Context context = getContext();
        AppDialog.getAlertDialogWithNoDismiss(context, context.getString(R.string.title_delete_udp), false,
                context.getString(R.string.delete_udp_description), false,
                context.getString(R.string.yes), context.getString(R.string.no), true, true, new DialogListener() {
                    @Override
                    public void PositiveButton(DialogInterface dialog, int id) {
                        if (mProgressDialog == null) {
                            mProgressDialog = AppDialog.getProgressDialog(context, context.getString(R.string.deleting_shuffle_message));
                            mProgressDialog.setCancelable(false);
                        }
                        mProgressDialog.show();
                        AppManager.getInstance().getRbtConnector().deleteUserDefinedPlaylist(udpAssetDTO.getId(), new AppBaselineCallback<String>() {
                            @Override
                            public void success(String result) {
                                mProgressDialog.dismiss();
                                ((BaseActivity) context).showShortToast(result);
                                if (mAdapterInternalCallback != null)
                                    mAdapterInternalCallback.reloadData();
                            }

                            @Override
                            public void failure(String errMsg) {
                                mProgressDialog.dismiss();
                                ((BaseActivity) context).showShortToast(errMsg);
                            }
                        });
                    }

                    @Override
                    public void NegativeButton(DialogInterface dialog, int id) {

                    }
                });
    }

    private SetShuffleMainBSFragment mSetShuffleMainBSFragment;

    private void editUdp(UdpAssetDTO udpAssetDTO) {
        mSetShuffleMainBSFragment = WidgetUtils.getUdpShuffleBottomSheet(udpAssetDTO.convert(), true).setCallback(new OnBottomSheetChangeListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

            }

            @Override
            public void onDismiss(DialogInterface dialogInterface, boolean success, String message, Object result) {
                handleBottomSheetResult(success, message);
                if (!success && mAdapterInternalCallback != null)
                    mAdapterInternalCallback.reloadData();
            }

            @Override
            public void onCancel(DialogInterface dialogInterface, boolean success, String message) {
                handleBottomSheetResult(success, message);
            }
        });
        mSetShuffleMainBSFragment.showSheet(mFragmentManager);
    }

    /*private void shareUdp(UdpAssetDTO udpAssetDTO) {
        new ShareHelper(getFragmentContext(), new ShareHelper.ShareDLGenerateListener() {
            @Override
            public void onGenerating() {
                AppDialog.showProgress(getChildFragmentManager());
            }

            @Override
            public void onSuccess(ShortDynamicLink shortDynamicLink, RingBackToneDTO ringBackToneDTO) {
                AppDialog.dismissProgress(getChildFragmentManager());
                ShareHelper.shareLink(getFragmentContext(), shortDynamicLink.getShortLink(), ringBackToneDTO);
            }

            @Override
            public void onFailure(String errorMessage) {
                AppDialog.dismissProgress(getChildFragmentManager());
                getRootActivity().showShortSnackBar(errorMessage);
            }
        }).generateShareRingBackContent(data);
    }*/

    /**
     * Handle callback response of bottom sheet
     *
     * @param success A boolean value that represent success/failure
     * @param message Success/Failure message
     */
    private void handleBottomSheetResult(boolean success, String message) {
        if (success && getContext() != null) {
            AppRatingPopup appRatingPopup = new AppRatingPopup(getContext(), () -> ((BaseActivity) getContext()).redirect(HomeActivity.class, null, true, true));
            appRatingPopup.show();
        }
    }

    private String getCaption(String caption, String count) {
        if (!TextUtils.isEmpty(caption) && !TextUtils.isEmpty(count)) {
            int finalCount = Integer.parseInt(count);
            return getCaption(caption, finalCount);
        }
        return caption;
    }

    private String getCaption(String caption, int count) {
        if (!TextUtils.isEmpty(caption) && count > 0) {
            String countCaption = getContext().getString(count > 1 ? R.string.shuffle_counts_caption : R.string.shuffle_count_caption, count);
            return (caption.length() <= 18 ? caption : (caption.substring(0, 17)) + "...") + " " + countCaption;
        }
        return caption;
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessage(ContentCountChangeEvent event) {
        if (mSetShuffleMainBSFragment != null && mSetShuffleMainBSFragment.isAdded() && mSetShuffleMainBSFragment.isVisible() && event != null && event.getCurrentCount() < 1)
            mSetShuffleMainBSFragment.dismissSheet();
    }

    @Override
    public void register() {
        super.register();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    @Override
    public void unregister() {
        super.unregister();
        EventBus.getDefault().unregister(this);
    }
}
