package com.health1st.yeop9657.health1st;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.model.LatLng;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.health1st.yeop9657.health1st.ResourceData.BasicData;
import com.health1st.yeop9657.health1st.ResourceData.BatGoal;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.yalantis.beamazingtoday.interfaces.BatModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity implements PermissionListener
{
    /* Vibrator */
    protected Vibrator mVibrator = null;

    /* Preference */
    protected SharedPreferences mShared = null;
    protected SharedPreferences.Editor mSharedWrite = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* POINT - : Vibrator */
        if (mVibrator == null) { mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); }

        /* POINT - : SharedPreferences */
        if (mShared == null) {
            mShared = PreferenceManager.getDefaultSharedPreferences(this);
            mSharedWrite = mShared.edit();
        }

        /* POINT - : FONT Open Source */
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "BMHANNA_11yrs_ttf.ttf")).addBold(Typekit.createFromAsset(this, "BMHANNA_11yrs_ttf.ttf"));

        /* POINT - : Permission */
        TedPermission.with(this).setPermissionListener(this).setDeniedMessage(R.string.Basic_Permission_Information)
                .setPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE).check();
    }

    /* MARK - : Typekit Method */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    /* MARK - : User Custom Method */
    protected void setToolBar(final Toolbar mToolbar, final String sTitle, final String sSubTitle)
    {
        mToolbar.setTitle(sTitle);
        mToolbar.setSubtitle(sSubTitle);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        mToolbar.inflateMenu(R.menu.main);
        setSupportActionBar(mToolbar);
    }

    protected void setImageView(final Context mContext, final ImageView mImageView, final int mResourceID)
    {
        final String mPhotoPath = mShared.getString("Helper_Image", BasicData.EMPTY_TEXT);

        /* POINT - : Glide Open Source */
        Glide.with(mContext).load( (mPhotoPath == BasicData.EMPTY_TEXT ? mResourceID : mPhotoPath) )
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation())).into(mImageView);
    }

    /* MARK - : set ArrayList<String> Preference Method */
    protected final void setArrayListPreference(final ArrayList<String> mArrayList, final String sKey)
    {
        if (mArrayList.isEmpty()) { mSharedWrite.putString(sKey, null); }
        else
        {
            final JSONArray aJSON = new JSONArray();
            for (final String mTemp : mArrayList) { aJSON.put(mTemp); }
            mSharedWrite.putString(sKey, aJSON.toString());
        }

        mSharedWrite.apply();
    }

    /* MARK - : get ArrayList<?> Preference Method */
    protected final ArrayList<?> getArrayListPreference(final String sKey) throws JSONException {
        final String sJSON = mShared.getString(sKey, null);

        ArrayList<?> acBasicList = null;
        if (sJSON == null) { return new ArrayList<>(BasicData.ALLOCATE_BASIC_VALUE); }

        final JSONArray asJSON = new JSONArray(sJSON);
        switch (sKey)
        {
            case (BasicData.MARKER_PREFERENCE_KEY) : {
                final ArrayList<LatLng> acLatLng = new ArrayList<LatLng>(BasicData.ALLOCATE_BASIC_VALUE);

                    for (int mCount = 0, mSize = asJSON.length(); mCount < mSize; mCount++) {
                        final String mLatLong[] = asJSON.optString(mCount).split(",");
                        acLatLng.add(new LatLng(Double.parseDouble(mLatLong[0]), Double.parseDouble(mLatLong[1])));
                    } acBasicList = acLatLng; break;
            }

            case (BasicData.BAT_TODO_KEY) : {
                final ArrayList<BatModel> acBatModel = new ArrayList<BatModel>(BasicData.ALLOCATE_BASIC_VALUE);

                    for (int mCount = 0, mSize = asJSON.length(); mCount < mSize; mCount++) {
                        acBatModel.add(new BatGoal(asJSON.optString(mCount)));
                    } acBasicList = acBatModel; break;
            }
        }

        return acBasicList;
    }

    /* MARK : - Permission Listener */
    @Override
    public void onPermissionGranted() {}

    @Override
    public void onPermissionDenied(ArrayList<String> arrayList) {}
}