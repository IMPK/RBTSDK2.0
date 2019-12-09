package com.onmobile.rbt.baseline.reciever;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.util.List;

public class TelephonyInfo {
    private static TelephonyInfo telephonyInfo;
    private String imsiSIM1;
    private String imsiSIM2;
    private boolean isSIM1Ready;
    private boolean isSIM2Ready;

    public String getImsiSIM1() {
        return imsiSIM1;
    }

    public String getImsiSIM2() {
        return imsiSIM2;
    }


    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }


    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }



    private TelephonyInfo() {
    }

    @SuppressLint("MissingPermission")
    public static TelephonyInfo getInstance(Context context){

        if(telephonyInfo == null) {

            telephonyInfo = new TelephonyInfo();

            TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

            telephonyInfo.imsiSIM1 = telephonyManager.getDeviceId();
            telephonyInfo.imsiSIM2 = null;

            try {
                telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceIdGemini", 0);
                telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceIdGemini", 1);
            } catch (GeminiMethodNotFoundException e) {
                e.printStackTrace();

                try {
                    telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceId", 0);
                    telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceId", 1);
                } catch (GeminiMethodNotFoundException e1) {
                    //Call here for next manufacturer's predicted method name if you wish
                    e1.printStackTrace();
                    try {
                        telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDeviceIdDs", 0);
                        telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDeviceIdDs", 1);
                    }catch (GeminiMethodNotFoundException e2) {
                        e2.printStackTrace();
                        try {
                            telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 0);
                            telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 1);
                        }catch (GeminiMethodNotFoundException e3) {
                            e3.printStackTrace();
                            try {
                                telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getDefault", 0);
                                telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getDefault", 1);
                            } catch (GeminiMethodNotFoundException e4) {
                                e4.printStackTrace();
                                return null;
                            }
                        }
                    }
                }
            }

            telephonyInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
            telephonyInfo.isSIM2Ready = false;

            try {
                telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimStateGemini", 0);
                telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimStateGemini", 1);
            } catch (GeminiMethodNotFoundException e) {

                e.printStackTrace();

                try{
                    telephonyInfo.imsiSIM1 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 0);
                    telephonyInfo.imsiSIM2 = getDeviceIdBySlot(context, "getSimSerialNumberGemini", 1);
                }catch (GeminiMethodNotFoundException e2) {
                    e2.printStackTrace();
                    try {
                        telephonyInfo.isSIM1Ready = getSIMStateBySlot(context, "getSimState", 0);
                        telephonyInfo.isSIM2Ready = getSIMStateBySlot(context, "getSimState", 1);
                    } catch (GeminiMethodNotFoundException e1) {
                        //Call here for next manufacturer's predicted method name if you wish
                        e1.printStackTrace();
                    }
                }
            }
        }

        return telephonyInfo;
    }

    public static String getDeviceIdBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        String imsi = null;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);

            if(ob_phone != null){
                imsi = ob_phone.toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return imsi;
    }

    public static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if(ob_phone != null){
                int simState = Integer.parseInt(ob_phone.toString());
                if(simState == TelephonyManager.SIM_STATE_READY){
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }


    private static class GeminiMethodNotFoundException extends Exception {

        private static final long serialVersionUID = -996812356902545308L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }

    public static String getDeviceID(Context context , int slot){
        if(telephonyInfo == null) {

            telephonyInfo = new TelephonyInfo();
        }
        try {
            return getDeviceIdBySlot(context,"getDeviceIdGemini",slot);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();

            try {
                return getDeviceIdBySlot(context, "getDeviceId", slot);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                e1.printStackTrace();
                try {
                    return getDeviceIdBySlot(context, "getDeviceIdDs", slot);
                }catch (GeminiMethodNotFoundException e2) {
                    e2.printStackTrace();
                    try {
                        return getDeviceIdBySlot(context, "getSimSerialNumberGemini", slot);
                    } catch (GeminiMethodNotFoundException e3) {
                        e3.printStackTrace();
                        try {
                            return getDeviceIdBySlot(context, "getDefault", slot);
                        } catch (GeminiMethodNotFoundException e4) {
                            e4.printStackTrace();
                            return null;
                        }
                    }
                }
            }
        }
    }

    public static boolean isDualSim(Context mContext) {

        boolean isDualSIM = false;
        try {
            TelephonyInfo telephonyInfo = TelephonyInfo.getInstance(mContext);

            String imsiSIM1 = telephonyInfo.getImsiSIM1();
            String imsiSIM2 = telephonyInfo.getImsiSIM2();

            boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
            boolean isSIM2Ready = telephonyInfo.isSIM2Ready();

            isDualSIM = imsiSIM2 != null && imsiSIM1 != null && !imsiSIM1.trim().equals(imsiSIM2.trim());


        } catch (Exception e) {
            e.printStackTrace();
        }
        return isDualSIM;

    }

    public static boolean isDeviceIsDualSim(Context mContext) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            SubscriptionManager subscriptionManager;
            subscriptionManager = (SubscriptionManager) mContext.
                    getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
            List sil = subscriptionManager.getActiveSubscriptionInfoList();
            return sil.size() > 1;
        } else {
            return isDualSim(mContext);
        }
    }
}
