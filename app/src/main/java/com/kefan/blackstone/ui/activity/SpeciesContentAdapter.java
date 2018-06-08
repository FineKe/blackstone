package com.kefan.blackstone.ui.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kefan.blackstone.R;
import com.kefan.blackstone.ShapeUtil.GlideRoundTransform;
import com.kefan.blackstone.database.Species;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class SpeciesContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private String dealPicure = "?imageView2/0/w/400/h/400";//本平台的图片采用了cdn分发，为了节省流量，采用七牛云的api处理接口
    private List<SpeciesClassActivity.Result> resultList;
    private int HEAD = 0;
    private int ITEM = 1;
    private Context context;

    private OnRecyclerViewItemClickeListener onRecyclerViewItemClickeListener = null;

    public SpeciesContentAdapter(Context context, List<SpeciesClassActivity.Result> resultList) {
        this.context = context;
        this.resultList = resultList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == HEAD) {
            return new HeadHolder(LayoutInflater.from(context).inflate(R.layout.species_content_recycler_view_head, viewGroup, false));
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.species_content_recycler_view_item, viewGroup, false);
            view.setOnClickListener(this);
            MyHolder holder = new MyHolder(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof HeadHolder) {
            HeadHolder headHolder = (HeadHolder) viewHolder;
            headHolder.orderOrFamilyChinaeseName.setText(resultList.get(i).getHead());
            headHolder.orderOrFamilyLatinName.setText(resultList.get(i).getLatinHead());
        } else if (viewHolder instanceof MyHolder) {
            MyHolder myHolder = (MyHolder) viewHolder;
            myHolder.tv_englishName.setText(resultList.get(i).getSpecies().getLatinName());
            Glide.with(context).load(resultList.get(i).getSpecies().getMainPhoto() + dealPicure).placeholder(R.mipmap.loading_small).transform(new GlideRoundTransform(context, 8)).into(myHolder.imageView);
            myHolder.tv_chineseName.setText(resultList.get(i).getSpecies().getChineseName());
            myHolder.tv_latinName.setText(resultList.get(i).getSpecies().getEnglishName());
            myHolder.itemView.setTag(resultList.get(i).getSpecies());
        }
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return resultList.get(position).getViewType();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_latinName, tv_chineseName, tv_englishName;
        public RoundedImageView imageView;

        // private ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView = (RoundedImageView) itemView.findViewById(R.id.species_picture);
            tv_chineseName = (TextView) itemView.findViewById(R.id.tv_chineseName);
            tv_latinName = (TextView) itemView.findViewById(R.id.tv_latinName);
            tv_englishName = (TextView) itemView.findViewById(R.id.tv_englishName);
        }
    }

    class HeadHolder extends RecyclerView.ViewHolder {
        private TextView orderOrFamilyChinaeseName;
        private TextView orderOrFamilyLatinName;

        public HeadHolder(View itemView) {
            super(itemView);
            orderOrFamilyChinaeseName = (TextView) itemView.findViewById(R.id.species_content_recyclerView_head_orderOrFamily_chineseName);
            orderOrFamilyLatinName = (TextView) itemView.findViewById(R.id.species_content_recyclerView_head_orderOrFamily_latinName);
        }
    }


    public void setOnRecyclerViewItemClickeListener(OnRecyclerViewItemClickeListener listener) {
        this.onRecyclerViewItemClickeListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickeListener != null) {
            //注意这里使用getTag方法获取数据
            onRecyclerViewItemClickeListener.onItemClick(v, (Species) v.getTag());
        }
    }

    public interface OnRecyclerViewItemClickeListener {
        void onItemClick(View view, Species data);
    }
}
