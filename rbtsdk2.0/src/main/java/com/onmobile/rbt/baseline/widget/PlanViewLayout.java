package com.onmobile.rbt.baseline.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.onmobile.rbt.baseline.http.api_action.dtos.ChartItemDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.PricingSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.UserSubscriptionDTO;
import com.onmobile.rbt.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;

/**
 * Created by Shahbaz Akhtar on 21/11/2018.
 *
 * @author Shahbaz Akhtar
 */
public class PlanViewLayout extends FrameLayout {

    public interface PlanSelectedListener {
        void onPlanSelected(PlanView plan);
    }

    private AppCompatTextView mTvPlanHeader, mTvPlanFooter, mTvPlanError;
    private LinearLayout mRGPlans;
    private ContentLoadingProgressBar mPlanLoading;

    @FunkyAnnotation.ViewStatus
    private int mStatus = FunkyAnnotation.VIEW_STATUS_LOADING;
    private String mHeaderText, mFooterText, mErrorText;

    private PlanSelectedListener mListener;

    private PricingIndividualDTO mPricingIndividualDTO;
    private RingBackToneDTO mRingBackToneDTO;
    private ChartItemDTO mChartItemDTO;
    private boolean isMyAccount = false;

    private Object mExtras;

    private PlanView selectedPlanView;

    public PlanViewLayout(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public PlanViewLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PlanViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PlanViewLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }

    @Override
    public void invalidate() {
        super.invalidate();
        update();
    }

    /**
     * Initialize Custom view
     *
     * @param context      Context
     * @param attrs        AttributeSet
     * @param defStyleAttr defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        setDefault();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PlanViewLayout, defStyleAttr, 0);
        if (array != null) {
            try {
                mHeaderText = array.getString(R.styleable.PlanViewLayout_header_text);
                mFooterText = array.getString(R.styleable.PlanViewLayout_footer_text);
                mErrorText = array.getString(R.styleable.PlanViewLayout_error_text);
            } finally {
                array.recycle();
            }
        }

        initView();
        updateOnce();
        invalidate();
    }

    /**
     * Initialize the views
     */
    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_plan_view_custom, null);

        mRGPlans = view.findViewById(R.id.rg_plans_bottom_sheet);
        mPlanLoading = view.findViewById(R.id.pb_plans_loading_bottom_sheet);
        mTvPlanHeader = view.findViewById(R.id.tv_header_plans_bottom_sheet);
        mTvPlanFooter = view.findViewById(R.id.tv_footer_plans_bottom_sheet);
        mTvPlanError = view.findViewById(R.id.tv_error_plans_bottom_sheet);

        addView(view);
    }

    /**
     * Initialize default values
     */
    private void setDefault() {

    }

    /**
     * Update UI for the first time
     */
    private void updateOnce() {

    }

    /**
     * Update UI on every invalidate
     */
    @SuppressLint("SwitchIntDef")
    private void update() {
        switch (mStatus) {
            case FunkyAnnotation.VIEW_STATUS_CONTENT:
                mTvPlanHeader.setText(mHeaderText);
                mTvPlanFooter.setText(mFooterText);
                showPlans();
                break;
            case FunkyAnnotation.VIEW_STATUS_LOADING:
                showPlanLoading();
                break;
            default:
                showPlanError();
                break;
        }
    }

    /**
     * Show plan layout and hide others
     */
    private void showPlans() {
        mRGPlans.setVisibility(View.VISIBLE);
        mPlanLoading.setVisibility(INVISIBLE);
        if(!isMyAccount) {
            mTvPlanHeader.setVisibility(View.VISIBLE);
        }
        else{
            mTvPlanHeader.setVisibility(View.GONE);
        }
        mTvPlanFooter.setVisibility(View.VISIBLE);
        mTvPlanError.setVisibility(View.INVISIBLE);
    }

    /**
     * Show loading layout and hide others
     */
    private void showPlanLoading() {
        mRGPlans.setVisibility(View.INVISIBLE);
        mPlanLoading.setVisibility(View.VISIBLE);
        mTvPlanError.setVisibility(View.INVISIBLE);
        if(!isMyAccount) {
            mTvPlanHeader.setVisibility(View.INVISIBLE);
        }
        else{
            mTvPlanHeader.setVisibility(View.GONE);
        }
        mTvPlanFooter.setVisibility(View.INVISIBLE);
    }

    /**
     * Show error/message layout and hide others
     */
    private void showPlanError() {
        mRGPlans.setVisibility(View.INVISIBLE);
        mPlanLoading.setVisibility(View.INVISIBLE);
        if(!isMyAccount) {
            mTvPlanHeader.setVisibility(View.INVISIBLE);
        }
        else{
            mTvPlanHeader.setVisibility(View.INVISIBLE);
        }
        mTvPlanFooter.setVisibility(View.INVISIBLE);
        mTvPlanError.setVisibility(View.VISIBLE);
        mTvPlanError.setText(!TextUtils.isEmpty(mErrorText) ? mErrorText : getContext().getString(R.string.something_went_wrong));
    }

    /**
     * Attach a new PlanView into the layout
     *
     * @param plan     PlanView
     * @param priceDTO PricingSubscriptionDTO pojo
     */
    public void addPlan(PlanView plan, PricingSubscriptionDTO priceDTO) {
        if (plan != null && priceDTO != null) {
            plan.setPriceDTO(priceDTO);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            RelativeLayout bestPlanLayout = (RelativeLayout) inflater.inflate(R.layout.best_plan_layout, null);

            if (!plan.isGift() &&priceDTO.getCatalog_subscription_id().equals(BaselineApplication.getApplication().getRbtConnector().getBestValuePack())) {
                bestPlanLayout.findViewById(R.id.best_plan_recommendation_layout).setVisibility(View.VISIBLE);
            }
            else{
                bestPlanLayout.findViewById(R.id.best_plan_recommendation_layout).setVisibility(View.INVISIBLE);
            }

            RelativeLayout bestPlanParentLayout = bestPlanLayout.findViewById(R.id.best_plan_parent_layout);
            plan.setId(mRGPlans.getChildCount());
            bestPlanParentLayout.addView(plan);

            mRGPlans.addView(bestPlanLayout);
            if(mRGPlans.getChildCount() == 1){
                plan.setChecked(true);
                selectedPlanView = plan;
                if(mListener != null) {
                    mListener.onPlanSelected(plan);
                }
            }

            bestPlanLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    for(int i=0; i<mRGPlans.getChildCount();i++){
                        RelativeLayout  planLayout = (RelativeLayout) mRGPlans.getChildAt(i);
                        RelativeLayout planParentLayout = planLayout.findViewById(R.id.best_plan_parent_layout);
                        PlanView planChildView = (PlanView) planParentLayout.getChildAt(0);

                        RelativeLayout currentChildLayout = view.findViewById(R.id.best_plan_parent_layout);
                        PlanView currentChildView = (PlanView) currentChildLayout.getChildAt(0);

                        if(planChildView.getId() == currentChildView.getId()){
                            currentChildView.setChecked(true);
                            selectedPlanView = currentChildView;
                            if(mListener != null) {
                                mListener.onPlanSelected(currentChildView);
                            }
                        }
                        else{
                            planChildView.setChecked(false);
                        }
                    }
                }
            });

            mStatus = FunkyAnnotation.VIEW_STATUS_CONTENT;
            invalidate();
        }
    }

    public void addPlan(PlanView plan, UserSubscriptionDTO userSubscriptionDTO) {
        if (plan != null && userSubscriptionDTO != null) {
            plan.setUserSubscriptionDTO(userSubscriptionDTO);
            mRGPlans.addView(plan);
            mStatus = FunkyAnnotation.VIEW_STATUS_CONTENT;
            invalidate();
        }
    }

    /**
     * Attach a new PlanView into the layout based on pojo
     *
     * @param priceDTO PricingSubscriptionDTO pojo
     */
    public void addPlan(PricingSubscriptionDTO priceDTO) {
        if (priceDTO != null) {
            PlanView plan = new PlanView(getContext());
            plan.setPriceDTO(priceDTO);
            mRGPlans.addView(plan);
            mStatus = FunkyAnnotation.VIEW_STATUS_CONTENT;
            invalidate();
        }
    }

    /**
     * show no plan as layout
     *
     * @param pricingIndividualDTO existing plan details
     */
    public boolean addEmptyPlan(PricingIndividualDTO pricingIndividualDTO) {
        if (pricingIndividualDTO != null) {
            mPricingIndividualDTO = pricingIndividualDTO;
            mErrorText = pricingIndividualDTO.getShortDescription();
            mStatus = FunkyAnnotation.VIEW_STATUS_ERROR;
            invalidate();
            return true;
        }
        return false;
    }

    /**
     * show loading layout
     */
    public void loading() {
        mStatus = FunkyAnnotation.VIEW_STATUS_LOADING;
        invalidate();
    }

    /**
     * show error layout
     *
     * @param errorMessage String value to show as error
     */
    public void error(String errorMessage) {
        mErrorText = errorMessage;
        mStatus = FunkyAnnotation.VIEW_STATUS_ERROR;
        invalidate();
    }

    /**
     * Returns the selected plan from Layout
     *
     * @return PlanView
     */
    public PlanView getSelectedPlan() {
        return selectedPlanView;//mRGPlans.findViewById(mRGPlans.getCheckedRadioButtonId());
    }

    /**
     * Set callback for user selection of PlanView
     *
     * @param listener PlanSelectedListener
     */
    public void addOnPlanSelectedListener(PlanSelectedListener listener) {
        mListener = listener;
//        mRGPlans.setOnCheckedChangeListener((group, checkedId) -> {
//            if (mListener != null)
//                mListener.onPlanSelected(group.findViewById(checkedId));
//        });
    }

    /**
     * Get status of view
     *
     * @return Status as integer
     */
    public int getStatus() {
        return mStatus;
    }

    /**
     * Set statsu for view
     *
     * @param mStatus Status as integer
     */
    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
        invalidate();
    }

    /**
     * Get Header text
     *
     * @return String value as header
     */
    public String getHeaderText() {
        return mHeaderText;
    }

    /**
     * Set header for layout
     *
     * @param headerText String value as header
     */
    public void setHeaderText(String headerText) {
        this.mHeaderText = headerText;
        invalidate();
    }

    /**
     * Get Footer text
     *
     * @return String value as header
     */
    public String getFooterText() {
        return mFooterText;
    }

    /**
     * Set footer for layout
     *
     * @param footerText String value as header
     */
    public void setFooterText(String footerText) {
        this.mFooterText = footerText;
        invalidate();
    }

    /**
     * Get error message
     *
     * @return String as error/message
     */
    public String getErrorText() {
        return mErrorText;
    }

    /**
     * Set error/message into the layout
     *
     * @param mErrorText String as error message
     */
    public void setErrorText(String mErrorText) {
        this.mErrorText = mErrorText;
        invalidate();
    }

    /**
     * Reset all content data
     *
     */
    public void resetData() {
        mRGPlans.removeAllViews();
        selectedPlanView = null;
        mPricingIndividualDTO = null;
        mRingBackToneDTO = null;
        mChartItemDTO = null;
        mExtras = null;
    }

    /**
     * Returns the count of Plans attached to layout
     *
     * @return Count
     */
    public int getPlanCount() {
        return mRGPlans.getChildCount();
    }

    public PricingIndividualDTO getPricingIndividualDTO() {
        return mPricingIndividualDTO;
    }

    public void setRingBackToneDTO(RingBackToneDTO ringBackToneDTO) {
        mRingBackToneDTO = ringBackToneDTO;
    }

    public RingBackToneDTO getRingBackToneDTO() {
        return mRingBackToneDTO;
    }

    public ChartItemDTO getChartItemDTO() {
        return mChartItemDTO;
    }

    public void setChartItemDTO(ChartItemDTO chartItemDTO) {
        mChartItemDTO = chartItemDTO;
    }

    public Object getExtras() {
        return mExtras;
    }

    public void setExtras(Object extras) {
        this.mExtras = extras;
    }

    public void setMyAccount(boolean isMyAccount){
        this.isMyAccount = isMyAccount;
    }
}
