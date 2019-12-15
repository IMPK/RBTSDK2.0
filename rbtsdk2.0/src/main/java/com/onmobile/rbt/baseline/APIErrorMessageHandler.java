package com.onmobile.rbt.baseline;

import android.content.Context;
import android.content.res.Resources;

import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.rbt.baseline.http.api_action.errormodule.ErrorSubCode;


public class APIErrorMessageHandler {

    public static String handleVoltronError(Context context ,ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse != null && errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case NO_NETWORK:
                    s = mContext.getString(R.string.error_handler_mobile_network);
                    break;
                case NETWORK_TIMEOUT:
                    s = mContext.getString(R.string.error_handler_mobile_network);
                    break;
                case INVALID_PIN:

                    s = mContext.getString(R.string.error_handler_invalid_otp);
                    break;
                case BLACKLISTED_USER:
                    s = mContext.getString(R.string.blacklisted_user_warning);
                    break;
                case NO_SIM:
                    s = mContext.getString(R.string.error_message_no_sim_present);
                    break;

                case INVALID_PARAMETER:
                    s = mContext.getString(R.string.error_handler_general_network_error);
                    break;

                case CONTENT_NOT_PURCHASABLE:
                    s = mContext.getString(R.string.error_handler_content_not_purchasable);
                    break;

                case SONG_IS_NOT_PURCHASABLE:
                    s = mContext.getString(R.string.error_handler_content_not_purchasable);
                    break;
                case RBT_ERROR:
                    s = handleRBTErrors(mContext , errorResponse);

                    break;
                case AUTHENTICATION_ERROR:
                    s = mContext.getString(R.string.app_error_general_desc);
                    break;

                case AUTHENTICATION_TOKEN_EXPIRED:
                case authentication_token_expired:
                    s = mContext.getString(R.string.app_error_general_desc);
                    break;
                case NO_CONNECTION_ERROR:
                    s = mContext.getString(R.string.error_handler_mobile_network);
                    break;
                default:
                    s = mContext.getString(R.string.error_handler_general_error);
                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_error);
        }
        return s;
    }

    private static String handleRBTErrors(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String result = "";
        if (errorResponse.getSubCode() != null) {
            switch (errorResponse.getSubCode()) {
                case SUBDOESNTEXIST:
                    result = mContext.getString(R.string.RBT_SUB_DOESNT_EXIST);
                    break;

                case INTERNAL_SERVER_ERROR:
                    result = mContext.getString(R.string.RBT_INTERNAL_SERVER_ERROR);

                    break;
                case SONGNOTFOUND:
                    result = mContext.getString(R.string.RBT_SONG_NOT_FOUND);

                    break;
                case SONGEXPIRED:
                    result = mContext.getString(R.string.RBT_SONG_EXPIRED);

                    break;
                case CATEGORYDOESNTEXIST:
                    result = mContext.getString(R.string.RBT_CATEGORY_DOESNT_EXIST);

                    break;
                case CONTENTEXPIRED:
                    result = mContext.getString(R.string.RBT_CONTENT_EXPIRED);

                    break;
                case SUBALREADYOWNSCONTENT:
                    result = mContext.getString(R.string.RBT_SUB_ALREADY_OWNS_CONTENT);

                    break;
                case OPERATION_NOT_ALLOWED:
                    result = mContext.getString(R.string.RBT_OPERATION_NOT_ALLOWED);

                    break;
                case INVALIDPARAMETER:
                    result = mContext.getString(R.string.RBT_INVALID_PARAMETER);

                    break;
                case DEACTIVATE_SELECTION_ERROR:
                    result = mContext.getString(R.string.RBT_DEACTIVATE_SELECTION_ERROR);

                    break;
                case UNKNOWNERROR:
                    result = mContext.getString(R.string.RBT_UNKNOWN_ERROR);

                    break;
                case INVALIDSHUFFLELISTID:
                    result = mContext.getString(R.string.RBT_INVALID_SHUFFLE_LIST_ID);

                    break;
                case UDPISACTIVE:
                    result = mContext.getString(R.string.RBT_UDP_IS_ACTIVE);

                    break;
                case SUBDOESNTOWNCONTENT:
                    result = mContext.getString(R.string.RBT_SUB_DOESNT_OWN_CONTENT);

                    break;
                case SUBACTPENDING:
                    result = mContext.getString(R.string.RBT_SUB_ACT_PENDING);

                    break;
                case INVALIDSUBSCRIBERID:
                    result = mContext.getString(R.string.RBT_INVALID_SUBSCRIBER_ID);

                    break;
                case OFFERNOTFOUND:
                    result = mContext.getString(R.string.RBT_OFFER_NOT_FOUND);

                    break;
                case SUBALREADYEXIST:
                    result = mContext.getString(R.string.RBT_SUB_ALREADY_EXIST);

                    break;
                case SUBSTATUSCHANGENOTALLOWED:
                    result = mContext.getString(R.string.RBT_SUB_STATUS_CHANGE_NOT_ALLOWED);

                    break;
                case SUBDEACTPENDING:
                    result = mContext.getString(R.string.RBT_SUB_DEACT_PENDING);

                    break;
                case SUBSCRIBERGRACE:
                    result = mContext.getString(R.string.RBT_SUBSCRIBER_GRACE);

                    break;
                case SUBSCRIBERSUSPENDED:
                    result = mContext.getString(R.string.RBT_SUBSCRIBER_SUSPENDED);

                    break;
                case PLAYRULENOTEXIST:
                    result = mContext.getString(R.string.RBT_PLAY_RULE_NOT_EXIST);
                    break;

                case LOCKED_USER:
                    result = mContext.getString(R.string.RBT_LOCKED_USER);
                    break;

                case CONTENT_BLOCKED_LITE_USER:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_LITE_USER);
                    break;

                case CONTENT_BLOCKED_ADRBT:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_ADRBT);
                    break;

                case CONTENT_BLOCKED_PROFILE:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_PROFILE);
                    break;

                case CONTENT_BLOCKED_CORP_USER:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_CORP_USER);
                    break;

                case MAX_UDP_LIMIT_REACHED:
                    result = mContext.getString(R.string.RBT_MAX_UDP_LIMIT_REACHED);
                    break;

                case DOWNLOADS_OVERLIMIT:
                    result = mContext.getString(R.string.download_limit_exceeded_error);
                    break;

                case WALLET_PENDING:
                    result = mContext.getString(R.string.paytm_pending_message);
                    break;

                default:
                    result = mContext.getString(R.string.app_error_general_desc);
                    break;
            }
            return result;
        } else {
            result = mContext.getString(R.string.app_error_general_desc);
            return result;
        }


    }

    public static String getErrorMessageFromErrorCode(Context context,ErrorCode errorCode) {
        Context mContext = context;
        String s = "";
        switch (errorCode) {
            case NO_NETWORK:
                s = mContext.getString(R.string.error_handler_mobile_network);
                break;
            case NETWORK_TIMEOUT:
                s = mContext.getString(R.string.error_handler_mobile_network);
                break;
            case INVALID_PIN:
                s = mContext.getString(R.string.error_handler_invalid_otp);
                break;
            case BLACKLISTED_USER:
                s = mContext.getString(R.string.blacklisted_user_warning);
                break;
            case NO_SIM:
                s = mContext.getString(R.string.error_message_no_sim_present);
                break;

            case INVALID_PARAMETER:
                s = mContext.getString(R.string.app_error_general_desc);
                break;

            case AUTHENTICATION_ERROR:
                s = mContext.getString(R.string.app_error_general_desc);
                break;
            case INTERNAL_SERVER_ERROR:
                s = mContext.getString(R.string.app_error_internal_server_description);
                break;
            default:
                s = mContext.getString(R.string.error_handler_general_network_error);
                break;
        }
        return s;
    }

    public static String getErrorMessage(Context mContext, ErrorResponse errorResponse) {
        String s = "";
        if (errorResponse!=null && errorResponse.getApiKey() != null) {
            switch (errorResponse.getApiKey()) {
                case SONG_PURCHASE_API:
                    s = handleSongPurchaseApiError(mContext , errorResponse);
                    break;
                case ADD_PLAYRULE_API:
                    s = handleAddPlayRuleApiError(mContext , errorResponse);
                    break;
                case DELETE_SONG_API:
                    s = handleDeletePlayRuleApiError(mContext , errorResponse);
                    break;
                case GET_CHARTS_API:
                    s = handleGetChartApiError(mContext , errorResponse);
                    break;
                case SEND_FEEDBACK_API:
                    s = handleSendFeedbackApiError(mContext , errorResponse);
                    break;
                case GET_PLAYRULE_API:
                    s = handlePlayruleListApiError(mContext , errorResponse);
                    break;
                case PURCHASE_COMBO_API:
                    s = handlePurchaseComboApiError(mContext , errorResponse);
                    break;
                case PRICING_API:
                    s = handlePricingApiError(mContext , errorResponse);
                    break;
                case REFERRAL_SERVICE_ERROR:
                    s = handleChildOperationErrorCode(mContext ,errorResponse);
                    break;
                case UDP_API:
                    s = handleUDPErrors(mContext ,errorResponse);
                    break;

                default:
                    s = handleVoltronError(mContext, errorResponse);
                    break;

            }
            return s;
        } else {
            s = handleVoltronError(mContext , errorResponse);
        }
        return s;
    }

    private static String handlePricingApiError(Context context, ErrorResponse errorResponse) {
        Context mContext = context;
        String s;
        switch (errorResponse.getSubCode()) {
            case INVALID_SUBSCRIBER_STATE:
                s = mContext.getString(R.string.PRICING_API_RBT_INVALID_SUBSCRIBER_STATE);
                break;
            default:
                s = mContext.getString(R.string.error_handler_general_network_error);
                break;
        }
        return s;
    }


    private static String handleChildOperationErrorCode(Context context,ErrorResponse errorResponse) {
        String s = null;
        if (errorResponse.getSubCode() != null) {
            switch (errorResponse.getSubCode()) {
                case PARENT_NOT_FOUND:
                    s = context.getString(R.string.PARENT_NOT_REGISTERED);
                    break;
                case INTERNAL_ERROR:
                    s = context.getString(R.string.INTERNAL_SERVER_ERROR);
                    break;
                case CHILD_ALREADY_ADDED:
                    s = context.getString(R.string.CHILD_ALREADY_ADDED_ERROR);
                    break;
                case CHILD_ACTIVE:
                    s = context.getString(R.string.CHILD_ACTIVE_ERROR);
                    break;
                case CHILD_INVALID:
                    s = context.getString(R.string.CHILD_INVALID_ERROR);
                    break;
                case CHILD_ALREADY_REGISTER_AS_PARENT:
                    s = context.getString(R.string.CHILD_ALREADY_ADDED_AS_PARENT_ERROR);
                    break;
                case CHILD_ALREADY_REMOVED:
                    s = context.getString(R.string.CHILD_ALREADY_REMOVED_ERROR);
                    break;
                case CHILD_LIMIT_EXCEEDED:
                    s = context.getString(R.string.CHILD_LIMIT_EXCEEDED);
                    break;

                case CHILD_ALREADY_ADDED_WITH_DIFFERENT_PARENT:
                    s = context.getString(R.string.CHILD_WITH_DIFFERENT_PARENT);
                    break;

                case OPERATOR_NOT_SUPPORTED:
                    s = context.getString(R.string.error_operator_not_supported);
                    break;

                case GIFT_EXPIRED:
                    s = context.getString(R.string.error_gift_expired);
                    break;
                default:
                    s = handleRBTErrors(context, errorResponse);
                    break;

            }
        }
        else {
            s = context.getString(R.string.error_handler_general_network_error);
        }
        return s;
    }


    private static String handleUDPErrors(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String result = "";
        if (errorResponse.getSubCode() != null) {
            switch (errorResponse.getSubCode()) {
                case SUBDOESNTEXIST:
                    // udp
                    result = mContext.getString(R.string.RBT_SUB_DOESNT_EXIST);
                    break;

                case INTERNAL_SERVER_ERROR:
                    result = mContext.getString(R.string.RBT_INTERNAL_SERVER_ERROR);

                    break;
                case SONGNOTFOUND:
                    result = mContext.getString(R.string.RBT_SONG_NOT_FOUND);

                    break;
                case SONGEXPIRED:
                    result = mContext.getString(R.string.RBT_SONG_EXPIRED);

                    break;
                case CATEGORYDOESNTEXIST:
                    result = mContext.getString(R.string.RBT_CATEGORY_DOESNT_EXIST);

                    break;
                case CONTENTEXPIRED:
                    result = mContext.getString(R.string.RBT_CONTENT_EXPIRED);

                    break;
                case SUBALREADYOWNSCONTENT:
                    result = mContext.getString(R.string.RBT_SUB_ALREADY_OWNS_CONTENT);

                    break;
                case OPERATION_NOT_ALLOWED:
                    result = mContext.getString(R.string.RBT_OPERATION_NOT_ALLOWED);

                    break;
                case INVALIDPARAMETER:
                    result = mContext.getString(R.string.RBT_INVALID_PARAMETER);

                    break;
                case DEACTIVATE_SELECTION_ERROR:
                    result = mContext.getString(R.string.RBT_DEACTIVATE_SELECTION_ERROR);

                    break;
                case UNKNOWNERROR:
                    result = mContext.getString(R.string.RBT_UNKNOWN_ERROR);

                    break;
                case INVALIDSHUFFLELISTID:
                    // udp
                    result = mContext.getString(R.string.RBT_INVALID_SHUFFLE_LIST_ID);

                    break;
                case UDPISACTIVE:
                    // udp
                    result = mContext.getString(R.string.RBT_UDP_IS_ACTIVE);

                    break;
                case SUBDOESNTOWNCONTENT:
                    // udp
                    result = mContext.getString(R.string.RBT_SUB_DOESNT_OWN_CONTENT);

                    break;
                case SUBACTPENDING:
                    result = mContext.getString(R.string.RBT_SUB_ACT_PENDING);

                    break;
                case INVALIDSUBSCRIBERID:
                    result = mContext.getString(R.string.RBT_INVALID_SUBSCRIBER_ID);

                    break;
                case OFFERNOTFOUND:
                    result = mContext.getString(R.string.RBT_OFFER_NOT_FOUND);

                    break;
                case SUBALREADYEXIST:
                    result = mContext.getString(R.string.RBT_SUB_ALREADY_EXIST);

                    break;
                case SUBSTATUSCHANGENOTALLOWED:
                    result = mContext.getString(R.string.RBT_SUB_STATUS_CHANGE_NOT_ALLOWED);

                    break;
                case SUBDEACTPENDING:
                    result = mContext.getString(R.string.RBT_SUB_DEACT_PENDING);

                    break;
                case SUBSCRIBERGRACE:
                    result = mContext.getString(R.string.RBT_SUBSCRIBER_GRACE);

                    break;
                case SUBSCRIBERSUSPENDED:
                    result = mContext.getString(R.string.RBT_SUBSCRIBER_SUSPENDED);

                    break;
                case PLAYRULENOTEXIST:
                    result = mContext.getString(R.string.RBT_PLAY_RULE_NOT_EXIST);
                    break;

                case LOCKED_USER:
                    result = mContext.getString(R.string.RBT_LOCKED_USER);
                    break;

                case CONTENT_BLOCKED_LITE_USER:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_LITE_USER);
                    break;

                case CONTENT_BLOCKED_ADRBT:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_ADRBT);
                    break;

                case CONTENT_BLOCKED_PROFILE:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_PROFILE);
                    break;

                case CONTENT_BLOCKED_CORP_USER:
                    result = mContext.getString(R.string.RBT_CONTENT_BLOCKED_CORP_USER);
                    break;

                case MAX_UDP_LIMIT_REACHED:
                    result = mContext.getString(R.string.RBT_MAX_UDP_LIMIT_REACHED);
                    break;

                case MAX_UDP_CONTENT_LIMIT_REACHED:
                    result = mContext.getString(R.string.MAX_UDP_CONTENT_LIMIT_REACHED);
                    break;

                default:
                    mContext.getString(R.string.app_error_general_desc);
                    break;
            }
            return result;
        } else {
            result = mContext.getString(R.string.app_error_general_desc);
            return result;
        }


    }

    private static String handlePurchaseComboApiError(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String s;
        if (errorResponse.getSubCode() != null) {
            switch (errorResponse.getSubCode()) {
                case CONTENT_BLOCKED_LITE_USER:
                    s = mContext.getString(R.string.PURCHASE_COMBO_API_CONTENT_BLOCKED_LITE_USER);
                    break;
                case CONTENT_BLOCKED_ADRBT:
                    s = mContext.getString(R.string.PURCHASE_COMBO_API_CONTENT_BLOCKED_ADRBT);
                    break;
                case CONTENT_BLOCKED_PROFILE:
                    s = mContext.getString(R.string.PURCHASE_COMBO_API_CONTENT_BLOCKED_PROFILE);
                    break;
                case CONTENT_BLOCKED_CORP_USER:
                    s = mContext.getString(R.string.PURCHASE_COMBO_API_CONTENT_BLOCKED_CORP_USER);
                    break;
                case LOCKED_USER:
                    s = mContext.getString(R.string.PURCHASE_COMBO_API_LOCKED_USER);
                    break;
                case CORPORATE_CONTENT_NOT_ALLOWED:
                    s = mContext.getString(R.string.PURCHASE_COMBO_CORPORATE_CONTEN_NOT_ALLOWED);
                    break;
                case GIFT_EXPIRED:
                    s = context.getString(R.string.error_gift_expired);
                    break;
                default:
                    s = handleRBTErrors(mContext , errorResponse);
                    break;
            }
            if (errorResponse.getSubCode().toString().equalsIgnoreCase(ErrorSubCode.DOWNLOAD_MONTHLY_LIMIT_REACHED.toString())) {
                String subcode = errorResponse.getSubCode().toString();
                String limitValue = subcode.substring(subcode.indexOf("%") + 1);
                limitValue = limitValue.substring(0, limitValue.indexOf("%"));
                if (limitValue != null && !limitValue.isEmpty() && limitValue.equalsIgnoreCase("0")) {
                    s = mContext.getString(R.string.download_limit_exceed);
                } else if (limitValue != null && !limitValue.isEmpty() && !limitValue.equalsIgnoreCase("0")) {
                    Resources resources = mContext.getResources();
                    s = String.format(resources.getString(R.string.download_limit_exceed_with_count_left), limitValue);
                } else {
                    s = mContext.getString(R.string.download_limit_exceed);
                }
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);
        }
        return s;
    }

    private static String handlePlayruleListApiError(Context context ,ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case RBT_ERROR:
                    if (errorResponse.getSubCode() != null) {
                        switch (errorResponse.getSubCode()) {
                            case SUBDOESNTEXIST:
                                s = mContext.getString(R.string.GET_PLAYRULE_API_RBT_SUB_DOESNT_EXIST);
                                break;
                            case INVALIDPARAMETER:
                                s = mContext.getString(R.string.GET_PLAYRULE_API_RBT_INVALID_PARAMETER);
                                break;

                            case PLAYRULENOTEXIST:
                                s = mContext.getString(R.string.GET_PLAYRULE_API_RBT_PLAY_RULE_NOT_EXIST);
                                break;

                            case UNKNOWNERROR:
                                s = mContext.getString(R.string.GET_PLAYRULE_API_RBT_UNKNOWN_ERROR);
                                break;

                            default:
                                s = mContext.getString(R.string.error_handler_general_network_error);
                                break;
                        }
                    } else {
                        s = mContext.getString(R.string.error_handler_general_network_error);
                    }
                    break;
                default:
                    s = handleVoltronError(mContext , errorResponse);

                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);

        }
        return s;
    }

    private static String handleSendFeedbackApiError(Context context, ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case GENERAL_ERROR:
                    s = mContext.getString(R.string.feedback_error);
                    break;
                case INTERNAL_SERVER_ERROR:
                    s = mContext.getString(R.string.feedback_error);
                    break;
                case BAD_REQUEST:
                    s = mContext.getString(R.string.feedback_error);
                    break;
                case AUTHENTICATION_ERROR:
                    s = mContext.getString(R.string.feedback_error);
                    break;
                default:
                    s = mContext.getString(R.string.feedback_error);
                    break;
            }
        } else {
            s = handleVoltronError(mContext , errorResponse);

        }
        return s;
    }

    private static String handleGetChartApiError(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case INTERNAL_SERVER_ERROR:
                    s = mContext.getString(R.string.app_error_internal_server_description);
                    break;
                default:
                    s = handleVoltronError(mContext , errorResponse);

                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);

        }
        return s;
    }

    private static String handleDeleteSongFromLibApiError(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case RBT_ERROR:
                    if (errorResponse.getSubCode() != null) {
                        switch (errorResponse.getSubCode()) {
                            case INVALIDPARAMETER:
                                s = mContext.getString(R.string.DELETE_SONG_API_RBT_INVALID_PARAMETER);
                                break;
                            case DEACTIVATE_SELECTION_ERROR:
                                s = mContext.getString(R.string.DELETE_SONG_API_RBT_DEACTIVATE_SELECTION_ERROR);
                                break;
                            case BLOCK_LAST_SONG_DELETION:
                                s = mContext.getString(R.string.error_remove_last_item);
                                break;
                            default:
                                s = mContext.getString(R.string.error_handler_general_network_error);
                                break;
                        }
                    } else {
                        s = mContext.getString(R.string.error_handler_general_network_error);
                    }
                    break;
                default:
                    s = handleVoltronError(mContext , errorResponse);

                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);

        }
        return s;
    }

    private static String handleDeletePlayRuleApiError(Context context, ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case RBT_ERROR:
                    if (errorResponse.getSubCode() != null) {
                        switch (errorResponse.getSubCode()) {
                            case SUBDOESNTEXIST:
                                s = mContext.getString(R.string.REMOVE_PALYRULE_API_RBT_SUB_DOESNT_EXIST);
                                break;
                            case UNKNOWNERROR:
                                s = mContext.getString(R.string.REMOVE_PALYRULE_API_RBT_UNKNOWN_ERROR);
                                break;

                            case PLAYRULENOTEXIST:
                                s = mContext.getString(R.string.REMOVE_PALYRULE_API_RBT_PLAY_RULE_NOT_EXIST);
                                break;

                            case BLOCK_LAST_SONG_DELETION:
                                s = mContext.getString(R.string.error_remove_last_item);
                                break;

                            default:
                                s = mContext.getString(R.string.error_handler_general_network_error);
                                break;
                        }
                    } else {
                        s = mContext.getString(R.string.error_handler_general_network_error);
                    }
                    break;
                default:
                    s = handleVoltronError(mContext , errorResponse);

                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);

        }
        return s;
    }

    private static String handleAddPlayRuleApiError(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case RBT_ERROR:
                    if (errorResponse.getSubCode() != null) {
                        switch (errorResponse.getSubCode()) {
                            case SONGEXPIRED:
                                s = mContext.getString(R.string.ADD_PLAYRULE_API_RBT_SONG_EXPIRED);
                                break;
                            case CATEGORYDOESNTEXIST:
                                s = mContext.getString(R.string.ADD_PLAYRULE_API_RBT_CATEGORY_DOESNT_EXIST);
                                break;
                            case CONTENTEXPIRED:
                                s = mContext.getString(R.string.ADD_PLAYRULE_API_RBT_CONTENT_EXPIRED);
                                break;
                            case SUBALREADYOWNSCONTENT:
                                s = mContext.getString(R.string.ADD_PLAYRULE_API_RBT_SUB_ALREADY_OWNS_CONTENT);
                                break;
                            case SUBDOESNTEXIST:
                                s = mContext.getString(R.string.ADD_PLAYRULE_API_RBT_SUB_DOESNT_EXIST);
                                break;
                            case SUBDOESNTOWNCONTENT:
                                s = mContext.getString(R.string.ADD_PLAYRULE_API_RBT_SUB_DOESNT_OWN_CONTENT);
                                break;

                            default:
                                s = mContext.getString(R.string.error_handler_general_network_error);
                                break;
                        }
                    } else {
                        s = mContext.getString(R.string.error_handler_general_network_error);
                    }
                    break;
                default:
                    s = handleVoltronError(mContext , errorResponse);

                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);

        }
        return s;
    }

    private static String handleSongPurchaseApiError(Context context,ErrorResponse errorResponse) {
        Context mContext = context;
        String s = "";
        if (errorResponse.getCode() != null) {
            switch (errorResponse.getCode()) {
                case RBT_ERROR:
                    if (errorResponse.getSubCode() != null) {
                        switch (errorResponse.getSubCode()) {
                            case SUBDOESNTEXIST:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_SUB_DOESNT_EXIST);

                                break;
                            case SONGNOTFOUND:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_SONG_NOT_FOUND);

                                break;

                            case SONGEXPIRED:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_SONG_EXPIRED);

                                break;
                            case CATEGORYDOESNTEXIST:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_CATEGORY_DOESNT_EXIST);

                                break;
                            case CONTENTEXPIRED:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_CONTENT_EXPIRED);

                                break;
                            case SUBALREADYOWNSCONTENT:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_SUB_ALREADY_OWNS_CONTENT);

                                break;

                            case OPERATION_NOT_ALLOWED:
                                s = mContext.getString(R.string.SONG_PURCHASE_API_RBT_OPERATION_NOT_ALLOWED);

                                break;
                            case CORPORATE_CONTENT_NOT_ALLOWED:
                                s = mContext.getString(R.string.PURCHASE_COMBO_CORPORATE_CONTEN_NOT_ALLOWED);
                                break;
                            default:
                                s = mContext.getString(R.string.error_handler_general_network_error);
                                break;

                        }
                        if (errorResponse.getSubCode().toString().equalsIgnoreCase(ErrorSubCode.DOWNLOAD_MONTHLY_LIMIT_REACHED.toString())) {
                            String subCodeString = errorResponse.getSubCode().toString();
                            String limitValue = subCodeString.substring(subCodeString.indexOf("%") + 1);
                            limitValue = limitValue.substring(0,limitValue.indexOf("%"));
                            if (limitValue != null && !limitValue.isEmpty() && limitValue.equalsIgnoreCase("0")) {
                                s = mContext.getString(R.string.download_limit_exceed);
                            } else if (limitValue != null && !limitValue.isEmpty() && !limitValue.equalsIgnoreCase("0")) {
                                Resources resources = mContext.getResources();
                                s = String.format(resources.getString(R.string.download_limit_exceed_with_count_left), limitValue);
                            } else {
                                s = mContext.getString(R.string.download_limit_exceed);
                            }
                        }
                    } else {
                        s = mContext.getString(R.string.error_handler_general_network_error);
                    }
                    break;
                default:
                    s = handleVoltronError(mContext , errorResponse);
                    break;
            }
        } else {
            s = mContext.getString(R.string.error_handler_general_network_error);
        }
        return s;
    }

    public static String getNoConnectionError(Context context){
        return context.getString(R.string.error_handler_general_network_error);
    }

    public static String getGeneralError(Context context){

        return context.getString(R.string.error_handler_general_error);
    }

    public static String getUserHistoryError(Context context){

        return context.getString(R.string.GET_PLAYRULE_API_RBT_SUB_DOESNT_EXIST);
    }
}
