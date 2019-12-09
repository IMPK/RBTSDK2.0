package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.graphics.Typeface;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class FontUtils {

    public static void setRegularFont(Context context, View view){
        setFont(context, view, AppConstant.Font.FONT_REGULAR);
    }

    public static void setLightFont(Context context, View view){
        setFont(context, view,  AppConstant.Font.FONT_LIGHT);
    }

    public static void setMediumFont(Context context, View view){
        setFont(context, view,  AppConstant.Font.FONT_MEDIUM);
    }

    public static void setBoldFont(Context context, View view){
        setFont(context, view,  AppConstant.Font.FONT_BOLD);
    }

    private static void setFont(Context context, View view, String fontName){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/"  + fontName);
        if(view instanceof AutoCompleteTextView){
            ((AutoCompleteTextView)view).setIncludeFontPadding(true);
            ((AutoCompleteTextView)view).setTypeface(typeface);
            ((AutoCompleteTextView)view).setIncludeFontPadding(true);
        }

        else if(view instanceof TextView){
            ((TextView)view).setIncludeFontPadding(true);
            ((TextView)view).setTypeface(typeface);
            ((TextView)view).setIncludeFontPadding(true);
        }
        else if(view instanceof EditText){
            ((EditText)view).setIncludeFontPadding(true);
            ((EditText)view).setTypeface(typeface);
            ((EditText)view).setTypeface(typeface);
        }
        else if(view instanceof TextInputLayout){
            ((TextInputLayout)view).setTypeface(typeface);
        }
    }


}
