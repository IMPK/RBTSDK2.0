package com.onmobile.baseline.http;

import android.app.Application;
import android.util.Log;

import com.onmobile.baseline.http.api_action.errormodule.ErrorCode;
import com.onmobile.baseline.http.api_action.errormodule.ErrorResponse;
import com.onmobile.baseline.http.api_action.errormodule.ErrorSubCode;
import com.onmobile.baseline.http.basecallback.BaselineCallback;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Nikita Gurwani .
 */
public class Configuration implements Serializable {

    static String version;
    static String response_type;
    static String API_domain;
    static String store_id;
    static String SERVER_NAME_CATALOG;
    static String SERVER_NAME_STORE;
    static String SERVER_NAME_NOTIFICATION;
    static String api_end_point_catalog;
    static String api_end_point_store;
    static String push_notification_host;
    static String network_utility_host;
    static String authentication_api;
    static String feedback_host;
    static String firebase_dynamic_link_domain;
    static String cutrbt_host_link;
    static String certificate_pinning_key;
    static String app_local_encryption_secret;
    static String dummy_purchase_api;
    static String autoheader_api_end_point;

    public static final String ENCODING_ID = "34";
    public static final String CURRENCY = "USD";
    public static final Double QUOTED_PRICE = 0.00;
    public static final String RETAIL_PRICE_ID = "1";
    public static int scheduleID = 1;
    public static String scheduleType = "Default";

    public void loadConfiguration(Application app, BaselineCallback<String> errorMsg) throws IllegalStateException {
        Properties applicationProperties = new Properties();
        try {
            applicationProperties.load(app.getAssets().open("application.properties"));
            if(errorMsg!=null){
                errorMsg.success("success");
            }
        } catch (IOException e) {
            Log.e(" ", "Could not access application properties.", e);
            //throw new IllegalStateException("Could not access application properties.", e);
            if(errorMsg!=null){
                errorMsg.failure(handleGeneralError(e));
            }
        }
        try {
            loadFromProperties(app, applicationProperties);
        } catch (Exception e) {
            //throw new IllegalStateException("Unable to load configuration.", e);
            if(errorMsg!=null){
                errorMsg.failure(handleGeneralError(e));
            }
        }


    }

    public ErrorResponse handleGeneralError(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        errorResponse.setCode(ErrorCode.GENERAL_ERROR);
        errorResponse.setSubCode(ErrorSubCode.GENERAL_ERROR);
        errorResponse.setDescription(ex.getMessage());
        return errorResponse;
    }



    @Target(ElementType.FIELD)
    public @interface Ignored {
        String info() default "";
    }

    @Target(ElementType.FIELD)
    public @interface Required {
        String info() default "";
    }

    public static class MissingConfigurationException extends Exception {

        private static final long serialVersionUID = 1L;

        public MissingConfigurationException() {
            super();
        }

        public MissingConfigurationException(String detailMessage, Throwable throwable) {
            super(detailMessage, throwable);
        }

        public MissingConfigurationException(String detailMessage) {
            super(detailMessage);
        }

        public MissingConfigurationException(Throwable throwable) {
            super(throwable);
        }

    }

    public static void loadFromProperties(Application app, Properties properties) throws IllegalArgumentException, IllegalAccessException, MissingConfigurationException {
        Log.d("sTag", "Loading configuration from properties.");

        // TODO should check for set properties that do not match any fields
        for (Field field : Configuration.class.getDeclaredFields()) {
            // check if the field is ignored
            if (field.getAnnotation(Ignored.class) != null) {
                // skip this field
                continue;
            }
            // not ignored, try to lookup the field
            if (properties.containsKey(field.getName())) {
                String s = properties.get(field.getName()).toString();

                // TODO these should fail if parsing fails
                if (field.getType().equals(Integer.class)) {
                    if (isNumeric(s)) {
                        field.set(null, Integer.valueOf(s));
                    }
                } else if (field.getType().equals(Integer.TYPE) || field.getType().equals(Integer.class)) {
                    if (isNumeric(s)) {
                        field.setInt(null, Integer.parseInt(s));
                    }
                } else if (field.getType().equals(Long.TYPE)) {
                    if (isLong(s)) {
                        field.setLong(null, Long.parseLong(s));
                    }
                } else if (field.getType().equals(Boolean.TYPE)) {
                    if (isBoolean(s)) {
                        field.setBoolean(null, Boolean.parseBoolean(s));
                    }
                } else if (field.getType().equals(Double.TYPE)) {
                    if (isDouble(s)) {
                        field.setDouble(null, Double.parseDouble(s));
                    }
                } else if (field.getType().equals(BigDecimal.class)) {
                    field.set(null, new BigDecimal(s));
                } else if (Set.class.isAssignableFrom(field.getType())) {
                    try {
                        Log.d("sTag", field.getName() + " is set: " + field.getGenericType().toString());
                    } catch (Exception e) {
                        // just a problem logging
                    }
                    Type[] actualTypeArguments = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                    if (actualTypeArguments.length == 1) {
                        Class<?> type = (Class<?>) actualTypeArguments[0];
                        if (type.isEnum()) {
                            handleEnumSet(field, s, type);
                        } else {
                        }
                    } else {
                        // handle failure, for now matching the quiet fail of
                        // the rest of this method
                    }
                } else if (field.getType().isEnum()) {
                    handleEnum(field, s);
                } else {
                    // TODO this is poor, just check the field class...
                    // If the string starts with C; then it is a collection of
                    // key/value pairs separated by ;
                    if (s.startsWith("C;")) {
                        // Trim C;
                        s = s.substring(2);

                        String[] collection = s.split(";");
                        Map<String, String> tmp = new HashMap<String, String>();
                        for (String val : collection) {
                            String[] keyVal = val.split("=");
                            if (keyVal.length >= 2) {
                                tmp.put(keyVal[0], keyVal[1]);
                            }
                        }
                        field.set(null, tmp);
                    } else {
                        // TODO explicitly check that it is string
                        field.set(null, s);
                    }
                }
            } else if (field.getAnnotation(Required.class) != null) {
                // could not find a required field
                throw new MissingConfigurationException("Required configuration property '" + field.getName() + "' is missing.");
            }
        }

        // trim available modes if this in not a phone capable device


    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        Configuration.version = version;
    }

    public static String getResponse_type() {
        return response_type;
    }

    public static void setResponse_type(String response_type) {
        Configuration.response_type = response_type;
    }

    public static String getApp_local_encryption_secret() {
        return app_local_encryption_secret;
    }

    public static void setApp_local_encryption_secret(String app_local_encryption_secret) {
        Configuration.app_local_encryption_secret = app_local_encryption_secret;
    }

    public static String getAPI_domain() {
        return API_domain;
    }

    public static void setAPI_domain(String API_domain) {
        Configuration.API_domain = API_domain;
    }

    public static String getStore_id() {
        return store_id;
    }

    public static void setStore_id(String store_id) {
        Configuration.store_id = store_id;
    }

    public static String getServerNameCatalog() {
        return SERVER_NAME_CATALOG;
    }

    public static void setServerNameCatalog(String serverNameCatalog) {
        SERVER_NAME_CATALOG = serverNameCatalog;
    }

    public static String getServerNameStore() {
        return SERVER_NAME_STORE;
    }

    public static void setServerNameStore(String serverNameStore) {
        SERVER_NAME_STORE = serverNameStore;
    }

    public static String getServerNameNotification() {
        return SERVER_NAME_NOTIFICATION;
    }

    public static void setServerNameNotification(String serverNameNotification) {
        SERVER_NAME_NOTIFICATION = serverNameNotification;
    }

    public static String getApi_end_point_catalog() {
        return api_end_point_catalog;
    }

    public static void setApi_end_point_catalog(String api_end_point_catalog) {
        Configuration.api_end_point_catalog = api_end_point_catalog;
    }

    public static String getApi_end_point_store() {
        return api_end_point_store;
    }

    public static void setApi_end_point_store(String api_end_point_store) {
        Configuration.api_end_point_store = api_end_point_store;
    }

    public static String getAutoheader_api_end_point() {
        return autoheader_api_end_point;
    }

    public static void setAutoheader_api_end_point(String autoheader_api_end_point) {
        Configuration.autoheader_api_end_point = autoheader_api_end_point;
    }

    public static String getFirebase_dynamic_link_domain() {
        return firebase_dynamic_link_domain;
    }

    public static void setFirebase_dynamic_link_domain(String firebase_dynamic_link_domain) {
        Configuration.firebase_dynamic_link_domain = firebase_dynamic_link_domain;
    }

    public static String getPush_notification_host() {
        return push_notification_host;
    }

    public static void setPush_notification_host(String push_notification_host) {
        Configuration.push_notification_host = push_notification_host;
    }

    public static String getCertificate_pinning_key() {
        return certificate_pinning_key;
    }

    public static void setCertificate_pinning_key(String certificate_pinning_key) {
        Configuration.certificate_pinning_key = certificate_pinning_key;
    }

    public static String getNetwork_utility_host() {
        return network_utility_host;
    }

    public static void setNetwork_utility_host(String network_utility_host) {
        Configuration.network_utility_host = network_utility_host;
    }

    public static String getDummy_purchase_api() {
        return dummy_purchase_api;
    }

    public static void setDummy_purchase_api(String dummy_purchase_api) {
        Configuration.dummy_purchase_api = dummy_purchase_api;
    }

    public static String getAuthentication_api() {
        return authentication_api;
    }

    public static void setAuthentication_api(String authentication_api) {
        Configuration.authentication_api = authentication_api;
    }

    public static String getCutrbt_host_link() {
        return cutrbt_host_link;
    }

    public static void setCutrbt_host_link(String cutrbt_host_link) {
        Configuration.cutrbt_host_link = cutrbt_host_link;
    }

    public static String getFeedback_host() {
        return feedback_host;
    }

    public static void setFeedback_host(String feedback_host) {
        Configuration.feedback_host = feedback_host;
    }

    private static void handleEnum(Field field, String s) throws IllegalAccessException {
        @SuppressWarnings("rawtypes") Enum enumValue = handleEnum(field.getType(), s);

        Log.d("sTag", field.getName() + " is enum, set to: " + enumValue.name());
        field.set(null, enumValue);
    }

    @SuppressWarnings("rawtypes")
    protected static Enum handleEnum(Class<?> type, String s) {
        @SuppressWarnings({"unchecked"}) Class<Enum> enumClass = (Class<Enum>) type;

        Enum[] enums = enumClass.getEnumConstants();

        @SuppressWarnings({"unchecked", "static-access"}) Enum enumValue = enums[0].valueOf(enumClass, s);
        return enumValue;
    }

    @SuppressWarnings("unchecked")
    protected static void handleEnumSet(Field field, String s, Class<?> type) throws IllegalAccessException {
        @SuppressWarnings({"rawtypes"}) Class<Enum> enumClass = (Class<Enum>) type;
        @SuppressWarnings({"rawtypes"}) EnumSet set = EnumSet.noneOf(enumClass);
        String[] items = s.split(",");
        for (String item : items) {
            set.add(handleEnum(type, item));
        }
        Log.d("sTag", field.getName() + " is enum set, set to: " + set.toString());
        field.set(null, set);
    }

    // Is numeric
    public static boolean isLong(String val) {
        try {
            Long.parseLong(val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Is double
    public static boolean isDouble(String val) {
        try {
            Double.parseDouble(val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumeric(String val) {
        try {
            Integer.parseInt(val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Is boolean
    public static boolean isBoolean(String val) {
        try {
            Boolean.parseBoolean(val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
