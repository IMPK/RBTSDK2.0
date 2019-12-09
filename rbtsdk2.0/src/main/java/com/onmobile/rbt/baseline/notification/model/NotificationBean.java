package com.onmobile.rbt.baseline.notification.model;

import android.text.TextUtils;


import com.onmobile.rbt.baseline.exception.InsufficientDataException;
import com.onmobile.rbt.baseline.util.FunkyAnnotation;

public class NotificationBean {
    @FunkyAnnotation.AutoProfileNotificationIds
    private int notificationId;
    private String channelId;
    private String channelName;
    private String title;
    private String message;
    private String bannerImage;


    NotificationBean() {

    }

    NotificationBean(@FunkyAnnotation.AutoProfileNotificationIds int notificationId, String channelId, String channelName, String title, String message, String bannerImage) {
        this.notificationId = notificationId;
        this.channelId = channelId;
        this.channelName = channelName;
        this.title = title;
        this.message = message;
        this.bannerImage = bannerImage;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getBannerImage() {
        return bannerImage;
    }


    public static class Builder {
        @FunkyAnnotation.AutoProfileNotificationIds
        private int notificationId;
        private String channelId;
        private String channelName;
        private String title;
        private String message;
        private String bannerImage;

        public Builder() {

        }

        public void setNotificationId(@FunkyAnnotation.AutoProfileNotificationIds int notificationId) {
            this.notificationId = notificationId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setBannerImage(String bannerImage) {
            this.bannerImage = bannerImage;
        }


        public NotificationBean build() throws InsufficientDataException {
            if (TextUtils.isEmpty(title))
                throw new InsufficientDataException("Notification title is empty");
            else if (TextUtils.isEmpty(message))
                throw new InsufficientDataException("Notification title is empty");
            return new NotificationBean(notificationId, channelId, channelName, title, message, bannerImage);
        }
    }
}
