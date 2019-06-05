package com.health1st.yeop9657.health1st.ResourceData;

import android.content.Context;
import android.graphics.Color;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;

/**
 * Created by yeop on 2017. 11. 1..
 */

public class GraphAdapter implements OnChartGestureListener, OnChartValueSelectedListener {

    /* MARK - : Context */
    private Context mContext = null;

    /* MARK - : GraphAdapter Creator */
    public GraphAdapter(final Context mContext) {
        this.mContext = mContext;
    }

    /* MARK - : draw Liner Graph Method */
    public final void drawHealthLinerGraph(final LineChart mLineChart, final ArrayList<Float> acHeart, final ArrayList<Float> acSpO2) {

        final ArrayList<Entry> acHeartEntry = new ArrayList<Entry>(BasicData.ALLOCATE_BASIC_VALUE);
        final ArrayList<Entry> acSpO2Entry = new ArrayList<Entry>(BasicData.ALLOCATE_BASIC_VALUE);
        for (int mCount = 0, mSize = acHeart.size(); mCount < mSize; mCount++) { acHeartEntry.add(new Entry(mCount, acHeart.get(mCount))); }
        for (int mCount = 0, mSize = acSpO2.size(); mCount < mSize; mCount++) { acSpO2Entry.add(new Entry(mCount, acSpO2.get(mCount))); }

        /* POINT - : LineDataSet */
        final LineDataSet mHeartRateDataSet = new LineDataSet(acHeartEntry, "Heart-Beat Rate");
        final LineDataSet mSPO2RateDataSet = new LineDataSet(acSpO2Entry, "SpO2 Rate");
        setLineData(mHeartRateDataSet, Color.RED);
        setLineData(mSPO2RateDataSet, Color.BLUE);

        /* POINT - : Basic Setting Graph Commends */
        mLineChart.getDescription().setEnabled(false);
        mLineChart.setDrawGridBackground(false);
        mLineChart.setTouchEnabled(true);
        mLineChart.setDragEnabled(true);
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(false);
        mLineChart.setBackgroundColor(Color.WHITE);

        final ArrayList<ILineDataSet> acLineDataSet = new ArrayList<ILineDataSet>(BasicData.ALLOCATE_BASIC_VALUE);
        acLineDataSet.add(mHeartRateDataSet);
        acLineDataSet.add(mSPO2RateDataSet);

        mLineChart.setData(new LineData(acLineDataSet));
    }

    /* MARK - : Set LineData Method */
    private void setLineData(final LineDataSet mLineDataSet, final int mColor) {
        mLineDataSet.setDrawIcons(false);       mLineDataSet.setDrawFilled(true);   mLineDataSet.setColor(mColor);
        mLineDataSet.setCircleColor(mColor);    mLineDataSet.setLineWidth(1f);      mLineDataSet.setCircleRadius(3f);
        mLineDataSet.setValueTextSize(9f);
    }

    /* TODO - : OnChartGestureListener Method */
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    /* TODO - : OnChartValueSelectedListener */

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
