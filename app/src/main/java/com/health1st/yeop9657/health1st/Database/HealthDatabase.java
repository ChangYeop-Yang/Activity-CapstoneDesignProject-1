package com.health1st.yeop9657.health1st.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.health1st.yeop9657.health1st.ResourceData.BasicData;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;
import java.util.ArrayList;

/**
 * Created by yeop on 2017. 11. 9..
 */

public class HealthDatabase extends SQLiteOpenHelper {

    /* TODO - : Database Information */
    private static final class DATABASE_INFORMATION {
        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "HealthInfo";
        private static final String DATABASE_COLUMN_HRM = "HRM";
        private static final String DATABASE_COLUMN_SPO2 = "SPO";
        private static final String DATABASE_COLUMN_DEVICE = "DEVICE";
    }

    /* MARK - : String */
    private static final String TAG = HealthDatabase.class.getSimpleName();

    public HealthDatabase(final Context context) {
        super(context, DATABASE_INFORMATION.DATABASE_NAME, null, DATABASE_INFORMATION.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    { sqLiteDatabase.execSQL("CREATE TABLE " + DATABASE_INFORMATION.DATABASE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, HRM TEXT, SPO TEXT, DEVICE TEXT);"); }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DATABASE_INFORMATION.DATABASE_NAME + ";");
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    /* TODO - : Getting Health Encrypt Key */
    private final String getHealthEncryptKey(final Context mContext) {

        /* POINT - : SharedPreference */
        final SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String mPasswdKey = mSharedPreferences.getString(BasicData.ENCRYPT_DECRYPT_KEY, null);

        return mPasswdKey;
    }

    /* TODO - : Health Insert Database Method */
    public final boolean insertHealthData(final SQLiteDatabase mSQLite, final Context mContext, final int mHRM, final int mSPO2, final String mDeviceName) {

        final String mPasswdKey = getHealthEncryptKey(mContext);

        try
        {
            /* POINT - : ContentValues */
            final ContentValues mContentValues = new ContentValues();
            mContentValues.put(DATABASE_INFORMATION.DATABASE_COLUMN_HRM, AESCrypt.encrypt(mPasswdKey, String.valueOf(mHRM)));
            mContentValues.put(DATABASE_INFORMATION.DATABASE_COLUMN_SPO2, AESCrypt.encrypt(mPasswdKey, String.valueOf(mSPO2)));
            mContentValues.put(DATABASE_INFORMATION.DATABASE_COLUMN_DEVICE, AESCrypt.encrypt(mPasswdKey, mDeviceName));

            mSQLite.insert(DATABASE_INFORMATION.DATABASE_NAME, null, mContentValues);
            return true;
        }
        catch (SQLiteException error) { Log.e(TAG, error.getMessage()); error.printStackTrace(); return false; }
        catch (GeneralSecurityException error) { Log.e(TAG, error.getMessage()); error.printStackTrace(); return false; }
    }

    /* TODO - : Health Select Database Method */
    public final ArrayList<HealthAdapter> selectHealthData(final SQLiteDatabase mSQLite, final Context mContext) {

        final String mPasswdKey = getHealthEncryptKey(mContext);

        /* MARK - : ArrayList<HealAdapter> */
        final ArrayList<HealthAdapter> mHealthList = new ArrayList<HealthAdapter>(BasicData.ALLOCATE_BASIC_VALUE);

        Cursor mCursor = null;
        try {

            mCursor = mSQLite.rawQuery("SELECT * FROM " + DATABASE_INFORMATION.DATABASE_NAME + ";", null);

            while (mCursor.moveToNext()) {

                final int mHRM = Integer.valueOf(AESCrypt.decrypt(mPasswdKey, mCursor.getString(1)));
                final int mSPO2 = Integer.valueOf(AESCrypt.decrypt(mPasswdKey, mCursor.getString(2)));
                final String mDeviceName = AESCrypt.decrypt(mPasswdKey, mCursor.getString(3));

                mHealthList.add(new HealthAdapter(mHRM, mSPO2, mDeviceName));
            }

            return mHealthList;
        }
        catch (SQLiteException error) { Log.e(TAG, error.getMessage()); error.printStackTrace(); return null; }
        catch (GeneralSecurityException error) { Log.e(TAG, error.getMessage()); error.printStackTrace(); return null; }
        finally { mCursor.close(); }
    }

    /* TODO - : Health Delete Database Method */
    public final boolean deleteHealthData(final SQLiteDatabase mSQLite, final String mDeviceName) {

        try { mSQLite.delete(DATABASE_INFORMATION.DATABASE_NAME, String.format("%s = %s", DATABASE_INFORMATION.DATABASE_COLUMN_DEVICE, mDeviceName), null); return true; }
        catch (SQLiteException error) { Log.e(TAG, error.getMessage()); error.printStackTrace(); return false; }
    }
}
