package com.health1st.yeop9657.health1st.Accessory;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;

/**
 * Created by yeop on 2017. 11. 9..
 */

public class PAARBandManager extends BluetoothManager implements View.OnClickListener {

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : Activity */
    private Activity mActivity = null;

    /* MARK - : PAARBand Manager Creator */
    public PAARBandManager(final Context mContext, final Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    public void onClick(View view) {

        /* POINT - : Button */
        final Button mButton = (Button)view;
        startDeviceVibrate(mContext);

        switch (view.getId())
        {

        }
    }
}
