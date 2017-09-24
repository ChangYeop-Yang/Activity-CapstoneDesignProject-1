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

    /*  POINT - : Preference*/
    private Preference mPhotoPreference = null;

    /* POINT - : Context */
    private Context mContext = null;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_setting);

        mContext = getPreferenceScreen().getContext();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        mPhotoPreference = findPreference("Helper_Image");
        mPhotoPreference.setOnPreferenceClickListener(this);
    }

    /* TODO - : Preference Click Listener */
    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        final Intent mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setType("image/*");
        startActivityForResult(mIntent, PICK_CONTACT_REQUEST);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST)
        {
            /* POINT - : SharedPreference */
            final SharedPreferences mReadShared = PreferenceManager.getDefaultSharedPreferences(mPhotoPreference.getContext());
            final SharedPreferences.Editor mWriteShared = mReadShared.edit();

            mWriteShared.putString("Helper_Image", data.getDataString());
            mWriteShared.commit();

            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE).setTitleText("사진 설정이 완료되었습니다.").show();
        }
    }

    /* TODO - : Preference Changed Listener */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(String.format("%s으로 설정되었습니다.", sharedPreferences.getString(s, "기본값"))).show();
    }
}
