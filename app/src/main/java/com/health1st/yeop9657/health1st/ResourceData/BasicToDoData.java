package com.health1st.yeop9657.health1st.ResourceData;

/**
 * Created by yeop on 2017. 9. 24..
 */

public class BasicToDoData {

    /* POINT - : String */
    private String sNumberDate = null;
    private String sMainTitle = null;
    private String sSummary = null;

    /* TODO - : Creator BasicToDoData */
    public BasicToDoData(final String sMainTitle, final String sSummary, final String sNumberDate)
    { this.sMainTitle = sMainTitle; this.sSummary = sSummary; this.sNumberDate = sNumberDate; }

    /* TODO - : User Custom Method */
    public final String getMainTitle() { return sMainTitle; }
    public final String getSummary() { return sSummary; }
    public final String getNumberDate() { return sNumberDate; }
}
