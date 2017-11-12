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

        /* MARK - : Xiaomi Mi-Band AlertNotification UUID */
        public static final UUID ALERT_NOTIFICATION_SERVICE = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public static final UUID ALERT_NOTIFICATION_CHARACTERISTIC = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");

        /* MARK - : Xiaomi Mi-Band HeartRate UUID */
        public static final UUID HEART_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static final UUID HEART_MEASUREMENT_CHARACTERISTIC = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static final UUID HEART_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static final UUID HEART_CONTROL_CHARACTERISTIC = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }

    public final static class PAARBand {
        public static final String PAAR_WATCH_CLASSIC = "00001101-0000-1000-8000-00805f9b34fb";
        public static String PAAR_WATCH_NOTIFY_CHARACTERISTIC = "6e407f01-b5a3-f393-e0a9-e50e24dcca9e";
        public static String PAAR_WATCH_SERVICE = "6e402650-b5a3-f393-e0a9-e50e24dcca9e";
        public static String TI_PAAR_WATCH_NOTIFY_CHARACTERISTIC = toUuid128StringFormat(32513);
        public static String TI_PAAR_WATCH_SERVICE = toUuid128StringFormat(9808);
        public static String TI_PAAR_WATCH_WRITE_CHARACTERISTIC = toUuid128StringFormat(32514);
        public static String pAAR_WATCH_WRITE_CHARACTERISTIC = "6e407f02-b5a3-f393-e0a9-e50e24dcca9e";

        private final static String toUuid128StringFormat(int parm) {
            final StringBuilder mStringBuilder = new StringBuilder("0000");
            mStringBuilder.append(Integer.toHexString(parm));
            mStringBuilder.append("-0000-1000-8000-00805f9b34fb");
            return mStringBuilder.toString();
        }
    }

    protected static final long SCAN_PERIOD = 10000;

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
