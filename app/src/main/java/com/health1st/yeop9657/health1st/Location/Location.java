package com.health1st.yeop9657.health1st.Location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by yeop on 2017. 9. 18..
 */

public class Location implements LocationListener
{
    /* POINT - : Context */
    private Context mContext = null;

    /* POINT - : Double */
    private double dLatitude = 0.0;
    private double dLongitude = 0.0;

    /* POINT - : Boolean */
    private Boolean bGPSEnabled = false;
    private Boolean bNetworkEnabled = false;

    /* POINT - : LocationManager */
    private LocationManager mLocationManager = null;

    /* POINT - : Long */
    private final static long MIN_DISTANCE_UPDATE = 10;
    private final static long MIN_TIME_UPDATE = 1000 * 60 * 1;

    /* POINT - : GoogleMap */
    private GoogleMap mGoogle = null;

    /* POINT - : Location Creator */
    public Location(final Context mContext, final GoogleMap mGoogle) {
        this.mContext = mContext;
        this.mGoogle = mGoogle;
        setLocationManager();
    }

    /* MARK - : User Custom Method */
    private void setLocationManager()
    {
        try
        {
            mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
            /* POINT - : Import GPS Information */
            bGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            /* POINT - : Import Network Information */
            bNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            /* POINT - : Check GPS and Network Provider */
            if (!bGPSEnabled && !bNetworkEnabled) { return; }

            /* POINT - : Import Network Provider */
            if (bNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

                if (mLocationManager != null) {
                    dLatitude = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude();
                    dLongitude = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude();
                }
            }

            /* POINT - : Import GPS Provider */
            if (bGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, this);

                if (mLocationManager != null) {
                    dLatitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLatitude();
                    dLongitude = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getLongitude();
                }
            }
        }
        catch (Exception error)
        {
            error.printStackTrace();
            Log.e("Location GPS Error!", error.getMessage());
        }
    }

    public final double getLatitude() { return this.dLatitude; }

    public final double getLongitude() { return this.dLongitude; }

    /* 위도와 경도를 통해서 지오코딩을 하여서 구글에서 주소를 반환하는 함수 */
    public final void getGEOAddress(final double Latitude, final double Longitude, final TextView mTextView) /* Latitude(위도) 와 Longitude(경도)의 인자를 받아서 사용하는 함수 */
    {
        /* Google Geocoder 을 위한 객체 생성 */
        final Geocoder geocoder = new Geocoder(mContext);

        /* 주소 관련 변수 */
        List<Address> list = null;

        try { list = geocoder.getFromLocation(Latitude, Longitude, 1);}
        catch (NumberFormatException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }

          /* 해당 지역의 정보를 받은 뒤 작동이 되는 구문 */
        if( (list != null) && (list.size()>0) ) {
            final Address address = list.get(0);

            String asAddress[] = new String[5];
            asAddress[0] = address.getPostalCode() == null ? null : address.getPostalCode();
            asAddress[1] = address.getLocality() == null ? null : address.getLocality();
            asAddress[2] = address.getSubLocality() == null ? null : address.getSubLocality();
            asAddress[3] = address.getThoroughfare() == null ? null : address.getThoroughfare();
            asAddress[4] = address.getFeatureName() == null ? null : address.getFeatureName();

            final StringBuffer mStringBuffer = new StringBuffer();
            for (final String sString : asAddress)
            { if (sString != null) { mStringBuffer.append(sString).append(" "); }  }

            /* 해당 텍스트 뷰에 출력 */
            mTextView.setText(mStringBuffer.toString());
        }
        /* 해당 주소가 없을 경우 주소를 찾을 수 없는 경고문을 텍스트 뷰에 출력 */
        else { mTextView.setText("Not Found Address."); }
    }

    /* MARK - : Location Listener */
    @Override
    public void onLocationChanged(android.location.Location location) {
        if (mLocationManager != null) {
            dLatitude = location.getLatitude();
            dLongitude = location.getLongitude();
            mGoogle.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(getLatitude(), getLongitude()), 15));
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
