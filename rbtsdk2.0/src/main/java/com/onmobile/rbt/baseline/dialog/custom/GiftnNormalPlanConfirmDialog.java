package com.onmobile.rbt.baseline.dialog.custom;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;
import com.onmobile.rbt.baseline.model.ContactModelDTO;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.widget.MultiLineRadioButton;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

/**
 * Created by Shahbaz Akhtar on 21/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public class GiftnNormalPlanConfirmDialog extends BaseDialog implements View.OnClickListener {

    private PLAN mCurrentPlanSelected = PLAN.GIFT;
    private RadioGroup mPlanGroup;
    private ActionCallBack mActionCallBack;
    private ContactModelDTO mContactModelDTO;

    public enum PLAN {
        GIFT,
        NORMAL
    }

    public interface ActionCallBack {
        void onContinue(PLAN plan);

        void onCancel();
    }

    public GiftnNormalPlanConfirmDialog(@NonNull Context context, ContactModelDTO contactModelDTO, ActionCallBack actionCallBack) {
        super(context);
        this.mContactModelDTO = contactModelDTO;
        this.mActionCallBack = actionCallBack;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_shuffle_upgrade;
    }

    @Override
    protected void bindRequirements() {

    }

    @Override
    protected void initViews() {
        mPlanGroup = findViewById(R.id.rg_shuffle_upgrade);
        AppCompatButton btnCancel = findViewById(R.id.btn_cancel_shuffle_upgrade);
        AppCompatButton btnContinue = findViewById(R.id.btn_continue_shuffle_upgrade);

        TextView info1 = findViewById(R.id.shuffle_upgrade_info1);
        TextView info2 = findViewById(R.id.shuffle_upgrade_info2);
        info1.setVisibility(View.GONE);
        info2.setVisibility(View.GONE);

        mPlanGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (PLAN.GIFT.ordinal() == checkedId) {
                mCurrentPlanSelected = PLAN.GIFT;
            } else if (PLAN.NORMAL.ordinal() == checkedId) {
                mCurrentPlanSelected = PLAN.NORMAL;
            }
        });

        btnCancel.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        generateOptions();
    }

    @Override
    protected boolean windowAnimationRequired() {
        return false;
    }

    @Override
    protected int windowAnimationStyle() {
        return 0;
    }

    private void generateOptions() {

        if (mPlanGroup != null) {

            try {
                if (mPlanGroup.getChildCount() > 0)
                    mPlanGroup.removeAllViews();
            } catch (Exception ignored) {
            }

            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            params.bottomMargin = (int) Util.convertDpToPixel(8, getContext());

            MultiLineRadioButton rb = new MultiLineRadioButton(getContext());
            rb.setId(1 + new Random().nextInt(AppConstant.SMALL_NUMBER_TO_GENERATE_RANDOM_ID));
            rb.setPrimaryTextBold(false);
            rb.setPrimaryText(String.format(getContext().getString(R.string.plan_choose_dialog_gift_option), mContactModelDTO.getName()));
            rb.setPrimaryTextSize(Util.convertDpToPixel(14, getContext()));
            rb.setSecondaryText("");
            rb.setLayoutParams(params);
            rb.setChecked(true);
            mPlanGroup.addView(rb);

            MultiLineRadioButton rb1 = new MultiLineRadioButton(getContext());
            rb1.setId(2 + new Random().nextInt(AppConstant.SMALL_NUMBER_TO_GENERATE_RANDOM_ID));
            rb1.setPrimaryTextBold(false);
            rb1.setPrimaryText(getContext().getString(R.string.plan_choose_dialog_normal_option));
            rb1.setPrimaryTextSize(Util.convertDpToPixel(14, getContext()));
            rb1.setLayoutParams(params);
            rb1.setChecked(false);
            mPlanGroup.addView(rb1);
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_cancel_shuffle_upgrade) {
            dismiss();
            if (mActionCallBack != null)
                mActionCallBack.onCancel();
        }else if(v.getId() == R.id.btn_continue_shuffle_upgrade) {
            dismiss();
            if (mActionCallBack != null)
                mActionCallBack.onContinue(mCurrentPlanSelected);
        }
    }
}
