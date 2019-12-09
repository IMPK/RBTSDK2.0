package com.onmobile.rbt.baseline.activities;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onmobile.rbt.baseline.R;
import com.onmobile.rbt.baseline.activities.base.BaseActivity;
import com.onmobile.rbt.baseline.application.BaselineApplication;
import com.onmobile.rbt.baseline.util.AppConstant;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PermissionActivity extends BaseActivity {

    private LinearLayout mPermissionParentLayout;

    @NonNull
    @Override
    protected String initTag() {
        return PermissionActivity.class.getSimpleName();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_permission;
    }

    @Override
    protected void unbindExtras(Intent intent) {

    }

    @Override
    protected void initViews() {
        mPermissionParentLayout = findViewById(R.id.permission_parent_layout);

        makeUI();
    }

    @Override
    protected void setupToolbar() {
        enableToolbarIndicator(R.drawable.ic_arrow_left_white_24dp, R.color.white);
        setToolbarColor(R.color.colorAccent, true);
        setToolbarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        setToolbarTitleColor(R.color.white);
        setToolbarTitle(getString(R.string.label_permissions));
    }

    @Override
    protected void bindViews() {

    }

    @Override
    protected void onPreOnCreate(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onPostOnCreate(@Nullable Bundle savedInstanceState) {

    }



    public void makeUI() {
        ArrayList<String> permissionList = new ArrayList<String>();
        for (String per : AppConstant.getAppPermission()) {
            permissionList.add(per);
        }
        for (String permissionItem : permissionList) {
            LinearLayout permissionItemLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.permission_item_layout, null);
            String title = getPermissionTitle(permissionItem);
            String subTitle = getPermissionSubtitle(permissionItem);
            if (title != null && subTitle != null && !title.isEmpty() && !subTitle.isEmpty()) {
                ((TextView) permissionItemLayout.findViewById(R.id.dialogPermissionTitle))
                        .setText(getPermissionTitle(permissionItem));

                ((TextView) permissionItemLayout.findViewById(R.id.dialogPermissionSubTitle))
                        .setText(getPermissionSubtitle(permissionItem));
                mPermissionParentLayout.addView(permissionItemLayout);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getPermissionTitle(String permissionItem) {
        String result = "";
        switch (permissionItem) {
            case "android.permission.RECEIVE_SMS":
                result = getString(R.string.message_permission_title);
                break;
            case "android.permission.READ_CONTACTS":
                result = getString(R.string.contacts_permission_title);
                break;
            case "android.permission.READ_PHONE_STATE":
                result = getString(R.string.phone_permission_title);
                break;
            case "android.permission.READ_CALENDAR":
                result = getString(R.string.calendar_permission_title);
                break;

        }
        return result;
    }

    private String getPermissionSubtitle(String permissionItem) {
        String result = "";
        switch (permissionItem) {
            case "android.permission.RECEIVE_SMS":
                result = getString(R.string.message_permission_subtitle);
                break;
            case "android.permission.READ_CONTACTS":
                result = getString(R.string.contacts_permission_subtitle);
                break;
            case "android.permission.READ_PHONE_STATE":
                result = getString(R.string.phone_permission_subtitle);
                break;
            case "android.permission.READ_CALENDAR":
                result = getString(R.string.calendar_permission_subtitle);
                break;
        }
        return result;
    }



    @Override
    protected void attachBaseContext(Context newBase) {
        Context context = BaselineApplication.getApplication().getAppLocaleManager().setLocale(newBase);
        super.attachBaseContext(context);
    }
}
