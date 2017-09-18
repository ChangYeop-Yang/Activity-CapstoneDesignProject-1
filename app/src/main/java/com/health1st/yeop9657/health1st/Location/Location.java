package com.health1st.yeop9657.health1st.Location;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    /* POINT - : Location Creator */
    public Location(final Context mContext) {
        this.mContext = mContext;
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

    /* MARK - : Location Listener */
    @Override
    public void onLocationChanged(android.location.Location location) {
        if (mLocationManager != null) {
            dLatitude = location.getLatitude();
            dLongitude = location.getLongitude();
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
