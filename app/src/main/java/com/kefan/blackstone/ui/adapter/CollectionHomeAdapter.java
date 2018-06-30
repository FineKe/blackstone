package com.kefan.blackstone.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kefan.blackstone.R;
import com.kefan.blackstone.model.SpeciesClass;
import com.kefan.blackstone.ui.activity.MyCollectionsTwoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @note:
 * @author: fine
 * @time: 2018/6/16 下午3:13
 */
public class CollectionHomeAdapter extends RecyclerView.Adapter<CollectionHomeAdapter.ViewHolder>{


    private Context context;
    private List<SpeciesClass> list;
    private LayoutInflater layoutInflater;

    public CollectionHomeAdapter(Context context, List<SpeciesClass> list) {
        this.context = context;
        this.list = list;
        layoutInflater=LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(layoutInflater.inflate(R.layout.item_view_collection_home_fragment,null,false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        SpeciesClass speciesClass=list.get(position);

        holder.name.setText(speciesClass.getName());

        holder.count.setText("共"+speciesClass.getCount()+"种");

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos=position;

                Intent intent = new Intent(context, MyCollectionsTwoActivity.class);
                intent.putExtra("position", pos);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name_item_view_collection_home_fragment)
        TextView name;

        @BindView(R.id.tv_count_item_view_collection_home_fragment)
        TextView count;

        @BindView(R.id.rl_item_view_collection_home_fragment)
        RelativeLayout relativeLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
