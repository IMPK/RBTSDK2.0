package com.onmobile.rbt.baseline.util.cut.ruler;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.onmobile.rbt.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.rbt.baseline.http.httpmodulemanagers.HttpModuleMethodManager;
import com.onmobile.rbt.baseline.BuildConfig;
import com.onmobile.rbt.baseline.application.SharedPrefProvider;
import com.onmobile.rbt.baseline.util.AppConstant;
import com.onmobile.rbt.baseline.util.EncoderHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Rémi Cholley on 29/08/2017.
 * Comment
 */

public class PreviewUrlGeneration {

    private static final String TAG = "UrlGeneration";

    public static final int FT_PREVIEW = 0;
    public final static int RINGTONE = 0;
    public final static int NOTIFICATION = 1;


    public String generateUrl(Context context, int ringtoneOrNotification, int previewOrDownload, RingBackToneDTO track, int startTime, int endTime, String firebaseUid) {


        TimeZone timeZone = TimeZone.getTimeZone("GMT");

        Calendar cal = Calendar.getInstance(timeZone);

        //If there is a time difference between device time and server time, add it to the current time
        if (SharedPrefProvider.getInstance(context).getPrefsServerTimeDiff() != null) {
            cal.setTimeInMillis(System.currentTimeMillis() - Long.parseLong(SharedPrefProvider.getInstance(context).getPrefsServerTimeDiff()));
        } else {
            //else no need to add dif
            cal.setTimeInMillis(System.currentTimeMillis());
        }

        long day = cal.get(Calendar.DAY_OF_YEAR);
        long hour = cal.get(Calendar.HOUR_OF_DAY);
        long minute = cal.get(Calendar.MINUTE);
        long second = cal.get(Calendar.SECOND);


        String dateString = Long.toString(((day - 1) * 86400) + ((hour) * 3600) + ((minute) * 60) + second);

        String number = "000000000000";
        String model = "Android";
        try {
            model = Build.MODEL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        List<String> param = new ArrayList<>();

        String madeRefId = track.getFullTrack().getMadeReferenceId();
        if(madeRefId.contains(".")){
            String [] madeRef = madeRefId.split("\\.");
            madeRefId = madeRef[0];
        }


        String trackId = madeRefId;
        String fileExt = "." + "mp3";

        param.add(dateString);
        param.add(trackId);
        param.add(Integer.toString(startTime));
        param.add(number);
        param.add("Onm0bile");
        param.add(model);
        param.add(BuildConfig.VERSION_NAME);
        String duration = String.valueOf(AppConstant.DEFAULT_CUT_RINGTONE_TIME);
        if (ringtoneOrNotification == NOTIFICATION) {
            duration = String.valueOf(AppConstant.DEFAULT_CUT_NOTIFICATION_TIME);
        }
        param.add(duration);
        String clipInfo = "baseline2.0Android" + "#" + "ringtone" + "#" + track.getAlbumName() + "#" + track.getPrimaryArtistName() + "#" + track.getLabelName();
        param.add(clipInfo);


        String generatedCode = null;
        try {
            generatedCode = com.onmobile.generatecodes.GenerateCode.urlEncrypter(EncoderHelper.getInputStream(context), param);
            String p = null;
            p = HttpModuleMethodManager.getCutRBTEndPoint() ;
            if(!TextUtils.isEmpty(track.getFullTrack().getMadeContext())){
                p= p.concat(track.getFullTrack().getMadeContext() + "/");
            }

            if (p == null || p.isEmpty()) {
                return null;
            }

            return p + generatedCode.concat(fileExt);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
