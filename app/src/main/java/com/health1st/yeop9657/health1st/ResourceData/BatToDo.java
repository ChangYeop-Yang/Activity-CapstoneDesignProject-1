package com.health1st.yeop9657.health1st.ResourceData;

import android.content.Context;
import android.support.constraint.solver.Goal;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import java.util.ArrayList;

public class BatToDo implements BatListener, OnOutsideClickedListener, OnItemClickListener
{
    /* POINT - : ArrayList */
    private ArrayList<BatModel> acBatModelList = null;

    /* POINT - : Context */
    private Context mContext = null;

    /* POINT - : BatToDo */
    private BatRecyclerView mBatRecyclerView = null;
    private BatAdapter mBatAdapter = null;
    private BatItemAnimator mBatAnimator = null;

    public BatToDo(final Context mContext, final BatRecyclerView mBatRecyclerView, final ArrayList<BatModel> acBatModelList)
    {
        this.mContext = mContext;

        /* POINT - : Init BatToDo */
        this.mBatRecyclerView = mBatRecyclerView;
        mBatAnimator = new BatItemAnimator();

        mBatRecyclerView.getView().setLayoutManager(new LinearLayoutManager(mContext));
        mBatRecyclerView.getView().setAdapter(mBatAdapter = new BatAdapter(this.acBatModelList = acBatModelList, this, mBatAnimator).setOnItemClickListener(this).setOnOutsideClickListener(this));

        final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        mItemTouchHelper.attachToRecyclerView(mBatRecyclerView.getView());
        mBatRecyclerView.getView().setItemAnimator(mBatAnimator);
        mBatRecyclerView.setAddItemListener(this);
    }

    /* TODO - : User Custom Method */
    public ArrayList<BatModel> getBatArrayList() { return this.acBatModelList; }

    /* TODO - : BatListener */
    @Override
    public void add(String string) {
        acBatModelList.add(0, new BatGoal(string));
        mBatAdapter.notify(AnimationType.ADD, 0);
    }

    @Override
    public void delete(int position) {
        acBatModelList.remove(position);
        mBatAdapter.notify(AnimationType.REMOVE, position);
    }

    @Override
    public void move(int from, int to) {

        if (from >= 0 && to >= 0) {
            mBatAnimator.setPosition(to);
            final BatModel mBatModel = acBatModelList.get(from);
            acBatModelList.remove(mBatModel);
            acBatModelList.add(to, mBatModel);
            mBatAdapter.notify(AnimationType.MOVE, from, to);

            if (from == 0 || to == 0) { mBatRecyclerView.getView().scrollToPosition(Math.min(from, to)); }
        }
    }

    /* TODO - : OnOutsideClickedListener */
    @Override public void onOutsideClicked() { mBatRecyclerView.revertAnimation(); }

    /* TODO - : OnItemClickListener */
    @Override public void onClick(BatModel batModel, int i) { Toast.makeText(mContext, String.format("ToDo: %s", batModel.getText()), Toast.LENGTH_SHORT).show(); }
}