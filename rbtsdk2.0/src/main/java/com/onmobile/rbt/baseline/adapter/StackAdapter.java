package com.onmobile.rbt.baseline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.holder.AzaanStackViewHolder;
import com.onmobile.rbt.baseline.holder.BannerStackViewHolder;
import com.onmobile.rbt.baseline.holder.MusicShuffleStackViewHolder;
import com.onmobile.rbt.baseline.holder.NameTunesStackViewHolder;
import com.onmobile.rbt.baseline.holder.OtherStackViewHolder;
import com.onmobile.rbt.baseline.holder.ProfileTuneStackViewHolder;
import com.onmobile.rbt.baseline.holder.RecommendationsStackViewHolder;
import com.onmobile.rbt.baseline.holder.StackViewHolder;
import com.onmobile.rbt.baseline.holder.TrendingStackViewHolder;
import com.onmobile.rbt.baseline.listener.LifeCycleCallback;
import com.onmobile.rbt.baseline.model.StackItem;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Shahbaz Akhtar on 10/09/2018.
 *
 * @author Shahbaz Akhtar
 */

public class StackAdapter extends RecyclerView.Adapter<StackViewHolder> implements LifeCycleCallback {

    public interface CallBack {
        void onItemOptionClick(int position, StackItem item);

        void onNextButtonClick(int position, StackItem item);
    }

    private Context mContext;
    private LayoutInflater mInflater;
    private List<StackItem> stackItems;
    private FragmentManager mFragmentManager;

    private CallBack mCallBack;
    private RecommendationsStackViewHolder mRecommendationsStackViewHolder;
    private BannerStackViewHolder mBannerStackViewHolder;
    public StackAdapter(@NonNull List<StackItem> stackItems, FragmentManager fragmentManager) {
        this.stackItems = stackItems;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewType(int position) {
        return stackItems.get(position).getType();
    }

    @NonNull
    @Override
    public StackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mInflater == null) {
            mContext = parent.getContext();
            mInflater = LayoutInflater.from(parent.getContext());
        }

        View root = mInflater.inflate(R.layout.layout_discover_stack_item_card, parent, false);
        View loadingLayout = mInflater.inflate(R.layout.layout_discover_stack_item_loading, parent, false);

        switch (viewType) {
            case FunkyAnnotation.TYPE_TRENDING:
                View contentLayout = mInflater.inflate(R.layout.layout_discover_stack_content_trending, parent, false);
                return new TrendingStackViewHolder(mContext, root, loadingLayout, contentLayout, mFragmentManager);
            case FunkyAnnotation.TYPE_PROFILE_TUNES:
                View profileLayout = mInflater.inflate(R.layout.layout_discover_stack_profile_tune, parent, false);
                return new ProfileTuneStackViewHolder(mContext, root, loadingLayout, profileLayout, mFragmentManager);
            case FunkyAnnotation.TYPE_NAME_TUNES:
                View nameTuneLayout = mInflater.inflate(R.layout.layout_discover_stack_name_tunes, parent, false);
                return new NameTunesStackViewHolder(mContext, root, loadingLayout, nameTuneLayout, mFragmentManager);
            case FunkyAnnotation.TYPE_MUSIC_SHUFFLES:
                View musicShuffleLayout = mInflater.inflate(R.layout.layout_discover_stack_music_shuffle, parent, false);
                return new MusicShuffleStackViewHolder(mContext, root, loadingLayout, musicShuffleLayout, mFragmentManager);
            case FunkyAnnotation.TYPE_RECOMMENDATIONS:
                View recommendationLayout = mInflater.inflate(R.layout.layout_discover_stack_recommendations, parent, false);
                mRecommendationsStackViewHolder = new RecommendationsStackViewHolder(mContext, root, loadingLayout, recommendationLayout, mFragmentManager);
                mRecommendationsStackViewHolder.onLifeCycleStart();
                return mRecommendationsStackViewHolder;
            case FunkyAnnotation.TYPE_BANNER:
                View bannerLayout = mInflater.inflate(R.layout.layout_discover_stack_banner, parent, false);
                mBannerStackViewHolder = new BannerStackViewHolder(mContext, root, loadingLayout, bannerLayout, mFragmentManager);
                mBannerStackViewHolder.onLifeCycleStart();
                return mBannerStackViewHolder;
            case FunkyAnnotation.TYPE_AZAN:
                View azanLayout = mInflater.inflate(R.layout.layout_discover_stack_content_azaan, parent, false);
                AzaanStackViewHolder azaanStackViewHolder = new AzaanStackViewHolder(mContext, root, loadingLayout, azanLayout, mFragmentManager);
                return azaanStackViewHolder;
            default:
                return new OtherStackViewHolder(mContext, root, loadingLayout, null);
        }

    }

    public StackAdapter setCallback(CallBack callback) {
        this.mCallBack = callback;
        return this;
    }

    @Override
    public void onBindViewHolder(@NonNull final StackViewHolder holder, final int position) {
        StackItem item = stackItems.get(position);

        bindCommonLayout(holder, item);

        if (item.isError()) {
            holder.showError(item.getErrorMessage());
            return;
        }

        if (item.isLoading()) {
            holder.showLoading();
            return;
        }

        switch (getItemViewType(position)) {
            case FunkyAnnotation.TYPE_TRENDING:
                bindTrending(holder, item, position);
                break;
            case FunkyAnnotation.TYPE_PROFILE_TUNES:
                bindProfileTunes(holder, item, position);
                break;
            case FunkyAnnotation.TYPE_NAME_TUNES:
                bindNameTunes(holder, item, position);
                break;
            case FunkyAnnotation.TYPE_MUSIC_SHUFFLES:
                bindMusicShuffle(holder, item, position);
                break;
            case FunkyAnnotation.TYPE_RECOMMENDATIONS:
                bindRecommendations(holder, item, position);
                break;
            case FunkyAnnotation.TYPE_BANNER:
                bindBanner(holder, item, position);
                break;
            case FunkyAnnotation.TYPE_AZAN:
                bindAzan(holder, item, position);
                break;
            default:
                break;
        }
    }

    private void bindCommonLayout(final StackViewHolder holder, StackItem item) {
        if (item.isOptionEnabled()) {
            holder.option.setVisibility(View.VISIBLE);
            holder.option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mCallBack != null && holder.getAdapterPosition() >= 0)
                        mCallBack.onItemOptionClick(holder.getAdapterPosition(), stackItems.get(holder.getAdapterPosition()));
                }
            });
        } else
            holder.option.setVisibility(View.GONE);

        if (item.isNextButton()) {
            holder.layoutNextButton.setVisibility(View.VISIBLE);
            holder.tvBtnNext.setText(item.getNextButtonLabel());
            if (!item.isNameTune()) {
                holder.tvBtnNext.setOnClickListener(view -> {
                    if (mCallBack != null && holder.getAdapterPosition() >= 0)
                        mCallBack.onNextButtonClick(holder.getAdapterPosition(), stackItems.get(holder.getAdapterPosition()));
                });
            }
        } else
            holder.layoutNextButton.setVisibility(View.GONE);

        if (item.getTitleColor() != 0) {
            holder.titleText.setTextColor(mContext.getResources().getColor(item.getTitleColor()));
            holder.subTitleText.setTextColor(mContext.getResources().getColor(item.getTitleColor()));
        }
        holder.titleText.setText(item.getTitle());
        holder.subTitleText.setText(item.getSubTitle());
    }

    @Override
    public int getItemCount() {
        return stackItems == null ? 0 : stackItems.size();
    }

    private void bindTrending(final StackViewHolder holder, StackItem item, int position) {
        TrendingStackViewHolder trendingHolder = (TrendingStackViewHolder) holder;
        if (item.getData() != null && item.getData() instanceof List) {
            trendingHolder.bindHolder((List<RingBackToneDTO>) item.getData());
        }
    }

    private void bindProfileTunes(final StackViewHolder holder, StackItem item, int position) {
        ProfileTuneStackViewHolder profileTuneHolder = (ProfileTuneStackViewHolder) holder;
        if (item.getData() != null && item.getData() instanceof List) {
            profileTuneHolder.bindHolder((List<RingBackToneDTO>) item.getData());
        }
    }

    private void bindRecommendations(final StackViewHolder holder, StackItem item, int position) {
        RecommendationsStackViewHolder recommendationsHolder = (RecommendationsStackViewHolder) holder;
        if (item.getData() != null && item.getData() instanceof List) {
            recommendationsHolder.bindHolder((List<RingBackToneDTO>) item.getData());
        }
    }

    private void bindBanner(final StackViewHolder holder, StackItem item, int position) {
        BannerStackViewHolder bannerStackViewHolder = (BannerStackViewHolder) holder;
        if (item.getData() != null && item.getData() instanceof List) {
            bannerStackViewHolder.bindHolder((List<BannerDTO>) item.getData());
        }
    }

    private void bindNameTunes(final StackViewHolder holder, StackItem item, int position) {
        NameTunesStackViewHolder nameTunesStackViewHolder = (NameTunesStackViewHolder) holder;
        if (item.getData() != null) {
            nameTunesStackViewHolder.bindHolder((List<RingBackToneDTO>) item.getData());
        }
    }

    private void bindMusicShuffle(final StackViewHolder holder, StackItem item, int position) {
        MusicShuffleStackViewHolder musicShuffleStackViewHolder = (MusicShuffleStackViewHolder) holder;
        if (item.getData() != null) {
            musicShuffleStackViewHolder.bindHolder((ChartItemDTO) item.getData());
        }
    }

    private void bindAzan(final StackViewHolder holder, StackItem item, int position) {
        AzaanStackViewHolder azanHolder = (AzaanStackViewHolder) holder;
        if (item.getData() != null && item.getData() instanceof List) {
            azanHolder.bindHolder((List<RingBackToneDTO>) item.getData());
        }
    }

    @Override
    public void onLifeCycleStart() {
        if (mRecommendationsStackViewHolder != null)
            mRecommendationsStackViewHolder.onLifeCycleStart();
        if (mBannerStackViewHolder != null)
            mBannerStackViewHolder.onLifeCycleStart();
    }

    @Override
    public void onLifeCycleStop() {
        if (mRecommendationsStackViewHolder != null)
            mRecommendationsStackViewHolder.onLifeCycleStop();
        if (mBannerStackViewHolder != null)
            mBannerStackViewHolder.onLifeCycleStop();
    }
}
