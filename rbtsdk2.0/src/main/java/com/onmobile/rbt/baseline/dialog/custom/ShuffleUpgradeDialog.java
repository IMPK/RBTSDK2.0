package com.onmobile.rbt.baseline.dialog.custom;

import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.onmobile.baseline.http.api_action.dtos.pricing.availability.PricingIndividualDTO;
import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.dialog.base.BaseDialog;
import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.widget.MultiLineRadioButton;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

/**
 * Created by Shahbaz Akhtar on 18/02/2019.
 *
 * @author Shahbaz Akhtar
 */

public class ShuffleUpgradeDialog extends BaseDialog implements View.OnClickListener {

    public interface ActionCallBack {
        void onContinue(PricingIndividualDTO item);

        void onCancel();
    }

    private RadioGroup mRGShuffleUpgrade;

    private List<PricingIndividualDTO> mListPricingIndividualDTOS;
    private ActionCallBack mActionCallBack;
    private PricingIndividualDTO mSelectedPlan;

    public ShuffleUpgradeDialog(@NonNull Context context, List<PricingIndividualDTO> mListPricingIndividualDTOS, ActionCallBack actionCallBack) {
        super(context);
        this.mListPricingIndividualDTOS = mListPricingIndividualDTOS;
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
        mRGShuffleUpgrade = findViewById(R.id.rg_shuffle_upgrade);
        AppCompatButton btnCancel = findViewById(R.id.btn_cancel_shuffle_upgrade);
        AppCompatButton btnContinue = findViewById(R.id.btn_continue_shuffle_upgrade);

        mRGShuffleUpgrade.setOnCheckedChangeListener((group, checkedId) -> {
            if (mListPricingIndividualDTOS != null && mListPricingIndividualDTOS.size() > 0) {
                int id = 1;
                for (PricingIndividualDTO item : mListPricingIndividualDTOS) {
                    if (checkedId == id) {
                        mSelectedPlan = item;
                        break;
                    }
                    id++;
                }
            }
        });

        btnCancel.setOnClickListener(this);
        btnContinue.setOnClickListener(this);
    }

    @Override
    protected void bindData() {
        if (mListPricingIndividualDTOS != null && mListPricingIndividualDTOS.size() > 0) {
            generateUpgradeShufflePlans();
            return;
        }
        dismiss();
        if (mActionCallBack != null)
            mActionCallBack.onCancel();
    }

    @Override
    protected boolean windowAnimationRequired() {
        return true;
    }

    @Override
    protected int windowAnimationStyle() {
        return 0;
    }

    private void generateUpgradeShufflePlans() {
        //Collections.reverse(mListPricingIndividualDTOS);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.MATCH_PARENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        params.bottomMargin = (int) Util.convertDpToPixel(8, getContext());
        int id = 1;
        for (PricingIndividualDTO item : mListPricingIndividualDTOS) {
            MultiLineRadioButton rb = new MultiLineRadioButton(getContext());
            rb.setId(id);
            rb.setPrimaryTextBold(true);
            rb.setPrimaryText(item.getType());
            rb.setPrimaryTextSize(Util.convertDpToPixel(14, getContext()));
            rb.setSecondaryText(item.getLongDescription());
            rb.setSecondaryTextSize(Util.convertDpToPixel(11, getContext()));
            rb.setLayoutParams(params);
            rb.setExtras(item);
            if (id == 1)
                rb.setChecked(true);
            mRGShuffleUpgrade.addView(rb);
            id++;
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
            if (mActionCallBack != null && mSelectedPlan != null)
                mActionCallBack.onContinue(mSelectedPlan);
        }
    }
}
