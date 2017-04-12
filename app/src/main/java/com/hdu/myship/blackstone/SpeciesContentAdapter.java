package com.hdu.myship.blackstone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.FileDescriptorBitmapDataLoadProvider;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import ShapeUtil.GlideRoundTransform;
import database.Species;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by MY SHIP on 2017/3/24.
 */

public class SpeciesContentAdapter extends RecyclerView.Adapter<SpeciesContentAdapter.MyHolder> implements View.OnClickListener{
    private String dealPicure="?imageView2/0/w/500/h/500";//本平台的图片采用了cdn分发，为了节省流量，采用七牛云的api处理接口
    private  List<Species>  list=new ArrayList<>();
    private Context context;

    private OnRecyclerViewItemClickeListener onRecyclerViewItemClickeListener=null;
    public SpeciesContentAdapter(List<Species> list, Context context ) {
        this.list=list;
        this.context=context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        MyHolder holder=new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.species_content_recycler_view_item,parent,false));
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.species_content_recycler_view_item,parent,false);
        view.setOnClickListener(this);
        MyHolder holder=new MyHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {

        Species species=list.get(position);//从数据库中读取当前位置的物种信息
        Glide.with(context).load(species.getMainPhoto()+dealPicure).placeholder(R.drawable.loading).transform(new GlideRoundTransform(context,5)).into(holder.imageView);//使用glide框架加载图片到imageview
        holder.tv_latinName.setText(species.getLatinName());
        holder.tv_chineseName.setText(species.getChineseName());
        holder.tv_englishName.setText(species.getFamily());
        holder.itemView.setTag(list.get(position));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_latinName,tv_chineseName,tv_englishName;
        private RoundedImageView imageView;
       // private ImageView imageView;
        public MyHolder(View itemView) {
            super(itemView);
            imageView= (RoundedImageView) itemView.findViewById(R.id.species_picture);
            tv_chineseName= (TextView) itemView.findViewById(R.id.tv_chineseName);
            tv_latinName= (TextView) itemView.findViewById(R.id.tv_latinName);
            tv_englishName= (TextView) itemView.findViewById(R.id.tv_englishName);


        }
    }

    public static interface OnRecyclerViewItemClickeListener
    {
        void onItemClick(View view,Species data);
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickeListener != null) {
            //注意这里使用getTag方法获取数据
            onRecyclerViewItemClickeListener.onItemClick(v,(Species) v.getTag());
        }
    }

    public void setOnRecyclerViewItemClickeListener(OnRecyclerViewItemClickeListener listener)
    {
        this.onRecyclerViewItemClickeListener=listener;
    }
}
