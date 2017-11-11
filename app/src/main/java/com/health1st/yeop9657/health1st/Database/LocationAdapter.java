package com.health1st.yeop9657.health1st.Database;

/**
 * Created by 양창엽 on 2017-11-11.
 */

public class LocationAdapter {

    /* MARK - : String */
    private String sDate = null;

    /* MARK - : Double */
    private double dLatitude = 0.0;
    private double dLongitude = 0.0;

    /* MARK - : LocationAdapter Creator */
    public LocationAdapter(final String sDate, final double dLatitude, final double dLongitude) {
        this.sDate = sDate;
        this.dLatitude = dLatitude;
        this.dLongitude = dLongitude;
    }

    /* MARK - : Getter Method */
    public final double getLatitude() { return this.dLatitude; }
    public final double getdLongitude() { return this.dLongitude; }
    public final String getDate() { return this.sDate; }
}
