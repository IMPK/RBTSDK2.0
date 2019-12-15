package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by nikita.gurwani on 4/23/2017.
 */

public class NonNetworkCGDTO implements Serializable {


    @SerializedName("supported")
    String supported;

    @SerializedName("message")
    String message;

    @SerializedName("verification_failed_message")
    String verification_failed_message;

    @SerializedName("verification_cancelled_message")
    String verification_cancelled_message;

    @SerializedName("no_sms_permission_message")
    String no_sms_permission_message;

    @SerializedName("sms_message_prefix")
    String sms_message_prefix;

    @SerializedName("sms_autoreply_text")
    String sms_autoreply_text;

    @SerializedName("sms_senderid_prefix")
    String sms_senderid_prefix;

    @SerializedName("no_autoreply_message")
    String no_autoreply_message;

    @SerializedName("sms_timeout_milliseconds")
    String sms_timeout;

    @SerializedName("type")
    String type;

    @SerializedName("message_ios")
    String messageIOS;

    public static final String NO_AUTOREPLY = "no_autoreply";
    public static final String AUTOREPLY = "autoreply";

    public String getSupported() {

        return supported != null ? supported : "false";
    }

    public void setSupported(String supported) {
        this.supported = supported;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVerification_failed_message() {
        return verification_failed_message;
    }

    public void setVerification_failed_message(String verification_failed_message) {
        this.verification_failed_message = verification_failed_message;
    }

    public String getVerification_cancelled_message() {
        return verification_cancelled_message;
    }

    public void setVerification_cancelled_message(String verification_cancelled_message) {
        this.verification_cancelled_message = verification_cancelled_message;
    }

    public String getNo_sms_permission_message() {
        return no_sms_permission_message;
    }

    public void setNo_sms_permission_message(String no_sms_permission_message) {
        this.no_sms_permission_message = no_sms_permission_message;
    }

    public String getSms_message_prefix() {
        return sms_message_prefix;
    }

    public void setSms_message_prefix(String sms_message_prefix) {
        this.sms_message_prefix = sms_message_prefix;
    }

    public String getSms_autoreply_text() {
        return sms_autoreply_text;
    }

    public void setSms_autoreply_text(String sms_autoreply_text) {
        this.sms_autoreply_text = sms_autoreply_text;
    }

    public String getSms_senderid_prefix() {
        return sms_senderid_prefix;
    }

    public void setSms_senderid_prefix(String sms_senderid_prefix) {
        this.sms_senderid_prefix = sms_senderid_prefix;
    }

    public String getNo_autoreply_message() {
        return no_autoreply_message;
    }

    public void setNo_autoreply_message(String no_autoreply_message) {
        this.no_autoreply_message = no_autoreply_message;
    }

    public String getSms_timeout() {
        return sms_timeout;
    }

    public void setSms_timeout(String sms_timeout) {
        this.sms_timeout = sms_timeout;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessageIOS() {
        return messageIOS;
    }

    public void setMessageIOS(String messageIOS) {
        this.messageIOS = messageIOS;
    }
}
