package com.onmobile.rbt.baseline.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;


import com.onmobile.rbt.baseline.notification.model.NotificationBean;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    public static final String EXTRA_NOTIFICATION_ID = "KEY:EXTRA_NOTIFICATION_ID";
    private Context mContext;
    private NotificationCompat.Builder mBuilder;
    private NotificationBean mNotificationBean;

    private NotificationHelper(Context context, NotificationCompat.Builder builder, NotificationBean bean) {
        mContext = context;
        mBuilder = builder;
        mNotificationBean = bean;
    }

    public static void cancel(Context context, int notificationId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
    }

    public void show() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(mNotificationBean.getChannelId(), mNotificationBean.getChannelName(), importance);
            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(mNotificationBean.getNotificationId(), mBuilder.build());
    }

/*    static String getChartIdAutoProfile(Context context, String profileTag) {
        AutoProfileTuneRepository autoProfileTuneRepository = new AutoProfileTuneRepository(context);
        Object obj = autoProfileTuneRepository.getRBTObject(profileTag);
        if (obj != null)
            if (obj instanceof LinkedTreeMap) {
                LinkedTreeMap item = (LinkedTreeMap) obj;
                if (item.containsKey("id")) return item.get("id").toString();
            }
        return null;
    }*/

}
