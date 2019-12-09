package com.onmobile.rbt.baseline.application;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.onmobile.rbt.baseline.util.Logger;

/**
 * Created by Nikita Gurwani .
 */
public class DatabaseManager extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "appStore.db";
    static final String TABLE_USER_SETTINGS = "userSettings";
    private static int DATABASE_VERSION = 1;
    private static DatabaseManager sInstance;
    static final String USER_SETTINGS_COLUMN_KEY = "key";
    public static final String USER_SETTINGS_COLUMN_VALUE = "value";
    static final String APP_MSISDN = "MSISDN";


    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseManager getInstance(Context context) {
        int version = BaselineApplication.getApplication().getDbVersionFromFile(DATABASE_NAME);
        if (version > 0)
            DATABASE_VERSION = version;
        if (sInstance == null) {
            sInstance = new DatabaseManager(context);
        }
        return sInstance;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Logger.e("db version ", " " + i + " " + i1);
    }
}
