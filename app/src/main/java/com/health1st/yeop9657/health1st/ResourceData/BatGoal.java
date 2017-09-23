package com.health1st.yeop9657.health1st.ResourceData;

import com.yalantis.beamazingtoday.interfaces.BatModel;

public class BatGoal implements BatModel
{
    private String sName = null;
    private boolean isChecked = false;

    /* TODO Creator BatGoal */
    public BatGoal(final String sName) { this.sName = sName; }

    @Override public boolean isChecked() { return this.isChecked; }
    @Override public String getText() { return this.sName; }
    @Override public void setChecked(boolean b) { this.isChecked = b; }
}