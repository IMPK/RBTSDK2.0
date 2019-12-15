package com.onmobile.rbt.baseline.http.api_action.dtos.appconfigdtos;

import android.text.TextUtils;
import android.util.Patterns;

import com.google.gson.internal.LinkedTreeMap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.onmobile.rbt.baseline.http.retrofit_io.APIRequestParameters;

/**
 * Created by Nikita Gurwani .
 */
public class RegistrationConfigManipulator extends AppConfigDataManipulator {

    public static boolean validateMsisdn(String msisdn){
        int min_length = -1;
        if (hasSpecialChars(msisdn)) {
            return false;
        }else{
            min_length = getMinMsisdnLength();
            int max_length = getMaxMsisdnLength();
            int input_number_length = 0;
            if (msisdn != null) {
                input_number_length = msisdn.trim().length();
            }
            if (input_number_length >= min_length && input_number_length <= max_length) {
                if (!TextUtils.isEmpty(msisdn)) {
                    return Patterns.PHONE.matcher(msisdn).matches();
                }
            }
        }
        return false;
    }

    private static boolean hasSpecialChars(String msisdn){
        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(msisdn);
        boolean b = m.find();
        return b;
    }

    public static int getMaxMsisdnLength() {
        int msisdn_length = -1;
        if (getAppConfigParentDTO()!= null) {
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            if (appConfigDTO != null) {
                LinkedTreeMap<String, String > registrationDTO =( LinkedTreeMap<String, String >) appConfigDTO.getRegistrationDTO();
                if (registrationDTO != null) {
                    if (Integer.parseInt(registrationDTO.get(APIRequestParameters.APIParameter.MSISDN_LENGTH) )== -1) {
                        msisdn_length = getMinMsisdnLength();
                    } else {
                        msisdn_length = Integer.parseInt(registrationDTO.get(APIRequestParameters.APIParameter.MAX_MSISDN_LENGTH));
                    }
                }

                }
            }
            return msisdn_length;
        }


    public static int getMinMsisdnLength() {
        String msisdn_length = "-1";
        if (getAppConfigParentDTO()!= null) {
            AppConfigDTO appConfigDTO = getAppConfigParentDTO().getAppConfigDTO();
            if (appConfigDTO != null) {
                LinkedTreeMap<String, String > registrationDTO =( LinkedTreeMap<String, String >) appConfigDTO.getRegistrationDTO();
                if (registrationDTO != null) {
                    msisdn_length = registrationDTO.get(APIRequestParameters.APIParameter.MSISDN_LENGTH);
                }
            }
        }

        return Integer.parseInt(msisdn_length);
    }

    public static int getDefaultOTPLength() {
        String length = "6";
        if(AppConfigDataManipulator.getAppConfigParentDTO()!=null &&
                AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO()!=null &&
                AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getRegistrationDTO()!=null){

            LinkedTreeMap<String, String> registration = (LinkedTreeMap<String, String>) AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getRegistrationDTO();
            try {
                length = registration.get(APIRequestParameters.APIParameter.OTP_LENGTH_NEW);
            }catch (Exception ex){
                length = registration.get(APIRequestParameters.APIParameter.OTP_LENGTH);
            }
        }

        return Integer.parseInt(length);
    }

    public static int getResendOTPLimit() {
        String length = "5";
        if(AppConfigDataManipulator.getAppConfigParentDTO()!=null &&
                AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO()!=null &&
                AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getRegistrationDTO()!=null){

            LinkedTreeMap<String, String> registration = (LinkedTreeMap<String, String>) AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getRegistrationDTO();
            try {
                length = registration.get(APIRequestParameters.APIParameter.OTP_RESEND_LIMIT);
            }catch (Exception ex){
                length = "5";
            }
        }
        if(length!=null) {
            return Integer.parseInt(length);
        }else{
            return Integer.parseInt("5");
        }
    }

    public static String getOTPSenderId() {
        if (AppConfigDataManipulator.getAppConfigParentDTO() != null &&
                AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO() != null &&
                AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getRegistrationDTO() != null) {

            LinkedTreeMap<String, String> registration = (LinkedTreeMap<String, String>) AppConfigDataManipulator.getAppConfigParentDTO().getAppConfigDTO().getRegistrationDTO();
            try {
                return registration.get(APIRequestParameters.APIParameter.OTP_SENDER_ID);
            } catch (Exception ignored) { }
        }
        return "";
    }
}
