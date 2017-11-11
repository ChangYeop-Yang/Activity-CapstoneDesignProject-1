package com.health1st.yeop9657.health1st;

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
import com.health1st.yeop9657.health1st.ResourceData.BasicData;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity
{
    /* Vibrator */
    protected Vibrator mVibrator = null;

    /* Preference */
    protected SharedPreferences mShared = null;
    protected SharedPreferences.Editor mSharedWrite = null;

    /* String */
    private String sSecurityPassword = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* POINT - : Vibrator */
        if (mVibrator == null) { mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); }

        /* POINT - : SharedPreferences */
        if (mShared == null) {
            mShared = PreferenceManager.getDefaultSharedPreferences(this);
            mSharedWrite = mShared.edit();

            sSecurityPassword = mShared.getString(BasicData.ENCRYPT_DECRYPT_KEY, null);
            if (sSecurityPassword == null) {

                /* TODO - : Random Generator */
                final Random mRandom = new Random();
                mRandom.setSeed(System.currentTimeMillis());

                sSecurityPassword = String.valueOf(mRandom.nextLong() + Integer.MAX_VALUE);
                mSharedWrite.putString(BasicData.ENCRYPT_DECRYPT_KEY, sSecurityPassword).apply();
            }
        }

        /* POINT - : FONT Open Source */
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "BMHANNA_11yrs_ttf.ttf")).addBold(Typekit.createFromAsset(this, "BMHANNA_11yrs_ttf.ttf"));
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
}