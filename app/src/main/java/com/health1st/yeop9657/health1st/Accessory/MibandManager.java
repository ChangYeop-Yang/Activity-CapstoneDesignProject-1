package com.health1st.yeop9657.health1st.Accessory;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.health1st.yeop9657.health1st.Database.HealthDatabase;
import com.health1st.yeop9657.health1st.R;
import com.health1st.yeop9657.health1st.ResourceData.BasicData;

import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by yeop on 2017. 11. 7..
 */

public class MibandManager extends BluetoothManager implements View.OnClickListener {

    /* MARK - : String */
    private final static String TAG = MibandManager.class.getSimpleName();
    private final static String MI_NAME = "MI Band 2";

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : Activity */
    private Activity mActivity = null;

    /* MARK - : View */
    private CardView mCardView = null;

    /* MARK - : Bluetooth Instance */
    private BluetoothGatt mDeviceBluetoothGatt = null;

    /* MARK - : Button */
    private Button aButtonList[] = null;

    /* MARK - : Boolean */
    private Boolean isVibrate = false;
    private Boolean isListeningHeartRate = false;

    /* MARK - : SweetAlertDialog */
    private SweetAlertDialog mSweetAlertDialog = null;

    /* MARK - : Mi-Band Manager Creator */
    public MibandManager(final Context mContext, final Activity mActivity, final CardView mCardView) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.mCardView = mCardView;
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

        /* POINT - : Setting Button Listener */
        aButtonList = new Button[]{(Button) mActivity.findViewById(R.id.Device_Heart_But), (Button) mActivity.findViewById(R.id.Device_Battery_But), (Button) mActivity.findViewById(R.id.Device_Find_But), (Button)mActivity.findViewById(R.id.Device_SPO2_But)};
        for (final Button mButton : aButtonList) { mButton.setOnClickListener(this); }
    }

    /* MARK - : Start Heart Beat Rate Method */
    public void startScanHeartRate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(Xiaomi.HEART_SERVICE)
                .getCharacteristic(Xiaomi.HEART_CONTROL_CHARACTERISTIC);
        bchar.setValue(new byte[]{21, 2, 1});

        mDeviceBluetoothGatt.writeCharacteristic(bchar);
    }

    /* MARK - : Set Listen Heart Rate Method */
    public void listenHeartRate() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(Xiaomi.HEART_SERVICE)
                .getCharacteristic(Xiaomi.HEART_MEASUREMENT_CHARACTERISTIC);

        mDeviceBluetoothGatt.setCharacteristicNotification(bchar, true);

        final BluetoothGattDescriptor descriptor = bchar.getDescriptor(Xiaomi.HEART_DESCRIPTOR);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        mDeviceBluetoothGatt.writeDescriptor(descriptor);

        isListeningHeartRate = true;
    }

    /* MARK - : Get Mi-Band Battery Method */
    public void getBatteryStatus() {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(Xiaomi.BASIC_SERVICE)
                .getCharacteristic(Xiaomi.BASIC_BATTERY_CHARACTERISTIC);

        if (!mDeviceBluetoothGatt.readCharacteristic(bchar)) {
            Toast.makeText(mContext, "Failed get battery info", Toast.LENGTH_SHORT).show();
        }
    }

    /* MARK - : Start Mi-Band Vibrate Method */
    public Boolean startBandVibrate(final Button mButton) {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(Xiaomi.ALERT_NOTIFICATION_SERVICE)
                .getCharacteristic(Xiaomi.ALERT_NOTIFICATION_CHARACTERISTIC);

        bchar.setValue(new byte[]{2});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar))
        { Toast.makeText(mContext, "Failed start vibrate", Toast.LENGTH_SHORT).show(); return false; }

        mButton.setText(R.string.Main_Device_Finding);
        return true;
    }

    /* MARK - : Stop Mi-Band Vibrate Method */
    public Boolean stopBandVibrate(final Button mButton) {

        final BluetoothGattCharacteristic bchar = mDeviceBluetoothGatt.getService(Xiaomi.ALERT_NOTIFICATION_SERVICE)
                .getCharacteristic(Xiaomi.ALERT_NOTIFICATION_CHARACTERISTIC);

        bchar.setValue(new byte[]{0});

        if (!mDeviceBluetoothGatt.writeCharacteristic(bchar))
        { Toast.makeText(mContext, "Failed stop vibrate", Toast.LENGTH_SHORT).show(); return true; }

        mButton.setText(R.string.Main_Device_Find);
        return false;
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
                    Snackbar.make(mActivity.findViewById(android.R.id.content), "Connect Xiaomi Mi-Band.", Snackbar.LENGTH_SHORT).show();
                    break;
                }
                case (BluetoothProfile.STATE_DISCONNECTED) :
                {
                    mDeviceBluetoothGatt.disconnect();
                    Snackbar.make(mActivity.findViewById(android.R.id.content), "Disconnect Xiaomi Mi-Band.", Snackbar.LENGTH_SHORT).show();
                    break;
                }
            }
        }

        @Override
        public void onServicesDiscovered(final BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Log.v(TAG, "onServicesDiscovered");

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCardView.setVisibility(View.VISIBLE);
                    Snackbar.make(mActivity.findViewById(android.R.id.content), "The Xiaomi Mi-Band Service is Enabled.", Snackbar.LENGTH_SHORT).show();
                }
            });

            listenHeartRate();
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);

            /* Using Battery Percent */
            final String mUUID = characteristic.getUuid().toString();
            if (mUUID.equals(Xiaomi.BASIC_BATTERY_CHARACTERISTIC.toString())) {
                final int mBatteryRate = (int)characteristic.getValue()[1];
                setButtonText(aButtonList[1], String.format("남은 배터리 : %d %%", mBatteryRate));

                if (mSweetAlertDialog != null) { mSweetAlertDialog.cancel(); }
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);

            /* Heart Beat Rate */
            final String mUUID = characteristic.getUuid().toString();
            if (mUUID.equals(Xiaomi.HEART_MEASUREMENT_CHARACTERISTIC.toString())) {

                final int mHeartBeatRate = convertByteToInt(characteristic.getValue());
                setButtonText(aButtonList[0], String.format("%d BPM", mHeartBeatRate));

                /* POINT - : Send Health Data Message */
                sendHealthData(mContext, mHeartBeatRate, 0);

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        /* POINT - : Heart Beat Rate */
                        final TextView mStateText = (TextView)mActivity.findViewById(R.id.Patient_State_Text);
                        final ImageView mStateImage = (ImageView)mActivity.findViewById(R.id.Patient_State_Image);
                        if (mHeartBeatRate > 50 && mHeartBeatRate < 80) { { Glide.with(mContext).load(R.drawable.ic_circle_green).into(mStateImage); mStateText.setText("정상"); } }
                        else if (mHeartBeatRate > 100 && mHeartBeatRate < 120) { Glide.with(mContext).load(R.drawable.ic_circle_orange).into(mStateImage); mStateText.setText("위험"); }
                        else if (mHeartBeatRate > 120) { Glide.with(mContext).load(R.drawable.ic_circle_red).into(mStateImage); mStateText.setText("긴급"); }
                    }
                });

                /* POINT - : HealthDatabase */
                final HealthDatabase mHealthDatabase = new HealthDatabase(mContext);
                mHealthDatabase.insertHealthData(mHealthDatabase.getWritableDatabase(), mContext, mHeartBeatRate, 0, gatt.getDevice().getName());

                if (mSweetAlertDialog != null) { mSweetAlertDialog.cancel(); }
            }
        }
    };

    /* TODO - : Setting Button Text Method */
    private void setButtonText(final Button mButton, final String mSting) {

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() { mButton.setText(mSting); }
        });
    }

    /* TODO - : Show SweetAlertDialog Method */
    private void showAlertDialog(final int mAlertType, final int mType, final String mTitle, final String mContent) {

        /* POINT - : SweetAlertDialog */
        mSweetAlertDialog = new SweetAlertDialog(mContext, mAlertType);
        mSweetAlertDialog.setTitleText(mTitle);
        mSweetAlertDialog.setContentText(mContent);

        switch (mType)
        {
            case (BasicData.BLUETOOTH_DEVICE_HEART_BEAT_RATE) : { mSweetAlertDialog.setCancelable(false); break; }
            case (BasicData.BLUETOOTH_DEVICE_BATTERY_RATE) : { mSweetAlertDialog.setCancelable(false); break; }
        }

        mSweetAlertDialog.show();
    }

    /* TODO - : OnClick Listener Method */
    @Override
    public void onClick(View view) {

        /* POINT - : Button */
        final Button mButton = (Button)view;
        startDeviceVibrate(view.getContext());

        switch (view.getId())
        {
            case (R.id.Device_SPO2_But) : { showAlertDialog(SweetAlertDialog.ERROR_TYPE, 0, "Unsupported SPO2", "Xiaomi Mi-Band는 SPO2를 지원하지 않습니다."); break; }
            case (R.id.Device_Find_But) : { isVibrate = isVibrate ? stopBandVibrate(mButton) : startBandVibrate(mButton); break; }
            case (R.id.Device_Battery_But) :
            {
                final String mName = "베터리정보 가져오기";
                final String mContent = "Battery Capacity을 측정하고 있습니다.\n잠시만 기다려주세요.";
                showAlertDialog(SweetAlertDialog.PROGRESS_TYPE, BasicData.BLUETOOTH_DEVICE_BATTERY_RATE, mName, mContent);
                getBatteryStatus(); break;
            }
            case (R.id.Device_Heart_But) :
            {
                /* POINT - : Check Heart Beat Rate */
                if (!isListeningHeartRate)
                { showAlertDialog(SweetAlertDialog.ERROR_TYPE, BasicData.BLUETOOTH_DEVICE_HEART_BEAT_RATE, "ERROR SCAN HRM", "Heart Beat Rate(=HRM)을 측정할 수 없습니다."); break; }

                final String mName = "심장박동수 측정";
                final String mContent = "Heart Beat Rate(=HRM)을 측정하고 있습니다.\n잠시만 기다려주세요.";
                showAlertDialog(SweetAlertDialog.PROGRESS_TYPE, BasicData.BLUETOOTH_DEVICE_HEART_BEAT_RATE, mName, mContent);
                startScanHeartRate(); break;
            }
        }
    }
}
