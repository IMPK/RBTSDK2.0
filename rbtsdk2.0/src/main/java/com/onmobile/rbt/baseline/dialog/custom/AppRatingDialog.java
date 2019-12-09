package com.onmobile.rbt.baseline.dialog.custom;

import android.content.Context;
import android.os.Handler;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by Shahbaz Akhtar on 21/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public class AppRatingDialog extends BaseDialog implements View.OnClickListener {

    private Context mContext;
    private ActionCallBack mActionCallBack;
    private AppCompatImageView mAppIntroOuterCircle;

    public AppRatingDialog(@NonNull Context context, ActionCallBack actionCallBack) {
        super(context);
        mContext = context;
        mActionCallBack = actionCallBack;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_app_rating;
    }

    @Override
    protected void bindRequirements() {

    }

    @Override
    protected void initViews() {
        mAppIntroOuterCircle = findViewById(R.id.iv_outer_app_intro_icon_layout);

        LinearLayout rateNowText = findViewById(R.id.rate_now_layout);
        LinearLayout remindMeLaterText = findViewById(R.id.remind_me_later_layout);
        LinearLayout noThanksText = findViewById(R.id.no_thanks_layout);

        rateNowText.setOnClickListener(this);
        remindMeLaterText.setOnClickListener(this);
        noThanksText.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        Animation rotationAnimation = AnimationUtils.loadAnimation(mContext, R.anim.rotate_anim_app_rating);
        new Handler().post(() -> mAppIntroOuterCircle.startAnimation(rotationAnimation));
    }

    @Override
    protected boolean windowAnimationRequired() {
        return true;
    }

    @Override
    protected int windowAnimationStyle() {
        return 0;
    }

    public interface ActionCallBack {
        void onRateNow();

        void onRemindMeLater();

        void onNoThanks();
    }

    @Override
    public void onClick(View v) {
        try {
            mAppIntroOuterCircle.getAnimation().cancel();
        } catch (Exception ignored) {
        }
        if(v.getId() == R.id.rate_now_layout) {
            dismiss();
            if (mActionCallBack != null) {
                SharedPrefProvider.getInstance(mContext).setAppRated(true);
                mActionCallBack.onRateNow();
            }
        }else if(v.getId() == R.id.remind_me_later_layout) {
            dismiss();
            if (mActionCallBack != null) {
                SharedPrefProvider.getInstance(mContext).setAppRatingRemindLater(true);
                mActionCallBack.onRemindMeLater();
            }
        }else if(v.getId() == R.id.no_thanks_layout) {
            dismiss();
            if (mActionCallBack != null) {
                SharedPrefProvider.getInstance(mContext).setAppRatingNoThanks(true);
                mActionCallBack.onNoThanks();
            }
        }
    }
}
