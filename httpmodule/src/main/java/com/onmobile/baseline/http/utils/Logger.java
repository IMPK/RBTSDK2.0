package com.onmobile.baseline.http.utils;

import android.util.Log;

import com.onmobile.baseline2DTO.httpmodule.BuildConfig;

public class Logger {

    String tag;

    private Logger(String tag) {
        this.tag = tag;
    }

    public static String getLogTag(Class classname) {
        if (classname != null) {
            return classname.getName();
        } else
            return "LoggerTAG";
    }

    public static Logger getLogger(Class classname) {
        return new Logger(classname.getSimpleName());
    }

    public void debug(String msg) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public void d(String msg) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public void i(String msg) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.i(tag, msg);
        }
    }

    public void w(String msg) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.w(tag, msg);
        }
    }

    public void w(String msg, Exception e) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.w(tag, msg + " " + e.toString());
        }
    }

    public void e(String msg) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }

    public void e(String msg, Exception e) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.e(tag, msg, e);
        }
    }

    public void v(String msg) {
        if (   tag != null && msg != null && BuildConfig.DEBUG) {
            Log.v(tag, msg);
        }
    }
}
