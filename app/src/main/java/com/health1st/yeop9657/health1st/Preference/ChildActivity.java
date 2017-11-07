package com.health1st.yeop9657.health1st.Preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.health1st.yeop9657.health1st.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ChildActivity extends PreferenceFragment implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener
{
    /* POINT - : Integer */
    private final static int PICK_CONTACT_REQUEST = 1;

    /* POINT - : Context */
    private Context mContext = null;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_setting);

        mContext = getPreferenceScreen().getContext();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        getPreferenceManager().findPreference("Helper_Image").setOnPreferenceClickListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    /* TODO - : Preference Click Listener */
    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        switch (preference.getTitleRes())
        {
            case R.string.Preference_Helper_Image : {

                final Intent mIntent = new Intent(Intent.ACTION_PICK);
                mIntent.setType("image/*");
                startActivityForResult(mIntent, PICK_CONTACT_REQUEST);
                return true;
            }
        } return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST)
        {
            /* POINT - : SharedPreference */
            final SharedPreferences mReadShared = PreferenceManager.getDefaultSharedPreferences(mContext);
            final SharedPreferences.Editor mWriteShared = mReadShared.edit();

            mWriteShared.putString("Helper_Image", data.getDataString());
            mWriteShared.commit();

            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getPreferenceManager().findPreference("Helper_Image").getTitle().toString())
                    .setContentText("선택 된 사진으로 설정되었습니다.").show();
        }
    }

    /* TODO - : Preference Changed Listener */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        final String sSharedValue = sharedPreferences.getString(key, "기본값");
        final CharSequence sSharedTitle = getPreferenceManager().findPreference(key).getTitle();

        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(sSharedTitle.subSequence(2, sSharedTitle.length()).toString())
                .setContentText(String.format("%s으로 설정되었습니다.", sSharedValue)).show();
    }
}