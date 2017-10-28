package com.health1st.yeop9657.health1st.Accessory;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

/**
 * Created by yeop on 2017. 10. 26..
 */

public class BluetoothManager implements BluetoothSPP.BluetoothConnectionListener, BluetoothSPP.BluetoothStateListener, BluetoothSPP.OnDataReceivedListener{

    /* MARK - : BluetoothSPP  */
    private BluetoothSPP mBlueSPP = null;

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : Bluetooth Adapter Creator */
    public BluetoothManager(final Context mContext) {
        this.mContext = mContext;
        this.mBlueSPP = new BluetoothSPP(mContext);

        if (isEnableBluetooth()) { startBluetooth(); }
        else { Toast.makeText(mContext, "Current Bluetooth is Disenabled.", Toast.LENGTH_SHORT).show(); }
    }

    /* MARK - : get Enable Bluetooth Method */
    public final boolean isEnableBluetooth() { return mBlueSPP.isBluetoothEnabled(); }

    /* MARK - : start Bluetooth Method */
    private final void startBluetooth() {
        mBlueSPP.setupService();
        mBlueSPP.setDeviceTarget(BluetoothState.DEVICE_OTHER);
        mBlueSPP.startService(BluetoothState.DEVICE_OTHER);

        mBlueSPP.setBluetoothConnectionListener(this);
        mBlueSPP.setBluetoothStateListener(this);
        mBlueSPP.setOnDataReceivedListener(this);

        ((Activity)mContext).startActivityForResult(new Intent(mContext, DeviceList.class), BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    /* MARK - : Bluetooth onActivityResult Listener */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            /* Connect Android to Other Device */
            case (BluetoothState.REQUEST_CONNECT_DEVICE) : {
                if (resultCode == Activity.RESULT_OK) { mBlueSPP.connect(data); }
                break;
            }

            case (BluetoothState.REQUEST_ENABLE_BT) : {
                mBlueSPP.setupService();
                mBlueSPP.startService(BluetoothState.DEVICE_OTHER);
                break;
            }
        }
    }

    /* TODO - : Bluetooth Connection Listener */
    @Override
    public void onDeviceConnected(String name, String address) {
        Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceDisconnected() {

    }

    @Override
    public void onDeviceConnectionFailed() {
    }

    /* TODO - : Bluetooth State Listener */
    @Override
    public void onServiceStateChanged(int state) {

    }

    /* TODO - : Bluetooth OnDataReceived Listener */
    @Override
    public void onDataReceived(byte[] data, String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
