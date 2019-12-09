package com.onmobile.rbt.baseline.widget;


import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

/**
 * Created by Shahbaz Akhtar on 02/10/2018.
 *
 * @author Shahbaz Akhtar
 */
public class LabeledView extends FrameLayout {

    public interface OnLabeledListener {
        void onClick(LabeledView view);

        void onSwitch(LabeledView view, boolean checked);
    }

    private int valueTextColor, labelTextColor;
    private float valueTextSize, labelTextSize, leftPadding, rightPadding, topPadding, bottomPadding;
    private String label, value;
    private boolean divider, switcher, switchStatus, userInteraction = true;
    private float defTextPadding;

    private AppCompatTextView mViewLabel, mViewValue;
    private View mView, mViewDataContainer, mViewDivider;
    private SwitchCompat mViewSwitch;

    private int mSwitchThumbColorNormal, mSwitchThumbColorActivated, mSwitchThumbColorDisabled;

    private OnLabeledListener mListener;

    public LabeledView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public LabeledView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public LabeledView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        setDefault();

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LabeledView, defStyleAttr, 0);
        if (array != null) {
            try {
                leftPadding = array.getDimension(R.styleable.LabeledView_leftPadding, leftPadding);
                rightPadding = array.getDimension(R.styleable.LabeledView_rightPadding, rightPadding);
                topPadding = array.getDimension(R.styleable.LabeledView_topPadding, topPadding);
                bottomPadding = array.getDimension(R.styleable.LabeledView_bottomPadding, bottomPadding);
                labelTextSize = array.getDimension(R.styleable.LabeledView_labelTextSize, labelTextSize);
                valueTextSize = array.getDimension(R.styleable.LabeledView_valueTextSize, valueTextSize);
                labelTextColor = array.getColor(R.styleable.LabeledView_labelTextColor, labelTextColor);
                valueTextColor = array.getColor(R.styleable.LabeledView_valueTextColor, valueTextColor);
                label = array.getString(R.styleable.LabeledView_label);
                value = array.getString(R.styleable.LabeledView_value);
                switcher = array.getBoolean(R.styleable.LabeledView_switcher, switcher);
                switchStatus = array.getBoolean(R.styleable.LabeledView_switchStatus, switchStatus);
                divider = array.getBoolean(R.styleable.LabeledView_bottomDivider, divider);
                userInteraction = array.getBoolean(R.styleable.LabeledView_userInteraction, userInteraction);
                mSwitchThumbColorNormal = array.getColor(R.styleable.LabeledView_switchColorNormal, mSwitchThumbColorNormal);
                mSwitchThumbColorActivated = array.getColor(R.styleable.LabeledView_switchColorActivated, mSwitchThumbColorActivated);
                mSwitchThumbColorDisabled = array.getColor(R.styleable.LabeledView_switchColorDisabled, mSwitchThumbColorDisabled);
            } finally {
                array.recycle();
            }
        }

        initView();
        addView(mView);
        updateOnce();
        invalidate();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        update();
    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.layout_labeled_view, null);

        mViewDataContainer = mView.findViewById(R.id.layout_data_container_labeled);
        mViewLabel = mView.findViewById(R.id.tv_label_labeled);
        mViewValue = mView.findViewById(R.id.tv_value_labeled);
        mViewSwitch = mView.findViewById(R.id.switch_labeled);
        mViewDivider = mView.findViewById(R.id.view_divider_labeled);

        updateUserInteraction();
    }


    public TextView getLabelTextView(){
        return mViewLabel;
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (switcher) {
                mViewSwitch.toggle();
                return;
            }
            if (mListener != null) {
                mListener.onClick(LabeledView.this);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switchStatus = b;
            if (mListener != null)
                mListener.onSwitch(LabeledView.this, b);
        }
    };

    private void updateOnce() {

    }

    private void update() {
        updateUI();
    }

    private void updateUI() {
        mViewDataContainer.setPaddingRelative((int) leftPadding, (int) topPadding, (int) rightPadding, (int) bottomPadding);

        if (labelTextSize > 0L)
            mViewLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, labelTextSize);
        mViewLabel.setTextColor(labelTextColor);
        mViewLabel.setText(label);

        if (!switcher) {
            if (valueTextSize > 0L)
                mViewValue.setTextSize(TypedValue.COMPLEX_UNIT_PX, valueTextSize);
            mViewValue.setTextColor(valueTextColor);
            mViewValue.setText(value);
            mViewValue.setVisibility(VISIBLE);
            mViewSwitch.setVisibility(GONE);
        } else {
            mViewSwitch.setChecked(switchStatus);
            mViewValue.setVisibility(GONE);
            mViewSwitch.setVisibility(VISIBLE);
        }

        if (divider) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = (int) (leftPadding + defTextPadding);
            params.rightMargin = (int) (rightPadding + defTextPadding);
            params.height = 1;
            mViewDivider.setLayoutParams(params);
            mViewDivider.setVisibility(VISIBLE);
        } else {
            mViewDivider.setVisibility(INVISIBLE);
        }

        updateSwitchColor();
    }

    private void updateSwitchColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ColorStateList thumbStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            mSwitchThumbColorDisabled,
                            mSwitchThumbColorActivated,
                            mSwitchThumbColorNormal
                    }
            );
            mViewSwitch.getThumbDrawable().setTintList(thumbStates);

            /*ColorStateList thumbBackgroundStates = new ColorStateList(
                    new int[][]{
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_checked},
                            new int[]{}
                    },
                    new int[]{
                            Util.getOscuredColor(mSwitchThumbColorDisabled),
                            Util.getOscuredColor(mSwitchThumbColorActivated),
                            Util.getOscuredColor(mSwitchThumbColorNormal)
                    }
            );
            mViewSwitch.getTrackDrawable().setTintList(thumbBackgroundStates);*/
        }
    }

    private void setDefault() {
        defTextPadding = getResources().getDimension(R.dimen.txt_default_padding);
        leftPadding = getResources().getDimension(R.dimen.start_end_padding_labeled);
        rightPadding = getResources().getDimension(R.dimen.start_end_padding_labeled);
        topPadding = getResources().getDimension(R.dimen.top_bottom_padding_labeled);
        bottomPadding = getResources().getDimension(R.dimen.top_bottom_padding_labeled);
        labelTextSize = getResources().getDimension(R.dimen.txt_label_labeled_size);
        valueTextSize = getResources().getDimension(R.dimen.txt_value_labeled_size);
        valueTextColor = Color.BLACK;
        labelTextColor = Color.BLACK;
        switcher = false;
        divider = false;
        mSwitchThumbColorNormal = ContextCompat.getColor(getContext(), R.color.white);
        mSwitchThumbColorActivated = WidgetUtils.fetchAccentColor(getContext());
        mSwitchThumbColorDisabled = ContextCompat.getColor(getContext(), R.color.switchColorForeground);
    }

    private void updateUserInteraction() {
        if (userInteraction) {
            mViewDataContainer.setOnClickListener(mClickListener);
            mViewSwitch.setOnCheckedChangeListener(mCheckedChangeListener);
        } else {
            mViewDataContainer.setOnClickListener(null);
            mViewSwitch.setOnCheckedChangeListener(null);
        }
        mViewSwitch.setEnabled(userInteraction);
    }

    public int getValueTextColor() {
        return valueTextColor;
    }

    public void setValueTextColor(int valueTextColor) {
        this.valueTextColor = valueTextColor;
        invalidate();
    }

    public int getLabelTextColor() {
        return labelTextColor;
    }

    public void setLabelTextColor(int labelTextColor) {
        this.labelTextColor = labelTextColor;
        invalidate();
    }

    public float getValueTextSize() {
        return valueTextSize;
    }

    public void setValueTextSize(float valueTextSize) {
        this.valueTextSize = valueTextSize;
        invalidate();
    }

    public float getLabelTextSize() {
        return labelTextSize;
    }

    public void setLabelTextSize(float labelTextSize) {
        this.labelTextSize = labelTextSize;
        invalidate();
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        invalidate();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        invalidate();
    }

    public boolean isDivider() {
        return divider;
    }

    public void setDivider(boolean divider) {
        this.divider = divider;
        invalidate();
    }

    public boolean isSwitcherEnabled() {
        return switcher;
    }

    public void enableSwitcher() {
        this.switcher = true;
        invalidate();
    }

    public void disableSwitcher() {
        this.switcher = false;
        invalidate();
    }

    public void setListener(OnLabeledListener listener) {
        this.mListener = listener;
    }

    public float getLeftPadding() {
        return leftPadding;
    }

    public void setLeftPadding(float leftPadding) {
        this.leftPadding = leftPadding;
        invalidate();
    }

    public float getRightPadding() {
        return rightPadding;
    }

    public void setRightPadding(float rightPadding) {
        this.rightPadding = rightPadding;
        invalidate();
    }

    public float getTopPadding() {
        return topPadding;
    }

    public void setTopPadding(float topPadding) {
        this.topPadding = topPadding;
        invalidate();
    }

    public float getBottomPadding() {
        return bottomPadding;
    }

    public void setBottomPadding(float bottomPadding) {
        this.bottomPadding = bottomPadding;
        invalidate();
    }

    public void setSwitcher(boolean switcher) {
        this.switcher = switcher;
        invalidate();
    }

    public boolean getSwitchStatus() {
        return switchStatus;
    }

    public void enableSwitchStatus() {
        this.switchStatus = true;
        invalidate();
    }

    public void disableSwitchStatus() {
        this.switchStatus = false;
        invalidate();
    }

    public void enableSwitchStatusSilently() {
        this.switchStatus = true;
        OnLabeledListener listener = mListener;
        mListener = null;
        invalidate();
        mListener = listener;
    }

    public void disableSwitchStatusSilently() {
        this.switchStatus = false;
        OnLabeledListener listener = mListener;
        mListener = null;
        invalidate();
        mListener = listener;
    }

    public void enableUserInteraction() {
        userInteraction = true;
        updateUserInteraction();
    }

    public void disableUserInteraction() {
        userInteraction = false;
        updateUserInteraction();
    }

    public int getSwitchThumbColorNormal() {
        return mSwitchThumbColorNormal;
    }

    public void setSwitchThumbColorNormal(int mSwitchThumbColorNormal) {
        this.mSwitchThumbColorNormal = mSwitchThumbColorNormal;
        invalidate();
    }

    public int getSwitchThumbColorActivated() {
        return mSwitchThumbColorActivated;
    }

    public void setSwitchThumbColorActivated(int mSwitchThumbColorActivated) {
        this.mSwitchThumbColorActivated = mSwitchThumbColorActivated;
        invalidate();
    }

    public int getSwitchThumbColorDisabled() {
        return mSwitchThumbColorDisabled;
    }

    public void setSwitchThumbColorDisabled(int mSwitchThumbColorDisabled) {
        this.mSwitchThumbColorDisabled = mSwitchThumbColorDisabled;
        invalidate();
    }

}
