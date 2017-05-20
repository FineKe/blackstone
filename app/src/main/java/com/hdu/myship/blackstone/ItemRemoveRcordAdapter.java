package com.hdu.myship.blackstone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MY SHIP on 2017/5/20.
 */

public class ItemRemoveRcordAdapter extends RecyclerView.Adapter <ItemRemoveRcordAdapter.ViewHolder>{
    private List<MyRecordsActivity.Record> mBirdList;


    public ItemRemoveRcordAdapter(List<MyRecordsActivity.Record>birdList){
        mBirdList = birdList;
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

        MyRecordsActivity.Record record=mBirdList.get(position);
        holder.date.setText(record.getDate());
        holder.amphibia.setText(record.getAmphibia());
        holder.reptile.setText(record.getReptile());
        holder.bird.setText(record.getBird());
        holder.insect.setText(record.getInsect());
    }
    @Override
    public int getItemCount(){
        return mBirdList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date, amphibia, reptile, bird,insect;
        public ViewHolder(View itemView){
            super(itemView);
            date= (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_date);
            amphibia = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_amphibia);
            reptile = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_reptiles);
            bird = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_bird);
            insect = (TextView) itemView.findViewById(R.id.activity_my_records_list_view_item_text_view_insect);

        }
    }
}
