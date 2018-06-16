package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kefan.blackstone.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MY SHIP on 2017/5/20.
 */

public class ItemRemoveRcordAdapter extends RecyclerView.Adapter <ItemRemoveRcordAdapter.ViewHolder>{

    private List<ObserveRecordFragment.Record> recordList;

    private Context context;

    private String TAG="ItemRemoveRcordAdapter";


    public ItemRemoveRcordAdapter(Context context,List<ObserveRecordFragment.Record>recordList){
        this.context=context;
        this.recordList = recordList;
    }
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_my_records_list_view_item,parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){

    }
    @Override
    public int getItemCount(){
        return recordList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.ll_record_item_view_oberver_record_fragment)
        public LinearLayout recordLayout;

        @BindView(R.id.tv_date_item_view_oberver_record_fragment)
        TextView date;

        @BindView(R.id.tv_species_count_item_view_oberver_record_fragment)
        TextView speciesCount;

        @BindView(R.id.tv_upload_state_item_view_oberver_record_fragment)
        TextView uploadState;

        @BindView(R.id.tv_upload_item_view_oberver_record_fragment)
        public TextView upload;

        @BindView(R.id.tv_delete_item_view_oberver_record_fragment)
        public TextView delete;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void removeItem(int position) {

    }
}
