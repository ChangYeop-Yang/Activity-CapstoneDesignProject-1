package com.health1st.yeop9657.health1st.Database;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by 양창엽 on 2017-11-30.
 */

public class LocationRangeRealmAdapter extends RealmObject {

    /* MARK - : Integer */
    private int         mRange = 0;

    /* MARK - : Double */
    private double      dLatitude = 0.0;
    private double      dLongitude = 0.0;

    /* MARK - : String */
    private String      sAddress = null;

    /* MARK - : Date */
    private Date        mDate = null;

    /* TODO - : Set/Get LocationRangeRealmAdapter Method */
    public void setDate(final Date mDate) { this.mDate = mDate; }
    public void setRange(final int mRange) { this.mRange = mRange; }
    public void setAddress(final String sAddress) { this.sAddress = sAddress; }
    public void setLatitude(final double dLatitude) { this.dLatitude = dLatitude; }
    public void setLongitude(final double dLongitude) { this.dLongitude = dLongitude; }

    public Date getDate() { return this.mDate; }
    public int getRange() { return this.mRange; }
    public String getAddress() { return this.sAddress; }
    public double getLatitude() { return this.dLatitude; }
    public double getLongitude() { return this.dLongitude; }
}
