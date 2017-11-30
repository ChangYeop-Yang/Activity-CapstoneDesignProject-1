package com.health1st.yeop9657.health1st.Database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by 양창엽 on 2017-11-30.
 */

public class TodoRealmAdapter extends RealmObject {

    /* POINT - : String */
    private String sNumberDate = null;
    private String sMainTitle = null;
    private String sSummary = null;

    /* TODO - : Set/Get TodoRealmAdapter Method */
    public void setDate(final String sDate) { this.sNumberDate = sDate; }
    public void setMainTitle(final String sMainTitle) { this.sMainTitle = sMainTitle; }
    public void setSummary(final String sSummary) { this.sSummary = sSummary; }

    public String getDate() { return this.sNumberDate; }
    public String getSummary() { return this.sSummary; }
    public String getMainTitle() { return this.sMainTitle; }
}
