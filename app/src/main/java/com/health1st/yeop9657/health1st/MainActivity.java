package com.health1st.yeop9657.health1st;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.health1st.yeop9657.health1st.Location.Location;
import com.health1st.yeop9657.health1st.Preference.ParentActivity;
import com.health1st.yeop9657.health1st.ResourceData.BasicData;
import com.health1st.yeop9657.health1st.ResourceData.BasicToDoData;
import com.health1st.yeop9657.health1st.ResourceData.ToDoAdapter;

import org.json.JSONException;

import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback
{
    /* HashMap Collection */
    private HashMap<String, TextView> cTextViewMap = new HashMap<String, TextView>();

    /* POINT - : Context */
    private Context mContext = MainActivity.this;

    /* POINT - : ImageView */
    private ImageView mHelperImage = null;

    /* POINT - : Location */
    private Location mLocation = null;

    /* POINT - : ArrayList<LatLng> */
    private ArrayList<LatLng> acLatLngList = null;
    private ArrayList<BasicToDoData> acToDoList = null;

    /* POINT - : RecyclerView */
    private RecyclerView mToDoRecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* MARK - : ToolBar */
        final Toolbar mToolbar = (Toolbar) findViewById(R.id.Main_ToolBar);
        setToolBar(mToolbar, "Health 1st Street", "HOME - Patient Version");

        /* MARK - : Button */
        final Button aButton[] = {(Button) findViewById(R.id.Main_Patient_SOS_Call), (Button) findViewById(R.id.Main_Patient_SOS_MMS), (Button) findViewById(R.id.MainHelperCall)};
        for (final Button mButton : aButton) { mButton.setOnClickListener(this); }

        /* MARK - : TextView */
        cTextViewMap.put("Helper_Name", (TextView) findViewById(R.id.MainHelperName));
        cTextViewMap.put("Helper_Address", (TextView) findViewById(R.id.MainHelperAddress));
        cTextViewMap.put("Helper_Tel", (TextView) findViewById(R.id.MainHelperTel));
        cTextViewMap.put("Patient_State", (TextView) findViewById(R.id.Patient_State_Text));
        cTextViewMap.put("Patient_Information", (TextView) findViewById(R.id.Main_Medical_History));
        cTextViewMap.put("Patient_Location", (TextView) findViewById(R.id.MainMapLocation));
        getSharedText();

        /* MARK - : ImageView */
        mHelperImage = (ImageView) findViewById(R.id.MainHelperImage);
        setImageView(mContext, mHelperImage, BasicData.HELPER_IMAGE);

        /* MARK - : MapFragment */
        SupportMapFragment mMapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.MainGoogleMap);
        mMapFragment.getMapAsync(this);

        /* MARK - : ToDo */
        try { acToDoList = (ArrayList<BasicToDoData>) getArrayListPreference(BasicData.BAT_TODO_KEY); }
        catch (JSONException error) { Log.e("ToDo JSON Error!", error.getMessage()); error.printStackTrace(); }
        catch (GeneralSecurityException e) { Log.e("ToDo Decrypt Error!", e.getMessage()); e.printStackTrace(); }

        mToDoRecyclerView = (RecyclerView)findViewById(R.id.Main_Patient_ToDo_Recycler);
        setRecyclerView(mToDoRecyclerView);

        /* MARK - : ImageButton */
        final ImageButton mAddButton = (ImageButton)findViewById(R.id.MainToDoAdd);
        mAddButton.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getSharedText();
        setImageView(mContext, mHelperImage, BasicData.HELPER_IMAGE);
    }

    /* MARK - : User Custom Method */
    private void getSharedText() {
        final String asHelperHeader[] = {"Helper_Name", "Helper_Address", "Helper_Tel"};

        /* POINT - : Helper Information */
        final String asHelper[] = {mShared.getString("Helper_Name", BasicData.EMPTY_TEXT), mShared.getString("Helper_Address", BasicData.EMPTY_TEXT), mShared.getString("Helper_Tel", BasicData.EMPTY_TEXT)};
        for (int mCount = 0, mSize = asHelper.length; mCount < mSize; mCount++) {
            cTextViewMap.get(asHelperHeader[mCount]).setText(asHelper[mCount]);
        }

        /* POINT - : Patient Information */
        final StringBuffer mStringBuffer = new StringBuffer();
        mStringBuffer.append("* 이름: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Name", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 나이: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Age", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 성별: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Sex", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 흡연여부: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Smoking", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 혈액형: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Blood", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 신장: ");
        mStringBuffer.append(String.format("%scm\n\n", mShared.getString("Patient_Height", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 체중: ");
        mStringBuffer.append(String.format("%skg\n\n", mShared.getString("Patient_Weight", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 질병: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Disease", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 복용중인 약: ");
        mStringBuffer.append(String.format("%s\n\n", mShared.getString("Patient_Medicine", BasicData.EMPTY_TEXT)));
        mStringBuffer.append("* 기타사항: ");
        mStringBuffer.append(String.format("%s\n", mShared.getString("Patient_Etc", BasicData.EMPTY_TEXT)));
        cTextViewMap.get("Patient_Information").setText(mStringBuffer.toString());
    }

    private MarkerOptions setMapMarker(final String mTitle, final String mSubText, final LatLng cLatLng)
    {
        final MarkerOptions mMakerOptions = new MarkerOptions();
        if (mTitle != null) { mMakerOptions.title(mTitle); }
        if (mSubText != null) { mMakerOptions.snippet(mSubText); }
        mMakerOptions.position(cLatLng);

        return mMakerOptions;
    }

    private void setRecyclerView(final RecyclerView mRecyclerView)
    {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(new ToDoAdapter(mContext, acToDoList));
    }

    /* TODO - : One Click Event Listener */
    @Override
    public void onClick(View view) {

        /* POINT : Action Vibrate and Toast */
        mVibrator.vibrate(BasicData.VIBRATE_VALUE);

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

                try {
                    SmsManager.getDefault().sendTextMessage("01064295758", null, String.format("Lat: %f, Long: %f, %s", mLocation.getLatitude(), mLocation.getLongitude(), cTextViewMap.get("Patient_Location").getText()), null, null);
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE).setTitleText("SMS 전송 성공")
                            .setContentText("응급기관으로 SMS 전송을 성공하였습니다.").show();
                }
                catch (Exception error) {
                    Log.e("Send Message Error!", error.getMessage()); error.printStackTrace();
                    new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE).setTitleText("SMS 전송 실패")
                            .setContentText("응급기관으로 SMS 전송을 실패하였습니다.").show();
                } break;
            }

            case R.id.MainToDoAdd : {

                /* POINT - : TextInputEditText */
                final TextInputEditText mToDoEdit = (TextInputEditText)findViewById(R.id.MainToDoEdit);

                if (mToDoEdit.getText().toString().isEmpty()) { new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE).setTitleText("ToDo 생성실패").setContentText("ToDo를 입력해주세요.").show(); }
                else {
                    /* POINT - : Simple Date Format */
                    final SimpleDateFormat mSimple = new SimpleDateFormat("MM-dd hh:mm");

                    /* POINT - : String */
                    final String mDate = mSimple.format(new Date(System.currentTimeMillis()));
                    final String mMainTitle = mToDoEdit.getText().toString();

                    acToDoList.add(new BasicToDoData(mMainTitle, BasicData.TODO_INPUT_PATIENT, mDate));
                    mToDoRecyclerView.setAdapter(new ToDoAdapter(mContext, acToDoList));

                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE).setTitleText("ToDo 생성완료")
                            .setContentText(String.format("%s가 생성되었습니다.", mToDoEdit.getText().toString())).show();
                    mToDoEdit.setText(null);
                } break;
            }
        }
    }

    /* TODO - : MenuItem Event Listener */
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

    /* TODO - : Map Ready Listener */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /* POINT - : Location */
        if (mLocation == null) { mLocation = new Location(mContext, googleMap); }
        mLocation.getGEOAddress(mLocation.getLatitude(), mLocation.getLongitude(), cTextViewMap.get("Patient_Location"));

        /* POINT - : LatLng */
        final LatLng sLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sLatLng, 15));

        /* MARK - : Import ArrayList Preference */
        try { acLatLngList = (ArrayList<LatLng>) getArrayListPreference(BasicData.MARKER_PREFERENCE_KEY); }
        catch (JSONException error) { Log.e("LatLng JSON Error!", error.getMessage()); error.printStackTrace(); }
        catch (GeneralSecurityException e) { Log.e("LatLng Decrypt Error!", e.getMessage()); e.printStackTrace(); }

        for (final LatLng mTempLoc : acLatLngList) { googleMap.addMarker(setMapMarker(null, null, mTempLoc)); }
    }

    /* TODO - : BackPressed Listener */
    @Override
    public void onBackPressed() {

        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("프로그램 종료").setContentText("Health 1st Street 프로그램을 종료하시겠습니까?")
                .setConfirmText("확인").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                /* POINT - : LatLng List */
                final ArrayList<String> acLocationList = new ArrayList<String>(BasicData.ALLOCATE_BASIC_VALUE);
                acLatLngList.add(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()));

                for (final LatLng mLocation : acLatLngList)
                { acLocationList.add(String.format("%f,%f", mLocation.latitude, mLocation.longitude)); }

                try { setArrayListPreference(acLocationList, BasicData.MARKER_PREFERENCE_KEY); }
                catch (GeneralSecurityException e) { Log.e("LatLng Encrypt Error!", e.getMessage()); e.printStackTrace(); }

                final ArrayList<String> acToDo = new ArrayList<String>(BasicData.ALLOCATE_BASIC_VALUE);
                for (final BasicToDoData mToDo : acToDoList) { acToDo.add(String.format("%s,%s,%s", mToDo.getMainTitle(), mToDo.getSummary(), mToDo.getNumberDate())); }

                try { setArrayListPreference(acToDo, BasicData.BAT_TODO_KEY); }
                catch (GeneralSecurityException e) { Log.e("ToDo Encrypt Error!", e.getMessage()); e.printStackTrace(); }

                sweetAlertDialog.cancel(); finish();
            }
        }).setCancelText("취소").show();
    }
}