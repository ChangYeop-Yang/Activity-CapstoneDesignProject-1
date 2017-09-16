package com.health1st.yeop9657.health1st.Preference;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.health1st.yeop9657.health1st.BaseActivity;
import com.health1st.yeop9657.health1st.R;

public class ParentActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        /* MARK - : Toolbar */
        final Toolbar mToolbar = (Toolbar)findViewById(R.id.Setting_Toolbar);
        setToolBar(mToolbar, "Health 1st Street", "SET UP - Patient Version");

        getFragmentManager().beginTransaction().replace(R.id.Setting_FrameLayout, new ChildActivity()).commit();
    }
}