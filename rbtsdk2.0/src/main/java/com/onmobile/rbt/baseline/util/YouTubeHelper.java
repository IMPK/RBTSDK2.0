package com.onmobile.rbt.baseline.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by prateek.khurana on 01-03-2018.
 */

public class YouTubeHelper {

    final String youTubeUrlRegEx = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/";
    final String[] videoIdRegex = { "\\?vi?=([^&]*)","watch\\?.*v=([^&]*)", "(?:embed|vi?)/([^/?]*)", "^([A-Za-z0-9\\-]*)"};

    public String extractVideoIdFromUrl(String url) {
        if(!TextUtils.isEmpty(url)) {
            String youTubeLinkWithoutProtocolAndDomain = youTubeLinkWithoutProtocolAndDomain(url);
            for (String regex : videoIdRegex) {
                Pattern compiledPattern = Pattern.compile(regex);
                Matcher matcher = compiledPattern.matcher(youTubeLinkWithoutProtocolAndDomain);

                if (matcher.find()) {
                    return matcher.group(1);
                }
            }
        }
        return null;
    }

    private String youTubeLinkWithoutProtocolAndDomain(String url) {
        if(!TextUtils.isEmpty(url)){
            Pattern compiledPattern = Pattern.compile(youTubeUrlRegEx);
            Matcher matcher = compiledPattern.matcher(url);
            if(matcher.find()){
                return url.replace(matcher.group(), "");
            }
            return url;
        }
        return null;
    }

    public boolean isUrlStartsWithHttpOrHttpsOrWWW(String youtubeUrl) {
        boolean is =  false;
        if(youtubeUrl != null) {
            if(youtubeUrl.toLowerCase().startsWith("com.onmobile.rbt.baseline.http://")) {
                is = true;
            }else if (youtubeUrl.toLowerCase().startsWith("https://")) {
                is = true;
            }else if (youtubeUrl.toLowerCase().startsWith("www")) {
                is = true;
            }
        }
        return is;
    }
}
