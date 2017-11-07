package com.health1st.yeop9657.health1st.Accessory;

import android.app.Activity;
import android.content.Context;

import java.util.UUID;

/**
 * Created by yeop on 2017. 11. 1..
 */

public class BluetoothManager extends Thread {

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : Activity */
    private Activity mActivity = null;

    /* MARK - : Integer */
    private static final int REQUEST_ENABLE_BT = 0;

    /* MARK - : UUID */
    private static final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    /* MARK - : String */
    private final String PAAR_BAND_NAME = "PAARBand";
    private final String MI_BAND_NAME = "MI Band 2";
    private final String TAG = BluetoothManager.class.getSimpleName();

    /* MARK - : BluetoothManager */
    public BluetoothManager(final Context mcontext, final Activity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
    }

    @Override
    public void run() {
        super.run();
    }
}
