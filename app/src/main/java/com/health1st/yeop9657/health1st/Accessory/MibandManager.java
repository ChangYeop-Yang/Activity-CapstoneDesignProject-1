package com.health1st.yeop9657.health1st.Accessory;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

/**
 * Created by yeop on 2017. 11. 7..
 */

public class MiBandManager extends Thread {

    /* TODO - : Basic UUID */
    public static class Basic {
        public static UUID service = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");
        public static UUID batteryCharacteristic = UUID.fromString("00000006-0000-3512-2118-0009af100700");
    }

    /* TODO - : Alert UUID */
    public static class AlertNotification {
        public static UUID service = UUID.fromString("00001802-0000-1000-8000-00805f9b34fb");
        public static UUID alertCharacteristic = UUID.fromString("00002a06-0000-1000-8000-00805f9b34fb");
    }

    /* TODO - : HeartRate UUID */
    public static class HeartRate {
        public static UUID service = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb");
        public static UUID measurementCharacteristic = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb");
        public static UUID descriptor = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
        public static UUID controlCharacteristic = UUID.fromString("00002a39-0000-1000-8000-00805f9b34fb");
    }

    /* MARK - : String */
    private final static String TAG = MiBandManager.class.getSimpleName();
    private final static String MI_NAME = "MI Band 2";

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : View */
    private View mView = null;

    /* MARK - : Bluetooth Instance */
    private BluetoothGatt mDeviceBluetoothGatt = null;

    Boolean isListeningHeartRate = false;

    /* MARK - : Mi-Band Manager Creator */
    public MiBandManager(final Context mContext, final View mView) {
        this.mContext = mContext;
        this.mView = mView;
    }

    @Override
    public void run() {
        super.run();

        /* POINT - : Bluetooth Adapter */
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Set<BluetoothDevice> mDevice = mBluetoothAdapter.getBondedDevices();

        /* POINT - : Connect Mi-Band */
        for (final BluetoothDevice mBluetooth : mDevice) {
            if (mBluetooth.getName() != null & mBluetooth.getName().contains(MI_NAME)) {
                mDeviceBluetoothGatt = mBluetooth.connectGatt(mContext, true, mBluetoothGattCallback);
                Log.e(TAG, String.format("%s %s", mBluetooth.getAddress(), mBluetooth.getName()));
            }
        }
    }

    /* MARK - : Start Heart Beat Rate Method */
    public void startScanHeartRate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(HeartRate.service)
                .getCharacteristic(HeartRate.controlCharacteristic);
        bchar.setValue(new byte[]{21, 2, 1});

        mDeviceBluetoothGatt.writeCharacteristic(bchar);
    }

    /* MARK - : Set Listen Heart Rate Method */
    public void listenHeartRate() {
        BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(HeartRate.service)
                .getCharacteristic(HeartRate.measurementCharacteristic);
        mDeviceBluetoothGatt.setCharacteristicNotification(bchar, true);
        BluetoothGattDescriptor descriptor = bchar.getDescriptor(HeartRate.descriptor);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mDeviceBluetoothGatt.writeDescriptor(descriptor);
        isListeningHeartRate = true;
    }

    /* MARK - : Get Mi-Band Battery Method */
    public void getBatteryStatus() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(Basic.service)
                .getCharacteristic(Basic.batteryCharacteristic);

        if (!mDeviceBluetoothGatt.readCharacteristic(bchar)) {
            Toast.makeText(mContext, "Failed get battery info", Toast.LENGTH_SHORT).show();
        }
    }

    /* MARK - : Start Mi-Band Vibrate Method */
    public void startBandVibrate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(AlertNotification.service)
                .getCharacteristic(AlertNotification.alertCharacteristic);

        bchar.setValue(new byte[]{2});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar)) {
            Toast.makeText(mContext, "Failed start vibrate", Toast.LENGTH_SHORT).show();
        }
    }

    /* MARK - : Stop Mi-Band Vibrate Method */
    public void stopBandVibrate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(AlertNotification.service)
                .getCharacteristic(AlertNotification.alertCharacteristic);

        bchar.setValue(new byte[]{0});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar)) {
            Toast.makeText(mContext, "Failed stop vibrate", Toast.LENGTH_SHORT).show();
        }
    }

    /* TODO - : BluetoothGattCallback Method */
    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            Log.v(TAG, "onConnectionStateChange");

            switch (newState)
            {
                case (BluetoothProfile.STATE_CONNECTED) :
                {
                    mDeviceBluetoothGatt.discoverServices();
                    Snackbar.make(mView, "Connect Xiaomi Mi-Band.", Snackbar.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothProfile.STATE_DISCONNECTED) :
                {
                    mDeviceBluetoothGatt.disconnect();
                    Snackbar.make(mView, "Disconnect Xiaomi Mi-Band.", Snackbar.LENGTH_SHORT).show();
                    break;
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.v("test", "onServicesDiscovered");
            listenHeartRate();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            Log.v("test", "onCharacteristicRead");
            byte[] data = characteristic.getValue();
            Log.e(TAG, Arrays.toString(data));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.v("test", "onCharacteristicChanged");
            byte[] data = characteristic.getValue();
            Log.e(TAG, Arrays.toString(data));
        }
    };
}
