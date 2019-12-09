package com.onmobile.rbt.baseline.model;

import com.onmobile.rbt.baseline.util.FunkyAnnotation;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Created by Shahbaz Akhtar on 10/09/2018.
 *
 * @author Shahbaz Akhtar
 */
public class StackItem {
    private int type;
    private String title, subTitle;
    @DrawableRes
    private int backgroundColor;
    private boolean option;
    @ColorRes
    private int optionColor;
    private int titleColor;
    private Object data;
    private boolean nextButton;
    private String nextButtonLabel;

    private boolean isError;
    private String errorMessage;
    private boolean loading;
    private boolean isNameTune = false;

    public StackItem(@FunkyAnnotation.StackItemType int type, @DrawableRes int backgroundColor, @Nullable Object data) {
        this.type = type;
        this.backgroundColor = backgroundColor;
        this.data = data;
    }

    public StackItem(@FunkyAnnotation.StackItemType int type, @DrawableRes int backgroundColor) {
        this.type = type;
        this.backgroundColor = backgroundColor;
        setLoading(true);
    }

    public int getType() {
        return type;
    }

    public boolean isOptionEnabled() {
        return option;
    }

    public StackItem enableOption(@ColorRes int optionColor) {
        this.option = true;
        this.optionColor = optionColor;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public StackItem setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public StackItem setSubTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void disableOption() {
        this.option = false;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public StackItem setData(Object data) {
        this.data = data;
        setError(false);
        setLoading(false);
        return this;
    }

    public Object getData() {
        return data;
    }

    public int getOptionColor() {
        return optionColor;
    }

    public StackItem setOptionColor(@ColorRes int optionColor) {
        this.optionColor = optionColor;
        return this;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public StackItem setTitleColor(int titleColor) {
        this.titleColor = titleColor;
        return this;
    }

    public boolean isNextButton() {
        return nextButton;
    }

    private void enableNextButton() {
        this.nextButton = true;
    }

    private void disableNextButton() {
        this.nextButton = true;
    }

    public String getNextButtonLabel() {
        return nextButtonLabel;
    }

    public StackItem setNextButtonLabel(@NonNull String nextButtonLabel) {
        enableNextButton();
        this.nextButtonLabel = nextButtonLabel;
        return this;
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        isError = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public boolean isNameTune() {
        return isNameTune;
    }

    public StackItem setNameTune(boolean nameTune) {
        isNameTune = nameTune;
        return this;
    }
}
