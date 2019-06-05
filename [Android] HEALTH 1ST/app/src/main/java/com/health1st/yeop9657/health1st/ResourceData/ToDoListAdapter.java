package com.health1st.yeop9657.health1st.ResourceData;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.health1st.yeop9657.health1st.Database.TodoRealmAdapter;
import com.health1st.yeop9657.health1st.R;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by yeop on 2017. 9. 24..
 */

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    /* Context */
    private Context mContext = null;

    /* ArrayList */
    private RealmResults<TodoRealmAdapter> mToDoList = null;

    /* KidsAdapter */
    private ToDoListAdapter mToDoAdapter = this;

    /* TODO - : Creator ToDoAdapter */
    public ToDoListAdapter(final Context mContext, RealmResults<TodoRealmAdapter> mToDoList) {
        this.mContext = mContext;
        this.mToDoList = mToDoList;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        /* POINT - : TextView */
        holder.mNumberDateText.setText(String.format("#%d     %s", position + 1, mToDoList.get(position).getDate()));
        holder.mMainTitleText.setText(mToDoList.get(position).getMainTitle());
        holder.mSummaryText.setText(mToDoList.get(position).getSummary());

        /* POINT - : ImageButton */
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE).setTitleText("ToDo 삭제")
                        .setContentText(String.format("%s가 삭제되었습니다.\n*삭제 된 내용은 복구되지 않습니다.", mToDoList.get(position).getMainTitle()))
                        .setConfirmText("확인").setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) { mToDoList.get(position).deleteFromRealm(); }
                                });
                                mToDoAdapter.notifyItemRemoved(position);
                                mToDoAdapter.notifyDataSetChanged();

                                sweetAlertDialog.cancel();
                            }
                        }).show();
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_todo, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public int getItemCount() { return mToDoList.size(); }

    /* TODO - : ViewHolder class */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /* POINT - : TextView */
        private TextView mNumberDateText = null;
        private TextView mMainTitleText = null;
        private TextView mSummaryText = null;

        /* POINT - : Button */
        private ImageButton mDelete = null;

        private ViewHolder(View itemView) {
            super(itemView);

            /* POINT - : TextView */
            mNumberDateText = (TextView)itemView.findViewById(R.id.Layout_ToDo_Number);
            mMainTitleText = (TextView)itemView.findViewById(R.id.Layout_ToDo_MainTitle);
            mSummaryText = (TextView)itemView.findViewById(R.id.Layout_ToDo_Summary);

            /* POINT - : ImageButton */
            mDelete = (ImageButton)itemView.findViewById(R.id.Layout_ToDo_Delete);
        }
    }
}
