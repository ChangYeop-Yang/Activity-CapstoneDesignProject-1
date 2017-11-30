package com.health1st.yeop9657.health1st.Database;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by 양창엽 on 2017-11-30.
 */

public class LocationRealmAdapter extends RealmObject {

    /* MARK - : Date */
    private Date    mDate = null;

    /* MARK - : Double */
    private double  dLatitude = 0.0;
    private double  dLongitude = 0.0;

    /* MARK - : String */
    private String  sAddress = null;

    /* TODO - : Set/Get LocationRealmAdapter Method */
    public void setDate(final Date mDate) { this.mDate = mDate; }
    public void setAddress(final String sAddress) { this.sAddress = sAddress; }
    public void setLatitude(final double dLatitude) { this.dLatitude = dLatitude; }
    public void setLongitude(final double dLongitude) { this.dLongitude = dLongitude; }

    public Date getDate() { return mDate; }
    public String getAddress() { return sAddress; }
    public double getLatitude() { return dLatitude; }
    public double getLongitude() { return dLongitude; }
}
