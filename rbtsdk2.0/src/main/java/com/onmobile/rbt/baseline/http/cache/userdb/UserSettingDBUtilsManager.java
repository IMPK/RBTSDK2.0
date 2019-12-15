package com.onmobile.rbt.baseline.http.cache.userdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.onmobile.rbt.baseline.http.utils.Logger;

public class UserSettingDBUtilsManager implements IUserSettingDBUtils {

    public static final String TAG = UserSettingDBUtilsManager.class.getSimpleName();
    private static final Logger sLogger = Logger.getLogger(UserSettingDBUtilsManager.class);
    private static UserSettingsDatabaseManager dbManager;
    SQLiteDatabase db;
    private Context mContext;


    public UserSettingDBUtilsManager(Context context) {
        this.mContext = context;
        dbManager = UserSettingsDatabaseManager.getInstance(context);
    }

    public SQLiteDatabase getWritableDatabase() throws SQLException {
        return dbManager.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() throws SQLException {
        return dbManager.getReadableDatabase();
    }

    public void close() {
        dbManager.close();
    }

    @Override
    public void updateUserMsisdn(String msisdn) {
        updateSettings(new UserSettings(UserSettingConstants.USER_MSISDN, msisdn));
    }

    public void initAllDefaultSettings() {

        sLogger.v("INITIALIZING THE DEFAULT USER SETTINGS ");
        addRow(new UserSettings(UserSettingConstants.USER_AUTH_TOKEN_ID, ""));
        addRow(new UserSettings(UserSettingConstants.USER_INFO, ""));
        addRow(new UserSettings(UserSettingConstants.USER_MSISDN, ""));
        addRow(new UserSettings(UserSettingConstants.USER_SUBSCRIPTION_INFO, ""));

    }

    @Override
    public int updateSettings(UserSettings settings) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_KEY, settings.getKey());
        values.put(UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_VALUE, settings.getValue());

        // updating row
        int rowsEffected = db.update(UserSettingsDatabaseManager.TABLE_USER_SETTINGS, values, UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_KEY + " = ?", new String[]{settings.getKey()});

        sLogger.v("Updating " + UserSettingsDatabaseManager.TABLE_USER_SETTINGS + " Key: " + settings.getKey() + " Value: " + settings.getValue());
        sLogger.v("Rows Updated " + rowsEffected + "");
        return rowsEffected;
    }

    // Adding new Row
    private void addRow(UserSettings settings) {
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_KEY, settings.getKey());
        values.put(UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_VALUE, settings.getValue());

        if (!checkIfRowExists(UserSettingsDatabaseManager.TABLE_USER_SETTINGS, UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_KEY, settings.getKey())) {
            // Inserting Row
            long index = db.insert(UserSettingsDatabaseManager.TABLE_USER_SETTINGS, null, values);
            if (index > 0) {
                sLogger.v("Insert " + UserSettingsDatabaseManager.TABLE_USER_SETTINGS + " Key: " + settings.getKey() + " Value: " + settings.getValue());
            } else {
                sLogger.e("Error in Insert " + UserSettingsDatabaseManager.TABLE_USER_SETTINGS + " Key: " + settings.getKey() + " Value: " + settings.getValue());
            }
        } else {
            sLogger.v("Already Inserted " + UserSettingsDatabaseManager.TABLE_USER_SETTINGS + " Key: " + settings.getKey() + " Value: " + settings.getValue());
        }

    }

    private boolean checkIfRowExists(String TableName, String dbfield, String fieldValue) {
        db = this.getReadableDatabase();
        String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor != null && cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        if (cursor != null) {
            cursor.close();
        }
        return true;
    }

    // Getting single UserSettings that matches "key"
    public UserSettings getUserSettings(String id) {
        UserSettings settings = new UserSettings();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + UserSettingsDatabaseManager.TABLE_USER_SETTINGS + " where " + UserSettingsDatabaseManager.USER_SETTINGS_COLUMN_KEY + " = '" + id + "'", null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                // System.out.println(" cursor SIZE " + cursor.getCount());
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String Key = cursor.getString(1);
                    String value = cursor.getString(2);
                    sLogger.v(" Reading " + UserSettingsDatabaseManager.TABLE_USER_SETTINGS + " Key: " + Key + " Value: " + value);
                    settings = new UserSettings(Key, value);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            sLogger.e(e.getMessage());
            cursor.close();
        }
        return settings;
    }
}
