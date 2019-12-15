package com.onmobile.rbt.baseline.holder;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.onmobile.rbt.baseline.http.api_action.dtos.BannerDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.HomeActivity;
import com.onmobile.rbt.baseline.activities.StoreActivity;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.adapter.BannerPagerAdapter;
import com.onmobile.rbt.baseline.analytics.AnalyticsConstants;
import com.onmobile.rbt.baseline.application.AppLocaleHelper;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.fragment.FragmentHomeStore;
import com.onmobile.rbt.baseline.listener.LifeCycleCallback;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.AppUtils;
import com.onmobile.rbt.baseline.util.BannerBadgeView;
import com.onmobile.rbt.baseline.widget.EnhancedWrapContentViewPager;
import com.onmobile.rbt.baseline.widget.pageindicator.PageIndicator;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class BannerStackViewHolder extends StackViewHolder<List<BannerDTO>> implements LifeCycleCallback {

    private BannerPagerAdapter mBannerPagerAdapter;
    private List<BannerDTO> mBannerDtoList;
    private FragmentManager mFragmentManager;
    private Context mContext;
    private RelativeLayout viewFlipperLayout;
    private EnhancedWrapContentViewPager viewPager;
    private PageIndicator pageIndicator;
    private ImageView mBackgroundImageView;
    private BlurView mBlurView;
    private View mDecorView;
    private ViewGroup mRootView;
    private TextView mGoToStoreBtn;
    private BannerBadgeView bannerBadgeView;
    private RelativeLayout angleTextRel;
    private AppLocaleHelper mAppLocaleHelper;

    private Handler handler;
    private Runnable runnable;

    public BannerStackViewHolder(Context context, View root, View loading, View content, FragmentManager fragmentManager) {
        super(context, root, loading, content);
        mContext = context;
        this.mFragmentManager = fragmentManager;
    }

    @Override
    protected void initViews() {
        mAppLocaleHelper = BaselineApplication.getApplication().getAppLocaleManager();
        if (contentLayout != null) {
            viewFlipperLayout = contentLayout.findViewById(R.id.banner_viewpager_layout);
            bannerBadgeView = contentLayout.findViewById(R.id.banner_bandageView);
            angleTextRel = contentLayout.findViewById(R.id.angle_text_relative);
            Configuration config = getContext().getResources().getConfiguration();
            if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                bannerBadgeView.setRotation(0);
                angleTextRel.setRotation(45);
            }
            viewPager = contentLayout.findViewById(R.id.banner_viewpager);
            mBackgroundImageView = contentLayout.findViewById(R.id.banner_card_image);
            pageIndicator = contentLayout.findViewById(R.id.indicator_banner_stack_discover);
            mDecorView = ((BaseActivity) getContext()).getWindow().getDecorView();
            mRootView = contentLayout.findViewById(R.id.blur_view_root);
            mBlurView = contentLayout.findViewById(R.id.blurView);
            mGoToStoreBtn = contentLayout.findViewById(R.id.tv_button_next_discover);
            mGoToStoreBtn.setOnClickListener(view -> {
                if (mContext instanceof HomeActivity) {
                    (((HomeActivity) mContext)).changeFragment(null, FragmentHomeStore.class, null);
                } else {
                    Bundle bannerBundle = new Bundle();
                    bannerBundle.putString(AppConstant.KEY_INTENT_CALLER_SOURCE, AnalyticsConstants.EVENT_PV_SOURCE_SET_FROM_BANNER_PRE_BUY);
                    ((BaseActivity) mContext).redirect(StoreActivity.class, bannerBundle, false, false);
                }
            });
            viewFlipperLayout.setOnClickListener(v -> {

            });

            viewPager.enableSmoothScroller();
        }
    }

    @Override
    protected void bindViews() {
    }

    @Override
    public void bindHolder(List<BannerDTO> data) {
        showContent();
        if (data != null && data.size() > 0 && mBannerPagerAdapter == null) {
            mBannerDtoList = new ArrayList<>();
            mBannerDtoList.addAll(data);
            int defaultSelection = 0;
            if (mBannerDtoList.size() == 1) {
                mBannerPagerAdapter = new BannerPagerAdapter(((AppCompatActivity) mContext).getSupportFragmentManager(), mContext, mBannerDtoList);
                viewPager.setAdapter(mBannerPagerAdapter);
                pageIndicator.attachToPager(viewPager);
            } else {
                /**
                 * Change made by Appolla Sreedhar
                 */

                if ((mAppLocaleHelper.setAppLocalForDeviceLanguage( BaselineApplication.getApplication().getApplicationContext(),true, false)).getLanguage().contains("ar")) {
                    defaultSelection = mBannerDtoList.size() - 1;
                } else {
                    defaultSelection = 1;
                }
                mBannerDtoList.add(mBannerDtoList.get(0));
                mBannerDtoList.add(0, mBannerDtoList.get(mBannerDtoList.size() - 2));
                mBannerPagerAdapter = new BannerPagerAdapter(((AppCompatActivity) mContext).getSupportFragmentManager(), mContext, mBannerDtoList);
                viewPager.setAdapter(mBannerPagerAdapter);
                viewPager.setOffscreenPageLimit(mBannerDtoList.size() + 1);
                int visibleDotCount = mBannerDtoList.size() / 2;
                visibleDotCount = visibleDotCount < 5 ? 5 : (visibleDotCount > 9 ? 9 : visibleDotCount);
                if (visibleDotCount % 2 == 0)
                    visibleDotCount++;
                pageIndicator.setVisibleDotCount(visibleDotCount);
                pageIndicator.attachToPager(viewPager);
                pageIndicator.setLooped(true);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(final int position) {
                        startAutoScroll();
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                            stopAutoScroll();
                        } else if (state == ViewPager.SCROLL_STATE_IDLE) {
                            final int currentItemCount = viewPager.getCurrentItem();
                            final int listSize = mBannerDtoList.size();
                            if (currentItemCount == 0)
                                viewPager.setCurrentItem(listSize - 2, false);
                            else if (currentItemCount == listSize - 1)
                                viewPager.setCurrentItem(1, false);
                            else
                                blurrBackground(mBannerDtoList.get(currentItemCount));
                            startAutoScroll();
                        }
                    }
                });
            }

            viewPager.setCurrentItem(defaultSelection,false);
            blurrBackground(mBannerDtoList.get(0));
            startAutoScroll();
            viewFlipperLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void unbind() {

    }

    private void blurrBackground(BannerDTO bannerDTO) {
        if (mBlurView != null)
            mBlurView.post(() -> {
                float radius = 5f;
                mBlurView.setupWith(mRootView)
                        .setFrameClearDrawable(mDecorView.getBackground())
                        .setBlurAlgorithm(new RenderScriptBlur(getContext()))
                        .setBlurRadius(radius)
                        .setHasFixedTransformationMatrix(true);

                int screenWidth = AppUtils.getScreenWidth(getContext()); //AppUtils.dpToPx(AppConstant.DEFAULT_IMAGE_BANNER_SIZE);
                String fitableImage = AppUtils.getFitableImage(mContext, bannerDTO.getImageURL(), screenWidth);

                Glide.with(mContext)
                        .load(fitableImage)
                        .error(R.drawable.default_album_art)
                        .placeholder(R.drawable.default_album_art)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(mBackgroundImageView);
            });
    }

    private void startAutoScroll() {
        stopAutoScroll();
        if (viewPager != null && mBannerDtoList != null && mBannerDtoList.size() > 1) {
            handler = new Handler();
            runnable = () -> {
                if (viewPager != null && mBannerDtoList != null) {
                    final int currentItemCount = viewPager.getCurrentItem();

                    /**
                     * Change made by Appolla Sreedhar
                     */

                    if ((mAppLocaleHelper.setAppLocalForDeviceLanguage(BaselineApplication.getApplication().getApplicationContext(),true, false)).getLanguage().contains("ar")) {
                        viewPager.setCurrentItem((0 < currentItemCount) ? currentItemCount - 1 : mBannerDtoList.size(), true);
                    } else {
                        viewPager.setCurrentItem((currentItemCount < mBannerDtoList.size() - 1) ? currentItemCount + 1 : 0, true);
                    }
                }
            };
            handler.postDelayed(runnable, AppConstant.BANNER_CARD_SWIPE_DELAY_DURATION);
        }
    }

    private void stopAutoScroll() {
        if (handler != null) {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
                runnable = null;
            }
            handler = null;
        }
    }

    @Override
    public void onLifeCycleStart() {
        if (viewPager != null)
            startAutoScroll();
    }

    @Override
    public void onLifeCycleStop() {
        stopAutoScroll();
    }
}
