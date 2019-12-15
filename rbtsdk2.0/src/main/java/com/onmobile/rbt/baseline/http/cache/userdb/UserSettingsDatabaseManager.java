package com.onmobile.rbt.baseline.http.cache.userdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.logging.Logger;


public class UserSettingsDatabaseManager extends SQLiteOpenHelper {
    private static final Logger sLogger = Logger.getLogger(UserSettingsDatabaseManager.class.getName());
    private static UserSettingsDatabaseManager sInstance;
    private Context mContext;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_settings_cache.db";

    public static final String TABLE_USER_SETTINGS = "user_settings_cache";
    public static final String USER_SETTINGS_COLUMN_ID = "_id";
    public static final String USER_SETTINGS_COLUMN_KEY = "key";
    public static final String USER_SETTINGS_COLUMN_VALUE = "value";


    private static final String CREATE_USER_SETTINGS_DATABASE = "create table IF NOT EXISTS "
            + TABLE_USER_SETTINGS + "("
            + USER_SETTINGS_COLUMN_ID + " integer primary key autoincrement, "
            + USER_SETTINGS_COLUMN_KEY + " text not null, "
            + USER_SETTINGS_COLUMN_VALUE + " text"
            + ")";

    public UserSettingsDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_SETTINGS_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }

    public static UserSettingsDatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UserSettingsDatabaseManager(context);
        }
        return sInstance;
    }


}
