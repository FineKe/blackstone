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
        if(recordList.size()!=0)
        {
            Log.d(TAG, "onBindViewHolder: "+recordList.size());
            Log.d(TAG, "onBindViewHolder: "+recordList.get(position).getId());
            Log.d(TAG, "onBindViewHolder: "+recordList.get(position).getUserId());
            Log.d(TAG, "onBindViewHolder: "+recordList.get(position).getTime());
            Log.d(TAG, "onBindViewHolder: "+recordList.get(position).getNoteCountses().get(0).getSpeciesType());
            ObserveRecordFragment.Record record=recordList.get(position);
            Date da=new Date(record.getTime());
            SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
            String mdate=format.format(da);
            Log.d(TAG, "onBindViewHolder:"+da.toString() );
            holder.date.setText(mdate.substring(0,4)+"年"+mdate.substring(5,7)+"月"+mdate.substring(8,10)+"日");
            for(ObserveRecordFragment.NoteCounts noteCounts:record.getNoteCountses())
            {
                switch (noteCounts.getSpeciesType())
                {
                    case "amphibia":holder.amphibia.setText("两栖类"+noteCounts.getCount()+"种");
                        holder.amphibia.setVisibility(View.VISIBLE);
                        break;
                    case "bird":holder.bird.setText("鸟类"+noteCounts.getCount()+"种");
                        holder.bird.setVisibility(View.VISIBLE);
                        break;
                    case "reptiles":holder.reptile.setText("爬行类"+noteCounts.getCount()+"种");
                        holder.reptile.setVisibility(View.VISIBLE);
                        break;
                    case "insect":holder.insect.setText("昆虫"+noteCounts.getCount()+"个目");
                        holder.insect.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    @Override
    public int getItemCount(){
        return recordList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date, amphibia, reptile, bird,insect,delete;
        public LinearLayout recordLayout;
        public ViewHolder(View itemView){
            super(itemView);
            date= (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_date);
            amphibia = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_amphibia);
            reptile = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_reptiles);
            bird = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_bird);
            insect = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_insect);
            delete = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_record_delete);
            recordLayout = (LinearLayout) itemView.findViewById(R.id.activity_my_records_list_view_item_linear_layout_record);
        }
    }

    public void removeItem(int position) {
    }
}
