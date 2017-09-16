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
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class BaseActivity extends AppCompatActivity
{
    /* Vibrator */
    protected Vibrator mVibrator = null;

    /* Preference */
    protected SharedPreferences mShared = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* POINT - : Vibrator */
        if (mVibrator == null) { mVibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE); }

        /* POINT - : SharedPreferences */
        if (mShared == null) { mShared = PreferenceManager.getDefaultSharedPreferences(this); }

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
        /* POINT - : Glide Open Source */
        Glide.with(mContext).load(mResourceID)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation())).into(mImageView);
    }
}