package com.health1st.yeop9657.health1st.Database;

/**
 * Created by yeop on 2017. 11. 9..
 */

public class HealthAdapter {

    /* MARK - : String */
    private String mDevice = null;

    /* MARK - : Integer */
    private int mHRM = 0;
    private int mSPO2 = 0;

    /* MARK - : Health Adapter Creator */
    public HealthAdapter(final int mHRM, final int mSPO2, final String mDevice) {
        this.mHRM = mHRM;
        this.mSPO2 = mSPO2;
        this.mDevice = mDevice;
    }

    /* MARK - : Getter Method */
    public final int getHeartBeatRate() { return this.mHRM; }
    public final int getSPO2Rate() { return this.mSPO2; }
    public final String getDeviceName() { return this.mDevice; }
}
