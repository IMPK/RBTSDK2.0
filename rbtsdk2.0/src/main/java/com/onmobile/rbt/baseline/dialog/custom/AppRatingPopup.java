package com.onmobile.rbt.baseline.dialog.custom;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;
import com.onmobile.rbt.baseline.dialog.AppDialog;

import java.util.Date;

/**
 * Created by Shahbaz Akhtar on 21/05/2019.
 *
 * @author Shahbaz Akhtar
 */

public class AppRatingPopup {

    private Context mContext;
    private IMoveNext mListener;

    public AppRatingPopup(Context context, IMoveNext listener) {
        mContext = context;
        mListener = listener;
    }

    public void show() {
        if (!AppConfigurationValues.IsShowRatingDialogFeature()) {
            mListener.moveNext();
        } else {
            boolean isAppRated = SharedPrefProvider.getInstance(mContext).isAppRated();
            boolean isAppRatingRemindLater = SharedPrefProvider.getInstance(mContext).isAppRatingRemindLater();
            boolean isAppRatingNoThanks = SharedPrefProvider.getInstance(mContext).isAppRatingNoThanks();
            boolean isAppRatingShownInSession = SharedPrefProvider.getInstance(mContext).isAppRatingShownInSession();

            if (isAppRated || isAppRatingNoThanks) {
                mListener.moveNext();
            } else if (!isAppRatingShownInSession) {
                SharedPrefProvider.getInstance(mContext).setAppRatingShownInSession(true);
                AppDialog.getAppRatingDialog(mContext, new AppRatingDialog.ActionCallBack() {
                    @Override
                    public void onRateNow() {
                        SharedPrefProvider.getInstance(mContext).setAppRated(true);
                        SharedPrefProvider.getInstance(mContext).setAppRatingRemindLater(false);
                        SharedPrefProvider.getInstance(mContext).setAppRatingNoThanks(false);
                        mListener.moveNext();

                        Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

                        new android.os.Handler().post(() -> {
                            try {
                                mContext.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
                            }
                        });

                    }

                    @Override
                    public void onRemindMeLater() {
                        SharedPrefProvider.getInstance(mContext).setAppRated(false);
                        SharedPrefProvider.getInstance(mContext).setAppRatingRemindLater(true);
                        SharedPrefProvider.getInstance(mContext).setAppRatingNoThanks(false);
                        mListener.moveNext();
                    }

                    @Override
                    public void onNoThanks() {
                        SharedPrefProvider.getInstance(mContext).setAppRated(false);
                        SharedPrefProvider.getInstance(mContext).setAppRatingRemindLater(false);
                        SharedPrefProvider.getInstance(mContext).setAppRatingNoThanks(true);
                        Date noThanksDate = new Date(System.currentTimeMillis());
                        SharedPrefProvider.getInstance(mContext).setAppRatingNoThanksDate(noThanksDate.getTime());
                        mListener.moveNext();

                    }
                });
            }
        }
    }

    public interface IMoveNext {
        void moveNext();
    }
}
