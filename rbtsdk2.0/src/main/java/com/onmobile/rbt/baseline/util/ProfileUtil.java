package com.onmobile.rbt.baseline.util;

import android.content.Context;
import android.text.TextUtils;

import com.onmobile.baseline.http.api_action.dtos.RingBackToneDTO;
import com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ScheduleDTO;
import com.onmobile.rbt.baseline.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class ProfileUtil {

    private static ProfileUtil instance;
    //private ArrayList<LanguageDTO> mLanguageList;

    public static ProfileUtil getInstance() {
        if (instance == null) {
            instance = new ProfileUtil();
        }
        return instance;
    }

    public ProfileUtil() {
//        AppConfigDTO appConfigDTO = MultiProcessPreferenceProvider.getAppConfigPreference(Constant.Preferences.APP_CONFIG);
//        if(appConfigDTO != null && appConfigDTO.getCatalogLanguageDTOArrayList() != null) {
//            mLanguageList = appConfigDTO.getCatalogLanguageDTOArrayList();
//        }
    }

    public HashMap<String, List<RingBackToneDTO>> getVoiceLanguageRingBackDTO(List<RingBackToneDTO> itemsList) {
        HashMap<String, List<RingBackToneDTO>> voiceLangMap = new HashMap<>();
        for (int i = 0; i < itemsList.size(); i++) {
            String voice = itemsList.get(i).getPrimaryArtistName();
            RingBackToneDTO ringbackDTO = itemsList.get(i);
            if (!voiceLangMap.containsKey(voice)) {
                voiceLangMap.put(voice, new ArrayList<RingBackToneDTO>());
            }
            voiceLangMap.get(voice).add(ringbackDTO);
        }
        return voiceLangMap;
    }

//    public Item getFirstRingbackToneDto(Context context, List<Item> itemsList) {
//        DatabaseFactory databaseFactory = DatabaseFactory.getInstance(context);
//        Item mDto;
//        mDto = itemsList.get(0);
//        for(Item item : itemsList){
//            boolean isPlayRuleExist = databaseFactory.isPlayRuleExistForSongId(item.getId());
//            if(isPlayRuleExist){
//                mDto = item;
//                break;
//            }
//        }
//        return mDto;
//`
//    }

    public List<String> getVoiceKeys(HashMap<String, List<RingBackToneDTO>> map) {
        return new ArrayList<>(map.keySet());
    }

    public List<RingBackToneDTO> getRingbackToneList(HashMap<String, List<RingBackToneDTO>> map, String voiceKey) {
        List<RingBackToneDTO> ringbackDTOs = null;
        for (String key : map.keySet()) {
            if (!TextUtils.isEmpty(key) && key.equals(voiceKey)) {
                ringbackDTOs = map.get(voiceKey);
            }
        }
        return ringbackDTOs;
    }

    public String toDaysHoursAndMinutesServerFormat(long timeInMilliSeconds) {
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;
        String time = days + ":" + hours + ":" + minutes + ":" + seconds;
        return totalDMH(days, hours, minutes);
    }

    public String totalDMH(long days, long hours, long minutes) {
        String duration = "";
        duration = days + "DT" + hours + "H" + minutes + "M";
        return "P" + duration + "0S";

    }

    public long getDurationFromPlayDuration(String playDuration) {
        playDuration = playDuration.replaceAll("P", "");
        playDuration = playDuration.replaceAll("0S", "");

        String[] dateSplit = playDuration.split("DT");
        long days = Long.valueOf(dateSplit[0]);
        playDuration = dateSplit[1];

        String[] hourSplit = playDuration.split("H");
        long hours = Long.valueOf(hourSplit[0]);
        playDuration = hourSplit[1];

        String[] minuteSplit = playDuration.split("M");
        long minutes = Long.valueOf(minuteSplit[0]);

        return toMilliSeconds(days, hours, minutes);

    }

//    public List<String> getDaysForSpinner(Context context) {
//        List<String> days = new ArrayList<>();
//        for (int i = 0; i < 60; i++) {
//            if (i < 32) {
//                days.add(i + context.getString(R.string.manul_profile_duration_days));
//            }
//        }
//        return days;
//    }

//    public List<String> getMinsForSpinner(Context context) {
//        List<String> mins = new ArrayList<>();
//        for (int i = 0; i < 60; i++) {
//            if (i < 60) {
//                mins.add(i + context.getString(R.string.manul_profile_duration_mins));
//            }
//        }
//        return mins;
//    }

//    public List<String> getHoursForSpinner(Context context) {
//        List<String> hours = new ArrayList<>();
//        for (int i = 0; i < 60; i++) {
//            if (i < 24) {
//                hours.add(i + context.getString(R.string.manual_profile_duration_hours));
//            }
//        }
//        return hours;
//    }

//    public String getCodeFromLanguage(String language){
//        if(mLanguageList != null) {
//            String languageCode = null;
//            for(LanguageDTO languageDTO : mLanguageList){
//                if(languageDTO.getLanguageName().equals(language)){
//                    languageCode = languageDTO.getIso_code();
//                    break;
//                }
//            }
//            return languageCode;
//        }
//        else{
//            return null;
//        }
//    }

//    public String getLanguageFromCode(String languageCode){
//        if(mLanguageList != null) {
//            String languageName = null;
//            for(LanguageDTO languageDTO : mLanguageList){
//                if(languageDTO.getIso_code().equals(languageCode)){
//                    languageName = languageDTO.getLanguageName();
//                    break;
//                }
//            }
//            return languageName;
//        }
//        else{
//            return null;
//        }
//    }


    private int[] getDurationArray(long days, long hours, long minutes) {
        int[] profileDurationArray = new int[3];
        profileDurationArray[0] = (int) days;
        profileDurationArray[1] = (int) hours;
        profileDurationArray[2] = (int) minutes;
        return profileDurationArray;
    }

    public int[] toDaysHoursAndMinutesDurationArray(long timeInMilliSeconds) {
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;
        String time = days + ":" + hours + ":" + minutes + ":" + seconds;
        return getDurationArray(days, hours, minutes);
    }

    public long toMilliSeconds(long days, long hours, long minutes) {
        return TimeUnit.DAYS.toMillis(days) + TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
    }

    public String getUserReadableDurationFormat(Context context, long profileDays, long profileHours, long profileMinutes) {
        if(context == null)
            return null;
        String mins = context.getResources().getString(R.string.profile_tunes_edit_text_hint_minutes);
        String min = context.getResources().getString(R.string.profile_tunes_edit_text_hint_minute);
        String hours = context.getResources().getString(R.string.profile_tunes_edit_text_hint_hours);
        String hour = context.getResources().getString(R.string.profile_tunes_edit_text_hint_hour);
        String days = context.getResources().getString(R.string.profile_tunes_edit_text_hint_days);
        String day = context.getResources().getString(R.string.profile_tunes_edit_text_hint_day);
        String duration = "";
        if (profileDays > 0 && profileHours > 0 && profileMinutes > 0) {
            duration = profileDays + "D" + " " + profileHours + "H" + " " + profileMinutes + "M";
        } else if (profileDays > 0 && profileHours > 0) {
            duration = profileDays + "D" + " " + profileHours + "H";
        } else if (profileDays > 0 && profileMinutes > 0) {
            duration = profileDays + "D" + " " + profileMinutes + "M";
        } else if (profileHours > 0 && profileMinutes > 0) {
            duration = profileHours + "H" + " " + profileMinutes + "M";
        } else if (profileHours > 0) {
            if (profileHours == 1) {
                duration = profileHours + " " + hour;
            } else {
                duration = profileHours + " " + hours;
            }
        } else if (profileMinutes > 0) {
            if (profileMinutes == 1) {
                duration = profileMinutes + min;
            } else {
                duration = profileMinutes + " " + mins;
            }

        } else if (profileDays > 0) {
            if (profileDays == 1) {
                duration = profileDays + day;
            } else {
                duration = profileDays + " " + days;
            }
        }
        return duration;
    }

    public long getDurationFromDateTimeRange(ScheduleDTO scheduleDTO) {
        String endDate = scheduleDTO.getDateRange().getEndDate();
        String startDate = scheduleDTO.getDateRange().getStartDate();
        String startTime = scheduleDTO.getTimeRange().getFromTime();
        String endTime = scheduleDTO.getTimeRange().getToTime();
        long startingTime = getTimeInMilliseconds(startDate + " " + startTime);
        long endingTime = getTimeInMilliseconds(endDate + " " + endTime);
        long durationTime = endingTime - startingTime;
        return durationTime;
    }

    public long getTimeInMilliseconds(String dateString) {
        long timeInMilliSeconds = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        try {
            date = format.parse(dateString);
            timeInMilliSeconds = date.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            timeInMilliSeconds = calendar.getTimeInMillis();

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliSeconds;
    }

    public String toDaysHoursAndMinutes(Context context, long timeInMilliSeconds) {
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + ":" + hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
        return getUserReadableDurationFormat(context, days, hours % 24, minutes % 60);
    }

    public static String toNoOfDaysHoursAndMinutes(long timeInMilliSeconds) {
        long seconds = timeInMilliSeconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String time = days + ":" + hours % 24 + ":" + minutes % 60;
        return time;
    }

    public static long getDifferenceInMillis(com.onmobile.baseline.http.api_action.storeapis.purchase_combo.ScheduleDTO scheduleDTO) {
        if (scheduleDTO == null)
            return -1;
        String startDate = scheduleDTO.getDateRange().getStartDate();
        String endDate = scheduleDTO.getDateRange().getEndDate();
        String startTime = scheduleDTO.getTimeRange().getFromTime();
        String endTime = scheduleDTO.getTimeRange().getToTime();
        return getDifferenceInMillis(startDate, startTime, endDate, endTime);
    }

    private static long getDifferenceInMillis(String startDate, String startTime, String endDate, String endTime) {
        if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(endTime))
            return -1;

        String dateStart = startDate + " " + startTime;
        String dateStop = endDate + " " + endTime;

        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss", Locale.getDefault());

        Date d1;
        Date d2;
        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

        // Get msec from each, and subtract.
        long diff = d2.getTime() - d1.getTime();
        /*long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);
        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");*/

        return diff;
    }

}
