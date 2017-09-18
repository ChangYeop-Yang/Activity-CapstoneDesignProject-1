package com.health1st.yeop9657.health1st.Preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.health1st.yeop9657.health1st.R;

public class ChildActivity extends PreferenceFragment implements Preference.OnPreferenceClickListener
{
    /* POINT - : Integer */
    private final static int PICK_CONTAT_REQUEST = 1;

    /*  POINT - : Preference*/
    private Preference mPhotoPreference = null;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_setting);

        mPhotoPreference = findPreference("Helper_Image");
        mPhotoPreference.setOnPreferenceClickListener(this);
    }

    /* MARK - : Preference Click Listener */
    @Override
    public boolean onPreferenceClick(Preference preference)
    {
        final Intent mIntent = new Intent(Intent.ACTION_PICK);
        mIntent.setType("image/*");
        startActivityForResult(mIntent, PICK_CONTAT_REQUEST);

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTAT_REQUEST)
        {
            /* POINT - : SharedPreference */
            final SharedPreferences mReadShared = PreferenceManager.getDefaultSharedPreferences(mPhotoPreference.getContext());
            final SharedPreferences.Editor mWriteShared = mReadShared.edit();

            mWriteShared.putString("Helper_Image", data.getDataString());
            mWriteShared.commit();
        }
    }
}
