package com.onmobile.rbt.baseline;

/**
 * Created by prateek.khurana on 06-Dec 2019
 */
public class Constant {
    public class packageNameProvided {
        public static final String VODAFONE_PLAY_PACKAGENAME = "com.vodafone.vodafoneplay";
        public static final String VODAFONE_IND_PACKAGENAME = "com.mventus.selfcare.activity";
        public static final String IDEA_PACKAGENAME = "com.ideacellular.myidea";
        public static final String GRAMEEN_PHONE_PACKAGENAME = "com.portonics.mygp";
        public static final String VODA_IDEA_PACKAGENAME = "";
        /*public static final String VODAFONE_IND_PACKAGENAME = "com.onmobile.callertunessdksample";*/

    }

    public static final boolean isPackageVerificationRequired = false;

    public class operator{
        public static final String IDEA_OPERATOR = "idea";
        public static final String VODAFONE_PLAY_OPERATOR = "vodafone play";
        public static final String VODAFONE_IND_OPERATOR = "vodafone";
        public static final String GRAMEEN_PHONE_OPERATOR = "grameen";
        public static final String VODA_IDEA_OPERATOR ="voda_idea";
    }

    public class Intent {
        public static final String EXTRA_MSISDN_SDK = "msisdn";
        public static final String EXTRA_IS_FROM_SELECT_LANGUAGE_SDK = "is_from_select_language";
        public static final String EXTRA_MSISDN_TYPE_SDK = "msisdn_type";
        public static final String EXTRA_OPERATOR_SDK = "operator";
    }


    public class Preferences {
//        public static final String USER_SELECTED_LANGUAGE = "user_selected_language";
//        public static final String USER_SELECTED_LANGUAGE_CODE = "user_selected_language_code";
//        public static final String USER_SELECTED_CHART_GROUP = "user_selected_chart_group";
//        public static final String USER_MSISDN_NUMBER = "user_msisdn_number";
        public static final String USER_MSISDN_TYPE = "user_msisdn_type";
//        public static final String USER_ID = "user_id";
//        public static final String APP_CONFIG = "app_config";
//        public static final String AUTH_TOKEN = "auth_token";
        public static final String OPERATOR_NAME="operator_name";
        public static final String ENCODED_OPERATOR_STRING="encoded_operator_string";
        public static final String SELECTED_OPERATOR = "Selected_Operator";
//        public static final String SELECTED_OPERATOR_LOCALE="selected_operator_locale";
    }

    public static String operatorName(String decodedOpertaorFetched){
        String operatorName = "";
        if(decodedOpertaorFetched.equalsIgnoreCase(Constant.operator.IDEA_OPERATOR)){
            operatorName = Constant.operator.IDEA_OPERATOR;
        }
        else if(decodedOpertaorFetched.equalsIgnoreCase(Constant.operator.VODAFONE_IND_OPERATOR)){
            operatorName = Constant.operator.VODAFONE_IND_OPERATOR;
        }
        else if(decodedOpertaorFetched.equalsIgnoreCase(Constant.operator.VODAFONE_PLAY_OPERATOR)){
            operatorName = Constant.operator.VODAFONE_PLAY_OPERATOR;
        }
        else if(decodedOpertaorFetched.equalsIgnoreCase(operator.GRAMEEN_PHONE_OPERATOR)) {
            operatorName = operator.GRAMEEN_PHONE_OPERATOR;
        }
        else if(decodedOpertaorFetched.equalsIgnoreCase(operator.VODA_IDEA_OPERATOR)) {
            operatorName=operator.VODA_IDEA_OPERATOR;
        }
        return operatorName;
    }
}
