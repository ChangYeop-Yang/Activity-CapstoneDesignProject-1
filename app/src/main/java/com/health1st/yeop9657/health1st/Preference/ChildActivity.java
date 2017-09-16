package com.health1st.yeop9657.health1st.Preference;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.health1st.yeop9657.health1st.R;

public class ChildActivity extends PreferenceFragment
{
    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_setting);
    }
}
