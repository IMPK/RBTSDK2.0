package com.onmobile.rbt.baseline.util;

import android.util.Log;

import com.onmobile.rbt.baseline.BuildConfig;
import com.onmobile.rbt.baseline.configuration.AppConfigurationValues;

public class Logger {

    private static boolean debuggable;
    private static Logger logger;

    private Logger() {
        debuggable = AppConfigurationValues.isDebuggable() && BuildConfig.DEBUG;
    }

    private static void init() {
        if (logger == null)
            logger = new Logger();
    }

    public static void debug(String tag, String msg) {
        init();
        if (debuggable) {
            Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        init();
        if (debuggable) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        init();
        if (debuggable) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        init();
        if (debuggable) {
            Log.w(tag, msg);
        }
    }

    public static void w(String tag, String msg, Exception e) {
        init();
        if (debuggable) {
            Log.w(tag, msg + " " + e.toString());
        }
    }

    public static void e(String tag, String msg) {
        init();
        if (debuggable) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Exception e) {
        init();
        if (debuggable) {
            Log.e(tag, msg, e);
        }
    }

    public static void v(String tag, String msg) {
        init();
        if (debuggable) {
            Log.v(tag, msg);
        }
    }

    public static boolean isDebuggable() {
        init();
        return debuggable;
    }
}
