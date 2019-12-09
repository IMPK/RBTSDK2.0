package com.onmobile.rbt.baseline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by prateek.khurana on 06-Dec 2019
 */
public class EventReceiver extends BroadcastReceiver {
    private static final String EVENT_BROAD_CAST_ACTION = "com.onmo.event.send";
    private static final String DATA_BUNDLE = "bundle";
    private static final String DATA_EVENT_CODE = "eventType";
    private static IRBTSDKEventlistener mRBTSDKEventlistener = null;
    //    listeners
    public static void registerEventListener(Context a_Context, IRBTSDKEventlistener a_RBTSDKEventlistener)  {
        IntentFilter intentFilter = new IntentFilter(EVENT_BROAD_CAST_ACTION);
        EventReceiver eventReceiver = new EventReceiver();
        if(a_RBTSDKEventlistener!=null)
            mRBTSDKEventlistener = a_RBTSDKEventlistener;


        if(a_Context!=null)
            a_Context.registerReceiver(eventReceiver, intentFilter);

    }

    @Override
    public void onReceive(Context arg0, Intent arg1) {

        if(arg1!=null && arg1.getExtras()!=null)
        {

            try{
                Bundle bundle = arg1.getExtras().getBundle(DATA_BUNDLE);
                int eventType = arg1.getExtras().getInt(DATA_EVENT_CODE);

                if(mRBTSDKEventlistener!=null)
                    mRBTSDKEventlistener.onEventListener(eventType, bundle);

                if(eventType ==1) //exit the SDK
                {
                    // Log.d("EventReceiver","onReceive -->:"+eventType);
                    unRegisterEventListener(arg0);
                }
            }
            catch (Exception ex){
                Log.e("EventReceiver","excep -->:"+ex.getMessage());
            }


        }

    }

    public void unRegisterEventListener(Context aContext) {
        try {
            if (aContext != null ) {
                aContext.unregisterReceiver(this);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
