package com.onmobile.rbt.baseline.http.api_action.errormodule;

import com.google.gson.annotations.SerializedName;

public enum ErrorCode {
    @SerializedName("NO_CONNECTION_ERROR")
    NO_CONNECTION_ERROR,
    @SerializedName("authentication_token_expired")
    authentication_token_expired,
    @SerializedName("AUTHENTICATION_TOKEN_EXPIRED")AUTHENTICATION_TOKEN_EXPIRED,
    @SerializedName("invalid_parameter")INVALID_PARAMETER,
    @SerializedName("operator_not_supported")OPERATOR_NOT_SUPPORTED,
    @SerializedName("billing_error")BILLING_ERROR,
    @SerializedName("unsupported_operation")UNSUPPORTED_OPERATION,
    @SerializedName("not_found")NOT_FOUND,
    @SerializedName("cds_io_error")CDS_IO_ERROR,
    @SerializedName("authentication_error")AUTHENTICATION_ERROR,
    @SerializedName("persistence_error") PERSISTENCE_ERROR,
    @SerializedName("subscription_error")SUBSCRIPTION_ERROR,
    @SerializedName("general_error")GENERAL_ERROR,
    @SerializedName("invalid_parameters")INVALID_PARAMETERS,
    @SerializedName("invalid_pin")INVALID_PIN,
    @SerializedName("rbt_error")RBT_ERROR,
    @SerializedName("insufficient_funds_error")INSUFFICIENT_FUNDS_ERROR,
    @SerializedName("insufficient_credits_error")INSUFFICIENT_CREDITS_ERROR,
    @SerializedName("purchase_error")PURCHASE_ERROR,
    @SerializedName("ringtone_error")RINGTONE_ERROR,
    @SerializedName("rinbuck_error")RINBUCK_ERROR,
    @SerializedName("cds_error")CDS_ERROR,
    @SerializedName("media_error")MEDIA_ERROR,
    @SerializedName("method_not_allowed")METHOD_NOT_ALLOWED,
    @SerializedName("subscription_expired")SUBSCRIPTION_EXPIRED,
    @SerializedName("subscription_terminated")SUBSCRIPTION_TERMINATED,
    @SerializedName("invalid_subscription_status")INVALID_SUBSCRIPTION_STATUS,
    @SerializedName("credit_is_expired")CREDIT_IS_EXPIRED,
    @SerializedName("invalid_user_agent")INVALID_USER_AGENT,
    @SerializedName("content_already_owned_error")CONTENT_ALREADY_OWNED_ERROR,
    @SerializedName("payment_service_unavailable")PAYMENT_SERVICE_UNAVAILABLE, // below error codes created by Ajit sharma
    @SerializedName("none")NONE,
    @SerializedName("general")GENERAL,
    @SerializedName("invalid_phone_type")INVALID_PHONE_TYPE,
    @SerializedName("no_network")NO_NETWORK,
    @SerializedName("network_timeout")NETWORK_TIMEOUT,
    @SerializedName("internal_server_error")INTERNAL_SERVER_ERROR, // 500x
    @SerializedName("bad_request")BAD_REQUEST, // 400
    @SerializedName("unauthorized")UNAUTHORIZED, // 401
    @SerializedName("forbidden")FORBIDDEN, // 403
    @SerializedName("sim_changed")SIM_CHANGED, NO_SIM, // from constants
    @SerializedName("media_title_not_found_error_code")MEDIA_TITLE_NOT_FOUND_ERROR_CODE,
    @SerializedName("network_not_available")NETWORK_NOT_AVAILABLE,
    @SerializedName("sd_card_full")SD_CARD_FULL,
    @SerializedName("sd_card_read_only")SD_CARD_READ_ONLY,
    @SerializedName("unknown_error")UNKNOWN_ERROR,
    @SerializedName("unknown_record_id")UNKNOWN_RECORD_ID,
    @SerializedName("login_error")LOGIN_ERROR,
    @SerializedName("download_fee_not_paid_error")DOWNLOAD_FEE_NOT_PAID_ERROR,
    @SerializedName("content_not_purchasable")CONTENT_NOT_PURCHASABLE,
    @SerializedName("blacklisted_user")BLACKLISTED_USER,
    @SerializedName("song_is_not_purchasable")SONG_IS_NOT_PURCHASABLE,

    //feedback error

    //Purchase Combo Api
    @SerializedName("content_blocked_lite_user")CONTENT_BLOCKED_LITE_USER, //	You are not allowed to activate this Callertune as this is a premium content
    @SerializedName("content_blocked_adrbt")CONTENT_BLOCKED_ADRBT, //Dear subscriber, you are currently active on Advertisement Callertune pack, Please deactivate by sending  SMS STOP to or call 155223 Tollfree and then try activating using VF CT App.
    @SerializedName("content_blocked_profile")CONTENT_BLOCKED_PROFILE, //Dear subscriber, you are currently active on Profile tune pack, Please deactivate by sending  SMS STOP to or call 155223 Tollfree and then try activating using VF CT App.
    @SerializedName("content_blocked_corp_user")CONTENT_BLOCKED_CORP_USER, //Corporate Caller Tune is active on your cell number, hence song cannot be changed. To stop the Corporate Caller Tune please contact your Relationship Manager.
    @SerializedName("locked_user")LOCKED_USER, // You cannot change your tunes since your Callertunes service is locked. To unlock sms CT UNLOCK to 56789. Rs. 5/SMS
    //Purchase Combo Api

    //ThirdParty API Auto Header  (2.4.2 Retrieve Msisdn Api)
    @SerializedName("ROLE_ID_NOT_LIVE") ROLE_ID_NOT_LIVE,
    @SerializedName("INVALID_CORRELATED_ID") INVALID_CORRELATED_ID,
    @SerializedName("MSISDN_IS_NULL") MSISDN_IS_NULL,

    @SerializedName("unkown")UNKNOWN;



    String value;
    public String getValue() {
        return value.toLowerCase();
    }
}
