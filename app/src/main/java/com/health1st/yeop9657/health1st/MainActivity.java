package com.health1st.yeop9657.health1st;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.health1st.yeop9657.health1st.Preference.ParentActivity;
import com.health1st.yeop9657.health1st.ResourceData.BasicData;

import java.util.HashMap;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    /* HashMap Collection */
    private HashMap<String, TextView> cTextViewMap = new HashMap<String, TextView>();

    /* Context */
    private Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* MARK - : ToolBar */
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.Main_ToolBar);
        setToolBar(mToolbar, "Health 1st Street", "HOME - Patient Version");

        /* MARK - : Button */
        final Button aButton[] = {(Button) findViewById(R.id.Main_Patient_SOS_Call), (Button) findViewById(R.id.Main_Patient_SOS_MMS), (Button) findViewById(R.id.MainHelperCall)};
        for (final Button mButton : aButton) {
            mButton.setOnClickListener(this);
        }

        /* MARK - : TextView */
        cTextViewMap.put("Helper_Name", (TextView) findViewById(R.id.MainHelperName));
        cTextViewMap.put("Helper_Address", (TextView) findViewById(R.id.MainHelperAddress));
        cTextViewMap.put("Helper_Tel", (TextView) findViewById(R.id.MainHelperTel));
        cTextViewMap.put("Patient_State", (TextView) findViewById(R.id.Patient_State_Text));
        cTextViewMap.put("Patient_Information", (TextView) findViewById(R.id.Main_Medical_History));
        cTextViewMap.put("Patient_Location", (TextView) findViewById(R.id.MainMapLocation));
        getSharedText();

        /* MARK - : ImageView */
        final ImageView mHelperImage = (ImageView) findViewById(R.id.MainHelperImage);
        setImageView(mContext, mHelperImage, BasicData.HELPER_IMAGE);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getSharedText();
    }

    /* MARK - : User Custom Method */
    private void getSharedText() {
        final String asHelperHeader[] = {"Helper_Name", "Helper_Address", "Helper_Tel"};
        final String asPatientHeader[] = {"Patient_State", "Patient_Information", "Patient_Location"};

        /* POINT - : Helper Information */
        final String asHelper[] = {mShared.getString("Helper_Name", BasicData.EMPTY_TEXT), mShared.getString("Helper_Address", BasicData.EMPTY_TEXT), mShared.getString("Helper_Tel", BasicData.EMPTY_TEXT)};
        for (int mCount = 0, mSize = asHelper.length; mCount < mSize; mCount++) {
            cTextViewMap.get(asHelperHeader[mCount]).setText(asHelper[mCount]);
        }

        /* POINT - : Patient Information */
        final StringBuffer mStringBuffer = new StringBuffer("\n");
        mStringBuffer.append("※ 혈액형: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Blood", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("※ 신장: ");
        mStringBuffer.append(String.format("%s㎝\n\n", mShared.getString("Patient_Height", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("※ 체중: ");
        mStringBuffer.append(String.format("%s㎏\n\n", mShared.getString("Patient_Weight", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("※ 질병: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Disease", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("※ 복용중인 약: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Medicine", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("※ 기타사항: ");
        mStringBuffer.append(String.format("%s\n", mShared.getString("Patient_Etc", BasicData.EMPTY_TEXT)));
        cTextViewMap.get("Patient_Information").setText(mStringBuffer.toString());
    }

    /* MARK - : One Click Event Listener */
    @Override
    public void onClick(View view) {

        /* #POINT : Action Vibrate and Toast */
        mVibrator.vibrate(BasicData.VIBRATE_VALUE);
        Toast.makeText(mContext, ((Button) view).getText(), Toast.LENGTH_SHORT).show();

        /* View OneClick Action */
        switch (view.getId()) {

            case R.id.MainHelperCall: {
                final String sTel = cTextViewMap.get("Helper_Tel").getText().toString();

                /* Empty Helper Tel Number */
                if (sTel == BasicData.EMPTY_TEXT || sTel == "")
                    { Toast.makeText(mContext, "보호자 전화번호가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show(); break; }

                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s", sTel)))); break;
            }

            case R.id.Main_Patient_SOS_Call: { startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:01064295758"))); break; }

            case R.id.Main_Patient_SOS_MMS: {
                break;
            }
        }
    }

    /* MARK - : MenuItem Event Listener */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        /* POINT - : MenuItem Vibrate and Toast */
        mVibrator.vibrate(BasicData.VIBRATE_VALUE);
        Toast.makeText(mContext, item.getTitle(), Toast.LENGTH_SHORT).show();

        /* POINT - : MenuItem Item Action */
        switch (item.getItemId())
        {
            case R.id.Main_Setting : { startActivity(new Intent(mContext, ParentActivity.class)); return true; }
            case R.id.Main_Sync : { return true; }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    { getMenuInflater().inflate(R.menu.main, menu); return true; }
}