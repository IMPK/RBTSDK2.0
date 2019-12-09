package com.onmobile.rbt.baseline.bottomsheet.action;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.onmobile.rbt.baseline.util.Util;
import com.onmobile.rbt.baseline.util.WidgetUtils;

import androidx.annotation.DrawableRes;

/**
 * Created by Shahbaz Akhtar on 30-11-2018.
 */
public class BSLAModel {
    private int id;
    private String label;
    private Drawable icon;

    public BSLAModel(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public BSLAModel(int id, String label, Drawable icon) {
        this.id = id;
        this.label = label;
        this.icon = icon;
    }

    public BSLAModel(int id, String label, @DrawableRes int icon, Context context) {
        this.id = id;
        this.label = label;
        this.icon = WidgetUtils.getDrawable(icon, context);
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public void setIcon(@DrawableRes int icon, Context context) {
        this.icon = WidgetUtils.getDrawable(icon, context);
    }
}
