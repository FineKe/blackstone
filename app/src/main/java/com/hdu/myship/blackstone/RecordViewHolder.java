package com.hdu.myship.blackstone;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by MY SHIP on 2017/5/20.
 */

public class RecordViewHolder extends RecyclerView.ViewHolder {
    public TextView date, amphibia, reptile, bird,insect;
    public TextView delete;
    public LinearLayout recordLayout;


    public RecordViewHolder(View itemView) {
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
