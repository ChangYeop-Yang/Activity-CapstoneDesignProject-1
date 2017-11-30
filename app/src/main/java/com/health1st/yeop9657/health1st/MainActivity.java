package com.health1st.yeop9657.health1st;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.health1st.yeop9657.health1st.Accessory.MibandManager;
import com.health1st.yeop9657.health1st.Database.HealthRealmAdapter;
import com.health1st.yeop9657.health1st.Database.LocationRealmAdapter;
import com.health1st.yeop9657.health1st.Database.TodoRealmAdapter;
import com.health1st.yeop9657.health1st.Location.Location;
import com.health1st.yeop9657.health1st.Preference.ParentActivity;
import com.health1st.yeop9657.health1st.ResourceData.BasicData;
import com.health1st.yeop9657.health1st.ResourceData.FHIRAdapter;
import com.health1st.yeop9657.health1st.ResourceData.GraphAdapter;
import com.health1st.yeop9657.health1st.ResourceData.ToDoListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends BaseActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener
{
    /* HashMap Collection */
    private HashMap<String, TextView> cTextViewMap = new HashMap<String, TextView>();

    /* POINT - : Context */
    private Context mContext = MainActivity.this;

    /* POINT - : ImageView */
    private ImageView mHelperImage = null;

    /* POINT - : Location */
    private Location mLocation = null;

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

        /* MARK - : RecyclerView */
        mToDoRecyclerView = (RecyclerView)findViewById(R.id.Main_Patient_ToDo_Recycler);
        setRecyclerView(mToDoRecyclerView);

        /* MARK - : ImageButton */
        final ImageButton mAddButton = (ImageButton)findViewById(R.id.MainToDoAdd);
        mAddButton.setOnClickListener(this);

        /* POINT - : Health Information Graph */
        setGraph((LineChart)findViewById(R.id.Main_Health_Chart));
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getSharedText();
        setImageView(mContext, mHelperImage, BasicData.HELPER_IMAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mRealm.close();
    }

    /* MARK - : Setting Graph Method */
    private void setGraph(final LineChart mLineChart) {

        /* MARK - : ArrayList */
        final ArrayList<Float> mHeartList = new ArrayList<Float>(10);
        final ArrayList<Float> mSpO2List = new ArrayList<Float>(10);

        /* MARK - : HealthDatabase */
        final RealmResults<HealthRealmAdapter> mHealthAdapterList = mRealm.where(HealthRealmAdapter.class).findAllSorted("mDate");

        if (mHealthAdapterList != null && mHealthAdapterList.size() != 0) {

            /* POINT - : Graph Adapter */
            final GraphAdapter mGraphAdapter = new GraphAdapter(mContext);
            for (final HealthRealmAdapter mAdapter : mHealthAdapterList) {
                mHeartList.add((float) mAdapter.getHRM());
                mSpO2List.add((float) mAdapter.getSPO2());
            }

            mGraphAdapter.drawHealthLinerGraph(mLineChart, mHeartList, mSpO2List);
        }
    }

    /* MARK - : User Custom Method */
    private void getSharedText() {
        final String asHelperHeader[] = {"Helper_Name", "Helper_Address", BasicData.SHARED_HELPER_TEL};

        /* POINT - : Helper Information */
        final String asHelper[] = {mShared.getString("Helper_Name", BasicData.EMPTY_TEXT), mShared.getString("Helper_Address", BasicData.EMPTY_TEXT), mShared.getString("Helper_Tel", BasicData.EMPTY_TEXT)};
        for (int mCount = 0, mSize = asHelper.length; mCount < mSize; mCount++) {
            cTextViewMap.get(asHelperHeader[mCount]).setText(asHelper[mCount]);
        }

        /* POINT - : Patient Information */
        final StringBuffer mStringBuffer = new StringBuffer();

        final Pair[] apPatientPairs = {new Pair("* 이름: ", mShared.getString(BasicData.SHARED_PATIENT_NAME, BasicData.EMPTY_TEXT)), new Pair("* 나이: ", mShared.getString(BasicData.SHARED_PATIENT_AGE, BasicData.EMPTY_TEXT)),
                new Pair("* 성별: ", mShared.getString(BasicData.SHARED_PATIENT_SEX, BasicData.EMPTY_TEXT)), new Pair("* 흡연여부: ", mShared.getString(BasicData.SHARED_PATIENT_SMOKE, BasicData.EMPTY_TEXT)),
                new Pair("* 혈액형: ", mShared.getString(BasicData.SHARED_PATIENT_BLOOD, BasicData.EMPTY_TEXT)), new Pair("* 신장: ", mShared.getString(BasicData.SHARED_PATIENT_HEIGHT, BasicData.EMPTY_TEXT)),
                new Pair("* 체중: ", mShared.getString(BasicData.SHARED_PATIENT_WEIGHT, BasicData.EMPTY_TEXT)), new Pair("* 질병: ", mShared.getString(BasicData.SHARED_PATIENT_DISEASE, BasicData.EMPTY_TEXT)),
                new Pair("* 복용중인 약: ", mShared.getString(BasicData.SHARED_PATIENT_MEDICINE, BasicData.EMPTY_TEXT)), new Pair("* 기타사항: ", mShared.getString(BasicData.SHARED_PATIENT_ETC, BasicData.EMPTY_TEXT))};

        for (final Pair<String, String> mPair : apPatientPairs) {
            mStringBuffer.append(mPair.first);
            mStringBuffer.append(String.format("%s\n\n", mPair.second));
        }

        cTextViewMap.get("Patient_Information").setText(mStringBuffer.toString());
    }

    private MarkerOptions setMapMarker(final String mTitle, final String mSubText, final LatLng cLatLng)
    {
        final MarkerOptions mMakerOptions = new MarkerOptions();
        mMakerOptions.title(mTitle);
        mMakerOptions.snippet(mSubText);
        mMakerOptions.position(cLatLng);

        return mMakerOptions;
    }

    private void setRecyclerView(final RecyclerView mRecyclerView)
    {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        final RealmResults<TodoRealmAdapter> mTodoList = mRealm.where(TodoRealmAdapter.class).findAll();
        mRecyclerView.setAdapter(new ToDoListAdapter(mContext, mTodoList));
    }

    /* TODO - : One Click Event Listener */
    @SuppressLint("MissingPermission")
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
                    SmsManager.getDefault().sendTextMessage("01043359311", null, String.format("Lat: %f, Long: %f, %s", mLocation.getLatitude(), mLocation.getLongitude(), cTextViewMap.get("Patient_Location").getText()), null, null);
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

                    /* POINT - : Realm */
                    mRealm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            final TodoRealmAdapter mAdapter = realm.createObject(TodoRealmAdapter.class);
                            mAdapter.setDate( mSimple.format(new Date(System.currentTimeMillis())) );
                            mAdapter.setMainTitle( mToDoEdit.getText().toString() );
                            mAdapter.setSummary(BasicData.TODO_INPUT_PATIENT);
                        }
                    });

                    /* POINT - : SweetAlertDialog */
                    new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE).setTitleText("ToDo 생성완료")
                            .setContentText(String.format("%s가 생성되었습니다.", mToDoEdit.getText().toString())).setConfirmText("확인").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) { setRecyclerView(mToDoRecyclerView); sweetAlertDialog.dismiss(); }
                    }).show();
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
            case R.id.Main_Sync : {

                /* POINT - : Sweet Dialog */
                final SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
                mSweetAlertDialog.setTitleText("Send FHIR Server").setContentText("FHIR 생체 정보를 서버로 전송 중 입니다.\n잠시만 기다려주세요.").setCancelable(false);
                mSweetAlertDialog.show();

                new FHIRAdapter(mContext, mShared, new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), mSweetAlertDialog).execute();
                return true;
            }
            case R.id.Main_Add_Hardware : { new MibandManager(mContext, this, (CardView) findViewById(R.id.Main_Device_CardView)).start(); return true; }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    { getMenuInflater().inflate(R.menu.main, menu); return true; }

    /* TODO - : Map Ready Listener */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        /* POINT - : Location */
        if (mLocation == null) { mLocation = new Location(mContext, googleMap, mShared); }
        mLocation.getGEOAddress(mLocation.getLatitude(), mLocation.getLongitude(), cTextViewMap.get("Patient_Location"));

        /* POINT - : LatLng */
        final LatLng sLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMapLongClickListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sLatLng, 15));

        /* MARK - : Import Location Data */
        final RealmResults<LocationRealmAdapter> mLocationList = mRealm.where(LocationRealmAdapter.class).findAll();
        for (final LocationRealmAdapter mAdapter : mLocationList) {
            googleMap.addMarker(setMapMarker("PATIENT TRACE", mAdapter.getDate().toString(), new LatLng(mAdapter.getLatitude(), mAdapter.getLongitude())));
        }

        /* MARK - : Export Jurisdiction Data */
        final String mString = mShared.getString(BasicData.LOCATION_PATIENT_KEY, null);
        if (mString != null) {
            final String mLatLongStr[] = mString.split(",");
            googleMap.addMarker(setMapMarker("Jurisdiction", new Date().toString(), new LatLng(Double.valueOf(mLatLongStr[0]), Double.valueOf(mLatLongStr[1])))).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        }
    }

    /* TODO - : Map Long Click Listener */
    @Override
    public void onMapLongClick(final LatLng latLng) {

        /* POINT - : Vibrate */
        mVibrator.vibrate(BasicData.VIBRATE_VALUE);

        /* POINT - : SweetAlertDialog */
        final SweetAlertDialog mSweetAlertDialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
        mSweetAlertDialog.setTitleText("Modify Jurisdiction").setContentText(String.format("Lat: %f, Long: %f 위치 좌표로 변경하시겠습니까?", latLng.latitude, latLng.longitude)).setConfirmText("설정");
        mSweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                mSharedWrite.putString(BasicData.LOCATION_PATIENT_KEY, String.format("%f, %f", latLng.latitude, latLng.longitude)).apply();
                sweetAlertDialog.cancel();
            }
        }).setCancelText("취소").show();
    }

    /* TODO - : BackPressed Listener */
    @Override
    public void onBackPressed() {

        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("프로그램 종료").setContentText("Health 1st Street 프로그램을 종료하시겠습니까?")
                .setConfirmText("확인").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {

                /* POINT - : Realm Location Database */
                mRealm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        final LocationRealmAdapter mAdapter = realm.createObject(LocationRealmAdapter.class);
                        mAdapter.setLatitude(mLocation.getLatitude());
                        mAdapter.setLongitude(mLocation.getLongitude());
                        mAdapter.setAddress(cTextViewMap.get("Patient_Location").getText().toString());
                        mAdapter.setDate(new Date());
                    }
                });

                sweetAlertDialog.cancel(); finish();
            }
        }).setCancelText("취소").show();
    }
}