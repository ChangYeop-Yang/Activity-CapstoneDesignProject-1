package com.health1st.yeop9657.health1st.Database;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 양창엽 on 2017-11-30.
 */

public class HealthRealmAdapter extends RealmObject {

    /* MARK - : String */
    private String  mDevice = null;

    /* MARK - : Integer */
    private int     mHRM = 0;
    private int     mSPO2 = 0;

    /* MARK - : Date */
    private Date    mDate = null;

    /* TODO - : Set/Get HealthRealmAdapter Method */
    public void setHRM(final int mHRM) { this.mHRM = mHRM; }
    public void setSPO2(final int mSPO2) { this.mSPO2 = mSPO2; }
    public void setDate(final Date mDate) { this.mDate = mDate; }
    public void setDeviceName(final String mDevice) { this.mDevice = mDevice; }

    public int getHRM() { return this.mHRM; }
    public int getSPO2() { return this.mSPO2; }
    public Date getDate() { return this.mDate; }
    public String getDeviceName() { return this.mDevice; }
}
