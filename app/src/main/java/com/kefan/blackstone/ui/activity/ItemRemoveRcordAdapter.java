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
import com.kefan.blackstone.common.SpeciesConstant;
import com.kefan.blackstone.database.Note;
import com.kefan.blackstone.database.Record;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by MY SHIP on 2017/5/20.
 */

public class ItemRemoveRcordAdapter extends RecyclerView.Adapter <ItemRemoveRcordAdapter.ViewHolder>{

    private List<Record> recordList;

    private Context context;

    private String TAG="ItemRemoveRcordAdapter";


    public ItemRemoveRcordAdapter(Context context,List<Record>recordList){
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

        Record record=recordList.get(position);

        holder.date.setText(dateForamt(record.getTime()));


//        List<Note> notes = DataSupport.where("record_id=?",String.valueOf(record.getId())).find(Note.class);

        holder.speciesCount.setText(specesCount(record.getNotes()));

        if (record.getAddToObservedList()) {
            holder.uploadState.setText("已上传");
            holder.upload.setVisibility(View.GONE);
        } else {
            holder.uploadState.setText("未上传");
            holder.upload.setVisibility(View.VISIBLE);
        }




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

    private String dateForamt(Long second) {

        Date da=new Date(second);
        SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
        String mdate=format.format(da);
        return mdate.substring(0,4)+"年"+mdate.substring(5,7)+"月"+mdate.substring(8,10)+"日";

    }

    /**
     * 物种种类文字描述转换
     * @param notes
     * @return
     */
    private String specesCount(List<Note> notes) {
        String strs[]={"","","",""};
        int counts[]={0,0,0,0};

        for (Note note : notes) {

            switch (note.getSpeciesType()) {

                case SpeciesConstant.BIRD:
                    counts[0]++;
                    break;

                case SpeciesConstant.INSECT:
                    counts[1]++;
                    break;

                case SpeciesConstant.AMPHIBIA:
                    counts[2]++;
                    break;

                case SpeciesConstant.REPTILES:
                    counts[3]++;
                    break;
            }

        }

        if (counts[0] != 0) {
            strs[0]="鸟类"+counts[0]+"种";
        }

        if (counts[1] != 0) {
            strs[1]="昆虫"+counts[1]+"种";
        }

        if (counts[2] != 0) {
            strs[2]="两栖类"+counts[2]+"种";
        }

        if (counts[3] != 0) {
            strs[3]="爬行类"+counts[3]+"种";
        }

        String str="";

        for (int i = 0; i < counts.length-2; i++) {

            if (counts[i] != 0 && counts[i] != 0) {
                str += strs[i] + "|";
            } else {
                str += strs[i];
            }

            str+=strs[counts.length-1];
        }


        return str;
    }
}
