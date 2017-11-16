package com.health1st.yeop9657.health1st.Accessory;

import android.content.Context;
import android.os.Vibrator;

import com.health1st.yeop9657.health1st.ResourceData.BasicData;

import java.util.UUID;

/**
 * Created by yeop on 2017. 11. 1..
 */

public class BluetoothManager extends Thread {

    /* MARK - : Vibrate */
    private Vibrator mVibrator = null;

    /* TODO - : Xiaomi Mi-Band UUID */
    public final static class Xiaomi {

        /* MARK - : Xiaomi Mi-Band Basic UUID */
        public static final UUID BASIC_SERVICE = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
        public static final UUID BASIC_BATTERY_CHARACTERISTIC = UUID.fromString("00000006-0000-3512-2118-0009af100700");
        public static final UUID BASIC_STEP_CHARACTERISTIC = UUID.fromString("00000007-0000-3512-2118-0009af100700");

        /* MARK - : Xiaomi Mi-Band AlertNotification UUID */
        public static final UUID ALERT_NOTIFICATION_SERVICE = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public static final UUID ALERT_NOTIFICATION_CHARACTERISTIC = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

        /* MARK - : Xiaomi Mi-Band HeartRate UUID */
        public static final UUID HEART_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static final UUID HEART_MEASUREMENT_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static final UUID HEART_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static final UUID HEART_CONTROL_CHARACTERISTIC = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }

    /* TODO - : Vibrate Method */
    protected void startDeviceVibrate(final Context mContext) {
        if (mVibrator == null) { mVibrator = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE); }
        mVibrator.vibrate(BasicData.VIBRATE_VALUE);
    }

    /* TODO - : Byte Convert Method */
    protected int convertByteToInt(final byte[] b) {
        int value= 0;
        for(int i=0; i<b.length; i++) { value = (value << 8) | b[i]; }

        return value;
    }
}
