package com.health1st.yeop9657.health1st;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FirstActivity extends BaseActivity implements PermissionListener
{
    /* POINT - : Integer */
    private int mLoopCount = 0;

    /* POINT - : TimerTask */
    private TimerTask mWaitTask = null;
    private TimerTask mLoadTask = null;
    private Timer mTotalTimer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        /* TODO - : ImageView and Integer */
        final int amImagePath[] = {R.drawable.bg_indego_1, R.drawable.bg_indego_2, R.drawable.bg_indego_3};
        final ImageView mBgImage = (ImageView)findViewById(R.id.First_Information_ImageView);
        mBgImage.setScaleType(ImageView.ScaleType.FIT_XY);
        mBgImage.setImageResource(amImagePath[((int)(Math.random() * 3))]);

        /* TODO - : Rooting Check */
        try {

            Runtime.getRuntime().exec("su");

            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("루팅 단말기 접근제한").setContentText("정보보안을 위해서 비정상적 루팅 단말기는 접근을 제한합니다.")
                    .setConfirmText("확인").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.cancel(); finish();
                }
            }).show();
        }
        catch (Exception mSuccess) {
            Log.i("Check Not Rooting", mSuccess.getMessage());

            /* TODO - : Check Permission */
            TedPermission.with(this).setPermissionListener(this).setDeniedMessage(R.string.Basic_Permission_Information)
                    .setPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE).check();
        }
    }

    @Override
    protected void onDestroy() {
        mTotalTimer.cancel();
        super.onDestroy();
    }

    /* MARK : - Permission Listener */
    @Override
    public void onPermissionGranted() {

        /* TODO - : String and TextView */
        final String asInformation[] = {"루팅 확인중", "권한 확인중", "복호화중"};
        final TextView mInforText = (TextView)findViewById(R.id.First_Information_TextView);

            /* TODO - : Background Wait Timer Task */
            mWaitTask = new TimerTask() {
                @Override
                public void run() { finish(); startActivity(new Intent(getApplicationContext(), MainActivity.class)); }
            };

            /* TODO - : Background Load Timer Task */
            mLoadTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable()
                    { @Override public void run() { if (mLoopCount < 3) { mInforText.setText(asInformation[mLoopCount++]); } } });
                }
            };

            /* TODO - : Create Timer */
            mTotalTimer = new Timer();
            mTotalTimer.schedule(mWaitTask, 3500);
            mTotalTimer.schedule(mLoadTask, 0, 1000);
    }

    @Override
    public void onPermissionDenied(ArrayList<String> arrayList) {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("APP 권한 설정 거부").setContentText("해당 APP을 구동하기 위해서는 시스템 권한이 필요합니다.\n설정을 통해서 권한을 허용해주세요.")
                .setConfirmText("확인").show();
    }
}